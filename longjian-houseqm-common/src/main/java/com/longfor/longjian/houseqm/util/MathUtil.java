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
        BigDecimal decimalb= new BigDecimal(b);
        BigDecimal divide = decimala.divide(decimalb,4,BigDecimal.ROUND_HALF_UP);
        BigDecimal perc = new BigDecimal(100);
        perc.setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal multiply = divide.multiply(perc);
        /*DecimalFormat df = new DecimalFormat("0.00");
        String result = df.format((float) a / (float) b * 100.0);*/
        return  formatToNumber(multiply);
    }

    /**
     * @Author hy
     * @Description 格式化
     * @Date 14:48 2019/1/22
     * @Param [a, b, pattern]
     * @return java.lang.String
     **/
    public static String getPercentageByPattern(int a, int b, String pattern) {
        if (a == 0 || b == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        String result = df.format((float) a / (float) b * 100.0);
        return result;
    }

    /**
     * @Author hy
     * @Description 格式化数字 0.00
     * @Date 16:04 2019/1/15
     * @Param [obj]
     * @return java.lang.String
     **/
    public static String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if(obj.compareTo(BigDecimal.ZERO)==0) {
            return "0.00";
        }else if(obj.compareTo(BigDecimal.ZERO)>0&&obj.compareTo(new BigDecimal(1))<0){
            return "0"+df.format(obj);
        }else {
            return df.format(obj);
        }
    }


}
