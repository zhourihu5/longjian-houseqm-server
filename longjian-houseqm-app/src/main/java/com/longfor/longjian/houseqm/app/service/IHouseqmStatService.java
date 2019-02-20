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

    TaskAreaListVo searchAreasByProjTaskIdTyp(Integer projectId, Integer taskId,int typ);

    AreaTaskListVo searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(Integer projectId, Integer areaId, List<Integer> categoryCls);

    List<HouseQmStatTaskDetailMemberCheckerRspVo> searchCheckerIssueStatusStatByProjTaskIdBetweenTime(Integer project_id, Integer task_id, Date start, Date end);

    List<HouseQmStatTaskDetailMemberRepairerRspVo> searchRepaireIssueStatusStatByProjTaskIdBetweenTime(Integer project_id, Integer task_id, Date start, Date end);

    HouseQmStatAreaSituationIssueRspVo getAreaIssueTypeStatByProjectIdAreaIdCategoryCls(Integer project_id, Integer area_id, Integer category_cls) throws Exception;

    StatCategoryStatRspVo searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(Integer project_id, Integer task_id, Integer area_id, Date beginOn, Date endOn);

    List<Integer> searchRepossessInspectionAreaIdsByConditions(Integer project_id, Integer task_id, Integer area_id, Integer status, Integer issue_status, Date startTime, Date endTime);

    List<Integer> searchInspectionAreaIdsByConditions(Integer project_id, Integer task_id, Integer area_id, Integer status, Integer issue_status);

    List<InspectionHouseStatusInfoVo> formatFenhuHouseInspectionStatusInfoByAreaIds(Integer task_id, List<Integer> ids);
}
