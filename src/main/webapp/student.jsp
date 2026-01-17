<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>学生 - 校园食堂系统</title>
  <!-- 引入统一公共样式：static/css/style.css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
  <script>
    // 点餐表单校验
    function checkOrderForm() {
      const checkboxes = document.getElementsByName("dishIds");
      let hasSelected = false;
      for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
          hasSelected = true;
          break;
        }
      }
      if (!hasSelected) {
        alert("请至少选择一道菜品后提交！");
        return false;
      }

      for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
          const dishId = checkboxes[i].value;
          const quantityInput = document.getElementsByName("quantity_" + dishId)[0];
          const quantity = parseInt(quantityInput.value);
          const maxStock = parseInt(quantityInput.max);

          if (isNaN(quantity) || quantity < 1 || quantity > maxStock) {
            alert(`菜品【${checkboxes[i].dataset.dishName}】的数量需在1-${maxStock}之间！`);
            quantityInput.focus();
            return false;
          }
        }
      }
      return confirm("确认提交点餐订单吗？");
    }

    // 日期筛选提交
    function submitDateFilter() {
      const dateInput = document.getElementById("queryDate");
      const mealType = document.getElementById("currentMealType").value;
      if (dateInput.value) {
        window.location.href = "${pageContext.request.contextPath}/student?date=" + dateInput.value + "&mealType=" + mealType;
      } else {
        alert("请选择查询日期！");
      }
    }
  </script>
</head>
<body>
<!-- 学生统一导航栏（最终正确版：仅锚点+退出登录，无无效后端路径） -->
<div class="common-nav">
  <a href="#balance" class="active">菜品点餐</a> <!-- 锚点：对应账户余额/菜品模块 id="balance" -->
  <a href="#orders">消费记录</a> <!-- 锚点：对应消费记录模块 id="orders" -->
  <a href="#recharge">账户充值</a> <!-- 锚点：对应充值表单模块 id="recharge" -->
  <a href="#summary">账户汇总</a> <!-- 锚点：对应账户汇总模块 id="summary" -->
  <a href="${pageContext.request.contextPath}/login.jsp">退出登录</a> <!-- 仅此项为后端页面跳转，保留不变 -->
</div>

<!-- 统一容器 -->
<div class="common-container">
  <div style="text-align: right; margin-bottom: 10px; font-weight: bold; color: #333;">
    欢迎您，${userName}
  </div>

  <h1 class="common-h1">学生点餐系统</h1>

  <!-- 提示信息（使用统一样式） -->
  <c:if test="${not empty errorMsg}">
    <div class="common-msg common-msg-error">${errorMsg}</div>
  </c:if>
  <c:if test="${not empty msg}">
    <div class="common-msg common-msg-success">${msg}</div>
  </c:if>

  <!-- 账户余额 -->
  <div class="common-card" id="balance">
    <h2 class="common-h2">账户余额: ¥<fmt:formatNumber value="${balance}" type="number" minFractionDigits="2"/></h2>
  </div>

  <!-- 今日菜单 -->
  <div class="common-card">
    <h2 class="common-h2">今日菜单</h2>
    <div style="margin: 10px 0;">
      <a href="${pageContext.request.contextPath}/student?mealType=1" class="common-btn ${currentMealType == 1 ? 'common-btn-primary' : 'common-btn-warning'}">早餐</a>
      <a href="${pageContext.request.contextPath}/student?mealType=2" class="common-btn ${currentMealType == 2 ? 'common-btn-primary' : 'common-btn-warning'}">午餐</a>
      <a href="${pageContext.request.contextPath}/student?mealType=3" class="common-btn ${currentMealType == 3 ? 'common-btn-primary' : 'common-btn-warning'}">晚餐</a>
    </div>

    <c:if test="${empty dishList}">
      <div class="common-empty">
        <c:choose>
          <c:when test="${currentMealType == 1}">暂无今日早餐菜品</c:when>
          <c:when test="${currentMealType == 2}">暂无今日午餐菜品</c:when>
          <c:when test="${currentMealType == 3}">暂无今日晚餐菜品</c:when>
          <c:otherwise>暂无菜品，请选择餐段</c:otherwise>
        </c:choose>
      </div>
    </c:if>

    <c:if test="${not empty dishList}">
      <form action="${pageContext.request.contextPath}/student/order" method="post" onsubmit="return checkOrderForm()">
        <input type="hidden" name="mealType" value="${currentMealType}">
        <table class="common-table">
          <tr>
            <th>菜品日期</th>
            <th>菜品名称</th>
            <th>单价</th>
            <th>剩余库存</th>
            <th>选择</th>
          </tr>
          <c:forEach var="dish" items="${dishList}">
            <c:set var="realStock" value="${dish.remainingStock == null ? (dish.totalStock - dish.soldCount) : dish.remainingStock}"/>
            <tr>
              <td><fmt:formatDate value="${dish.dishDate}" pattern="yyyy-MM-dd"/></td>
              <td>${dish.dishName}</td>
              <td>¥<fmt:formatNumber value="${dish.price}" minFractionDigits="2"/></td>
              <td>${realStock}</td>
              <td>
                <input type="checkbox" name="dishIds" value="${dish.dishid}" data-dish-name="${dish.dishName}">
                <label class="common-form-label" style="width: 50px;">数量：</label>
                <input type="number" name="quantity_${dish.dishid}" min="1" max="${realStock}" value="1" class="common-form-input" style="width: 80px;">
              </td>
            </tr>
          </c:forEach>
        </table>
        <div style="margin-top: 15px;">
          <button type="submit" class="common-btn common-btn-success">确认点餐</button>
        </div>
      </form>
    </c:if>
  </div>

  <!-- 充值功能 -->
  <div class="common-card" id="recharge">
    <h2 class="common-h2">账户充值</h2>
    <form action="${pageContext.request.contextPath}/student/api/student/recharge" method="post" onsubmit="return confirm('确认充值该金额吗？')">
      <div class="common-form-group">
        <label class="common-form-label">充值金额：</label>
        <input type="number" name="amount" min="1" step="0.01" value="50" required class="common-form-input">
        <label style="margin-left: 10px;">元</label>
      </div>
      <div class="common-form-group" style="margin-left: 110px;">
        <button type="submit" class="common-btn common-btn-primary">立即充值</button>
      </div>
    </form>
  </div>

  <!-- 日期筛选 -->
  <div class="common-card">
    <div class="common-form-group">
      <label class="common-form-label">查询日期：</label>
      <c:if test="${not empty queryDate}">
        <fmt:formatDate value="${queryDate}" pattern="yyyy-MM-dd" var="formattedQueryDate"/>
      </c:if>
      <input type="date" id="queryDate" value="${formattedQueryDate != null ? formattedQueryDate : ''}" class="common-form-input">
      <input type="hidden" id="currentMealType" value="${currentMealType}">
      <button type="button" onclick="submitDateFilter()" class="common-btn common-btn-info" style="margin-left: 10px;">查询该日期记录</button>
    </div>
  </div>

  <!-- 消费记录 -->
  <div class="common-card" id="orders">
    <h2 class="common-h2">消费记录</h2>
    <c:if test="${empty orderList}">
      <div class="common-empty">暂无消费记录</div>
    </c:if>
    <c:if test="${not empty orderList}">
      <table class="common-table">
        <tr>
          <th>消费时间</th>
          <th>订单号</th>
          <th>餐段</th>
          <th>消费金额</th>
          <th>订单状态</th>
        </tr>
        <c:forEach var="order" items="${orderList}">
          <tr>
            <td><fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${order.orderNo}</td>
            <td>
              <c:choose>
                <c:when test="${order.mealType == 1}">早餐</c:when>
                <c:when test="${order.mealType == 2}">午餐</c:when>
                <c:when test="${order.mealType == 3}">晚餐</c:when>
                <c:otherwise>未知</c:otherwise>
              </c:choose>
            </td>
            <td>¥<fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2"/></td>
            <td>
              <c:choose>
                <c:when test="${order.orderStatus == 1}">待备餐</c:when>
                <c:when test="${order.orderStatus == 2}">已完成</c:when>
                <c:when test="${order.orderStatus == 3}">已取消</c:when>
                <c:otherwise>未知</c:otherwise>
              </c:choose>
            </td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>

  <!-- 充值记录 -->
  <div class="common-card" id="rechargeRecords">
    <h2 class="common-h2">充值记录</h2>
    <c:if test="${empty rechargeList}">
      <div class="common-empty">暂无充值记录</div>
    </c:if>
    <c:if test="${not empty rechargeList}">
      <table class="common-table">
        <tr>
          <th>充值时间</th>
          <th>充值单号</th>
          <th>充值金额</th>
          <th>支付状态</th>
        </tr>
        <c:forEach var="recharge" items="${rechargeList}">
          <tr>
            <td><fmt:formatDate value="${recharge.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td>${recharge.rechargeNo}</td>
            <td>¥<fmt:formatNumber value="${recharge.amount}" minFractionDigits="2"/></td>
            <td>${recharge.payStatus == 1 ? '支付成功' : '支付失败'}</td>
          </tr>
        </c:forEach>
      </table>
    </c:if>
  </div>

  <!-- 账户汇总 -->
  <div class="common-card" id="summary" style="background: #f8f9fa;">
    <h2 class="common-h2">账户汇总</h2>
    <p>累计消费：¥<fmt:formatNumber value="${totalConsume}" minFractionDigits="2"/></p>
    <p>累计充值：¥<fmt:formatNumber value="${totalRecharge}" minFractionDigits="2"/></p>
    <p>当前余额：¥<fmt:formatNumber value="${balance}" minFractionDigits="2"/></p>
  </div>
</div>
</body>
</html>