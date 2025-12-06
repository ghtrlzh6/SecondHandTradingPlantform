<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>消息交流 - Campus BookSwap</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .book-info { 
            border: 1px solid #ddd; 
            padding: 20px; 
            margin-bottom: 20px; 
            background-color: #f9f9f9;
        }
        .messages-container {
            border: 1px solid #ddd;
            padding: 20px;
            margin-bottom: 20px;
            max-height: 400px;
            overflow-y: auto;
        }
        .message {
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 4px;
        }
        .message.sent {
            background-color: #e3f2fd;
            text-align: right;
        }
        .message.received {
            background-color: #f5f5f5;
        }
        .message-header {
            font-size: 0.9em;
            color: #666;
            margin-bottom: 5px;
        }
        .message-content {
            margin: 5px 0;
        }
        .message-form {
            margin-top: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group textarea {
            width: 100%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            height: 80px;
            resize: vertical;
        }
        .btn {
            padding: 10px 15px;
            background-color: #008CBA;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #007B9A;
        }
        .back-link {
            margin-top: 20px;
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

<h2>与卖家交流</h2>

<div class="book-info">
    <h3>${book.title}</h3>
    <p><strong>作者:</strong> ${book.author}</p>
    <p><strong>价格:</strong> ￥${book.price}</p>
</div>

<div class="messages-container">
    <c:if test="${empty messages}">
        <p>暂无消息，发起第一个对话吧！</p>
    </c:if>
    
    <c:forEach var="message" items="${messages}">
        <div class="message <c:if test='${message.senderId == user.id}'>sent</c:if><c:if test='${message.senderId != user.id}'>received</c:if>">
            <div class="message-header">
                <c:if test="${message.senderId == user.id}">我</c:if>
                <c:if test="${message.senderId != user.id}">卖家</c:if>
                - ${message.sentAt}
            </div>
            <div class="message-content">${message.content}</div>
        </div>
    </c:forEach>
</div>

<div class="message-form">
    <form method="post" action="messages">
        <input type="hidden" name="bookId" value="${book.id}">
        <input type="hidden" name="receiverId" value="${book.sellerId}">
        
        <div class="form-group">
            <textarea name="content" placeholder="输入您的消息..." required></textarea>
        </div>
        
        <div class="form-group">
            <button type="submit" class="btn">发送消息</button>
        </div>
    </form>
</div>

<div class="back-link">
    <a href="book?id=${book.id}">返回书籍详情</a> | <a href="books">返回书籍列表</a>
</div>
</body>
</html>