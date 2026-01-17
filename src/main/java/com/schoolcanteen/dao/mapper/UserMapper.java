package com.schoolcanteen.dao.mapper;

import com.schoolcanteen.entity.User;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserMapper {

    List<User> selectAll();

    User selectByUserid(Long userid);
    User selectByPhone(String phone);
    int insert(User user);
    int update(User user);
    int delete(Long userid);
    int updatePassword(@Param("userid") Long userid, @Param("password") String password);
    int updateStatus(@Param("userid") Long userid, @Param("status") Integer status);
    List<User> selectByUserType(Integer userType);

}