package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.dto.ApiResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 用户API Servlet - 处理用户相关的API请求
 */
public class UserApiServlet extends HttpServlet {
    private UserService userService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userService = ServiceFactory.getInstance().getUserService();
            objectMapper = new ObjectMapper();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    /**
     * POST /api/users?action=register - 用户注册
     * POST /api/users?action=login - 用户登录
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        if ("register".equals(action)) {
            handleRegister(request, response);
        } else if ("login".equals(action)) {
            handleLogin(request, response);
        } else {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "无效的操作类型");
        }
    }

    /**
     * GET /api/users/profile - 获取当前用户信息
     * GET /api/users/{id} - 获取指定用户信息（仅管理员）
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/profile")) {
                // 获取当前用户信息 /api/users/profile
                handleGetCurrentUser(request, response);
            } else {
                // 获取指定用户信息 /api/users/{id}
                handleGetUserById(request, response, pathInfo);
            }
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误");
        }
    }

    /**
     * 处理用户注册
     */
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try {
            // 获取注册参数
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            
            // 验证输入参数
            if (!ValidationUtil.isValidUsername(username)) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "用户名必须是3-20位字母数字下划线");
                return;
            }
            
            if (!ValidationUtil.isValidPassword(password)) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "密码长度至少6位");
                return;
            }
            
            if (!ValidationUtil.isValidEmail(email)) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "邮箱格式不正确");
                return;
            }
            
            // 创建用户
            boolean success = userService.register(username, password, email);
            if (!success) {
                sendError(response, HttpServletResponse.SC_CONFLICT, "注册失败，用户名可能已存在");
                return;
            }
            
            // 返回成功消息
            ApiResponse<Void> apiResponse = ApiResponse.success("注册成功", null);
            objectMapper.writeValue(response.getWriter(), apiResponse);
            
        } catch (SQLException e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据库错误：" + e.getMessage());
        }
    }

    /**
     * 处理用户登录
     */
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            // 验证输入参数
            if (ValidationUtil.isBlank(username) || ValidationUtil.isBlank(password)) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "用户名和密码不能为空");
                return;
            }
            
            // 验证用户登录
            User user = userService.login(username, password);
            if (user == null) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "用户名或密码错误");
                return;
            }
            
            // 设置会话
            request.getSession().setAttribute("user", user);
            
            // 返回用户信息（不包含密码）
            user.setPassword(null);
            ApiResponse<User> apiResponse = ApiResponse.success("登录成功", user);
            objectMapper.writeValue(response.getWriter(), apiResponse);
            
        } catch (SQLException e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据库错误：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    private void handleGetCurrentUser(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null) {
            sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
            return;
        }
        
        // 返回用户信息（不包含密码）
        currentUser.setPassword(null);
        ApiResponse<User> apiResponse = ApiResponse.success(currentUser);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * 获取指定用户信息
     */
    private void handleGetUserById(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws IOException {
        
        // 检查管理员权限
        User currentUser = (User) request.getSession().getAttribute("user");
        if (currentUser == null || !currentUser.isAdmin()) {
            sendError(response, HttpServletResponse.SC_FORBIDDEN, "需要管理员权限");
            return;
        }
        
        // 解析用户ID
        Long userId = parseUserId(pathInfo);
        if (userId == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "无效的用户ID");
            return;
        }
        
        try {
            User user = userService.findById(userId);
            if (user == null) {
                sendError(response, HttpServletResponse.SC_NOT_FOUND, "用户不存在");
                return;
            }
            
            // 返回用户信息（不包含密码）
            user.setPassword(null);
            ApiResponse<User> apiResponse = ApiResponse.success(user);
            objectMapper.writeValue(response.getWriter(), apiResponse);
            
        } catch (SQLException e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据库错误：" + e.getMessage());
        }
    }

    /**
     * 从路径中解析用户ID
     */
    private Long parseUserId(String pathInfo) {
        try {
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2) {
                return Long.parseLong(parts[1]);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    /**
     * 发送错误响应
     */
    private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        ApiResponse<Void> apiResponse = ApiResponse.error(statusCode, message);
        response.setStatus(statusCode);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
