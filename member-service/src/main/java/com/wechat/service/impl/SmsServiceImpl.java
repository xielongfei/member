package com.wechat.service.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import com.wechat.entity.BasicInfo;
import com.wechat.entity.Members;
import com.wechat.service.IBasicInfoService;
import com.wechat.service.ISmsService;
import com.wechat.sms.CacheUtil;
import com.wechat.sms.SmsProperties;
import com.wechat.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

/**
 * @date: 2023/07/08
 * @description:
 */
@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    //验证码模板
    public static final String verificationCodeTemplateId = "1858174";
    //会员警告模板
    public static final String memberWarnTemplateId = "1873321";
    //管理员警告模板
    public static final String superWarnTemplateId = "1865610";

    @Autowired
    SmsProperties smsProperties;

    @Autowired
    private IBasicInfoService basicInfoService;

    @Override
    public boolean sendVerificationCode(String phone) {
        //生成6位数随机验证码
        String verificationCode = String.format("%06d", new Random().nextInt(1000000));
        String[] templateParamSet = {verificationCode};
        boolean bool = this.send(phone, verificationCodeTemplateId, templateParamSet);
        if (bool) {
            CacheUtil.cache.put(phone, verificationCode);
            //将验证码放入redis中
//            redisService.setCacheObject(VERIFICATION_CODE + phoneNumber, verificationCode, 60*5L, TimeUnit.SECONDS);
        }
        return bool;
    }

    @Override
    public boolean sendMemberWarnMsg(Members members) {
        String customer_phone = "未填";
        List<BasicInfo> list = basicInfoService.list();
        if (!CollectionUtils.isEmpty(list)) {
            BasicInfo basicInfo = list.get(0);
            customer_phone = basicInfo.getCustomerPhone();
        }

        LocalDate warnDate = members.getWarnDate();
        String range = DateUtil.getMMDD(warnDate) + "~" + DateUtil.getMMDD(LocalDate.now());
        long daysDiff = ChronoUnit.DAYS.between(members.getWarnDate(), LocalDate.now());
        String[] templateParamSet = {members.getShopId(), range, String.valueOf(daysDiff), customer_phone};
        boolean bool = this.send(members.getPhone(), memberWarnTemplateId, templateParamSet);
        return bool;
    }

    @Override
    public boolean sendSuperWarnMsg(String phone, Members members) {
        LocalDate warnDate = members.getWarnDate();
        String range = DateUtil.getMMDD(warnDate) + "~" + DateUtil.getMMDD(LocalDate.now());
        long daysDiff = ChronoUnit.DAYS.between(members.getWarnDate(), LocalDate.now());
        String[] templateParamSet = {members.getShopId(), range, String.valueOf(daysDiff)};
        boolean bool = this.send(phone, superWarnTemplateId, templateParamSet);
        return bool;
    }

    /**
     * 发送短信
     *
     * @param phone
     * @param templateId
     * @param templateParamSet
     * @return
     */
    public boolean send(String phone, String templateId, String[] templateParamSet) {
        //判断手机号是否为空
        if (!StringUtils.hasLength(phone)) {
            return false;
        }
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(smsProperties.getSecretId(), smsProperties.getSecretKey());
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的  第二个参数是地域信息
            SmsClient client = new SmsClient(cred, "ap-guangzhou", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            //设置固定的参数
            req.setSmsSdkAppId(smsProperties.getSdkAppId());// 短信应用ID: 短信SdkAppId在 [短信控制台] 添加应用后生成的实际SdkAppId
            req.setSignName(smsProperties.getSignName());//短信签名内容: 使用 UTF-8 编码，必须填写已审核通过的签名
            req.setTemplateId(templateId);//模板 ID: 必须填写已审核通过的模板 ID
            /* 用户的 session 内容: 可以携带用户侧 ID 等上下文信息，server 会原样返回 */
//            String sessionContext = "xxx";
//            req.setSessionContext(sessionContext);

            //设置发送相关的参数
            String[] phoneNumberSet1 = {"+86" + phone};
            req.setPhoneNumberSet(phoneNumberSet1);//发送的手机号

            req.setTemplateParamSet(templateParamSet);//模板参数
            //发送短信
            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            log.info("验证码：{}，响应体：{}", templateParamSet, SendSmsResponse.toJsonString(resp));
            return true;
        } catch (TencentCloudSDKException e) {
            e.printStackTrace();
            return false;
        }
    }

}
