package com.itmio.universitystudentloan.mapper;

import com.itmio.universitystudentloan.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.PostMapping;

@Mapper
public interface UserMapper {
    //注册
    @Insert("insert into user(user_type,username,password,create_time,update_time) values (#{usertype},#{username},#{password},now(),now())")
    void register(Integer usertype,String username,String password);

    @Select("select * from user where username=#{username}")
    User findByUser(String username);

    @Update("update user set nickname=#{nickname},email=#{email},update_time=now() where id=#{id}")
    void userUpdate(User user);

    @Update("update user set user_pic=#{avatarUrl},update_time=now() where id=#{id}")
    void userAvatar(String avatarUrl, Integer id);

    @Update("update user set password=#{md5String},update_time=now() where id=#{id}")
    void userPassword(String md5String,Integer id);
}
