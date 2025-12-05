package com.example.demo.service;

import com.example.demo.dao.BookDao;
import com.example.demo.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookDao bookDao;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(bookDao);
    }

    @Test
    void testSearchBooks() throws SQLException {
        // 准备测试数据
        Book book1 = new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L);
        Book book2 = new Book("Java核心技术", "Cay S. Horstmann", new BigDecimal("89.00"), "Java核心知识", "", 2L);
        
        List<Book> mockBooks = Arrays.asList(book1, book2);
        
        // 设置mock行为
        when(bookDao.findByKeyword("Java", 0, 10)).thenReturn(mockBooks);
        when(bookDao.countByKeyword("Java")).thenReturn(2);
        
        // 执行测试
        BookService.BookPageResult result = bookService.searchBooks("Java", 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getBooks().size());
        assertEquals(2, result.getTotalCount());
        assertEquals(1, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
        
        // 验证方法调用
        verify(bookDao).findByKeyword("Java", 0, 10);
        verify(bookDao).countByKeyword("Java");
    }

    @Test
    void testGetAllBooks() throws SQLException {
        // 准备测试数据
        Book book1 = new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L);
        Book book2 = new Book("设计模式", "GOF", new BigDecimal("79.00"), "设计模式经典", "", 2L);
        
        List<Book> mockBooks = Arrays.asList(book1, book2);
        
        // 设置mock行为
        when(bookDao.findAll(0, 10)).thenReturn(mockBooks);
        when(bookDao.countAll()).thenReturn(2);
        
        // 执行测试
        BookService.BookPageResult result = bookService.getAllBooks(1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getBooks().size());
        assertEquals(2, result.getTotalCount());
        assertEquals(1, result.getCurrentPage());
        
        // 验证方法调用
        verify(bookDao).findAll(0, 10);
        verify(bookDao).countAll();
    }
}