package com.schoolcanteen.controller;

import com.schoolcanteen.entity.*;
import com.schoolcanteen.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Date;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService1 studentService1;
    @Autowired
    private OrderService1 orderService1;
    @Autowired
    private DishService1 dishService1;
    @Autowired
    private RechargeService1 rechargeService1;
    @Autowired
    private AccountFlowService1 accountFlowService1;
    /**
     * 学生首页：统一传递参数，匹配JSP渲染
     */
    @GetMapping
    public String studentIndex(
            @RequestParam(value = "mealType", defaultValue = "2") Integer mealType,
            @RequestParam(value = "date", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date queryDate, // 添加日期格式注解
            HttpSession session,
            Model model) {
        // 1. 登录态校验
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getUserType() != 4) {
            model.addAttribute("errorMsg", "请以学生身份登录");
            return "login";
        }

        // 2. 学生基础信息
        Student student = studentService1.getStudentByUserId(loginUser.getUserid());
        if (student == null) {
            model.addAttribute("errorMsg", "未查询到该账号关联的学生信息");
            return "login";
        }
        Long studentId = student.getStudentid();
        model.addAttribute("studentId", studentId);
        model.addAttribute("balance", studentService1.getBalance(studentId));

        // 3. 今日菜单
        Date today = new Date(System.currentTimeMillis());
        List<Dish> dishList = dishService1.getDailyMenu(today, mealType);
        model.addAttribute("dishList", dishList == null ? new ArrayList<>() : dishList);
        model.addAttribute("currentMealType", mealType);

        // 4. 消费记录（使用传入的查询日期）
        Date orderStartDate = queryDate == null ? today : queryDate;
        Date orderEndDate = queryDate == null ? today : queryDate;

        List<Order> orderList = orderService1.getStudentOrders(studentId, orderStartDate, orderEndDate);
        model.addAttribute("orderList", orderList == null ? new ArrayList<>() : orderList);
        model.addAttribute("queryDate", orderStartDate);

        // 5. 充值记录
        List<Recharge> rechargeList = rechargeService1.getStudentRecharges(studentId, orderStartDate, orderEndDate);
        model.addAttribute("rechargeList", rechargeList == null ? new ArrayList<>() : rechargeList);

        // 6. 账户流水汇总
        BigDecimal totalConsume = orderService1.getStudentConsumption(studentId, null, null);
        BigDecimal totalRecharge = rechargeService1.getTotalRecharge(studentId, null, null);
        model.addAttribute("totalConsume", totalConsume);
        model.addAttribute("totalRecharge", totalRecharge);

        return "student";
    }


    /**
     * 点餐接口：统一提示信息key为msg/errorMsg
     */
    @PostMapping("/order")
    @Transactional(rollbackFor = Exception.class)
    public String placeOrder(
            @RequestParam("mealType") Integer mealType,
            @RequestParam(value = "dishIds", required = false) List<Long> dishIds,
            @RequestParam Map<String, String> quantityMap,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // 1. 基础校验
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 4) {
                throw new RuntimeException("请以学生身份登录");
            }
            Long loginUserId = loginUser.getUserid(); // 获取登录用户的user_id（关键）
            if (dishIds == null || dishIds.isEmpty()) {
                throw new RuntimeException("请至少选择一道菜品");
            }

            Student student = studentService1.getStudentByUserId(loginUserId); // 学生关联的是登录用户的user_id
            if (student == null) {
                throw new RuntimeException("学生信息不存在");
            }
            Long studentId = student.getStudentid();

            // 兼容所有Date类型的构造方式
            Date today = new Date(System.currentTimeMillis());

            // 2. 库存+数量+日期校验
            List<Map<String, Object>> dishItems = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (Long dishId : dishIds) {
                Dish dish = dishService1.getDishById(dishId);
                // 校验菜品有效性
                if (dish == null) {
                    throw new RuntimeException("菜品ID：" + dishId + " 不存在");
                }
                if (dish.getStatus() != 1) {
                    throw new RuntimeException("菜品【" + dish.getDishName() + "】已下架，无法点餐");
                }

                // 验证是否为今日菜品
                List<Dish> dailyDish = dishService1.getDailyMenu(today, mealType);
                boolean isTodayDish = dailyDish.stream().anyMatch(d -> d.getDishid().equals(dishId));
                if (!isTodayDish) {
                    throw new RuntimeException("菜品【" + dish.getDishName() + "】非今日菜品，无法点餐");
                }

                // 数量校验
                String quantityStr = quantityMap.get("quantity_" + dishId);
                if (quantityStr == null || quantityStr.isEmpty()) {
                    throw new RuntimeException("菜品【" + dish.getDishName() + "】数量未填写");
                }
                Integer quantity;
                try {
                    quantity = Integer.parseInt(quantityStr);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("菜品【" + dish.getDishName() + "】数量格式错误（请输入数字）");
                }
                if (quantity <= 0) {
                    throw new RuntimeException("菜品【" + dish.getDishName() + "】数量必须大于0");
                }

                // 库存校验（直接计算生成列逻辑）
                Integer realStock = dish.getTotalStock() - dish.getSoldCount();
                if (quantity > realStock) {
                    throw new RuntimeException("菜品【" + dish.getDishName() + "】库存不足（剩余：" + realStock + "）");
                }

                // 累计金额
                totalAmount = totalAmount.add(dish.getPrice().multiply(new BigDecimal(quantity)));
                Map<String, Object> dishItem = new HashMap<>();
                dishItem.put("dishid", dishId);
                dishItem.put("dishName", dish.getDishName());
                dishItem.put("quantity", quantity);
                dishItem.put("price", dish.getPrice());
                dishItems.add(dishItem);
            }

            // 3. 余额校验
            BigDecimal currentBalance = studentService1.getBalance(studentId);
            if (currentBalance == null || currentBalance.compareTo(totalAmount) < 0) {
                throw new RuntimeException("余额不足！当前余额：¥" + (currentBalance == null ? 0 : currentBalance) + "，需支付：¥" + totalAmount);
            }

            // 4. 下单（核心修复：传入loginUserId而非studentId）
            Order order = orderService1.placeOrder(studentId, today, mealType, dishItems, loginUserId);
            redirectAttributes.addFlashAttribute("msg", "点餐成功！订单号：" + order.getOrderNo());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMsg", "点餐失败：" + e.getMessage());
        }
        return "redirect:/student";
    }
    /**
     * 充值接口：关联RechargeService生成充值记录
     */
    @PostMapping("/api/student/recharge")
    @Transactional(rollbackFor = Exception.class)
    public String recharge(
            @RequestParam("amount") BigDecimal amount,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            // 1. 基础校验
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("充值金额必须大于0");
            }

            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 4) {
                throw new RuntimeException("请以学生身份登录");
            }

            // 2. 学生信息校验
            Student student = studentService1.getStudentByUserId(loginUser.getUserid());
            if (student == null) {
                throw new RuntimeException("学生信息不存在");
            }
            Long studentId = student.getStudentid();

            // 3. 执行充值（RechargeService封装充值记录、余额、流水）
            Recharge recharge = rechargeService1.recharge(studentId, amount, 1, loginUser.getUserid());
            BigDecimal newBalance = studentService1.getBalance(studentId);
            redirectAttributes.addFlashAttribute("msg", "充值成功！本次充值¥" + amount + "，当前余额：¥" + newBalance + "，充值单号：" + recharge.getRechargeNo());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "充值失败：" + e.getMessage());
        }
        return "redirect:/student";
    }



}