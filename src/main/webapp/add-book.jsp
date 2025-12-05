<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>发布书籍 - Campus BookSwap</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        form { max-width: 500px; }
        label { display: block; margin-top: 10px; font-weight: bold; }
        input, textarea { width: 100%; padding: 8px; box-sizing: border-box; }
        textarea { height: 100px; }
        .error { color: red; margin-top: 10px; }
        .button { margin-top: 20px; }
    </style>
</head>
<body>
<h2>发布书籍</h2>

<c:if test="${not empty errorMessage}">
    <div class="error">${errorMessage}</div>
</c:if>

<form action="addBook" method="post">
    <label for="title">书名:</label>
    <input type="text" id="title" name="title" required>
    
    <label for="author">作者:</label>
    <input type="text" id="author" name="author" required>
    
    <label for="price">价格:</label>
    <input type="number" id="price" name="price" step="0.01" min="0" required>
    
    <label for="description">描述:</label>
    <textarea id="description" name="description"></textarea>
    
    <label for="imageUrl">图片URL:</label>
    <input type="url" id="imageUrl" name="imageUrl">
    
    <div class="button">
        <input type="submit" value="发布">
        <input type="reset" value="重置">
    </div>
</form>

<p><a href="index.jsp">返回首页</a></p>
</body>
</html>