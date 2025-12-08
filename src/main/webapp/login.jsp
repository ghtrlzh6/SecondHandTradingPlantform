<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 如果用户已登录，重定向到主页
    if (session.getAttribute("user") != null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户登录 - Campus BookSwap</title>
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
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 2rem;
        }

        /* 背景装饰 */
        .bg-decoration {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            overflow: hidden;
            z-index: -1;
        }

        .bg-circle {
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.1);
            animation: float 6s ease-in-out infinite;
        }

        .bg-circle:nth-child(1) {
            width: 80px;
            height: 80px;
            top: 20%;
            left: 10%;
            animation-delay: 0s;
        }

        .bg-circle:nth-child(2) {
            width: 120px;
            height: 120px;
            top: 60%;
            right: 10%;
            animation-delay: 2s;
        }

        .bg-circle:nth-child(3) {
            width: 60px;
            height: 60px;
            bottom: 20%;
            left: 20%;
            animation-delay: 4s;
        }

        /* 登录容器 */
        .login-container {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(10px);
            padding: 3rem;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            width: 100%;
            max-width: 450px;
            animation: slideUp 0.8s ease;
        }

        /* Logo区域 */
        .logo-section {
            text-align: center;
            margin-bottom: 2rem;
        }

        .logo {
            font-size: 3rem;
            margin-bottom: 1rem;
            animation: bounce 2s ease-in-out infinite;
        }

        .site-name {
            font-size: 1.5rem;
            font-weight: bold;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        /* 页面标题 */
        .page-title {
            text-align: center;
            margin-bottom: 2rem;
        }

        .page-title h2 {
            font-size: 2rem;
            color: #333;
            margin-bottom: 0.5rem;
        }

        .page-title p {
            color: #666;
            font-size: 1rem;
        }

        /* 表单样式 */
        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            color: #4CAF50;
            font-weight: 600;
            font-size: 1.1rem;
        }

        .form-group input {
            width: 100%;
            padding: 1rem 1.2rem;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
            font-family: inherit;
        }

        .form-group input:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
        }

        /* 错误消息 */
        .error-message {
            background: #ffebee;
            color: #e74c3c;
            padding: 1rem 1.5rem;
            border-radius: 10px;
            border-left: 4px solid #e74c3c;
            margin-bottom: 1.5rem;
            text-align: center;
            animation: shake 0.5s ease;
        }

        /* 登录按钮 */
        .login-button {
            width: 100%;
            padding: 1rem 2rem;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            border: none;
            border-radius: 30px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
            margin-bottom: 1.5rem;
        }

        .login-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
        }

        .login-button:active {
            transform: translateY(0);
        }

        /* 链接区域 */
        .links {
            text-align: center;
            border-top: 1px solid #e0e0e0;
            padding-top: 1.5rem;
        }

        .links a {
            color: #2196F3;
            text-decoration: none;
            margin: 0 0.5rem;
            padding: 0.5rem 1rem;
            border-radius: 20px;
            transition: all 0.3s ease;
        }

        .links a:hover {
            background-color: #e3f2fd;
            color: #1976D2;
        }

        /* 分隔符 */
        .separator {
            color: #999;
            margin: 0 0.5rem;
        }

        /* 记住我选项 */
        .remember-me {
            display: flex;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .remember-me input[type="checkbox"] {
            margin-right: 0.5rem;
            width: auto;
        }

        .remember-me label {
            margin-bottom: 0;
            color: #666;
            font-weight: normal;
            cursor: pointer;
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

        @keyframes float {
            0%, 100% {
                transform: translateY(0px);
            }
            50% {
                transform: translateY(-20px);
            }
        }

        @keyframes bounce {
            0%, 20%, 50%, 80%, 100% {
                transform: translateY(0);
            }
            40% {
                transform: translateY(-10px);
            }
            60% {
                transform: translateY(-5px);
            }
        }

        @keyframes shake {
            0%, 100% { transform: translateX(0); }
            25% { transform: translateX(-10px); }
            75% { transform: translateX(10px); }
        }

        /* 响应式设计 */
        @media (max-width: 768px) {
            body {
                padding: 1rem;
            }

            .login-container {
                padding: 2rem;
                margin: 1rem;
            }

            .logo {
                font-size: 2.5rem;
            }

            .page-title h2 {
                font-size: 1.8rem;
            }

            .links {
                display: flex;
                flex-direction: column;
                gap: 1rem;
            }

            .separator {
                display: none;
            }
        }
    </style>
</head>
<body>
    <!-- 背景装饰 -->
    <div class="bg-decoration">
        <div class="bg-circle"></div>
        <div class="bg-circle"></div>
        <div class="bg-circle"></div>
    </div>

    <!-- 登录容器 -->
    <div class="login-container">
        <!-- Logo区域 -->
        <div class="logo-section">
            <div class="logo">📚</div>
            <div class="site-name">Campus BookSwap</div>
        </div>

        <!-- 页面标题 -->
        <div class="page-title">
            <h2>🔐 用户登录</h2>
            <p>欢迎回到校园二手书交易平台</p>
        </div>

        <!-- 错误消息 -->
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                ⚠️ <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <!-- 登录表单 -->
        <form action="login" method="post">
            <div class="form-group">
                <label for="username">👤 用户名</label>
                <input type="text" id="username" name="username" required placeholder="请输入用户名">
            </div>

            <div class="form-group">
                <label for="password">🔒 密码</label>
                <input type="password" id="password" name="password" required placeholder="请输入密码">
            </div>

            <div class="remember-me">
                <input type="checkbox" id="remember" name="remember">
                <label for="remember">记住我</label>
            </div>

            <button type="submit" class="login-button">
                ✅ 登录
            </button>
        </form>

        <!-- 链接区域 -->
        <div class="links">
            <a href="register.jsp">🆕 没有账户？点击注册</a>
            <span class="separator">|</span>
            <a href="welcome.jsp">🏠 返回首页</a>
        </div>
    </div>
</body>
</html>
