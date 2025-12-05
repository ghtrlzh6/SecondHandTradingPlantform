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

public class BookDetailServlet extends HttpServlet {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取书籍ID参数
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID");
                return;
            }
            
            Long bookId;
            try {
                bookId = Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
                return;
            }
            
            // 获取书籍详情
            Book book = bookService.getBookById(bookId);
            if (book == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }
            
            // 设置request属性
            request.setAttribute("book", book);
            
            // 转发到JSP页面
            request.getRequestDispatcher("/book-detail.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching book details", e);
        }
    }
}