package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskVo;
import com.longfor.longjian.houseqm.app.vo.task.HouseQmCheckTaskListAndTotalVo;
import com.longfor.longjian.houseqm.po.User;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;

import java.util.List;
import java.util.Map;

public interface ITaskService {
    HouseQmCheckTaskRspVo getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId);

    List<Integer> getHouseqmCheckTaskCheckedAreas(Integer projectId, Integer taskId);

    void deleteHouseQmCheckTaskByProjTaskId(Integer project_id, Integer task_id) throws Exception;

    List<UserInHouseQmCheckTask> searchUserInKeyHouseQmCheckTaskByTaskId(Integer task_id);

    Map<Integer, User> getUsersByIds(List<Integer> uids);

    HouseQmCheckTaskListAndTotalVo searchHouseQmCheckTaskByProjCategoryClsStatusPage(Integer projId, Integer category_cls, Integer status, Integer page, Integer page_size);
}
