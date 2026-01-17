package com.schoolcanteen.dao.mapper;

import com.schoolcanteen.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface StudentMapper {

Student selectByStudentid(Long studentid);
    Student selectByUserid(Long userid);
    Student selectByStudentNo(String studentNo);
    int insert(Student student);
    int update(Student student);
    int updateBalance(@Param("studentid") Long studentid, @Param("balance") BigDecimal balance);
    List<Student> selectByClassName(String className);
    List<Student> selectByParentId(Long parentId);
    int updateParentBindStatus(@Param("studentid") Long studentid, @Param("parentId") Long parentId, @Param("status") Integer status);
}