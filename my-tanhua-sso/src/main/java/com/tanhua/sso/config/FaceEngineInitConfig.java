package com.tanhua.sso.config;

import com.arcsoft.face.enums.ErrorInfo;
import com.tanhua.sso.utils.FaceEngineUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @description: some desc
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/9 10:19
 */
@Component
public class FaceEngineInitConfig {

    @PostConstruct
    public static void init() {
        // 激活引擎
        Integer activeCode = FaceEngineUtils.activeEngine();
        if (activeCode != ErrorInfo.MOK.getValue() && activeCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            //todo:抛出自定义异常;
            throw new RuntimeException("引擎激活失败");
        }

        //初始化引擎
        int initCode = FaceEngineUtils.initEngine();

        if (initCode != ErrorInfo.MOK.getValue()) {
            //todo:抛出自定义异常
            throw new RuntimeException("初始化引擎出错!");
        }
    }
}
