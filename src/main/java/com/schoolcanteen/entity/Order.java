package com.schoolcanteen.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.databind.JsonNode;

public class Order {
    private Long orderid;
    private String orderNo;
    private Long studentId;
    private Date consumeDate;
    private Integer mealType;
    private JsonNode dishInfo; // JSON字段
    private Integer dishCount;
    private BigDecimal totalAmount;
    private Integer payStatus;
    private Integer orderStatus;
    private Date createTime;
    private Date payTime;
    private Date completeTime;

    // 构造函数
    public Order() {}

    public Order(Long orderid, String orderNo, Long studentId, Date consumeDate, Integer mealType, JsonNode dishInfo, Integer dishCount, BigDecimal totalAmount, Integer payStatus, Integer orderStatus, Date createTime, Date payTime, Date completeTime) {
        this.orderid = orderid;
        this.orderNo = orderNo;
        this.studentId = studentId;
        this.consumeDate = consumeDate;
        this.mealType = mealType;
        this.dishInfo = dishInfo;
        this.dishCount = dishCount;
        this.totalAmount = totalAmount;
        this.payStatus = payStatus;
        this.orderStatus = orderStatus;
        this.createTime = createTime;
        this.payTime = payTime;
        this.completeTime = completeTime;
    }

    public Long getOrderid() {
        return orderid;
    }

    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Date getConsumeDate() {
        return consumeDate;
    }

    public void setConsumeDate(Date consumeDate) {
        this.consumeDate = consumeDate;
    }

    public Integer getMealType() {
        return mealType;
    }

    public void setMealType(Integer mealType) {
        this.mealType = mealType;
    }

    public JsonNode getDishInfo() {
        return dishInfo;
    }

    public void setDishInfo(JsonNode dishInfo) {
        this.dishInfo = dishInfo;
    }

    public Integer getDishCount() {
        return dishCount;
    }

    public void setDishCount(Integer dishCount) {
        this.dishCount = dishCount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }
}
