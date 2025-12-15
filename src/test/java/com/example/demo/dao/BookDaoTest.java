package com.example.demo.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.dao.impl.BookDaoImpl;
import com.example.demo.model.Book;

class BookDaoTest {

    private DataSource dataSource;
    private BookDao bookDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        dataSource = createTestDataSource();
        bookDao = new BookDaoImpl(dataSource);
        connection = dataSource.getConnection();
        createTestTables();
        insertTestData();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private DataSource createTestDataSource() {
        org.h2.jdbcx.JdbcDataSource ds = new org.h2.jdbcx.JdbcDataSource();
        // 使用唯一的数据库名称避免与其他测试冲突
        ds.setURL("jdbc:h2:mem:bookTestDb;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    private void createTestTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // 先删除可能存在的表
            stmt.execute("DROP TABLE IF EXISTS books");
            stmt.execute("DROP TABLE IF EXISTS users");
            // 创建用户表
            stmt.execute("CREATE TABLE users (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(50) NOT NULL UNIQUE, " +
                    "password VARCHAR(100) NOT NULL, " +
                    "email VARCHAR(100) NOT NULL, " +
                    "rating DECIMAL(3,2) DEFAULT 5.00, " +
                    "total_ratings INT DEFAULT 0, " +
                    "role VARCHAR(20) DEFAULT 'user', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")");
            
            // 创建书籍表
            stmt.execute("CREATE TABLE books (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(200) NOT NULL, " +
                    "author VARCHAR(100) NOT NULL, " +
                    "price DECIMAL(10, 2) NOT NULL, " +
                    "description TEXT, " +
                    "image_url VARCHAR(500), " +
                    "seller_id BIGINT NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'available', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (seller_id) REFERENCES users(id)" +
                    ")");
        }
    }

    private void insertTestData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // 插入测试用户
            stmt.execute("INSERT INTO users (username, password, email, role) VALUES " +
                    "('seller1', 'pass1', 'seller1@example.com', 'user'), " +
                    "('seller2', 'pass2', 'seller2@example.com', 'user')");
        }
    }

    @Test
    void testSaveBook() throws SQLException {
        Book book = new Book("Java编程思想", "Bruce Eckel", 
                new BigDecimal("99.00"), "Java经典教材", "", 1L);
        book.setStatus("available");

        Long bookId = bookDao.save(book);

        assertNotNull(bookId);
        assertTrue(bookId > 0);

        // 验证书籍已保存
        Optional<Book> savedBook = bookDao.findById(bookId);
        assertTrue(savedBook.isPresent());
        assertEquals("Java编程思想", savedBook.get().getTitle());
        assertEquals("Bruce Eckel", savedBook.get().getAuthor());
        assertEquals(new BigDecimal("99.00"), savedBook.get().getPrice());
    }

    @Test
    void testFindById() throws SQLException {
        Book book = new Book("设计模式", "GOF", 
                new BigDecimal("79.00"), "设计模式经典", "", 1L);
        Long bookId = bookDao.save(book);

        Optional<Book> foundBook = bookDao.findById(bookId);

        assertTrue(foundBook.isPresent());
        assertEquals(bookId, foundBook.get().getId());
        assertEquals("设计模式", foundBook.get().getTitle());
        assertEquals("GOF", foundBook.get().getAuthor());
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        Optional<Book> foundBook = bookDao.findById(999L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    void testFindAll() throws SQLException {
        // 创建多本书
        bookDao.save(new Book("Java核心技术", "Cay S. Horstmann", 
                new BigDecimal("89.00"), "Java核心知识", "", 1L));
        bookDao.save(new Book("算法导论", "Thomas H. Cormen", 
                new BigDecimal("129.00"), "算法经典", "", 2L));

        List<Book> books = bookDao.findAll(0, 10);

        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(b -> "Java核心技术".equals(b.getTitle())));
        assertTrue(books.stream().anyMatch(b -> "算法导论".equals(b.getTitle())));
    }

    @Test
    void testFindAllWithPagination() throws SQLException {
        // 创建多本书
        for (int i = 1; i <= 15; i++) {
            bookDao.save(new Book("Book " + i, "Author " + i, 
                    new BigDecimal("50.00"), "Description " + i, "", 1L));
        }

        // 测试第一页
        List<Book> firstPage = bookDao.findAll(0, 5);
        assertEquals(5, firstPage.size());

        // 测试第二页
        List<Book> secondPage = bookDao.findAll(5, 5);
        assertEquals(5, secondPage.size());

        // 测试最后一页
        List<Book> lastPage = bookDao.findAll(10, 5);
        assertEquals(5, lastPage.size());
    }

    @Test
    void testFindBySellerId() throws SQLException {
        // 创建不同卖家的书
        bookDao.save(new Book("Book1", "Author1", 
                new BigDecimal("50.00"), "Desc1", "", 1L));
        bookDao.save(new Book("Book2", "Author2", 
                new BigDecimal("60.00"), "Desc2", "", 1L));
        bookDao.save(new Book("Book3", "Author3", 
                new BigDecimal("70.00"), "Desc3", "", 2L));

        List<Book> seller1Books = bookDao.findBySellerId(1L);
        assertEquals(2, seller1Books.size());

        List<Book> seller2Books = bookDao.findBySellerId(2L);
        assertEquals(1, seller2Books.size());
    }

    @Test
    void testFindByKeyword() throws SQLException {
        // 创建测试书籍
        bookDao.save(new Book("Java编程思想", "Bruce Eckel", 
                new BigDecimal("99.00"), "Java经典教材", "", 1L));
        bookDao.save(new Book("Java核心技术", "Cay S. Horstmann", 
                new BigDecimal("89.00"), "Java核心知识", "", 1L));
        bookDao.save(new Book("设计模式", "GOF", 
                new BigDecimal("79.00"), "设计模式经典", "", 2L));

        List<Book> javaBooks = bookDao.findByKeyword("Java", 0, 10);
        assertEquals(2, javaBooks.size());

        List<Book> designBooks = bookDao.findByKeyword("设计", 0, 10);
        assertEquals(1, designBooks.size());

        List<Book> noMatchBooks = bookDao.findByKeyword("Python", 0, 10);
        assertEquals(0, noMatchBooks.size());
    }

    @Test
    void testFindByKeywordWithPagination() throws SQLException {
        // 创建多本包含关键词的书
        for (int i = 1; i <= 12; i++) {
            bookDao.save(new Book("Java Book " + i, "Java Author " + i, 
                    new BigDecimal("50.00"), "Java Description " + i, "", 1L));
        }

        // 测试分页
        List<Book> firstPage = bookDao.findByKeyword("Java", 0, 5);
        assertEquals(5, firstPage.size());

        List<Book> secondPage = bookDao.findByKeyword("Java", 5, 5);
        assertEquals(5, secondPage.size());

        List<Book> thirdPage = bookDao.findByKeyword("Java", 10, 5);
        assertEquals(2, thirdPage.size());
    }

    @Test
    void testCountAll() throws SQLException {
        // 初始数量为0
        assertEquals(0, bookDao.countAll());

        // 创建书籍
        bookDao.save(new Book("Book1", "Author1", 
                new BigDecimal("50.00"), "Desc1", "", 1L));
        bookDao.save(new Book("Book2", "Author2", 
                new BigDecimal("60.00"), "Desc2", "", 1L));

        // 验证数量
        assertEquals(2, bookDao.countAll());
    }

    @Test
    void testCountByKeyword() throws SQLException {
        // 创建测试书籍
        bookDao.save(new Book("Java编程思想", "Bruce Eckel", 
                new BigDecimal("99.00"), "Java经典教材", "", 1L));
        bookDao.save(new Book("Java核心技术", "Cay S. Horstmann", 
                new BigDecimal("89.00"), "Java核心知识", "", 1L));
        bookDao.save(new Book("设计模式", "GOF", 
                new BigDecimal("79.00"), "设计模式经典", "", 2L));

        assertEquals(2, bookDao.countByKeyword("Java"));
        assertEquals(1, bookDao.countByKeyword("设计"));
        assertEquals(0, bookDao.countByKeyword("Python"));
    }

    @Test
    void testUpdateBook() throws SQLException {
        // 创建书籍
        Book book = new Book("Original Title", "Original Author", 
                new BigDecimal("50.00"), "Original Description", "", 1L);
        Long bookId = bookDao.save(book);

        // 更新书籍
        book.setId(bookId);
        book.setTitle("Updated Title");
        book.setPrice(new BigDecimal("75.00"));
        book.setStatus("sold");

        int updated = bookDao.update(book);
        assertEquals(1, updated);

        // 验证更新
        Optional<Book> updatedBook = bookDao.findById(bookId);
        assertTrue(updatedBook.isPresent());
        assertEquals("Updated Title", updatedBook.get().getTitle());
        assertEquals(new BigDecimal("75.00"), updatedBook.get().getPrice());
        assertEquals("sold", updatedBook.get().getStatus());
    }

    @Test
    void testFindAllIncludingRemoved() throws SQLException {
        // 创建不同状态的书籍
        Book availableBook = new Book("Available Book", "Author1", 
                new BigDecimal("50.00"), "Desc1", "", 1L);
        availableBook.setStatus("available");
        bookDao.save(availableBook);

        Book removedBook = new Book("Removed Book", "Author2", 
                new BigDecimal("60.00"), "Desc2", "", 1L);
        removedBook.setStatus("removed");
        bookDao.save(removedBook);

        // 测试包含已下架的查询
        List<Book> allBooks = bookDao.findAllIncludingRemoved(0, 10);
        assertEquals(2, allBooks.size());

        int count = bookDao.countAllIncludingRemoved();
        assertEquals(2, count);
    }

    @Test
    void testFindBooksWithStatusFiltering() throws SQLException {
        // 先创建一个用户
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO users (username, password, email) VALUES ('seller', 'pass', 'seller@test.com')");
        }
        // 创建不同状态的书籍，通过update方法设置sold状态
        Book availableBook = new Book("Available Book", "Author1", 
                new BigDecimal("50.00"), "Desc1", "", 1L);
        availableBook.setStatus("available");
        Long book1Id = bookDao.save(availableBook);

        Book soldBook = new Book("Sold Book", "Author2", 
                new BigDecimal("60.00"), "Desc2", "", 1L);
        Long book2Id = bookDao.save(soldBook);
        
        // 保存后再更新为sold状态
        soldBook.setId(book2Id);
        soldBook.setStatus("sold");
        bookDao.update(soldBook);

        // findAll只返回available状态的书籍
        List<Book> allBooks = bookDao.findAll(0, 10);
        
        // 验证findAll只返回available状态的书
        assertEquals(1, allBooks.size(), "Should have only 1 available book");
        assertEquals("Available Book", allBooks.get(0).getTitle());
        assertEquals("available", allBooks.get(0).getStatus());
        
        // 使用findAllIncludingRemoved可以获取所有状态的书
        List<Book> allIncluding = bookDao.findAllIncludingRemoved(0, 10);
        assertEquals(2, allIncluding.size(), "Should have 2 books total including sold");
    }

    @Test
    void testMultipleSellerBooks() throws SQLException {
        // 创建多个卖家的多本书
        for (int i = 1; i <= 5; i++) {
            bookDao.save(new Book("Seller1 Book " + i, "Author " + i, 
                    new BigDecimal("50.00"), "Desc " + i, "", 1L));
            bookDao.save(new Book("Seller2 Book " + i, "Author " + i, 
                    new BigDecimal("60.00"), "Desc " + i, "", 2L));
        }

        List<Book> seller1Books = bookDao.findBySellerId(1L);
        assertEquals(5, seller1Books.size());

        List<Book> seller2Books = bookDao.findBySellerId(2L);
        assertEquals(5, seller2Books.size());
    }
}
