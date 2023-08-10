package com.packages.controller;

import com.packages.entity.User;
import com.packages.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService UserService;

    @PostMapping("/login")
    public int insertMaterials(@RequestBody User User, HttpServletRequest request) {
        String uname = User.getUname();
        String pwd = User.getPwd();
        int client = User.getClient();
        Integer uid = UserService.login(uname,pwd,client);
        if(uid != 0){
            //将登陆成功后的user对象，存储到session
            HttpSession session = request.getSession();
            session.setAttribute("uid",uid);
            return 1;  // 返回登录成功标识
        } else {
            return 0;  // 返回登录失败标识
        }
    }
}
