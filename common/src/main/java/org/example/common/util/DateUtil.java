package org.example.common.util;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateUtil extends cn.hutool.core.date.DateUtil {

    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss,SSS";

    /**
     * 将时间戳转换为指定格式的日期时间字符串
     *
     * @param timestamp 时间戳（以毫秒为单位）
     * @return 格式化后的日期时间字符串
     */
    public static String format(long timestamp, String dateTimeFormat) {
        try {
            return DateFormatUtils.format(timestamp, dateTimeFormat);
        } catch (Exception e) {
            return "";
        }
    }

    public static String format(Long timestamp) {
        return format(timestamp, DATE_FORMAT_YYYY_MM_DD_HH_MM_SS_SSS);
    }

    public static Long thisTimeMills() {
        return LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
    }

    public static int currentHour() {
        ZoneId beijingZone = ZoneId.of("Asia/Shanghai");
        LocalDateTime nowInBeijing = LocalDateTime.now(beijingZone);
        return nowInBeijing.getHour();
    }
}
