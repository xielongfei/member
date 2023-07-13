package com.wechat.enums;

/**
 * @author: xielongfei
 * @date: 2023/07/07
 * @description:
 */

public enum MembershipType {
    SILVER(1, "白银会员"),
    GOLD(2, "黄金会员"),
    DIAMOND(3, "钻石会员"),
    SUPER_ADMIN(4, "超级管理员");

    private int code;
    private String name;

    MembershipType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getNameByCode(int code) {
        for (MembershipType type : MembershipType.values()) {
            if (type.getCode() == code) {
                return type.getName();
            }
        }
        return "Unknown";
    }
}
