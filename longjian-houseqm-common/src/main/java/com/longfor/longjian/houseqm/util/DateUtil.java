package com.longfor.longjian.houseqm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Houyan
 * @date 2018/12/19 0019 17:28
 */
public class DateUtil {

    /**
     * 时间比较排序
     * @param s1
     * @param s2
     * @return
     */
    public static long dateCompare(String s1, String s2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = sdf.parse(s1);
            Date d2 = sdf.parse(s2);
            return ((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     *  Date to int
     * @param dt
     * @return
     */
    public static int datetimeToTimeStamp(Date dt){
        if (dt==null){
            return 0;
        }else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strdt = formatter.format(dt);
            Date initDate = null;
            try {
                initDate = formatter.parse("1980-01-01 08:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (strdt.equals("0001-01-01 00:00:00")||strdt.equals("")||!dt.after(initDate)){
                return 0;
            }else{
                return (int) (dt.getTime()/1000);
            }
        }
    }

}
