package com.isolate.tanhua.mytanhuasso.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.isolate.tanhua.mytanhuasso.vo.PicUploadResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

/**
 * @description: 文件处理
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/8 12:55
 */
public class FileUtils {

    /**
     * 阿里云存储服务(OSS)参数信息
     */
    private static final String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI5t5tZnoi49wem81TYkWz";
    private static final String ACCESS_KEY_SECRET = "xQOVUXbjnWoxAFxRfIuFpMya2TDMxb";
    private static final String BUCKET_NAME = "isolate-h";
    private static final String URL_PREFIX ="https://isolate-h.oss-cn-hangzhou.aliyuncs.com/";



    // 允许上传图片文件的格式
    private static final String[] IMAGE_TYPE = new String[]{
            ".bmp", ".jpg",".jpeg", ".gif", ".png"
    };

    private static final OSSClient ossClient =new OSSClient(ENDPOINT,ACCESS_KEY_ID,ACCESS_KEY_SECRET);


    public static String upload(MultipartFile uploadFile) {

        //图片做校验，对后缀名
        boolean isLegal = false;

        // 校验文件后缀,不符合IMAGE_TYPE后缀的直接不上传
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(uploadFile.getOriginalFilename(),
                    type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
            //todo:抛出自定义异常
            System.out.println("文件格式不匹配!");
            return null;
        }

        // 文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);

        try {
            // 目录结构：images/2018/12/29/xxxx.jpg
            ossClient.putObject(BUCKET_NAME, filePath, new ByteArrayInputStream(uploadFile.getBytes()));
        } catch (Exception e) {
            //todo:抛出自定义异常
            System.out.println("文件上传失败!");
            return null;
        }

        return URL_PREFIX + filePath;
    }

    private static String getFilePath(String sourceFileName) {
        DateTime dateTime = new DateTime();
        //images/yyyy/MM/dd/xxxxxxx.jpg
        return "images/" + dateTime.toString("yyyy")
                + "/" + dateTime.toString("MM") + "/"
                + dateTime.toString("dd") + "/" + System.currentTimeMillis() +
                RandomUtils.nextInt(100, 9999) + "." +
                StringUtils.substringAfterLast(sourceFileName, ".");
    }
}
