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
 * 基础信息表
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@Getter
@Setter
@TableName("basic_info")
public class BasicInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 客服电话
     */
    @TableField("customer_phone")
    private String customerPhone;

    /**
     * 物料联系人
     */
    @TableField("material_contact")
    private String materialContact;

    /**
     * 物料联系人二维码
     */
    @TableField("material_contact_qr_code")
    private String materialContactQrCode;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_date")
    private LocalDateTime updateDate;
}
