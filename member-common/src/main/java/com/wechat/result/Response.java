package com.wechat.result;

import com.wechat.entity.Members;
import com.wechat.util.ConstantsUtil;

import java.util.List;

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
        //preHandle(data);
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

    public static void preHandle(Object data) {
        // 检查返回类型是否为Member或Member列表类型
        if (data instanceof Members) {
            Members member = (Members) data;
            member.setFilePath(addExtraInfo(member.getFilePath()));
        } else if (data instanceof List<?>) {
            List<?> list = (List<?>) data;
            for (Object obj : list) {
                if (obj instanceof Members) {
                    Members member = (Members) obj;
                    member.setFilePath(addExtraInfo(member.getFilePath()));
                }
            }
        }
    }

    // 添加额外信息的方法
    private static String addExtraInfo(String url) {
        // 在这里添加额外的信息到url字段
        return ConstantsUtil.serviceUrl + url;
    }
}
