package com.itmio.universitystudentloan.service.impl;

import com.itmio.universitystudentloan.mapper.UserMapper;
import com.itmio.universitystudentloan.pojo.User;
import com.itmio.universitystudentloan.utils.Md5Util;
import com.itmio.universitystudentloan.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService implements com.itmio.universitystudentloan.service.UserService {
    @Autowired
    private UserMapper userMapper;
    //注册
    @Override
    public void register(Integer usertype,String username,String password) {
        userMapper.register(usertype,username,password);
    }

    //根据用户名查找用户
    @Override
    public User findByUser(String username) {
        return userMapper.findByUser(username);
    }

    //更新用户信息
    @Override
    public void userUpdate(User user) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id= (Integer) map.get("id");
        user.setId(id);
        userMapper.userUpdate(user);
    }

    //更新用户头像
    @Override
    public void userAvatar(String avatarUrl) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id= (Integer) map.get("id");
        userMapper.userAvatar(avatarUrl,id);
    }

    //重置密码
    @Override
    public void userPassword(String newPwd) {
        String md5String = Md5Util.getMD5String(newPwd);
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer id= (Integer) map.get("id");
        userMapper.userPassword(md5String,id);
    }
}
