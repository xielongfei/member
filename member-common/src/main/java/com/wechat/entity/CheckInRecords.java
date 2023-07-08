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
     * 警告状态：0-非警告状态，1-警告状态
     */
    @TableField("warning_status")
    private Integer warningStatus;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_date")
    private LocalDateTime updateDate;
}
