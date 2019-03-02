package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.app.vo.task.HouseQmCheckTaskListAndTotalVo;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;

import java.util.List;
import java.util.Map;

public interface ITaskService {
    HouseQmCheckTaskRspVo getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId);

    List<Integer> getHouseqmCheckTaskCheckedAreas(Integer projectId, Integer taskId);

    void deleteHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId) throws Exception;

    List<UserInHouseQmCheckTask> searchUserInKeyHouseQmCheckTaskByTaskId(Integer taskId);

    Map<Integer, User> getUsersByIds(List<Integer> uids);

    HouseQmCheckTaskListAndTotalVo searchHouseQmCheckTaskByProjCategoryClsStatusPage(Integer projId, Integer categoryCls, Integer status, Integer page, Integer pageSize);
}
