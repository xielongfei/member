package com.wechat.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wechat.entity.Members;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.entity.response.MembersResponse;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author 
 * @since 2023-07-06
 */
public interface MembersMapper extends BaseMapper<Members> {

    /**
     * 查询全部会员
     * @return
     */
    IPage<MembersResponse> getAllMembersWithCheckInStatus(IPage page, @Param("search") String search);

    /**
     * 查询已打卡的会员
     * @return
     */
    IPage<MembersResponse> getCheckedInMembers(IPage page, @Param("search") String search);

    /**
     * 查询未打卡的会员
     * @return
     */
    IPage<MembersResponse> getNotCheckedInMembers(IPage page, @Param("search") String search);

}
