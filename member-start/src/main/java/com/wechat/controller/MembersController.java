package com.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wechat.entity.BasicInfo;
import com.wechat.entity.CheckInRecords;
import com.wechat.entity.Members;
import com.wechat.entity.request.MembersRequest;
import com.wechat.entity.response.MembersResponse;
import com.wechat.enums.MembershipType;
import com.wechat.jwt.JwtTokenUtils;
import com.wechat.mapper.MembersMapper;
import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import com.wechat.service.IBasicInfoService;
import com.wechat.service.ICheckInRecordsService;
import com.wechat.service.IMembersService;
import com.wechat.service.ISmsService;
import com.wechat.sms.CacheUtil;
import com.wechat.util.ExportUtils;
import com.wechat.util.GeoUtils;
import com.wechat.util.MenuUtil;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author
 * @since 2023-07-06
 */
@RestController
@RequestMapping("/members")
public class MembersController {

    /**
     * 350米内只能出现一家店铺
     */
    public static final double distance = 0.35;

    @Autowired
    private IMembersService membersService;

    @Autowired
    private MembersMapper membersMapper;

    @Autowired
    private ISmsService smsService;

    @Autowired
    private IBasicInfoService basicInfoService;

    @Autowired
    private ICheckInRecordsService checkInRecordsService;

    private final JwtTokenUtils jwtTokenUtils;

    public MembersController(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @ApiOperation("登录授权")
    @PostMapping(value = "/login")
    public Object login(@RequestBody MembersRequest membersRequest) {
        String code = CacheUtil.cache.getIfPresent(membersRequest.getPhone());
        if (!Objects.equals(code, membersRequest.getVerificationCode())) {
            return Response.failure(ResultCode.UNAUTHORIZED);
        }
        Members members = membersService.getOne(Wrappers.<Members>lambdaQuery().eq(Members::getPhone, membersRequest.getPhone()));
        if (members == null) {
            return Response.failure(ResultCode.SC_FORBIDDEN);
        }
        if (Objects.equals(1, members.getMemberTypeId()) || Objects.equals(2, members.getMemberTypeId())) {
            //警告状态下禁止登录
            if (Objects.equals(members.getWarnStatus(), 1)) {
                return Response.failure(ResultCode.SC_FORBIDDEN);
            }
//            //31天未打卡禁止登录
//            long daysDiff = ChronoUnit.DAYS.between(members.getWarnDate(), LocalDate.now());
//            if (daysDiff >= 31) {
//                return Response.failure(ResultCode.SC_FORBIDDEN);
//            }
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("member", members);
        String token = jwtTokenUtils.createToken(members);
        claims.put("token", token);
        claims.put("auth", MenuUtil.menuMap.get(members.getMemberTypeId()));
        return Response.success(claims);
    }

    @ApiOperation("发送验证码")
    @PostMapping("/sendCode")
    public Object sendCode(@RequestBody Members members) {
        boolean isSend = smsService.sendVerificationCode(members.getPhone());
        if (isSend) {
            return Response.success();
        } else {
            return Response.failure(ResultCode.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "查单个会员")
    @GetMapping(value = "/getOne")
    public Object getOne(Members members) {
        //        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Members members = (Members)authentication.getPrincipal();
        Members memberDB = membersService.getById(members.getId());
        return Response.success(memberDB);
    }

    @ApiOperation(value = "查会员列表")
    @GetMapping(value = "/list")
    public Object list(Page page, MembersRequest membersRequest) {
        IPage iPage;
        //List<MembersResponse> list;
        if (Objects.equals(membersRequest.getCheckInTab(), 1)) {
            iPage = membersMapper.getCheckedInMembers(page, membersRequest.getSearch());
        } else if (Objects.equals(membersRequest.getCheckInTab(), 2)) {
            iPage = membersMapper.getNotCheckedInMembers(page, membersRequest.getSearch());
        } else {
            iPage = membersMapper.getAllMembersWithCheckInStatus(page, membersRequest.getSearch());
        }

        return Response.success(iPage);
    }

    @ApiOperation(value = "姓名或编号模糊搜索会员")
    @GetMapping(value = "/searchMemberByNameOrShopId")
    public Object searchMemberByNameOrShopId(MembersRequest membersRequest) {
        LambdaQueryWrapper wrapper = Wrappers.<Members>lambdaQuery()
                .like(Members::getName, membersRequest.getSearch())
                .or().like(Members::getShopId, membersRequest.getSearch());
        List<Members> list = membersService.list(wrapper);
        return Response.success(list);
    }

    @ApiOperation(value = "新增会员")
    @PostMapping(value = "/add")
    public Object add(@RequestBody Members members) {

        List<Members> list = membersService.list();
        for (Members location : list) {
            double locationDistance = GeoUtils.calculateDistance(members.getLatitude(), members.getLongitude(), location.getLatitude(), location.getLongitude());
            if (locationDistance <= distance) {
                return Response.failure(ResultCode.DISTANCE_PROTECTION_LIMIT);
            }
        }

        boolean bool = membersService.add(members);
        if (bool) {
            return Response.success();
        } else {
            return Response.failure(ResultCode.DUPLICATE_PHONE);
        }
    }

    @ApiOperation(value = "更新会员")
    @PostMapping(value = "/update")
    public Object update(@RequestBody Members members) {

        List<Members> list = membersService.list();
        for (Members location : list) {
            if (Objects.equals(members.getId(), location.getId())) {
                continue;
            }
            if (members.getLatitude() != null && members.getLongitude() != null) {
                double locationDistance = GeoUtils.calculateDistance(members.getLatitude(), members.getLongitude(), location.getLatitude(), location.getLongitude());
                if (locationDistance <= distance) {
                    return Response.failure(ResultCode.DISTANCE_PROTECTION_LIMIT);
                }
            }
        }
        boolean b = members.getIdCard() != null && members.getIdCard().length() > 0;
        LambdaQueryWrapper wrapper = Wrappers.<Members>lambdaQuery()
                .eq(Members::getPhone, members.getPhone())
                .or().eq(Members::getShopId, members.getShopId())
                .func(b, f -> f.or().eq(Members::getIdCard, members.getIdCard()));
        Members membersDB = membersService.getOne(wrapper);
        if (membersDB != null && !Objects.equals(membersDB.getId(), members.getId())) {
            return Response.failure(ResultCode.DUPLICATE_PHONE);
        }
        membersService.updateById(members);
        return Response.success();
    }

    @ApiOperation(value = "删除会员")
    @PostMapping(value = "/remove")
    public Object remove(@RequestBody Members members) {
        membersService.removeById(members);
        //根据累计会员字段
        List<BasicInfo> list = basicInfoService.list();
        if (!CollectionUtils.isEmpty(list)) {
            BasicInfo basicInfo = list.get(0);
            basicInfo.setAccumulatedDeletedMembers(basicInfo.getAccumulatedDeletedMembers() + 1);
            basicInfoService.updateById(basicInfo);
        }
        //删除会员打卡记录
        checkInRecordsService.remove(Wrappers.<CheckInRecords>lambdaQuery().eq(CheckInRecords::getMemberId, members.getId()));
        return Response.success();
    }

    @ApiOperation(value = "查会员距离")
    @GetMapping(value = "/distance")
    public Object getDistance(MembersRequest membersRequest) {
        String distance = membersService.getDistance(membersRequest);
        return Response.success(distance);
    }

    @ApiOperation(value = "查超级会员列表")
    @GetMapping(value = "/listSuperMember")
    public Object listSuperMember() {
        List<Members> list = membersService.list(Wrappers.<Members>lambdaQuery()
                .eq(Members::getMemberTypeId, MembershipType.SUPER_ADMIN.getCode()));
        return Response.success(list);
    }

    @ApiOperation(value = "经纬度查附近会员")
    @GetMapping(value = "/findNearbyLocations")
    public Object findNearbyLocations(Double latitude, Double longitude) {
        List<Members> nearbyLocations = new ArrayList<>();

        List<Members> list = membersService.list();
        double distance = 2; //2km
        for (Members location : list) {
            double locationDistance = GeoUtils.calculateDistance(latitude, longitude, location.getLatitude(), location.getLongitude());
            if (locationDistance <= distance) {
                nearbyLocations.add(location);
            }
        }
        return Response.success(nearbyLocations);
    }

    @ApiOperation(value = "解除告警")
    @PostMapping(value = "/disableWarning")
    public Object disableWarning(@RequestBody Members members) {
        boolean b = members.getShopId() != null && members.getShopId().length() > 0;
        LambdaUpdateWrapper wrapper = Wrappers.<Members>lambdaUpdate()
                .set(Members::getWarnStatus, 0)
                .set(Members::getWarnDate, LocalDate.now())
                .func(b, f -> f.eq(Members::getShopId, members.getShopId()));
        membersService.update(wrapper);
        return Response.success();
    }

    @ApiOperation(value = "会员导出")
    @GetMapping("/export")
    public void exportMembers(HttpServletResponse response) throws IOException {
        List<Members> members = membersService.list();
        ExportUtils.exportMembersToExcel(members, response);
    }
}
