<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购买书籍 - Campus BookSwap</title>
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
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 2rem;
        }

        /* 页面标题 */
        .page-title {
            text-align: center;
            color: white;
            margin-bottom: 2rem;
        }

        .page-title h2 {
            font-size: 2.5rem;
            margin-bottom: 0.5rem;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
        }

        /* 书籍信息卡片 */
        .book-info-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: slideUp 0.6s ease;
        }

        .book-header {
            border-bottom: 2px solid #f0f0f0;
            padding-bottom: 1rem;
            margin-bottom: 1.5rem;
        }

        .book-title {
            font-size: 1.8rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 0.5rem;
        }

        .book-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 1rem;
        }

        .book-author {
            color: #666;
            font-size: 1.1rem;
        }

        .book-price {
            font-size: 1.5rem;
            font-weight: bold;
            color: #e74c3c;
        }

        .book-description {
            color: #555;
            line-height: 1.6;
            margin-top: 1rem;
        }

        /* 订单表单 */
        .order-form-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: slideUp 0.8s ease;
        }

        .form-title {
            font-size: 1.5rem;
            color: #333;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #333;
        }

        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 1rem;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
            font-family: inherit;
        }

        .form-group input:focus,
        .form-group textarea:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
        }

        .form-group textarea {
            height: 120px;
            resize: vertical;
        }

        .form-help {
            font-size: 0.9rem;
            color: #666;
            margin-top: 0.5rem;
        }

        .submit-section {
            text-align: center;
            margin-top: 2rem;
        }

        .btn-submit {
            padding: 1rem 3rem;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            border: none;
            border-radius: 30px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
        }

        .btn-submit:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
        }

        /* 操作链接 */
        .action-links {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
            animation: slideUp 1s ease;
        }

        .action-links a {
            color: #2196F3;
            text-decoration: none;
            margin: 0 1rem;
            padding: 0.5rem 1rem;
            border: 1px solid #2196F3;
            border-radius: 20px;
            transition: all 0.3s ease;
            display: inline-block;
        }

        .action-links a:hover {
            background-color: #2196F3;
            color: white;
        }

        /* 错误提示 */
        .error-message {
            background: #ffebee;
            color: #c62828;
            padding: 1rem;
            border-radius: 10px;
            margin-bottom: 1rem;
            border-left: 4px solid #c62828;
        }

        /* 动画效果 */
        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            .navbar {
                flex-direction: column;
                gap: 1rem;
            }

            .page-title h2 {
                font-size: 2rem;
            }

            .book-meta {
                flex-direction: column;
                align-items: flex-start;
            }

            .action-links a {
                display: block;
                margin: 0.5rem 0;
            }
        }
    </style>
</head>
<body>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login");
        return;
    }
%>

<!-- 头部导航 -->
<header class="header">
    <nav class="navbar">
        <div class="logo">📚 Campus BookSwap</div>
        <div class="user-info">
            <span>👤 <%= ((com.example.demo.model.User)session.getAttribute("user")).getUsername() %></span>
            <a href="logout">退出登录</a>
        </div>
    </nav>
</header>

<!-- 主要内容 -->
<main class="main-container">
    <!-- 页面标题 -->
    <section class="page-title">
        <h2>🛒 购买书籍</h2>
    </section>

    <!-- 错误提示 -->
    <c:if test="${not empty error}">
        <div class="error-message">
            ${error}
        </div>
    </c:if>

    <!-- 书籍信息 -->
    <section class="book-info-card">
        <div class="book-header">
            <div class="book-title">${book.title}</div>
            <div class="book-meta">
                <div class="book-author">作者：${book.author}</div>
                <div class="book-price">￥${book.price}</div>
            </div>
        </div>
        <div class="book-description">
            <strong>书籍描述：</strong>${book.description}
        </div>
    </section>

    <!-- 订单表单 -->
    <section class="order-form-card">
        <h3 class="form-title">📝 填写订单信息</h3>
        <form method="post" action="buy">
            <input type="hidden" name="bookId" value="${book.id}">
            
            <div class="form-group">
                <label for="shippingAddress">📍 收货地址：</label>
                <textarea id="shippingAddress" name="shippingAddress" required 
                          placeholder="请输入详细的收货地址，包括省市区、街道、门牌号等"></textarea>
            </div>
            
            <div class="form-group">
                <label for="paymentPassword">💳 支付密码：</label>
                <input type="password" id="paymentPassword" name="paymentPassword" 
                       placeholder="请输入支付密码（可选）">
                <div class="form-help">
                    💡 如果您尚未设置支付密码，可以留空，系统将直接从您的钱包余额中扣除
                </div>
            </div>
            
            <div class="submit-section">
                <button type="submit" class="btn-submit">确认购买</button>
            </div>
        </form>
    </section>

    <!-- 操作链接 -->
    <section class="action-links">
        <a href="book?id=${book.id}">📖 返回书籍详情</a>
        <a href="books">📚 返回书籍列表</a>
        <a href="index.jsp">🏠 返回首页</a>
    </section>
</main>
</body>
</html>
