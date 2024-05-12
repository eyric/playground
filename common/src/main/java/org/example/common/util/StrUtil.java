package org.example.common.util;

import cn.hutool.core.util.ArrayUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StrUtil extends cn.hutool.core.util.StrUtil {

    /**
     * 字符串数组是否不为空
     */
    public static boolean isNotEmpty(String[] values) {
        return values != null && values.length > 0;
    }

    /**
     * 字符串数组是否为空
     */
    public static boolean isEmpty(String[] values) {
        return values == null || values.length == 0;
    }

    /**
     * 对象连接
     */
    public static String join(String delimiter, Object... objs) {

        return ArrayUtil.join(objs, delimiter);
    }

    public static <T> List<T> split(String s, String delimiter, Class<T> clazz) {
        if (isEmpty(s) || isEmpty(delimiter)) {
            return List.of();
        }
        List<String> list = split(s, delimiter);
        if (CollUtil.isEmpty(list)) {
            return List.of();
        }
        return list.stream().map(item -> JSON.parseObject(item, clazz)).collect(Collectors.toList());
    }

    public static String filterEmoji(String inputText) {
        String regex = "[\\x{1F600}-\\x{1F64F}\\x{1F300}-\\x{1F5FF}\\x{1F680}-\\x{1F6FF}\\x{1F700}-\\x{1F77F}\\x" +
                "{1F780}-\\x{1F7FF}\\x{1F800}-\\x{1F8FF}\\x{1F900}-\\x{1F9FF}\\x{1FA00}-\\x{1FA6F}\\x{1FA70}-\\x" +
                "{1FAFF}\\x{1FAB0}-\\x{1FABF}\\x{2B50}\\x{1F004}\\x{1F0CF}\\x{1F170}-\\x{1F251}\\x{1F004}\\x{2702}\\x{1F004}]+";
        Pattern pattern = Pattern.compile(regex, Pattern.UNICODE_CHARACTER_CLASS);
        Matcher matcher = pattern.matcher(inputText);
        return matcher.replaceAll("");
    }

    public static String splitAndGetLast(String str, String delimiter) {
        if (isEmpty(str)) {
            return str;
        }

        String[] arr = str.split(delimiter);
        return arr[arr.length - 1];
    }

}
