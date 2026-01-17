<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>校园食堂 - 注册</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }
        body {
            background: #f5f5f5;
            padding: 50px;
        }
        .register-box {
            width: 400px;
            margin: 0 auto;
            background: white;
            padding: 30px;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .title {
            text-align: center;
            margin-bottom: 20px;
            font-size: 20px;
            color: #333;
        }
        .form-item {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #666;
            font-size: 14px;
        }
        input, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 3px;
            font-size: 14px;
        }
        input:focus, select:focus {
            outline: none;
            border-color: #409eff;
        }
        .btn {
            width: 100%;
            padding: 10px;
            background: #409eff;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            font-size: 16px;
        }
        .btn:hover {
            background: #337ecc;
        }
        .tip {
            color: #f56c6c;
            font-size: 12px;
            margin-top: 5px;
            display: none;
        }
        .link {
            text-align: center;
            margin-top: 15px;
            font-size: 14px;
        }
        .link a {
            color: #409eff;
            text-decoration: none;
        }
        /* 新增：提示信息样式 */
        .msg {
            text-align: center;
            margin-bottom: 15px;
            padding: 10px;
            border-radius: 3px;
            font-size: 14px;
        }
        .msg-error {
            color: #f56c6c;
            background: #fef0f0;
            border: 1px solid #fde2e2;
        }
        .msg-success {
            color: #67c23a;
            background: #f0f9ff;
            border: 1px solid #e6f7ff;
        }
    </style>
</head>
<body>
<div class="register-box">
    <h3 class="title">用户注册</h3>

    <!-- 新增：后端返回的错误提示 -->
    <c:if test="${not empty errorMsg}">
        <div class="msg msg-error">${errorMsg}</div>
    </c:if>

    <form id="regForm" action="${pageContext.request.contextPath}/register" method="post">
        <!-- 原有表单内容保留不变 -->
        <div class="form-item">
            <label>帐号 *</label>
            <input type="tel" name="phone" id="phone" placeholder="请输入11位数字" maxlength="11">
            <div class="tip" id="phoneTip">请输入11位数字</div>
        </div>

        <div class="form-item">
            <label>密码 *</label>
            <input type="password" name="password" id="pwd" placeholder="6-20位字母+数字">
            <div class="tip" id="pwdTip">密码需6-20位字母+数字组合</div>
        </div>

        <div class="form-item">
            <label>真实姓名 *</label>
            <input type="text" name="userName" id="name" placeholder="请输入真实姓名">
            <div class="tip" id="nameTip">姓名不能为空</div>
        </div>

        <div class="form-item">
            <label>用户类型 *</label>
            <select name="userType" id="type">
                <option value="">请选择</option>
                <option value="1">管理员</option>
                <option value="2">教师</option>
                <option value="3">家长</option>
                <option value="4">学生</option>
                <option value="5">餐饮</option>
            </select>
            <div class="tip" id="typeTip">请选择用户类型</div>
        </div>

        <div class="form-item">
            <label>性别</label>
            <label style="display: inline-block; margin-right: 15px;">
                <input type="radio" name="gender" value="1" checked> 男
            </label>
            <label style="display: inline-block;">
                <input type="radio" name="gender" value="2"> 女
            </label>
        </div>

        <button type="submit" class="btn">注册</button>
        <div class="link">
            已有账号？<a href="${pageContext.request.contextPath}/login">立即登录</a>
        </div>
    </form>
</div>

<script>
    // 原有前端验证逻辑保留不变
    document.getElementById("regForm").onsubmit = function() {
        let flag = true;

        const phone = document.getElementById("phone").value.trim();
        if (!/^1[3-9]\d{9}$/.test(phone)) {
            document.getElementById("phoneTip").style.display = "block";
            flag = false;
        } else {
            document.getElementById("phoneTip").style.display = "none";
        }

        const pwd = document.getElementById("pwd").value.trim();
        const pwdReg = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{6,20}$/;
        if (!pwdReg.test(pwd)) {
            document.getElementById("pwdTip").style.display = "block";
            flag = false;
        } else {
            document.getElementById("pwdTip").style.display = "none";
        }

        const name = document.getElementById("name").value.trim();
        if (name === "") {
            document.getElementById("nameTip").style.display = "block";
            flag = false;
        } else {
            document.getElementById("nameTip").style.display = "none";
        }

        const type = document.getElementById("type").value;
        if (type === "") {
            document.getElementById("typeTip").style.display = "block";
            flag = false;
        } else {
            document.getElementById("typeTip").style.display = "none";
        }

        return flag;
    };

    const allInputs = document.querySelectorAll("input, select");
    allInputs.forEach(function(el) {
        el.addEventListener('focus', function() {
            const tipElem = document.getElementById(this.id + "Tip");
            if (tipElem) {
                tipElem.style.display = "none";
            }
        });
    });
</script>
</body>
</html>