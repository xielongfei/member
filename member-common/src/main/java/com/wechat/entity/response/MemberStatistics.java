package com.wechat.entity.response;

import lombok.Data;

/**
 * @date: 2023/07/19
 * @description:
 */
@Data
public class MemberStatistics {

    // 累计添加会员数
    private long totalMemberCount;

    // 当前会员数
    private long currentMemberCount;

    // 超级会员数
    private long superMemberCount;

    // 黄金会员数
    private long goldMemberCount;

    // 钻石会员数
    private long diamondMemberCount;

    // 白银会员数
    private long silverMemberCount;

    // 本月新加入会员数
    private long newMemberCountThisMonth;

    // 本年度新加入会员数
    private long newMemberCountThisYear;

    // 哪个省份会员最多
    private String maxProvince;

    //最多省份会员数量
    private long maxProvinceCount;

    // 哪个地市会员最多
    private String maxCity;

    //最多地市会员数量
    private long maxCityCount;

    // 警告次数最多的会员
    private long maxWarnCountMember;

    // 处于警告中的人数
    private long warningMemberCount;

    // 连续一个月未打卡的会员
    private long warnOneMonthAgoMember;

    // 打卡次数最多的会员
    private String mostCheckInMember;

    // 打卡次数最多的会员打卡次数
    private long mostCheckInCount;

    // 打卡次数最少的会员
    private String leastCheckInMember;

    // 打卡次数最少的会员打卡次数
    private long leastCheckInCount;

    // 连续打卡次数最多的会员
    private String mostConsecutiveCheckInMember;

    // 连续打卡次数最多的会员连续打卡次数
    private long mostConsecutiveCheckInCount;

}
