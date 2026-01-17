package com.schoolcanteen.service;

import com.schoolcanteen.dao.mapper.AccountFlowMapper;
import com.schoolcanteen.dao.mapper.RechargeMapper;
import com.schoolcanteen.entity.AccountFlow;
import com.schoolcanteen.entity.Recharge;
import com.schoolcanteen.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RechargeService1 {
    @Autowired
    private RechargeMapper rechargeMapper;
    @Autowired
    private StudentService1 studentService;
    @Autowired
    private AccountFlowMapper accountFlowMapper;

    public Recharge recharge(Long studentid, BigDecimal amount, Integer rechargeType, Long rechargeUserId) {
        // 生成充值单号
        String rechargeNo = "REC" + System.currentTimeMillis();

        // 创建充值记录
        Recharge recharge = new Recharge();
        recharge.setRechargeNo(rechargeNo);
        recharge.setStudentId(studentid);
        recharge.setRechargeType(rechargeType);
        recharge.setRechargeUserId(rechargeUserId); // 需确保是t_user.user_id
        recharge.setAmount(amount);
        recharge.setPayMethod(1); // 1-在线支付
        recharge.setPayStatus(1); // 1-支付成功
        recharge.setPayTime(new Date());

        rechargeMapper.insert(recharge);

        // 增加余额并记录流水
        Student student = studentService.getStudentInfo(studentid);
        studentService.updateBalance(studentid, amount, 2, rechargeNo, rechargeUserId);

        // 记录账户流水
        AccountFlow flow = new AccountFlow();
        flow.setStudentId(studentid);
        flow.setFlowType(2); // 2-充值
        flow.setAmount(amount);
        flow.setCurrentBalance(student.getBalance().add(amount));
        flow.setRelateNo(rechargeNo);
        flow.setOperatorId(rechargeUserId);
        flow.setCreateTime(new Date());
        accountFlowMapper.insert(flow);

        return recharge;
    }


    public List<Recharge> getStudentRecharges(Long studentId, Date startDate, Date endDate) {
        return rechargeMapper.selectByStudentId(studentId, startDate, endDate);
    }

    public BigDecimal getTotalRecharge(Long studentId, Date startDate, Date endDate) {
        BigDecimal sum = rechargeMapper.sumAmountByStudent(studentId, startDate, endDate);
        return sum != null ? sum : BigDecimal.ZERO;
    }
}