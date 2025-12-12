package com.example.demo.dao;

import com.example.demo.model.Order;
import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order create(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findByBookId(Long bookId);
    List<Order> findAll();
    Order update(Order order);
    void delete(Long id);
    
    /**
     * 获取DataSource（用于Service层直接查询）
     * @return DataSource
     */
    DataSource getDataSource();
}
