package com.wechat.entity.request;

import com.wechat.entity.Members;
import lombok.Data;

/**
 * @author: xielongfei
 * @date: 2023/07/08
 * @description:
 */
@Data
public class MembersRequest extends Members {

    /**
     * 商铺编号A
     */
    private String shopIdA;

    /**
     * 商铺编号B
     */
    private String shopIdB;

    /**
     * 验证码
     */
    private String verificationCode;
}
