package com.example.demo.service;

import com.example.demo.dao.BookDao;
import com.example.demo.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookDao bookDao;

    private BookService bookService;

    @BeforeEach
    void setUp() {
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
    void testSearchBooksEmptyResult() throws SQLException {
        // 设置mock行为 - 没有匹配结果
        when(bookDao.findByKeyword("NonExistent", 0, 10)).thenReturn(Arrays.asList());
        when(bookDao.countByKeyword("NonExistent")).thenReturn(0);
        
        // 执行测试
        BookService.BookPageResult result = bookService.searchBooks("NonExistent", 1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getBooks().size());
        assertEquals(0, result.getTotalCount());
        assertEquals(1, result.getCurrentPage());
        assertEquals(0, result.getTotalPages());
        assertFalse(result.hasNextPage());
        assertFalse(result.hasPreviousPage());
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

    @Test
    void testGetAllBooksWithPagination() throws SQLException {
        // 准备测试数据 - 总共25本书
        List<Book> firstPageBooks = Arrays.asList(
                new Book("Book1", "Author1", new BigDecimal("50.00"), "Desc1", "", 1L),
                new Book("Book2", "Author2", new BigDecimal("60.00"), "Desc2", "", 1L),
                new Book("Book3", "Author3", new BigDecimal("70.00"), "Desc3", "", 1L)
        );
        
        // 设置mock行为
        when(bookDao.findAll(0, 10)).thenReturn(firstPageBooks);
        when(bookDao.countAll()).thenReturn(25);
        
        // 执行测试
        BookService.BookPageResult result = bookService.getAllBooks(1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.getBooks().size());
        assertEquals(25, result.getTotalCount());
        assertEquals(1, result.getCurrentPage());
        assertEquals(3, result.getTotalPages());
        assertTrue(result.hasNextPage());
        assertFalse(result.hasPreviousPage());
        assertEquals(2, result.getNextPage());
    }

    @Test
    void testGetAllBooksForAdmin() throws SQLException {
        // 准备测试数据
        List<Book> allBooks = Arrays.asList(
                new Book("Available Book", "Author1", new BigDecimal("50.00"), "Desc1", "", 1L),
                new Book("Sold Book", "Author2", new BigDecimal("60.00"), "Desc2", "", 1L),
                new Book("Removed Book", "Author3", new BigDecimal("70.00"), "Desc3", "", 1L)
        );
        
        // 设置mock行为
        when(bookDao.findAllIncludingRemoved(0, 10)).thenReturn(allBooks);
        when(bookDao.countAllIncludingRemoved()).thenReturn(3);
        
        // 执行测试
        BookService.BookPageResult result = bookService.getAllBooksForAdmin(1, 10);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(3, result.getBooks().size());
        assertEquals(3, result.getTotalCount());
        assertEquals(1, result.getCurrentPage());
        
        // 验证方法调用
        verify(bookDao).findAllIncludingRemoved(0, 10);
        verify(bookDao).countAllIncludingRemoved();
    }

    @Test
    void testGetBookById() throws SQLException {
        // 准备测试数据
        Book book = new Book("Java编程思想", "Bruce Eckel", new BigDecimal("99.00"), "Java经典教材", "", 1L);
        book.setId(1L);
        
        // 设置mock行为
        when(bookDao.findById(1L)).thenReturn(Optional.of(book));
        
        // 执行测试
        Book result = bookService.getBookById(1L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("Java编程思想", result.getTitle());
        assertEquals("Bruce Eckel", result.getAuthor());
        
        // 验证方法调用
        verify(bookDao).findById(1L);
    }

    @Test
    void testGetBookByIdNotFound() throws SQLException {
        // 设置mock行为
        when(bookDao.findById(999L)).thenReturn(Optional.empty());
        
        // 执行测试
        Book result = bookService.getBookById(999L);
        
        // 验证结果
        assertNull(result);
        
        // 验证方法调用
        verify(bookDao).findById(999L);
    }

    @Test
    void testAddBook() throws SQLException {
        // 设置mock行为
        when(bookDao.save(any(Book.class))).thenReturn(1L);
        
        // 执行测试
        Long result = bookService.addBook("新书", "新作者", new BigDecimal("88.00"), "新书描述", "", 1L);
        
        // 验证结果
        assertEquals(1L, result);
        
        // 验证方法调用
        verify(bookDao).save(any(Book.class));
    }

    @Test
    void testUpdateBook() throws SQLException {
        // 准备测试数据
        Book book = new Book("更新的书", "更新的作者", new BigDecimal("99.00"), "更新的描述", "", 1L);
        book.setId(1L);
        
        // 设置mock行为
        when(bookDao.update(book)).thenReturn(1);
        
        // 执行测试
        int result = bookService.updateBook(book);
        
        // 验证结果
        assertEquals(1, result);
        
        // 验证方法调用
        verify(bookDao).update(book);
    }

    @Test
    void testUpdateBookFailed() throws SQLException {
        // 准备测试数据
        Book book = new Book("更新的书", "更新的作者", new BigDecimal("99.00"), "更新的描述", "", 1L);
        book.setId(1L);
        
        // 设置mock行为
        when(bookDao.update(book)).thenReturn(0);
        
        // 执行测试
        int result = bookService.updateBook(book);
        
        // 验证结果
        assertEquals(0, result);
        
        // 验证方法调用
        verify(bookDao).update(book);
    }

    @Test
    void testGetBooksBySellerId() throws SQLException {
        // 准备测试数据
        List<Book> sellerBooks = Arrays.asList(
                new Book("Book1", "Author1", new BigDecimal("50.00"), "Desc1", "", 1L),
                new Book("Book2", "Author2", new BigDecimal("60.00"), "Desc2", "", 1L)
        );
        
        // 设置mock行为
        when(bookDao.findBySellerId(1L)).thenReturn(sellerBooks);
        
        // 执行测试
        List<Book> result = bookService.getBooksBySellerId(1L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        
        // 验证方法调用
        verify(bookDao).findBySellerId(1L);
    }

    @Test
    void testGetBooksBySellerIdEmpty() throws SQLException {
        // 设置mock行为
        when(bookDao.findBySellerId(999L)).thenReturn(Arrays.asList());
        
        // 执行测试
        List<Book> result = bookService.getBooksBySellerId(999L);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.size());
        
        // 验证方法调用
        verify(bookDao).findBySellerId(999L);
    }

    @Test
    void testBookPageResultPagination() {
        // 测试BookPageResult的分页逻辑
        BookService.BookPageResult result = new BookService.BookPageResult(
                Arrays.asList(), 100, 3, 10
        );
        
        // 验证分页信息
        assertEquals(100, result.getTotalCount());
        assertEquals(3, result.getCurrentPage());
        assertEquals(10, result.getTotalPages());
        assertTrue(result.hasNextPage());
        assertTrue(result.hasPreviousPage());
        assertEquals(4, result.getNextPage());
        assertEquals(2, result.getPreviousPage());
    }

    @Test
    void testBookPageResultFirstPage() {
        // 测试第一页
        BookService.BookPageResult result = new BookService.BookPageResult(
                Arrays.asList(), 50, 1, 10
        );
        
        assertEquals(1, result.getCurrentPage());
        assertEquals(5, result.getTotalPages());
        assertTrue(result.hasNextPage());
        assertFalse(result.hasPreviousPage());
        assertEquals(2, result.getNextPage());
    }

    @Test
    void testBookPageResultLastPage() {
        // 测试最后一页
        BookService.BookPageResult result = new BookService.BookPageResult(
                Arrays.asList(), 50, 5, 10
        );
        
        assertEquals(5, result.getCurrentPage());
        assertEquals(5, result.getTotalPages());
        assertFalse(result.hasNextPage());
        assertTrue(result.hasPreviousPage());
        assertEquals(4, result.getPreviousPage());
    }

    @Test
    void testBookPageResultEdgeCases() {
        // 测试边界情况
        BookService.BookPageResult singlePage = new BookService.BookPageResult(
                Arrays.asList(new Book("Book", "Author", BigDecimal.TEN, "Desc", "", 1L)), 
                1, 1, 10
        );
        
        assertEquals(1, singlePage.getTotalCount());
        assertEquals(1, singlePage.getCurrentPage());
        assertEquals(1, singlePage.getTotalPages());
        assertFalse(singlePage.hasNextPage());
        assertFalse(singlePage.hasPreviousPage());
        assertEquals(1, singlePage.getNextPage());
        assertEquals(1, singlePage.getPreviousPage());
    }
}
