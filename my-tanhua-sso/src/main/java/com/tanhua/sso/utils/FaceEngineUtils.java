package com.tanhua.sso.utils;

import com.arcsoft.face.*;
import com.arcsoft.face.enums.*;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.toolkit.ImageFactory.getGrayData;
import static com.arcsoft.face.toolkit.ImageFactory.getRGBData;


public class FaceEngineUtils {

    private static final String APP_ID = "9FTXektuEMQkqGYA9JyLcM3TW9hkj9e3XC6Xu6xA8voN";
    private static final String SDK_KEY = "A2Cxq6rCbpZL93dnAw2TkCxutptgQArFqR79yYUsVoaa";

    // 虹软SDK中的WIN64文件存放的位置的绝对路径
    private static final String LIB_PATH = "F:\\lib\\WIN64";


    private static FaceEngine faceEngine;


    //获取人脸识别操作对象
   static{
        faceEngine = new FaceEngine(LIB_PATH);
    }
    //激活引擎
    public static Integer activeEngine() {
        //激活引擎
        return faceEngine.activeOnline(APP_ID, SDK_KEY);
    }

    //初始化引擎
    public static Integer initEngine() {
        //引擎配置
        EngineConfiguration engineConfiguration = new EngineConfiguration();
        //IMAGE检测模式，用于处理单张的图像数据
        engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
        //人脸检测角度，全角度
        engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_ALL_OUT);

        //功能配置
        FunctionConfiguration functionConfiguration = new FunctionConfiguration();
        functionConfiguration.setSupportAge(true);
        functionConfiguration.setSupportFace3dAngle(true);
        functionConfiguration.setSupportFaceDetect(true);
        functionConfiguration.setSupportFaceRecognition(true);
        functionConfiguration.setSupportGender(true);
        functionConfiguration.setSupportLiveness(true);
        functionConfiguration.setSupportIRLiveness(true);
        engineConfiguration.setFunctionConfiguration(functionConfiguration);

        //初始化引擎
        return faceEngine.init(engineConfiguration);
    }

    /**
     * 检测图片是否为人像
     *
     * @param imageInfo 图像对象
     * @return true:人像，false:非人像
     */
    public static boolean checkIsPortrait(ImageInfo imageInfo) {
        // 定义人脸列表
        List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
        faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
        return !faceInfoList.isEmpty();
    }

    public static boolean checkIsPortrait(byte[] imageData) {
        return checkIsPortrait(ImageFactory.getRGBData(imageData));
    }

    public static boolean checkIsPortrait(File file) {
        return checkIsPortrait(ImageFactory.getRGBData(file));
    }

    public static void main(String[] args) {

        //激活引擎
        int errorCode = activeEngine();

        if (errorCode != ErrorInfo.MOK.getValue() && errorCode != ErrorInfo.MERR_ASF_ALREADY_ACTIVATED.getValue()) {
            System.out.println("引擎激活失败");
        }

        //初始化引擎
        errorCode = initEngine();

        if (errorCode != ErrorInfo.MOK.getValue()) {
            System.out.println("初始化引擎失败");
        }


        //人脸检测
        Boolean result = checkIsPortrait(new File("C:\\Users\\强\\Desktop\\图片\\QQ图片20170327235103.jpg"));
        System.out.println(result);
//
//        //引擎卸载
//        errorCode = faceEngine.unInit();
    }



//其他功能,暂时用不到,以后可挑选使用
//    //特征提取
//    FaceFeature faceFeature = new FaceFeature();
//    errorCode = faceEngine.extractFaceFeature(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList.get(0), faceFeature);
//        System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
//
//    //人脸检测2
//    ImageInfo imageInfo2 = getRGBData(new File("d:\\ccc.jpg"));
//    List<FaceInfo> faceInfoList2 = new ArrayList<FaceInfo>();
//    errorCode = faceEngine.detectFaces(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(), imageInfo.getImageFormat(), faceInfoList2);
//        System.out.println(faceInfoList);
//
//    //特征提取2
//    FaceFeature faceFeature2 = new FaceFeature();
//    errorCode = faceEngine.extractFaceFeature(imageInfo2.getImageData(), imageInfo2.getWidth(), imageInfo2.getHeight(), imageInfo.getImageFormat(), faceInfoList2.get(0), faceFeature2);
//        System.out.println("特征值大小：" + faceFeature.getFeatureData().length);
//
//    //特征比对
//    FaceFeature targetFaceFeature = new FaceFeature();
//        targetFaceFeature.setFeatureData(faceFeature.getFeatureData());
//    FaceFeature sourceFaceFeature = new FaceFeature();
//        sourceFaceFeature.setFeatureData(faceFeature2.getFeatureData());
//    FaceSimilar faceSimilar = new FaceSimilar();
//
//    errorCode = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
//
//        System.out.println("相似度：" + faceSimilar.getScore());
//
//    //设置活体测试
//    errorCode = faceEngine.setLivenessParam(0.5f, 0.7f);
//    //人脸属性检测
//    FunctionConfiguration configuration = new FunctionConfiguration();
//        configuration.setSupportAge(true);
//        configuration.setSupportFace3dAngle(true);
//        configuration.setSupportGender(true);
//        configuration.setSupportLiveness(true);
//    errorCode = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList, configuration);
//
//
//    //性别检测
//    List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
//    errorCode = faceEngine.getGender(genderInfoList);
//        System.out.println("性别：" + genderInfoList.get(0).getGender());
//
//    //年龄检测
//    List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
//    errorCode = faceEngine.getAge(ageInfoList);
//        System.out.println("年龄：" + ageInfoList.get(0).getAge());
//
//    //3D信息检测
//    List<Face3DAngle> face3DAngleList = new ArrayList<Face3DAngle>();
//    errorCode = faceEngine.getFace3DAngle(face3DAngleList);
//        System.out.println("3D角度：" + face3DAngleList.get(0).getPitch() + "," + face3DAngleList.get(0).getRoll() + "," + face3DAngleList.get(0).getYaw());
//
//    //活体检测
//    List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
//    errorCode = faceEngine.getLiveness(livenessInfoList);
//        System.out.println("活体：" + livenessInfoList.get(0).getLiveness());
//
//
//    //IR属性处理
//    ImageInfo imageInfoGray = getGrayData(new File("d:\\IR_480p.jpg"));
//    List<FaceInfo> faceInfoListGray = new ArrayList<FaceInfo>();
//    errorCode = faceEngine.detectFaces(imageInfoGray.getImageData(), imageInfoGray.getWidth(), imageInfoGray.getHeight(), imageInfoGray.getImageFormat(), faceInfoListGray);
//
//    FunctionConfiguration configuration2 = new FunctionConfiguration();
//        configuration2.setSupportIRLiveness(true);
//    errorCode = faceEngine.processIr(imageInfoGray.getImageData(), imageInfoGray.getWidth(), imageInfoGray.getHeight(), imageInfoGray.getImageFormat(), faceInfoListGray, configuration2);
//    //IR活体检测
//    List<IrLivenessInfo> irLivenessInfo = new ArrayList<>();
//    errorCode = faceEngine.getLivenessIr(irLivenessInfo);
//        System.out.println("IR活体：" + irLivenessInfo.get(0).getLiveness());
//
//    ImageInfoEx imageInfoEx = new ImageInfoEx();
//        imageInfoEx.setHeight(imageInfo.getHeight());
//        imageInfoEx.setWidth(imageInfo.getWidth());
//        imageInfoEx.setImageFormat(imageInfo.getImageFormat());
//        imageInfoEx.setImageDataPlanes(new byte[][]{imageInfo.getImageData()});
//        imageInfoEx.setImageStrides(new int[]{imageInfo.getWidth() * 3});
//    List<FaceInfo> faceInfoList1 = new ArrayList<>();
//    errorCode = faceEngine.detectFaces(imageInfoEx, DetectModel.ASF_DETECT_MODEL_RGB, faceInfoList1);
//
//    FunctionConfiguration fun = new FunctionConfiguration();
//        fun.setSupportAge(true);
//    errorCode = faceEngine.process(imageInfoEx, faceInfoList1, functionConfiguration);
//    List<AgeInfo> ageInfoList1 = new ArrayList<>();
//    int age = faceEngine.getAge(ageInfoList1);
//        System.out.println("年龄：" + ageInfoList1.get(0).getAge());
//
//    FaceFeature feature = new FaceFeature();
//    errorCode = faceEngine.extractFaceFeature(imageInfoEx, faceInfoList1.get(0), feature);


}