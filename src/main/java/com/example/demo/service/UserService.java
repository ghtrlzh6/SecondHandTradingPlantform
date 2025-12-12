package com.example.demo.service;

import com.example.demo.dao.BookDao;
import com.example.demo.dao.UserDao;
import com.example.demo.model.Book;
import com.example.demo.model.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @return 注册成功返回true，否则返回false
     * @throws SQLException 数据库访问异常
     */
    public boolean register(String username, String password, String email) throws SQLException {
        // 检查用户名是否已存在
        if (userDao.existsByUsername(username)) {
            return false;
        }

        // 创建并保存新用户
        User user = new User(username, password, email);
        userDao.save(user);
        return true;
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，否则返回null
     * @throws SQLException 数据库访问异常
     */
    public User login(String username, String password) throws SQLException {
        Optional<User> userOpt = userDao.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // 实际项目中应该使用加密密码比较
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 根据ID查找用户
     *
     * @param id 用户ID
     * @return 用户对象
     * @throws SQLException 数据库访问异常
     */
    public User findById(Long id) throws SQLException {
        Optional<User> userOpt = userDao.findById(id);
        return userOpt.orElse(null);
    }

    /**
     * 获取管理员用户
     *
     * @return 管理员用户对象，如果没有管理员则返回null
     * @throws SQLException 数据库访问异常
     */
    public User findAdmin() throws SQLException {
        Optional<User> adminOpt = userDao.findAdmin();
        return adminOpt.orElse(null);
    }
}
