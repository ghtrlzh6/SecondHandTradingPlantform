package com.example.demo.model;

import java.math.BigDecimal;
import java.util.List;

public class UserStats {
    private User user;
    private int totalBooksPosted;
    private int booksSold;
    private int booksAvailable;
    private BigDecimal totalEarnings;
    private List<Book> userBooks;
    private List<Book> soldBooks;

    public UserStats() {
    }

    public UserStats(User user) {
        this.user = user;
    }

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTotalBooksPosted() {
        return totalBooksPosted;
    }

    public void setTotalBooksPosted(int totalBooksPosted) {
        this.totalBooksPosted = totalBooksPosted;
    }

    public int getBooksSold() {
        return booksSold;
    }

    public void setBooksSold(int booksSold) {
        this.booksSold = booksSold;
    }

    public int getBooksAvailable() {
        return booksAvailable;
    }

    public void setBooksAvailable(int booksAvailable) {
        this.booksAvailable = booksAvailable;
    }

    public BigDecimal getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(BigDecimal totalEarnings) {
        this.totalEarnings = totalEarnings;
    }

    public List<Book> getUserBooks() {
        return userBooks;
    }

    public void setUserBooks(List<Book> userBooks) {
        this.userBooks = userBooks;
    }

    public List<Book> getSoldBooks() {
        return soldBooks;
    }

    public void setSoldBooks(List<Book> soldBooks) {
        this.soldBooks = soldBooks;
    }
}
