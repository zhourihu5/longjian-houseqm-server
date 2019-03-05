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

    public static List<Float> strToFloats(String str, String split) {
        if (StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        split = ".".equals(split) ? "\\." : split;
        Stream<String> sids = Arrays.stream(str.split(split));
        return sids.filter(StringUtils::isNotBlank).map(Float::parseFloat).collect(Collectors.toList());
    }

    public static List<String> strToStrs(String str, String split) {
        if (StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        split = ".".equals(split) ? "\\." : split;
        Stream<String> sids = Arrays.stream(str.split(split));
        return sids.filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    /**
     * 去掉字符串首尾的指定字符
     *
     * @param source  需要处理的字符串
     * @param element 需要去掉的字符
     * @return
     */
    public static String trimFirstAndLastChar(String source, char element) {
        if (source.equals(String.valueOf(element))) {
            return source;
        }
        boolean beginIndexFlag;
        boolean endIndexFlag;
        do {
            int beginIndex = source.indexOf(element) == 0 ? 1 : 0;
            int endIndex = source.lastIndexOf(element) + 1 == source.length() ? source.lastIndexOf(element) : source.length();
            source = source.substring(beginIndex, endIndex);
            beginIndexFlag = (source.indexOf(element) == 0);
            endIndexFlag = (source.lastIndexOf(element) + 1 == source.length());
        } while (beginIndexFlag || endIndexFlag);
        return source;
    }

}
