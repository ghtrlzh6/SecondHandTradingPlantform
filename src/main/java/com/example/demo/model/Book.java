package com.example.demo.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Book {
    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private Long sellerId;
    private String status;
    private LocalDateTime createdAt;

    public Book() {
    }

    public Book(String title, String author, BigDecimal price, String description, String imageUrl, Long sellerId) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.sellerId = sellerId;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
