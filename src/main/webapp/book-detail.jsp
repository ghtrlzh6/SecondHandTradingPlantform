<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>书籍详情 - Campus BookSwap</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .book-info { margin-bottom: 20px; }
        .book-image { max-width: 200px; height: auto; }
        .back-link { margin-top: 20px; }
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

<h2>书籍详情</h2>

<div class="book-info">
    <h3>${book.title}</h3>
    <p><strong>作者:</strong> ${book.author}</p>
    <p><strong>价格:</strong> ￥${book.price}</p>
    <p><strong>描述:</strong> ${book.description}</p>
    <c:if test="${not empty book.imageUrl}">
        <img src="${book.imageUrl}" alt="${book.title}" class="book-image">
    </c:if>
    <p><strong>发布时间:</strong> ${book.createdAt}</p>
</div>

<div class="back-link">
    <a href="books">返回书籍列表</a> | <a href="index.jsp">返回首页</a>
</div>
</body>
</html>