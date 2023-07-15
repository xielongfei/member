package com.wechat.mapper;

import com.wechat.entity.Members;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wechat.entity.response.MembersResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<MembersResponse> getAllMembersWithCheckInStatus(@Param("search") String search);

    /**
     * 查询已打卡的会员
     * @return
     */
    List<MembersResponse> getCheckedInMembers(@Param("search") String search);

    /**
     * 查询未打卡的会员
     * @return
     */
    List<MembersResponse> getNotCheckedInMembers(@Param("search") String search);






}
