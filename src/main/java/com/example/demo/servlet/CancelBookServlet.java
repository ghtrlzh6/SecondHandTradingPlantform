package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class CancelBookServlet extends HttpServlet {
    private BookService bookService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            bookService = ServiceFactory.getInstance().getBookService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 检查用户是否已登录
            if (request.getSession().getAttribute("user") == null) {
                response.sendRedirect("login");
                return;
            }

            String bookIdParam = request.getParameter("bookId");
            if (bookIdParam == null || bookIdParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID");
                return;
            }

            Long bookId;
            try {
                bookId = Long.parseLong(bookIdParam);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
                return;
            }

            // 获取书籍信息
            Book book = bookService.getBookById(bookId);
            if (book == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

            // 检查当前用户是否是书籍的发布者
            com.example.demo.model.User currentUser = (com.example.demo.model.User) request.getSession().getAttribute("user");
            if (!currentUser.getId().equals(book.getSellerId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "You can only cancel your own books");
                return;
            }

            // 检查书籍状态，只有可售状态的书籍才能取消发布
            if (!"available".equals(book.getStatus())) {
                request.getSession().setAttribute("errorMessage", "只有可售状态的书籍才能取消发布");
                response.sendRedirect("book?id=" + bookId);
                return;
            }

            // 取消发布书籍（将状态设置为取消发布）
            book.setStatus("cancelled");
            bookService.updateBook(book);

            // 设置成功消息
            request.getSession().setAttribute("successMessage", "书籍已成功下架");
            
            // 重定向到用户主页
            response.sendRedirect("profile");
        } catch (SQLException e) {
            throw new ServletException("Database error while cancelling book", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 对于GET请求，重定向到书籍详情页
        String bookIdParam = request.getParameter("bookId");
        if (bookIdParam != null && !bookIdParam.isEmpty()) {
            response.sendRedirect("book?id=" + bookIdParam);
        } else {
            response.sendRedirect("books");
        }
    }
}
