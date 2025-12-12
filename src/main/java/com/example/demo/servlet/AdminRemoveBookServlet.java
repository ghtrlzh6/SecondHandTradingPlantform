package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 管理员下架书籍Servlet
 */
public class AdminRemoveBookServlet extends HttpServlet {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 设置响应编码
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        // 检查用户是否已登录且为管理员
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            response.sendRedirect("login");
            return;
        }
        
        // 获取书籍ID
        String bookIdStr = request.getParameter("bookId");
        if (bookIdStr == null || bookIdStr.trim().isEmpty()) {
            request.setAttribute("error", "书籍ID不能为空");
            request.getRequestDispatcher("/books").forward(request, response);
            return;
        }
        
        try {
            Long bookId = Long.parseLong(bookIdStr);
            
            // 获取书籍信息
            Book book = bookService.getBookById(bookId);
            if (book == null) {
                request.setAttribute("error", "书籍不存在");
                request.getRequestDispatcher("/books").forward(request, response);
                return;
            }
            
            // 更新书籍状态为"removed"（已下架）
            book.setStatus("removed");
            int result = bookService.updateBook(book);
            
            if (result > 0) {
                // 重定向到书籍列表页面
                response.sendRedirect(request.getContextPath() + "/books");
            } else {
                request.setAttribute("error", "下架书籍失败，请重试");
                request.getRequestDispatcher("/books").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "无效的书籍ID");
            request.getRequestDispatcher("/books").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error while removing book", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 检查用户是否已登录且为管理员
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            response.sendRedirect("login");
            return;
        }
        
        // 对于GET请求，重定向到书籍列表页面
        response.sendRedirect(request.getContextPath() + "/books");
    }
}
