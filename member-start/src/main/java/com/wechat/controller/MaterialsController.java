package com.wechat.controller;

import com.wechat.entity.Materials;
import com.wechat.service.IMaterialsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 * 物料表 前端控制器
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@RestController
@RequestMapping("/materials")
public class MaterialsController {

    @Autowired
    private IMaterialsService materialsService;

    @ApiOperation(value = "查单个物料")
    @GetMapping(value = "/getOne")
    public Object getOne(Materials materialsRequest) {
        Materials materials = materialsService.getById(materialsRequest.getId());
        return materials;
    }

    @ApiOperation(value = "查物料列表")
    @GetMapping(value = "/list")
    public Object list() {
        List<Materials> list = materialsService.list();
        return list;
    }

    @ApiOperation(value = "新增物料")
    @PostMapping(value = "/add")
    public Object add(@RequestBody Materials materials) {
        //上传图片、缩略图
        return materialsService.save(materials);
    }

    @ApiOperation(value = "更新物料")
    @PostMapping(value = "/update")
    public Object update(@RequestBody Materials materials) {
        //上传图片、缩略图
        return materialsService.updateById(materials);
    }

}
