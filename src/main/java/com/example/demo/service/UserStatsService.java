package com.example.demo.service;

import com.example.demo.model.UserStats;
import com.example.demo.model.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserStatsService {
    /**
     * 获取用户统计信息
     *
     * @param userId 用户ID
     * @return 用户统计信息
     * @throws SQLException 数据库访问异常
     */
    Optional<UserStats> getUserStats(Long userId) throws SQLException;

    /**
     * 更新用户评分
     *
     * @param userId 用户ID
     * @param newRating 新评分 (1.0-5.0)
     * @throws SQLException 数据库访问异常
     */
    void updateUserRating(Long userId, double newRating) throws SQLException;

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     * @throws SQLException 数据库访问异常
     */
    Optional<User> getUserById(Long userId) throws SQLException;
}
