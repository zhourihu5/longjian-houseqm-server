package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Wang on 2019/2/28.
 */
public interface ScanMsgPushService {

    LjBaseResponse scanNoticeCenter(String categoryCls);

    void sendUPush(String title, String msg, Integer taskId, List<Integer> userIds, Integer appFlag);

     Map<Integer,User> createUserMap(List<Integer>noticeUserIds);
}
