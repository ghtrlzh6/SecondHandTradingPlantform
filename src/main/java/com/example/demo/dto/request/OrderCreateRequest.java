package com.example.demo.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 订单创建请求模型
 */
@Schema(description = "订单创建请求模型")
public class OrderCreateRequest {
    private Long bookId;
    private String shippingAddress;
    
    // Getters and setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
