package com.example.demo.servlet;

import com.example.demo.config.ServiceFactory;
import com.example.demo.model.User;
import com.example.demo.model.Wallet;
import com.example.demo.service.WalletService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NamingException;
import java.io.IOException;
import java.math.BigDecimal;

public class WalletServlet extends HttpServlet {
    private WalletService walletService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            walletService = ServiceFactory.getInstance().getWalletService();
        } catch (NamingException e) {
            throw new ServletException("Failed to initialize service factory", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 显示钱包页面
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        Wallet wallet = walletService.getWallet(user.getId());
        request.setAttribute("wallet", wallet);
        request.getRequestDispatcher("/wallet.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 处理钱包操作（充值/提现）
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        String amountStr = request.getParameter("amount");
        
        if (action == null || amountStr == null || amountStr.isEmpty()) {
            response.sendRedirect("wallet");
            return;
        }
        
        try {
            BigDecimal amount = new BigDecimal(amountStr);
            
            if ("deposit".equals(action)) {
                walletService.deposit(user.getId(), amount);
            } else if ("withdraw".equals(action)) {
                walletService.withdraw(user.getId(), amount);
            }
            
            response.sendRedirect("wallet");
        } catch (NumberFormatException e) {
            response.sendRedirect("wallet?error=invalid_amount");
        }
    }
}