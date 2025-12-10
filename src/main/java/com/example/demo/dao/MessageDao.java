package com.example.demo.dao;

import com.example.demo.model.Message;
import com.example.demo.model.Conversation;
import java.util.List;

public interface MessageDao {
    Message create(Message message);
    Message findById(Long messageId);
    List<Message> findByUserId(Long userId);
    List<Message> findByBookId(Long bookId);
    List<Message> findConversation(Long user1Id, Long user2Id, Long bookId);
    List<Conversation> findUserConversations(Long userId);
    Message update(Message message);
    void delete(Long messageId);
    List<Message> findUnreadMessages(Long userId);
}
