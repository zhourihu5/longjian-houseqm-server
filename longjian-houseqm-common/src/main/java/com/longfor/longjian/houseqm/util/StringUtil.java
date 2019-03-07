package com.longfor.longjian.houseqm.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhouxingjia
 */
public class StringUtil {
    private StringUtil(){

    }

    public static List<Integer> strToInts(String str, String split) {
        if (StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        split = ".".equals(split) ? "\\." : split;
        Stream<String> sids = Arrays.stream(str.split(split));
        return sids.filter(StringUtils::isNotBlank).map(Integer::parseInt).collect(Collectors.toList());
    }

    public static List<String> strToStrs(String str, String split) {
        if (StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        split = ".".equals(split) ? "\\." : split;
        Stream<String> sids = Arrays.stream(str.split(split));
        return sids.filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

}
