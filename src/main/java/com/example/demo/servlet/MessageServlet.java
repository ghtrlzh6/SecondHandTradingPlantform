package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Book;
import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
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

public class MessageServlet extends HttpServlet {
    private MessageService messageService;
    private BookService bookService;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            messageService = ServiceFactory.getInstance().getMessageService();
            bookService = ServiceFactory.getInstance().getBookService();
            userService = ServiceFactory.getInstance().getUserService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 显示消息交流页面
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String bookIdParam = request.getParameter("bookId");
        String type = request.getParameter("type");
        String orderIdParam = request.getParameter("orderId");
        
        if (bookIdParam == null || bookIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID");
            return;
        }

        try {
            Long bookId = Long.parseLong(bookIdParam);
            
            Book book;
            
            // 检查是否是申诉消息（bookId为负数或type为appeal）
            boolean isAppeal = ("appeal".equals(type) || bookId < 0);
            if (isAppeal) {
                // 创建一个虚拟的书籍对象用于申诉
                book = new Book();
                book.setId(bookId);
                book.setTitle("订单申诉");
                book.setAuthor("系统");
                book.setPrice(java.math.BigDecimal.ZERO);
                
                // 获取订单信息
                if (orderIdParam != null && !orderIdParam.isEmpty()) {
                    try {
                        Long orderId = Long.parseLong(orderIdParam);
                        book.setDescription("关于订单 #" + orderId + " 的申诉");
                    } catch (NumberFormatException e) {
                        book.setDescription("订单申诉");
                    }
                } else {
                    book.setDescription("订单申诉");
                }
            } else {
                // 正常的书籍消息
                book = bookService.getBookById(bookId);
                if (book == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                    return;
                }
            }

            // 获取与此书相关的消息
            List<Message> messages = messageService.getBookMessages(bookId);
            
            request.setAttribute("book", book);
            request.setAttribute("messages", messages);
            request.setAttribute("isAppeal", isAppeal);
            request.getRequestDispatcher("/messages.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching messages", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 发送消息
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String bookIdParam = request.getParameter("bookId");
        String content = request.getParameter("content");
        String receiverIdParam = request.getParameter("receiverId");
        String type = request.getParameter("type");

        if (bookIdParam == null || bookIdParam.isEmpty() || 
            content == null || content.isEmpty()) {
            response.sendRedirect("books");
            return;
        }

        try {
            Long bookId = Long.parseLong(bookIdParam);
            Long receiverId = Long.parseLong(receiverIdParam);
            
            // 发送消息
            messageService.sendMessage(user.getId(), receiverId, bookId, content);
            
            // 根据类型重定向回消息页面
            boolean isAppeal = ("appeal".equals(type) || bookId < 0);
            if (isAppeal) {
                response.sendRedirect("messages?bookId=" + bookId + "&type=appeal");
            } else {
                response.sendRedirect("messages?bookId=" + bookId);
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters");
        }
    }
}
