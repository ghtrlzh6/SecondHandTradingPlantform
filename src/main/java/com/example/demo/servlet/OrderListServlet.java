package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderListServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应编码为UTF-8
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 检查用户是否已登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        // 获取订单类型参数（buy/sell）
        String orderType = request.getParameter("type");
        if (orderType == null || orderType.isEmpty()) {
            orderType = "buy"; // 默认显示购买订单
        }
        
        // 获取当前用户ID
        Long userId = user.getId();
        
        // 根据类型获取订单
        List<Order> orders;
        if ("sell".equals(orderType)) {
            orders = orderService.getSellerOrders(userId);
        } else {
            orders = orderService.getUserOrders(userId);
        }
        
        // 计算各状态订单数量
        Map<String, Integer> statusCounts = calculateStatusCounts(orders);
        
        // 设置request属性
        request.setAttribute("orders", orders);
        request.setAttribute("orderType", orderType);
        request.setAttribute("statusCounts", statusCounts);
        request.setAttribute("isSellerView", "sell".equals(orderType));
        
        // 转发到JSP页面
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
    
    /**
     * 计算各状态订单数量
     */
    private Map<String, Integer> calculateStatusCounts(List<Order> orders) {
        Map<String, Integer> counts = new HashMap<>();
        counts.put("pending", 0);
        counts.put("paid", 0);
        counts.put("shipped", 0);
        counts.put("completed", 0);
        counts.put("cancelled", 0);
        
        for (Order order : orders) {
            String status = order.getStatus();
            if (counts.containsKey(status)) {
                counts.put(status, counts.get(status) + 1);
            }
        }
        
        return counts;
    }
}
