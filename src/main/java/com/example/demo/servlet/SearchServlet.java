package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.service.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class SearchServlet extends HttpServlet {
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
            // 获取搜索关键词
            String keyword = request.getParameter("keyword");
            if (keyword == null || keyword.trim().isEmpty()) {
                // 如果没有关键词，重定向到书籍列表页面
                response.sendRedirect("books");
                return;
            }
            
            // 获取分页参数
            int page = 1;
            int pageSize = 10;
            
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                }
            } catch (NumberFormatException e) {
                // 如果页码格式不正确，默认为第一页
                page = 1;
            }
            
            // 搜索书籍
            BookService.BookPageResult result = bookService.searchBooks(keyword, page, pageSize);
            
            // 设置request属性
            request.setAttribute("books", result.getBooks());
            request.setAttribute("currentPage", result.getCurrentPage());
            request.setAttribute("totalPages", result.getTotalPages());
            request.setAttribute("totalCount", result.getTotalCount());
            request.setAttribute("hasNextPage", result.hasNextPage());
            request.setAttribute("hasPreviousPage", result.hasPreviousPage());
            request.setAttribute("nextPage", result.getNextPage());
            request.setAttribute("previousPage", result.getPreviousPage());
            request.setAttribute("keyword", keyword);
            
            // 转发到JSP页面
            request.getRequestDispatcher("/book-list.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Database error while searching books", e);
        }
    }
}