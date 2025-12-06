<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>我的订单 - Campus BookSwap</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .back-link { margin-top: 20px; }
        .status-pending { color: #ff9800; }
        .status-paid { color: #2196f3; }
        .status-shipped { color: #9c27b0; }
        .status-completed { color: #4caf50; }
        .status-cancelled { color: #f44336; }
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

<h2>我的订单</h2>

<c:if test="${empty orders}">
    <p>您还没有任何订单。</p>
</c:if>

<c:if test="${not empty orders}">
    <table>
        <thead>
            <tr>
                <th>订单号</th>
                <th>商品</th>
                <th>状态</th>
                <th>下单时间</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="order" items="${orders}">
                <tr>
                    <td>${order.id}</td>
                    <td><!-- 这里需要关联书籍信息 --></td>
                    <td>
                        <span class="
                            <c:if test='${order.status == "pending"}'>status-pending</c:if>
                            <c:if test='${order.status == "paid"}'>status-paid</c:if>
                            <c:if test='${order.status == "shipped"}'>status-shipped</c:if>
                            <c:if test='${order.status == "completed"}'>status-completed</c:if>
                            <c:if test='${order.status == "cancelled"}'>status-cancelled</c:if>
                        ">
                            <c:choose>
                                <c:when test='${order.status == "pending"}'>待支付</c:when>
                                <c:when test='${order.status == "paid"}'>已支付</c:when>
                                <c:when test='${order.status == "shipped"}'>已发货</c:when>
                                <c:when test='${order.status == "completed"}'>已完成</c:when>
                                <c:when test='${order.status == "cancelled"}'>已取消</c:when>
                                <c:otherwise>${order.status}</c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td>${order.orderedAt}</td>
                    <td>
                        <c:if test='${order.status == "shipped"}'>
                            <form method="post" action="confirm-delivery" style="display: inline;">
                                <input type="hidden" name="orderId" value="${order.id}">
                                <button type="submit">确认收货</button>
                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</c:if>

<div class="back-link">
    <a href="index.jsp">返回首页</a>
</div>
</body>
</html>