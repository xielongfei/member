package com.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wechat.entity.CheckInRecords;
import com.wechat.entity.response.CheckInStat;

/**
 * <p>
 * 打卡记录表 服务类
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
public interface ICheckInRecordsService extends IService<CheckInRecords> {

    CheckInStat getCheckInStat(Integer memberId);
}
