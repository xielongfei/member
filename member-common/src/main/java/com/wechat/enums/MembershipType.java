package com.wechat.enums;

/**
 * @author: xielongfei
 * @date: 2023/07/07
 * @description:
 */

public enum MembershipType {
    SILVER("白银"),
    GOLD("金牌"),
    PLATINUM("铂金"),
    SUPER("超级");

    private String name;

    MembershipType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
