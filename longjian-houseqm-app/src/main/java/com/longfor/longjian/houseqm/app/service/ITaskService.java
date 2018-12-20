package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;

import java.util.List;

public interface ITaskService {
    HouseQmCheckTaskRspVo getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId);

    List<Integer> getHouseqmCheckTaskCheckedAreas(Integer projectId, Integer taskId);

}
