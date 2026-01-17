package com.schoolcanteen.service;

import com.schoolcanteen.dao.mapper.UserMapper;
import com.schoolcanteen.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService1 {
    @Autowired
    private UserMapper userMapper;

    // 用户登录
//
    public User login(String phone,String password){
        if (phone == null || phone.isEmpty() || password == null || password.isEmpty()) {
            throw new RuntimeException("手机号和密码不能为空");
        }//格式验证
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            throw new RuntimeException("手机号格式不正确");
        }
        // 查询用户
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            throw new RuntimeException("账号不存在");
        }
        //验证密码
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 验证账号状态
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        //返回用户信息
        User safeUser = new User();
        safeUser.setUserid(user.getUserid());
        safeUser.setPhone(user.getPhone());
        safeUser.setUserName(user.getUserName());
        safeUser.setUserType(user.getUserType());
        safeUser.setStatus(user.getStatus());
        safeUser.setGender(user.getGender());
        safeUser.setRelateId(user.getRelateId());
        safeUser.setPermissions(user.getPermissions());

        return safeUser;

    }

    public void register(User user) {
        if (userMapper.selectByPhone(user.getPhone()) != null) {
            throw new RuntimeException("手机号已注册");
        }
        userMapper.insert(user);
    }
    // 管理员功能
    public List<User> selectAllUsers() {
        return userMapper.selectAll();
    }

    // 根据ID查询用户
    public User selectByUserid(Long userid) {
        return userMapper.selectByUserid(userid);
    }
    // 根据用户类型查询
    public List<User> selectByUserType(Integer userType) {
        return userMapper.selectByUserType(userType);
    }

    // 更新用户状态（禁用/启用）
    public void updateStatus(Long userid, Integer status) {
        userMapper.updateStatus(userid, status);
    }
    public void addUser(User user) {
        userMapper.insert(user);
    }

    public void updateUser(User user) {
        userMapper.update(user);
    }

    public void deleteUser(Long userid) {
        userMapper.delete(userid);
    }

    public void resetPassword(Long userid, String newPassword) {
        userMapper.updatePassword(userid, newPassword);
    }
}
