package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.TaskList2Vo;
import com.longfor.longjian.houseqm.app.vo.TaskRoleListVo;
import com.longfor.longjian.houseqm.app.vo.task.CheckTaskIssueTypeStatInfo;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;

import java.util.List;
import java.util.Map;

public interface ITaskListService {
    TaskList2Vo list(int teamId, int projectId, int categoryCls, int status);

    TaskRoleListVo taskRole(Integer taskId);

    Team getTopTeam(int teamId);

    Map<Integer, CheckTaskIssueTypeStatInfo> searchTaskIssueStatMapByTaskIds(List<Integer> taskIds);
}
