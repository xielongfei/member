package com.wechat.service;

import com.wechat.entity.Members;

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
    boolean sendVerificationCode(String phoneNumber);

    /**
     * 给会员发送告警短信
     * @param members
     * @return
     */
    boolean sendMemberWarnMsg(Members members);

    /**
     * 给管理员发送告警短信
     * @param members
     * @return
     */
    boolean sendSuperWarnMsg(String phone, Members members);

}
