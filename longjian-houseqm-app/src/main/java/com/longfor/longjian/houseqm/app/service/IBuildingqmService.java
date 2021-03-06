package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.req.TaskEditReq;
import com.longfor.longjian.houseqm.app.req.TaskReq;
import com.longfor.longjian.houseqm.app.vo.ApiIssueLogVo;
import com.longfor.longjian.houseqm.app.vo.MyIssuePatchListVo;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.app.vo.TaskMemberListVo;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskSquad;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IBuildingqmService {

    TaskListVo myTaskList(Integer userId);

    TaskMemberListVo taskSquadsMembers(String taskIdsStr);

    MyIssuePatchListVo myIssuePathList(int userId, int taskId, int timestamp);


    void create(Integer uid, TaskReq taskReq);

    List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId);

    void edit(Integer uid, TaskEditReq taskEditReq);

    ApiIssueLogVo getIssueListLogByLastIdAndUpdataAt(Integer taskId, Integer timestamp, String issueUuid);

    Map<String, Object> issuestatisticexport(Integer categoryCls, String items, HttpServletResponse response);
}
