package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.UserStats;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class UserProfileServlet extends HttpServlet {
    private com.example.demo.service.UserStatsService userStatsService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userStatsService = ServiceFactory.getInstance().getUserStatsService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查用户是否已登录
        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect("welcome.jsp");
            return;
        }

        Long userId = (Long) request.getSession().getAttribute("userId");
        
        try {
            Optional<UserStats> userStatsOpt = userStatsService.getUserStats(userId);
            if (userStatsOpt.isPresent()) {
                UserStats userStats = userStatsOpt.get();
                request.setAttribute("userStats", userStats);
                request.getRequestDispatcher("/user-profile.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMessage", "无法获取用户信息");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching user stats", e);
        }
    }
}
