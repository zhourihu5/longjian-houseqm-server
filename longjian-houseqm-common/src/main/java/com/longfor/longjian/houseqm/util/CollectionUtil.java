package com.longfor.longjian.houseqm.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author Houyan
 */
public class CollectionUtil {

    private CollectionUtil(){}

    /**
     * 集合去重
     *
     * @return java.util.List
     * @author hy
     */
    public static <T> List<T> removeDuplicate(List<T> list) {
        return new ArrayList<>(new HashSet<>(list));
    }
}
