package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NamingException;
import java.io.IOException;

public class ConfirmDeliveryServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            orderService = ServiceFactory.getInstance().getOrderService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理确认收货请求
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect("orders");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdParam);
            
            // 确认收货
            boolean success = orderService.confirmDelivery(orderId);
            
            if (success) {
                // 重定向到订单页面并显示成功消息
                response.sendRedirect("orders?message=delivery_confirmed");
            } else {
                // 重定向到订单页面并显示错误消息
                response.sendRedirect("orders?error=delivery_failed");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("orders");
        } catch (Exception e) {
            throw new ServletException("Error while confirming delivery", e);
        }
    }
}
