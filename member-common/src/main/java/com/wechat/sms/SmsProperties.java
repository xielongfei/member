package com.wechat.sms;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xielongfei
 * @date: 2023/07/08
 * @description:
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tencent.sms")
public class SmsProperties {

    private String secretId;
    private String secretKey ;
    private String signName ;
    private String sdkAppId;
    private String templateId;
}
