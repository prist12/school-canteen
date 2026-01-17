package com.schoolcanteen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.schoolcanteen.dao.mapper.AccountFlowMapper;
import com.schoolcanteen.dao.mapper.OrderMapper;
import com.schoolcanteen.entity.AccountFlow;
import com.schoolcanteen.entity.Dish;
import com.schoolcanteen.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService1 {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DishService1 dishService;
    @Autowired
    private StudentService1 studentService;
    @Autowired
    private AccountFlowMapper accountFlowMapper;

    public Order placeOrder(Long studentid, Date consumeDate, Integer mealType, List<Map<String, Object>> dishItems, Long loginUserId) {
        // 计算总金额和验证库存
        BigDecimal totalAmount = BigDecimal.ZERO;
        Integer totalCount = 0;

        for (Map<String, Object> item : dishItems) {
            Long dishid = (Long) item.get("dishid");
            Integer quantity = (Integer) item.get("quantity");
            Dish dish = dishService.getDishById(dishid);

            if (!dishService.checkStock(dishid, quantity)) {
                throw new RuntimeException("菜品" + dish.getDishName() + "库存不足");
            }

            totalAmount = totalAmount.add(dish.getPrice().multiply(BigDecimal.valueOf(quantity)));
            totalCount += quantity;
        }

        // 验证余额
        BigDecimal balance = studentService.getBalance(studentid);
        if (balance.compareTo(totalAmount) < 0) {
            throw new RuntimeException("余额不足");
        }

        // 生成订单号
        String orderNo = "ORD" + System.currentTimeMillis();

        // 创建订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setStudentId(studentid);
        order.setConsumeDate(consumeDate);
        order.setMealType(mealType);
        order.setDishCount(totalCount);
        order.setTotalAmount(totalAmount);
        order.setPayStatus(1); // 1-已支付
        order.setOrderStatus(1); // 1-待备餐
        order.setCreateTime(new Date());
        order.setPayTime(new Date());

        // 将菜品信息转为JSON
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode dishInfo = mapper.valueToTree(dishItems);
            order.setDishInfo(dishInfo);
        } catch (Exception e) {
            throw new RuntimeException("菜品信息转换失败");
        }

        orderMapper.insert(order);

        // 扣减库存
        for (Map<String, Object> item : dishItems) {
            Long dishid = (Long) item.get("dishid");
            Integer quantity = (Integer) item.get("quantity");
            dishService.reduceStock(dishid, quantity);
        }

        // 扣减余额并记录流水
        studentService.updateBalance(studentid, totalAmount, 1, orderNo, loginUserId);

        // 记录账户流水
        BigDecimal currentBalance = balance.subtract(totalAmount);
        AccountFlow flow = new AccountFlow();
        flow.setStudentId(studentid);
        flow.setFlowType(1); // 1-消费
        flow.setAmount(totalAmount.negate()); // 消费金额为负数
        flow.setCurrentBalance(currentBalance);
        flow.setRelateNo(orderNo);
        flow.setOperatorId(loginUserId);
        flow.setCreateTime(new Date());
        accountFlowMapper.insert(flow);

        return order;
    }

    // 其他方法保持不变...
    public List<Order> getStudentOrders(Long studentId, Date startDate, Date endDate) {
        return orderMapper.selectByStudentId(studentId, startDate, endDate);
    }

    public BigDecimal getStudentConsumption(Long studentId, Date startDate, Date endDate) {
        BigDecimal sum = orderMapper.sumTotalAmountByStudent(studentId, startDate, endDate);
        return sum != null ? sum : BigDecimal.ZERO;
    }
}