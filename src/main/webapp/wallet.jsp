<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>我的钱包 - Campus BookSwap</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .wallet-info { 
            border: 1px solid #ddd; 
            padding: 20px; 
            margin-bottom: 20px; 
            background-color: #f9f9f9;
        }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
        .form-group input { 
            padding: 8px; 
            width: 200px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
        }
        .btn { 
            padding: 10px 15px; 
            background-color: #008CBA; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
            margin-right: 10px;
        }
        .btn:hover { background-color: #007B9A; }
        .btn-withdraw { background-color: #f44336; }
        .btn-withdraw:hover { background-color: #d32f2f; }
        .error { color: red; margin-bottom: 15px; }
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

<h2>我的钱包</h2>

<c:if test="${not empty param.error}">
    <div class="error">
        <c:choose>
            <c:when test="${param.error == 'invalid_amount'}">
                金额格式不正确，请输入有效数字。
            </c:when>
            <c:otherwise>
                操作失败，请重试。
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<div class="wallet-info">
    <h3>账户余额</h3>
    <p><strong>当前余额：</strong> 
        <c:choose>
            <c:when test="${wallet != null}">
                ￥${wallet.balance}
            </c:when>
            <c:otherwise>
                ￥0.00
            </c:otherwise>
        </c:choose>
    </p>
</div>

<h3>充值/提现</h3>
<form method="post" action="wallet">
    <div class="form-group">
        <label for="amount">金额：</label>
        <input type="number" id="amount" name="amount" step="0.01" min="0.01" required>
    </div>
    
    <div class="form-group">
        <button type="submit" name="action" value="deposit" class="btn">充值</button>
        <button type="submit" name="action" value="withdraw" class="btn btn-withdraw">提现</button>
    </div>
</form>

<div class="back-link">
    <a href="index.jsp">返回首页</a>
</div>
</body>
</html>