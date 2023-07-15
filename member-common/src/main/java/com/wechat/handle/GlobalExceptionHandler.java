package com.wechat.handle;

import com.alibaba.fastjson2.JSON;
import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @date: 2023/07/12
 * @description:
 */
@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public void handleException(HttpServletResponse response, Exception ex) throws IOException {
        log.error("系统异常捕获, ", ex);
        ResultCode resultCode = ResultCode.INTERNAL_SERVER_ERROR;
        // 处理异常逻辑，例如记录日志、返回特定的错误消息等
        if (ex instanceof IllegalArgumentException) {
            resultCode = ResultCode.PARAMS_IS_INVALID;
        }
        if (ex instanceof NullPointerException) {
            resultCode = ResultCode.NULL_POINT;
        }

        // 设置响应的Content-Type为application/json
        response.setContentType("application/json");
        // 设置响应的字符编码为UTF-8
        response.setCharacterEncoding("UTF-8");

        // 将JSON字符串写入响应
        PrintWriter writer = response.getWriter();
        // 创建错误响应对象
        writer.write(JSON.toJSONString(Response.failure(resultCode)));
        writer.flush();
    }
}