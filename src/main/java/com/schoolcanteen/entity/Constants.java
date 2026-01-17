
package com.schoolcanteen.entity;

/**
 * 常量类
 */
public class Constants {

    // 用户类型
    public static final int USER_TYPE_ADMIN = 1;     // 系统管理员
    public static final int USER_TYPE_TEACHER = 2;   // 教师
    public static final int USER_TYPE_PARENT = 3;    // 家长
    public static final int USER_TYPE_STUDENT = 4;   // 学生
    public static final int USER_TYPE_CATERING = 5;  // 餐饮员

    // 用户状态
    public static final int USER_STATUS_DISABLED = 0; // 禁用
    public static final int USER_STATUS_NORMAL = 1;   // 正常

    // 性别
    public static final int GENDER_MALE = 1;         // 男
    public static final int GENDER_FEMALE = 2;       // 女
    public static final int GENDER_UNKNOWN = 0;      // 未知

    // 餐段类型
    public static final int MEAL_TYPE_BREAKFAST = 1; // 早餐
    public static final int MEAL_TYPE_LUNCH = 2;     // 午餐
    public static final int MEAL_TYPE_DINNER = 3;    // 晚餐

    // 菜品状态
    public static final int DISH_STATUS_OFFLINE = 0; // 下架
    public static final int DISH_STATUS_ONLINE = 1;  // 在售

    // 支付状态
    public static final int PAY_STATUS_PENDING = 1;  // 待支付
    public static final int PAY_STATUS_SUCCESS = 2;  // 支付成功
    public static final int PAY_STATUS_FAILED = 3;   // 支付失败

    // 订单状态
    public static final int ORDER_STATUS_CREATED = 1;   // 已下单
    public static final int ORDER_STATUS_PROCESSING = 2; // 制作中
    public static final int ORDER_STATUS_COMPLETED = 3;  // 已完成
    public static final int ORDER_STATUS_CANCELLED = 4;  // 已取消

    // 流水类型
    public static final int FLOW_TYPE_CONSUME = 1;      // 消费
    public static final int FLOW_TYPE_STUDENT_RECHARGE = 2; // 学生充值
    public static final int FLOW_TYPE_PARENT_RECHARGE = 3;  // 家长代充
    public static final int FLOW_TYPE_REFUND = 4;       // 退款

    // 充值类型
    public static final int RECHARGE_TYPE_STUDENT = 2;  // 学生充值
    public static final int RECHARGE_TYPE_PARENT = 3;   // 家长代充

    // 支付方式
    public static final int PAY_METHOD_WECHAT = 1;      // 微信
    public static final int PAY_METHOD_ALIPAY = 2;      // 支付宝
    public static final int PAY_METHOD_CASH = 3;        // 现金
}