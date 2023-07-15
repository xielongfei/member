package com.wechat.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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

    @ApiOperation(value = "获取地图key")
    @GetMapping(value = "/getMapKey")
    public Object getMapKey() {
        List<BasicInfo> list = basicInfoService.list();
        BasicInfo basicInfo = list.get(0);
        String key = "";
        // 找到满足条件的任意一个 key
        JSONArray mapKey = JSONArray.parseArray(basicInfo.getMapKey());
        JSONObject foundKey = mapKey.stream()
                .map(obj -> (JSONObject) obj)
                .filter(jsonObject -> jsonObject.getIntValue("limit") > 0)
                .findAny()
                .orElse(null);

        if (foundKey != null) {
            mapKey.remove(foundKey);
            key = foundKey.getString("key");
            int limit = foundKey.getIntValue("limit");

            // 更新 limit 值
            foundKey.put("limit", limit - 1);
            mapKey.add(foundKey);
            BasicInfo info = new BasicInfo();
            info.setId(basicInfo.getId());
            info.setMapKey(mapKey.toJSONString());
            basicInfoService.updateById(info);
        }
        return Response.success(key);
    }

}
