package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.*;

import java.util.List;

public interface IHouseqmStatService {

    CheckerStatListVo searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds);

    ProjectDailyListVo searchTaskSituationDailyByProjTaskIdInOnPage(Integer projectId, List<Integer> taskIdList, Integer pageNum, Integer pageSize);

    ProjectOveralListVo.ProjectOveralVo getInspectTaskStatByProjTaskId(Integer projectId, Integer taskId);

    TaskAreaListVo searchAreasByProjTaskIdTyp(Integer projectId, Integer taskId);

    AreaTaskListVo searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(Integer projectId, Integer areaId, List<Integer> categoryCls);



}
