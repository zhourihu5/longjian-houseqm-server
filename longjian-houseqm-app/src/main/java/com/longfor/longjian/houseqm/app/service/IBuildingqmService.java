package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.MyIssuePatchListVo;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.app.vo.TaskMemberListVo;

public interface IBuildingqmService {
    TaskListVo myTaskList(Integer userId);

    TaskMemberListVo taskSquadsMembers(String taskIdsStr);

    MyIssuePatchListVo myIssuePathList(int userId, int taskId, int timestamp);


}
