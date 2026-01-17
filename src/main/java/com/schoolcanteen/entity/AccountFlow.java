package com.schoolcanteen.entity;

import java.math.BigDecimal;
import java.util.Date;

public class AccountFlow {
    private Long accountflowid;
    private Long studentId;
    private Integer flowType;
    private BigDecimal amount;
    private BigDecimal currentBalance;
    private String relateNo;
    private Long operatorId;
    private Date createTime;

    public AccountFlow(Long accountflowid, Long studentId, Integer flowType, BigDecimal amount, BigDecimal currentBalance, String relateNo, Long operatorId, Date createTime) {
        this.accountflowid = accountflowid;
        this.studentId = studentId;
        this.flowType = flowType;
        this.amount = amount;
        this.currentBalance = currentBalance;
        this.relateNo = relateNo;
        this.operatorId = operatorId;
        this.createTime = createTime;
    }
// 构造函数

    public AccountFlow() {
    }

    public Long getAccountflowid() {
        return accountflowid;
    }

    public void setAccountflowid(Long accountflowid) {
        this.accountflowid = accountflowid;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Integer getFlowType() {
        return flowType;
    }

    public void setFlowType(Integer flowType) {
        this.flowType = flowType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getRelateNo() {
        return relateNo;
    }

    public void setRelateNo(String relateNo) {
        this.relateNo = relateNo;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}