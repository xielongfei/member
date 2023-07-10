package com.wechat.util;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @description: 资源辅助类
 */
public final class ResourceHelper {
    /**
     * 以字符串方式获取资源
     *
     * @param clazz 类
     * @param name 资源名称
     * @return 字符串
     */
    public static <T> String getResourceAsString(Class<T> clazz, String name) {
        try (InputStream is = clazz.getClassLoader().getResourceAsStream(name)) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(String.format("以字符串方式获取资源(%s)异常", name), e);
        }
    }
}
