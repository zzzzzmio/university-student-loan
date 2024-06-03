package com.itmio.universitystudentloan.controller;

import com.itmio.universitystudentloan.pojo.Result;
import com.itmio.universitystudentloan.pojo.User;
import com.itmio.universitystudentloan.service.UserService;
import com.itmio.universitystudentloan.utils.JwtUtil;
import com.itmio.universitystudentloan.utils.Md5Util;
import com.itmio.universitystudentloan.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin
public class UserController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;

    //注册
    @PostMapping("/register")
    public Result register(Integer usertype, @Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        //查看是否有重名
        User u = userService.findByUser(username);
        if(u==null){
            //加密密码
            String pd = Md5Util.getMD5String(password);
            userService.register(usertype,username,pd);
            return Result.success();
        }
        else {
            return Result.error("该用户名已被占用");
        }
    }

    //登录
    @PostMapping("/login")
    public Result login(Integer usertype, @Pattern(regexp = "^\\S{5,16}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        //检查用户名是否存在
        User u = userService.findByUser(username);
        if(u==null){
            return Result.error("用户名不存在");
        }
        if(Md5Util.getMD5String(password).equals(u.getPassword()) && usertype==u.getUsertype()){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",u.getId());
            claims.put("username",u.getUsername());
            String token = JwtUtil.genToken(claims);
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token);
            return Result.success(token);
        }
        return Result.error("密码或用户类型不正确");
    }

    //获取用户信息
    @GetMapping("/userinfo")
    public Result<User> getUserInfo(){
        Map<String,Object> map = ThreadLocalUtil.get();
        String username= (String) map.get("username");
        User loginuser = userService.findByUser(username);
        return Result.success(loginuser);
    }

    //更改用户信息
    @PutMapping("/update")
    public Result userUpdate(@RequestBody @Validated User user){
        userService.userUpdate(user);
        return  Result.success();
    }

    //更新用户头像
    @PutMapping("/avatar")
    public Result userAvatar(@RequestParam @URL String avatarUrl){
        userService.userAvatar(avatarUrl);
        return Result.success();
    }
    //重置密码
    @PatchMapping("/password")
    public Result userPassword(@RequestParam Map<String,String> params,@RequestHeader("Authorization") String token){
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUser(username);
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if(!Md5Util.getMD5String(oldPwd).equals(loginUser.getPassword())){
            return Result.error("密码错误");
        }
        if(oldPwd.equals(newPwd)){
            return Result.error("新密码不能与原密码重复");
        }
        if(!newPwd.equals(rePwd)){
            return Result.error("两次输入的密码不一致");
        }
        userService.userPassword(newPwd);
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
