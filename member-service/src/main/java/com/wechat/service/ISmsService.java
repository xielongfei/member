package com.wechat.service;

/**
 * @author: xielongfei
 * @date: 2023/07/08
 * @description:
 */
public interface ISmsService {

    /**
     * 发送验证码
     * @param phoneNumber
     * @return
     */
    boolean send(String phoneNumber);
}
