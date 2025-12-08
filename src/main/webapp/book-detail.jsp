<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>书籍详情 - Campus BookSwap</title>
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
            max-width: 1200px;
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
            animation: fadeInDown 0.8s ease;
        }

        /* 书籍详情卡片 */
        .book-detail-card {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: slideUp 0.8s ease;
        }

        .book-content {
            display: grid;
            grid-template-columns: 1fr 2fr;
            gap: 2rem;
            align-items: start;
        }

        .book-image-section {
            text-align: center;
        }

        .book-image {
            width: 100%;
            max-width: 300px;
            height: auto;
            border-radius: 15px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
            transition: transform 0.3s ease;
        }

        .book-image:hover {
            transform: scale(1.05);
        }

        .no-image {
            width: 100%;
            max-width: 300px;
            height: 400px;
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            border-radius: 15px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 4rem;
            color: #999;
        }

        .book-info-section h3 {
            font-size: 2rem;
            color: #333;
            margin-bottom: 1.5rem;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .book-info-item {
            margin-bottom: 1rem;
            padding: 1rem;
            background: #f8f9fa;
            border-radius: 10px;
            border-left: 4px solid #4CAF50;
        }

        .book-info-item strong {
            color: #4CAF50;
            font-weight: 600;
            display: inline-block;
            margin-right: 0.5rem;
        }

        .book-price {
            font-size: 2rem;
            font-weight: bold;
            color: #e74c3c;
            margin-bottom: 1rem;
        }

        .book-description {
            background: #f8f9fa;
            padding: 1.5rem;
            border-radius: 10px;
            margin-bottom: 1rem;
            line-height: 1.6;
            color: #555;
        }

        .book-meta {
            display: flex;
            align-items: center;
            gap: 1rem;
            color: #666;
            font-size: 0.9rem;
        }

        /* 操作按钮区域 */
        .actions-section {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            text-align: center;
            animation: slideUp 1s ease;
        }

        .buy-form {
            display: inline-block;
        }

        .btn-buy {
            padding: 1rem 3rem;
            background: linear-gradient(45deg, #e74c3c, #c0392b);
            color: white;
            border: none;
            border-radius: 30px;
            font-size: 1.2rem;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(231, 76, 60, 0.3);
            text-decoration: none;
            display: inline-block;
        }

        .btn-buy:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(231, 76, 60, 0.4);
        }

        /* 导航链接 */
        .navigation {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
            animation: slideUp 1.2s ease;
        }

        .nav-links {
            display: flex;
            justify-content: center;
            gap: 2rem;
            flex-wrap: wrap;
        }

        .nav-links a {
            color: #2196F3;
            text-decoration: none;
            padding: 0.8rem 1.5rem;
            border: 1px solid #2196F3;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .nav-links a:hover {
            background-color: #2196F3;
            color: white;
            transform: translateY(-2px);
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

            .book-content {
                grid-template-columns: 1fr;
                gap: 1.5rem;
            }

            .book-info-section h3 {
                font-size: 1.5rem;
            }

            .book-price {
                font-size: 1.5rem;
            }

            .nav-links {
                flex-direction: column;
                gap: 1rem;
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
            <h2>📖 书籍详情</h2>
        </section>

        <!-- 书籍详情卡片 -->
        <section class="book-detail-card">
            <div class="book-content">
                <!-- 书籍图片 -->
                <div class="book-image-section">
                    <c:choose>
                        <c:when test="${not empty book.imageUrl}">
                            <img src="${book.imageUrl}" alt="${book.title}" class="book-image">
                        </c:when>
                        <c:otherwise>
                            <div class="no-image">📚</div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- 书籍信息 -->
                <div class="book-info-section">
                    <h3>${book.title}</h3>
                    
                    <div class="book-price">￥${book.price}</div>
                    
                    <div class="book-info-item">
                        <strong>👤 作者:</strong> ${book.author}
                    </div>
                    
                    <c:if test="${not empty book.description}">
                        <div class="book-description">
                            <strong>📝 书籍描述:</strong><br>
                            ${book.description}
                        </div>
                    </c:if>
                    
                    <div class="book-info-item">
                        <strong>📅 发布时间:</strong> ${book.createdAt}
                    </div>
                    
                    <div class="book-meta">
                        <span>🏷️ 二手书籍</span>
                        <span>📖 精品推荐</span>
                    </div>
                </div>
            </div>
        </section>

        <!-- 操作按钮区域 -->
        <section class="actions-section">
            <form action="buy" method="get" class="buy-form">
                <input type="hidden" name="bookId" value="${book.id}">
                <button type="submit" class="btn-buy">
                    🛒 立即购买
                </button>
            </form>
        </section>

        <!-- 导航链接 -->
        <section class="navigation">
            <div class="nav-links">
                <a href="books">📚 返回书籍列表</a>
                <a href="index.jsp">🏠 返回首页</a>
            </div>
        </section>
    </main>
</body>
</html>
