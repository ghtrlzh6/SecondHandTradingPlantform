package com.example.demo.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ValidationUtilTest {

    @Test
    void testValidUsername() {
        // 测试有效的用户名
        assertTrue(ValidationUtil.isValidUsername("testuser"));
        assertTrue(ValidationUtil.isValidUsername("user123"));
        assertTrue(ValidationUtil.isValidUsername("test_user"));
        assertTrue(ValidationUtil.isValidUsername("TestUser123"));
        assertTrue(ValidationUtil.isValidUsername("a"));
    }

    @Test
    void testInvalidUsername() {
        // 测试无效的用户名
        assertFalse(ValidationUtil.isValidUsername(null));
        assertFalse(ValidationUtil.isValidUsername(""));
        assertFalse(ValidationUtil.isValidUsername("abcdefghijklmnopqrstuvwxyz12345")); // 太长（超过20位）
    }

    @Test
    void testValidEmail() {
        // 测试有效的邮箱
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertTrue(ValidationUtil.isValidEmail("user.name@domain.co.uk"));
        assertTrue(ValidationUtil.isValidEmail("user+tag@example.org"));
        assertTrue(ValidationUtil.isValidEmail("user123@test-domain.com"));
    }

    @Test
    void testInvalidEmail() {
        // 测试无效的邮箱
        assertFalse(ValidationUtil.isValidEmail(null));
        assertFalse(ValidationUtil.isValidEmail(""));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
        assertFalse(ValidationUtil.isValidEmail("@example.com"));
        assertFalse(ValidationUtil.isValidEmail("user@"));
        assertFalse(ValidationUtil.isValidEmail("user@example"));
        assertFalse(ValidationUtil.isValidEmail("user@example."));
        assertFalse(ValidationUtil.isValidEmail("user name@example.com"));
        assertFalse(ValidationUtil.isValidEmail("user@.com"));
    }

    @Test
    void testValidPassword() {
        // 测试有效的密码
        assertTrue(ValidationUtil.isValidPassword("password123"));
        assertTrue(ValidationUtil.isValidPassword("SecurePass123!"));
        assertTrue(ValidationUtil.isValidPassword("MyPassword@2023"));
        assertTrue(ValidationUtil.isValidPassword("abcDEF123"));
    }

    @Test
    void testInvalidPassword() {
        // 测试无效的密码
        assertFalse(ValidationUtil.isValidPassword(null));
        assertFalse(ValidationUtil.isValidPassword(""));
        assertFalse(ValidationUtil.isValidPassword("123")); // 太短（少于6位）
        assertFalse(ValidationUtil.isValidPassword("12345")); // 5位，太短
    }

    @Test
    void testValidPrice() {
        // 测试有效的价格
        assertTrue(ValidationUtil.isValidPrice(java.math.BigDecimal.valueOf(99.99)));
        assertTrue(ValidationUtil.isValidPrice(java.math.BigDecimal.valueOf(0.01)));
        assertTrue(ValidationUtil.isValidPrice(java.math.BigDecimal.valueOf(1000.00)));
        assertTrue(ValidationUtil.isValidPrice(java.math.BigDecimal.valueOf(123.45)));
    }

    @Test
    void testInvalidPrice() {
        // 测试无效的价格
        assertFalse(ValidationUtil.isValidPrice(null));
        assertFalse(ValidationUtil.isValidPrice(java.math.BigDecimal.ZERO));
        assertFalse(ValidationUtil.isValidPrice(java.math.BigDecimal.valueOf(-10.00)));
        assertFalse(ValidationUtil.isValidPrice(java.math.BigDecimal.valueOf(999999.99))); // 超过上限
    }

    @Test
    void testValidTitle() {
        // 测试有效的标题
        assertTrue(ValidationUtil.isValidBookTitle("Java编程思想"));
        assertTrue(ValidationUtil.isValidBookTitle("设计模式"));
        assertTrue(ValidationUtil.isValidBookTitle("算法导论 第4版"));
        assertTrue(ValidationUtil.isValidBookTitle("a"));
    }

    @Test
    void testInvalidTitle() {
        // 测试无效的标题
        assertFalse(ValidationUtil.isValidBookTitle(null));
        assertFalse(ValidationUtil.isValidBookTitle(""));
        assertFalse(ValidationUtil.isValidBookTitle(generateLongString(201))); // 太长
    }

    @Test
    void testValidAuthor() {
        // 测试有效的作者
        assertTrue(ValidationUtil.isValidAuthor("Bruce Eckel"));
        assertTrue(ValidationUtil.isValidAuthor("Joshua Bloch"));
        assertTrue(ValidationUtil.isValidAuthor("a"));
    }

    @Test
    void testInvalidAuthor() {
        // 测试无效的作者
        assertFalse(ValidationUtil.isValidAuthor(null));
        assertFalse(ValidationUtil.isValidAuthor(""));
        assertFalse(ValidationUtil.isValidAuthor(generateLongString(101))); // 太长
    }

    @Test
    void testValidPage() {
        // 测试有效的页码
        assertTrue(ValidationUtil.isValidPage(1));
        assertTrue(ValidationUtil.isValidPage(10));
        assertTrue(ValidationUtil.isValidPage(100));
    }

    @Test
    void testInvalidPage() {
        // 测试无效的页码
        assertFalse(ValidationUtil.isValidPage(null));
        assertFalse(ValidationUtil.isValidPage(0));
        assertFalse(ValidationUtil.isValidPage(-1));
    }

    @Test
    void testValidPageSize() {
        // 测试有效的页面大小
        assertTrue(ValidationUtil.isValidPageSize(1));
        assertTrue(ValidationUtil.isValidPageSize(10));
        assertTrue(ValidationUtil.isValidPageSize(50));
        assertTrue(ValidationUtil.isValidPageSize(100));
    }

    @Test
    void testInvalidPageSize() {
        // 测试无效的页面大小
        assertFalse(ValidationUtil.isValidPageSize(null));
        assertFalse(ValidationUtil.isValidPageSize(0));
        assertFalse(ValidationUtil.isValidPageSize(-1));
        assertFalse(ValidationUtil.isValidPageSize(101));
    }

    @Test
    void testValidId() {
        // 测试有效的ID
        assertTrue(ValidationUtil.isValidId(1L));
        assertTrue(ValidationUtil.isValidId(100L));
        assertTrue(ValidationUtil.isValidId(999999L));
    }

    @Test
    void testInvalidId() {
        // 测试无效的ID
        assertFalse(ValidationUtil.isValidId(null));
        assertFalse(ValidationUtil.isValidId(0L));
        assertFalse(ValidationUtil.isValidId(-1L));
    }

    @Test
    void testEdgeCases() {
        // 测试边界情况
        assertTrue(ValidationUtil.isValidUsername("a")); // 最小长度1位
        assertTrue(ValidationUtil.isValidBookTitle("a")); // 最小长度
        assertTrue(ValidationUtil.isValidAuthor("a")); // 最小长度
        
        // 用户名、书名和作者名都支持最小1个字符
        assertTrue(ValidationUtil.isValidUsername("ab"));
        assertTrue(ValidationUtil.isValidBookTitle("ab"));
        assertTrue(ValidationUtil.isValidAuthor("ab"));
    }

    @Test
    void testSpecialCharacters() {
        // 测试特殊字符处理 - 新的验证逻辑允许所有字符，只检查长度
        assertTrue(ValidationUtil.isValidUsername("user@test"));
        assertTrue(ValidationUtil.isValidUsername("user#name"));
        assertTrue(ValidationUtil.isValidUsername("user name"));
    }

    @Test
    void testUnicodeCharacters() {
        // 测试Unicode字符
        assertTrue(ValidationUtil.isValidUsername("用户名")); // 中文字符
        assertTrue(ValidationUtil.isValidBookTitle("书籍标题")); // 中文字符
        assertTrue(ValidationUtil.isValidAuthor("作者名字")); // 中文字符
        // Email验证使用正则表达式，不支持中文域名部分
        assertFalse(ValidationUtil.isValidEmail("测试@example.com")); // 中文用户名部分不符合标准邮箱格式
        assertTrue(ValidationUtil.isValidEmail("test@example.com")); // 标准邮箱格式
    }

    /**
     * 生成指定长度的字符串用于测试
     */
    private String generateLongString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("a");
        }
        return sb.toString();
    }
}
