package com.example.demo.config;

import com.example.demo.dao.BookDao;
import com.example.demo.dao.UserDao;
import com.example.demo.dao.impl.BookDaoImpl;
import com.example.demo.dao.impl.UserDaoImpl;
import com.example.demo.service.BookService;
import com.example.demo.service.MessageService;
import com.example.demo.service.OrderService;
import com.example.demo.service.UserService;
import com.example.demo.service.WalletService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ServiceFactory {
    private static ServiceFactory instance;
    private final UserService userService;
    private final BookService bookService;
    private final WalletService walletService;
    private final MessageService messageService;
    private final OrderService orderService;

    private ServiceFactory() throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource dataSource = (DataSource) envContext.lookup("jdbc/BookSwapDB");

        UserDao userDao = new UserDaoImpl(dataSource);
        BookDao bookDao = new BookDaoImpl(dataSource);

        this.userService = new UserService(userDao);
        this.bookService = new BookService(bookDao);
        this.walletService = new WalletService(dataSource);
        this.messageService = new MessageService(dataSource);
        this.orderService = new OrderService(dataSource, walletService);
    }

    public static synchronized ServiceFactory getInstance() throws NamingException {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }

    public BookService getBookService() {
        return bookService;
    }

    public WalletService getWalletService() {
        return walletService;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public OrderService getOrderService() {
        return orderService;
    }
}