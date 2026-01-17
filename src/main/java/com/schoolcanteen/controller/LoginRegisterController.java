package com.schoolcanteen.controller;

import com.schoolcanteen.entity.User;
import com.schoolcanteen.service.UserService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class LoginRegisterController {


@Autowired
private UserService1 userService1;


    // 跳转登录页

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String toLoginPage(){
        return "login";
    }
    // 跳转注册页
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String toRegisterPage() {
        return "register";
    }
    @PostMapping("/login")
    public String doLogin(
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "password", required = false) String password,
            Model model,
            HttpSession session) {

        // 1. 基础参数校验
        if (phone == null || phone.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("errorMsg", "手机号或密码不能为空");
            return "login"; // 回显登录页并提示错误
        }

        try {
            // 2. 调用服务层校验账号密码（数据库查询+密码比对+状态校验）
            User loginUser = userService1.login(phone, password);

            // 3. 登录成功：存储用户信息到Session
            session.setAttribute("loginUser", loginUser); // 存储完整用户信息
            session.setAttribute("userPhone", loginUser.getPhone()); // 手机号
            session.setAttribute("userType", loginUser.getUserType()); // 用户类型（角色）
            session.setAttribute("userName", loginUser.getUserName()); // 真实姓名

            // 4. 根据用户类型（角色）跳转不同页面
            Integer userType = loginUser.getUserType();
            switch (userType) {
                case 1: // 系统管理员
                    return "redirect:/admin"; // 重定向到管理员首页
                case 3: // 家长
                    return "redirect:/parent"; // 重定向到家长首页
                case 4: // 学生
                    return "redirect:/student"; // 重定向到学生首页
                case 5: // 餐饮员
                    return "redirect:/catering"; // 重定向到餐饮员首页
                default: // 未知角色
                    model.addAttribute("errorMsg", "未知的用户角色，请联系管理员");
                    return "login";
            }

        } catch (RuntimeException e) {
            // 5. 登录失败：捕获服务层异常，回显错误信息
            model.addAttribute("errorMsg", e.getMessage());
            return "login";
        }
    }
   // 处理注册表单提交（POST请求
    @PostMapping("/register")
    public String doRegister(
            @RequestParam("phone") String phone,
            @RequestParam("password") String password,
            @RequestParam("userName") String userName,
            @RequestParam("gender") String gender,
            @RequestParam("userType") Integer userType,
            Model model
            ){
        try {
            // 1. 参数校验
            if (phone == null || phone.trim().isEmpty() || password == null || password.trim().isEmpty()
                    || userName == null || userName.trim().isEmpty() || gender == null || gender.trim().isEmpty()
                    || userType == null) {
                model.addAttribute("errorMsg", "请填写完整的注册信息");
                return "register"; // 回显注册页并提示错误
            }
            // 2. 调用服务层进行注册
            User user = new User();
            user.setPhone(phone.trim());
            user.setPassword(password.trim());
            user.setUserName(userName.trim());
            user.setUserType(userType);
            user.setGender(Integer.valueOf(gender));
            user.setStatus(1);
            user.setRelateId(null); // 关联ID暂为空，后续可完善
            user.setPermissions(null);
            userService1.register(user);
            model.addAttribute("successMsg", "注册成功，请登录");
            return "login";
        }catch (RuntimeException e){
            model.addAttribute("errorMsg", e.getMessage());
        }catch (Exception e){
            model.addAttribute("errorMsg", "注册失败，请稍后再试");
        }
        return "register";
    }
    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 销毁会话
        return "redirect:/login"; // 重定向到登录页
    }
}