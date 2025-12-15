package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * API响应基础类
 */
@Schema(description = "标准API响应格式")
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int code;
    private long timestamp;

    public ApiResponse() {
        this.timestamp = System.currentTimeMillis();
    }

    public ApiResponse(boolean success, String message, T data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
