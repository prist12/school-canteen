package com.schoolcanteen.service;

import com.schoolcanteen.dao.mapper.AccountFlowMapper;
import com.schoolcanteen.entity.AccountFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class AccountFlowService1 {
    @Autowired
    private AccountFlowMapper accountFlowMapper;

    public List<AccountFlow> getStudentFlows(Long studentId, Integer flowType, Date startTime, Date endTime) {
        return accountFlowMapper.selectByStudentId(studentId, flowType, startTime, endTime);
    }

    public BigDecimal getFlowSummary(Long studentId, Integer flowType, Date startTime, Date endTime) {
        BigDecimal sum = accountFlowMapper.sumAmountByStudentAndType(studentId, flowType, startTime, endTime);
        return sum != null ? sum : BigDecimal.ZERO;
    }
    public void insert(AccountFlow accountFlow) {
        accountFlowMapper.insert(accountFlow);
    }
}