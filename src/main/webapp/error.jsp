<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>错误 - 校园食堂系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<div class="container">
    <h1>操作失败</h1>
    <div style="padding:20px;background:#ffe6e6;border:1px solid #ff9999;border-radius:5px;">
        <h3>错误信息:</h3>
        <p><%= request.getAttribute("error") %></p>
    </div>
    <a href="javascript:history.back()" class="btn">返回上一页</a>
    <a href="login.jsp" class="btn btn-primary">返回首页</a>
</div>
</body>
</html>