package com.tanhua.common.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.common.annotations.Cache;
import com.tanhua.common.interceptor.RedisCacheInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.concurrent.TimeUnit;

// Controller Advice
// MyResponseBodyAdvice  这个Bean是一个AOP的bean

@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {

    /**
     * 读取配置文件中的缓存启用开关
      */
    @Value("${tanhua.cache.enable}")
    private Boolean enable;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 我们要对哪些方法的响应进行增强
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 开关处于开启状态  是get请求  包含了@Cache注解
        return enable && returnType.hasMethodAnnotation(GetMapping.class)
                && returnType.hasMethodAnnotation(Cache.class);
    }

    // 真正进行的一个增强
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        if (null == body) {
            return null;
        }

        try {

            String redisValue = null;
            // dody 是一个响应体
            // Redis中只能存储字符串
            if (body instanceof String) {
                redisValue = (String) body;
            } else {
                redisValue = MAPPER.writeValueAsString(body);
            }
            // 生成redisKey
            String redisKey = RedisCacheInterceptor.createRedisKey(((ServletServerHttpRequest) request).getServletRequest());

            // 获取自定义注解Cache
            // cache.time() 获取注解中time的属性值
            Cache cache = returnType.getMethodAnnotation(Cache.class);
            System.out.println("统一缓存已经保存起来了了了了....");
            //缓存的时间单位是秒
            this.redisTemplate.opsForValue().set(redisKey, redisValue, Long.valueOf(cache.time()), TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body;
    }
}
