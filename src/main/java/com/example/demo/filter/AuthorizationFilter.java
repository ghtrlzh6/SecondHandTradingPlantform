package com.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        
        // 静态资源和公开页面不需要检查登录状态
        if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".png") || 
            uri.endsWith(".jpg") || uri.endsWith(".jpeg") || uri.endsWith(".gif") ||
            uri.equals(contextPath + "/") || uri.equals(contextPath + "/index.jsp") ||
            uri.startsWith(contextPath + "/login") || 
            uri.startsWith(contextPath + "/register")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 检查用户是否已经登录
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // 用户未登录，重定向到登录页面
            resp.sendRedirect(contextPath + "/login");
            return;
        }
        
        // 用户已登录，继续处理请求
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码（如果需要）
    }

    @Override
    public void destroy() {
        // 清理代码（如果需要）
    }
}