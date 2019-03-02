package com.longfor.longjian.houseqm.config;

import java.util.Calendar;
import java.util.Date;

/**
 * @author lipeishuai
 * @date 2019/1/24 20:56
 */

public class LjTimeUtil {


    /**
     * 昨天的零点零分零秒
     *
     * @return
     */
    public static Date yesterdayZeroDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
