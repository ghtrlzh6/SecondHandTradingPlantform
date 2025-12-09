<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    // 检查用户是否已登录
    if (session.getAttribute("user") == null) {
        response.sendRedirect("welcome.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Campus BookSwap - 二手书交易平台</title>
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

        /* 欢迎区域 */
        .welcome-section {
            text-align: center;
            color: white;
            margin-bottom: 3rem;
        }

        .welcome-section h1 {
            font-size: 3rem;
            margin-bottom: 1rem;
            text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
            animation: fadeInDown 0.8s ease;
        }

        .welcome-section p {
            font-size: 1.2rem;
            opacity: 0.9;
            animation: fadeInUp 0.8s ease;
        }

        /* 搜索区域 */
        .search-section {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 3rem;
            animation: fadeIn 1s ease;
        }

        .search-form {
            display: flex;
            gap: 1rem;
            align-items: center;
            justify-content: center;
        }

        .search-form input[type="text"] {
            flex: 1;
            max-width: 500px;
            padding: 1rem 1.5rem;
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
            padding: 1rem 2rem;
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

        /* 功能卡片网格 */
        .features-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 2rem;
            margin-bottom: 3rem;
        }

        .feature-card {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            text-align: center;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
            animation: slideUp 0.8s ease;
            text-decoration: none;
            color: inherit;
        }

        .feature-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
        }

        .feature-icon {
            width: 60px;
            height: 60px;
            margin: 0 auto 1rem;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.5rem;
            color: white;
        }

        .feature-card h3 {
            margin-bottom: 0.5rem;
            color: #333;
        }

        .feature-card p {
            color: #666;
            font-size: 0.9rem;
        }

        /* API文档区域 */
        .api-section {
            background: white;
            border-radius: 15px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            animation: fadeIn 1.2s ease;
        }

        .api-section h3 {
            color: #333;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .api-links {
            display: flex;
            gap: 2rem;
            flex-wrap: wrap;
        }

        .api-links a {
            color: #2196F3;
            text-decoration: none;
            padding: 0.5rem 1rem;
            border: 1px solid #2196F3;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .api-links a:hover {
            background-color: #2196F3;
            color: white;
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

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(50px);
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

            .welcome-section h1 {
                font-size: 2rem;
            }

            .search-form {
                flex-direction: column;
            }

            .search-form input[type="text"] {
                max-width: 100%;
            }

            .features-grid {
                grid-template-columns: 1fr;
            }

            .api-links {
                flex-direction: column;
                gap: 1rem;
            }
        }
    </style>
</head>
<body>
    <!-- 头部导航 -->
    <header class="header">
        <nav class="navbar">
            <div class="logo">📚 Campus BookSwap</div>
            <div class="user-info">
                <span>👤 <%= ((com.example.demo.model.User)session.getAttribute("user")).getUsername() %></span>
                <a href="profile">我的主页</a>
                <a href="logout">退出登录</a>
            </div>
        </nav>
    </header>

    <!-- 主要内容 -->
    <main class="main-container">
        <!-- 欢迎区域 -->
        <section class="welcome-section">
            <h1>欢迎来到校园二手书交易平台!</h1>
            <p>让知识的价值在校园中流转，让每一本好书找到新的主人</p>
        </section>

        <!-- 搜索区域 -->
        <section class="search-section">
            <form action="search" method="get" class="search-form">
                <input type="text" name="keyword" placeholder="搜索你想要的书籍..." required>
                <input type="submit" value="🔍 搜索">
            </form>
        </section>

        <!-- 功能卡片区域 -->
        <section class="features-grid">
            <a href="books" class="feature-card">
                <div class="feature-icon">📖</div>
                <h3>浏览书籍</h3>
                <p>发现校园中的优质二手书籍，找到你需要的知识宝藏</p>
            </a>

            <a href="addBook" class="feature-card">
                <div class="feature-icon">➕</div>
                <h3>发布书籍</h3>
                <p>分享你的闲置书籍，让知识继续传递价值</p>
            </a>

            <a href="wallet" class="feature-card">
                <div class="feature-icon">💰</div>
                <h3>我的钱包</h3>
                <p>管理你的账户余额，安全便捷的交易体验</p>
            </a>

            <a href="orders" class="feature-card">
                <div class="feature-icon">📋</div>
                <h3>我的订单</h3>
                <p>查看交易记录，跟踪订单状态</p>
            </a>
        </section>

        <!-- API文档区域 -->
        <section class="api-section">
            <h3>🔧 开发者API</h3>
            <p style="margin-bottom: 1rem; color: #666;">本平台提供RESTful API供开发者使用，支持程序化访问：</p>
            <div class="api-links">
                <a href="api/books">📡 获取书籍API</a>
                <a href="swagger-ui/index.html">📚 Swagger文档</a>
            </div>
        </section>
    </main>
</body>
</html>
