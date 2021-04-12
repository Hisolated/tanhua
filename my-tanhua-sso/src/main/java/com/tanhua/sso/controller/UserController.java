package com.tanhua.sso.controller;


import com.tanhua.common.enums.ResultEnum;
import com.tanhua.common.exception.BusinessException;
import com.tanhua.common.exception.SystemException;
import com.tanhua.common.pojo.User;
import com.tanhua.common.utils.UserThreadLocal;
import com.tanhua.sso.service.UserInfoService;
import com.tanhua.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        Map<String,Object> result = userService.loginVerification(phone, code);
        System.out.println(result);
        if(result.size() == 0){
            throw  new BusinessException(ResultEnum.CREATE_TOKEN_FAIL.getCode(),ResultEnum.CREATE_TOKEN_FAIL.getMessage());
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/loginReginfo")
    public ResponseEntity<Object>  loginReginfo(@RequestBody Map<String,String> param){

        //1. 解析token,查询用户
        User user = UserThreadLocal.get();
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


    /**
     * 校验token，根据token查询用户数据
     *
     * @param token
     * @return
     */
    @GetMapping("{token}")
    public User queryUserByToken(@PathVariable("token") String token) {
        return this.userService.findByToken(token);
    }
}
