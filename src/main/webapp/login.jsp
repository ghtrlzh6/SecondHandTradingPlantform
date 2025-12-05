<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户登录 - Campus BookSwap</title>
</head>
<body>
<h2>用户登录</h2>
<form action="login" method="post">
    <label for="username">用户名:</label>
    <input type="text" id="username" name="username" required><br><br>
    
    <label for="password">密码:</label>
    <input type="password" id="password" name="password" required><br><br>
    
    <input type="submit" value="登录">
</form>
<p><a href="register.jsp">没有账户？点击注册</a></p>
</body>
</html>