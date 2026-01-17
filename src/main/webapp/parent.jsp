<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
  <title>家长端 - 学生管理</title>
  <!-- 引入统一公共样式：static/css/style.css -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/style.css">
</head>
<body>
<!-- 家长统一导航栏（实现功能跳转） -->
<div class="common-nav">
  <a href="${pageContext.request.contextPath}/parent" class="active">学生列表</a>
  <a href="${pageContext.request.contextPath}/logout">退出登录</a>
</div>

<!-- 统一容器 -->
<div class="common-container">
  <h1 class="common-h1">家长管理系统</h1>

  <!-- 消息提示（使用统一样式） -->
  <c:if test="${not empty msg}">
    <div class="common-msg ${code == 200 ? 'common-msg-success' : 'common-msg-error'}">${msg}</div>
  </c:if>

  <!-- 1. 学生列表页面 -->
  <c:if test="${page == 'studentList' or empty page}">
    <div class="common-card">
      <h2 class="common-h2">我的孩子</h2>
      <c:if test="${not empty studentList}">
        <c:forEach var="student" items="${studentList}">
          <div class="common-card" style="margin-bottom: 15px; padding: 20px;">
            <h3 class="common-h3">学生姓名：${studentNameMap[student.studentid]}</h3>
            <h3 class="common-h3">学号：${student.studentNo}</h3>
            <p>班级：${student.className}</p>
            <c:if test="${student.gender != null}">
              <p>性别：
                <c:choose>
                  <c:when test="${student.gender == 1}">男</c:when>
                  <c:when test="${student.gender == 2}">女</c:when>
                  <c:otherwise>未设置</c:otherwise>
                </c:choose>
              </p>
            </c:if>
            <p class="common-h3" style="color: #e74c3c; margin: 10px 0;">余额：¥<fmt:formatNumber value="${student.balance}" pattern="0.00"/></p>
            <div class="operate-btn">
              <a href="${pageContext.request.contextPath}/parent/toRecharge?studentId=${student.studentid}"
                 class="common-btn common-btn-success">充值</a>
              <a href="${pageContext.request.contextPath}/parent/toFlowQuery?studentId=${student.studentid}"
                 class="common-btn common-btn-primary">查看流水</a>
            </div>
          </div>
        </c:forEach>
      </c:if>
      <c:if test="${empty studentList}">
        <div class="common-empty">暂无绑定学生</div>
      </c:if>
    </div>
  </c:if>

  <!-- 2. 充值页面 -->
  <c:if test="${page == 'recharge'}">
    <div class="common-card">
      <h2 class="common-h2">学生充值</h2>
      <div style="margin-bottom: 20px;">
        <h3 class="common-h3">学生姓名：${studentName}</h3>
        <h3 class="common-h3">学号：${student.studentNo}</h3>
        <p>班级：${student.className}</p>
        <c:if test="${student.gender != null}">
          <p>性别：
            <c:choose>
              <c:when test="${student.gender == 1}">男</c:when>
              <c:when test="${student.gender == 2}">女</c:when>
              <c:otherwise>未设置</c:otherwise>
            </c:choose>
          </p>
        </c:if>
        <p class="common-h3" style="color: #e74c3c;">当前余额：¥<fmt:formatNumber value="${student.balance}" pattern="0.00"/></p>
      </div>

      <form action="${pageContext.request.contextPath}/parent/doRecharge" method="post">
        <input type="hidden" name="studentId" value="${student.studentid}">
        <input type="hidden" name="relateNo" value="RECHARGE_${System.currentTimeMillis()}">

        <div class="common-form-group">
          <label class="common-form-label">充值金额：</label>
          <input type="number" name="amount" min="1" step="0.01" value="100" required class="common-form-input">
        </div>

        <div class="common-form-group">
          <label class="common-form-label">备注：</label>
          <input type="text" name="remark" placeholder="请输入备注" class="common-form-input">
        </div>

        <div class="common-form-group" style="margin-left: 110px;">
          <button type="submit" class="common-btn common-btn-primary">确认充值</button>
          <a href="${pageContext.request.contextPath}/parent" class="common-btn common-btn-danger" style="margin-left: 10px;">返回</a>
        </div>
      </form>
    </div>
  </c:if>

  <!-- 3. 账单流水页面 -->
  <c:if test="${page == 'flowQuery'}">
    <div class="common-card">
      <h2 class="common-h2">账单流水查询</h2>
      <div style="margin-bottom: 20px;">
        <h3 class="common-h3">学生姓名：${studentName}</h3>
        <h3 class="common-h3">学号：${student.studentNo}</h3>
        <p>班级：${student.className}</p>
      </div>

      <form action="${pageContext.request.contextPath}/parent/queryFlows" method="post">
        <input type="hidden" name="studentId" value="${student.studentid}">

        <div class="common-form-group">
          <label class="common-form-label">流水类型：</label>
          <select name="flowType" class="common-form-select">
            <option value="">全部</option>
            <option value="1">消费</option>
            <option value="2">充值</option>
          </select>
        </div>

        <div class="common-form-group">
          <label class="common-form-label">开始日期：</label>
          <input type="date" name="startTime" class="common-form-input">
        </div>

        <div class="common-form-group">
          <label class="common-form-label">结束日期：</label>
          <input type="date" name="endTime" class="common-form-input">
        </div>

        <div class="common-form-group" style="margin-left: 110px;">
          <button type="submit" class="common-btn common-btn-primary">查询</button>
          <a href="${pageContext.request.contextPath}/parent" class="common-btn common-btn-danger" style="margin-left: 10px;">返回</a>
        </div>
      </form>

      <c:if test="${not empty flowList}">
        <h3 class="common-h3">流水记录（总计：¥<fmt:formatNumber value="${summary}" pattern="0.00"/>）</h3>
        <table class="common-table">
          <tr>
            <th>时间</th>
            <th>类型</th>
            <th>金额</th>
            <th>余额</th>
            <th>关联单号</th>
          </tr>
          <c:forEach var="flow" items="${flowList}">
            <tr>
              <td><fmt:formatDate value="${flow.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
              <td style="color: ${flow.flowType == 1 ? '#e74c3c' : '#27ae60'}">
                  ${flow.flowType == 1 ? '消费' : '充值'}
              </td>
              <td>
                <c:choose>
                  <c:when test="${flow.flowType == 1}">-</c:when>
                  <c:when test="${flow.flowType == 2}">+</c:when>
                </c:choose>
                ¥<fmt:formatNumber value="${flow.amount.abs()}" pattern="0.00"/>
              </td>
              <td>¥<fmt:formatNumber value="${flow.currentBalance}" pattern="0.00"/></td>
              <td>${flow.relateNo}</td>
            </tr>
          </c:forEach>
        </table>
      </c:if>
      <c:if test="${empty flowList}">
        <div class="common-empty">暂无符合条件的流水记录</div>
      </c:if>
    </div>
  </c:if>
</div>
</body>
</html>