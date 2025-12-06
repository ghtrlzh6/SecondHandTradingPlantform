package com.example.demo.dao.impl;

import com.example.demo.dao.MessageDao;
import com.example.demo.model.Message;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    private DataSource dataSource;

    public MessageDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Message create(Message message) {
        String sql = "INSERT INTO messages (sender_id, receiver_id, book_id, content, sent_at, is_read) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, message.getSenderId());
            stmt.setLong(2, message.getReceiverId());
            stmt.setLong(3, message.getBookId());
            stmt.setString(4, message.getContent());
            stmt.setTimestamp(5, Timestamp.valueOf(message.getSentAt()));
            stmt.setBoolean(6, message.getRead());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("创建消息失败，没有行受到影响。");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("创建消息失败，没有获取到ID。");
                }
            }
            return message;
        } catch (SQLException e) {
            throw new RuntimeException("创建消息时发生错误", e);
        }
    }

    @Override
    public List<Message> findByUserId(Long userId) {
        String sql = "SELECT id, sender_id, receiver_id, book_id, content, sent_at, is_read FROM messages WHERE sender_id = ? OR receiver_id = ? ORDER BY sent_at ASC";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message();
                    message.setId(rs.getLong("id"));
                    message.setSenderId(rs.getLong("sender_id"));
                    message.setReceiverId(rs.getLong("receiver_id"));
                    message.setBookId(rs.getLong("book_id"));
                    message.setContent(rs.getString("content"));
                    message.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime());
                    message.setRead(rs.getBoolean("is_read"));
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询用户消息时发生错误", e);
        }
        return messages;
    }

    @Override
    public List<Message> findByBookId(Long bookId) {
        String sql = "SELECT id, sender_id, receiver_id, book_id, content, sent_at, is_read FROM messages WHERE book_id = ? ORDER BY sent_at ASC";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message();
                    message.setId(rs.getLong("id"));
                    message.setSenderId(rs.getLong("sender_id"));
                    message.setReceiverId(rs.getLong("receiver_id"));
                    message.setBookId(rs.getLong("book_id"));
                    message.setContent(rs.getString("content"));
                    message.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime());
                    message.setRead(rs.getBoolean("is_read"));
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询书籍相关消息时发生错误", e);
        }
        return messages;
    }

    @Override
    public List<Message> findConversation(Long user1Id, Long user2Id, Long bookId) {
        String sql = "SELECT id, sender_id, receiver_id, book_id, content, sent_at, is_read FROM messages " +
                "WHERE book_id = ? AND ((sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)) " +
                "ORDER BY sent_at ASC";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, bookId);
            stmt.setLong(2, user1Id);
            stmt.setLong(3, user2Id);
            stmt.setLong(4, user2Id);
            stmt.setLong(5, user1Id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message();
                    message.setId(rs.getLong("id"));
                    message.setSenderId(rs.getLong("sender_id"));
                    message.setReceiverId(rs.getLong("receiver_id"));
                    message.setBookId(rs.getLong("book_id"));
                    message.setContent(rs.getString("content"));
                    message.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime());
                    message.setRead(rs.getBoolean("is_read"));
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询对话时发生错误", e);
        }
        return messages;
    }

    @Override
    public Message update(Message message) {
        String sql = "UPDATE messages SET is_read = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, message.getRead());
            stmt.setLong(2, message.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("更新消息失败，消息可能不存在。");
            }
            return message;
        } catch (SQLException e) {
            throw new RuntimeException("更新消息时发生错误", e);
        }
    }

    @Override
    public void delete(Long messageId) {
        String sql = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, messageId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("删除消息时发生错误", e);
        }
    }

    @Override
    public List<Message> findUnreadMessages(Long userId) {
        String sql = "SELECT id, sender_id, receiver_id, book_id, content, sent_at, is_read FROM messages WHERE receiver_id = ? AND is_read = FALSE ORDER BY sent_at ASC";
        List<Message> messages = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Message message = new Message();
                    message.setId(rs.getLong("id"));
                    message.setSenderId(rs.getLong("sender_id"));
                    message.setReceiverId(rs.getLong("receiver_id"));
                    message.setBookId(rs.getLong("book_id"));
                    message.setContent(rs.getString("content"));
                    message.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime());
                    message.setRead(rs.getBoolean("is_read"));
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询未读消息时发生错误", e);
        }
        return messages;
    }
}