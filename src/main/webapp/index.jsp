<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Campus BookSwap - 二手书交易平台</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; }
        .nav-links { margin-top: 20px; }
        .nav-links a { 
            display: inline-block; 
            margin-right: 15px; 
            padding: 10px 15px; 
            background-color: #4CAF50; 
            color: white; 
            text-decoration: none; 
            border-radius: 4px; 
        }
        .nav-links a:hover { background-color: #45a049; }
        .search-form { margin-top: 20px; }
        .search-form input[type="text"] { 
            padding: 10px; 
            width: 300px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
        }
        .search-form input[type="submit"] { 
            padding: 10px 15px; 
            background-color: #008CBA; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
        }
        .search-form input[type="submit"]:hover { background-color: #007B9A; }
        .api-doc-link {
            margin-top: 30px;
            padding: 15px;
            background-color: #e7f3ff;
            border-left: 6px solid #2196F3;
        }
    </style>
</head>
<body>
<h1>欢迎来到校园二手书交易平台!</h1>

<div class="search-form">
    <form action="search" method="get">
        <input type="text" name="keyword" placeholder="输入书名或作者关键词..." required>
        <input type="submit" value="搜索">
    </form>
</div>

<div class="nav-links">
    <a href="login">登录</a>
    <a href="register">注册</a>
    <a href="books">浏览书籍</a>
    <a href="addBook">发布书籍</a>
</div>

<div class="api-doc-link">
    <h3>API文档</h3>
    <p>本平台提供RESTful API供开发者使用：</p>
    <ul>
        <li><a href="api/books">获取所有书籍列表</a> - JSON格式</li>
        <li><a href="swagger-ui/index.html">Swagger UI文档</a> - API交互式文档</li>
    </ul>
</div>
</body>
</html>