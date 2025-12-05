package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userService = ServiceFactory.getInstance().getUserService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 显示注册页面
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理注册请求
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        try {
            boolean success = userService.register(username, password, email);
            if (success) {
                // 注册成功，重定向到登录页面
                response.sendRedirect("login.jsp");
            } else {
                // 用户名已存在
                request.setAttribute("errorMessage", "用户名已存在");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error during registration", e);
        }
    }
}