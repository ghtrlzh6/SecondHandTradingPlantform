package com.example.demo.service;

import com.example.demo.dao.MessageDao;
import com.example.demo.dao.impl.MessageDaoImpl;
import com.example.demo.model.Message;

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
     */
    public Message sendMessage(Long senderId, Long receiverId, Long bookId, String content) {
        Message message = new Message(senderId, receiverId, bookId, content);
        return messageDao.create(message);
    }

    /**
     * 获取用户的所有消息
     * @param userId 用户ID
     * @return 消息列表
     */
    public List<Message> getUserMessages(Long userId) {
        return messageDao.findByUserId(userId);
    }

    /**
     * 获取特定商品的交流记录
     * @param bookId 商品ID
     * @return 消息列表
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
     */
    public List<Message> getConversation(Long user1Id, Long user2Id, Long bookId) {
        return messageDao.findConversation(user1Id, user2Id, bookId);
    }

    /**
     * 标记消息为已读
     * @param messageId 消息ID
     * @return 更新后的消息
     */
    public Message markAsRead(Long messageId) {
        // 这里需要先获取消息，然后更新状态
        // 在实际实现中，我们可能需要一个getMessageById方法
        throw new UnsupportedOperationException("此方法需要在MessageDao中添加额外的方法支持");
    }

    /**
     * 获取用户的未读消息
     * @param userId 用户ID
     * @return 未读消息列表
     */
    public List<Message> getUnreadMessages(Long userId) {
        return messageDao.findUnreadMessages(userId);
    }

    /**
     * 删除消息
     * @param messageId 消息ID
     */
    public void deleteMessage(Long messageId) {
        messageDao.delete(messageId);
    }
}