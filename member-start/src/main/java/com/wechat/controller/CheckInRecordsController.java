package com.wechat.controller;

import com.wechat.entity.CheckInRecords;
import com.wechat.entity.response.CheckInStat;
import com.wechat.service.ICheckInRecordsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return checkInRecordsService.save(checkInRecords);
    }

    @ApiOperation(value = "统计会员打卡次数")
    @GetMapping(value = "/countMemberCheckIn")
    public Object countMemberCheckIn(CheckInRecords checkInRecords) {
        CheckInStat checkInStat = checkInRecordsService.getCheckInStat(checkInRecords.getMemberId());
        return checkInStat;
    }
}
