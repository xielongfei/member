package com.wechat.schedule;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.BasicInfo;
import com.wechat.entity.CheckLink;
import com.wechat.entity.Members;
import com.wechat.service.IBasicInfoService;
import com.wechat.service.ICheckLinkService;
import com.wechat.service.IMembersService;
import com.wechat.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private IBasicInfoService basicInfoService;

    @Autowired
    private ICheckLinkService linkService;

    /**
     * 每天扫描31内未打卡得会员
     * 1. 警告次数+1
     * 2. 设置警告状态
     * 3. 警告日期重置
     * 4. 并给会员和管理员发送短信
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
                members.setWarnCount(members.getWarnCount() + 1);
                members.setWarnStatus(1);
                members.setWarnDate(LocalDate.now());
                membersService.updateById(members);

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

    /**
     * 刷新地图Map key
     */
    @Scheduled(cron = "0 0 1 * * ?") // 每天早上1点触发任务
    public void refreshMapKey() {
        try {
            List<BasicInfo> list = basicInfoService.list();
            if (!CollectionUtils.isEmpty(list)) {
                BasicInfo basicInfo = list.get(0);

                // 解析JSON字符串为JSONArray
                JSONArray jsonArray = JSON.parseArray(basicInfo.getMapKey());

                // 遍历JSONArray，修改每个JSONObject中的"limit"字段值为50
                for (Object obj : jsonArray) {
                    if (obj instanceof JSONObject) {
                        JSONObject jsonObject = (JSONObject) obj;
                        jsonObject.put("limit", 50);
                    }
                }

                // 将修改后的JSONArray转换回JSON字符串
                String modifiedJson = jsonArray.toJSONString();
                basicInfo.setMapKey(modifiedJson);
                basicInfoService.updateById(basicInfo);
            }
            log.info("刷新地图key完成");
        } catch (Exception e) {
            log.error("刷新地图异常：{}", e);
        }
    }

    //控制短信频率

    /**
     * 定时任务凌晨删除文件夹
     */
    @Scheduled(cron = "0 30 0 * * ?") // 每天0点30分触发任务
    public void refreshFolder() {
        LambdaQueryWrapper wrappers = Wrappers.<CheckLink>lambdaQuery().gt(CheckLink::getCreateDate, LocalDateTime.now().withHour(0).withMinute(0));
        List<CheckLink> list = linkService.list(wrappers);
        if (list.size() == 0) {
            //清空数据库
            List<CheckLink> checkLinkList = linkService.list();
            linkService.removeBatchByIds(checkLinkList.stream().map(CheckLink::getId).collect(Collectors.toList()));
            //删除文件夹
            String directoryPath = "/images/checkIn";
            // 调用清空目录的方法
            clearDirectory(new File(directoryPath));
        }
    }

    /**
     * 清空文件夹
     * @param directory
     */
    public static void clearDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    // 递归清空子目录和文件
                    clearDirectory(file);
                }
            }
        }
        // 删除文件或空目录
        //directory.delete();
    }
}
