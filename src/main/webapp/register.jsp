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
    <title>用户注册 - Campus BookSwap</title>
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

        /* 注册容器 */
        .register-container {
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

        /* 密码强度指示器 */
        .password-strength {
            margin-top: 0.5rem;
            height: 4px;
            background: #e0e0e0;
            border-radius: 2px;
            overflow: hidden;
        }

        .password-strength-bar {
            height: 100%;
            width: 0%;
            transition: all 0.3s ease;
            border-radius: 2px;
        }

        .strength-weak { background: #e74c3c; width: 33%; }
        .strength-medium { background: #f39c12; width: 66%; }
        .strength-strong { background: #4CAF50; width: 100%; }

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

        /* 注册提示 */
        .register-hint {
            background: #e8f5e8;
            color: #2e7d32;
            padding: 1rem 1.5rem;
            border-radius: 10px;
            margin-bottom: 1.5rem;
            border-left: 4px solid #4CAF50;
            font-size: 0.9rem;
        }

        /* 注册按钮 */
        .register-button {
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

        .register-button:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
        }

        .register-button:active {
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

        .separator {
            color: #999;
            margin: 0 0.5rem;
        }

        /* 服务条款 */
        .terms {
            margin-bottom: 1.5rem;
        }

        .terms label {
            display: flex;
            align-items: flex-start;
            color: #666;
            font-size: 0.9rem;
            cursor: pointer;
        }

        .terms input[type="checkbox"] {
            margin-right: 0.5rem;
            margin-top: 2px;
            width: auto;
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

            .register-container {
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

    <!-- 注册容器 -->
    <div class="register-container">
        <!-- Logo区域 -->
        <div class="logo-section">
            <div class="logo">📚</div>
            <div class="site-name">Campus BookSwap</div>
        </div>

        <!-- 页面标题 -->
        <div class="page-title">
            <h2>🆕 用户注册</h2>
            <p>加入我们，开始你的二手书交易之旅</p>
        </div>

        <!-- 错误消息 -->
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div class="error-message">
                ⚠️ <%= request.getAttribute("errorMessage") %>
            </div>
        <% } %>

        <!-- 注册提示 -->
        <div class="register-hint">
            💡 请填写真实信息，我们承诺保护您的隐私安全
        </div>

        <!-- 注册表单 -->
        <form action="register" method="post">
            <div class="form-group">
                <label for="username">👤 用户名</label>
                <input type="text" id="username" name="username" required placeholder="请输入用户名">
            </div>

            <div class="form-group">
                <label for="email">📧 邮箱</label>
                <input type="email" id="email" name="email" required placeholder="请输入邮箱地址">
            </div>

            <div class="form-group">
                <label for="password">🔒 密码</label>
                <input type="password" id="password" name="password" required placeholder="请输入密码" onkeyup="checkPasswordStrength(this.value)">
                <div class="password-strength">
                    <div class="password-strength-bar" id="strengthBar"></div>
                </div>
            </div>

            <div class="terms">
                <label>
                    <input type="checkbox" id="terms" name="terms" required>
                    我已阅读并同意<a href="#" style="color: #4CAF50;">服务条款</a>和<a href="#" style="color: #4CAF50;">隐私政策</a>
                </label>
            </div>

            <button type="submit" class="register-button">
                ✅ 立即注册
            </button>
        </form>

        <!-- 链接区域 -->
        <div class="links">
            <a href="login.jsp">🔐 已有账户？点击登录</a>
            <span class="separator">|</span>
            <a href="welcome.jsp">🏠 返回首页</a>
        </div>
    </div>

    <script>
        // 密码强度检测
        function checkPasswordStrength(password) {
            const strengthBar = document.getElementById('strengthBar');
            let strength = 0;

            if (password.length >= 6) strength++;
            if (password.match(/[a-z]/) && password.match(/[A-Z]/)) strength++;
            if (password.match(/[0-9]/)) strength++;
            if (password.match(/[^a-zA-Z0-9]/)) strength++;

            strengthBar.className = 'password-strength-bar';
            if (password.length === 0) {
                strengthBar.style.width = '0%';
            } else if (strength <= 1) {
                strengthBar.classList.add('strength-weak');
            } else if (strength === 2) {
                strengthBar.classList.add('strength-medium');
            } else {
                strengthBar.classList.add('strength-strong');
            }
        }
    </script>
</body>
</html>
