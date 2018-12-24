package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.*;

import java.util.Date;

public interface IHouseqmStatisticService {

    TaskStatVo.IssueStatVo getCheckTaskIssueTypeStatByTaskIdAreaId(Integer taskId, Integer areaId);

    TaskStatVo.HouseStatVo getHouseQmCheckTaskHouseStatByTaskId(Integer projectId, Integer taskId, Integer areaId);

    TaskRepairStatVo searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn);

    HouseqmStatisticCategoryIssueListRspMsgVo taskIssueRepairList(Integer projectId,Integer taskId,Integer areaId,Integer beginOn,Integer endOn,Integer timestamp,Integer planStatus,String source,Integer page,Integer pageSize);

}
