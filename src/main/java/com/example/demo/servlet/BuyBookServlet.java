package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.Book;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.OrderService;
import com.example.demo.service.WalletService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

public class BuyBookServlet extends HttpServlet {
    private BookService bookService;
    private OrderService orderService;
    private WalletService walletService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            bookService = ServiceFactory.getInstance().getBookService();
            orderService = ServiceFactory.getInstance().getOrderService();
            walletService = ServiceFactory.getInstance().getWalletService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 显示购买确认页面
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String bookIdParam = request.getParameter("bookId");
        if (bookIdParam == null || bookIdParam.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing book ID");
            return;
        }

        try {
            Long bookId = Long.parseLong(bookIdParam);
            Book book = bookService.getBookById(bookId);
            if (book == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

            request.setAttribute("book", book);
            request.getRequestDispatcher("/buy-book.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching book details", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理购买请求
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        String bookIdParam = request.getParameter("bookId");
        String shippingAddress = request.getParameter("shippingAddress");
        String paymentPassword = request.getParameter("paymentPassword");

        if (bookIdParam == null || bookIdParam.isEmpty() || 
            shippingAddress == null || shippingAddress.isEmpty()) {
            response.sendRedirect("books");
            return;
        }

        try {
            Long bookId = Long.parseLong(bookIdParam);
            Book book = bookService.getBookById(bookId);
            if (book == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

            // 检查用户不能购买自己的书籍
            if (book.getSellerId().equals(user.getId())) {
                request.setAttribute("error", "不能购买自己发布的书籍");
                request.getRequestDispatcher("/buy-book.jsp").forward(request, response);
                return;
            }

            // 创建订单
            Order order = orderService.createOrder(bookId, user.getId(), shippingAddress, paymentPassword);
            
            // 执行支付
            boolean paid = orderService.payForOrder(order.getId(), book.getPrice(), user.getId(), book.getSellerId());
            
            // 重定向到订单确认页面或其他适当页面
            if (paid) {
                response.sendRedirect("orders?message=payment_success");
            } else {
                response.sendRedirect("orders?error=insufficient_balance");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID");
        } catch (SQLException e) {
            throw new ServletException("Database error while purchasing book", e);
        }
    }
}
