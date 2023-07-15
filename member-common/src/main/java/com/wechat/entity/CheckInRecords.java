package com.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 打卡记录表
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@Getter
@Setter
@TableName("check_in_records")
public class CheckInRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID，对应members(id)
     */
    @TableField("member_id")
    private Integer memberId;

    /**
     * 打卡日期
     */
    @TableField("check_in_date")
    private LocalDateTime checkInDate;

    /**
     * 连续打卡次数
     */
    @TableField("consecutive_check_ins")
    private Integer consecutiveCheckIns;

    /**
     * 省
     */
    @TableField("province")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    private String city;

    /**
     * 区/县
     */
    @TableField("county")
    private String county;

    /**
     * 详细地址
     */
    @TableField("address")
    private String address;

    /**
     * 完整地址，包括省、市、区和详细地址的组合
     */
    @TableField("full_address")
    private String fullAddress;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_date")
    private LocalDateTime updateDate;
}
