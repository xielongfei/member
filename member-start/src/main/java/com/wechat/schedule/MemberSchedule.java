package com.wechat.schedule;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.Members;
import com.wechat.service.IMembersService;
import com.wechat.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * @date: 2023/07/15
 * @description:
 */

@Component
@Slf4j
public class MemberSchedule {

    @Autowired
    private IMembersService membersService;

    @Autowired
    private ISmsService smsService;

    /**
     * 每天扫描31内未打卡得会员，警告次数+1，并给会员和管理员发送短信
     */
    @Scheduled(cron = "0 0 8 * * ?") // 每天早上8点触发任务
    public void scanAndSendNotifications() {
        try {
            Thread.sleep(10000);
            List<Members> superMember = membersService.list(Wrappers.<Members>lambdaQuery()
                    .eq(Members::getMemberTypeId, 4));

            LambdaQueryWrapper wrapper = Wrappers.<Members>lambdaQuery()
                    .in(Members::getMemberTypeId, Arrays.asList(1, 2))
                    .le(Members::getWarnDate, LocalDate.now().minusDays(31));
            List<Members> list = membersService.list(wrapper);
            for (Members members : list) {
                smsService.sendMemberWarnMsg(members);

                superMember.stream().forEach(e -> {
                    smsService.sendSuperWarnMsg(e.getPhone(), members);
                });
            }
            log.info("告警短信处理完毕, 共处理会员：{}个", list.size());
        } catch (Exception e) {
            log.error("告警任务异常：{}", e);
        }
    }
}
