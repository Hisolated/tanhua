package com.isolate.tanhua.mytanhuasso.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.isolate.tanhua.mytanhuasso.pojo.User;
import com.isolate.tanhua.mytanhuasso.pojo.UserInfo;
import com.isolate.tanhua.mytanhuasso.service.UserService;
import com.isolate.tanhua.mytanhuasso.mapper.UserMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
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
    private RocketMQTemplate rocketMQTemplate;

    @Value("${jwt.secret}")
    private String secret;

    @Override
    public void login(String phone) {
        //todo:作验证码发送操作,同时将phone保存到Redis中
        //1.发送验证码到对应的手机,由于短信暂时用不了,所以需要固定验证码
        String verificationCode = "123456";
        //2.保存phone到Redis中
        String redisKey = "CHECK_CODE_" + phone;
        System.out.println("redisKey:" + redisKey);
        redisTemplate.opsForValue().set(redisKey, verificationCode);
    }

    @Override
    public String loginVerification(String phone, String code) {
        String redisKey = "CHECK_CODE_" + phone;
        boolean isNew = false;

        String redisCode = redisTemplate.opsForValue().get(redisKey);
        System.out.println("redisCode:" +redisCode);

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

        //设置jwt的头
        Map<String, Object> header = new HashMap<String, Object>();
        header.put(JwsHeader.TYPE,JwsHeader.JWT_TYPE);
        header.put(JwsHeader.ALGORITHM,"HS256");

        //设置payload
        Map<String, Object> claims = new HashMap<String, Object>();
        claims.put("id", user.getId());
        claims.put("mobile",user.getMobile());

        String token = Jwts.builder()
                .setHeader(header)
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

    @Override
    public User findByToken(String token) {

        try {
            //1. 解析token
            Map<String,Object> claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("claims = " + claims);
            //2. 解析token中的id,同时通过id查询数据
            User user = userMapper.selectById(claims.get("id").toString());
            System.out.println(user);
            return user;
        } catch (ExpiredJwtException e) {
            //todo:此处先用这种方法展示,后期要使用自定义异常携带信息抛出
            System.out.println("token已经过期");
        } catch (Exception e) {
            log.error("token不合法 = " + token,e);
            System.out.println("token不合法!");
        }
        return null;
    }

    public static void main(String[] args) {

        String token ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxNzYwMjAyNjg2OCIsImlkIjoxLCJleHAiOjE2MTc4MjI3NDh9.rEfwcMWi89M69w2r2ICzPC3HNYl31LYYa0xMJHwy9Ik";
        String  secret = "76bd425b6f29f7fcc2e0bfc286043df1";
        Map<String,Object> claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();

        Object id = claims.get("id");
        System.out.println("id = " + id);
        System.out.println(claims);

    }
}
