package com.longfor.longjian.houseqm.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author Houyan
 * @date 2018/12/19 0019 17:56
 */
public class MathUtil {

    /**
     * 用于taskDetail() 计算百分比
     * 四舍五入
     *
     * @param a
     * @param b
     * @return
     */
    public static String getPercentage(int a, int b) {
        if (a == 0 || b == 0) {
            return "0.00";
        }
        BigDecimal decimala = new BigDecimal(a);
        BigDecimal decimalb = new BigDecimal(b);
        BigDecimal divide = decimala.divide(decimalb, 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal perc = new BigDecimal(100);
        perc = perc.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply = divide.multiply(perc);
        return formatToNumber(multiply);
    }

    /**
     * @return java.lang.String
     * @Author hy
     * @Description 格式
     * @Date 15:38 2019/1/28
     * @Param [a, b]
     **/
    public static String getPercentage2(int a, int b) {
        if (a == 0 || b == 0) {
            return "0";
        }
        BigDecimal decimala = new BigDecimal(a);
        BigDecimal decimalb = new BigDecimal(b);
        BigDecimal divide = decimala.divide(decimalb, 4, BigDecimal.ROUND_HALF_UP);
        BigDecimal perc = new BigDecimal(100);
        perc = perc.setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply = divide.multiply(perc);
        return formatToNumber(multiply);
    }

    /**
     * @return java.lang.String
     * @Author hy
     * @Description 格式化
     * @Date 14:48 2019/1/22
     * @Param [a, b, pattern]
     **/
    public static String getPercentageByPattern(int a, int b, String pattern) {
        if (a == 0 || b == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format((float) a / (float) b * 100.0);
    }

    /**
     * @return java.lang.String
     * @Author hy
     * @Description 格式化数字 0.00
     * @Date 16:04 2019/1/15
     * @Param [obj]
     **/
    public static String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (obj.compareTo(BigDecimal.ZERO) == 0) {
            return "0.00";
        } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
            return "0" + df.format(obj);
        } else {
            return df.format(obj);
        }
    }


}
