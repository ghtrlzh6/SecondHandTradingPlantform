package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Conversation;
import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class MessageListServlet extends HttpServlet {
    private MessageService messageService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            messageService = ServiceFactory.getInstance().getMessageService();
            userService = ServiceFactory.getInstance().getUserService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // 获取用户的对话列表
            List<Conversation> conversations = messageService.getUserConversations(user.getId());
            
            // 计算总的未读消息数量
            int totalUnreadCount = conversations.stream()
                    .mapToInt(Conversation::getUnreadCount)
                    .sum();
            
            request.setAttribute("conversations", conversations);
            request.setAttribute("unreadCount", totalUnreadCount);
            request.getRequestDispatcher("/message-list.jsp").forward(request, response);
            
        } catch (RuntimeException e) {
            throw new ServletException("Error while fetching conversations", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String action = request.getParameter("action");
        
        try {
            if ("markConversationAsRead".equals(action)) {
                String bookIdParam = request.getParameter("bookId");
                String otherUserIdParam = request.getParameter("otherUserId");
                if (bookIdParam != null && !bookIdParam.isEmpty() && 
                    otherUserIdParam != null && !otherUserIdParam.isEmpty()) {
                    Long bookId = Long.parseLong(bookIdParam);
                    Long otherUserId = Long.parseLong(otherUserIdParam);
                    messageService.markConversationAsRead(user.getId(), otherUserId, bookId);
                }
            } else if ("markAllAsRead".equals(action)) {
                messageService.markAllAsRead(user.getId());
            }
            
            // 重定向回消息列表
            response.sendRedirect("message-list");
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID parameter");
        } catch (RuntimeException e) {
            throw new ServletException("Error processing message action", e);
        } catch (Exception e) {
            throw new ServletException("Error processing message action", e);
        }
    }
}
