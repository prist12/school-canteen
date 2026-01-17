package com.schoolcanteen.entity;


/**
 * 用户表实体类
 */
public class User  {

    private Long userid;                // 用户唯一标识
    private String phone;           // 登录账号
    private String password;        // 密码
    private String userName;        // 真实姓名
    private Integer userType;       // 用户类型：1-系统管理员 2-教师 3-家长 4-学生 5-餐饮员
    private Integer status;         // 账号状态：0-禁用 1-正常
    private Integer gender;         // 性别：1-男 2-女 0-未知
    private Long relateId;          // 关联ID（教师-班级ID/家长-学生ID）
    private String permissions;     // 权限JSON数组

    // 无参构造函数
    public User() {}

    // Getter和Setter方法

    public User(Long userid, String phone, String password, String userName, Integer userType, Integer status, Integer gender, Long relateId, String permissions) {
        this.userid = userid;
        this.phone = phone;
        this.password = password;
        this.userName = userName;
        this.userType = userType;
        this.status = status;
        this.gender = gender;
        this.relateId = relateId;
        this.permissions = permissions;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Long getRelateId() {
        return relateId;
    }

    public void setRelateId(Long relateId) {
        this.relateId = relateId;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}