package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.req.TaskReq;
import com.longfor.longjian.houseqm.app.vo.MyIssuePatchListVo;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.app.vo.TaskMemberListVo;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;

import java.util.List;

public interface IBuildingqmService {
    TaskListVo myTaskList(Integer userId);

    TaskMemberListVo taskSquadsMembers(String taskIdsStr);

    MyIssuePatchListVo myIssuePathList(int userId, int taskId, int timestamp);


    void create(Integer uid, TaskReq taskReq);

    List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId);
}
