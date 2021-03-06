package com.longfor.longjian.houseqm.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private DateUtil(){

    }
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
    private  static final String YMDHMS="yyyy-MM-dd HH:mm:ss";
    private  static final String ERROR=  "error:";
    public static final boolean datetimeZero(Date date) {
        //315532800000L=dateFormat.parse("1980-01-01 08:00:00").getTime()
        return date == null || date.getTime() <= 315532800000L;
    }

    public static final String formatBySec(Date date) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(YMDHMS);
        return dateFormat.format(date);
    }

    public static String dateToString(Date date, String formatStr) {
        if (date == null) {
            return "";
        } else {
            if (StringUtils.isEmpty(formatStr)) {
                formatStr = YMDHMS;
            }

            DateTime dateTime = new DateTime(date);
            return dateTime.toString(formatStr);
        }
    }

    public static final boolean datetimeBefore(Date date, Date date2) {
        return date.getTime() < date2.getTime();
    }

    public static Date strToDate(String str, String pattern) {
        Date date = null;
        try {
            if (StringUtils.isNotEmpty(str)) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                date = sdf.parse(str);
            }
        } catch (ParseException e) {
            logger.error(ERROR, e.getMessage());
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
            logger.error(ERROR, e.getMessage());
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
            SimpleDateFormat formatter = new SimpleDateFormat(YMDHMS);
            String strdt = formatter.format(dt);
            Date initDate = null;
            try {
                initDate = formatter.parse("1980-01-01 08:00:00");
            } catch (ParseException e) {
                logger.error(ERROR, e.getMessage());
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
        long timeLong = timestamp * 1000l;
        DateFormat ymdhmsFormat = new SimpleDateFormat(partten);
        String nowTimeStr = ymdhmsFormat.format(timeLong);
        Date timeDate = null;
        try {
            timeDate = ymdhmsFormat.parse(nowTimeStr);
        } catch (ParseException e) {
            logger.error(ERROR, e.getMessage());
        }
        return timeDate;
    }

    /**
     * long -> Date
     *
     * @param
     * @return java.util.Date
     * @author hy
     * @date 2018/12/22 0022
     */
    public static Date timeStampTwoToDate(long timestamp, String partten) {
        SimpleDateFormat  ymdhmsFormat = new SimpleDateFormat (partten);
        String nowTimeStr = ymdhmsFormat.format(timestamp);
        Date timeDate = null;
        try {
            timeDate = ymdhmsFormat.parse(nowTimeStr);
        } catch (ParseException e) {
            logger.error(ERROR,e.getMessage());
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
        return new SimpleDateFormat(partten).format(now);
    }

    // 时间戳转日期
    public static Date transForDate(Integer ms) {
        if (ms == null) {
            ms = 0;
        }
        long msl = (long) ms * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS);
        Date temp = null;
        try {
            String str = sdf.format(msl);
            temp = sdf.parse(str);
        } catch (ParseException e) {
            logger.error(ERROR, e.getMessage());
        }
        return temp;
    }

    public static Date dateAddDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

}
