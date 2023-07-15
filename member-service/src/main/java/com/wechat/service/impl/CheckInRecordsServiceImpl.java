package com.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.CheckInRecords;
import com.wechat.entity.response.CheckInStat;
import com.wechat.mapper.CheckInRecordsMapper;
import com.wechat.service.ICheckInRecordsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * <p>
 * 打卡记录表 服务实现类
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@Service
public class CheckInRecordsServiceImpl extends ServiceImpl<CheckInRecordsMapper, CheckInRecords> implements ICheckInRecordsService {

    @Override
    public CheckInStat countMemberCheckIn(CheckInRecords checkInRecords) {
        CheckInStat checkInStat = this.getCheckInStat(checkInRecords.getMemberId());
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atStartOfDay();
        LocalDateTime endDateTime = today.atTime(23, 59, 59);
        LambdaQueryWrapper wrapper = Wrappers.<CheckInRecords>lambdaQuery()
                .eq(CheckInRecords::getMemberId, checkInRecords.getMemberId())
                .between(CheckInRecords::getCheckInDate, startDateTime, endDateTime);
        CheckInRecords inRecords = super.getOne(wrapper);
        if (inRecords != null) {
            checkInStat.setCheckInStatus(1);
            checkInStat.setCheckInDate(inRecords.getCheckInDate());
        } else {
            checkInStat.setCheckInStatus(2);
        }
        return checkInStat;
    }

    @Override
    public CheckInStat getCheckInStat(Integer memberId) {
        LambdaQueryWrapper wrapper = Wrappers.<CheckInRecords>lambdaQuery()
                .eq(CheckInRecords::getMemberId, memberId)
                .orderByDesc(CheckInRecords::getCheckInDate);
        List<CheckInRecords> checkInRecords = super.list(wrapper);

        LocalDate currentDate = LocalDate.now();
        int consecutiveCheckIns = 0;
        int consecutiveMissedCheckIns = 0;
        int totalCheckIns = checkInRecords.size();

        for (CheckInRecords record : checkInRecords) {
            LocalDate checkInDate = record.getCheckInDate().toLocalDate();

            if (checkInDate.equals(currentDate)) {
                consecutiveCheckIns++;
            } else if (consecutiveCheckIns == 0) {
                long daysBetween = ChronoUnit.DAYS.between(checkInDate, currentDate);
                consecutiveMissedCheckIns = (int) daysBetween;
                break;
            } else {
                break;
            }
              currentDate = currentDate.minusDays(1);

        }
        return CheckInStat.builder().consecutiveCheckIns(consecutiveCheckIns)
                .consecutiveMissedCheckIns(consecutiveMissedCheckIns)
                .totalCheckIns(totalCheckIns).build();
    }
}
