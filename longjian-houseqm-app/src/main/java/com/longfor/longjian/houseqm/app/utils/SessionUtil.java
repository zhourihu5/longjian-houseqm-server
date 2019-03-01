package com.longfor.longjian.houseqm.app.utils;

import com.longfor.longjian.common.util.SessionInfo;

import javax.annotation.Resource;

/**
 * Created by Wang on 2019/3/1.
 */
public class SessionUtil {

    public static Integer getUid(SessionInfo sessionInfo){

        Integer uid = (Integer) sessionInfo.getBaseInfo("userId");

        return uid;
    }
}
