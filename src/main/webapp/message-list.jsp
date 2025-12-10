<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的消息 - Campus BookSwap</title>
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

        .unread-count {
            background: #e74c3c;
            color: white;
            padding: 0.3rem 0.8rem;
            border-radius: 15px;
            font-size: 0.9rem;
            margin-left: 0.5rem;
            animation: pulse 2s infinite;
        }

        /* 操作栏 */
        .actions-bar {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 2rem;
            display: flex;
            justify-content: space-between;
            align-items: center;
            animation: slideUp 0.8s ease;
        }

        .btn-mark-all-read {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            border: none;
            padding: 0.8rem 1.5rem;
            border-radius: 25px;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
        }

        .btn-mark-all-read:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(76, 175, 80, 0.3);
        }

        /* 对话列表 */
        .conversation-list {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            animation: slideUp 1s ease;
        }

        .conversation-item {
            display: flex;
            align-items: center;
            padding: 1.5rem;
            border-bottom: 1px solid #f0f0f0;
            transition: all 0.3s ease;
            cursor: pointer;
            position: relative;
        }

        .conversation-item:hover {
            background: #f8f9fa;
            transform: translateX(5px);
        }

        .conversation-item:last-child {
            border-bottom: none;
        }

        .conversation-item.unread {
            background: #e3f2fd;
            border-left: 4px solid #2196F3;
        }

        .conversation-avatar {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            background: linear-gradient(45deg, #FF6B6B, #4ECDC4);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1.5rem;
            margin-right: 1rem;
            flex-shrink: 0;
        }

        .conversation-content {
            flex: 1;
            min-width: 0;
        }

        .conversation-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
        }

        .conversation-user {
            font-weight: bold;
            color: #333;
            font-size: 1.1rem;
        }

        .conversation-time {
            color: #999;
            font-size: 0.9rem;
        }

        .conversation-book {
            color: #4CAF50;
            font-size: 0.9rem;
            margin-bottom: 0.3rem;
        }

        .conversation-text {
            color: #666;
            line-height: 1.4;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .unread-badge {
            background: #e74c3c;
            color: white;
            border-radius: 10px;
            padding: 0.2rem 0.6rem;
            font-size: 0.8rem;
            margin-left: 0.5rem;
        }

        .conversation-actions {
            display: flex;
            gap: 0.5rem;
            margin-left: 1rem;
        }

        .btn-conversation-action {
            background: none;
            border: 1px solid #ddd;
            padding: 0.5rem;
            border-radius: 5px;
            cursor: pointer;
            transition: all 0.3s ease;
            color: #666;
        }

        .btn-conversation-action:hover {
            background: #f0f0f0;
            border-color: #999;
        }

        /* 空状态 */
        .empty-state {
            text-align: center;
            padding: 4rem 2rem;
            color: #999;
        }

        .empty-state-icon {
            font-size: 4rem;
            margin-bottom: 1rem;
        }

        .empty-state-text {
            font-size: 1.2rem;
            margin-bottom: 2rem;
        }

        /* 导航链接 */
        .navigation {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
            animation: slideUp 1.2s ease;
            margin-top: 2rem;
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

        @keyframes pulse {
            0% { transform: scale(1); }
            50% { transform: scale(1.05); }
            100% { transform: scale(1); }
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

            .actions-bar {
                flex-direction: column;
                gap: 1rem;
                text-align: center;
            }

            .conversation-item {
                padding: 1rem;
            }

            .conversation-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 0.3rem;
            }

            .conversation-actions {
                flex-direction: column;
                margin-left: 0.5rem;
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
            <h2>💬 我的对话</h2>
            <%
                Integer unreadCount = (Integer) request.getAttribute("unreadCount");
                if (unreadCount != null && unreadCount > 0) {
                    out.println("<span class=\"unread-count\">" + unreadCount + " 条未读</span>");
                }
            %>
        </section>

        <!-- 操作栏 -->
        <section class="actions-bar">
            <div>
                <strong>对话管理</strong>
            </div>
            <div>
                <%
                    if (unreadCount != null && unreadCount > 0) {
                        out.println("<form action=\"message-list\" method=\"post\" style=\"display: inline-block;\" onsubmit=\"return confirm('确定要标记所有消息为已读吗？');\">");
                        out.println("<input type=\"hidden\" name=\"action\" value=\"markAllAsRead\">");
                        out.println("<button type=\"submit\" class=\"btn-mark-all-read\">");
                        out.println("✓ 全部标记为已读");
                        out.println("</button>");
                        out.println("</form>");
                    }
                %>
            </div>
        </section>

        <!-- 对话列表 -->
        <section class="conversation-list">
            <%
                java.util.List<com.example.demo.model.Conversation> conversations = (java.util.List<com.example.demo.model.Conversation>) request.getAttribute("conversations");
                if (conversations == null || conversations.isEmpty()) {
                    out.println("<div class=\"empty-state\">");
                    out.println("<div class=\"empty-state-icon\">💬</div>");
                    out.println("<div class=\"empty-state-text\">暂无对话</div>");
                    out.println("<div>当您与其他用户交流时，对话会显示在这里</div>");
                    out.println("</div>");
                } else {
                    for (com.example.demo.model.Conversation conversation : conversations) {
                        boolean hasUnread = conversation.getUnreadCount() > 0;
                        
                        out.println("<div class=\"conversation-item" + (hasUnread ? " unread" : "") + "\" onclick=\"openConversation(" + conversation.getBookId() + ", " + conversation.getOtherUserId() + ")\">");
                        
                        // 头像
                        String avatar = conversation.isOtherUserSeller() ? "📚" : "👤";
                        out.println("<div class=\"conversation-avatar\">" + avatar + "</div>");
                        
                        // 对话内容
                        out.println("<div class=\"conversation-content\">");
                        out.println("<div class=\"conversation-header\">");
                        
                        String userRole = conversation.isOtherUserSeller() ? "卖家" : "买家";
                        out.println("<span class=\"conversation-user\">" + conversation.getOtherUsername() + " (" + userRole + ")</span>");
                        
                        if (hasUnread) {
                            out.println("<span class=\"unread-badge\">" + conversation.getUnreadCount() + "</span>");
                        }
                        
                        out.println("<span class=\"conversation-time\">" + conversation.getLastMessageTime().toString() + "</span>");
                        out.println("</div>");
                        
                        out.println("<div class=\"conversation-book\">📚 " + conversation.getBookTitle() + "</div>");
                        out.println("<div class=\"conversation-text\">" + conversation.getLastMessageContent() + "</div>");
                        out.println("</div>");
                        
                        // 操作按钮
                        out.println("<div class=\"conversation-actions\">");
                        if (hasUnread) {
                            out.println("<form action=\"message-list\" method=\"post\" style=\"display: inline;\" onclick=\"event.stopPropagation()\">");
                            out.println("<input type=\"hidden\" name=\"action\" value=\"markConversationAsRead\">");
                            out.println("<input type=\"hidden\" name=\"bookId\" value=\"" + conversation.getBookId() + "\">");
                            out.println("<input type=\"hidden\" name=\"otherUserId\" value=\"" + conversation.getOtherUserId() + "\">");
                            out.println("<button type=\"submit\" class=\"btn-conversation-action\" title=\"标记对话为已读\">✓</button>");
                            out.println("</form>");
                        }
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
                <a href="profile">👤 个人主页</a>
            </div>
        </section>
    </main>

    <script>
        function openConversation(bookId, otherUserId) {
            // 跳转到聊天页面
            window.location.href = 'messages?bookId=' + bookId + '&otherUserId=' + otherUserId;
        }
    </script>
</body>
</html>
