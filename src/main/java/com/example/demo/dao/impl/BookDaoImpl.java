package com.example.demo.dao.impl;

import com.example.demo.dao.BookDao;
import com.example.demo.model.Book;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpl implements BookDao {
    private final DataSource dataSource;

    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Long save(Book book) throws SQLException {
        String sql = "INSERT INTO books (title, author, price, description, image_url, seller_id) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setBigDecimal(3, book.getPrice());
            stmt.setString(4, book.getDescription());
            stmt.setString(5, book.getImageUrl());
            stmt.setLong(6, book.getSellerId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    book.setId(id);
                    return id;
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public Optional<Book> findById(Long id) throws SQLException {
        String sql = "SELECT id, title, author, price, description, image_url, seller_id, created_at FROM books WHERE id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPrice(rs.getBigDecimal("price"));
                    book.setDescription(rs.getString("description"));
                    book.setImageUrl(rs.getString("image_url"));
                    book.setSellerId(rs.getLong("seller_id"));
                    book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    return Optional.of(book);
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT id, title, author, price, description, image_url, seller_id, created_at FROM books ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPrice(rs.getBigDecimal("price"));
                    book.setDescription(rs.getString("description"));
                    book.setImageUrl(rs.getString("image_url"));
                    book.setSellerId(rs.getLong("seller_id"));
                    book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    books.add(book);
                }
                return books;
            }
        }
    }

    @Override
    public List<Book> findByKeyword(String keyword, int offset, int limit) throws SQLException {
        String sql = "SELECT id, title, author, price, description, image_url, seller_id, created_at FROM books " +
                     "WHERE title LIKE ? OR author LIKE ? ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPrice(rs.getBigDecimal("price"));
                    book.setDescription(rs.getString("description"));
                    book.setImageUrl(rs.getString("image_url"));
                    book.setSellerId(rs.getLong("seller_id"));
                    book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    books.add(book);
                }
                return books;
            }
        }
    }

    @Override
    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM books";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        }
    }

    @Override
    public int countByKeyword(String keyword) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM books WHERE title LIKE ? OR author LIKE ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchKeyword = "%" + keyword + "%";
            stmt.setString(1, searchKeyword);
            stmt.setString(2, searchKeyword);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count");
                }
                return 0;
            }
        }
    }

    @Override
    public List<Book> findBySellerId(Long sellerId) throws SQLException {
        String sql = "SELECT id, title, author, price, description, image_url, seller_id, created_at FROM books WHERE seller_id = ? ORDER BY created_at DESC";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, sellerId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                List<Book> books = new ArrayList<>();
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getLong("id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setPrice(rs.getBigDecimal("price"));
                    book.setDescription(rs.getString("description"));
                    book.setImageUrl(rs.getString("image_url"));
                    book.setSellerId(rs.getLong("seller_id"));
                    book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    books.add(book);
                }
                return books;
            }
        }
    }
}