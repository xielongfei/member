package com.wechat.result;

import lombok.Getter;

/**
 * @description:
 */
@Getter
public enum ResultCode {

    SUCCESS(200, "成功"),//成功
    //FAIL(400, "失败"),//失败
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "认证失败"),//未认证
    SC_FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "不存在"),//不存在
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),//服务器内部错误
    METHOD_NOT_ALLOWED(405,"方法不被允许"),

    /*参数错误:1001-1999*/
    PARAMS_IS_INVALID(1001, "参数无效"),
    PARAMS_IS_BLANK(1002, "参数为空"),
    NULL_POINT(1003, "空指针"),

    /*用户错误2001-2999*/
    CHECKED_IN(2001, "已打卡"),
    DUPLICATE_ENTRY(2002, "重复添加"),
    DISTANCE_PROTECTION_LIMIT(2003, "保存失败，当前所选位置与他人店铺位置距离过近");

    private Integer code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}