package com.longfor.longjian.houseqm.util;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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

    /**
     *  字符串转换成对应的List String泛型
     * @author hy
     * @date 2018/12/21 0021
     * @param ids
     * @param sep
     * @return java.util.List<java.lang.String>
     */
    public static List<String> splitToStringComma(String ids,String sep){
        String[] str = ids.split(sep);
        List<String> list = Arrays.asList(str);
        return list;
    }

    /**
     * 集合数组 按给定的sep 拼接成字符串
     * @param list
     * @return
     */
    public static String dataToString(List list,String sep) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size(); i++) {
            if (sb.length() > 0) {//该步即不会第一位有sep，也防止最后一位拼接sep！
                sb.append(sep);
            }
            sb.append(list.get(i));
        }
        return sb.toString();
    }

    /**
     *  判断字符是否为数字类型
     * @author hy
     * @date 2018/12/21 0021
     * @param str
     * @return boolean
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

}
