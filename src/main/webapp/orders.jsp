<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的订单 - Campus BookSwap</title>
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

        /* 订单统计卡片 */
        .stats-container {
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
            animation: slideUp 0.8s ease;
            transition: all 0.3s ease;
        }

        .stat-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
        }

        .stat-icon {
            font-size: 2rem;
            margin-bottom: 0.5rem;
        }

        .stat-number {
            font-size: 1.8rem;
            font-weight: bold;
            color: #333;
            margin-bottom: 0.5rem;
        }

        .stat-label {
            color: #666;
            font-size: 0.9rem;
        }

        /* 订单列表容器 */
        .orders-container {
            background: white;
            border-radius: 20px;
            padding: 2rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            animation: slideUp 1s ease;
        }

        /* 筛选标签 */
        .filter-tabs {
            display: flex;
            gap: 1rem;
            margin-bottom: 2rem;
            border-bottom: 2px solid #f0f0f0;
            padding-bottom: 1rem;
            overflow-x: auto;
        }

        .filter-tab {
            padding: 0.8rem 1.5rem;
            background: transparent;
            border: none;
            border-radius: 20px 20px 0 0;
            cursor: pointer;
            transition: all 0.3s ease;
            color: #666;
            font-weight: 600;
            white-space: nowrap;
        }

        .filter-tab.active {
            color: #4CAF50;
            background: #e8f5e8;
            border-bottom: 2px solid #4CAF50;
        }

        .filter-tab:hover {
            background: #f8f9fa;
            color: #4CAF50;
        }

        /* 订单网格 */
        .orders-grid {
            display: grid;
            gap: 1.5rem;
        }

        .order-card {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 1.5rem;
            border-left: 4px solid #e0e0e0;
            transition: all 0.3s ease;
            animation: fadeIn 0.6s ease;
        }

        .order-card:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
            border-left-color: #4CAF50;
        }

        /* 订单头部 */
        .order-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #e0e0e0;
        }

        .order-id {
            font-weight: bold;
            color: #333;
            font-size: 1.1rem;
        }

        .order-status {
            padding: 0.5rem 1rem;
            border-radius: 20px;
            font-size: 0.9rem;
            font-weight: 600;
        }

        .status-pending {
            background: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }

        .status-paid {
            background: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }

        .status-shipped {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .status-completed {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .status-cancelled {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        /* 订单内容 */
        .order-content {
            display: grid;
            grid-template-columns: 1fr auto;
            gap: 1rem;
            align-items: center;
            margin-bottom: 1rem;
        }

        .book-info {
            flex: 1;
        }

        .book-title {
            font-weight: 600;
            color: #333;
            margin-bottom: 0.5rem;
        }

        .book-meta {
            color: #666;
            font-size: 0.9rem;
        }

        .order-price {
            font-size: 1.3rem;
            font-weight: bold;
            color: #e74c3c;
        }

        /* 订单底部 */
        .order-footer {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding-top: 1rem;
            border-top: 1px solid #e0e0e0;
        }

        .order-date {
            color: #666;
            font-size: 0.9rem;
        }

        .order-actions {
            display: flex;
            gap: 0.5rem;
        }

        .btn {
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 20px;
            font-size: 0.9rem;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            font-weight: 600;
        }

        .btn-primary {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(76, 175, 80, 0.4);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
            box-shadow: 0 2px 8px rgba(108, 117, 125, 0.3);
        }

        .btn-secondary:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }

        /* 空状态 */
        .empty-state {
            text-align: center;
            padding: 4rem 2rem;
            color: #666;
        }

        .empty-icon {
            font-size: 4rem;
            margin-bottom: 1rem;
            opacity: 0.5;
        }

        .empty-text {
            font-size: 1.2rem;
            margin-bottom: 1rem;
        }

        .empty-hint {
            color: #999;
            font-size: 0.9rem;
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

            .page-title h2 {
                font-size: 2rem;
            }

            .stats-container {
                grid-template-columns: repeat(2, 1fr);
                gap: 1rem;
            }

            .filter-tabs {
                flex-wrap: wrap;
            }

            .order-content {
                grid-template-columns: 1fr;
                gap: 0.5rem;
            }

            .order-footer {
                flex-direction: column;
                gap: 1rem;
                align-items: flex-start;
            }

            .order-actions {
                width: 100%;
                justify-content: flex-end;
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
            <h2>📋 我的订单</h2>
        </section>

        <!-- 订单统计 -->
        <div class="stats-container">
            <div class="stat-card">
                <div class="stat-icon">⏳</div>
                <div class="stat-number">0</div>
                <div class="stat-label">待支付</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">📦</div>
                <div class="stat-number">0</div>
                <div class="stat-label">待发货</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">🚚</div>
                <div class="stat-number">0</div>
                <div class="stat-label">待收货</div>
            </div>
            <div class="stat-card">
                <div class="stat-icon">✅</div>
                <div class="stat-number">0</div>
                <div class="stat-label">已完成</div>
            </div>
        </div>

        <!-- 订单列表 -->
        <div class="orders-container">
            <c:choose>
                <c:when test="${empty orders}">
                    <!-- 空状态 -->
                    <div class="empty-state">
                        <div class="empty-icon">📦</div>
                        <div class="empty-text">您还没有任何订单</div>
                        <div class="empty-hint">去首页看看有什么好书吧！</div>
                        <div style="margin-top: 2rem;">
                            <a href="index.jsp" class="btn btn-primary">🛍️ 开始购书</a>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- 订单筛选标签 -->
                    <div class="filter-tabs">
                        <button class="filter-tab active" onclick="filterOrders('all')">全部订单</button>
                        <button class="filter-tab" onclick="filterOrders('pending')">待支付</button>
                        <button class="filter-tab" onclick="filterOrders('paid')">已支付</button>
                        <button class="filter-tab" onclick="filterOrders('shipped')">待收货</button>
                        <button class="filter-tab" onclick="filterOrders('completed')">已完成</button>
                    </div>

                    <!-- 订单网格 -->
                    <div class="orders-grid">
                        <c:forEach var="order" items="${orders}">
                            <div class="order-card" data-status="${order.status}">
                                <div class="order-header">
                                    <div class="order-id">订单号：${order.id}</div>
                                    <div class="order-status status-${order.status}">
                                        <c:choose>
                                            <c:when test='${order.status == "pending"}'>⏳ 待支付</c:when>
                                            <c:when test='${order.status == "paid"}'>💰 已支付</c:when>
                                            <c:when test='${order.status == "shipped"}'>📦 待收货</c:when>
                                            <c:when test='${order.status == "completed"}'>✅ 已完成</c:when>
                                            <c:when test='${order.status == "cancelled"}'>❌ 已取消</c:when>
                                            <c:otherwise>${order.status}</c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="order-content">
                                    <div class="book-info">
                                        <div class="book-title">书籍信息</div>
                                        <div class="book-meta">下单时间：${order.orderedAt}</div>
                                    </div>
                                    <div class="order-price">￥99.00</div>
                                </div>

                                <div class="order-footer">
                                    <div class="order-date">${order.orderedAt}</div>
                                    <div class="order-actions">
                                        <c:if test='${order.status == "shipped"}'>
                                            <form method="post" action="confirm-delivery" style="display: inline;">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <button type="submit" class="btn btn-primary">✅ 确认收货</button>
                                            </form>
                                        </c:if>
                                        <c:if test='${order.status == "pending"}'>
                                            <a href="#" class="btn btn-secondary">💰 立即支付</a>
                                        </c:if>
                                        <a href="#" class="btn btn-secondary">📖 查看详情</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 返回链接 -->
        <div class="back-link">
            <a href="index.jsp">🏠 返回首页</a>
        </div>
    </main>

    <script>
        // 筛选订单
        function filterOrders(status) {
            // 移除所有active类
            document.querySelectorAll('.filter-tab').forEach(tab => {
                tab.classList.remove('active');
            });
            
            // 添加active类到当前标签
            event.target.classList.add('active');
            
            // 筛选订单卡片
            const orderCards = document.querySelectorAll('.order-card');
            orderCards.forEach(card => {
                if (status === 'all' || card.dataset.status === status) {
                    card.style.display = 'block';
                } else {
                    card.style.display = 'none';
                }
            });
        }
    </script>
</body>
</html>
