<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>欢迎 - Campus BookSwap</title>
    <style>
        body { 
            font-family: Arial, sans-serif; 
            margin: 0;
            padding: 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        .welcome-container {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 15px 30px rgba(0,0,0,0.2);
            text-align: center;
            max-width: 500px;
            width: 90%;
        }
        h1 {
            color: #333;
            margin-bottom: 30px;
            font-size: 2.5em;
        }
        .nav-links {
            display: flex;
            justify-content: center;
            gap: 20px;
            flex-wrap: wrap;
        }
        .nav-links a { 
            display: inline-block; 
            padding: 15px 30px; 
            background-color: #4CAF50; 
            color: white; 
            text-decoration: none; 
            border-radius: 5px; 
            font-size: 1.2em;
            transition: background-color 0.3s;
        }
        .nav-links a:hover { 
            background-color: #45a049; 
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        .description {
            color: #666;
            margin: 30px 0;
            line-height: 1.6;
            font-size: 1.1em;
        }
    </style>
</head>
<body>
    <div class="welcome-container">
        <h1>欢迎来到校园二手书交易平台</h1>
        
        <div class="description">
            <p>在这里，您可以轻松买卖二手教材和图书，为环保和节约贡献力量。</p>
            <p>请登录或注册以开始使用我们的服务。</p>
        </div>
        
        <div class="nav-links">
            <a href="login">用户登录</a>
            <a href="register">用户注册</a>
        </div>
    </div>
</body>
</html>