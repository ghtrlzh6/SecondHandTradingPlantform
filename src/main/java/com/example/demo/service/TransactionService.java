package com.example.demo.service;

import com.example.demo.dao.OrderDao;
import com.example.demo.model.Book;
import com.example.demo.model.Order;
import com.example.demo.model.Wallet;

import javax.sql.DataSource;
import java.math.BigDecimal;

/**
 * 交易服务类，处理订单确认收货后的资金转移
 */
public class TransactionService {
    private OrderService orderService;
    private WalletService walletService;
    private BookService bookService;

    public TransactionService(DataSource dataSource, WalletService walletService, BookService bookService) {
        this.orderService = new OrderService(dataSource, walletService);
        this.walletService = walletService;
        this.bookService = bookService;
    }

    /**
     * 买家确认收货
     * @param orderId 订单ID
     * @return 是否确认成功
     */
    public boolean confirmDelivery(Long orderId) {
        // 获取订单信息
        Order order = orderService.getOrder(orderId);
        if (order == null) {
            return false;
        }

        // 检查订单状态是否为已发货
        if (!Order.STATUS_SHIPPED.equals(order.getStatus())) {
            return false;
        }

        // 获取商品信息以获取价格
        Book book = null;
        try {
            book = bookService.getBookById(order.getBookId());
        } catch (Exception e) {
            // 处理可能的SQL异常
            return false;
        }
        if (book == null) {
            return false;
        }

        // 转移资金从平台账户到卖家钱包
        Wallet sellerWallet = walletService.receive(book.getSellerId(), book.getPrice());
        
        // 更新订单状态为已完成
        order.setStatus(Order.STATUS_COMPLETED);
        orderService.updateOrder(order);
        
        return sellerWallet != null;
    }

    /**
     * 获取订单服务实例
     * @return 订单服务实例
     */
    public OrderService getOrderService() {
        return orderService;
    }
}