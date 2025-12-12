package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class SellerOrderServlet extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        try {
            orderService = ServiceFactory.getInstance().getOrderService();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize services", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 检查用户是否已登录
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        
        try {
            // 获取卖家订单
            List<Order> orders = orderService.getSellerOrders(user.getId());
            request.setAttribute("orders", orders);
            request.setAttribute("isSellerView", true);
            
            // 转发到订单页面，使用不同的视图模式
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "获取订单时发生错误：" + e.getMessage());
            request.getRequestDispatcher("/orders.jsp").forward(request, response);
        }
    }
}
