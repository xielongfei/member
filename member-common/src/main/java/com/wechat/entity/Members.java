package com.wechat.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author 
 * @since 2023-07-06
 */
@Getter
@Setter
@TableName("members")
public class Members implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 加盟时间
     */
    @TableField("join_date")
    private LocalDate joinDate;

    /**
     * 商铺编号
     */
    @TableField("shop_id")
    private String shopId;

    /**
     * 推荐人编号
     */
    @TableField("referral_id")
    private String referralId;

    /**
     * 会员类型ID
     */
    @TableField("member_type_id")
    private Integer memberTypeId;

    /**
     * 会员类型名称（白银、金牌、铂金、超级）
     */
    @TableField("member_type_name")
    private String memberTypeName;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 手机号
     */
    @TableField("phone")
    private String phone;

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

    /**
     * 纬度
     */
    @TableField("latitude")
    private BigDecimal latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private BigDecimal longitude;

    /**
     * 删除状态：0-未删除，1-已删除
     */
    @TableField("delete_status")
    private Integer deleteStatus;

    /**
     * 照片路径
     */
    @TableField("file_path")
    private String filePath;

    @TableField("create_date")
    private LocalDateTime createDate;

    @TableField("update_date")
    private LocalDateTime updateDate;
}
