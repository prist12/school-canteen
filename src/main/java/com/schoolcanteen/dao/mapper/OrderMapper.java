package com.schoolcanteen.dao.mapper;

import com.schoolcanteen.entity.Order;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderMapper {
    Order selectByOrderid(Long orderid);
    Order selectByOrderNo(String orderNo);

    List<Order> selectByStudentId(
            @Param("studentId") Long studentId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );    int insert(Order order);
    int updateStatus(@Param("orderid") Long orderid, @Param("payStatus") Integer payStatus, @Param("orderStatus") Integer orderStatus);
    BigDecimal sumTotalAmountByStudent(
            @Param("studentId") Long studentId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );}