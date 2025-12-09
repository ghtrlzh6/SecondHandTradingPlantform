package com.example.demo.dao;

import com.example.demo.model.UserStats;
import com.example.demo.model.Book;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserStatsDao {
    /**
     * 获取用户统计信息
     *
     * @param userId 用户ID
     * @return 用户统计信息
     * @throws SQLException 数据库访问异常
     */
    Optional<UserStats> getUserStats(Long userId) throws SQLException;

    /**
     * 获取用户发布的书籍列表
     *
     * @param userId 用户ID
     * @return 用户发布的书籍列表
     * @throws SQLException 数据库访问异常
     */
    List<Book> getUserBooks(Long userId) throws SQLException;

    /**
     * 获取用户已售出的书籍列表
     *
     * @param userId 用户ID
     * @return 用户已售出的书籍列表
     * @throws SQLException 数据库访问异常
     */
    List<Book> getUserSoldBooks(Long userId) throws SQLException;

    /**
     * 获取用户发布的书籍总数
     *
     * @param userId 用户ID
     * @return 发布的书籍总数
     * @throws SQLException 数据库访问异常
     */
    int getTotalBooksPosted(Long userId) throws SQLException;

    /**
     * 获取用户已售出的书籍总数
     *
     * @param userId 用户ID
     * @return 已售出的书籍总数
     * @throws SQLException 数据库访问异常
     */
    int getBooksSoldCount(Long userId) throws SQLException;

    /**
     * 获取用户当前可用的书籍数量
     *
     * @param userId 用户ID
     * @return 当前可用的书籍数量
     * @throws SQLException 数据库访问异常
     */
    int getAvailableBooksCount(Long userId) throws SQLException;

    /**
     * 获取用户总收入
     *
     * @param userId 用户ID
     * @return 总收入
     * @throws SQLException 数据库访问异常
     */
    double getTotalEarnings(Long userId) throws SQLException;
}
