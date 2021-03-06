package com.tanhua.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.common.enums.SexEnum;
import com.tanhua.common.mapper.UserInfoMapper;
import com.tanhua.common.pojo.User;
import com.tanhua.common.pojo.UserInfo;
import com.tanhua.sso.service.UserInfoService;
import com.tanhua.sso.service.UserService;
import com.tanhua.sso.utils.FaceEngineUtils;
import com.tanhua.sso.utils.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/7 15:25
 */
@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserService userService;


    @Override
    public Boolean saveUserInfo(Map<String, String> param, User user) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSex(StringUtils.equalsIgnoreCase(param.get("gender"), "man") ? SexEnum.MAN : SexEnum.WOMAN);
        userInfo.setUserId(user.getId());
        userInfo.setNickName(param.get("nickname"));
        userInfo.setBirthday(param.get("birthday"));
        userInfo.setCity(param.get("city"));

        int result = userInfoMapper.insert(userInfo);

        return result == 1;
    }

    @Override
    public Boolean saveUserLogo(MultipartFile file, String token) {
        //1. 解析token(注意:存的是user_id,而不是userinfo的id)
        User user = userService.findByToken(token);
        if (null == user) {
            return false;
        }

        try {
            //2. 校验人像,使用虹软人脸识别技术
            boolean b = FaceEngineUtils.checkIsPortrait(file.getBytes());
            if (!b) {
                //通过人脸识别后,返回不是人脸
                return false;
            }
        } catch (IOException e) {
            //todo:抛出自定义异常(虹软人脸识别失败),其实是getBytes报的异常
            e.printStackTrace();
        }

        //3. 对file进行处理,上传阿里云oss服务器,同时返回访问路径
        try {
            String filePath = FileUploadUtils.upload(file);
            if (null != filePath) {
                //4. 图片上传成功,将图片路径更新到mysql数据库中userinfo表中
                UpdateWrapper<UserInfo> wrapper = new UpdateWrapper<>();
                wrapper.set("logo", filePath)
                        .eq("user_id", user.getId());
                int result = userInfoMapper.update(null, wrapper);
                return true;
            }
        } catch (IOException e) {
            //todo:抛出自定义异常(上传阿里云失败),这个其实是file.getBate()的异常,即文件无法转化为字节码数组
            e.printStackTrace();
        }
        return null;
    }
}
