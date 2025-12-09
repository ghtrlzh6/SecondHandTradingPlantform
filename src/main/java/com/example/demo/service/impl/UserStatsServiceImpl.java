package com.example.demo.service.impl;

import com.example.demo.dao.UserStatsDao;
import com.example.demo.dao.UserDao;
import com.example.demo.service.UserStatsService;
import com.example.demo.model.UserStats;
import com.example.demo.model.User;

import java.sql.SQLException;
import java.util.Optional;

public class UserStatsServiceImpl implements UserStatsService {
    private final UserStatsDao userStatsDao;
    private final UserDao userDao;

    public UserStatsServiceImpl(UserStatsDao userStatsDao, UserDao userDao) {
        this.userStatsDao = userStatsDao;
        this.userDao = userDao;
    }

    @Override
    public Optional<UserStats> getUserStats(Long userId) throws SQLException {
        return userStatsDao.getUserStats(userId);
    }

    @Override
    public void updateUserRating(Long userId, double newRating) throws SQLException {
        Optional<User> userOpt = userDao.findById(userId);
        if (!userOpt.isPresent()) {
            throw new SQLException("User not found with ID: " + userId);
        }

        User user = userOpt.get();
        int currentTotalRatings = user.getTotalRatings() != null ? user.getTotalRatings() : 0;
        double currentRating = user.getRating() != null ? user.getRating().doubleValue() : 5.0;

        // 计算新的平均评分
        int newTotalRatings = currentTotalRatings + 1;
        double newAverageRating = ((currentRating * currentTotalRatings) + newRating) / newTotalRatings;

        // 限制评分范围在1.0-5.0之间
        newAverageRating = Math.max(1.0, Math.min(5.0, newAverageRating));

        userDao.updateUserRating(userId, newAverageRating, newTotalRatings);
    }

    @Override
    public Optional<User> getUserById(Long userId) throws SQLException {
        return userDao.findById(userId);
    }
}
