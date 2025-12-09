package com.example.demo.service;

import com.example.demo.dao.BookDao;
import com.example.demo.model.Book;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class BookService {
    private final BookDao bookDao;

    public BookService(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    /**
     * 发布新书
     *
     * @param title       书名
     * @param author      作者
     * @param price       价格
     * @param description 描述
     * @param imageUrl    图片URL
     * @param sellerId    卖家ID
     * @return 发布成功返回书籍ID，失败返回null
     * @throws SQLException 数据库访问异常
     */
    public Long addBook(String title, String author, BigDecimal price, String description, String imageUrl, Long sellerId) throws SQLException {
        Book book = new Book(title, author, price, description, imageUrl, sellerId);
        return bookDao.save(book);
    }

    /**
     * 获取所有书籍（分页）
     *
     * @param page     页码（从1开始）
     * @param pageSize 每页大小
     * @return 书籍列表和总数量的包装对象
     * @throws SQLException 数据库访问异常
     */
    public BookPageResult getAllBooks(int page, int pageSize) throws SQLException {
        int offset = (page - 1) * pageSize;
        List<Book> books = bookDao.findAll(offset, pageSize);
        int totalCount = bookDao.countAll();
        return new BookPageResult(books, totalCount, page, pageSize);
    }

    /**
     * 根据关键词搜索书籍（分页）
     *
     * @param keyword  关键词
     * @param page     页码（从1开始）
     * @param pageSize 每页大小
     * @return 书籍列表和总数量的包装对象
     * @throws SQLException 数据库访问异常
     */
    public BookPageResult searchBooks(String keyword, int page, int pageSize) throws SQLException {
        int offset = (page - 1) * pageSize;
        List<Book> books = bookDao.findByKeyword(keyword, offset, pageSize);
        int totalCount = bookDao.countByKeyword(keyword);
        return new BookPageResult(books, totalCount, page, pageSize);
    }

    /**
     * 根据ID获取书籍详情
     *
     * @param id 书籍ID
     * @return 书籍对象
     * @throws SQLException 数据库访问异常
     */
    public Book getBookById(Long id) throws SQLException {
        return bookDao.findById(id).orElse(null);
    }

    /**
     * 根据卖家ID获取其发布的书籍
     *
     * @param sellerId 卖家ID
     * @return 书籍列表
     * @throws SQLException 数据库访问异常
     */
    public List<Book> getBooksBySellerId(Long sellerId) throws SQLException {
        return bookDao.findBySellerId(sellerId);
    }

    /**
     * 更新书籍信息
     *
     * @param book 书籍对象
     * @return 影响的行数
     * @throws SQLException 数据库访问异常
     */
    public int updateBook(Book book) throws SQLException {
        return bookDao.update(book);
    }

    /**
     * 书籍分页结果包装类
     */
    public static class BookPageResult {
        private final List<Book> books;
        private final int totalCount;
        private final int currentPage;
        private final int pageSize;
        private final int totalPages;

        public BookPageResult(List<Book> books, int totalCount, int currentPage, int pageSize) {
            this.books = books;
            this.totalCount = totalCount;
            this.currentPage = currentPage;
            this.pageSize = pageSize;
            this.totalPages = (int) Math.ceil((double) totalCount / pageSize);
        }

        // Getters
        public List<Book> getBooks() {
            return books;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public boolean hasNextPage() {
            return currentPage < totalPages;
        }

        public boolean hasPreviousPage() {
            return currentPage > 1;
        }

        public int getNextPage() {
            return Math.min(currentPage + 1, totalPages);
        }

        public int getPreviousPage() {
            return Math.max(currentPage - 1, 1);
        }
    }
}
