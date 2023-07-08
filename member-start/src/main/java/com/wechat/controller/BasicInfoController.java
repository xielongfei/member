package com.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.BasicInfo;
import com.wechat.service.IBasicInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 基础信息表 前端控制器
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@RestController
@RequestMapping("/basicInfo")
public class BasicInfoController {

    @Autowired
    private IBasicInfoService basicInfoService;

    @ApiOperation(value = "查基础信息")
    @GetMapping(value = "/getOne")
    public Object getOne(BasicInfo basicInfoRequest) {
        BasicInfo basicInfo = basicInfoService.getById(basicInfoRequest.getId());
        return basicInfo;
    }

    @ApiOperation(value = "更新基础信息")
    @PostMapping(value = "/update")
    public Object update(@RequestBody BasicInfo basicInfo) {
        LambdaQueryWrapper wrapper = Wrappers.<BasicInfo>lambdaQuery()
                .orderByAsc(BasicInfo::getId)
                .last(" limit 1 ");
        BasicInfo dbInfo = basicInfoService.getOne(wrapper);
        if (dbInfo != null) {
            basicInfo.setId(dbInfo.getId());
        }
        return basicInfoService.saveOrUpdate(basicInfo);
    }

}
