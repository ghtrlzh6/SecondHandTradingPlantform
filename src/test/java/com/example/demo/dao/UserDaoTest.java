package com.example.demo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.dao.impl.UserDaoImpl;
import com.example.demo.model.User;

class UserDaoTest {

    private DataSource dataSource;
    private UserDao userDao;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // 使用H2内存数据库进行测试
        dataSource = createTestDataSource();
        userDao = new UserDaoImpl(dataSource);
        connection = dataSource.getConnection();
        createTestTables();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private DataSource createTestDataSource() {
        // 创建H2内存数据源，使用唯一的数据库名称
        org.h2.jdbcx.JdbcDataSource ds = new org.h2.jdbcx.JdbcDataSource();
        ds.setURL("jdbc:h2:mem:userTestDb;DB_CLOSE_DELAY=-1");
        ds.setUser("sa");
        ds.setPassword("");
        return ds;
    }

    private void createTestTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            // 先删除可能存在的表
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
        }
    }

    @Test
    void testSaveUser() throws SQLException {
        User user = new User("testuser", "password123", "test@example.com");
        user.setRating(java.math.BigDecimal.valueOf(4.5));
        user.setTotalRatings(10);
        user.setRole("user");

        Long userId = userDao.save(user);

        assertNotNull(userId);
        assertTrue(userId > 0);

        // 验证用户已保存
        Optional<User> savedUser = userDao.findByUsername("testuser");
        assertTrue(savedUser.isPresent());
        assertEquals("testuser", savedUser.get().getUsername());
        assertEquals("test@example.com", savedUser.get().getEmail());
    }

    @Test
    void testFindById() throws SQLException {
        // 先创建用户
        User user = new User("testuser", "password123", "test@example.com");
        Long userId = userDao.save(user);

        // 查找用户
        Optional<User> foundUser = userDao.findById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getId());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    void testFindByUsername() throws SQLException {
        // 先创建用户
        User user = new User("testuser", "password123", "test@example.com");
        userDao.save(user);

        // 查找用户
        Optional<User> foundUser = userDao.findByUsername("testuser");

        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByUsernameNotFound() throws SQLException {
        Optional<User> foundUser = userDao.findByUsername("nonexistent");
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testExistsByUsername() throws SQLException {
        // 先创建用户
        User user = new User("testuser", "password123", "test@example.com");
        userDao.save(user);

        // 测试存在的用户名
        assertTrue(userDao.existsByUsername("testuser"));

        // 测试不存在的用户名
        assertFalse(userDao.existsByUsername("nonexistent"));
    }

    @Test
    void testUpdateUserRating() throws SQLException {
        // 先创建用户
        User user = new User("testuser", "password123", "test@example.com");
        Long userId = userDao.save(user);

        // 更新评分
        userDao.updateUserRating(userId, 4.8, 15);

        // 验证评分已更新
        Optional<User> updatedUser = userDao.findById(userId);
        assertTrue(updatedUser.isPresent());
        // BigDecimal比较需要使用compareTo，因为4.8和4.80在BigDecimal中不equals
        assertEquals(0, java.math.BigDecimal.valueOf(4.8).compareTo(updatedUser.get().getRating()));
        assertEquals(15, updatedUser.get().getTotalRatings().intValue());
    }

    @Test
    void testFindAdmin() throws SQLException {
        // 创建普通用户
        userDao.save(new User("normaluser", "password123", "normal@example.com"));

        // 创建管理员用户 - 直接通过SQL插入以确保role被正确设置
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO users (username, password, email, role) VALUES ('admin', 'admin123', 'admin@example.com', 'admin')");
        }

        // 查找管理员
        Optional<User> foundAdmin = userDao.findAdmin();

        assertTrue(foundAdmin.isPresent(), "Should find the admin user");
        assertEquals("admin", foundAdmin.get().getUsername());
        assertEquals("admin", foundAdmin.get().getRole(), "User should have admin role");
    }

    @Test
    void testFindAdminNotFound() throws SQLException {
        // 只创建普通用户
        userDao.save(new User("normaluser", "password123", "normal@example.com"));

        // 查找管理员
        Optional<User> foundAdmin = userDao.findAdmin();

        assertFalse(foundAdmin.isPresent());
    }

    @Test
    void testSaveDuplicateUsername() throws SQLException {
        // 创建第一个用户
        userDao.save(new User("testuser", "password1", "user1@example.com"));

        // 尝试创建相同用户名的用户，应该抛出异常
        assertThrows(SQLException.class, () -> {
            userDao.save(new User("testuser", "password2", "user2@example.com"));
        });
    }

    @Test
    void testFindByIdNotFound() throws SQLException {
        Optional<User> foundUser = userDao.findById(999L);
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testMultipleUsers() throws SQLException {
        // 创建多个用户
        userDao.save(new User("user1", "password1", "user1@example.com"));
        userDao.save(new User("user2", "password2", "user2@example.com"));
        userDao.save(new User("user3", "password3", "user3@example.com"));

        // 验证每个用户都能找到
        assertTrue(userDao.existsByUsername("user1"));
        assertTrue(userDao.existsByUsername("user2"));
        assertTrue(userDao.existsByUsername("user3"));
        assertFalse(userDao.existsByUsername("user4"));
    }
}
