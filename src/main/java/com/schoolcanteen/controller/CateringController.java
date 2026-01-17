
package com.schoolcanteen.controller;

        import com.schoolcanteen.entity.Dish;
        import com.schoolcanteen.entity.User;
        import com.schoolcanteen.service.DishService1;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.annotation.*;
        import org.springframework.web.servlet.mvc.support.RedirectAttributes;

        import javax.servlet.http.HttpSession;
        import java.math.BigDecimal;
        import java.sql.Date;
        import java.util.List;

/**
 * 餐饮员控制器：菜品增删改、库存管理、当日菜单管理
 */
@Controller
@RequestMapping("/catering")
public class CateringController {

    @Autowired
    private DishService1 dishService1;

    /**
     * 餐饮员首页：展示当日菜单、库存预警
     */
    @GetMapping
    public String cateringIndex(Model model) {
        // 1. 查询当日所有菜品（按餐段分类）
        Date today = new Date(System.currentTimeMillis());
        List<Dish> breakfastList = dishService1.getDailyMenuAllStatus(today, 1); // 早餐
        List<Dish> lunchList = dishService1.getDailyMenuAllStatus(today, 2);   // 午餐
        List<Dish> dinnerList = dishService1.getDailyMenuAllStatus(today, 3);  // 晚餐
        model.addAttribute("breakfastList", breakfastList);
        model.addAttribute("lunchList", lunchList);
        model.addAttribute("dinnerList", dinnerList);

        // 2. 查询库存预警菜品（默认阈值10）
        List<Dish> lowStockList = dishService1.getLowStockDishes(today,10);
        model.addAttribute("lowStockList", lowStockList);

        return "catering"; // 对应餐饮员JSP页面
    }

    /**
     * 添加菜品
     */
    @PostMapping("/dish/add")
    public String addDish(
            @RequestParam("dishName") String dishName,
            @RequestParam("price") BigDecimal price,
            @RequestParam("mealType") Integer mealType,
            @RequestParam("dishDate") Date dishDate,
            @RequestParam("totalStock") Integer totalStock,
            @RequestParam("threshold") Integer threshold,
            HttpSession session, // 新增：获取Session，读取登录用户信息
            RedirectAttributes redirectAttributes) {
        try {
            // 1. 获取当前登录的餐饮员用户ID（需确保登录时将用户信息存入Session）
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null) {
                throw new RuntimeException("请先登录！");
            }
            Long createUserId = loginUser.getUserid(); // 餐饮员的用户ID

            // 2. 封装菜品信息
            Dish dish = new Dish();
            dish.setDishName(dishName);
            dish.setPrice(price);
            dish.setMealType(mealType);
            dish.setDishDate(dishDate);
            dish.setTotalStock(totalStock);
            dish.setSoldCount(0); // 初始销量0
            dish.setThreshold(threshold);
            dish.setStatus(1); // 1-上架状态
            dish.setCreateUserId(createUserId); // 关键：赋值create_user_id
            dish.setCreateTime(new java.util.Date()); // 创建时间

            // 3. 添加菜品
            dishService1.addDish(dish);
            redirectAttributes.addFlashAttribute("msg", "菜品【" + dishName + "】添加成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "添加菜品失败：" + e.getMessage());
        }
        return "redirect:/catering";
    }

    /**
     * 修改菜品（回显+提交）
     */
    @GetMapping("/dish/edit/{dishid}")
    public String editDish(
            @PathVariable("dishid") Long dishid,
            Model model,
            RedirectAttributes redirectAttributes) {
        // 校验菜品ID有效性
        if (dishid == null || dishid <= 0) {
            redirectAttributes.addFlashAttribute("errorMsg", "菜品ID无效！");
            return "redirect:/catering";
        }
        Dish dish = dishService1.getDishById(dishid);
        if (dish == null) {
            redirectAttributes.addFlashAttribute("errorMsg", "菜品不存在！");
            return "redirect:/catering";
        }
        model.addAttribute("editDish", dish);
        // 携带当日菜单数据
        cateringIndex(model);
        return "catering";
    }

    // 提交修改
    @PostMapping("/dish/update")
    public String updateDish(
            @RequestParam("dishid") Long dishid,
            @RequestParam("dishName") String dishName,
            @RequestParam("price") BigDecimal price,
            @RequestParam("mealType") Integer mealType,
            @RequestParam("threshold") Integer threshold,
            @RequestParam("status") Integer status,
            RedirectAttributes redirectAttributes) {
        try {
            Dish dish = dishService1.getDishById(dishid);
            if (dish == null) {
                throw new RuntimeException("菜品不存在");
            }
            // 更新可修改字段（不修改库存/销量，库存单独管理）
            dish.setDishName(dishName);
            dish.setPrice(price);
            dish.setMealType(mealType);
            dish.setThreshold(threshold);
            dish.setStatus(status); // 1-上架，0-下架

            dishService1.updateDish(dish);
            redirectAttributes.addFlashAttribute("msg", "菜品【" + dishName + "】修改成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "修改菜品失败：" + e.getMessage());
        }
        return "redirect:/catering";
    }

    /**
     * 删除菜品
     */
    @GetMapping("/dish/delete/{dishid}")
    public String deleteDish(
            @PathVariable("dishid") Long dishid,
            RedirectAttributes redirectAttributes) {
        try {
            if (dishid == null || dishid <= 0) {
                throw new RuntimeException("菜品ID无效！");
            }
            Dish dish = dishService1.getDishById(dishid);
            if (dish == null) {
                throw new RuntimeException("菜品不存在");
            }
            // 物理删除：调用service删除数据库记录
            dishService1.deleteDish(dishid);
            redirectAttributes.addFlashAttribute("msg", "菜品【" + dish.getDishName() + "】已永久删除！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "删除菜品失败：" + e.getMessage());
        }
        return "redirect:/catering";
    }

    /**
     * 菜品补货
     */
    @PostMapping("/dish/addStock")
    public String addStock(
            @RequestParam("dishid") Long dishid,
            @RequestParam("quantity") Integer quantity,
            RedirectAttributes redirectAttributes) {
        try {
            // 校验补货数量
            if (quantity <= 0) {
                throw new RuntimeException("补货数量必须大于0");
            }
            Dish dish = dishService1.getDishById(dishid);
            if (dish == null) {
                throw new RuntimeException("菜品不存在");
            }
            // 计算新库存：总库存+补货量，剩余库存同步增加
            int newTotalStock = dish.getTotalStock() + quantity;
            int newRemainingStock = dish.getRemainingStock() + quantity;
            // 更新库存（同步更新总库存和剩余库存）
            dishService1.updateDishStock(dishid, newTotalStock, newRemainingStock);
            redirectAttributes.addFlashAttribute("msg", "菜品【" + dish.getDishName() + "】补货成功！新增库存：" + quantity);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "补货失败：" + e.getMessage());
        }
        return "redirect:/catering";
    }

    /**
     * 上下架菜品
     */
    @GetMapping("/dish/toggleStatus/{dishid}/{status}")
    public String toggleDishStatus(
            @PathVariable("dishid") Long dishid,
            @PathVariable("status") Integer status,
            RedirectAttributes redirectAttributes) {
        try {
            // 校验状态合法性（仅允许0/1）
            if (status != 0 && status != 1) {
                throw new RuntimeException("状态值无效（仅支持0-下架/1-上架）");
            }
            Dish dish = dishService1.getDishById(dishid);
            if (dish == null) {
                throw new RuntimeException("菜品不存在");
            }
            String statusDesc = status == 1 ? "上架" : "下架";
            dishService1.toggleDishStatus(dishid, status);
            redirectAttributes.addFlashAttribute("msg", "菜品【" + dish.getDishName() + "】已" + statusDesc + "！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "修改菜品状态失败：" + e.getMessage());
        }
        return "redirect:/catering";
    }

    /**
     * 扩展：查询当日指定餐段菜单（供前端筛选）
     */
    @GetMapping("/dish/daily")
    public String getDailyMenu(
            @RequestParam("date") Date date,
            @RequestParam("mealType") Integer mealType,
            Model model) {
        List<Dish> dailyMenu = dishService1.getDailyMenu(date, mealType);
        model.addAttribute("dailyMenu", dailyMenu);
        // 携带其他数据
        cateringIndex(model);
        return "catering";
    }
}