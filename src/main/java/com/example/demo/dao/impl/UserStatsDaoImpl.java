package com.example.demo.dao.impl;

import com.example.demo.dao.UserStatsDao;
import com.example.demo.dao.UserDao;
import com.example.demo.model.UserStats;
import com.example.demo.model.Book;
import com.example.demo.model.User;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserStatsDaoImpl implements UserStatsDao {
    private final DataSource dataSource;
    private final UserDao userDao;

    public UserStatsDaoImpl(DataSource dataSource, UserDao userDao) {
        this.dataSource = dataSource;
        this.userDao = userDao;
    }

    @Override
    public Optional<UserStats> getUserStats(Long userId) throws SQLException {
        Optional<User> userOpt = userDao.findById(userId);
        if (!userOpt.isPresent()) {
            return Optional.empty();
        }

        User user = userOpt.get();
        UserStats userStats = new UserStats(user);
        
        // 获取统计数据
        userStats.setTotalBooksPosted(getTotalBooksPosted(userId));
        userStats.setBooksSold(getBooksSoldCount(userId));
        userStats.setBooksAvailable(getAvailableBooksCount(userId));
        userStats.setTotalEarnings(BigDecimal.valueOf(getTotalEarnings(userId)));
        userStats.setUserBooks(getUserBooks(userId));
        userStats.setSoldBooks(getUserSoldBooks(userId));

        return Optional.of(userStats);
    }

    @Override
    public List<Book> getUserBooks(Long userId) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b WHERE b.seller_id = ? AND b.status = 'available' " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = mapResultSetToBook(rs);
                    books.add(book);
                }
            }
        }
        return books;
    }

    @Override
    public List<Book> getUserSoldBooks(Long userId) throws SQLException {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b " +
                     "WHERE b.seller_id = ? AND b.status = 'sold' " +
                     "ORDER BY b.created_at DESC";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = mapResultSetToBook(rs);
                    books.add(book);
                }
            }
        }
        return books;
    }

    @Override
    public int getTotalBooksPosted(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books WHERE seller_id = ?";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public int getBooksSoldCount(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books b " +
                     "WHERE b.seller_id = ? AND b.status = 'sold'";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public int getAvailableBooksCount(Long userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM books b " +
                     "WHERE b.seller_id = ? AND b.status = 'available'";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public double getTotalEarnings(Long userId) throws SQLException {
        String sql = "SELECT COALESCE(SUM(b.price), 0) FROM books b " +
                     "WHERE b.seller_id = ? AND b.status = 'sold'";
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPrice(rs.getBigDecimal("price"));
        book.setDescription(rs.getString("description"));
        book.setImageUrl(rs.getString("image_url"));
        book.setSellerId(rs.getLong("seller_id"));
        book.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return book;
    }
}
