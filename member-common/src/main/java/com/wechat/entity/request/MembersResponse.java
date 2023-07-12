package com.wechat.entity.request;

import com.wechat.entity.Members;
import lombok.Data;
import org.springframework.core.io.Resource;

/**
 * @author: xielongfei
 * @date: 2023/07/08
 * @description:
 */
@Data
public class MembersResponse extends Members {

    /**
     * 打卡状态  1: 已打卡, 2: 未打卡
     */
    private Integer checkInStatus;
}
