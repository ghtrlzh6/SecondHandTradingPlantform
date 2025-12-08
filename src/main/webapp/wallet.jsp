<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的钱包 - Campus BookSwap</title>
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
            max-width: 1000px;
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

        /* 钱包余额卡片 */
        .balance-card {
            background: white;
            border-radius: 20px;
            padding: 2.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: slideUp 0.8s ease;
            text-align: center;
            position: relative;
            overflow: hidden;
        }

        .balance-card::before {
            content: '';
            position: absolute;
            top: -50%;
            right: -50%;
            width: 200%;
            height: 200%;
            background: linear-gradient(45deg, rgba(76, 175, 80, 0.1), rgba(69, 160, 73, 0.1));
            border-radius: 50%;
            z-index: 0;
        }

        .balance-content {
            position: relative;
            z-index: 1;
        }

        .balance-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
            animation: pulse 2s ease-in-out infinite;
        }

        .balance-title {
            font-size: 1.2rem;
            color: #666;
            margin-bottom: 1rem;
        }

        .balance-amount {
            font-size: 3rem;
            font-weight: bold;
            background: linear-gradient(45deg, #4CAF50, #45a049);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
            margin-bottom: 1rem;
        }

        .balance-status {
            display: inline-block;
            padding: 0.5rem 1rem;
            background: #e8f5e8;
            color: #2e7d32;
            border-radius: 20px;
            font-size: 0.9rem;
        }

        /* 交易表单卡片 */
        .transaction-card {
            background: white;
            border-radius: 20px;
            padding: 2.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: slideUp 1s ease;
        }

        .transaction-title {
            font-size: 1.8rem;
            color: #333;
            margin-bottom: 2rem;
            text-align: center;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
        }

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

        .amount-input {
            width: 100%;
            padding: 1rem 1.5rem;
            border: 2px solid #e0e0e0;
            border-radius: 15px;
            font-size: 1.2rem;
            text-align: center;
            transition: all 0.3s ease;
            font-family: inherit;
        }

        .amount-input:focus {
            outline: none;
            border-color: #4CAF50;
            box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
        }

        .amount-hint {
            text-align: center;
            color: #666;
            font-size: 0.9rem;
            margin-top: 0.5rem;
        }

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
            min-width: 150px;
        }

        .btn-deposit {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            box-shadow: 0 4px 15px rgba(76, 175, 80, 0.3);
        }

        .btn-deposit:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(76, 175, 80, 0.4);
        }

        .btn-withdraw {
            background: linear-gradient(45deg, #ff6b6b, #ee5a52);
            color: white;
            box-shadow: 0 4px 15px rgba(255, 107, 107, 0.3);
        }

        .btn-withdraw:hover {
            transform: translateY(-2px);
            box-shadow: 0 6px 20px rgba(255, 107, 107, 0.4);
        }

        /* 快捷金额 */
        .quick-amounts {
            display: flex;
            gap: 0.5rem;
            justify-content: center;
            margin-bottom: 1.5rem;
            flex-wrap: wrap;
        }

        .quick-amount {
            padding: 0.5rem 1rem;
            background: #f8f9fa;
            border: 1px solid #e0e0e0;
            border-radius: 20px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 0.9rem;
        }

        .quick-amount:hover {
            background: #4CAF50;
            color: white;
            border-color: #4CAF50;
        }

        /* 错误消息 */
        .error-message {
            background: #ffebee;
            color: #e74c3c;
            padding: 1rem 1.5rem;
            border-radius: 10px;
            border-left: 4px solid #e74c3c;
            margin-bottom: 2rem;
            text-align: center;
            animation: shake 0.5s ease;
        }

        /* 交易记录 */
        .history-section {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            animation: slideUp 1.2s ease;
        }

        .history-title {
            font-size: 1.5rem;
            color: #333;
            margin-bottom: 1.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .empty-history {
            text-align: center;
            color: #666;
            padding: 2rem;
            font-style: italic;
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

        @keyframes pulse {
            0%, 100% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.1);
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

            .balance-amount {
                font-size: 2.5rem;
            }

            .button-group {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }

            .quick-amounts {
                flex-direction: column;
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
            <h2>💰 我的钱包</h2>
        </section>

        <!-- 错误消息 -->
        <c:if test="${not empty param.error}">
            <div class="error-message">
                <c:choose>
                    <c:when test="${param.error == 'invalid_amount'}">
                        ⚠️ 金额格式不正确，请输入有效数字
                    </c:when>
                    <c:otherwise>
                        ❌ 操作失败，请重试
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <!-- 钱包余额卡片 -->
        <section class="balance-card">
            <div class="balance-content">
                <div class="balance-icon">💳</div>
                <div class="balance-title">账户余额</div>
                <div class="balance-amount">
                    ￥<c:choose>
                        <c:when test="${wallet != null}">${wallet.balance}</c:when>
                        <c:otherwise>0.00</c:otherwise>
                    </c:choose>
                </div>
                <div class="balance-status">✅ 账户正常</div>
            </div>
        </section>

        <!-- 交易表单卡片 -->
        <section class="transaction-card">
            <h3 class="transaction-title">💸 充值/提现</h3>
            
            <form method="post" action="wallet">
                <div class="form-group">
                    <label for="amount">交易金额 (元)</label>
                    <input type="number" id="amount" name="amount" step="0.01" min="0.01" required 
                           class="amount-input" placeholder="0.00">
                    <div class="amount-hint">请输入充值或提现金额</div>
                </div>

                <!-- 快捷金额选择 -->
                <div class="quick-amounts">
                    <div class="quick-amount" onclick="setAmount(10)">￥10</div>
                    <div class="quick-amount" onclick="setAmount(50)">￥50</div>
                    <div class="quick-amount" onclick="setAmount(100)">￥100</div>
                    <div class="quick-amount" onclick="setAmount(200)">￥200</div>
                    <div class="quick-amount" onclick="setAmount(500)">￥500</div>
                </div>

                <div class="button-group">
                    <button type="submit" name="action" value="deposit" class="btn btn-deposit">
                        💰 充值
                    </button>
                    <button type="submit" name="action" value="withdraw" class="btn btn-withdraw">
                        💸 提现
                    </button>
                </div>
            </form>
        </section>

        <!-- 交易记录 -->
        <section class="history-section">
            <h3 class="history-title">📊 交易记录</h3>
            <div class="empty-history">
                暂无交易记录，完成交易后将显示在这里
            </div>
        </section>

        <!-- 返回链接 -->
        <div class="back-link">
            <a href="index.jsp">🏠 返回首页</a>
        </div>
    </main>

    <script>
        // 设置快捷金额
        function setAmount(amount) {
            document.getElementById('amount').value = amount;
        }

        // 格式化金额输入
        document.getElementById('amount').addEventListener('input', function(e) {
            let value = e.target.value;
            if (value < 0) {
                e.target.value = 0;
            }
        });
    </script>
</body>
</html>
