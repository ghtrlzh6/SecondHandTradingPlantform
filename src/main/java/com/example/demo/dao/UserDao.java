package com.example.demo.dao;

import com.example.demo.model.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao {
    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户对象的Optional包装
     * @throws SQLException 数据库访问异常
     */
    Optional<User> findByUsername(String username) throws SQLException;

    /**
     * 根据用户ID查找用户
     *
     * @param id 用户ID
     * @return 用户对象的Optional包装
     * @throws SQLException 数据库访问异常
     */
    Optional<User> findById(Long id) throws SQLException;

    /**
     * 保存新用户
     *
     * @param user 用户对象
     * @return 保存后的用户ID
     * @throws SQLException 数据库访问异常
     */
    Long save(User user) throws SQLException;

    /**
     * 根据用户名检查用户是否存在
     *
     * @param username 用户名
     * @return 如果存在返回true，否则返回false
     * @throws SQLException 数据库访问异常
     */
    boolean existsByUsername(String username) throws SQLException;

    /**
     * 更新用户评分
     *
     * @param userId 用户ID
     * @param newRating 新的评分
     * @param totalRatings 总评分次数
     * @throws SQLException 数据库访问异常
     */
    void updateUserRating(Long userId, double newRating, int totalRatings) throws SQLException;

    /**
     * 获取管理员用户
     *
     * @return 管理员用户对象，如果没有管理员则返回null
     * @throws SQLException 数据库访问异常
     */
    Optional<User> findAdmin() throws SQLException;
}
