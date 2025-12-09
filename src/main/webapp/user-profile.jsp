<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.example.demo.model.UserStats" %>
<%@ page import="com.example.demo.model.User" %>
<%@ page import="com.example.demo.model.Book" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.util.List" %>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("welcome.jsp");
        return;
    }
    
    UserStats userStats = (UserStats) request.getAttribute("userStats");
    if (userStats == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    User user = userStats.getUser();
    List<Book> userBooks = userStats.getUserBooks();
    List<Book> soldBooks = userStats.getSoldBooks();
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户主页 - <%= user.getUsername() %></title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            color: #333;
        }

        /* 头部导航栏 */
        .header {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            box-shadow: 0 2px 20px rgba(0, 0, 0, 0.1);
            padding: 0;
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .navbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 1rem 2rem;
            max-width: 1200px;
            margin: 0 auto;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: bold;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .nav-links {
            display: flex;
            gap: 1rem;
        }

        .nav-links a {
            color: #666;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .nav-links a:hover {
            background-color: #f0f0f0;
            color: #333;
        }

        .nav-links a.active {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 1rem;
            color: #666;
        }

        .user-info a {
            color: #e74c3c;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border: 1px solid #e74c3c;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .user-info a:hover {
            background-color: #e74c3c;
            color: white;
        }

        /* 主要内容区域 */
        .main-container {
            max-width: 1200px;
            margin: 2rem auto;
            padding: 0 2rem;
        }

        /* 用户信息卡片 */
        .profile-header {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: fadeInDown 0.8s ease;
        }

        .profile-info {
            display: flex;
            align-items: center;
            gap: 2rem;
        }

        .avatar {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2.5rem;
            color: white;
            font-weight: bold;
        }

        .user-details h1 {
            color: #333;
            margin-bottom: 0.5rem;
            font-size: 2rem;
        }

        .user-details p {
            color: #666;
            margin-bottom: 0.3rem;
        }

        .rating {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            margin-top: 0.5rem;
        }

        .stars {
            color: #ffc107;
            font-size: 1.2rem;
        }

        /* 统计数据网格 */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .stat-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            text-align: center;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            animation: fadeInUp 0.8s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
        }

        .stat-icon {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .stat-number {
            font-size: 2rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 0.3rem;
        }

        .stat-label {
            color: #666;
            font-size: 0.9rem;
        }

        /* 书籍列表 */
        .books-section {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: fadeIn 1s ease;
        }

        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding-bottom: 1rem;
            border-bottom: 2px solid #f0f0f0;
        }

        .section-title {
            font-size: 1.5rem;
            color: #333;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .books-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 1.5rem;
        }

        .book-card {
            border: 1px solid #e0e0e0;
            border-radius: 10px;
            padding: 1rem;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .book-card:hover {
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            transform: translateY(-2px);
        }

        .book-title {
            font-weight: bold;
            color: #333;
            margin-bottom: 0.5rem;
            font-size: 1.1rem;
        }

        .book-author {
            color: #666;
            margin-bottom: 0.5rem;
            font-size: 0.9rem;
        }

        .book-price {
            color: #4CAF50;
            font-weight: bold;
            font-size: 1.2rem;
        }

        .book-date {
            color: #999;
            font-size: 0.8rem;
            margin-top: 0.5rem;
        }

        .empty-state {
            text-align: center;
            padding: 2rem;
            color: #666;
        }

        .empty-state-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }

        /* 动画效果 */
        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translateY(-30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                gap: 1rem;
            }

            .profile-info {
                flex-direction: column;
                text-align: center;
            }

            .stats-grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .books-grid {
                grid-template-columns: 1fr;
            }

            .section-header {
                flex-direction: column;
                gap: 1rem;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>
    <!-- 头部导航 -->
    <header class="header">
        <nav class="navbar">
            <div class="logo">📚 Campus BookSwap</div>
            <div class="nav-links">
                <a href="index.jsp">首页</a>
                <a href="profile" class="active">我的主页</a>
                <a href="books">浏览书籍</a>
                <a href="addBook">发布书籍</a>
                <a href="wallet">钱包</a>
                <a href="orders">订单</a>
            </div>
            <div class="user-info">
                <span>👤 <%= user.getUsername() %></span>
                <a href="logout">退出登录</a>
            </div>
        </nav>
    </header>

    <!-- 主要内容 -->
    <main class="main-container">
        <!-- 用户信息卡片 -->
        <section class="profile-header">
            <div class="profile-info">
                <div class="avatar">
                    <%= user.getUsername().toUpperCase().charAt(0) %>
                </div>
                <div class="user-details">
                    <h1><%= user.getUsername() %></h1>
                    <p>📧 <%= user.getEmail() %></p>
                    <p>📅 注册时间: <%= user.getCreatedAt().toLocalDate() %></p>
                    <div class="rating">
                        <span class="stars">
                            <%= getStarRating(user.getRating() != null ? user.getRating().doubleValue() : 5.0) %>
                        </span>
                        <span><%= String.format("%.1f", user.getRating() != null ? user.getRating().doubleValue() : 5.0) %></span>
                        <span>(<%= user.getTotalRatings() != null ? user.getTotalRatings() : 0 %> 次评分)</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- 统计数据 -->
        <section class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon">📚</div>
                <div class="stat-number"><%= userStats.getTotalBooksPosted() %></div>
                <div class="stat-label">发布书籍</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">✅</div>
                <div class="stat-number"><%= userStats.getBooksSold() %></div>
                <div class="stat-label">已售出</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">📖</div>
                <div class="stat-number"><%= userStats.getBooksAvailable() %></div>
                <div class="stat-label">在售中</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">💰</div>
                <div class="stat-number">¥<%= String.format("%.2f", userStats.getTotalEarnings() != null ? userStats.getTotalEarnings() : BigDecimal.ZERO) %></div>
                <div class="stat-label">总收入</div>
            </div>
        </section>

        <!-- 当前在售书籍 -->
        <section class="books-section">
            <div class="section-header">
                <h2 class="section-title">
                    <span>📖</span>
                    <span>当前在售书籍 (<%= userStats.getBooksAvailable() %>)</span>
                </h2>
                <a href="addBook" style="color: #4CAF50; text-decoration: none;">+ 发布新书籍</a>
            </div>
            <% if (userBooks != null && !userBooks.isEmpty()) { %>
                <div class="books-grid">
                    <% for (Book book : userBooks) { %>
                        <div class="book-card" onclick="window.location.href='book?id=<%= book.getId() %>'">
                            <div class="book-title"><%= book.getTitle() %></div>
                            <div class="book-author">作者: <%= book.getAuthor() %></div>
                            <div class="book-price">¥<%= String.format("%.2f", book.getPrice()) %></div>
                            <div class="book-date">发布于 <%= book.getCreatedAt().toLocalDate() %></div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📚</div>
                    <p>暂无在售书籍</p>
                    <p>点击上方"发布新书籍"开始出售</p>
                </div>
            <% } %>
        </section>

        <!-- 已售出书籍 -->
        <section class="books-section">
            <div class="section-header">
                <h2 class="section-title">
                    <span>✅</span>
                    <span>已售出书籍 (<%= userStats.getBooksSold() %>)</span>
                </h2>
            </div>
            <% if (soldBooks != null && !soldBooks.isEmpty()) { %>
                <div class="books-grid">
                    <% for (Book book : soldBooks) { %>
                        <div class="book-card" onclick="window.location.href='book?id=<%= book.getId() %>'">
                            <div class="book-title"><%= book.getTitle() %></div>
                            <div class="book-author">作者: <%= book.getAuthor() %></div>
                            <div class="book-price">¥<%= String.format("%.2f", book.getPrice()) %></div>
                            <div style="color: #28a745; font-size: 0.9rem; margin-top: 0.5rem;">✓ 已售出</div>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <div class="empty-state">
                    <div class="empty-state-icon">📦</div>
                    <p>暂无已售出书籍</p>
                </div>
            <% } %>
        </section>
    </main>
</body>
</html>

<%!
    private String getStarRating(double rating) {
        int fullStars = (int) rating;
        int halfStar = (rating % 1 >= 0.5) ? 1 : 0;
        int emptyStars = 5 - fullStars - halfStar;
        
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append("⭐");
        }
        if (halfStar > 0) {
            stars.append("✨");
        }
        for (int i = 0; i < emptyStars; i++) {
            stars.append("☆");
        }
        return stars.toString();
    }
%>
