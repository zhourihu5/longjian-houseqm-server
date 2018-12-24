package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.HouseQmIssueCategoryStatVo;
import com.longfor.longjian.houseqm.app.vo.TaskRepairStatVo;
import com.longfor.longjian.houseqm.app.vo.TaskStatVo;

import java.util.Date;
import java.util.List;

public interface IHouseqmStatisticService {

    TaskStatVo.IssueStatVo getCheckTaskIssueTypeStatByTaskIdAreaId(Integer taskId, Integer areaId);

    TaskStatVo.HouseStatVo getHouseQmCheckTaskHouseStatByTaskId(Integer projectId, Integer taskId, Integer areaId);

    TaskRepairStatVo searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn);

    List<HouseQmIssueCategoryStatVo> searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns);

    HouseqmStatisticCategoryIssueListRspMsgVo taskIssueRepairList(Integer projectId,Integer taskId,Integer areaId,Integer beginOn,Integer endOn,Integer timestamp,Integer planStatus,String source,Integer page,Integer pageSize);

    IssueRepairStatisticVo projectIssueRepair(Integer projectId,String source,Integer areaId,Integer beginOn,Integer endOn,Integer timestamp);
}
