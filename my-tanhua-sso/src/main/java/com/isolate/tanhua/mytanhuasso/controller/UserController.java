package com.isolate.tanhua.mytanhuasso.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.additional.update.impl.UpdateChainWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isolate.tanhua.mytanhuasso.pojo.User;
import com.isolate.tanhua.mytanhuasso.pojo.UserInfo;
import com.isolate.tanhua.mytanhuasso.service.UserInfoService;
import com.isolate.tanhua.mytanhuasso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String,String> param) {
        System.out.println("login()");
        String phone = param.get("phone");
        userService.login(phone);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/loginVerification")
    @ResponseBody
    public ResponseEntity<Object> loginVerification(@RequestBody Map<String,String> param){
        String phone = param.get("phone");
        String code = param.get("verificationCode");
        String data = userService.loginVerification(phone, code);
        System.out.println(data);
        if(StringUtils.isBlank(data)){
            //todo:登录失败,抛出自定义异常
        }
        Map<String, Object> result = new HashMap<>(2);
        String[] ss = StringUtils.split(data, '|');
        result.put("token", ss[0]);
        result.put("isNew", Boolean.valueOf(ss[1]));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/loginReginfo")
    public ResponseEntity<Object>  loginReginfo(@RequestHeader("Authorization") String token, @RequestBody Map<String,String> param){

        System.out.println("token: " + token);
        //1. 解析token,查询用户
        User user = userService.findByToken(token);
        System.out.println("user: " + user);
        //2. 通过token返回的用户,更新userinfo的信息

        boolean result = userInfoService.saveUserInfo(param,user);
        if(!result){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
        return ResponseEntity.ok(null);
    }

    //设置头像
    @PostMapping("/loginReginfo/head")
    public ResponseEntity<Object> saveUserLogo(@RequestParam("headPhoto") MultipartFile file,
                                               @RequestHeader("Authorization") String token) {
        try {
            Boolean bool = this.userInfoService.saveUserLogo(file, token);
            if (bool) {
                return ResponseEntity.ok(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
