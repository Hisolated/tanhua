package com.tanhua.sso.config;

import com.arcsoft.face.enums.ErrorInfo;
import com.tanhua.common.enums.ResultEnum;
import com.tanhua.common.exception.BusinessException;
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
            throw new BusinessException(ResultEnum.ACTIVE_FACE_ENGINE_FAIL.getCode(),ResultEnum.ACTIVE_FACE_ENGINE_FAIL.getMessage());
        }

        //初始化引擎
        int initCode = FaceEngineUtils.initEngine();

        if (initCode != ErrorInfo.MOK.getValue()) {
            throw new BusinessException(ResultEnum.INIT_FACE_ENGINE_FAIL.getCode(),ResultEnum.INIT_FACE_ENGINE_FAIL.getMessage());
        }
    }
}
