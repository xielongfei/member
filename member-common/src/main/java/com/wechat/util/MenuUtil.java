package com.wechat.util;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: xielongfei
 * @date: 2023/07/10
 * @description:
 */
@Component
public class MenuUtil {

    public static final Map<Integer, Object> menuMap = new HashMap<>();

    public MenuUtil() {
        String json = ResourceHelper.getResourceAsString(getClass(), "auth.json");
        JSONArray jsonArray = JSON.parseArray(json);
        for (Object obj : jsonArray) {
            JSONObject jsonObject = (JSONObject) obj;
            int memberTypeId = jsonObject.getIntValue("memberTypeId");
            menuMap.put(memberTypeId, jsonObject);
        }
    }
}
