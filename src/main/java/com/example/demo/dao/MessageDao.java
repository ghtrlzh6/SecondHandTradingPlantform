package com.example.demo.dao;

import com.example.demo.model.Message;
import java.util.List;

public interface MessageDao {
    Message create(Message message);
    List<Message> findByUserId(Long userId);
    List<Message> findByBookId(Long bookId);
    List<Message> findConversation(Long user1Id, Long user2Id, Long bookId);
    Message update(Message message);
    void delete(Long messageId);
    List<Message> findUnreadMessages(Long userId);
}