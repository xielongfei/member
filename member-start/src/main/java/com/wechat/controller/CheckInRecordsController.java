package com.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.CheckInRecords;
import com.wechat.entity.response.CheckInStat;
import com.wechat.result.Response;
import com.wechat.service.ICheckInRecordsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        checkInRecords.setCheckInDate(LocalDateTime.now());
        //计算连续打卡次数
        checkInRecordsService.save(checkInRecords);
        return Response.success();
    }

    @ApiOperation(value = "打卡记录")
    @GetMapping(value = "/list")
    public Object list(CheckInRecords checkInRecords) {
        LambdaQueryWrapper wrapper = Wrappers.<CheckInRecords>lambdaQuery().eq(CheckInRecords::getMemberId, checkInRecords.getMemberId());
        List<CheckInRecords> list = checkInRecordsService.list(wrapper);
        return Response.success(list);
    }

    @ApiOperation(value = "统计会员打卡次数")
    @GetMapping(value = "/countMemberCheckIn")
    public Object countMemberCheckIn(CheckInRecords checkInRecords) {
        CheckInStat checkInStat = checkInRecordsService.getCheckInStat(checkInRecords.getMemberId());
        return Response.success(checkInStat);
    }
}
