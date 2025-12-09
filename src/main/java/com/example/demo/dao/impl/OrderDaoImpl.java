package com.example.demo.dao.impl;

import com.example.demo.dao.OrderDao;
import com.example.demo.model.Order;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDaoImpl implements OrderDao {
    private DataSource dataSource;

    public OrderDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public Order create(Order order) {
        String sql = "INSERT INTO orders (book_id, buyer_id, shipping_address, payment_password, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, order.getBookId());
            stmt.setLong(2, order.getBuyerId());
            stmt.setString(3, order.getShippingAddress());
            stmt.setString(4, order.getPaymentPassword());
            stmt.setString(5, order.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("创建订单失败，没有行受到影响。");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("创建订单失败，没有获取到ID。");
                }
            }
            return order;
        } catch (SQLException e) {
            throw new RuntimeException("创建订单时发生错误", e);
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT o.id, o.book_id, o.buyer_id, o.shipping_address, o.payment_password, o.status, o.ordered_at, b.title, b.author, b.price FROM orders o LEFT JOIN books b ON o.book_id = b.id WHERE o.id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setBookId(rs.getLong("book_id"));
                    order.setBuyerId(rs.getLong("buyer_id"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setPaymentPassword(rs.getString("payment_password"));
                    order.setStatus(rs.getString("status"));
                    order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());
                    
                    // 设置书籍信息
                    order.setBookTitle(rs.getString("title"));
                    order.setBookAuthor(rs.getString("author"));
                    order.setBookPrice(rs.getBigDecimal("price"));
                    
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询订单时发生错误", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        String sql = "SELECT o.id, o.book_id, o.buyer_id, o.shipping_address, o.payment_password, o.status, o.ordered_at, b.title, b.author, b.price FROM orders o LEFT JOIN books b ON o.book_id = b.id WHERE o.buyer_id = ? ORDER BY o.ordered_at DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setBookId(rs.getLong("book_id"));
                    order.setBuyerId(rs.getLong("buyer_id"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setPaymentPassword(rs.getString("payment_password"));
                    order.setStatus(rs.getString("status"));
                    order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());
                    
                    // 设置书籍信息
                    order.setBookTitle(rs.getString("title"));
                    order.setBookAuthor(rs.getString("author"));
                    order.setBookPrice(rs.getBigDecimal("price"));
                    
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询用户订单时发生错误", e);
        }
        return orders;
    }

    @Override
    public List<Order> findByBookId(Long bookId) {
        String sql = "SELECT id, book_id, buyer_id, shipping_address, payment_password, status, ordered_at FROM orders WHERE book_id = ? ORDER BY ordered_at DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getLong("id"));
                    order.setBookId(rs.getLong("book_id"));
                    order.setBuyerId(rs.getLong("buyer_id"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setPaymentPassword(rs.getString("payment_password"));
                    order.setStatus(rs.getString("status"));
                    order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询商品订单时发生错误", e);
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT id, book_id, buyer_id, shipping_address, payment_password, status, ordered_at FROM orders ORDER BY ordered_at DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getLong("id"));
                order.setBookId(rs.getLong("book_id"));
                order.setBuyerId(rs.getLong("buyer_id"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setPaymentPassword(rs.getString("payment_password"));
                order.setStatus(rs.getString("status"));
                order.setOrderedAt(rs.getTimestamp("ordered_at").toLocalDateTime());
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询所有订单时发生错误", e);
        }
        return orders;
    }

    @Override
    public Order update(Order order) {
        String sql = "UPDATE orders SET book_id = ?, buyer_id = ?, shipping_address = ?, payment_password = ?, status = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, order.getBookId());
            stmt.setLong(2, order.getBuyerId());
            stmt.setString(3, order.getShippingAddress());
            stmt.setString(4, order.getPaymentPassword());
            stmt.setString(5, order.getStatus());
            stmt.setLong(6, order.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("更新订单失败，订单可能不存在。");
            }
            return order;
        } catch (SQLException e) {
            throw new RuntimeException("更新订单时发生错误", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM orders WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("删除订单时发生错误", e);
        }
    }
}
