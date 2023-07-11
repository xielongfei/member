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
import com.wechat.util.MenuUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
//        if (!Objects.equals(code, membersRequest.getVerificationCode())) {
//            return Response.failure(ResultCode.UNAUTHORIZED);
//        }
        Members members = membersService.getOne(Wrappers.<Members>lambdaQuery().eq(Members::getPhone, membersRequest.getPhone()));
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
        Members memberDB = membersService.getById(members.getId());
        return Response.success(memberDB);
    }

    @ApiOperation(value = "查会员列表")
    @GetMapping(value = "/list")
    public Object list() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Members members = (Members)authentication.getPrincipal();
        List<Members> list = membersService.list();
        return Response.success(list);
    }

    @ApiOperation(value = "新增会员")
    @PostMapping(value = "/add")
    public Object add(@RequestBody Members members) throws IOException {
        boolean bool = membersService.add(members);
        if (bool) {
            return Response.success();
        } else {
            return Response.failure(ResultCode.PARAMS_IS_INVALID);
        }
    }

    @ApiOperation(value = "更新会员")
    @PostMapping(value = "/update")
    public Object update(@RequestBody Members members) {
        membersService.updateById(members);
        return Response.success();
    }

    @ApiOperation(value = "查会员距离")
    @GetMapping(value = "/distance")
    public Object getDistance(MembersRequest membersRequest) {
        String distance = membersService.getDistance(membersRequest);
        return Response.success(distance);
    }
}
