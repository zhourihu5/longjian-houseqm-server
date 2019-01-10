package com.longfor.longjian.houseqm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Houyan
 * @date 2018/12/19 0019 17:28
 */
public class DateUtil {


    public static Date strToDate(String str, String pattern) {
        Date date = null;
        try {
            if (str.length()>0){
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                date = sdf.parse(str);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 时间比较排序
     *
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
     * Date to int
     *
     * @param dt
     * @return
     */
    public static int datetimeToTimeStamp(Date dt) {
        if (dt == null) {
            return 0;
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strdt = formatter.format(dt);
            Date initDate = null;
            try {
                initDate = formatter.parse("1980-01-01 08:00:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (strdt.equals("0001-01-01 00:00:00") || strdt.equals("") || !dt.after(initDate)) {
                return 0;
            } else {
                return (int) (dt.getTime() / 1000);
            }
        }
    }

    /**
     * int -> Date
     *
     * @param
     * @return java.util.Date
     * @author hy
     * @date 2018/12/22 0022
     */
    public static Date timeStampToDate(int timestamp, String partten) {
        long timeLong = new Long(timestamp).longValue() * 1000;
        DateFormat ymdhmsFormat = new SimpleDateFormat(partten);
        String nowTimeStr = ymdhmsFormat.format(timeLong);
        Date timeDate = null;
        try {
            timeDate = ymdhmsFormat.parse(nowTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeDate;
    }

    /**
     * 获得当前时间以 partten 格式
     *
     * @param partten
     * @return java.lang.String
     * @author hy
     * @date 2018/12/24 0024
     */
    public static String getNowTimeStr(String partten) {
        Date now = new Date();
        String nowStr = new SimpleDateFormat(partten).format(now);
        return nowStr;
    }

    // 时间戳转日期
    public static Date transForDate(Integer ms) {
        if (ms == null) {
            ms = 0;
        }
        long msl = (long) ms * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = null;
        if (ms != null) {
            try {
                String str = sdf.format(msl);
                temp = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public static Date dateAddDay(Date date,int day){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH,day);
        Date time = c.getTime();
        return time;
    }

}
