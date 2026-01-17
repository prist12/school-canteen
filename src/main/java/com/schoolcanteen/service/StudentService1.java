package com.schoolcanteen.service;

import com.schoolcanteen.dao.mapper.StudentMapper;
import com.schoolcanteen.entity.Student;
import com.schoolcanteen.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StudentService1 {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private UserService1 userService;
    public Student getStudentByUserId(Long userId) {
        return studentMapper.selectByUserid(userId);
    }

    public Student getStudentInfo(Long studentid) {
        return studentMapper.selectByStudentid(studentid);
    }

    public String getStudentUserName(Long studentId) {
        // 1. 查询学生信息，获取userId
        Student student = this.getStudentInfo(studentId);
        if (student == null || student.getUserId() == null) {
            return "未知姓名";
        }
        // 2. 根据userId查询User信息，获取userName
        User studentUser = userService.selectByUserid(student.getUserId());
        return studentUser != null ? studentUser.getUserName() : "未知姓名";
    }
    public void addStudent(Student student) {
        studentMapper.insert(student);
    }

    public void updateStudent(Student student) {
        studentMapper.update(student);
    }

    public List<Student> getClassStudents(String className) {
        return studentMapper.selectByClassName(className);
    }

    // 账户操作
    public BigDecimal getBalance(Long studentid) {
        Student student = studentMapper.selectByStudentid(studentid);
        return student != null ? student.getBalance() : BigDecimal.ZERO;
    }

    @Transactional
    public void updateBalance(Long studentid, BigDecimal amount, Integer flowType, String relateNo, Long operatorId) {
        Student student = studentMapper.selectByStudentid(studentid);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        BigDecimal newBalance;
        if (flowType == 1) { // 1-消费
            newBalance = student.getBalance().subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("余额不足");
            }
        } else if (flowType == 2) { // 2-充值
            newBalance = student.getBalance().add(amount);
        } else {
            throw new RuntimeException("无效的流水类型");
        }
        studentMapper.updateBalance(studentid, newBalance);
    }
    // 家长绑定
    public void bindParent(Long studentid, Long parentId) {
        studentMapper.updateParentBindStatus(studentid, parentId, 1); // 1-已绑定
    }
    public List<Student> getStudentsByParentId(Long parentId) {
        return studentMapper.selectByParentId(parentId);
    }
}
