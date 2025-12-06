<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>购买书籍 - Campus BookSwap</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .book-info { 
            border: 1px solid #ddd; 
            padding: 20px; 
            margin-bottom: 20px; 
            background-color: #f9f9f9;
        }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input, .form-group textarea { 
            padding: 8px; 
            width: 300px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
        }
        .form-group textarea { 
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
        .btn:hover { background-color: #007B9A; }
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

<h2>购买书籍</h2>

<div class="book-info">
    <h3>${book.title}</h3>
    <p><strong>作者:</strong> ${book.author}</p>
    <p><strong>价格:</strong> ￥${book.price}</p>
    <p><strong>描述:</strong> ${book.description}</p>
</div>

<h3>填写订单信息</h3>
<form method="post" action="buy">
    <input type="hidden" name="bookId" value="${book.id}">
    
    <div class="form-group">
        <label for="shippingAddress">收货地址：</label>
        <textarea id="shippingAddress" name="shippingAddress" required></textarea>
    </div>
    
    <div class="form-group">
        <label for="paymentPassword">支付密码：</label>
        <input type="password" id="paymentPassword" name="paymentPassword">
        <small>如果您尚未设置支付密码，可以留空</small>
    </div>
    
    <div class="form-group">
        <button type="submit" class="btn">确认购买</button>
    </div>
</form>

<div class="back-link">
    <a href="book?id=${book.id}">返回书籍详情</a> | <a href="books">返回书籍列表</a>
</div>
</body>
</html>