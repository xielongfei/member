package com.wechat.jwt;

import com.alibaba.fastjson2.JSON;
import com.wechat.result.Response;
import com.wechat.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * @date: 2023/07/07
 * @description:
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // 设置响应的Content-Type为application/json
        response.setContentType("application/json");
        // 设置响应的字符编码为UTF-8
        response.setCharacterEncoding("UTF-8");

        // 将JSON字符串写入响应
        PrintWriter writer = response.getWriter();
        // 创建错误响应对象
        writer.write(JSON.toJSONString(Response.failure(ResultCode.UNAUTHORIZED)));
        writer.flush();
    }
}