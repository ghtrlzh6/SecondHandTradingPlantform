package com.example.demo.service;

import com.example.demo.dao.MessageDao;
import com.example.demo.dao.impl.MessageDaoImpl;
import com.example.demo.model.Message;
import com.example.demo.model.Conversation;

import javax.sql.DataSource;
import java.util.List;

public class MessageService {
    private MessageDao messageDao;

    public MessageService(DataSource dataSource) {
        this.messageDao = new MessageDaoImpl(dataSource);
    }

    /**
     * 发送消息
     * @param senderId 发送者ID
     * @param receiverId 接收者ID
     * @param bookId 商品ID
     * @param content 消息内容
     * @return 发送的消息
     * @throws RuntimeException 数据库异常时抛出
     */
    public Message sendMessage(Long senderId, Long receiverId, Long bookId, String content) {
        Message message = new Message(senderId, receiverId, bookId, content);
        return messageDao.create(message);
    }

    /**
     * 获取用户的所有消息
     * @param userId 用户ID
     * @return 消息列表
     * @throws RuntimeException 数据库异常时抛出
     */
    public List<Message> getUserMessages(Long userId) {
        return messageDao.findByUserId(userId);
    }

    /**
     * 获取特定商品的交流记录
     * @param bookId 商品ID
     * @return 消息列表
     * @throws RuntimeException 数据库异常时抛出
     */
    public List<Message> getBookMessages(Long bookId) {
        return messageDao.findByBookId(bookId);
    }

    /**
     * 获取两个用户关于某个商品的对话
     * @param user1Id 用户1 ID
     * @param user2Id 用户2 ID
     * @param bookId 商品ID
     * @return 对话消息列表
     * @throws RuntimeException 数据库异常时抛出
     */
    public List<Message> getConversation(Long user1Id, Long user2Id, Long bookId) {
        return messageDao.findConversation(user1Id, user2Id, bookId);
    }

    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @return 更新后的消息
     * @throws RuntimeException 数据库异常时抛出
     */
    public Message markAsRead(Long messageId) {
        Message message = messageDao.findById(messageId);
        if (message != null) {
            message.setRead(true);
            return messageDao.update(message);
        }
        return null;
    }

    /**
     * 标记用户的所有未读消息为已读
     * @param userId 用户ID
     * @throws RuntimeException 数据库异常时抛出
     */
    public void markAllAsRead(Long userId) {
        List<Message> unreadMessages = getUnreadMessages(userId);
        for (Message message : unreadMessages) {
            markAsRead(message.getId());
        }
    }

    /**
     * 获取用户的未读消息数量
     * @param userId 用户ID
     * @return 未读消息数量
     * @throws RuntimeException 数据库异常时抛出
     */
    public int getUnreadMessageCount(Long userId) {
        return messageDao.findUnreadMessages(userId).size();
    }

    /**
     * 获取用户的未读消息
     * @param userId 用户ID
     * @return 未读消息列表
     * @throws RuntimeException 数据库异常时抛出
     */
    public List<Message> getUnreadMessages(Long userId) {
        return messageDao.findUnreadMessages(userId);
    }

    /**
     * 删除消息
     * @param messageId 消息ID
     * @throws RuntimeException 数据库异常时抛出
     */
    public void deleteMessage(Long messageId) {
        messageDao.delete(messageId);
    }

    /**
     * 获取用户的对话列表
     * @param userId 用户ID
     * @return 对话列表，按最后消息时间倒序排列
     * @throws RuntimeException 数据库异常时抛出
     */
    public List<Conversation> getUserConversations(Long userId) {
        return messageDao.findUserConversations(userId);
    }

    /**
     * 标记某个对话中的所有消息为已读
     * @param userId 当前用户ID
     * @param otherUserId 对话另一方用户ID
     * @param bookId 书籍ID
     * @throws RuntimeException 数据库异常时抛出
     */
    public void markConversationAsRead(Long userId, Long otherUserId, Long bookId) {
        List<Message> conversation = getConversation(userId, otherUserId, bookId);
        for (Message message : conversation) {
            if (!message.getRead() && message.getReceiverId().equals(userId)) {
                markAsRead(message.getId());
            }
        }
    }
}
