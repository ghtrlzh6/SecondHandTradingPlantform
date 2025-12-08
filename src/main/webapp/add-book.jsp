<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>发布书籍 - Campus BookSwap</title>
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
            animation: fadeInDown 0.8s ease;
        }

        .page-title p {
            font-size: 1.1rem;
            opacity: 0.9;
        }

        /* 表单卡片 */
        .form-card {
            background: white;
            border-radius: 20px;
            padding: 2.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            animation: slideUp 0.8s ease;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #4CAF50;
            font-size: 1.1rem;
        }

        .form-group input,
        .form-group textarea {
            width: 100%;
            padding: 1rem 1.2rem;
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
            resize: vertical;
            min-height: 120px;
        }

        /* 错误消息 */
        .error-message {
            background: #ffebee;
            color: #e74c3c;
            padding: 1rem 1.5rem;
            border-radius: 10px;
            border-left: 4px solid #e74c3c;
            margin-bottom: 1.5rem;
            animation: shake 0.5s ease;
        }

        /* 按钮组 */
        .button-group {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 2rem;
        }

        .btn {
            padding: 1rem 2.5rem;
            border: none;
            border-radius: 30px;
            font-size: 1.1rem;
            cursor: pointer;
            transition: all 0.3s ease;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
        }

        .btn-primary {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
        }

        .btn-secondary {
            background: linear-gradient(45deg, #6c757d, #5a6268);
            color: white;
            box-shadow: 0 4px 15px rgba(108, 117, 125, 0.3);
        }

        .btn-secondary:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(108, 117, 125, 0.4);
        }

        /* 返回链接 */
        .back-link {
            text-align: center;
            margin-top: 2rem;
        }

        .back-link a {
            color: #2196F3;
            text-decoration: none;
            padding: 0.8rem 1.5rem;
            border: 1px solid #2196F3;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .back-link a:hover {
            background-color: #2196F3;
            color: white;
            transform: translateY(-2px);
        }

        /* 表单提示 */
        .form-hint {
            background: #e8f5e8;
            color: #2e7d32;
            padding: 1rem 1.5rem;
            border-radius: 10px;
            margin-bottom: 2rem;
            border-left: 4px solid #4CAF50;
        }

        .form-hint h4 {
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
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

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
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

            .form-card {
                padding: 1.5rem;
            }

            .button-group {
                flex-direction: column;
            }

            .btn {
                width: 100%;
                text-align: center;
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
            <h2>📖 发布书籍</h2>
            <p>分享你的闲置书籍，让知识继续传递价值</p>
        </section>

        <!-- 表单卡片 -->
        <section class="form-card">
            <!-- 表单提示 -->
            <div class="form-hint">
                <h4>💡 发布提示</h4>
                <p>请填写书籍的详细信息，准确的描述有助于更快找到买家。价格合理、描述真实的书籍更容易成交。</p>
            </div>

            <!-- 错误消息 -->
            <c:if test="${not empty errorMessage}">
                <div class="error-message">
                    ⚠️ ${errorMessage}
                </div>
            </c:if>

            <!-- 发布表单 -->
            <form action="addBook" method="post">
                <div class="form-group">
                    <label for="title">📚 书名 *</label>
                    <input type="text" id="title" name="title" required placeholder="请输入书籍名称">
                </div>

                <div class="form-group">
                    <label for="author">✍️ 作者 *</label>
                    <input type="text" id="author" name="author" required placeholder="请输入作者姓名">
                </div>

                <div class="form-group">
                    <label for="price">💰 价格 (元) *</label>
                    <input type="number" id="price" name="price" step="0.01" min="0" required placeholder="0.00">
                </div>

                <div class="form-group">
                    <label for="description">📝 书籍描述</label>
                    <textarea id="description" name="description" placeholder="请描述书籍的新旧程度、适用人群、内容简介等信息..."></textarea>
                </div>

                <div class="form-group">
                    <label for="imageUrl">🖼️ 图片URL</label>
                    <input type="url" id="imageUrl" name="imageUrl" placeholder="https://example.com/book-cover.jpg (可选)">
                </div>

                <!-- 按钮组 -->
                <div class="button-group">
                    <button type="submit" class="btn btn-primary">
                        ✅ 发布书籍
                    </button>
                    <button type="reset" class="btn btn-secondary">
                        🔄 重置表单
                    </button>
                </div>
            </form>
        </section>

        <!-- 返回链接 -->
        <div class="back-link">
            <a href="index.jsp">🏠 返回首页</a>
        </div>
    </main>
</body>
</html>
