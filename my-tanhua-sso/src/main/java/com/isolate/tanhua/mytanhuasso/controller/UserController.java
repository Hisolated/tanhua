package com.isolate.tanhua.mytanhuasso.controller;

import com.isolate.tanhua.mytanhuasso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void login(@RequestBody String phone) {
        System.out.println("login()");
        userService.login(phone);
    }

    @PostMapping("/loginVerification")
    @ResponseBody
    public String loginVerification(@RequestBody String phone,@RequestBody String verificationCode) {
        String token = userService.loginVerification(phone, verificationCode);
        return token;
    }
}
