package com.tanhua.sso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tanhua.common.pojo.User;
import com.tanhua.common.pojo.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/7 15:58
 */
public interface UserInfoService extends IService<UserInfo> {

    public Boolean saveUserInfo(Map<String,String> param, User user);

    public Boolean saveUserLogo(MultipartFile file, String token);
}
