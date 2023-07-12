package com.wechat.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

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

    /**
     * 打卡日期
     */
    private LocalDateTime checkInDate;

    /**
     * 打卡状态  1: 已打卡, 2: 未打卡
     */
    private Integer checkInStatus;
}
