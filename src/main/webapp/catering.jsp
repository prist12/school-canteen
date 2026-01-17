<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>餐饮员 - 校园食堂系统</title>
  <!-- 引入统一公共样式：static/css/style.css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<!-- 餐饮员统一导航栏（实现功能跳转） -->
<div class="common-nav">
  <a href="#menu" class="active">添加菜品</a>
  <a href="#daily-menu">当日菜单</a>
  <a href="#stock">库存管理</a>
  <a href="${pageContext.request.contextPath}/login.jsp">退出登录</a>
</div>

<!-- 统一容器 -->
<div class="common-container">
  <h1 class="common-h1">菜品库存管理系统</h1>

  <!-- 提示信息（使用统一样式） -->
  <c:if test="${not empty msg}">
    <div class="common-msg common-msg-success">${msg}</div>
  </c:if>
  <c:if test="${not empty errorMsg}">
    <div class="common-msg common-msg-error">${errorMsg}</div>
  </c:if>

  <!-- 添加菜品（统一卡片样式） -->
  <div class="common-card" id="menu">
    <h2 class="common-h2">添加菜品</h2>
    <form action="${pageContext.request.contextPath}/catering/dish/add" method="post">
      <div class="common-form-group">
        <label class="common-form-label">菜品名称：</label>
        <input type="text" name="dishName" class="common-form-input" required>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">价格：</label>
        <input type="number" name="price" step="0.01" class="common-form-input" required>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">餐段：</label>
        <select name="mealType" class="common-form-select" required>
          <option value="1">早餐</option>
          <option value="2">午餐</option>
          <option value="3">晚餐</option>
        </select>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">日期：</label>
        <input type="date" name="dishDate" class="common-form-input" required>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">库存数量：</label>
        <input type="number" name="totalStock" min="1" class="common-form-input" required>
      </div>
      <div class="common-form-group">
        <label class="common-form-label">预警阈值：</label>
        <input type="number" name="threshold" value="10" class="common-form-input" required>
      </div>
      <div class="common-form-group" style="margin-left: 110px;">
        <button type="submit" class="common-btn common-btn-success">添加菜品</button>
      </div>
    </form>
<div class="daliy-menu" id="daily-menu">
    <!-- 当日早餐菜单 -->
    <h3 class="common-h3">当日早餐菜单</h3>
    <c:if test="${empty breakfastList}">
      <div class="common-empty">暂无早餐菜品</div>
    </c:if>
    <c:if test="${not empty breakfastList}">
      <table class="common-table">
        <tr>
          <th>菜品名称</th>
          <th>价格</th>
          <th>库存</th>
          <th>销量</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        <c:forEach var="dish" items="${breakfastList}">
          <tr>
            <td>${dish.dishName}</td>
            <td>¥<fmt:formatNumber value="${dish.price}" minFractionDigits="2"/></td>
            <td>${dish.remainingStock}</td>
            <td>${dish.soldCount}</td>
            <td>${dish.status == 1 ? '上架' : '下架'}</td>
            <td class="operate-btn">
              <a href="${pageContext.request.contextPath}/catering/dish/edit/${dish.dishid}" class="common-btn common-btn-primary">修改</a>
              <a href="${pageContext.request.contextPath}/catering/dish/toggleStatus/${dish.dishid}/${dish.status == 1 ? 0 : 1}" class="common-btn common-btn-warning">${dish.status == 1 ? '下架' : '上架'}</a>
              <a href="${pageContext.request.contextPath}/catering/dish/delete/${dish.dishid}" class="common-btn common-btn-danger" onclick="return confirm('确认删除？')">删除</a>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>

    <!-- 当日午餐菜单 -->
    <h3 class="common-h3">当日午餐菜单</h3>
    <c:if test="${empty lunchList}">
      <div class="common-empty">暂无午餐菜品</div>
    </c:if>
    <c:if test="${not empty lunchList}">
      <table class="common-table">
        <tr>
          <th>菜品名称</th>
          <th>价格</th>
          <th>库存</th>
          <th>销量</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        <c:forEach var="dish" items="${lunchList}">
          <tr>
            <td>${dish.dishName}</td>
            <td>¥<fmt:formatNumber value="${dish.price}" minFractionDigits="2"/></td>
            <td>${dish.remainingStock}</td>
            <td>${dish.soldCount}</td>
            <td>${dish.status == 1 ? '上架' : '下架'}</td>
            <td class="operate-btn">
              <a href="${pageContext.request.contextPath}/catering/dish/edit/${dish.dishid}" class="common-btn common-btn-primary">修改</a>
              <a href="${pageContext.request.contextPath}/catering/dish/toggleStatus/${dish.dishid}/${dish.status == 1 ? 0 : 1}" class="common-btn common-btn-warning">${dish.status == 1 ? '下架' : '上架'}</a>
              <a href="${pageContext.request.contextPath}/catering/dish/delete/${dish.dishid}" class="common-btn common-btn-danger" onclick="return confirm('确认删除？')">删除</a>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>

    <!-- 当日晚餐菜单 -->
    <h3 class="common-h3">当日晚餐菜单</h3>
    <c:if test="${empty dinnerList}">
      <div class="common-empty">暂无晚餐菜品</div>
    </c:if>
    <c:if test="${not empty dinnerList}">
      <table class="common-table">
        <tr>
          <th>菜品名称</th>
          <th>价格</th>
          <th>库存</th>
          <th>销量</th>
          <th>状态</th>
          <th>操作</th>
        </tr>
        <c:forEach var="dish" items="${dinnerList}">
          <tr>
            <td>${dish.dishName}</td>
            <td>¥<fmt:formatNumber value="${dish.price}" minFractionDigits="2"/></td>
            <td>${dish.remainingStock}</td>
            <td>${dish.soldCount}</td>
            <td>${dish.status == 1 ? '上架' : '下架'}</td>
            <td class="operate-btn">
              <a href="${pageContext.request.contextPath}/catering/dish/edit/${dish.dishid}" class="common-btn common-btn-primary">修改</a>
              <a href="${pageContext.request.contextPath}/catering/dish/toggleStatus/${dish.dishid}/${dish.status == 1 ? 0 : 1}" class="common-btn common-btn-warning">${dish.status == 1 ? '下架' : '上架'}</a>
              <a href="${pageContext.request.contextPath}/catering/dish/delete/${dish.dishid}" class="common-btn common-btn-danger" onclick="return confirm('确认删除？')">删除</a>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>
  </div>
  <!-- 库存管理（统一卡片样式） -->
  <div class="common-card" id="stock">
    <h2 class="common-h2">库存预警</h2>
    <c:if test="${empty lowStockList}">
      <div class="common-empty">暂无库存预警菜品</div>
    </c:if>
    <c:if test="${not empty lowStockList}">
      <table class="common-table">
        <tr>
          <th>菜品</th>
          <th>当前库存</th>
          <th>预警值</th>
          <th>操作</th>
        </tr>
        <c:forEach var="dish" items="${lowStockList}">
          <tr>
            <td>${dish.dishName}</td>
            <td style="color:red;">${dish.remainingStock}</td>
            <td>${dish.threshold}</td>
            <td>
              <form action="${pageContext.request.contextPath}/catering/dish/addStock" method="post" style="display:inline;">
                <input type="hidden" name="dishid" value="${dish.dishid}">
                <label class="common-form-label" style="width: 80px;">补货数量：</label>
                <input type="number" name="quantity" value="50" style="width:80px;" min="1" required class="common-form-input">
                <button type="submit" class="common-btn common-btn-warning">补货</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>

  <!-- 菜品修改弹窗（简化版，统一样式） -->
  <c:if test="${not empty editDish}">
    <div class="common-card">
      <h2 class="common-h2">修改菜品</h2>
      <form action="${pageContext.request.contextPath}/catering/dish/update" method="post">
        <input type="hidden" name="dishid" value="${editDish.dishid}">
        <div class="common-form-group">
          <label class="common-form-label">菜品名称：</label>
          <input type="text" name="dishName" value="${editDish.dishName}" class="common-form-input" required>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">价格：</label>
          <input type="number" name="price" step="0.01" value="${editDish.price}" class="common-form-input" required>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">餐段：</label>
          <select name="mealType" class="common-form-select" required>
            <option value="1" ${editDish.mealType == 1 ? 'selected' : ''}>早餐</option>
            <option value="2" ${editDish.mealType == 2 ? 'selected' : ''}>午餐</option>
            <option value="3" ${editDish.mealType == 3 ? 'selected' : ''}>晚餐</option>
          </select>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">预警阈值：</label>
          <input type="number" name="threshold" value="${editDish.threshold}" class="common-form-input" required>
        </div>
        <div class="common-form-group">
          <label class="common-form-label">状态：</label>
          <select name="status" class="common-form-select" required>
            <option value="1" ${editDish.status == 1 ? 'selected' : ''}>上架</option>
            <option value="0" ${editDish.status == 0 ? 'selected' : ''}>下架</option>
          </select>
        </div>
        <div class="common-form-group" style="margin-left: 110px;">
          <button type="submit" class="common-btn common-btn-primary">保存修改</button>
        </div>
      </form>
    </div>
  </c:if>
</div>
</body>
</html>