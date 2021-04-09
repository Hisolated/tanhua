package com.isolate.tanhua.mytanhuasso.config;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.enums.ErrorInfo;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.isolate.tanhua.mytanhuasso.utils.FaceEngineUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
