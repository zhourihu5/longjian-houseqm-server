package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.houseqmissue.HouseQmCheckTaskIssueVo;

import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.service
 * @ClassName: PushService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 17:52
 */
public interface PushService {

    void sendUPushByIssues(List<HouseQmCheckTaskIssueVo> issues);


}
