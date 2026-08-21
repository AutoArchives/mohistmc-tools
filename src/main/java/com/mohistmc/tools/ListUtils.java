package com.mohistmc.tools;

import java.util.HashSet;
import java.util.List;

/**
 * @author Mgazul by MohistMC
 * @date 2023/7/27 14:37:28
 */
public class ListUtils {

    /**
     * 仅当 key 不存在时才加入 list（去重添加）。
     */
    public static void addIfAbsent(List<String> list, String key) {
        if (!list.contains(key)) {
            list.add(key);
        }
    }

    /**
     * 判断 list1 中的元素是否全部存在于 list2 中（且两者大小一致）。
     */
    public static boolean containsAll(List<Object> list1, List<Object> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        return new HashSet<>(list2).containsAll(list1);
    }
}
