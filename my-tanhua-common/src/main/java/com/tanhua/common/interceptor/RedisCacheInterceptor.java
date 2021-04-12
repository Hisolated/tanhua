package com.tanhua.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanhua.common.annotations.Cache;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RedisCacheInterceptor implements HandlerInterceptor {

    @Value("${tanhua.cache.enable}")
    private Boolean enable;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /* 在请求到达目标资源之前干的事  */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //缓存的全局开关的校验
        if (!enable) {
            return true;
        }

        // 校验handler是否是HandlerMethod
        // 判断请求是不是控制器中的一个方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 判断是否为get请求
        if (!((HandlerMethod) handler).hasMethodAnnotation(GetMapping.class)) {
            return true;
        }

        // 判断是否添加了@Cache注解
        if (!((HandlerMethod) handler).hasMethodAnnotation(Cache.class)) {
            return true;
        }

        // 缓存命中
        String redisKey = createRedisKey(request);
        // 从Redis中取值
        String cacheData = this.redisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isEmpty(cacheData)) {
            //缓存未命中
            System.out.println("统一缓存未命中....");
            return true;
        }

        // 将data数据进行响应
        System.out.println("统一缓存命中了了了了....");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(cacheData);

        return false;
    }

    /*@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在响应到达客户端之前  执行
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 在响应到达客户端之后  执行
    }*/

    /**
     * 生成redis中的key，规则：SERVER_CACHE_DATA_MD5(url + param + token)
     *
     * @param request
     * @return
     */
    public static String createRedisKey(HttpServletRequest request) throws Exception {
        // 获取请求地址
        String url = request.getRequestURI();
        // 获取请求参数
        String param = MAPPER.writeValueAsString(request.getParameterMap());
        // 获取请求头中的Token
        String token = request.getHeader("Authorization");
        // 唯一确定一个请求
        String data = url + "_" + param + "_" + token;
        // 定义Redis Key
        return "SERVER_CACHE_DATA_" + DigestUtils.md5Hex(data);
    }
}
