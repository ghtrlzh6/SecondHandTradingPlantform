package com.example.demo.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 参数验证工具类
 */
public class ValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );
    
    /**
     * 验证字符串是否为空或空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        return !isBlank(email) && EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * 验证密码强度（6-100位）
     */
    public static boolean isValidPassword(String password) {
        return !isBlank(password) && password.length() >= 6 && password.length() <= 100;
    }
    
    /**
     * 验证用户名（1-20位字母数字下划线或中文）
     */
    public static boolean isValidUsername(String username) {
        if (isBlank(username)) return false;
        // 支持1-20位的字母、数字、下划线或中文字符
        return username.length() >= 1 && username.length() <= 20;
    }
    
    /**
     * 验证书名（1-200字符）
     */
    public static boolean isValidBookTitle(String title) {
        return !isBlank(title) && title.length() <= 200;
    }
    
    /**
     * 验证作者名（1-100字符）
     */
    public static boolean isValidAuthor(String author) {
        return !isBlank(author) && author.length() <= 100;
    }
    
    /**
     * 验证价格（正数，最大值999999.99，不包含）
     */
    public static boolean isValidPrice(BigDecimal price) {
        return price != null && price.compareTo(BigDecimal.ZERO) > 0 
               && price.compareTo(new BigDecimal("999999.99")) < 0;
    }
    
    /**
     * 验证页码
     */
    public static boolean isValidPage(Integer page) {
        return page != null && page >= 1;
    }
    
    /**
     * 验证页面大小
     */
    public static boolean isValidPageSize(Integer pageSize) {
        return pageSize != null && pageSize >= 1 && pageSize <= 100;
    }
    
    /**
     * 验证ID（正整数）
     */
    public static boolean isValidId(Long id) {
        return id != null && id > 0;
    }
}
