package com.wechat.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @date: 2023/07/13
 * @description:
 */
@Component
public class ConstantsUtil {

    public static String serviceUrl;

    @Value("${service.url}")
    public void setServiceUrl(String serviceUrl) {
        ConstantsUtil.serviceUrl = serviceUrl;
    }
}
