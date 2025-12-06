package com.example.demo.model;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long bookId;
    private Long buyerId;
    private String shippingAddress;
    private String paymentPassword;
    private String status; // pending, paid, shipped, completed, cancelled
    private LocalDateTime orderedAt;

    public Order() {
        this.status = "pending";
        this.orderedAt = LocalDateTime.now();
    }

    public Order(Long bookId, Long buyerId, String shippingAddress, String paymentPassword) {
        this();
        this.bookId = bookId;
        this.buyerId = buyerId;
        this.shippingAddress = shippingAddress;
        this.paymentPassword = paymentPassword;
    }

    // Constants for order status
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PAID = "paid";
    public static final String STATUS_SHIPPED = "shipped";
    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_CANCELLED = "cancelled";

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentPassword() {
        return paymentPassword;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }
}