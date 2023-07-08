package com.wechat.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: xielongfei
 * @date: 2023/07/08
 * @description:
 */
@Data
@AllArgsConstructor
public class CheckInStat {

    /**
     * 连续打卡次数
     */
    private int consecutiveCheckIns;

    /**
     * 连续未打卡次数
     */
    private int consecutiveMissedCheckIns;

    /**
     * 累计打卡天数
     */
    private int totalCheckIns;
}
