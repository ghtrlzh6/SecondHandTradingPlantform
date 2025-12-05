<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>书籍列表 - Campus BookSwap</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .pagination { margin-top: 20px; }
        .pagination a { margin: 0 5px; text-decoration: none; color: blue; }
        .pagination a:hover { text-decoration: underline; }
        .current-page { font-weight: bold; }
        .search-form { margin-bottom: 20px; }
        .search-form input[type="text"] { 
            padding: 8px; 
            width: 300px; 
            border: 1px solid #ccc; 
            border-radius: 4px; 
        }
        .search-form input[type="submit"] { 
            padding: 8px 12px; 
            background-color: #008CBA; 
            color: white; 
            border: none; 
            border-radius: 4px; 
            cursor: pointer; 
        }
        .search-form input[type="submit"]:hover { background-color: #007B9A; }
    </style>
</head>
<body>
<h2>书籍列表</h2>

<div class="search-form">
    <form action="search" method="get">
        <input type="text" name="keyword" placeholder="输入书名或作者关键词..." 
               value="${not empty keyword ? keyword : ''}" required>
        <input type="submit" value="搜索">
    </form>
</div>

<c:if test="${not empty keyword}">
    <p>搜索"${keyword}"的结果，共找到${totalCount}条记录</p>
</c:if>

<table>
    <thead>
        <tr>
            <th>书名</th>
            <th>作者</th>
            <th>价格</th>
            <th>发布时间</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="book" items="${books}">
            <tr>
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>￥${book.price}</td>
                <td>${book.createdAt}</td>
                <td><a href="book?id=${book.id}">查看详情</a></td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<div class="pagination">
    <c:if test="${hasPreviousPage}">
        <a href="?page=${previousPage}&keyword=${keyword}">上一页</a>
    </c:if>
    
    <span>第 ${currentPage} 页，共 ${totalPages} 页，总计 ${totalCount} 条记录</span>
    
    <c:if test="${hasNextPage}">
        <a href="?page=${nextPage}&keyword=${keyword}">下一页</a>
    </c:if>
</div>

<p><a href="index.jsp">返回首页</a></p>
</body>
</html>