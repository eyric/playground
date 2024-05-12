package org.example.common.util;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class CollUtil extends cn.hutool.core.collection.CollUtil {

    /**
     * 计算两个集合的差集
     */
    public static <T> List<T> getDifference(List<T> list1, List<T> list2) {
        Set<T> set2 = new HashSet<>(list2);
        return list1.stream()
                .filter(element -> !set2.contains(element))
                .collect(Collectors.toList());
    }

    public static <T> T getLast(List<T> list) {
        if (isEmpty(list)) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    public static <T> T randomGet(List<T> list) {
        if (CollUtil.isEmpty(list)) {
            return null;
        }
        int idx = new Random().nextInt(list.size());
        return list.get(idx);
    }
}
