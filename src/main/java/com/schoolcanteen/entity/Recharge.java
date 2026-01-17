package com.schoolcanteen.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Recharge {
    private Long rechargeid;
    private String rechargeNo;
    private Long studentId;
    private Integer rechargeType;
    private Long rechargeUserId;
    private BigDecimal amount;
    private Integer payMethod;
    private Integer payStatus;
    private Date payTime;

    // 构造函数
    public Recharge() {}

    public Recharge(Long rechargeid, String rechargeNo, Long studentId, Integer rechargeType, Long rechargeUserId, BigDecimal amount, Integer payMethod, Integer payStatus, Date payTime) {
        this.rechargeid = rechargeid;
        this.rechargeNo = rechargeNo;
        this.studentId = studentId;
        this.rechargeType = rechargeType;
        this.rechargeUserId = rechargeUserId;
        this.amount = amount;
        this.payMethod = payMethod;
        this.payStatus = payStatus;
        this.payTime = payTime;
    }

    public Long getRechargeid() {
        return rechargeid;
    }

    public void setRechargeid(Long rechargeid) {
        this.rechargeid = rechargeid;
    }

    public String getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(String rechargeNo) {
        this.rechargeNo = rechargeNo;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(Integer rechargeType) {
        this.rechargeType = rechargeType;
    }

    public Long getRechargeUserId() {
        return rechargeUserId;
    }

    public void setRechargeUserId(Long rechargeUserId) {
        this.rechargeUserId = rechargeUserId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(Integer payMethod) {
        this.payMethod = payMethod;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
