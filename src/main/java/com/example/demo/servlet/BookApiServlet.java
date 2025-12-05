package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Book;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BookApiServlet extends HttpServlet {
    private BookService bookService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            bookService = ServiceFactory.getInstance().getBookService();
            objectMapper = new ObjectMapper();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取所有书籍
            List<Book> books = bookService.getAllBooks(1, Integer.MAX_VALUE).getBooks();
            
            // 设置响应类型为JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            // 将书籍列表转换为JSON并输出
            objectMapper.writeValue(response.getWriter(), books);
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching books", e);
        }
    }
}