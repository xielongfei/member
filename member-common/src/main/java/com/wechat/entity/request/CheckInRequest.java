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
     * 打卡月份搜索
     */
    private String checkInMonth;
}
