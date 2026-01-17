package com.schoolcanteen.dao.mapper;

import com.schoolcanteen.entity.AccountFlow;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface AccountFlowMapper {
int insert(AccountFlow accountFlow);
    List<AccountFlow> selectByStudentId(@Param("studentId") Long studentId, @Param("flowType") Integer flowType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
    BigDecimal sumAmountByStudentAndType(@Param("studentId") Long studentId, @Param("flowType") Integer flowType, @Param("startTime") Date startTime, @Param("endTime") Date endTime);
}