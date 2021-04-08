package com.isolate.tanhua.mytanhuasso.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.isolate.tanhua.mytanhuasso.enums.SexEnum;
import com.isolate.tanhua.mytanhuasso.mapper.UserInfoMapper;
import com.isolate.tanhua.mytanhuasso.pojo.User;
import com.isolate.tanhua.mytanhuasso.pojo.UserInfo;
import com.isolate.tanhua.mytanhuasso.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/7 15:25
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper,UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public Boolean saveUserInfo(Map<String,String> param, User user) {
        UserInfo userInfo =new UserInfo();
        userInfo.setSex(SexEnum.valueOf(param.get("gender")));
        userInfo.setUserId(user.getId());
        userInfo.setNickName(param.get("nickname"));
        userInfo.setBirthday(param.get("birthday"));
        userInfo.setCity(param.get("city"));

        int result = userInfoMapper.insert(userInfo);

        return result == 1 ? true:false;
    }
}
