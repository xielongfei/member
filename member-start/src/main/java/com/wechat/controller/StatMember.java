package com.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.BasicInfo;
import com.wechat.entity.CheckInRecords;
import com.wechat.entity.Members;
import com.wechat.entity.response.MemberStatistics;
import com.wechat.result.Response;
import com.wechat.service.IBasicInfoService;
import com.wechat.service.ICheckInRecordsService;
import com.wechat.service.IMembersService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author: xielongfei
 * @date: 2023/07/19
 * @description:
 */
@RestController
@RequestMapping("/stat")
public class StatMember {

    @Autowired
    private IMembersService membersService;

    @Autowired
    private ICheckInRecordsService checkInRecordsService;

    @Autowired
    private IBasicInfoService basicInfoService;

    @ApiOperation(value = "统计会员")
    @GetMapping(value = "/member")
    public Object member() {
        MemberStatistics statistics = new MemberStatistics();
        // 1. 当前会员数
        long currentMemberCount = membersService.count();
        statistics.setCurrentMemberCount(currentMemberCount);

        // 2. 累计添加会员数
        //根据累计会员字段
        List<BasicInfo> list = basicInfoService.list();
        if (!CollectionUtils.isEmpty(list)) {
            BasicInfo basicInfo = list.get(0);
            statistics.setTotalMemberCount(currentMemberCount + basicInfo.getAccumulatedDeletedMembers());
        } else {
            statistics.setTotalMemberCount(currentMemberCount);
        }

        // 3. 超级会员数
        long superMemberCount = membersService.count(Wrappers.<Members>lambdaQuery().eq(Members::getMemberTypeId, 4));
        statistics.setSuperMemberCount(superMemberCount);

        // 4. 黄金会员数
        long goldMemberCount = membersService.count(Wrappers.<Members>lambdaQuery().eq(Members::getMemberTypeId, 2));
        statistics.setGoldMemberCount(goldMemberCount);

        // 5. 钻石会员数
        long diamondMemberCount = membersService.count(Wrappers.<Members>lambdaQuery().eq(Members::getMemberTypeId, 3));
        statistics.setDiamondMemberCount(diamondMemberCount);

        // 6. 白银会员数
        long silverMemberCount = membersService.count(Wrappers.<Members>lambdaQuery().eq(Members::getMemberTypeId, 1));
        statistics.setSilverMemberCount(silverMemberCount);

        // 7. 本月新加入会员数
        long newMemberCountThisMonth = membersService.count(Wrappers.<Members>lambdaQuery()
                .ge(Members::getCreateDate, LocalDate.now().withDayOfMonth(1)));
        statistics.setNewMemberCountThisMonth(newMemberCountThisMonth);

        // 8. 本年度新加入会员数
        long newMemberCountThisYear = membersService.count(Wrappers.<Members>lambdaQuery()
                .ge(Members::getCreateDate, LocalDate.now().withDayOfYear(1)));
        statistics.setNewMemberCountThisYear(newMemberCountThisYear);

        // 9. 哪个省份会员最多
        QueryWrapper<Members> maxProvinceQuery = new QueryWrapper<>();
        maxProvinceQuery.select("province") // 只选择省份字段
                .groupBy("province") // 按省份分组
                .orderByDesc("count(*)") // 按照数量降序排序
                .last("limit 1"); // 限制只返回一条结果
        String maxProvince = membersService.getOne(maxProvinceQuery).getProvince();
        statistics.setMaxProvince(maxProvince);

        LambdaQueryWrapper<Members> maxProvinceQueryCount = Wrappers.lambdaQuery();
        maxProvinceQueryCount.eq(Members::getProvince, maxProvince);
        long maxProvinceCount = membersService.count(maxProvinceQueryCount);
        statistics.setMaxProvinceCount(maxProvinceCount);

        // 10. 哪个地市会员最多
        QueryWrapper<Members> maxCityQuery = new QueryWrapper<>();
        maxCityQuery.select("city").groupBy("city").orderByDesc("count(*)").last("limit 1");
        String maxCity = membersService.getOne(maxCityQuery).getCity();
        statistics.setMaxCity(maxCity);

        LambdaQueryWrapper<Members> maxCityQueryCount = Wrappers.lambdaQuery();
        maxCityQueryCount.eq(Members::getCity, maxCity);
        long maxCityCount = membersService.count(maxCityQueryCount);
        statistics.setMaxCityCount(maxCityCount);

        // 11. 警告次数最多的会员
        LambdaQueryWrapper<Members> maxWarnCountQuery = Wrappers.lambdaQuery();
        maxWarnCountQuery.gt(Members::getWarnCount, 0).orderByDesc(Members::getWarnCount).last("limit 1");
        Members maxWarnCountMember = membersService.getOne(maxWarnCountQuery);
        if (maxWarnCountMember == null) {
            statistics.setMaxWarnCountMember("-");
        } else {
            statistics.setMaxWarnCountMember(maxWarnCountMember.getName());
        }

        // 12. 处于警告中的人数
        long warningMemberCount = membersService.count(Wrappers.<Members>lambdaQuery().eq(Members::getWarnStatus, 1));
        statistics.setWarningMemberCount(warningMemberCount);

        // 13. 连续一个月未打卡的会员
        LambdaQueryWrapper<Members> warnOneMonthAgoQuery = Wrappers.lambdaQuery();
        warnOneMonthAgoQuery.le(Members::getWarnDate, LocalDate.now().minusMonths(1));
        long warnOneMonthAgoMember = membersService.count(warnOneMonthAgoQuery);
        statistics.setWarnOneMonthAgoMember(warnOneMonthAgoMember);


        // 打卡次数最多的会员和次数
        QueryWrapper mostCheckInQuery = new QueryWrapper<CheckInRecords>().select("member_id as memberId", "COUNT(*) as checkInCount")
                .groupBy("member_id")
                .orderByDesc("checkInCount")
                .last("limit 1");
        Map<String, Long> mostCheckInResult = checkInRecordsService.getMap(mostCheckInQuery);
        statistics.setMostCheckInMember(membersService.getById(mostCheckInResult.get("memberId")).getName());
        statistics.setMostCheckInCount(mostCheckInResult.get("checkInCount"));

        // 打卡次数最少的会员和次数
        QueryWrapper leastCheckInQuery = new QueryWrapper<CheckInRecords>().select("member_id as memberId", "COUNT(*) as checkInCount")
                .groupBy("member_id")
                .orderByAsc("checkInCount")
                .last("limit 1");
        Map<String, Long> leastCheckInResult = checkInRecordsService.getMap(leastCheckInQuery);
        statistics.setLeastCheckInMember(membersService.getById(leastCheckInResult.get("memberId")).getName());
        statistics.setLeastCheckInCount(leastCheckInResult.get("checkInCount"));

        // 连续打卡次数最多的会员和次数
        QueryWrapper mostConsecutiveCheckInQuery = new QueryWrapper<CheckInRecords>().select("member_id as memberId", "MAX(consecutive_check_ins) as consecutiveCheckInCount")
                .groupBy("member_id")
                .orderByDesc("consecutiveCheckInCount")
                .last("limit 1");
        Map<String, Integer> mostConsecutiveCheckInResult = checkInRecordsService.getMap(mostConsecutiveCheckInQuery);
        statistics.setMostConsecutiveCheckInMember(membersService.getById(mostConsecutiveCheckInResult.get("memberId")).getName());
        Integer consecutiveCheckInCount = mostConsecutiveCheckInResult.get("consecutiveCheckInCount");
        statistics.setMostConsecutiveCheckInCount(consecutiveCheckInCount == null ? 0 : consecutiveCheckInCount);

        return Response.success(statistics);
    }
}
