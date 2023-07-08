package com.wechat.controller;

import com.wechat.entity.Members;
import com.wechat.entity.request.MembersRequest;
import com.wechat.jwt.JwtTokenUtils;
import com.wechat.service.IMembersService;
import com.wechat.service.ISmsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @GetMapping(value = "/login")
    public String login(String user,String password){
        Map map = new HashMap();
        map.put("user",user);
        map.put("password",password);
        return jwtTokenUtils.createToken(map);
    }

    @ApiOperation("发送验证码")
    @PostMapping("/sendCode")
    public Object sendCode(String phoneNumber) {
        boolean isSend = smsService.send(phoneNumber);
        if (isSend){
            return "success";
        } else {
            return "短信发送失败！";
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
