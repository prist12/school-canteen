package com.schoolcanteen.service;

import com.schoolcanteen.dao.mapper.DishMapper;
import com.schoolcanteen.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class DishService1 {
    @Autowired
    private DishMapper dishMapper;

    // 菜品管理
    public void updateDishStock(Long dishid, Integer newTotalStock) {
        dishMapper.updateTotalStock(dishid, newTotalStock);
    }
    public void addDish(Dish dish) {
        dishMapper.insert(dish);
    }

    public void updateDish(Dish dish) {
        dishMapper.update(dish);
    }
    public void deleteDish(Long dishid) {
        dishMapper.delete(dishid);
    }
    // 上下架菜品（修改状态）
    public void toggleDishStatus(Long dishid, Integer status) {
        dishMapper.updateStatus(dishid, status);
    }
    public List<Dish> getDishByMealType(Integer mealType) {
        return dishMapper.selectByMealType(mealType);
    }
    // 库存管理
    public boolean checkStock(Long dishid, Integer requiredQuantity) {
        Dish dish = dishMapper.selectByDishid(dishid);
        if (dish == null) {
            return false;
        }
        // 兜底计算剩余库存：优先用数据库的remaining_stock，无则计算total_stock - sold_count
        Integer realStock = dish.getRemainingStock() == null ? (dish.getTotalStock() - dish.getSoldCount()) : dish.getRemainingStock();
        return realStock >= requiredQuantity;
    }

    @Transactional
    public void reduceStock(Long dishid, Integer quantity) {
        Dish dish = dishMapper.selectByDishid(dishid);
        if (dish == null) {
            throw new RuntimeException("菜品不存在");
        }
        Integer realStock = dish.getTotalStock() - dish.getSoldCount();
        if (quantity > realStock) {
            throw new RuntimeException("库存不足，剩余：" + realStock);
        }
        int newSoldCount = dish.getSoldCount() + quantity;
        dishMapper.updateSoldCount(dishid, newSoldCount);
    }

    // 查询
    public List<Dish> getDailyMenu(Date date, Integer mealType) {
        return dishMapper.selectByDateAndMealType(date, mealType);
    }
    // 补货：更新总库存和剩余库存
    @Transactional
    public void updateDishStock(Long dishid, int newTotalStock, int newRemainingStock) {
        dishMapper.updateTotalStock(dishid, newTotalStock);
    }
    // 查询库存预警菜品
    public List<Dish> getLowStockDishes(Date  date,Integer threshold) {
        return dishMapper.selectByThreshold(date, threshold);
    }
    // 根据ID查询菜品
    public Dish getDishById(Long dishid) {
        return dishMapper.selectByDishid(dishid);
    }
}
