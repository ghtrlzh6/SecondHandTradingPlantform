<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户注册 - Campus BookSwap</title>
</head>
<body>
<h2>用户注册</h2>
<form action="register" method="post">
    <label for="username">用户名:</label>
    <input type="text" id="username" name="username" required><br><br>
    
    <label for="password">密码:</label>
    <input type="password" id="password" name="password" required><br><br>
    
    <label for="email">邮箱:</label>
    <input type="email" id="email" name="email" required><br><br>
    
    <input type="submit" value="注册">
</form>
<p><a href="login.jsp">已有账户？点击登录</a></p>
</body>
</html>