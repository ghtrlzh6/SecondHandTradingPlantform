package com.example.demo.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 成功响应模型
 */
@Schema(description = "成功响应模型")
public class SuccessResponse {
    private String message;

    public SuccessResponse() {
        this.message = "操作成功";
    }

    public SuccessResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
