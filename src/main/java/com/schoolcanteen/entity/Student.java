package com.schoolcanteen.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Student {
    private Long studentid;
    private Long userId;
    private String studentNo;
    private String className;
    private Integer gender;
    private BigDecimal balance;
    private Long parentId;
    private Integer parentBindStatus;
    private Long teacherId;
    private Date createTime;

    // 构造函数
    public Student() {}

    public Student(Long studentid, Long userId, String studentNo, String className, Integer gender, BigDecimal balance, Long parentId, Integer parentBindStatus, Long teacherId, Date createTime) {
        this.studentid = studentid;
        this.userId = userId;
        this.studentNo = studentNo;
        this.className = className;
        this.gender = gender;
        this.balance = balance;
        this.parentId = parentId;
        this.parentBindStatus = parentBindStatus;
        this.teacherId = teacherId;
        this.createTime = createTime;
    }

    public Long getStudentid() {
        return studentid;
    }

    public void setStudentid(Long studentid) {
        this.studentid = studentid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getParentBindStatus() {
        return parentBindStatus;
    }

    public void setParentBindStatus(Integer parentBindStatus) {
        this.parentBindStatus = parentBindStatus;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
