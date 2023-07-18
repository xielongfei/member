package com.wechat.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
//@ExcelIgnoreUnannotated
public class Members implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "会员编号")
    private Integer id;

    /**
     * 姓名
     */
    @TableField("name")
    @ExcelProperty(value = "姓名")
    private String name;

    /**
     * 加盟时间
     */
    @TableField("join_date")
    @ExcelProperty(value = "加盟时间")
    private LocalDate joinDate;

    /**
     * 商铺编号
     */
    @TableField("shop_id")
    @ExcelProperty(value = "商铺编号")
    private String shopId;

    /**
     * 推荐人编号
     */
    @TableField("referral_id")
    @ExcelProperty(value = "推荐人编号")
    private String referralId;

    /**
     * 会员类型ID（1: 白银会员, 2: 黄金会员, 3: 钻石会员, 4: 超级管理员）
     */
    @TableField("member_type_id")
    @ExcelIgnore
    private Integer memberTypeId;

    /**
     * 会员类型名称
     */
    @TableField("member_type_name")
    @ExcelProperty(value = "会员等级")
    private String memberTypeName;

    /**
     * 身份证号
     */
    @TableField("id_card")
    @ExcelProperty(value = "身份证号")
    private String idCard;

    /**
     * 手机号
     */
    @TableField("phone")
    @ExcelProperty(value = "手机号")
    private String phone;

    /**
     * 省
     */
    @TableField("province")
    @ExcelProperty(value = "省")
    private String province;

    /**
     * 市
     */
    @TableField("city")
    @ExcelProperty(value = "市")
    private String city;

    /**
     * 区/县
     */
    @TableField("county")
    @ExcelProperty(value = "区/县")
    private String county;

    /**
     * 详细地址
     */
    @TableField("address")
    @ExcelProperty(value = "详细地址")
    private String address;

    /**
     * 完整地址，包括省、市、区和详细地址的组合
     */
    @TableField("full_address")
    @ExcelProperty(value = "完整地址")
    private String fullAddress;

    /**
     * 纬度
     */
    @TableField("latitude")
    @ExcelProperty(value = "纬度")
    private Double latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    @ExcelProperty(value = "经度")
    private Double longitude;

    /**
     * 照片路径
     */
    @TableField("file_path")
    @ExcelIgnore
    private String filePath;

    /**
     * 警告状态：0-非警告状态，1-警告状态
     */
    @TableField("warn_status")
    @ExcelProperty(value = "警告状态")
    private Integer warnStatus;

    /**
     * 警告次数
     */
    @TableField("warn_count")
    @ExcelProperty(value = "警告次数")
    private Integer warnCount;

    /**
     * 警告时间
     */
    @TableField("warn_date")
    @ExcelProperty(value = "警告时间")
    private LocalDate warnDate;

    @TableField("create_date")
    @ExcelProperty(value = "创建时间")
    private LocalDateTime createDate;

    @TableField("update_date")
    @ExcelProperty(value = "更新时间")
    private LocalDateTime updateDate;
}
