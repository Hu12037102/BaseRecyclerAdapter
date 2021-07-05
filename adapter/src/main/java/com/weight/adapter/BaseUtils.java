package com.weight.adapter;

import java.util.List;

/**
 * 作者: 胡庆岭
 * 创建时间: 2021/7/4 10:03
 * 更新时间: 2021/7/4 10:03
 * 描述:
 */
public class BaseUtils {
    public static <T> boolean isEmptyList(List<T> list) {
        return list == null || list.size() == 0;
    }

    public static <T> int getListSize(List<T> list) {
        return isEmptyList(list) ? 0 : list.size();
    }
}
