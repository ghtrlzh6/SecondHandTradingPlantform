package com.example.demo.dao;

import com.example.demo.model.Order;
import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order create(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findByBookId(Long bookId);
    List<Order> findAll();
    Order update(Order order);
    void delete(Long id);
}