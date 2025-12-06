<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 如果用户已登录，重定向到主页
    if (session.getAttribute("user") != null) {
        response.sendRedirect("index.jsp");
        return;
    }
%>
<html>
<head>
    <title>用户登录 - Campus BookSwap</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            margin: 0;
            padding: 0;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .login-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 15px 30px rgba(0,0,0,0.2);
            width: 90%;
            max-width: 400px;
        }
        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: bold;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box;
        }
        input[type="submit"] {
            width: 100%;
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        .links {
            text-align: center;
            margin-top: 20px;
        }
        .links a {
            color: #666;
            text-decoration: none;
        }
        .links a:hover {
            text-decoration: underline;
        }
        .error-message {
            color: red;
            margin-bottom: 20px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>用户登录</h2>
    
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="error-message"><%= request.getAttribute("errorMessage") %></div>
    <% } %>
    
    <form action="login" method="post">
        <label for="username">用户名:</label>
        <input type="text" id="username" name="username" required>
        
        <label for="password">密码:</label>
        <input type="password" id="password" name="password" required>
        
        <input type="submit" value="登录">
    </form>
    
    <div class="links">
        <a href="register.jsp">没有账户？点击注册</a> | 
        <a href="welcome.jsp">返回首页</a>
    </div>
</div>
</body>
</html>