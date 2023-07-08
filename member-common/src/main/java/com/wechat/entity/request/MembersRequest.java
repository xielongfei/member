package com.wechat.entity.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author: xielongfei
 * @date: 2023/07/08
 * @description:
 */
@Data
public class MembersRequest {

    /**
     * 商铺编号A
     */
    private String shopIdA;

    /**
     * 商铺编号B
     */
    private String shopIdB;
}
