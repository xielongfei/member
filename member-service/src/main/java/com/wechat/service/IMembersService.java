package com.wechat.service;

import com.wechat.entity.Members;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wechat.entity.request.MembersRequest;
import com.wechat.result.Result;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author 
 * @since 2023-07-06
 */
public interface IMembersService extends IService<Members> {

    /**
     * 删除会员
     * @param members
     * @return
     */
    Result add(Members members);

    /**
     * 移除会员
     * @param members
     * @return
     */
    boolean remove(Members members);

    /**
     * 计算会员距离
     * @param membersRequest
     * @return
     */
    String getDistance(MembersRequest membersRequest);
}
