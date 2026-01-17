package com.schoolcanteen.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Dish {
    private Long dishid;
    private String dishName;
    private Integer mealType;
    private Date dishDate;
    private BigDecimal price;
    private Integer totalStock;
    private Integer soldCount;
    private Integer remainingStock;
    private Integer threshold;
    private Integer status;
    private Long createUserId;
    private Date createTime;

    // 构造函数
    public Dish() {}

    public Dish(Long dishid, String dishName, Integer mealType, Date dishDate, BigDecimal price, Integer totalStock, Integer soldCount, Integer remainingStock, Integer threshold, Integer status, Long createUserId, Date createTime) {
        this.dishid = dishid;
        this.dishName = dishName;
        this.mealType = mealType;
        this.dishDate = dishDate;
        this.price = price;
        this.totalStock = totalStock;
        this.soldCount = soldCount;
        this.remainingStock = remainingStock;
        this.threshold = threshold;
        this.status = status;
        this.createUserId = createUserId;
        this.createTime = createTime;
    }

    public Long getDishid() {
        return dishid;
    }

    public void setDishid(Long dishid) {
        this.dishid = dishid;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getMealType() {
        return mealType;
    }

    public void setMealType(Integer mealType) {
        this.mealType = mealType;
    }

    public Date getDishDate() {
        return dishDate;
    }

    public void setDishDate(Date dishDate) {
        this.dishDate = dishDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(Integer totalStock) {
        this.totalStock = totalStock;
    }

    public Integer getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Integer soldCount) {
        this.soldCount = soldCount;
    }

    public Integer getRemainingStock() {
        return remainingStock;
    }

    public void setRemainingStock(Integer remainingStock) {
        this.remainingStock = remainingStock;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public void setThreshold(Integer threshold) {
        this.threshold = threshold;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
