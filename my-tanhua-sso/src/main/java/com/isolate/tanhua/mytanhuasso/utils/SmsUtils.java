package com.isolate.tanhua.mytanhuasso.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;

/**
 * @description: 短信工具类(使用的是阿里的短信服务)
 * @author: isolate
 * @email: 15071340963@163.com
 * @date: 2021/4/8 9:32
 */
public class SmsUtils {


    /**
     * todo:由于阿里短信验证个人申请很难通过,
     *      此处信息都是copy过来的,为测试数据,不一定有用.后期申请成功需修改为自己的
     */
    private static final String REGION_ID = "cn-hangzhou";
    private static final String ACCESS_KEY_ID = "LTAI4G6iTZ6KQrXN1uXg7fy9";
    private static final String ACCESS_KEY_SECRET = "O88aUovKCVm0GLzTAjhiG6U8figN7c";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String SIGN_NAME = "ABC商城";
    private static final String TEMPLATE_CODE = "SMS_204756062";

    /**
     * todo:此处信息暂时不知道有没有用,配置文件写的,先留着
     */
//    private static String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
//    private static String accessKeyId2 = "LTAI4G3iwLAPWLAxD3hJ9Tv8";
//    private static String accessKeySecret2 = "bX8gvHGQJsPGtfXXY7DGI3zC61TFz1 ";
//    private static String bucketName= "hztanhua";
//    private static String urlPrefix="https://hztanhua.oss-cn-hangzhou.aliyuncs.com/";

    /**使用阿里短信服务发送短信验证码(主代码来源阿里官网)
     *
     * @param mobile 手机号
     * @param code  验证码
     * @param smsTemplateCode 短信模板信息编号,为阿里官网设置模板后会提供
     * @return
     */
    public static CommonResponse sendSms(String mobile,String code,String smsTemplateCode){
        DefaultProfile profile = DefaultProfile.getProfile(REGION_ID,ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(DOMAIN);
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", REGION_ID);
        request.putQueryParameter("PhoneNumbers", mobile); //目标手机号
        request.putQueryParameter("SignName", SIGN_NAME); //签名名称
        request.putQueryParameter("TemplateCode", smsTemplateCode); //短信模板
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");//模板中变量替换
        try {
            CommonResponse response = client.getCommonResponse(request);
            String data = response.getData();
            if (StringUtils.contains(data, "\"Message\":\"OK\"")) {
                System.out.println("发送短信验证码成功~ data = " + data);
                return response;
            }
            System.out.println("发送短信验证码失败~ data = " + data);
        } catch (Exception e) {
            System.out.println("发送短信验证码失败~ mobile = " + mobile+ ",错误信息: " + e.getMessage());
        }
        return null;
    }

    /**
     * 使用默认短信模板进行发送验证码
     * @param mobile 手机号
     * @param code  验证码
     * @return
     */
    public static CommonResponse sendSms(String mobile,String code){
            return sendSms(mobile,code,TEMPLATE_CODE);
    }

}
