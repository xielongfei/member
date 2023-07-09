package com.wechat.sms;

import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @author: xielongfei
 * @date: 2023/07/09
 * @description:
 */
public class CacheUtil {

    // 创建一个Cache实例
    public static final com.google.common.cache.Cache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES) // 设置过期时间为5分钟
            .build();
}
