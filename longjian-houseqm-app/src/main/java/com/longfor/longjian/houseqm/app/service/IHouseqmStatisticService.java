package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.TaskRepairStatVo;
import com.longfor.longjian.houseqm.app.vo.TaskStatVo;

import java.util.Date;

public interface IHouseqmStatisticService {

    TaskStatVo.IssueStatVo getCheckTaskIssueTypeStatByTaskIdAreaId(Integer taskId, Integer areaId);

    TaskStatVo.HouseStatVo getHouseQmCheckTaskHouseStatByTaskId(Integer projectId, Integer taskId, Integer areaId);

    TaskRepairStatVo searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn);

}
