package com.wechat.entity.request;

import com.wechat.entity.CheckInRecords;
import lombok.Data;

/**
 * @author: xielongfei
 * @date: 2023/07/12
 * @description:
 */
@Data
public class CheckInRequest extends CheckInRecords {

    /**
     * 打卡月份搜索 %Y-%m
     *
     */
    private String checkInMonth;

    /**
     * 打卡日期搜索 %Y-%m-%d
     */
    private String searchCheckInDate;
}
