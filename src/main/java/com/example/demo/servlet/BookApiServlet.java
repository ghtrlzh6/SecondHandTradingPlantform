package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.BookCreateRequest;
import com.example.demo.dto.PaginatedResponse;
import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.util.ValidationUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * 书籍API Servlet - 支持完整的RESTful操作
 */
public class BookApiServlet extends HttpServlet {
    private BookService bookService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            bookService = ServiceFactory.getInstance().getBookService();
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    /**
     * GET /api/books - 获取书籍列表（支持分页和搜索）
     * GET /api/books/{id} - 获取单本书籍详情
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.equals("/")) {
                // 获取书籍列表
                handleGetBooks(request, response);
            } else {
                // 获取单本书籍详情 /api/books/{id}
                handleGetBookById(request, response, pathInfo);
            }
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器内部错误");
        }
    }

    /**
     * POST /api/books - 创建新书籍
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            // 检查用户是否已登录
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
                return;
            }
            
            // 解析请求体
            BookCreateRequest bookRequest = objectMapper.readValue(request.getReader(), BookCreateRequest.class);
            
            // 验证输入参数
            String validationError = validateBookCreateRequest(bookRequest);
            if (validationError != null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, validationError);
                return;
            }
            
            // 保存书籍
            Long bookId = bookService.addBook(
                bookRequest.getTitle(),
                bookRequest.getAuthor(),
                bookRequest.getPrice(),
                bookRequest.getDescription(),
                bookRequest.getImageUrl(),
                currentUser.getId()
            );
            
            Book createdBook = bookService.getBookById(bookId);
            
            // 返回创建的书籍信息
            ApiResponse<Book> apiResponse = ApiResponse.success("书籍创建成功", createdBook);
            objectMapper.writeValue(response.getWriter(), apiResponse);
            
        } catch (SQLException e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据库错误：" + e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "请求格式错误");
        }
    }

    /**
     * PUT /api/books/{id} - 更新书籍信息
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "缺少书籍ID");
                return;
            }
            
            // 解析书籍ID
            Long bookId = parseBookId(pathInfo);
            if (bookId == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "无效的书籍ID");
                return;
            }
            
            // 检查用户权限
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
                return;
            }
            
            // 获取现有书籍信息
            Book existingBook = bookService.getBookById(bookId);
            if (existingBook == null) {
                sendError(response, HttpServletResponse.SC_NOT_FOUND, "书籍不存在");
                return;
            }
            
            // 检查是否为书籍所有者或管理员
            if (!existingBook.getSellerId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, "无权限修改此书籍");
                return;
            }
            
            // 解析更新数据
            BookCreateRequest updateRequest = objectMapper.readValue(request.getReader(), BookCreateRequest.class);
            
            // 验证输入参数
            String validationError = validateBookCreateRequest(updateRequest);
            if (validationError != null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, validationError);
                return;
            }
            
            // 更新书籍信息
            existingBook.setTitle(updateRequest.getTitle());
            existingBook.setAuthor(updateRequest.getAuthor());
            existingBook.setPrice(updateRequest.getPrice());
            existingBook.setDescription(updateRequest.getDescription());
            existingBook.setImageUrl(updateRequest.getImageUrl());
            
            // 保存更新
            int updateResult = bookService.updateBook(existingBook);
            Book updatedBook = bookService.getBookById(bookId);
            
            // 返回更新后的书籍信息
            ApiResponse<Book> apiResponse = ApiResponse.success("书籍更新成功", updatedBook);
            objectMapper.writeValue(response.getWriter(), apiResponse);
            
        } catch (SQLException e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据库错误：" + e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "请求格式错误");
        }
    }

    /**
     * DELETE /api/books/{id} - 删除书籍
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "缺少书籍ID");
                return;
            }
            
            // 解析书籍ID
            Long bookId = parseBookId(pathInfo);
            if (bookId == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "无效的书籍ID");
                return;
            }
            
            // 检查用户权限
            User currentUser = (User) request.getSession().getAttribute("user");
            if (currentUser == null) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "请先登录");
                return;
            }
            
            // 获取书籍信息
            Book existingBook = bookService.getBookById(bookId);
            if (existingBook == null) {
                sendError(response, HttpServletResponse.SC_NOT_FOUND, "书籍不存在");
                return;
            }
            
            // 检查是否为书籍所有者或管理员
            if (!existingBook.getSellerId().equals(currentUser.getId()) && !currentUser.isAdmin()) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, "无权限删除此书籍");
                return;
            }
            
            // 删除书籍 - 更新状态为已下架
            existingBook.setStatus("removed");
            bookService.updateBook(existingBook);
            
            // 返回成功响应
            ApiResponse<Void> apiResponse = ApiResponse.success("书籍删除成功", null);
            objectMapper.writeValue(response.getWriter(), apiResponse);
            
        } catch (SQLException e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "数据库错误：" + e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "请求格式错误");
        }
    }

    /**
     * 处理获取书籍列表请求
     */
    private void handleGetBooks(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        
        // 获取查询参数
        String keyword = request.getParameter("keyword");
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        
        // 设置默认值
        int page = 1;
        int size = 10;
        
        // 解析分页参数
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
                if (!ValidationUtil.isValidPage(page)) {
                    sendError(response, HttpServletResponse.SC_BAD_REQUEST, "无效的页码");
                    return;
                }
            } catch (NumberFormatException e) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "页码格式错误");
                return;
            }
        }
        
        if (sizeStr != null) {
            try {
                size = Integer.parseInt(sizeStr);
                if (!ValidationUtil.isValidPageSize(size)) {
                    sendError(response, HttpServletResponse.SC_BAD_REQUEST, "无效的页面大小");
                    return;
                }
            } catch (NumberFormatException e) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "页面大小格式错误");
                return;
            }
        }
        
        // 获取书籍列表
        BookService.BookPageResult result;
        if (keyword != null && !keyword.trim().isEmpty()) {
            result = bookService.searchBooks(keyword.trim(), page, size);
        } else {
            User currentUser = (User) request.getSession().getAttribute("user");
            boolean isAdmin = currentUser != null && currentUser.isAdmin();
            result = isAdmin ? 
                bookService.getAllBooksForAdmin(page, size) : 
                bookService.getAllBooks(page, size);
        }
        
        // 构建分页响应
        PaginatedResponse<Book> paginatedResponse = new PaginatedResponse<>(
            result.getBooks(),
            result.getCurrentPage(),
            size,
            result.getTotalCount()
        );
        
        // 返回响应
        ApiResponse<PaginatedResponse<Book>> apiResponse = ApiResponse.success(paginatedResponse);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * 处理获取单本书籍详情请求
     */
    private void handleGetBookById(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        
        Long bookId = parseBookId(pathInfo);
        if (bookId == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "无效的书籍ID");
            return;
        }
        
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, "书籍不存在");
            return;
        }
        
        ApiResponse<Book> apiResponse = ApiResponse.success(book);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }

    /**
     * 从路径中解析书籍ID
     */
    private Long parseBookId(String pathInfo) {
        try {
            String[] parts = pathInfo.split("/");
            if (parts.length >= 2) {
                return Long.parseLong(parts[1]);
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return null;
    }

    /**
     * 验证书籍创建请求
     */
    private String validateBookCreateRequest(BookCreateRequest request) {
        if (!ValidationUtil.isValidBookTitle(request.getTitle())) {
            return "书名不能为空且长度不能超过200字符";
        }
        if (!ValidationUtil.isValidAuthor(request.getAuthor())) {
            return "作者名不能为空且长度不能超过100字符";
        }
        if (!ValidationUtil.isValidPrice(request.getPrice())) {
            return "价格必须是大于0且不超过999999.99的数字";
        }
        // 描述和图片URL是可选的，不需要强制验证
        return null;
    }

    /**
     * 发送错误响应
     */
    private void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        ApiResponse<Void> apiResponse = ApiResponse.error(statusCode, message);
        response.setStatus(statusCode);
        objectMapper.writeValue(response.getWriter(), apiResponse);
    }
}
