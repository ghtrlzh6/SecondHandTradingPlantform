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
     * 获取用户的所有订单
     * @param buyerId 买家ID
     * @return 订单列表
     */
    public List<Order> getUserOrders(Long buyerId) {
        return orderDao.findByBuyerId(buyerId);
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

        // 执行支付
        if (walletService.pay(buyerId, bookPrice)) {
            // 更新订单状态为已支付
            Order order = getOrder(orderId);
            if (order != null) {
                order.setStatus(Order.STATUS_PAID);
                orderDao.update(order);
                
                // 将款项暂时保存在平台账户中，等待确认收货后再转给卖家
                return true;
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