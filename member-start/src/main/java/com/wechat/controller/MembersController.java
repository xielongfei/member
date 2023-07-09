package com.wechat.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.Members;
import com.wechat.entity.request.MembersRequest;
import com.wechat.jwt.JwtTokenUtils;
import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import com.wechat.service.IMembersService;
import com.wechat.service.ISmsService;
import com.wechat.sms.CacheUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @Autowired
    private IMembersService membersService;

    @Autowired
    private ISmsService smsService;

    private final JwtTokenUtils jwtTokenUtils;

    public MembersController(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @ApiOperation("登录授权")
    @PostMapping(value = "/login")
    public Object login(@RequestBody MembersRequest membersRequest){
        String code = CacheUtil.cache.getIfPresent(membersRequest.getPhone());
//        if (!Objects.equals(code, verificationCode)) {
//            return Response.failure(ResultCode.UNAUTHORIZED);
//        }
        Members members = membersService.getOne(Wrappers.<Members>lambdaQuery().eq(Members::getPhone, membersRequest.getPhone()));
        Map<String, Object> claims = new HashMap<>();
        claims.put("member", members);
        String token = jwtTokenUtils.createToken(claims);
        claims.put("token", token);
        claims.put("auth", "");
        return Response.success(claims);
    }

    @ApiOperation("发送验证码")
    @PostMapping("/sendCode")
    public Object sendCode(@RequestBody Members members) {
        boolean isSend = smsService.send(members.getPhone());
        if (isSend){
            return Response.success();
        } else {
            return Response.failure(ResultCode.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "查单个会员")
    @GetMapping(value = "/getOne")
    public Object getOne(Members members) {
        Members dbMembers = membersService.getById(members.getId());
        return dbMembers;
    }

    @ApiOperation(value = "查会员列表")
    @GetMapping(value = "/list")
    public Object list() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Members members = (Members)authentication.getPrincipal();
        List<Members> list = membersService.list();
        return list;
    }

    @ApiOperation(value = "新增会员")
    @PostMapping(value = "/add")
    public Object add(@RequestBody Members members) {
        return membersService.add(members);
    }

    @ApiOperation(value = "更新会员")
    @PostMapping(value = "/update")
    public Object update(@RequestBody Members members) {
        return membersService.updateById(members);
    }

    @ApiOperation(value = "查会员距离")
    @GetMapping(value = "/distance")
    public Object getDistance(MembersRequest membersRequest) {
        String distance = membersService.getDistance(membersRequest);
        return distance;
    }
}
