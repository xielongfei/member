package com.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 物料表
 * </p>
 *
 * @author 
 * @since 2023-07-08
 */
@Getter
@Setter
@TableName("materials")
public class Materials implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物料ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 物料名称
     */
    @TableField("name")
    private String name;

    /**
     * 详情备注
     */
    @TableField("description")
    private String description;

    /**
     * 售价
     */
    @TableField("selling_price")
    private BigDecimal sellingPrice;

    /**
     * 原价
     */
    @TableField("original_price")
    private BigDecimal originalPrice;

    /**
     * 数量
     */
    @TableField("quantity")
    private Integer quantity;

    /**
     * 物料图片（原图）
     */
    @TableField("image_url")
    private String imageUrl;

    /**
     * 物料图片（缩略图）
     */
    @TableField("tn_image_url")
    private String tnImageUrl;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_date")
    private LocalDateTime updateDate;
}
