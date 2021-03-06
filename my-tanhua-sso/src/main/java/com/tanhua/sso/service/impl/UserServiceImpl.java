package com.tanhua.sso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.common.enums.ResultEnum;
import com.tanhua.common.exception.BusinessException;
import com.tanhua.common.mapper.UserMapper;
import com.tanhua.common.pojo.User;
import com.tanhua.sso.service.UserService;
import com.tanhua.sso.utils.JwtUtils;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Override
    public void login(String phone) {
        //todo:作验证码发送操作,同时将phone保存到Redis中
        //1.发送验证码到对应的手机,由于短信暂时用不了,所以需要固定验证码
        String verificationCode = "123456";
        //2.保存phone到Redis中
        String redisKey = "user:login:" + phone;
        this.redisTemplate.opsForValue().set(redisKey, verificationCode);
    }

    @Override
    public Map<String, Object> loginVerification(String phone, String code) {
        // redis中key的命名(项目:业务名:手机号)
        String redisKey = "user:login:" + phone;
        boolean isNew = false;

        String redisCode = this.redisTemplate.opsForValue().get(redisKey);
        System.out.println("redisCode:" + redisCode);

        if (!StringUtils.equals(code, redisCode)) {
            /**
             * 验证码错误
             */
            throw new BusinessException(ResultEnum.VERIFY_CODE_FAIL.getCode(), ResultEnum.VERIFY_CODE_FAIL.getMessage());
        }
        //验证码正确
        //验证完成后,redis中数据需要删除
        this.redisTemplate.delete(redisKey);

        //从数据库中查询,看是否存在该用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", phone);

        User user = this.userMapper.selectOne(queryWrapper);
        if (null == user) {
            //mysql中不存在该用户,将该用户保存到数据库中,暂时不保存密码
            //需要注册该用户,同时设置默认密码
            user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex("123456"));
            this.userMapper.insert(user);

            isNew = true;
        }
        //生成token

        //设置payload
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("mobile", user.getMobile());

        // 过期时间设置为12小时
        String token = JwtUtils.createToken(claims, 12 * 60 * 60L);

        try {
            //发送用户登录成功的消息
            Map<String, Object> msg = new HashMap<>();
            msg.put("id", user.getId());
            msg.put("date", System.currentTimeMillis());

            this.rocketMQTemplate.convertAndSend("tanhua-sso-login", msg);
        } catch (MessagingException e) {
            log.error("发送消息失败！", e);
            throw new BusinessException(ResultEnum.ROCKETMQ_SENDMSG_FAIL.getCode(), ResultEnum.ROCKETMQ_SENDMSG_FAIL.getMessage());
        }

        Map<String, Object> result = new HashMap<>(2);
        result.put("token", token);
        result.put("isNew", isNew);

        return result;
    }

    @Override
    public User findByToken(String token) {

        try {
            //1. 解析token
            Claims claims = JwtUtils.decodeToken(token);
            //2. 解析token中的id,同时通过id查询数据
            User user = this.userMapper.selectById(claims.get("id").toString());
            return user;
        } catch (ExpiredJwtException e) {
            System.out.println("token已经过期");
            throw new BusinessException(ResultEnum.TOKEN_EXPIRED.getCode(), ResultEnum.TOKEN_EXPIRED.getMessage());
        } catch (Exception e) {
            System.out.println("token不合法!");
            throw new BusinessException(ResultEnum.TOKEN_ILLEGAL.getCode(), ResultEnum.TOKEN_ILLEGAL.getMessage());
        }
    }

}
