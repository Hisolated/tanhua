package com.isolate.tanhua.mytanhuasso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.isolate.tanhua.mytanhuasso.pojo.User;
import com.isolate.tanhua.mytanhuasso.service.UserService;
import com.isolate.tanhua.mytanhuasso.mapper.UserMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
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
    private RedisTemplate rocketMQTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public void login(String phone) {
        //todo:作验证码发送操作,同时将phone保存到Redis中
        //1.发送验证码到对应的手机,由于短信暂时用不了,所以需要固定验证码
        String verificationCode = "123456";
        //2.保存phone到Redis中
        String redisKey = "CHECK_CODE_" + phone;
        redisTemplate.opsForValue().set(redisKey, verificationCode);
    }

    @Override
    public String loginVerification(String phone, String code) {
        String redisKey = "CHECK_CODE_" + phone;
        boolean isNew = false;

        String redisCode = redisTemplate.opsForValue().get(redisKey);

        if (!StringUtils.equals(code, redisCode)) {
            return null; //验证码错误
        }
        //验证码正确

        //从数据库中查询,看是否存在该用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mobile", phone);

        User user = this.userMapper.selectOne(queryWrapper);
        if (user == null) {
            //mysql中不存在该用户,将该用户保存到数据库中,暂时不保存密码
            //需要注册该用户
            user = new User();
            user.setMobile(phone);
            user.setPassword(DigestUtils.md5Hex("123456"));
            userMapper.insert(user);

            isNew = true;
        }


        //生成token
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());

        // 生成token
        String token = Jwts.builder()
                .setClaims(claims) //payload，存放数据的位置，不能放置敏感数据，如：密码等
                .signWith(SignatureAlgorithm.HS256, secret) //设置加密方法和加密盐
                .setExpiration(new DateTime().plusHours(12).toDate()) //设置过期时间，12小时后过期
                .compact();

        try {
            //发送用户登录成功的消息
            Map<String, Object> msg = new HashMap<>();
            msg.put("id", user.getId());
            msg.put("date", System.currentTimeMillis());

            this.rocketMQTemplate.convertAndSend("tanhua-sso-login", msg);
        } catch (MessagingException e) {
            log.error("发送消息失败！", e);
        }

        return token + "|" + isNew;
    }
}
