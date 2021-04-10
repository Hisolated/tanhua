package com.tanhua.common.annotations;

import java.lang.annotation.*;

/**
 * 被标记为Cache的Controller进行缓存，其他情况不进行缓存
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented //标记注解
public @interface Cache {

    /**
     * 缓存时间，默认为60秒
     * @return
     */
    String time() default "60";
}