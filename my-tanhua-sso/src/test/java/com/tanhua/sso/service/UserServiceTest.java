package com.tanhua.sso.service;

import com.tanhua.sso.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/7 14:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private UserService userService;

    @Test
    public void testGetRedis(){
        String phone = redisTemplate.boundValueOps("CHECK_CODE_17602026868").get();
        System.out.println("phone = " + phone);
    }

    @Test
    public void testFindByToken(){
        User user = userService.findByToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiIxNzYwMjAyNjg2OCIsImlkIjoxLCJleHAiOjE2MTc4MjI3NDh9.rEfwcMWi89M69w2r2ICzPC3HNYl31LYYa0xMJHwy9Ik");
        System.out.println(user);
    }
}
