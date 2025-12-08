<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>书籍列表 - Campus BookSwap</title>
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
        }

        /* 搜索区域 */
        .search-section {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
        }

        .search-form {
            display: flex;
            gap: 1rem;
            align-items: center;
            justify-content: center;
        }

        .search-form input[type="text"] {
            flex: 1;
            max-width: 400px;
            padding: 0.8rem 1.2rem;
            border: 2px solid #e0e0e0;
            border-radius: 30px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .search-form input[type="text"]:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
        }

        .search-form input[type="submit"] {
            padding: 0.8rem 1.5rem;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            border: none;
            border-radius: 30px;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
        }

        .search-form input[type="submit"]:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
        }

        /* 搜索结果信息 */
        .search-info {
            background: rgba(255, 255, 255, 0.9);
            border-radius: 10px;
            padding: 1rem;
            margin-bottom: 1.5rem;
            color: #333;
            text-align: center;
        }

        /* 书籍网格 */
        .books-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }

        .book-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            animation: slideUp 0.6s ease;
        }

        .book-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
        }

        .book-title {
            font-size: 1.2rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 0.5rem;
        }

        .book-author {
            color: #666;
            margin-bottom: 0.5rem;
        }

        .book-price {
            font-size: 1.3rem;
            font-weight: bold;
            color: #e74c3c;
            margin-bottom: 0.5rem;
        }

        .book-date {
            color: #999;
            font-size: 0.9rem;
            margin-bottom: 1rem;
        }

        .book-action {
            display: inline-block;
            padding: 0.6rem 1.2rem;
            background: linear-gradient(45deg, #2196F3, #1976D2);
            color: white;
            text-decoration: none;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .book-action:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(33, 150, 243, 0.3);
        }

        /* 分页 */
        .pagination {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
            margin-bottom: 2rem;
        }

        .pagination-info {
            color: #666;
            margin-bottom: 1rem;
        }

        .pagination-links {
            display: flex;
            justify-content: center;
            gap: 1rem;
            align-items: center;
        }

        .pagination a {
            padding: 0.5rem 1rem;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            text-decoration: none;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .pagination a:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
        }

        .current-page {
            padding: 0.5rem 1rem;
            background: linear-gradient(45deg, #2196F3, #1976D2);
            color: white;
            border-radius: 20px;
            font-weight: bold;
        }

        /* 返回首页按钮 */
        .back-home {
            text-align: center;
            margin-top: 2rem;
        }

        .back-home a {
            display: inline-block;
            padding: 1rem 2rem;
            background: linear-gradient(45deg, #9C27B0, #7B1FA2);
            color: white;
            text-decoration: none;
            border-radius: 30px;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(156, 39, 176, 0.3);
        }

        .back-home a:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(156, 39, 176, 0.4);
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

            .search-form {
                flex-direction: column;
            }

            .search-form input[type="text"] {
                max-width: 100%;
            }

            .books-grid {
                grid-template-columns: 1fr;
            }

            .pagination-links {
                flex-direction: column;
                gap: 0.5rem;
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
            <h2>📖 书籍列表</h2>
        </section>

        <!-- 搜索区域 -->
        <section class="search-section">
            <form action="search" method="get" class="search-form">
                <input type="text" name="keyword" placeholder="搜索你想要的书籍..." 
                       value="${not empty keyword ? keyword : ''}" required>
                <input type="submit" value="🔍 搜索">
            </form>
        </section>

        <!-- 搜索结果信息 -->
        <c:if test="${not empty keyword}">
            <div class="search-info">
                搜索"${keyword}"的结果，共找到<strong>${totalCount}</strong>条记录
            </div>
        </c:if>

        <!-- 书籍网格 -->
        <section class="books-grid">
            <c:forEach var="book" items="${books}">
                <div class="book-card">
                    <div class="book-title">${book.title}</div>
                    <div class="book-author">作者：${book.author}</div>
                    <div class="book-price">￥${book.price}</div>
                    <div class="book-date">发布时间：${book.createdAt}</div>
                    <a href="book?id=${book.id}" class="book-action">查看详情</a>
                </div>
            </c:forEach>
        </section>

        <!-- 分页 -->
        <section class="pagination">
            <div class="pagination-info">
                第 ${currentPage} 页，共 ${totalPages} 页，总计 ${totalCount} 条记录
            </div>
            <div class="pagination-links">
                <c:if test="${hasPreviousPage}">
                    <a href="?page=${previousPage}&keyword=${keyword}">⬅️ 上一页</a>
                </c:if>
                
                <span class="current-page">当前页</span>
                
                <c:if test="${hasNextPage}">
                    <a href="?page=${nextPage}&keyword=${keyword}">下一页 ➡️</a>
                </c:if>
            </div>
        </section>

        <!-- 返回首页 -->
        <div class="back-home">
            <a href="index.jsp">🏠 返回首页</a>
        </div>
    </main>
</body>
</html>
