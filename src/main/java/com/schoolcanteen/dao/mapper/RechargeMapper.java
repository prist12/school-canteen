package com.schoolcanteen.dao.mapper;

import com.schoolcanteen.entity.Recharge;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface RechargeMapper {
   Recharge selectByRechargeid(Long rechargeid);
    Recharge selectByRechargeNo(String rechargeNo);
 List<Recharge> selectByStudentId(
         @Param("studentId") Long studentId,
         @Param("startDate") Date startDate,
         @Param("endDate") Date endDate
 );
   int insert(Recharge recharge);
    int updatePayStatus(@Param("rechargeid") Long rechargeid, @Param("payStatus") Integer payStatus, @Param("payTime") Date payTime);
 BigDecimal sumAmountByStudent(
         @Param("studentId") Long studentId,
         @Param("startDate") Date startDate,
         @Param("endDate") Date endDate
 );}