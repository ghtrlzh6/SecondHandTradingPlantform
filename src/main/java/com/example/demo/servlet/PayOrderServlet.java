package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 支付订单Servlet
 */
public class PayOrderServlet extends HttpServlet {
    
    private OrderService orderService;
    private BookService bookService;
    
    @Override
    public void init() throws ServletException {
        try {
            orderService = ServiceFactory.getInstance().getOrderService();
            bookService = ServiceFactory.getInstance().getBookService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize services", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 获取当前用户
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null) {
                response.sendRedirect("login");
                return;
            }
            
            // 获取订单ID
            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
                request.getSession().setAttribute("error", "订单ID不能为空");
                response.sendRedirect("orders");
                return;
            }
            
            Long orderId;
            try {
                orderId = Long.parseLong(orderIdStr.trim());
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "无效的订单ID");
                response.sendRedirect("orders");
                return;
            }
            
            // 获取订单信息以获取书籍价格和卖家ID
            Order order = orderService.getOrder(orderId);
            if (order == null) {
                request.getSession().setAttribute("error", "订单不存在");
                response.sendRedirect("orders");
                return;
            }
            
            // 获取书籍信息
            var book = bookService.getBookById(order.getBookId());
            if (book == null) {
                request.getSession().setAttribute("error", "书籍不存在");
                response.sendRedirect("orders");
                return;
            }
            
            // 调用服务层处理支付
            boolean success = orderService.payForOrder(orderId, book.getPrice(), currentUser.getId(), book.getSellerId());
            
            if (success) {
                request.getSession().setAttribute("message", "订单支付成功！");
            } else {
                request.getSession().setAttribute("error", "支付失败：订单不存在、不属于当前用户或状态不正确");
            }
            
            response.sendRedirect("orders");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "支付过程中发生错误：" + e.getMessage());
            response.sendRedirect("orders");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 支付只接受POST请求
        response.sendRedirect("orders");
    }
}
