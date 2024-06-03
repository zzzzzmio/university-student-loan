package com.itmio.universitystudentloan.service;

import com.itmio.universitystudentloan.pojo.User;

public interface UserService {
    void register(Integer usertype,String username,String password);

    User findByUser(String username);

    void userUpdate(User user);

    void userAvatar(String avatarUrl);

    void userPassword(String newPwd);
}
