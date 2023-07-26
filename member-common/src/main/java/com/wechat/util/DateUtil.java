package com.wechat.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author: xielongfei
 * @date: 2023/07/19
 * @description:
 */
public class DateUtil {

    // 定义日期格式化器，仅包含月份和日期
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

    public static final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static String getMMDD(LocalDate localDate) {
        return localDate.format(formatter);
    }
}
