package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.service.UserService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterServletTest {

    @Mock
    private UserService userService;
    
    @Mock
    private ServiceFactory serviceFactory;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private RequestDispatcher requestDispatcher;
    
    private RegisterServlet registerServlet;
    
    @BeforeEach
    void setUp() throws NamingException {
        registerServlet = new RegisterServlet();
    }
    
    @Test
    void testInit() throws ServletException, NamingException {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            // 执行初始化
            registerServlet.init();
            
            // 验证没有异常抛出
            assertDoesNotThrow(() -> registerServlet.init());
        }
    }
    
    @Test
    void testInitWithNamingException() {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenThrow(new RuntimeException("Naming error"));
            
            // 验证初始化失败抛出RuntimeException
            assertThrows(RuntimeException.class, () -> registerServlet.init());
        }
    }
    
    @Test
    void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);
        
        // 执行GET请求
        registerServlet.doGet(request, response);
        
        // 验证转发到注册页面
        verify(requestDispatcher).forward(request, response);
    }
    
    @Test
    void testDoPostSuccessfulRegistration() throws ServletException, IOException, SQLException {
        // 设置mock行为
        when(request.getParameter("username")).thenReturn("newuser");
        when(request.getParameter("password")).thenReturn("newpass");
        when(request.getParameter("email")).thenReturn("newuser@example.com");
        when(userService.register("newuser", "newpass", "newuser@example.com")).thenReturn(true);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            registerServlet.init();
            
            // 执行POST请求
            registerServlet.doPost(request, response);
            
            // 验证重定向到登录页面
            verify(response).sendRedirect("login.jsp");
        }
    }
    
    @Test
    void testDoPostWithExistingUsername() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 用户名已存在
        when(request.getParameter("username")).thenReturn("existinguser");
        when(request.getParameter("password")).thenReturn("pass");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);
        when(userService.register("existinguser", "pass", "test@example.com")).thenReturn(false);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            registerServlet.init();
            
            // 执行POST请求
            registerServlet.doPost(request, response);
            
            // 验证错误消息设置和转发
            verify(request).setAttribute("errorMessage", "用户名已存在");
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoPostWithSQLException() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 数据库异常
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getParameter("email")).thenReturn("test@example.com");
        when(userService.register("testuser", "testpass", "test@example.com"))
            .thenThrow(new SQLException("Database error"));
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            registerServlet.init();
            
            // 验证抛出ServletException
            ServletException exception = assertThrows(ServletException.class, 
                () -> registerServlet.doPost(request, response));
            assertTrue(exception.getMessage().contains("Database error during registration"));
        }
    }
    
    @Test
    void testDoPostWithNullParameters() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 参数为null
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getParameter("email")).thenReturn(null);
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);
        when(userService.register(isNull(), isNull(), isNull())).thenReturn(false);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            registerServlet.init();
            
            // 执行POST请求
            registerServlet.doPost(request, response);
            
            // 验证错误消息设置和转发
            verify(request).setAttribute("errorMessage", "用户名已存在");
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoPostWithEmptyParameters() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 参数为空字符串
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("email")).thenReturn("");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);
        when(userService.register(eq(""), eq(""), eq(""))).thenReturn(false);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            registerServlet.init();
            
            // 执行POST请求
            registerServlet.doPost(request, response);
            
            // 验证错误消息设置和转发
            verify(request).setAttribute("errorMessage", "用户名已存在");
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoPostWithInvalidEmail() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 无效邮箱
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getParameter("email")).thenReturn("invalid-email");
        when(request.getRequestDispatcher("/register.jsp")).thenReturn(requestDispatcher);
        when(userService.register("testuser", "testpass", "invalid-email")).thenReturn(false);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            registerServlet.init();
            
            // 执行POST请求
            registerServlet.doPost(request, response);
            
            // 验证错误消息设置和转发
            verify(request).setAttribute("errorMessage", "用户名已存在");
            verify(requestDispatcher).forward(request, response);
        }
    }
}
