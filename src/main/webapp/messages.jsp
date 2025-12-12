<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>聊天详情 - Campus BookSwap</title>
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
            height: calc(100vh - 200px);
            display: flex;
            flex-direction: column;
        }

        /* 书籍信息卡片 */
        .book-info-card {
            background: white;
            border-radius: 15px;
            padding: 1.5rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin-bottom: 1rem;
            animation: slideDown 0.8s ease;
        }

        .book-info-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .book-title {
            font-size: 1.3rem;
            font-weight: bold;
            color: #333;
        }

        .book-price {
            font-size: 1.2rem;
            font-weight: bold;
            color: #e74c3c;
        }

        .book-meta {
            display: flex;
            gap: 2rem;
            color: #666;
            font-size: 0.9rem;
        }

        /* 聊天容器 */
        .chat-container {
            background: white;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            flex: 1;
            display: flex;
            flex-direction: column;
            overflow: hidden;
            animation: fadeIn 1s ease;
        }

        /* 消息列表区域 */
        .messages-area {
            flex: 1;
            padding: 1.5rem;
            overflow-y: auto;
            background: #f8f9fa;
        }

        .message-bubble {
            margin-bottom: 1rem;
            display: flex;
            align-items: flex-start;
            animation: messageSlide 0.3s ease;
        }

        .message-bubble.sent {
            justify-content: flex-end;
        }

        .message-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(45deg, #2196F3, #1976D2);
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            font-size: 1rem;
            margin-right: 0.8rem;
            flex-shrink: 0;
        }

        .message-bubble.sent .message-avatar {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            margin-right: 0;
            margin-left: 0.8rem;
            order: 2;
        }

        .message-content {
            max-width: 70%;
            background: white;
            padding: 1rem;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            position: relative;
        }

        .message-bubble.sent .message-content {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
        }

        .message-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 0.5rem;
            font-size: 0.8rem;
            color: #999;
        }

        .message-bubble.sent .message-header {
            color: rgba(255, 255, 255, 0.8);
        }

        .message-sender {
            font-weight: bold;
        }

        .message-time {
            font-size: 0.7rem;
        }

        .message-text {
            line-height: 1.4;
            word-wrap: break-word;
        }

        /* 消息输入区域 */
        .message-input-area {
            padding: 1.5rem;
            background: white;
            border-top: 1px solid #e0e0e0;
        }

        .message-form {
            display: flex;
            gap: 1rem;
            align-items: flex-end;
        }

        .message-input {
            flex: 1;
            padding: 1rem;
            border: 2px solid #e0e0e0;
            border-radius: 25px;
            font-size: 1rem;
            resize: none;
            min-height: 50px;
            max-height: 120px;
            font-family: inherit;
            transition: border-color 0.3s ease;
        }

        .message-input:focus {
            outline: none;
            border-color: #4CAF50;
        }

        .btn-send {
            background: linear-gradient(45deg, #4CAF50, #45a049);
            color: white;
            border: none;
            padding: 1rem 1.5rem;
            border-radius: 25px;
            font-size: 1rem;
            cursor: pointer;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .btn-send:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(76, 175, 80, 0.3);
        }

        .btn-send:disabled {
            opacity: 0.6;
            cursor: not-allowed;
            transform: none;
        }

        /* 导航链接 */
        .navigation {
            background: white;
            border-radius: 15px;
            padding: 1rem;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            text-align: center;
            margin-top: 1rem;
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

        /* 空状态 */
        .empty-messages {
            text-align: center;
            padding: 3rem;
            color: #999;
        }

        .empty-icon {
            font-size: 3rem;
            margin-bottom: 1rem;
        }

        /* 动画效果 */
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }

        @keyframes slideDown {
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

        @keyframes messageSlide {
            from {
                opacity: 0;
                transform: translateY(20px);
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

            .main-container {
                margin: 1rem;
                padding: 0 1rem;
            }

            .book-info-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 0.5rem;
            }

            .book-meta {
                flex-direction: column;
                gap: 0.5rem;
            }

            .message-bubble {
                margin-bottom: 0.8rem;
            }

            .message-content {
                max-width: 85%;
            }

            .message-form {
                flex-direction: column;
                gap: 0.8rem;
            }

            .btn-send {
                width: 100%;
                justify-content: center;
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
        <%
            com.example.demo.model.Book book = (com.example.demo.model.Book) request.getAttribute("book");
            com.example.demo.model.User currentUser = (com.example.demo.model.User) session.getAttribute("user");
            java.util.List<com.example.demo.model.Message> messages = (java.util.List<com.example.demo.model.Message>) request.getAttribute("messages");
        %>

        <!-- 书籍信息卡片 -->
        <section class="book-info-card">
            <%
                Boolean isAppeal = (Boolean) request.getAttribute("isAppeal");
                if (isAppeal != null && isAppeal) {
            %>
                <!-- 申诉信息显示 -->
                <div class="book-info-header">
                    <div class="book-title">📝 <%= book.getTitle() %></div>
                    <div class="book-price">订单申诉</div>
                </div>
                <div class="book-meta">
                    <div>📋 <%= book.getDescription() %></div>
                    <div>🏪 与管理员对话</div>
                    <div>📅 申诉时间: <%= new java.util.Date() %></div>
                </div>
            <%
                } else {
            %>
                <!-- 正常书籍信息显示 -->
                <div class="book-info-header">
                    <div class="book-title">📚 <%= book.getTitle() %></div>
                    <div class="book-price">￥<%= book.getPrice() %></div>
                </div>
                <div class="book-meta">
                    <div>👤 作者: <%= book.getAuthor() %></div>
                    <div>🏪 卖家ID: <%= book.getSellerId() %></div>
                    <div>📅 发布时间: <%= book.getCreatedAt() %></div>
                </div>
            <%
                }
            %>
        </section>

        <!-- 聊天容器 -->
        <section class="chat-container">
            <!-- 消息列表区域 -->
            <div class="messages-area" id="messagesArea">
                <%
                    if (messages == null || messages.isEmpty()) {
                        out.println("<div class=\"empty-messages\">");
                        out.println("<div class=\"empty-icon\">💬</div>");
                        out.println("<div>暂无消息，开始对话吧！</div>");
                        out.println("</div>");
                    } else {
                        for (com.example.demo.model.Message message : messages) {
                            boolean isSent = message.getSenderId().equals(currentUser.getId());
                            
                            out.println("<div class=\"message-bubble" + (isSent ? " sent" : "") + "\">");
                            
                            // 头像
                            out.println("<div class=\"message-avatar\">" + (isSent ? "📤" : "👤") + "</div>");
                            
                            // 消息内容
                            out.println("<div class=\"message-content\">");
                            out.println("<div class=\"message-header\">");
                            out.println("<span class=\"message-sender\">" + (isSent ? "我" : "对方") + "</span>");
                            out.println("<span class=\"message-time\">" + message.getSentAt().toString() + "</span>");
                            out.println("</div>");
                            out.println("<div class=\"message-text\">" + message.getContent() + "</div>");
                            out.println("</div>");
                            
                            out.println("</div>");
                        }
                    }
                %>
            </div>

            <!-- 消息输入区域 -->
            <div class="message-input-area">
                <form class="message-form" action="messages" method="post" id="messageForm">
                    <input type="hidden" name="bookId" value="<%= book.getId() %>">
                    <%
                        // 确定接收者ID
                        Long receiverId;
                        
                        if (isAppeal != null && isAppeal) {
                            // 申诉情况：接收者是管理员
                            // 从URL参数中获取管理员ID，或者从消息历史中获取
                            if (messages != null && !messages.isEmpty()) {
                                // 找到最后一条消息的发送者（如果是用户发的，则接收者是管理员）
                                receiverId = null;
                                for (int i = messages.size() - 1; i >= 0; i--) {
                                    if (!messages.get(i).getSenderId().equals(currentUser.getId())) {
                                        receiverId = messages.get(i).getSenderId();
                                        break;
                                    }
                                }
                                // 如果没找到，默认设为1（假设管理员ID为1）
                                if (receiverId == null) {
                                    receiverId = 1L;
                                }
                            } else {
                                // 没有消息历史，默认设为1（假设管理员ID为1）
                                receiverId = 1L;
                            }
                        } else {
                            // 正常情况：接收者是书籍卖家
                            receiverId = book.getSellerId();
                            if (book.getSellerId().equals(currentUser.getId()) && messages != null && !messages.isEmpty()) {
                                // 如果是卖家，找到最后一条消息的发送者作为接收者
                                for (int i = messages.size() - 1; i >= 0; i--) {
                                    if (!messages.get(i).getSenderId().equals(currentUser.getId())) {
                                        receiverId = messages.get(i).getSenderId();
                                        break;
                                    }
                                }
                            }
                        }
                    %>
                    <input type="hidden" name="receiverId" value="<%= receiverId %>">
                    <%
                        if (isAppeal != null && isAppeal) {
                    %>
                        <input type="hidden" name="type" value="appeal">
                    <%
                        }
                    %>
                    <textarea 
                        name="content" 
                        class="message-input" 
                        placeholder="<%= (isAppeal != null && isAppeal) ? "请详细描述您的申诉问题..." : "输入消息..." %>" 
                        required
                        onkeydown="handleKeyPress(event)"
                    ></textarea>
                    <button type="submit" class="btn-send" id="sendBtn">
                        <span>发送</span>
                        <span>📤</span>
                    </button>
                </form>
            </div>
        </section>

        <!-- 导航链接 -->
        <section class="navigation">
            <div class="nav-links">
                <a href="message-list">💌 消息列表</a>
                <a href="books">📚 返回书籍列表</a>
                <a href="index.jsp">🏠 返回首页</a>
            </div>
        </section>
    </main>

    <script>
        // 自动滚动到底部
        function scrollToBottom() {
            const messagesArea = document.getElementById('messagesArea');
            messagesArea.scrollTop = messagesArea.scrollHeight;
        }

        // 页面加载时滚动到底部
        window.addEventListener('load', scrollToBottom);

        // 处理键盘事件
        function handleKeyPress(event) {
            if (event.key === 'Enter' && !event.shiftKey) {
                event.preventDefault();
                document.getElementById('messageForm').submit();
            }
        }

        // 表单提交时禁用发送按钮
        document.getElementById('messageForm').addEventListener('submit', function() {
            const sendBtn = document.getElementById('sendBtn');
            const messageInput = document.querySelector('.message-input');
            
            sendBtn.disabled = true;
            sendBtn.innerHTML = '<span>发送中...</span><span>⏳</span>';
            
            // 5秒后重新启用按钮（防止长时间等待）
            setTimeout(() => {
                sendBtn.disabled = false;
                sendBtn.innerHTML = '<span>发送</span><span>📤</span>';
            }, 5000);
        });

        // 自动调整文本框高度
        const messageInput = document.querySelector('.message-input');
        messageInput.addEventListener('input', function() {
            this.style.height = 'auto';
            this.style.height = Math.min(this.scrollHeight, 120) + 'px';
        });

        // 定期刷新消息（模拟实时效果）
        setInterval(() => {
            // 这里可以实现AJAX刷新，但为了简单起见，暂时不实现
        }, 30000); // 30秒刷新一次
    </script>
</body>
</html>
