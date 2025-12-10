package com.example.demo.model;

import java.time.LocalDateTime;

/**
 * 对话模型类
 * 用于表示两个用户关于某本书的对话
 */
public class Conversation {
    private Long bookId;
    private String bookTitle;
    private Long otherUserId;
    private String otherUsername;
    private Long lastMessageId;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;
    private int unreadCount;
    private boolean isOtherUserSeller;

    public Conversation() {}

    public Conversation(Long bookId, String bookTitle, Long otherUserId, String otherUsername,
                      Long lastMessageId, String lastMessageContent, LocalDateTime lastMessageTime,
                      int unreadCount, boolean isOtherUserSeller) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.otherUserId = otherUserId;
        this.otherUsername = otherUsername;
        this.lastMessageId = lastMessageId;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageTime = lastMessageTime;
        this.unreadCount = unreadCount;
        this.isOtherUserSeller = isOtherUserSeller;
    }

    // Getters and Setters
    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Long getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(Long otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUsername() {
        return otherUsername;
    }

    public void setOtherUsername(String otherUsername) {
        this.otherUsername = otherUsername;
    }

    public Long getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(Long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public boolean isOtherUserSeller() {
        return isOtherUserSeller;
    }

    public void setOtherUserSeller(boolean otherUserSeller) {
        isOtherUserSeller = otherUserSeller;
    }
}
