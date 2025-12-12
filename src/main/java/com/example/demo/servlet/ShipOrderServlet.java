package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ShipOrderServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 检查用户是否已登录
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User user = (User) request.getSession().getAttribute("user");
        
        try {
            Long orderId = Long.parseLong(request.getParameter("orderId"));
            
            // 验证用户是否是此订单的卖家
            boolean isSeller = orderService.validateOrderOwnership(orderId, user.getId());
            if (!isSeller) {
                request.setAttribute("error", "您没有权限操作此订单");
                request.getRequestDispatcher("/seller-orders").forward(request, response);
                return;
            }
            
            // 标记订单为已发货
            boolean success = orderService.markAsShipped(orderId);
            
            if (success) {
                request.setAttribute("message", "订单已发货");
            } else {
                request.setAttribute("error", "发货失败，请检查订单状态");
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的订单ID");
        } catch (Exception e) {
            request.setAttribute("error", "发货时发生错误：" + e.getMessage());
        }
        
        // 重定向到卖家订单页面
        response.sendRedirect(request.getContextPath() + "/seller-orders");
    }
}
