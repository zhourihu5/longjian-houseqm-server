package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.common.util.SessionInfo;


/**
 * Created by Wang on 2019/3/1.
 */
public class SessionUtil {

    public static Integer getUid(SessionInfo sessionInfo){

        return (Integer) sessionInfo.getBaseInfo("userId");
    }
}
