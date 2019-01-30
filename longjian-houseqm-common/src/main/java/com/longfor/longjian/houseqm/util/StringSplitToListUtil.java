package com.longfor.longjian.houseqm.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Houyan
 * @date 2018/12/19 0019 16:56
 */
public class StringSplitToListUtil {

    /**
     * @Author hy
     * @Description 统计sep在str中出现的次数 非重叠次数 对应go源码中的strings.count(str,sep)
     * @Date 10:51 2019/1/30
     * @Param [str, sep]
     * @return int
     **/
    public static int count(String str, String sep) {
        int fromIndex = 0;
        int count = 0;
        while (true) {
            int index = str.indexOf(sep, fromIndex);
            if (-1 != index) {
                fromIndex = index + 1;
                count++;
            } else {
                break;
            }
        }

        return count;
    }

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

    /**
     * 字符串转换成对应的List String泛型
     *
     * @param ids
     * @param sep
     * @return java.util.List<java.lang.String>
     * @author hy
     * @date 2018/12/21 0021
     */
    public static List<String> splitToStringComma(String ids, String sep) {
        String[] str = ids.split(sep);
        ArrayList<String> list = Lists.newArrayList();
        for (String s : str) {
            list.add(s);
        }
        return list;
    }

    /**
     * 集合数组 按给定的sep 拼接成字符串
     *
     * @param list
     * @return
     */
    public static String dataToString(List list, String sep) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (sb.length() > 0) {//该步即不会第一位有sep，也防止最后一位拼接sep！
                sb.append(sep);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     * 判断字符是否为数字类型
     *
     * @param str
     * @return boolean
     * @author hy
     * @date 2018/12/21 0021
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }


    /**
     * 移除首尾指定字符 并以指定字符分割。
     * @param str
     * @param removeSep
     * @param splitSep
     * @return java.util.List<java.lang.String>
     * @author hy
     * @date 2018/12/22 0022
     */
    public static List<String> removeStartAndEndStrAndSplit(String str, String removeSep, String splitSep) {
        String s = StringUtils.removeStart(str, removeSep);
        String s1 = StringUtils.removeEnd(s, removeSep);
        List<String> strings = splitToStringComma(s1, splitSep);
        return strings;
    }

    public static List<Integer> strToInts(String str, String split) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        split = ".".equals(split) ? "\\." : split;
        Stream<String> sids = Arrays.stream(str.split(split));
        return sids.filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(Integer::parseInt).collect(Collectors.toList());
    }

    public static List<Float> strToFloats(String str, String split) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        split = ".".equals(split) ? "\\." : split;
        Stream<String> sids = Arrays.stream(str.split(split));
        return sids.filter(org.apache.commons.lang3.StringUtils::isNotBlank).map(Float::parseFloat).collect(Collectors.toList());
    }

    public static List<String> strToStrs(String str, String split) {
        if (org.apache.commons.lang3.StringUtils.isBlank(str)) {
            return Lists.newArrayList();
        }
        split = ".".equals(split) ? "\\." : split;
        Stream<String> sids = Arrays.stream(str.split(split));
        return sids.filter(org.apache.commons.lang3.StringUtils::isNotBlank).collect(Collectors.toList());
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
