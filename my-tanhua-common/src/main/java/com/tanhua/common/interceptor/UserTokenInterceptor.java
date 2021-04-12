package com.tanhua.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.tanhua.common.annotations.NoAuthorization;
import com.tanhua.common.pojo.User;
import com.tanhua.common.service.UserService;
import com.tanhua.common.utils.UserThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Token的统一处理
 * 需要对哪些请求的Token进行处理
 * 怎么解决 ?
 * 写一个自定义注解  哪个不需要进行Token的认证 我们就在哪个请求方法上添加注解就可以了
 * 点明: 自己总结 : 自定义注解咋用
 * 做项目 目光 不能仅仅盯着  视频中讲的需求
 */
@Component
public class UserTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    /**
     * 在请求到达目标资源之前会执行的方法
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 校验handler是否是HandlerMethod
        // 当前正在请求执行的是不是一个Controller中方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 判断是否包含@NoAuthorization注解，如果包含，直接放行
        if (((HandlerMethod) handler).hasMethodAnnotation(NoAuthorization.class)) {
            return true;
        }

        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotEmpty(token)) {
            // 调用SSO进行Token的认证
            User user = this.userService.queryUserByToken(token);
            if (user != null) {
                //token有效
                // 将User对象绑定到ThreadLocal中
                UserThreadLocal.set(user);
                return true;
            }
        }

        // token无效，响应状态为401
        // 401 无权限
        response.setStatus(401); //无权限
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //从ThreadLocal中移除User对象
        UserThreadLocal.remove();
    }
}
