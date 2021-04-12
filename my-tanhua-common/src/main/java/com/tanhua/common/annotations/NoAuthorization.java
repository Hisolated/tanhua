package com.tanhua.common.annotations;

import java.lang.annotation.*;

/**
 * 用于判断是否需要token认证
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented //标记注解
public @interface NoAuthorization {

}