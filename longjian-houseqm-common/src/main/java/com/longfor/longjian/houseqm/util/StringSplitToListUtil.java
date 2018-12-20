package com.longfor.longjian.houseqm.util;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/19 0019 16:56
 */
public class StringSplitToListUtil {


    /**
     * 字符串分割 转换为int类型的
     *
     * @param ids
     * @param sep 分割符
     * @return
     */
    public static List<Integer> splitToIdsComma(String ids, String sep) {
        List<Integer> list = Lists.newArrayList();
        ids.trim();
        String[] str = ids.split(sep);
        List<String> areaList = Arrays.asList(str);
        for (String s : areaList) {
            if (s.equals("")) {
                continue;
            }
            list.add(Integer.valueOf(s));
        }
        return list;
    }
}
