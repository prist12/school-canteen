<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>登录 - 校园食堂系统</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
    <style>
        .container {
            max-width: 400px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            font-weight: bold;
        }
        input, select {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .btn {
            padding: 8px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        .btn-primary {
            background-color: #007bff;
            color: white;
            margin-right: 10px;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        .error-message {
            color: #dc3545;
            margin-top: 10px;
            padding: 8px;
            border: 1px solid #f5c6cb;
            border-radius: 4px;
            background-color: #f8d7da;
        }
    </style>
</head>
<body>
<div class="container">
    <h1 style="text-align: center;">校园食堂系统登录</h1>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <div class="form-group">
            <label for="phone">登录账号</label>
            <input type="tel" id="phone" name="phone"
                   pattern="1[3-9]\d{9}"
                   title="请输入有效的11位手机号"
                   required>
        </div>

        <div class="form-group">
            <label for="password">密码</label>
            <input type="password" id="password" name="password"
                   minlength="6"
                   title="密码长度不能少于6位"
                   required>
        </div>

        <div class="form-group">
            <label for="userType">用户类型</label>
            <select id="userType" name="userType" required>
                <option value="">--请选择用户类型--</option>
                <option value="1">系统管理员</option>
                <option value="2">教师</option>
                <option value="3">家长</option>
                <option value="4">学生</option>
                <option value="5">餐饮员</option>
            </select>
        </div>

        <div style="margin-top: 20px; text-align: center;">
            <button type="submit" class="btn btn-primary">登录</button>
            <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-secondary">注册</a>
        </div>
    </form>

    <!-- 错误信息展示（支持EL表达式和传统方式兼容） -->
    <c:if test="${not empty errorMsg || not empty requestScope.error}">
        <div class="error-message">
            <c:choose>
                <c:when test="${not empty errorMsg}">${errorMsg}</c:when>
                <c:otherwise><%= request.getAttribute("error") %></c:otherwise>
            </c:choose>
        </div>
    </c:if>
</div>
</body>
</html>