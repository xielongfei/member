package com.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.CheckInRecords;
import com.wechat.entity.request.CheckInRequest;
import com.wechat.entity.response.CheckInStat;
import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import com.wechat.service.ICheckInRecordsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 打卡记录表 前端控制器
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@RestController
@RequestMapping("/checkInRecords")
public class CheckInRecordsController {

    @Autowired
    private ICheckInRecordsService checkInRecordsService;

    @ApiOperation(value = "打卡")
    @PostMapping(value = "/checkIn")
    public Object checkIn(@RequestBody CheckInRecords checkInRecords) {
        //查询今天是否已打卡
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atStartOfDay();
        LocalDateTime endDateTime = today.atTime(23, 59, 59);
        LambdaQueryWrapper wrapper = Wrappers.<CheckInRecords>lambdaQuery()
                .eq(CheckInRecords::getMemberId, checkInRecords.getMemberId())
                .between(CheckInRecords::getCheckInDate, startDateTime, endDateTime);
        CheckInRecords inRecords = checkInRecordsService.getOne(wrapper);
        if (inRecords != null) {
            return Response.failure(ResultCode.CHECKED_IN);
        }

        checkInRecords.setCheckInDate(LocalDateTime.now());
        //计算连续打卡次数
        checkInRecordsService.save(checkInRecords);
        return Response.success();
    }

    @ApiOperation(value = "打卡记录")
    @GetMapping(value = "/list")
    public Object list(CheckInRequest checkInRequest) {
        LambdaQueryWrapper wrapper = Wrappers.<CheckInRecords>lambdaQuery()
                .eq(CheckInRecords::getMemberId, checkInRequest.getMemberId())
                .apply("DATE_FORMAT(check_in_date, '%Y-%m') = {0}", checkInRequest.getCheckInMonth());
        List<CheckInRecords> list = checkInRecordsService.list(wrapper);
        return Response.success(list);
    }

    @ApiOperation(value = "统计会员打卡次数")
    @GetMapping(value = "/countMemberCheckIn")
    public Object countMemberCheckIn(CheckInRecords checkInRecords) {
        CheckInStat checkInStat = checkInRecordsService.getCheckInStat(checkInRecords.getMemberId());
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime = today.atStartOfDay();
        LocalDateTime endDateTime = today.atTime(23, 59, 59);
        LambdaQueryWrapper wrapper = Wrappers.<CheckInRecords>lambdaQuery()
                .eq(CheckInRecords::getMemberId, checkInRecords.getMemberId())
                .between(CheckInRecords::getCheckInDate, startDateTime, endDateTime);
        CheckInRecords inRecords = checkInRecordsService.getOne(wrapper);
        if (inRecords != null) {
            checkInStat.setCheckInStatus(1);
            checkInStat.setCheckInDate(inRecords.getCheckInDate());
        } else {
            checkInStat.setCheckInStatus(2);
        }
        return Response.success(checkInStat);
    }
}
