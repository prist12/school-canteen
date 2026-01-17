package com.schoolcanteen.controller;

import com.schoolcanteen.entity.User;
import com.schoolcanteen.service.UserService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员控制器 - 处理用户增删改查
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService1 userService1;

    /**
     * 1. 管理员首页 - 展示所有用户，支持按类型筛选
     */
    @GetMapping
    public String adminPage(Model model,
                            @RequestParam(required = false) Integer userType,
                            HttpSession session) {
        // 验证是否为管理员登录（防止越权访问）
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getUserType() != 1) {
            model.addAttribute("errorMsg", "请以管理员身份登录！");
            return "login";
        }

        List<User> userList;
        if (userType != null) {
            userList = userService1.selectByUserType(userType);
        } else {
            userList = userService1.selectAllUsers();
        }

        model.addAttribute("userList", userList);
        model.addAttribute("selectedType", userType); // 回显筛选条件
        return "admin";
    }

    /**
     * 2. 添加用户（完整字段）
     */
    @PostMapping("/addUser")
    public String addUser(
            @RequestParam String phone,
            @RequestParam String userName,
            @RequestParam Integer userType,
            @RequestParam Integer gender,
            @RequestParam(required = false) Long relateId,
            @RequestParam(required = false) String permissions,
            RedirectAttributes redirectAttributes) {
        try {
            // 参数校验
            if (phone == null || phone.isEmpty() || !phone.matches("^1[3-9]\\d{9}$")) {
                throw new RuntimeException("请输入正确的手机号！");
            }
            if (userName == null || userName.isEmpty()) {
                throw new RuntimeException("姓名不能为空！");
            }
            if (userType == null || (userType < 1 || userType > 5)) {
                throw new RuntimeException("请选择正确的用户类型！");
            }

            // 构建用户对象（完整字段）
            User user = new User();
            user.setPhone(phone);
            user.setPassword("123456"); // 默认初始密码
            user.setUserName(userName);
            user.setUserType(userType);
            user.setGender(gender);
            user.setRelateId(relateId);
            user.setPermissions(permissions);
            user.setStatus(1); // 默认启用

            userService1.register(user); // 使用register方法（已校验手机号唯一性）
            redirectAttributes.addFlashAttribute("msg", "用户添加成功！初始密码：123456");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "添加失败：" + e.getMessage());
        }
        return "redirect:/admin";
    }

    /**
     * 3. 删除用户（修复路径冲突）
     */
    @GetMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long userid,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {
        try {
            // 防止删除管理员自身
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser.getUserid().equals(userid)) {
                throw new RuntimeException("不能删除当前登录的管理员账号！");
            }

            userService1.deleteUser(userid);
            redirectAttributes.addFlashAttribute("msg", "用户删除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "删除失败：" + e.getMessage());
        }
        return "redirect:/admin";
    }

    /**
     * 4. 跳转到编辑用户页面（回显所有字段）
     */
    @GetMapping("/edit/{userid}")
    public String toEditPage(@PathVariable("userid") Long userid,
                             Model model,
                             HttpSession session) {
        // 管理员权限验证
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || loginUser.getUserType() != 1) {
            model.addAttribute("errorMsg", "请以管理员身份登录！");
            return "login";
        }

        User user = userService1.selectByUserid(userid);
        if (user == null) {
            model.addAttribute("errorMsg", "用户不存在！");
            return "redirect:/admin";
        }
        model.addAttribute("user", user);
        return "editUser"; // 编辑页面
    }

    /**
     * 5. 提交编辑用户信息（更新所有字段）
     */
    @PostMapping("/updateUser")
    public String updateUser(
            @RequestParam Long userid,
            @RequestParam String phone,
            @RequestParam String userName,
            @RequestParam Integer userType,
            @RequestParam Integer status,
            @RequestParam Integer gender,
            @RequestParam(required = false) Long relateId,
            @RequestParam(required = false) String permissions,
            RedirectAttributes redirectAttributes) {
        try {
            // 参数校验
            if (phone == null || phone.isEmpty() || !phone.matches("^1[3-9]\\d{9}$")) {
                throw new RuntimeException("请输入正确的手机号！");
            }
            if (userName == null || userName.isEmpty()) {
                throw new RuntimeException("姓名不能为空！");
            }

            // 构建更新对象
            User user = new User();
            user.setUserid(userid);
            user.setPhone(phone);
            user.setUserName(userName);
            user.setUserType(userType);
            user.setStatus(status);
            user.setGender(gender);
            user.setRelateId(relateId);
            user.setPermissions(permissions);

            userService1.updateUser(user);
            redirectAttributes.addFlashAttribute("msg", "用户信息更新成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "更新失败：" + e.getMessage());
        }
        return "redirect:/admin";
    }

    /**
     * 6. 禁用/启用用户
     */
    @GetMapping("/changeStatus")
    public String changeUserStatus(@RequestParam("id") Long userid,
                                   @RequestParam("status") Integer status,
                                   RedirectAttributes redirectAttributes) {
        try {
            userService1.updateStatus(userid, status);
            redirectAttributes.addFlashAttribute("msg", status == 1 ? "用户启用成功！" : "用户禁用成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMsg", "状态修改失败：" + e.getMessage());
        }
        return "redirect:/admin";
    }
    /**
     * API接口：获取用户信息（JSON格式）
     */
    @ResponseBody
    @GetMapping("/api/user/{userid}")
    public ResponseEntity<Map<String, Object>> getUserJson(@PathVariable("userid") Long userid,
                                                           HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 管理员权限验证
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 1) {
                result.put("code", 401);
                result.put("msg", "请以管理员身份登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            User user = userService1.selectByUserid(userid);
            if (user == null) {
                result.put("code", 404);
                result.put("msg", "用户不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }

            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}