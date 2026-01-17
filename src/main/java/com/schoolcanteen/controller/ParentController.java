package com.schoolcanteen.controller;

import com.schoolcanteen.entity.AccountFlow;
import com.schoolcanteen.entity.Recharge;
import com.schoolcanteen.entity.Student;
import com.schoolcanteen.entity.User;
import com.schoolcanteen.service.AccountFlowService1;
import com.schoolcanteen.service.RechargeService1;
import com.schoolcanteen.service.StudentService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * 家长控制器
 * 提供学生账单查询、余额查询、充值等功能
 */
@Controller
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private StudentService1 studentService;

    @Autowired
    private AccountFlowService1 accountFlowService;

    // 新增注入充值服务
    @Autowired
    private RechargeService1 rechargeService;

    /**
     * 家长首页 - 从session获取parentId
     */
    @GetMapping("")
    public String parentIndex(HttpSession session, Model model) {
        try {
            // 从session获取登录用户信息
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                model.addAttribute("code", 401);
                model.addAttribute("msg", "请以家长身份登录");
                return "login"; // 跳转到登录页
            }

            Long parentId = loginUser.getUserid(); // 使用登录用户的ID作为parentId
            List<Student> studentList = studentService.getStudentsByParentId(parentId);
            // 获取学生名称
            Map<Long, String> studentNameMap = new HashMap<>();
            for (Student student : studentList) {
                String studentName = studentService.getStudentUserName(student.getStudentid());
                studentNameMap.put(student.getStudentid(), studentName);
            }
            model.addAttribute("studentList", studentList);
            model.addAttribute("studentNameMap", studentNameMap); // 传入学生姓名Map
            model.addAttribute("parentId", parentId);
            model.addAttribute("page", "studentList");
            return "parent";
        } catch (Exception e) {
            model.addAttribute("code", 500);
            model.addAttribute("msg", "查询学生列表失败：" + e.getMessage());
            return "parent";
        }
    }

    /**
     * 跳转到充值页面 - 从session获取parentId
     */
    @GetMapping("/toRecharge")
    public String toRecharge(@RequestParam Long studentId, HttpSession session, Model model) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                model.addAttribute("code", 401);
                model.addAttribute("msg", "请以家长身份登录");
                return "login";
            }

            Long parentId = loginUser.getUserid();
            validateParentStudentBind(studentId, parentId);

            Student student = studentService.getStudentInfo(studentId);
            String studentName = studentService.getStudentUserName(studentId);
            model.addAttribute("student", student);
            model.addAttribute("studentName", studentName);
            model.addAttribute("parentId", parentId);
            model.addAttribute("page", "recharge");
            return "parent";
        } catch (Exception e) {
            model.addAttribute("code", 400);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("page", "studentList");
            return "parent";
        }
    }

    /**
     * 跳转到流水查询页面 - 从session获取parentId
     */
    @GetMapping("/toFlowQuery")
    public String toFlowQuery(@RequestParam Long studentId, HttpSession session, Model model) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                model.addAttribute("code", 401);
                model.addAttribute("msg", "请以家长身份登录");
                return "login";
            }

            Long parentId = loginUser.getUserid();
            validateParentStudentBind(studentId, parentId);

            Student student = studentService.getStudentInfo(studentId);
            String studentName = studentService.getStudentUserName(studentId);
            model.addAttribute("student", student);
            model.addAttribute("studentName", studentName);
            model.addAttribute("parentId", parentId);
            model.addAttribute("page", "flowQuery");
            return "parent";
        } catch (Exception e) {
            model.addAttribute("code", 400);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("page", "studentList");
            return "parent";
        }
    }

    /**
     * 流水查询接口（适配JSP表单） - 从session获取parentId
     */
    @PostMapping("/queryFlows")
    public String getStudentFlows(
            @RequestParam Long studentId,
            @RequestParam(required = false) Integer flowType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endTime,
            HttpSession session,
            Model model) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                model.addAttribute("code", 401);
                model.addAttribute("msg", "请以家长身份登录");
                return "login";
            }

            Long parentId = loginUser.getUserid();
            validateParentStudentBind(studentId, parentId);

            Student student = studentService.getStudentInfo(studentId);

            // 转换时间（补全时分秒）
            java.sql.Date startSqlDate = null;
            java.sql.Date endSqlDate = null;
            if (startTime != null) {
                startSqlDate = new java.sql.Date(startTime.getTime());
            }
            if (endTime != null) {
                // 结束时间补全为当天23:59:59
                Calendar cal = Calendar.getInstance();
                cal.setTime(endTime);
                cal.set(Calendar.HOUR_OF_DAY, 23);
                cal.set(Calendar.MINUTE, 59);
                cal.set(Calendar.SECOND, 59);
                endSqlDate = new java.sql.Date(cal.getTimeInMillis());
            }

            List<AccountFlow> flowList = accountFlowService.getStudentFlows(studentId, flowType, startSqlDate, endSqlDate);
            BigDecimal summary = accountFlowService.getFlowSummary(studentId, flowType, startSqlDate, endSqlDate);

            model.addAttribute("student", student);
            model.addAttribute("parentId", parentId);
            model.addAttribute("flowList", flowList);
            model.addAttribute("summary", summary);
            model.addAttribute("page", "flowQuery");
            return "parent";
        } catch (Exception e) {
            model.addAttribute("code", 400);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("page", "flowQuery");
            return "parent";
        }
    }

    /**
     * 充值接口（适配JSP表单提交） - 从session获取parentId
     */
    @PostMapping("/doRecharge")
    @Transactional(rollbackFor = Exception.class)
    public String recharge(
            @RequestParam Long studentId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String remark,
            HttpSession session,
            Model model) {
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                model.addAttribute("code", 401);
                model.addAttribute("msg", "请以家长身份登录");
                return "login";
            }

            Long parentId = loginUser.getUserid();

            // 参数校验
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("充值金额必须大于0");
            }

            // 验证家长与学生的绑定关系
            validateParentStudentBind(studentId, parentId);

            //调用RechargeService生成充值订单+更新余额+流水
            Recharge recharge = rechargeService.recharge(studentId, amount, 1, parentId);

            // 查询充值后的学生信息
            Student student = studentService.getStudentInfo(studentId);
            BigDecimal newBalance = studentService.getBalance(studentId);
            String studentName = studentService.getStudentUserName(studentId);
            model.addAttribute("code", 200);
            model.addAttribute("msg", "充值成功！充值单号：" + recharge.getRechargeNo() + "，当前余额：¥" + newBalance);
            model.addAttribute("student", student);
            model.addAttribute("studentName", studentName);
            model.addAttribute("parentId", parentId);
            model.addAttribute("page", "recharge");
            return "parent";
        } catch (Exception e) {
            model.addAttribute("code", 400);
            model.addAttribute("msg", "充值失败：" + e.getMessage());
            model.addAttribute("page", "recharge");
            return "parent";
        }
    }

    /**
     * 查询学生余额（API接口）- 从session获取parentId
     */
    @ResponseBody
    @GetMapping("/api/balance")
    public ResponseEntity<Map<String, Object>> getStudentBalance(
            @RequestParam Long studentId,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                result.put("code", 401);
                result.put("msg", "请以家长身份登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            Long parentId = loginUser.getUserid();
            validateParentStudentBind(studentId, parentId);

            BigDecimal balance = studentService.getBalance(studentId);
            result.put("code", 200);
            result.put("msg", "余额查询成功");
            result.put("data", balance);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("msg", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "系统异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 查询学生流水（API接口）- 从session获取parentId
     */
    @ResponseBody
    @GetMapping("/api/flows")
    public ResponseEntity<Map<String, Object>> getStudentAccountFlows(
            @RequestParam Long studentId,
            @RequestParam(required = false) Integer flowType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                result.put("code", 401);
                result.put("msg", "请以家长身份登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            Long parentId = loginUser.getUserid();
            validateParentStudentBind(studentId, parentId);

            List<AccountFlow> flows = accountFlowService.getStudentFlows(studentId, flowType,
                    startTime != null ? new java.sql.Date(startTime.getTime()) : null,
                    endTime != null ? new java.sql.Date(endTime.getTime()) : null);

            // 计算流水汇总
            BigDecimal sum = accountFlowService.getFlowSummary(studentId, flowType,
                    startTime != null ? new java.sql.Date(startTime.getTime()) : null,
                    endTime != null ? new java.sql.Date(endTime.getTime()) : null);

            result.put("code", 200);
            result.put("msg", "流水查询成功");
            result.put("data", flows);
            result.put("summary", sum);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("msg", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "系统异常：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 学生账户充值（API接口）- 从session获取parentId
     */
    @ResponseBody
    @PostMapping("/api/recharge")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Map<String, Object>> rechargeStudentAccount(
            @RequestParam Long studentId,
            @RequestParam BigDecimal amount,
            @RequestParam String relateNo,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        try {
            User loginUser = (User) session.getAttribute("loginUser");
            if (loginUser == null || loginUser.getUserType() != 3) {
                result.put("code", 401);
                result.put("msg", "请以家长身份登录");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            Long parentId = loginUser.getUserid();

            // 参数校验
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("充值金额必须大于0");
            }

            // 验证家长与学生的绑定关系
            validateParentStudentBind(studentId, parentId);

            // 调用充值服务生成订单+更新余额+流水
            Recharge recharge = rechargeService.recharge(studentId, amount, 1, parentId);

            // 查询充值后的余额
            BigDecimal newBalance = studentService.getBalance(studentId);

            result.put("code", 200);
            result.put("msg", "充值成功");
            result.put("data", newBalance);
            result.put("rechargeNo", recharge.getRechargeNo()); // 返回充值单号
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            result.put("code", 400);
            result.put("msg", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "充值失败：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    /**
     * 验证家长与学生的绑定关系
     */
    private void validateParentStudentBind(Long studentId, Long parentId) {
        Student student = studentService.getStudentInfo(studentId);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        if (!parentId.equals(student.getParentId()) || student.getParentBindStatus() != 1) {
            throw new RuntimeException("您未绑定该学生，无法操作");
        }
    }

}