package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class AddBookServlet extends HttpServlet {
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
        // 显示添加书籍页面
        request.getRequestDispatcher("/add-book.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取表单参数
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String priceStr = request.getParameter("price");
            String description = request.getParameter("description");
            String imageUrl = request.getParameter("imageUrl");
            
            // 获取当前登录用户ID（从session中获取）
            Long sellerId = (Long) request.getSession().getAttribute("userId");
            if (sellerId == null) {
                // 用户未登录，重定向到登录页面
                response.sendRedirect("login");
                return;
            }
            
            // 参数验证
            if (title == null || title.trim().isEmpty() ||
                author == null || author.trim().isEmpty() ||
                priceStr == null || priceStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "书名、作者和价格不能为空");
                request.getRequestDispatcher("/add-book.jsp").forward(request, response);
                return;
            }
            
            BigDecimal price;
            try {
                price = new BigDecimal(priceStr);
                if (price.compareTo(BigDecimal.ZERO) <= 0) {
                    request.setAttribute("errorMessage", "价格必须大于0");
                    request.getRequestDispatcher("/add-book.jsp").forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "价格格式不正确");
                request.getRequestDispatcher("/add-book.jsp").forward(request, response);
                return;
            }
            
            // 添加书籍
            Long bookId = bookService.addBook(title, author, price, description, imageUrl, sellerId);
            
            if (bookId != null) {
                // 添加成功，重定向到书籍详情页面
                response.sendRedirect("book?id=" + bookId);
            } else {
                // 添加失败
                request.setAttribute("errorMessage", "添加书籍失败，请稍后重试");
                request.getRequestDispatcher("/add-book.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error while adding book", e);
        }
    }
}