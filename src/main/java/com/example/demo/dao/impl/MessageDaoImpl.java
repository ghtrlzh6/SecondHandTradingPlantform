package com.example.demo.dao.impl;

import com.example.demo.dao.MessageDao;
import com.example.demo.model.Message;
import com.example.demo.model.Conversation;

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
    public Message findById(Long messageId) {
        String sql = "SELECT id, sender_id, receiver_id, book_id, content, sent_at, is_read FROM messages WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, messageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Message message = new Message();
                    message.setId(rs.getLong("id"));
                    message.setSenderId(rs.getLong("sender_id"));
                    message.setReceiverId(rs.getLong("receiver_id"));
                    message.setBookId(rs.getLong("book_id"));
                    message.setContent(rs.getString("content"));
                    message.setSentAt(rs.getTimestamp("sent_at").toLocalDateTime());
                    message.setRead(rs.getBoolean("is_read"));
                    return message;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询消息时发生错误", e);
        }
        return null;
    }

    @Override
    public Message create(Message message) {
        // 对于申诉消息（book_id为负数），需要特殊处理
        String sql;
        if (message.getBookId() < 0) {
            // 申诉消息：允许book_id为负数，不强制外键约束
            sql = "INSERT INTO messages (sender_id, receiver_id, book_id, content, sent_at, is_read) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            // 普通消息：验证book_id是否存在
            sql = "INSERT INTO messages (sender_id, receiver_id, book_id, content, sent_at, is_read) " +
                  "SELECT ?, ?, ?, ?, ?, ? FROM books WHERE id = ? LIMIT 1";
        }
        
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, message.getSenderId());
            stmt.setLong(2, message.getReceiverId());
            stmt.setLong(3, message.getBookId());
            stmt.setString(4, message.getContent());
            stmt.setTimestamp(5, Timestamp.valueOf(message.getSentAt()));
            stmt.setBoolean(6, message.getRead());
            
            // 如果是普通消息，需要设置book_id参数
            if (message.getBookId() >= 0) {
                stmt.setLong(7, message.getBookId());
            }

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                if (message.getBookId() < 0) {
                    throw new SQLException("创建申诉消息失败，没有行受到影响。");
                } else {
                    throw new SQLException("创建消息失败，书籍不存在或已被删除。");
                }
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

    @Override
    public List<Conversation> findUserConversations(Long userId) {
        // 修改SQL查询，支持申诉消息（负数book_id）
        String sql = "SELECT " +
                "m.book_id, " +
                "CASE " +
                "  WHEN m.book_id < 0 THEN '订单申诉' " +
                "  ELSE b.title " +
                "END as book_title, " +
                "CASE " +
                "  WHEN m.sender_id = ? THEN m.receiver_id " +
                "  ELSE m.sender_id " +
                "END as other_user_id, " +
                "CASE " +
                "  WHEN m.sender_id = ? THEN u_receiver.username " +
                "  ELSE u_sender.username " +
                "END as other_username, " +
                "m.id as last_message_id, " +
                "m.content as last_message_content, " +
                "m.sent_at as last_message_time, " +
                "CASE " +
                "  WHEN m.book_id < 0 THEN NULL " +
                "  ELSE b.seller_id " +
                "END as seller_id " +
                "FROM messages m " +
                "LEFT JOIN books b ON m.book_id = b.id " +
                "JOIN users u_sender ON m.sender_id = u_sender.id " +
                "JOIN users u_receiver ON m.receiver_id = u_receiver.id " +
                "WHERE m.id IN (" +
                "  SELECT MAX(id) " +
                "  FROM messages " +
                "  WHERE sender_id = ? OR receiver_id = ? " +
                "  GROUP BY book_id, " +
                "    CASE " +
                "      WHEN sender_id = ? THEN receiver_id " +
                "      ELSE sender_id " +
                "    END" +
                ") " +
                "AND (m.sender_id = ? OR m.receiver_id = ?) " +
                "ORDER BY m.sent_at DESC";

        List<Conversation> conversations = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 设置参数
            stmt.setLong(1, userId);
            stmt.setLong(2, userId);
            stmt.setLong(3, userId);
            stmt.setLong(4, userId);
            stmt.setLong(5, userId);
            stmt.setLong(6, userId);
            stmt.setLong(7, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Conversation conversation = new Conversation();
                    conversation.setBookId(rs.getLong("book_id"));
                    conversation.setBookTitle(rs.getString("book_title"));
                    conversation.setOtherUserId(rs.getLong("other_user_id"));
                    conversation.setOtherUsername(rs.getString("other_username"));
                    conversation.setLastMessageId(rs.getLong("last_message_id"));
                    conversation.setLastMessageContent(rs.getString("last_message_content"));
                    conversation.setLastMessageTime(rs.getTimestamp("last_message_time").toLocalDateTime());
                    
                    // 判断对方是否是卖家（仅对普通消息有效）
                    Long sellerId = rs.getLong("seller_id");
                    if (!rs.wasNull()) {
                        Long otherUserId = rs.getLong("other_user_id");
                        conversation.setOtherUserSeller(sellerId.equals(otherUserId));
                    } else {
                        // 申诉消息，对方是管理员
                        conversation.setOtherUserSeller(false);
                    }
                    
                    // 查询未读消息数量
                    int unreadCount = getUnreadCountForConversation(conn, userId, conversation.getBookId(), conversation.getOtherUserId());
                    conversation.setUnreadCount(unreadCount);
                    
                    conversations.add(conversation);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("查询用户对话列表时发生错误", e);
        }
        return conversations;
    }

    private int getUnreadCountForConversation(Connection conn, Long userId, Long bookId, Long otherUserId) throws SQLException {
        String sql = "SELECT COUNT(*) as unread_count " +
                "FROM messages " +
                "WHERE receiver_id = ? AND book_id = ? AND sender_id = ? AND is_read = FALSE";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, bookId);
            stmt.setLong(3, otherUserId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("unread_count");
                }
            }
        }
        return 0;
    }
}
