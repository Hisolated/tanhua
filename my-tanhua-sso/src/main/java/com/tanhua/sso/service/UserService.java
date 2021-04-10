package com.tanhua.sso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.common.pojo.User;

import java.util.Map;

/**
*
*/
public interface UserService extends IService<User> {

    /**
     * 请求验证码下发
     * @param phone
     */
    public void login(String phone);

    Map<String,Object> loginVerification(String phone, String code);

    User findByToken(String token);
}
