<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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

        /* 私信模态框样式 */
        .modal {
            display: none;
            position: fixed;
            z-index: 2000;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            animation: fadeIn 0.3s ease;
        }

        .modal-content {
            background-color: white;
            margin: 10% auto;
            padding: 2rem;
            border-radius: 15px;
            width: 90%;
            max-width: 500px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
            animation: slideDown 0.3s ease;
        }

        .modal-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding-bottom: 1rem;
            border-bottom: 2px solid #f0f0f0;
        }

        .modal-header h3 {
            color: #333;
            font-size: 1.5rem;
            margin: 0;
        }

        .close-btn {
            background: none;
            border: none;
            font-size: 1.5rem;
            cursor: pointer;
            color: #999;
            transition: color 0.3s ease;
        }

        .close-btn:hover {
            color: #333;
        }

        .message-form {
            display: flex;
            flex-direction: column;
            gap: 1rem;
        }

        .message-form textarea {
            width: 100%;
            padding: 1rem;
            border: 2px solid #e0e0e0;
            border-radius: 10px;
            font-size: 1rem;
            resize: vertical;
            min-height: 120px;
            font-family: inherit;
            transition: border-color 0.3s ease;
        }

        .message-form textarea:focus {
            outline: none;
            border-color: #2196F3;
        }

        .message-form .btn-send {
            background: linear-gradient(45deg, #2196F3, #1976D2);
            color: white;
            border: none;
            padding: 1rem 2rem;
            border-radius: 25px;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.3s ease;
            align-self: flex-end;
        }

        .message-form .btn-send:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(33, 150, 243, 0.3);
        }

        .book-info-message {
            background: #f8f9fa;
            padding: 1rem;
            border-radius: 10px;
            margin-bottom: 1rem;
            border-left: 4px solid #4CAF50;
        }

        .book-info-message strong {
            color: #4CAF50;
        }

        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        @keyframes slideDown {
            from {
                transform: translateY(-50px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
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

            .modal-content {
                margin: 5% auto;
                padding: 1.5rem;
                width: 95%;
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
                    <%
                        com.example.demo.model.Book book = (com.example.demo.model.Book) request.getAttribute("book");
                        if (book != null && book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                            out.println("<img src=\"" + book.getImageUrl() + "\" alt=\"" + book.getTitle() + "\" class=\"book-image\">");
                        } else {
                            out.println("<div class=\"no-image\">📚</div>");
                        }
                    %>
                </div>

                <!-- 书籍信息 -->
                <div class="book-info-section">
                    <h3><%= book.getTitle() %></h3>
                    
                    <div class="book-price">￥<%= book.getPrice() %></div>
                    
                    <div class="book-info-item">
                        <strong>👤 作者:</strong> <%= book.getAuthor() %>
                    </div>
                    
                    <div class="book-info-item">
                        <strong>🏪 卖家:</strong> 
                        <%
                            com.example.demo.model.User seller = (com.example.demo.model.User) request.getAttribute("seller");
                            Boolean isOwner = (Boolean) request.getAttribute("isOwner");
                            if (seller != null) {
                                out.println(seller.getUsername());
                                if (!isOwner) {
                                    out.println("<a href=\"profile?userId=" + seller.getId() + "\" style=\"margin-left: 10px; color: #4CAF50; text-decoration: none; font-size: 0.9rem;\">");
                                    out.println("🔍 查看卖家主页");
                                    out.println("</a>");
                                    out.println("<button onclick=\"openMessageModal()\" style=\"margin-left: 10px; background: #2196F3; color: white; border: none; padding: 5px 10px; border-radius: 15px; cursor: pointer; font-size: 0.9rem;\">");
                                    out.println("💬 私信卖家");
                                    out.println("</button>");
                                }
                            }
                        %>
                    </div>
                    
                    <% if (book.getDescription() != null && !book.getDescription().isEmpty()) { %>
                        <div class="book-description">
                            <strong>📝 书籍描述:</strong><br>
                            <%= book.getDescription() %>
                        </div>
                    <% } %>
                    
                    <div class="book-info-item">
                        <strong>📅 发布时间:</strong> <%= book.getCreatedAt() %>
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
            <%
                if (isOwner) {
                    // 书籍发布者看到的操作
                    out.println("<div style=\"text-align: center; padding: 2rem;\">");
                    out.println("<div style=\"background: #fff3cd; color: #856404; padding: 1rem; border-radius: 10px; margin-bottom: 1rem;\">");
                    out.println("<strong>📚 这是您发布的书籍</strong>");
                    out.println("</div>");
                    
                    String statusText = "未知";
                    if ("available".equals(book.getStatus())) {
                        statusText = "可售中";
                    } else if ("sold".equals(book.getStatus())) {
                        statusText = "已售出";
                    } else if ("cancelled".equals(book.getStatus())) {
                        statusText = "已下架";
                    }
                    
                    out.println("<div style=\"background: #ffeaa7; color: #856404; padding: 1rem; border-radius: 10px; margin-bottom: 1rem;\">");
                    out.println("<strong>📊 状态: " + statusText + "</strong>");
                    out.println("</div>");
                    out.println("<div style=\"background: #d4edda; color: #155724; padding: 1rem; border-radius: 10px;\">");
                    out.println("<strong>💰 价格: ￥" + book.getPrice() + "</strong>");
                    out.println("</div>");
                    out.println("<div style=\"display: flex; gap: 1rem; justify-content: center; margin-top: 1rem;\">");
                    
                    if ("available".equals(book.getStatus())) {
                        out.println("<form action=\"cancelBook\" method=\"post\" style=\"display: inline-block;\" onsubmit=\"return confirm('确定要下架这本书吗？下架后将无法恢复。');\">");
                        out.println("<input type=\"hidden\" name=\"bookId\" value=\"" + book.getId() + "\">");
                        out.println("<button type=\"submit\" style=\"background: #dc3545; color: white; padding: 0.8rem 1.5rem; border: none; border-radius: 5px; cursor: pointer;\">");
                        out.println("🚫 取消发布");
                        out.println("</button>");
                        out.println("</form>");
                    } else if ("cancelled".equals(book.getStatus())) {
                        out.println("<div style=\"background: #6c757d; color: white; padding: 0.8rem 1.5rem; border-radius: 5px; display: inline-block;\">");
                        out.println("🚫 已下架");
                        out.println("</div>");
                    }
                    
                    out.println("</div>");
                    out.println("</div>");
                } else {
                    // 非书籍发布者看到的购买按钮
                    if ("available".equals(book.getStatus())) {
                        out.println("<form action=\"buy\" method=\"get\" class=\"buy-form\">");
                        out.println("<input type=\"hidden\" name=\"bookId\" value=\"" + book.getId() + "\">");
                        out.println("<button type=\"submit\" class=\"btn-buy\">");
                        out.println("🛒 立即购买");
                        out.println("</button>");
                        out.println("</form>");
                    } else {
                        out.println("<div style=\"text-align: center; padding: 2rem;\">");
                        out.println("<div style=\"background: #dc3545; color: white; padding: 1.5rem; border-radius: 10px;\">");
                        out.println("<strong>🚫 此书已售出</strong>");
                        out.println("</div>");
                        out.println("<div style=\"background: #f8d7da; color: #343a40; padding: 1rem; border-radius: 10px;\">");
                        out.println("<strong>📚 书籍状态: 已售出</strong>");
                        out.println("</div>");
                        out.println("</div>");
                    }
                }
            %>
        </section>

        <!-- 导航链接 -->
        <section class="navigation">
            <div class="nav-links">
                <a href="books">📚 返回书籍列表</a>
                <a href="index.jsp">🏠 返回首页</a>
            </div>
        </section>
    </main>

    <!-- 私信模态框 -->
    <div id="messageModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3>💬 发送私信</h3>
                <button class="close-btn" onclick="closeMessageModal()">&times;</button>
            </div>
            <div class="book-info-message">
                <strong>📚 书籍:</strong> <%= book.getTitle() %><br>
                <strong>💰 价格:</strong> ￥<%= book.getPrice() %><br>
                <strong>👤 卖家:</strong> <%= seller.getUsername() %>
            </div>
            <form class="message-form" action="messages" method="post">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <input type="hidden" name="receiverId" value="<%= seller.getId() %>">
                <textarea name="content" placeholder="请输入您想对卖家说的话..." required></textarea>
                <button type="submit" class="btn-send">发送消息</button>
            </form>
        </div>
    </div>

    <script>
        function openMessageModal() {
            document.getElementById('messageModal').style.display = 'block';
        }

        function closeMessageModal() {
            document.getElementById('messageModal').style.display = 'none';
        }

        // 点击模态框外部关闭
        window.onclick = function(event) {
            const modal = document.getElementById('messageModal');
            if (event.target === modal) {
                closeMessageModal();
            }
        }

        // 表单提交后关闭模态框
        document.querySelector('.message-form').addEventListener('submit', function() {
            setTimeout(function() {
                closeMessageModal();
            }, 100);
        });
    </script>
</body>
</html>
