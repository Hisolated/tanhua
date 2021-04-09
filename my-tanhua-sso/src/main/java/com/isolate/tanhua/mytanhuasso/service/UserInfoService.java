package com.isolate.tanhua.mytanhuasso.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.isolate.tanhua.mytanhuasso.pojo.User;
import com.isolate.tanhua.mytanhuasso.pojo.UserInfo;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
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
