package com.example.demo.service;

import com.example.demo.dao.OrderDao;
import com.example.demo.dao.impl.OrderDaoImpl;
import com.example.demo.model.Book;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.model.Wallet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private OrderDao orderDao;
    private WalletService walletService;

    public OrderService(DataSource dataSource, WalletService walletService) {
        this.orderDao = new OrderDaoImpl(dataSource);
        this.walletService = walletService;
    }

    /**
     * 创建订单
     * @param bookId 商品ID
     * @param buyerId 买家ID
     * @param shippingAddress 收货地址
     * @param paymentPassword 支付密码
     * @return 创建的订单
     */
    public Order createOrder(Long bookId, Long buyerId, String shippingAddress, String paymentPassword) {
        Order order = new Order(bookId, buyerId, shippingAddress, paymentPassword);
        return orderDao.create(order);
    }

    /**
     * 获取订单详情
     * @param orderId 订单ID
     * @return 订单对象
     */
    public Order getOrder(Long orderId) {
        Optional<Order> orderOptional = orderDao.findById(orderId);
        return orderOptional.orElse(null);
    }

    /**
     * 获取用户作为买家的所有订单
     * @param buyerId 买家ID
     * @return 订单列表
     */
    public List<Order> getUserOrders(Long buyerId) {
        return orderDao.findByBuyerId(buyerId);
    }

    /**
     * 获取用户作为卖家的所有订单
     * @param sellerId 卖家ID
     * @return 订单列表
     */
    public List<Order> getSellerOrders(Long sellerId) {
        return orderDao.findBySellerId(sellerId);
    }

    /**
     * 验证用户是否是订单的卖家
     * @param orderId 订单ID
     * @param userId 用户ID
     * @return 是否是卖家
     */
    public boolean validateOrderOwnership(Long orderId, Long userId) {
        try {
            String sql = "SELECT b.seller_id FROM books b JOIN orders o ON b.id = o.book_id WHERE o.id = ?";
            try (java.sql.Connection conn = orderDao.getDataSource().getConnection();
                 java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                
                stmt.setLong(1, orderId);
                try (java.sql.ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Long sellerId = rs.getLong("seller_id");
                        return sellerId.equals(userId);
                    }
                }
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("验证订单所有权时发生错误", e);
        }
        return false;
    }

    /**
     * 支付订单
     * @param orderId 订单ID
     * @param bookPrice 商品价格
     * @param buyerId 买家ID
     * @param sellerId 卖家ID
     * @return 支付是否成功
     */
    public boolean payForOrder(Long orderId, BigDecimal bookPrice, Long buyerId, Long sellerId) {
        // 检查买家是否有足够的余额
        if (!walletService.hasSufficientBalance(buyerId, bookPrice)) {
            return false;
        }

        // 执行支付 - 暂存款项，不直接转给卖家
        if (walletService.pay(buyerId, bookPrice)) {
            // 更新订单状态为已支付
            Order order = getOrder(orderId);
            if (order != null) {
                order.setStatus(Order.STATUS_PAID);
                orderDao.update(order);
                
                // 更新书籍状态为已售出
                try {
                    // 获取书籍ID
                    String getBookIdSql = "SELECT book_id FROM orders WHERE id = ?";
                    Long bookId = null;
                    try (java.sql.Connection conn = orderDao.getDataSource().getConnection();
                         java.sql.PreparedStatement stmt = conn.prepareStatement(getBookIdSql)) {
                            
                            stmt.setLong(1, orderId);
                            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    bookId = rs.getLong("book_id");
                                }
                            }
                        }
                    
                    if (bookId != null) {
                        String sql = "UPDATE books SET status = 'sold' WHERE id = ? AND seller_id = ?";
                        try (java.sql.Connection conn = orderDao.getDataSource().getConnection();
                             java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                                
                                stmt.setLong(1, bookId);
                                stmt.setLong(2, sellerId);
                                stmt.executeUpdate();
                            }
                    }
                } catch (java.sql.SQLException e) {
                    throw new RuntimeException("更新书籍状态时发生错误", e);
                }
                
                // 注意：这里不再直接将款项转给卖家，等买家确认收货后再转账
                
                return true;
            }
        }
        return false;
    }

    /**
     * 标记订单为已发货
     * @param orderId 订单ID
     * @return 是否标记成功
     */
    public boolean markAsShipped(Long orderId) {
        Order order = getOrder(orderId);
        if (order != null && Order.STATUS_PAID.equals(order.getStatus())) {
            order.setStatus(Order.STATUS_SHIPPED);
            orderDao.update(order);
            return true;
        }
        return false;
    }

    /**
     * 确认收货，将款项转给卖家并标记订单完成
     * @param orderId 订单ID
     * @return 是否确认成功
     */
    public boolean confirmDelivery(Long orderId) {
        Order order = getOrder(orderId);
        if (order != null && Order.STATUS_SHIPPED.equals(order.getStatus())) {
            // 获取书籍信息以获取价格和卖家ID
            try {
                // 这里需要获取BookService，但为了避免循环依赖，我们直接查询数据库
                String sql = "SELECT b.price, b.seller_id FROM books b JOIN orders o ON b.id = o.book_id WHERE o.id = ?";
                try (java.sql.Connection conn = orderDao.getDataSource().getConnection();
                     java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                    
                    stmt.setLong(1, orderId);
                    try (java.sql.ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            BigDecimal bookPrice = rs.getBigDecimal("price");
                            Long sellerId = rs.getLong("seller_id");
                            
                            // 将款项转给卖家
                            walletService.deposit(sellerId, bookPrice);
                            
                            // 更新订单状态为已完成
                            order.setStatus(Order.STATUS_COMPLETED);
                            orderDao.update(order);
                            
                            return true;
                        }
                    }
                }
            } catch (java.sql.SQLException e) {
                throw new RuntimeException("确认收货时发生错误", e);
            }
        }
        return false;
    }

    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 是否取消成功
     */
    public boolean cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (order != null && Order.STATUS_PENDING.equals(order.getStatus())) {
            order.setStatus(Order.STATUS_CANCELLED);
            orderDao.update(order);
            return true;
        }
        return false;
    }

    /**
     * 获取所有订单
     * @return 所有订单列表
     */
    public List<Order> getAllOrders() {
        return orderDao.findAll();
    }

    /**
     * 更新订单
     * @param order 订单对象
     * @return 更新后的订单
     */
    public Order updateOrder(Order order) {
        return orderDao.update(order);
    }
}
