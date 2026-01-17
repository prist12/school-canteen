<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>管理员 - 校园食堂系统</title>
  <!-- 引入统一公共样式：static/css/style.css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<!-- 管理员统一导航栏（实现功能跳转） -->
<div class="common-nav">
  <a href="#search-add-box" class="active">筛选用户</a>
  <a href="#add-form-box" >添加用户</a>
  <a href="#user-list-box" >用户列表</a>
  <a href="${pageContext.request.contextPath}/login.jsp">退出登录</a>
</div>

<!-- 统一容器 -->
<div class="common-container">
  <h1 class="common-h1">用户管理系统</h1>

  <!-- 提示信息（使用统一样式） -->
  <c:if test="${not empty msg}">
    <div class="common-msg common-msg-success">${msg}</div>
  </c:if>
  <c:if test="${not empty errorMsg}">
    <div class="common-msg common-msg-error">${errorMsg}</div>
  </c:if>

  <!-- 筛选+添加表单区域 -->
  <div class="search-add-box" id="search-add-box">
    <!-- 筛选表单 -->
    <div class="search-box">
      <h3 class="common-h3">用户筛选</h3>
      <form action="${pageContext.request.contextPath}/admin" method="get">
        <div class="common-form-group">
          <label class="common-form-label">用户类型：</label>
          <select name="userType" class="common-form-select" onchange="this.form.submit()">
            <option value="">全部类型</option>
            <option value="1" ${selectedType == 1 ? 'selected' : ''}>管理员</option>
            <option value="2" ${selectedType == 2 ? 'selected' : ''}>教师</option>
            <option value="3" ${selectedType == 3 ? 'selected' : ''}>家长</option>
            <option value="4" ${selectedType == 4 ? 'selected' : ''}>学生</option>
            <option value="5" ${selectedType == 5 ? 'selected' : ''}>餐饮员</option>
          </select>
        </div>
      </form>
    </div>

    <!-- 添加用户表单 -->
    <div class="add-form-box" id="add-form-box">
      <h3 class="common-h3">添加新用户</h3>
      <form action="${pageContext.request.contextPath}/admin/addUser" method="post">
        <div class="common-form-group">
          <label class="common-form-label">手机号：</label>
          <input type="text" name="phone" class="common-form-input" placeholder="请输入11位手机号" required>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">姓名：</label>
          <input type="text" name="userName" class="common-form-input" placeholder="请输入真实姓名" required>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">用户类型：</label>
          <select name="userType" class="common-form-select" required>
            <option value="">请选择类型</option>
            <option value="1">管理员</option>
            <option value="2">教师</option>
            <option value="3">家长</option>
            <option value="4">学生</option>
            <option value="5">餐饮员</option>
          </select>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">性别：</label>
          <select name="gender" class="common-form-select" required>
            <option value="">请选择性别</option>
            <option value="1">男</option>
            <option value="2">女</option>
            <option value="0">未知</option>
          </select>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">关联ID：</label>
          <input type="text" name="relateId" class="common-form-input" placeholder="教师-班级ID/家长-学生ID">
        </div>
        <div class="common-form-group">
          <label class="common-form-label">权限：</label>
          <textarea name="permissions" class="common-form-textarea" placeholder="JSON格式，如：['add','delete']"></textarea>
        </div>
        <div class="common-form-group" style="margin-left: 110px;">
          <button type="submit" class="common-btn common-btn-success">添加用户</button>
        </div>
      </form>
    </div>
  </div>

  <!-- 用户列表 -->
  <h3 class="common-h3" id="user-list-box">用户列表</h3>
  <c:if test="${empty userList}">
    <div class="common-empty">暂无用户数据，请先添加用户！</div>
  </c:if>
  <c:if test="${not empty userList}">
    <table class="common-table">
      <tr>
        <th>用户ID</th>
        <th>手机号</th>
        <th>姓名</th>
        <th>用户类型</th>
        <th>账号状态</th>
        <th>性别</th>
        <th>关联ID</th>
        <th>权限</th>
        <th>操作</th>
      </tr>
      <c:forEach var="user" items="${userList}">
        <tr>
          <td>${user.userid}</td>
          <td>${user.phone}</td>
          <td>${user.userName}</td>
          <td>
            <c:choose>
              <c:when test="${user.userType == 1}">管理员</c:when>
              <c:when test="${user.userType == 2}">教师</c:when>
              <c:when test="${user.userType == 3}">家长</c:when>
              <c:when test="${user.userType == 4}">学生</c:when>
              <c:when test="${user.userType == 5}">餐饮员</c:when>
              <c:otherwise>未知</c:otherwise>
            </c:choose>
          </td>
          <td>
            <span style="color: ${user.status == 1 ? 'green' : 'red'}">
                ${user.status == 1 ? '正常' : '禁用'}
            </span>
          </td>
          <td>
            <c:choose>
              <c:when test="${user.gender == 1}">男</c:when>
              <c:when test="${user.gender == 2}">女</c:when>
              <c:otherwise>未知</c:otherwise>
            </c:choose>
          </td>
          <td>${user.relateId == null ? '-' : user.relateId}</td>
          <td>${user.permissions == null ? '-' : user.permissions}</td>
          <td class="operate-btn">
            <a href="javascript:openEditModal(${user.userid})" class="common-btn common-btn-primary">编辑</a>
            <a href="${pageContext.request.contextPath}/admin/changeStatus?id=${user.userid}&status=${user.status == 1 ? 0 : 1}"
               onclick="return confirm('确定${user.status == 1 ? '禁用' : '启用'}【${user.userName}】吗？')"
               class="common-btn ${user.status == 1 ? 'common-btn-danger' : 'common-btn-success'}">
                ${user.status == 1 ? '禁用' : '启用'}
            </a>
            <a href="${pageContext.request.contextPath}/admin/deleteUser?id=${user.userid}"
               onclick="return confirm('确定删除【${user.userName}】吗？删除后不可恢复！')" class="common-btn common-btn-danger">删除</a>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</div>

<!-- 编辑用户弹窗（使用统一弹窗样式） -->
<div class="common-modal" id="editModal">
  <div class="common-modal-content">
    <span class="common-modal-close" onclick="closeEditModal()">&times;</span>
    <h2 class="common-h2">编辑用户信息</h2>
    <form id="editForm" action="${pageContext.request.contextPath}/admin/updateUser" method="post">
      <input type="hidden" name="userid" id="editUserId">

      <div class="common-form-group">
        <label class="common-form-label">手机号：</label>
        <input type="text" name="phone" id="editPhone" class="common-form-input" required>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">姓名：</label>
        <input type="text" name="userName" id="editUserName" class="common-form-input" required>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">用户类型：</label>
        <select name="userType" id="editUserType" class="common-form-select" required>
          <option value="1">管理员</option>
          <option value="2">教师</option>
          <option value="3">家长</option>
          <option value="4">学生</option>
          <option value="5">餐饮员</option>
        </select>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">账号状态：</label>
        <select name="status" id="editStatus" class="common-form-select" required>
          <option value="1">正常</option>
          <option value="0">禁用</option>
        </select>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">性别：</label>
        <select name="gender" id="editGender" class="common-form-select" required>
          <option value="1">男</option>
          <option value="2">女</option>
          <option value="0">未知</option>
        </select>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">关联ID：</label>
        <input type="text" name="relateId" id="editRelateId" class="common-form-input">
      </div>
      <div class="common-form-group">
        <label class="common-form-label">权限：</label>
        <textarea name="permissions" id="editPermissions" class="common-form-textarea"></textarea>
      </div>
      <div class="common-form-group" style="margin-left: 110px;">
        <button type="submit" class="common-btn common-btn-primary">保存修改</button>
        <button type="button" class="common-btn common-btn-danger" onclick="closeEditModal()" style="margin-left: 10px;">取消</button>
      </div>
    </form>
  </div>
</div>

<script>
  // 打开编辑弹窗并加载用户信息
  function openEditModal(userId) {
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '${pageContext.request.contextPath}/admin/api/user/' + userId, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
      if (xhr.readyState == 4) {
        if (xhr.status == 200) {
          try {
            const response = JSON.parse(xhr.responseText);
            if (response.code === 200) {
              const user = response.data;
              // 填充表单数据
              document.getElementById('editUserId').value = user.userid;
              document.getElementById('editPhone').value = user.phone;
              document.getElementById('editUserName').value = user.userName;
              document.getElementById('editUserType').value = user.userType;
              document.getElementById('editStatus').value = user.status;
              document.getElementById('editGender').value = user.gender;
              document.getElementById('editRelateId').value = user.relateId || '';
              document.getElementById('editPermissions').value = user.permissions || '';

              document.getElementById('editModal').style.display = 'block';
            } else {
              alert(response.msg);
            }
          } catch (e) {
            alert('数据解析失败');
          }
        } else {
          alert('获取用户信息失败，状态码：' + xhr.status);
        }
      }
    };
    xhr.send();
  }

  // 关闭编辑弹窗
  function closeEditModal() {
    document.getElementById('editModal').style.display = 'none';
  }

  // 点击弹窗外部关闭
  window.onclick = function(event) {
    const modal = document.getElementById('editModal');
    if (event.target == modal) {
      closeEditModal();
    }
  }
</script>
</body>
</html>