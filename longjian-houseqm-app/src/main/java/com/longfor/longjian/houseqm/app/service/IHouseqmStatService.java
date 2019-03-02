package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.InspectionHouseStatusInfoVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.StatCategoryStatRspVo;

import java.util.Date;
import java.util.List;

public interface IHouseqmStatService {

    CheckerStatListVo searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds);

    ProjectDailyListVo searchTaskSituationDailyByProjTaskIdInOnPage(Integer projectId, List<Integer> taskIdList, Integer pageNum, Integer pageSize);

    ProjectOveralListVo.ProjectOveralVo getInspectTaskStatByProjTaskId(Integer projectId, Integer taskId);

    TaskAreaListVo searchAreasByProjTaskIdTyp(Integer projectId, Integer taskId, int typ);

    AreaTaskListVo searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(Integer projectId, Integer areaId, List<Integer> categoryCls);

    List<HouseQmStatTaskDetailMemberCheckerRspVo> searchCheckerIssueStatusStatByProjTaskIdBetweenTime(Integer projectId, Integer taskId, Date start, Date end);

    List<HouseQmStatTaskDetailMemberRepairerRspVo> searchRepaireIssueStatusStatByProjTaskIdBetweenTime(Integer projectId, Integer taskId, Date start, Date end);

    HouseQmStatAreaSituationIssueRspVo getAreaIssueTypeStatByProjectIdAreaIdCategoryCls(Integer projectId, Integer areaId, Integer categoryCls) throws Exception;

    StatCategoryStatRspVo searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn);

    List<Integer> searchRepossessInspectionAreaIdsByConditions(Integer projectId, Integer taskId, Integer areaId, Integer status, Integer issueStatus, Date startTime, Date endTime);

    List<Integer> searchInspectionAreaIdsByConditions(Integer projectId, Integer taskId, Integer areaId, Integer status, Integer issueStatus);

    List<InspectionHouseStatusInfoVo> formatFenhuHouseInspectionStatusInfoByAreaIds(Integer taskId, List<Integer> ids);
}
