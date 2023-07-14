package com.wechat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.wechat.entity.BasicInfo;
import com.wechat.result.Response;
import com.wechat.service.IBasicInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Object getOne() {
        List<BasicInfo> list = basicInfoService.list();
        BasicInfo basicInfo = null;
        if (!CollectionUtils.isEmpty(list)) {
            basicInfo = list.get(0);
        }
        return Response.success(basicInfo);
    }

    @ApiOperation(value = "新增/更新基础信息")
    @PostMapping(value = "/update")
    public Object update(@RequestBody BasicInfo basicInfo) {
        LambdaQueryWrapper wrapper = Wrappers.<BasicInfo>lambdaQuery()
                .orderByAsc(BasicInfo::getId)
                .last(" limit 1 ");
        BasicInfo dbInfo = basicInfoService.getOne(wrapper);
        if (dbInfo != null) {
            basicInfo.setId(dbInfo.getId());
        }
        basicInfoService.saveOrUpdate(basicInfo);
        return Response.success();
    }

}
