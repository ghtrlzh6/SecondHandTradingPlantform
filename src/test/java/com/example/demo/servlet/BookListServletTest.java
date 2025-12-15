package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookListServletTest {

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
    private RequestDispatcher requestDispatcher;
    
    private BookListServlet bookListServlet;
    
    @BeforeEach
    void setUp() throws NamingException {
        bookListServlet = new BookListServlet();
    }
    
    @Test
    void testInit() throws ServletException, NamingException {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            // 执行初始化
            bookListServlet.init();
            
            // 验证没有异常抛出
            assertDoesNotThrow(() -> bookListServlet.init());
        }
    }
    
    @Test
    void testInitWithNamingException() {
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenThrow(new RuntimeException("Naming error"));
            
            // 验证初始化失败抛出RuntimeException
            RuntimeException exception = assertThrows(RuntimeException.class, () -> bookListServlet.init());
            assertTrue(exception.getMessage().contains("Naming error"));
        }
    }
    
    @Test
    void testDoGetWithNormalUser() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User normalUser = new User("user", "pass", "user@example.com");
        normalUser.setId(1L);
        normalUser.setRole("user");
        
        List<Book> mockBooks = Arrays.asList(
            new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L),
            new Book("设计模式", "GOF", new BigDecimal("79.00"), "设计模式经典", "", 2L)
        );
        
        BookService.BookPageResult mockResult = new BookService.BookPageResult(
            mockBooks, 2, 1, 10
        );
        
        // 设置mock行为
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(normalUser);
        when(request.getParameter("page")).thenReturn("1");
        when(request.getRequestDispatcher("/book-list.jsp")).thenReturn(requestDispatcher);
        when(bookService.getAllBooks(1, 10)).thenReturn(mockResult);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookListServlet.init();
            
            // 执行GET请求
            bookListServlet.doGet(request, response);
            
            // 验证响应设置
            verify(response).setContentType("text/html;charset=UTF-8");
            verify(response).setCharacterEncoding("UTF-8");
            
            // 验证request属性设置
            verify(request).setAttribute("books", mockBooks);
            verify(request).setAttribute("currentPage", 1);
            verify(request).setAttribute("totalPages", 1);
            verify(request).setAttribute("totalCount", 2);
            verify(request).setAttribute("hasNextPage", false);
            verify(request).setAttribute("hasPreviousPage", false);
            verify(request).setAttribute("nextPage", 1);
            verify(request).setAttribute("previousPage", 1);
            
            // 验证转发
            verify(requestDispatcher).forward(request, response);
            
            // 验证调用了正确的方法
            verify(bookService).getAllBooks(1, 10);
        }
    }
    
    @Test
    void testDoGetWithAdminUser() throws ServletException, IOException, SQLException {
        // 准备测试数据
        User adminUser = new User("admin", "admin", "admin@example.com");
        adminUser.setId(1L);
        adminUser.setRole("admin");
        
        List<Book> mockBooks = Arrays.asList(
            new Book("Available Book", "Author1", new BigDecimal("50.00"), "Desc1", "", 1L),
            new Book("Removed Book", "Author2", new BigDecimal("60.00"), "Desc2", "", 2L)
        );
        
        BookService.BookPageResult mockResult = new BookService.BookPageResult(
            mockBooks, 2, 1, 10
        );
        
        // 设置mock行为
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getParameter("page")).thenReturn("1");
        when(request.getRequestDispatcher("/book-list.jsp")).thenReturn(requestDispatcher);
        when(bookService.getAllBooksForAdmin(1, 10)).thenReturn(mockResult);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookListServlet.init();
            
            // 执行GET请求
            bookListServlet.doGet(request, response);
            
            // 验证调用了管理员方法
            verify(bookService).getAllBooksForAdmin(1, 10);
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoGetWithNoUser() throws ServletException, IOException, SQLException {
        // 准备测试数据
        List<Book> mockBooks = Arrays.asList(
            new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L)
        );
        
        BookService.BookPageResult mockResult = new BookService.BookPageResult(
            mockBooks, 1, 1, 10
        );
        
        // 设置mock行为 - 没有用户登录
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("1");
        when(request.getRequestDispatcher("/book-list.jsp")).thenReturn(requestDispatcher);
        when(bookService.getAllBooks(1, 10)).thenReturn(mockResult);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookListServlet.init();
            
            // 执行GET请求
            bookListServlet.doGet(request, response);
            
            // 验证调用了普通用户方法
            verify(bookService).getAllBooks(1, 10);
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoGetWithInvalidPageNumber() throws ServletException, IOException, SQLException {
        // 准备测试数据
        List<Book> mockBooks = Arrays.asList(
            new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L)
        );
        
        BookService.BookPageResult mockResult = new BookService.BookPageResult(
            mockBooks, 1, 1, 10
        );
        
        // 设置mock行为 - 无效页码
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("invalid");
        when(request.getRequestDispatcher("/book-list.jsp")).thenReturn(requestDispatcher);
        when(bookService.getAllBooks(1, 10)).thenReturn(mockResult);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookListServlet.init();
            
            // 执行GET请求
            bookListServlet.doGet(request, response);
            
            // 验证使用默认页码1
            verify(bookService).getAllBooks(1, 10);
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoGetWithNullPageParameter() throws ServletException, IOException, SQLException {
        // 准备测试数据
        List<Book> mockBooks = Arrays.asList(
            new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L)
        );
        
        BookService.BookPageResult mockResult = new BookService.BookPageResult(
            mockBooks, 1, 1, 10
        );
        
        // 设置mock行为 - 页码参数为null
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getParameter("page")).thenReturn(null);
        when(request.getRequestDispatcher("/book-list.jsp")).thenReturn(requestDispatcher);
        when(bookService.getAllBooks(1, 10)).thenReturn(mockResult);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookListServlet.init();
            
            // 执行GET请求
            bookListServlet.doGet(request, response);
            
            // 验证使用默认页码1
            verify(bookService).getAllBooks(1, 10);
            verify(requestDispatcher).forward(request, response);
        }
    }
    
    @Test
    void testDoGetWithSQLException() throws ServletException, IOException, SQLException {
        // 设置mock行为 - 数据库异常
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("1");
        when(bookService.getAllBooks(1, 10)).thenThrow(new SQLException("Database error"));
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookListServlet.init();
            
            // 验证抛出ServletException
            ServletException exception = assertThrows(ServletException.class, 
                () -> bookListServlet.doGet(request, response));
            assertTrue(exception.getMessage().contains("Database error while fetching books"));
        }
    }
    
    @Test
    void testDoGetWithEmptyBookList() throws ServletException, IOException, SQLException {
        // 准备测试数据 - 空列表
        List<Book> emptyBooks = Arrays.asList();
        BookService.BookPageResult mockResult = new BookService.BookPageResult(
            emptyBooks, 0, 1, 10
        );
        
        // 设置mock行为
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("1");
        when(request.getRequestDispatcher("/book-list.jsp")).thenReturn(requestDispatcher);
        when(bookService.getAllBooks(1, 10)).thenReturn(mockResult);
        
        // 模拟成功初始化
        try (MockedStatic<ServiceFactory> mockedFactory = mockStatic(ServiceFactory.class)) {
            mockedFactory.when(ServiceFactory::getInstance).thenReturn(serviceFactory);
            when(serviceFactory.getBookService()).thenReturn(bookService);
            
            bookListServlet.init();
            
            // 执行GET请求
            bookListServlet.doGet(request, response);
            
            // 验证设置了空列表属性
            verify(request).setAttribute("books", emptyBooks);
            verify(request).setAttribute("totalCount", 0);
            verify(request).setAttribute("totalPages", 0);
            verify(requestDispatcher).forward(request, response);
        }
    }
}
