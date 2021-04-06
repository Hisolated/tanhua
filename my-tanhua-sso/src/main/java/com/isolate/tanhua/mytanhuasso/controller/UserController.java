package com.isolate.tanhua.mytanhuasso.controller;

import com.isolate.tanhua.mytanhuasso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/6 19:04
 */
@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public void  login(String phone){
            userService.login(phone);
    }

    @PostMapping("/loginVerification")
    @ResponseBody
    public String  loginVerification(String phone,String verificationCode){
        String token = userService.loginVerification(phone, verificationCode);
        return token;
    }
}
