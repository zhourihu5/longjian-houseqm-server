package com.longfor.longjian.houseqm.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Houyan
 */
public class CollectionUtil {

    /**
     *  集合去重
     * @author hy
     * @return java.util.List
     */
    public static <T> List<T> removeDuplicate(List<T> list) {
        return new ArrayList<T>(new HashSet<T>(list));
    }
}
