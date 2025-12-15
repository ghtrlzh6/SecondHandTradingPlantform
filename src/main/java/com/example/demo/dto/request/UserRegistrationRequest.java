package com.example.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 用户注册请求模型
 */
@Schema(description = "用户注册请求")
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String email;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
