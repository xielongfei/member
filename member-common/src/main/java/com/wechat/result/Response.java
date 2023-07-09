package com.wechat.result;

/**
 * @description:
 */
public class Response {

    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    // 只返回状态
    public static Result success() {
        return new Result()
                .setResult(ResultCode.SUCCESS);
    }

    // 成功返回数据
    public static Result success(Object data) {
        return new Result()
                .setResult(ResultCode.SUCCESS, data);
    }

    // 失败
    public static Result failure(ResultCode resultCode) {
        return new Result()
                .setResult(resultCode);
    }

    // 失败
    public static Result failure(ResultCode resultCode, Object data) {
        return new Result()
                .setResult(resultCode, data);
    }
}
