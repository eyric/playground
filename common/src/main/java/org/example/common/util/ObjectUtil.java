package org.example.common.util;

import java.util.List;

public class ObjectUtil extends cn.hutool.core.util.ObjectUtil {

    private static final List<String> FUZZY_EMPTY_STR_LIST = List.of("", "0", "null", "{}", "[]");

    public static boolean fuzzyEmpty(Object o) {
        if (isEmpty(o)) {
            return true;
        }

        return FUZZY_EMPTY_STR_LIST.contains(String.valueOf(o));
    }

    public static boolean fuzzyEqual(Object o1, Object o2) {
        if (equal(o1, o2)) {
            return true;
        }

        return fuzzyEmpty(o1) && fuzzyEmpty(o2);
    }
}
