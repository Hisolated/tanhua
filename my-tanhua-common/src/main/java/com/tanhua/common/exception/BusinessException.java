package com.tanhua.common.exception;

import lombok.Data;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/3/19 20:03
 */
@Data
public class BusinessException extends RuntimeException {
    //自定义异常中封装对应的错误编码，用于异常处理时获取对应的操作编码
    private Integer code;

    public BusinessException(Integer code){
        this.code=code;
    }

    public BusinessException(Integer code,String message) {
        super(message);
        this.code=code;
    }

    public BusinessException(Integer code,String message, Throwable cause) {
        super(message, cause);
        this.code=code;
    }

    public BusinessException(Integer code,Throwable cause) {
        super(cause);
        this.code=code;
    }

    protected BusinessException(Integer code,String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code=code;
    }
}
