package com.longfor.longjian.houseqm.util;

import java.text.DecimalFormat;

/**
 * @author Houyan
 * @date 2018/12/19 0019 17:56
 */
public class MathUtil {

    /**
     *  用于taskDetail() 计算百分比
     *  四舍五入
     * @param a
     * @param b
     * @return
     */
    public static String getPercentage(int a, int b) {
        if (a == 0 || b == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String result = df.format((float) a / (float) b * 100.0);
        return result;
    }

    public static String getPercentageByPattern(int a, int b,String pattern) {
        if (a == 0 || b == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        String result = df.format((float) a / (float) b * 100.0);
        return result;
    }

}
