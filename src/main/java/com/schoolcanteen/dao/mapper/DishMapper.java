package com.schoolcanteen.dao.mapper;

import com.schoolcanteen.entity.Dish;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface DishMapper {
    int insert(Dish dish);
    int update(Dish dish);
    int delete(Long dishid);
    int updateTotalStock(@Param("dishid") Long dishid, @Param("newTotalStock") Integer newTotalStock);

    int updateSoldCount(@Param("dishid") Long dishid, @Param("newSoldCount") Integer newSoldCount);
    int updateStatus(@Param("dishid") Long dishid, @Param("status") Integer status);
    List<Dish> selectByMealType(@Param("mealType") Integer mealType);
    Dish selectByDishid(Long dishid);
    List<Dish> getDailyMenuAllStatus(@Param("dishDate")Date dishDate, @Param("mealType") Integer mealType);

    List<Dish> selectByDateAndMealType(@Param("dishDate") Date dishDate, @Param("mealType") Integer mealType);

    List<Dish> selectByThreshold(@Param("dishDate") Date dishDate, @Param("threshold") Integer threshold);


}