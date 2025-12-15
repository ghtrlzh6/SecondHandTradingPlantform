package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.BookCreateRequest;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookApiServletTest {

    @Mock
    private BookService bookService;
    
    @Mock
    private ServiceFactory serviceFactory;
    
    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private HttpSession session;
    
    @Mock
    private PrintWriter printWriter;
    
    private BookApiServlet bookApiServlet;
    private ObjectMapper objectMapper;
    
    @BeforeEach
    void setUp() throws NamingException {
        bookApiServlet = new BookApiServlet();
        objectMapper = new ObjectMapper();
    }
    
    @Test
    void testInit() throws ServletException, NamingException {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            // 执行初始化
            bookApiServlet.init();
            
            // 验证没有异常抛出
            assertDoesNotThrow(() -> bookApiServlet.init());
        }
    }
    
    @Test
    void testInitWithNamingException() {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenThrow(new RuntimeException("Naming error"));
            
            // 验证初始化失败抛出RuntimeException  
            assertThrows(RuntimeException.class, () -> bookApiServlet.init());
        }
    }
    
    @Test
    void testDoGetBookList() throws ServletException, IOException, SQLException {
        // 准备测试数据
        List<Book> mockBooks = Arrays.asList(
            new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L),
            new Book("设计模式", "GOF", new BigDecimal("79.00"), "设计模式经典", "", 2L)
        );
        
        BookService.BookPageResult mockResult = new BookService.BookPageResult(
            mockBooks, 2, 1, 10
        );
        
        StringWriter stringWriter = new StringWriter();
        
        // 设置mock行为
        when(request.getPathInfo()).thenReturn("/");
        when(request.getParameter("page")).thenReturn("1");
        when(request.getParameter("size")).thenReturn("10");
        when(request.getParameter("keyword")).thenReturn(null);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        when(bookService.getAllBooks(1, 10)).thenReturn(mockResult);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行GET请求
            bookApiServlet.doGet(request, response);
            
            // 验证响应设置
            verify(response).setContentType("application/json");
            verify(response).setCharacterEncoding("UTF-8");
            
            // 验证调用了正确的方法
            verify(bookService).getAllBooks(1, 10);
        }
    }
    
    @Test
    void testDoGetBookById() throws ServletException, IOException, SQLException {
        // 准备测试数据
        Book mockBook = new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L);
        mockBook.setId(1L);
        
        // 设置mock行为
        when(request.getPathInfo()).thenReturn("/1");
        when(response.getWriter()).thenReturn(printWriter);
        when(bookService.getBookById(1L)).thenReturn(mockBook);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行GET请求
            bookApiServlet.doGet(request, response);
            
            // 验证响应设置
            verify(response).setContentType("application/json");
            verify(response).setCharacterEncoding("UTF-8");
            
            // 验证调用了正确的方法
            verify(bookService).getBookById(1L);
        }
    }
    
    @Test
    void testDoGetBookByIdNotFound() throws ServletException, IOException, SQLException {
        // 设置mock行为
        when(request.getPathInfo()).thenReturn("/999");
        when(response.getWriter()).thenReturn(printWriter);
        when(bookService.getBookById(999L)).thenReturn(null);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行GET请求
            bookApiServlet.doGet(request, response);
            
            // 验证404响应
            verify(response).setStatus(404);
        }
    }
    
    @Test
    void testDoPostCreateBook() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User currentUser = new User("seller", "pass", "seller@example.com");
        currentUser.setId(1L);
        currentUser.setRole("user");
        
        BookCreateRequest bookRequest = new BookCreateRequest();
        bookRequest.setTitle("新书");
        bookRequest.setAuthor("新作者");
        bookRequest.setPrice(new BigDecimal("88.00"));
        bookRequest.setDescription("新书描述");
        bookRequest.setImageUrl("image.jpg");
        
        Book createdBook = new Book("新书", "新作者", new BigDecimal("88.00"), "新书描述", "image.jpg", 1L);
        createdBook.setId(1L);
        
        String jsonRequest = objectMapper.writeValueAsString(bookRequest);
        
        // 设置mock行为
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));
        when(response.getWriter()).thenReturn(printWriter);
        when(bookService.addBook(anyString(), anyString(), any(BigDecimal.class), anyString(), anyString(), anyLong())).thenReturn(1L);
        when(bookService.getBookById(1L)).thenReturn(createdBook);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行POST请求
            bookApiServlet.doPost(request, response);
            
            // 验证响应设置
            verify(response).setContentType("application/json");
            verify(response).setCharacterEncoding("UTF-8");
            
            // 验证调用了正确的方法
            verify(bookService).addBook(eq("新书"), eq("新作者"), eq(new BigDecimal("88.00")), eq("新书描述"), eq("image.jpg"), eq(1L));
        }
    }
    
    @Test
    void testDoPostUnauthorized() throws ServletException, IOException {
        // 设置mock行为 - 未登录用户
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(response.getWriter()).thenReturn(printWriter);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行POST请求
            bookApiServlet.doPost(request, response);
            
            // 验证401响应
            verify(response).setStatus(401);
        }
    }
    
    @Test
    void testDoPutUpdateBook() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User currentUser = new User("seller", "pass", "seller@example.com");
        currentUser.setId(1L);
        currentUser.setRole("user");
        
        Book existingBook = new Book("原书名", "原作者", new BigDecimal("50.00"), "原描述", "原图片", 1L);
        existingBook.setId(1L);
        
        BookCreateRequest updateRequest = new BookCreateRequest();
        updateRequest.setTitle("更新书名");
        updateRequest.setAuthor("更新作者");
        updateRequest.setPrice(new BigDecimal("60.00"));
        updateRequest.setDescription("更新描述");
        updateRequest.setImageUrl("更新图片");
        
        Book updatedBook = new Book("更新书名", "更新作者", new BigDecimal("60.00"), "更新描述", "更新图片", 1L);
        updatedBook.setId(1L);
        
        String jsonRequest = objectMapper.writeValueAsString(updateRequest);
        
        // 设置mock行为
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));
        when(response.getWriter()).thenReturn(printWriter);
            when(bookService.getBookById(1L)).thenReturn(existingBook).thenReturn(updatedBook);
            when(bookService.updateBook(any(Book.class))).thenReturn(1);
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行PUT请求
            bookApiServlet.doPut(request, response);
            
            // 验证响应设置
            verify(response).setContentType("application/json");
            verify(response).setCharacterEncoding("UTF-8");
            
            // 验证调用了正确的方法
            verify(bookService).updateBook(any(Book.class));
        }
    }
    
    @Test
    void testDoPutUnauthorized() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User otherUser = new User("other", "pass", "other@example.com");
        otherUser.setId(2L);
        otherUser.setRole("user");
        
        Book existingBook = new Book("书名", "作者", new BigDecimal("50.00"), "描述", "图片", 1L);
        existingBook.setId(1L);
        
        BookCreateRequest updateRequest = new BookCreateRequest();
        updateRequest.setTitle("更新书名");
        updateRequest.setAuthor("更新作者");
        updateRequest.setPrice(new BigDecimal("60.00"));
        
        String jsonRequest = objectMapper.writeValueAsString(updateRequest);
        
        // 设置mock行为 - 非书籍所有者
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(otherUser);
        lenient().when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));
        when(response.getWriter()).thenReturn(printWriter);
        lenient().when(bookService.getBookById(1L)).thenReturn(existingBook);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行PUT请求
            bookApiServlet.doPut(request, response);
            
            // 验证403响应
            verify(response).setStatus(403);

        }
    }
    
    @Test
    void testDoDeleteBook() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User currentUser = new User("seller", "pass", "seller@example.com");
        currentUser.setId(1L);
        currentUser.setRole("user");
        
        Book existingBook = new Book("书名", "作者", new BigDecimal("50.00"), "描述", "图片", 1L);
        existingBook.setId(1L);
        
        // 设置mock行为
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(response.getWriter()).thenReturn(printWriter);
        when(bookService.getBookById(1L)).thenReturn(existingBook);
        when(bookService.updateBook(any(Book.class))).thenReturn(1);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行DELETE请求
            bookApiServlet.doDelete(request, response);
            
            // 验证响应设置
            verify(response).setContentType("application/json");
            verify(response).setCharacterEncoding("UTF-8");
            
            // 验证调用了正确的方法
            verify(bookService).updateBook(any(Book.class));
        }
    }
    
    @Test
    void testDoDeleteUnauthorized() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User otherUser = new User("other", "pass", "other@example.com");
        otherUser.setId(2L);
        otherUser.setRole("user");
        
        Book existingBook = new Book("书名", "作者", new BigDecimal("50.00"), "描述", "图片", 1L);
        existingBook.setId(1L);
        
        // 设置mock行为 - 非书籍所有者
        when(request.getPathInfo()).thenReturn("/1");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(otherUser);
        when(response.getWriter()).thenReturn(printWriter);
        when(bookService.getBookById(1L)).thenReturn(existingBook);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行DELETE请求
            bookApiServlet.doDelete(request, response);
            
            // 验证403响应
            verify(response).setStatus(403);
        }
    }
    
    @Test
    void testDoGetWithInvalidBookId() throws ServletException, IOException {
        // 设置mock行为 - 无效的书籍ID
        when(request.getPathInfo()).thenReturn("/invalid");
        when(response.getWriter()).thenReturn(printWriter);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行GET请求
            bookApiServlet.doGet(request, response);
            
            // 验证400响应
            verify(response).setStatus(400);
        }
    }
    
    @Test
    void testDoPostWithInvalidData() throws ServletException, IOException {
        // 准备测试数据
        User currentUser = new User("seller", "pass", "seller@example.com");
        currentUser.setId(1L);
        currentUser.setRole("user");
        
        BookCreateRequest invalidRequest = new BookCreateRequest();
        invalidRequest.setTitle(""); // 无效：空标题
        invalidRequest.setAuthor("作者");
        invalidRequest.setPrice(new BigDecimal("50.00"));
        
        String jsonRequest = objectMapper.writeValueAsString(invalidRequest);
        
        // 设置mock行为
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(currentUser);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(jsonRequest)));
        when(response.getWriter()).thenReturn(printWriter);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookApiServlet.init();
            
            // 执行POST请求
            bookApiServlet.doPost(request, response);
            
            // 验证400响应
            verify(response).setStatus(400);
        }
    }
}
