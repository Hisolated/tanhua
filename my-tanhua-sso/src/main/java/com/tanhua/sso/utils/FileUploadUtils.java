package com.tanhua.sso.utils;

import com.aliyun.oss.OSSClient;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @description: 文件处理
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/8 12:55
 */
public class FileUploadUtils {

    /**
     * 阿里云存储服务(OSS)参数信息
     */
    private static final String ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String ACCESS_KEY_ID = "LTAI5t5tZnoi49wem81TYkWz";
    private static final String ACCESS_KEY_SECRET = "xQOVUXbjnWoxAFxRfIuFpMya2TDMxb";
    private static final String BUCKET_NAME = "isolate-h";
    private static final String URL_PREFIX = "https://isolate-h.oss-cn-hangzhou.aliyuncs.com/";

    private static OSSClient ossClient;

    static {
        ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }


    /**
     *
     * @param uploadFile
     * @return 返回上传后的文件路径
     * @throws IOException
     */
    public static String upload(MultipartFile uploadFile) throws IOException {

        // 文件新路径
        String fileName = uploadFile.getOriginalFilename();
        String filePath = getFilePath(fileName);
        byte[] fileBytes = uploadFile.getBytes();

        // 目录结构：images/2018/12/29/xxxx.jpg,上传阿里云存储服务器
        ossClient.putObject(BUCKET_NAME, filePath, new ByteArrayInputStream(fileBytes));

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

    public static void main(String[] args) {

    }
}
