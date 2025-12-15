package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

/**
 * 统一API响应格式
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private int code;
    private LocalDateTime timestamp;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, T data, int code) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "操作成功", data, 200);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, 200);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(false, message, null, code);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, 500);
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return new ApiResponse<>(false, message, null, 400);
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return new ApiResponse<>(false, message, null, 401);
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return new ApiResponse<>(false, message, null, 403);
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return new ApiResponse<>(false, message, null, 404);
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
