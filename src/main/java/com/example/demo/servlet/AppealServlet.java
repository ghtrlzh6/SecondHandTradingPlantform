package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NamingException;
import java.io.IOException;

public class AppealServlet extends HttpServlet {
    private UserService userService;
    private BookService bookService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            userService = ServiceFactory.getInstance().getUserService();
            bookService = ServiceFactory.getInstance().getBookService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理申诉请求，重定向到与管理员的消息页面
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null || orderIdParam.isEmpty()) {
            response.sendRedirect("orders");
            return;
        }

        try {
            long orderId = Long.parseLong(orderIdParam);
            
            // 获取管理员用户
            User admin = userService.findAdmin();
            if (admin == null) {
                response.sendRedirect("orders?error=noAdmin");
                return;
            }

            // 为了复用现有的消息系统，我们需要创建一个虚拟的书籍ID
            // 这里使用一个特殊的负数来标识申诉消息
            long appealBookId = -orderId; // 使用负数的订单ID作为申诉的书籍ID
            
            // 重定向到消息页面，与管理员进行对话
            response.sendRedirect(String.format("messages?bookId=%d&receiverId=%d&type=appeal&orderId=%d", 
                    appealBookId, admin.getId(), orderId));
            
        } catch (NumberFormatException e) {
            response.sendRedirect("orders");
        } catch (Exception e) {
            throw new ServletException("Error processing appeal", e);
        }
    }
}
