package com.wechat.handle;

import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @date: 2023/07/12
 * @description:
 */
@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public Object handleException(Exception ex) {
        log.error("系统异常捕获, ", ex);
        // 处理异常逻辑，例如记录日志、返回特定的错误消息等
        if (ex instanceof IllegalArgumentException) {
            return Response.failure(ResultCode.PARAMS_IS_INVALID);
        }
        if (ex.getCause() instanceof NullPointerException) {
            return Response.failure(ResultCode.BAD_REQUEST);
        }
        return Response.failure(ResultCode.INTERNAL_SERVER_ERROR);
    }
}