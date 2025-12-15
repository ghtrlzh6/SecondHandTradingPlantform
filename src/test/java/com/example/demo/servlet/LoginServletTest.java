package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.User;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServletTest {

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
    
    private LoginServlet loginServlet;
    
    @BeforeEach
    void setUp() throws NamingException {
        loginServlet = new LoginServlet();
    }
    
    @Test
    void testInit() throws ServletException, NamingException {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            // 执行初始化
            loginServlet.init();
            
            // 验证没有异常抛出
            assertDoesNotThrow(() -> loginServlet.init());
        }
    }
    
    @Test
    void testInitWithNamingException() {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenThrow(new RuntimeException("Naming error"));
            
            // 验证初始化失败抛出RuntimeException
            assertThrows(RuntimeException.class, () -> loginServlet.init());
        }
    }
    
    @Test
    void testDoGet() throws ServletException, IOException {
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        
        // 执行GET请求
        loginServlet.doGet(request, response);
        
        // 验证转发到登录页面
        verify(requestDispatcher).forward(request, response);
    }
    
    @Test
    void testDoPostSuccessfulLogin() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User mockUser = new User("testuser", "testpass", "test@example.com");
        mockUser.setId(1L);
        
        // 设置mock行为
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpass");
        when(request.getSession()).thenReturn(session);
        when(userService.login("testuser", "testpass")).thenReturn(mockUser);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            loginServlet.init();
            
            // 执行POST请求
            loginServlet.doPost(request, response);
            
            // 验证session设置和重定向
            verify(session).setAttribute("user", mockUser);
            verify(session).setAttribute("userId", 1L);
            verify(response).sendRedirect("index.jsp");
        }
    }
    
    @Test
    void testDoPostFailedLogin() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 登录失败
        when(request.getParameter("username")).thenReturn("wronguser");
        when(request.getParameter("password")).thenReturn("wrongpass");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        when(userService.login("wronguser", "wrongpass")).thenReturn(null);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            loginServlet.init();
            
            // 执行POST请求
            loginServlet.doPost(request, response);
            
            // 验证错误消息设置和转发
            verify(request).setAttribute("errorMessage", "用户名或密码错误");
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoPostWithSQLException() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 数据库异常
        when(request.getParameter("username")).thenReturn("testuser");
        when(request.getParameter("password")).thenReturn("testpass");
        when(userService.login("testuser", "testpass")).thenThrow(new SQLException("Database error"));
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            loginServlet.init();
            
            // 验证抛出ServletException
            ServletException exception = assertThrows(ServletException.class, 
                () -> loginServlet.doPost(request, response));
            assertTrue(exception.getMessage().contains("Database error during login"));
        }
    }
    
    @Test
    void testDoPostWithNullParameters() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 参数为null
        when(request.getParameter("username")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(null);
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        when(userService.login(null, null)).thenReturn(null);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            loginServlet.init();
            
            // 执行POST请求
            loginServlet.doPost(request, response);
            
            // 验证错误处理
            verify(request).setAttribute("errorMessage", "用户名或密码错误");
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoPostWithEmptyParameters() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 参数为空字符串
        when(request.getParameter("username")).thenReturn("");
        when(request.getParameter("password")).thenReturn("");
        when(request.getRequestDispatcher("/login.jsp")).thenReturn(requestDispatcher);
        when(userService.login("", "")).thenReturn(null);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getUserService()).thenReturn(userService);
            
            loginServlet.init();
            
            // 执行POST请求
            loginServlet.doPost(request, response);
            
            // 验证错误处理
            verify(request).setAttribute("errorMessage", "用户名或密码错误");
            verify(requestDispatcher).forward(request, response);
        }
    }
}
