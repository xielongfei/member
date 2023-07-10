package com.wechat.util;

import java.util.List;
import java.util.Map;

/**
 * @description:
 */
public interface JsonProvider {

    Map parse(String var1);

    <K, V> Map<K, V> parse(String var1, Class<K> var2, Class<V> var3);

    <T extends Map, K, V> Map<K, V> parse(String var1, Class<T> var2, Class<K> var3, Class<V> var4);

    <T> T parse(String var1, Class<T> var2);

    <T> List<T> parseList(String var1, Class<T> var2);

    String convertObj(Object var1);

    <T> T convertObj(Object var1, Class<T> var2);
}
