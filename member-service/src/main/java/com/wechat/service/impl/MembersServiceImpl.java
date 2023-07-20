package com.wechat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.Members;
import com.wechat.entity.request.MembersRequest;
import com.wechat.enums.MembershipType;
import com.wechat.mapper.MembersMapper;
import com.wechat.service.IMembersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author 
 * @since 2023-07-06
 */
@Service
public class MembersServiceImpl extends ServiceImpl<MembersMapper, Members> implements IMembersService {

    @Override
    public boolean add(Members members){
        boolean b = members.getIdCard() != null && members.getIdCard().length() > 0;
        LambdaQueryWrapper wrapper = Wrappers.<Members>lambdaQuery()
                .eq(Members::getPhone, members.getPhone())
                .or().eq(Members::getShopId, members.getShopId())
                .func(b, f -> f.or().eq(Members::getIdCard, members.getIdCard()));
        Members membersDB = super.getOne(wrapper);
        if (membersDB != null) {
            return false;
        }
        members.setMemberTypeName(MembershipType.getNameByCode(members.getMemberTypeId()));
        members.setWarnDate(LocalDate.now());
        return super.save(members);
    }

    @Override
    public boolean remove(Members members) {
        //只修改删除状态
        boolean bool = super.updateById(members);
        return bool;
    }

    @Override
    public String getDistance(MembersRequest membersRequest) {
        Members membersA = super.getOne(Wrappers.<Members>lambdaQuery().eq(Members::getShopId, membersRequest.getShopIdA()));
        Members membersb = super.getOne(Wrappers.<Members>lambdaQuery().eq(Members::getShopId, membersRequest.getShopIdB()));
        double distance = getDistanceBetween(membersA.getLatitude(), membersA.getLongitude(),
                membersb.getLatitude(), membersb.getLongitude());
        // 将距离格式化为需要的单位或精度
        String formattedDistance = formatDistance(distance);
        return formattedDistance;
    }

    // 使用Haversine formula计算两个地点之间的距离
    private double getDistanceBetween(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // 地球平均半径，单位：千米
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    // 格式化距离，可以按需求设置单位和精度
    private String formatDistance(double distance) {
        return String.format("%.2f km", distance);
    }
}
