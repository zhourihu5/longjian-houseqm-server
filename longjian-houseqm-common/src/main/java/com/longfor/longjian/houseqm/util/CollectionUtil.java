package com.longfor.longjian.houseqm.util;

import java.util.HashSet;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/22 0022 15:13
 */
public class CollectionUtil {

    /**
     *  集合去重
     * @author hy
     * @date 2018/12/22 0022
     * @param list
     * @return java.util.List
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }
}
