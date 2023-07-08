package com.wechat.service.impl;

import com.wechat.entity.Materials;
import com.wechat.mapper.MaterialsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wechat.service.IMaterialsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 物料表 服务实现类
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@Service
public class MaterialsServiceImpl extends ServiceImpl<MaterialsMapper, Materials> implements IMaterialsService {

}
