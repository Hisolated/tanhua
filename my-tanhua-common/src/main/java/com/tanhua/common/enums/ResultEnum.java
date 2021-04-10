package com.tanhua.common.enums;

import lombok.Getter;

/**
 * @description: 系统异常代码
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/10 19:41
 */
@Getter
public enum  ResultEnum {

    SUCCESS(00000, "success"),
    TOKEN_EXPIRED(10001,"token已经过期"),
    TOKEN_ILLEGAL(10002,"token不合法"),
    ROCKETMQ_SENDMSG_FAIL(10003,"rocketMQ发送消息失败"),
    VERIFY_CODE_FAIL(10004,"验证码错误"),
    CREATE_TOKEN_FAIL(10005,"token生成失败"),
    NOT_NETWORK(1, "系统繁忙，请稍后再试。"),
    LOGIN_VERIFY_FALL(2, "登录失效"),
    PARAM_VERIFY_FALL(3, "参数验证错误"),
    AUTH_FAILED(4, "权限验证失败"),
    DATA_NOT(5, "没有相关数据"),
    DATA_CHANGE(6, "数据没有任何更改"),
    DATA_REPEAT(7, "数据已存在");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}