package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
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
        // 显示登录页面
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理登录请求
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = userService.login(username, password);
            if (user != null) {
                // 登录成功，将用户信息存储到session中
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getId());
                
                // 重定向到主页
                response.sendRedirect("index.jsp");
            } else {
                // 登录失败，返回错误信息
                request.setAttribute("errorMessage", "用户名或密码错误");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error during login", e);
        }
    }
}