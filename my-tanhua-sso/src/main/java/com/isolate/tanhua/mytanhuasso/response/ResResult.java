package com.isolate.tanhua.mytanhuasso.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * @description: 返回结果类
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/8 10:15
 */
@Getter
@Setter
@NoArgsConstructor
public class ResResult<T> implements Serializable {
    private String resMsg;

    private int resCode;

    private T resData;

    private ResResult(int resCode, T resData, String resMsg) {
        this.resCode = resCode;
        this.resData = resData;
        this.resMsg = resMsg;
    }

    public static <R> ResResult<R> ok(R object) {
        return new ResResult<R>(20000, object, "success!");
    }

    public static <R> ResResult<R> error(int code, String message) {
        return new ResResult<R>(code, null, message);
    }

    public static <R> ResResult<R> error(String message) {
        return new ResResult<R>(1, null, message);
    }

    public static <R> ResResult<R> normal(int code, R object, String message) {
        return new ResResult<R>(code, object, message);
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + resMsg + '\'' +
                ", code=" + resCode +
                ", data=" + resData +
                '}';
    }

    public static ResResult errorParam(String argName) {
        return error(3, "错误的" + argName + "参数");
    }

    public static ResResult errorParams(String argName) {
        return error(4, argName);
    }
}
