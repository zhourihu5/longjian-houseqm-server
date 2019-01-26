package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.HouseQmIssueCategoryStatVo;
import com.longfor.longjian.houseqm.app.vo.TaskRepairStatVo;
import com.longfor.longjian.houseqm.app.vo.TaskStatVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectCheckerStatRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectIssueStatRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectListRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.ProjectRepairerStatRspVo;
import com.longfor.longjian.houseqm.po.Area;

import java.util.Date;
import java.util.List;

public interface IHouseqmStatisticService {

    ProjectRepairerStatRspVo projectRepairerStat(int uid, Integer project_id, Integer task_id, String source, Integer stat_begin, Integer stat_end, Integer timestamp);

    ProjectCheckerStatRspVo projectCheckerStat(int uid, Integer project_id, Integer task_id, String source, Integer stat_begin, Integer stat_end, Integer timestamp);

    ProjectIssueStatRspVo projectIssueStat(int uid, Integer project_id, String source, Integer area_id, Integer timestamp);

    ProjectListRspVo projectList(int uid, String source, Integer timestamp);

    TaskStatVo.IssueStatVo getCheckTaskIssueTypeStatByTaskIdAreaId(Integer taskId, Integer areaId);

    TaskStatVo.HouseStatVo getHouseQmCheckTaskHouseStatByTaskId(Integer projectId, Integer taskId, Integer areaId);

    TaskRepairStatVo searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn);

    List<HouseQmIssueCategoryStatVo> searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns);

    HouseQmCheckTaskIssueVoRsp searchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged(Integer projectId, String categoryKey, Integer areaId, Integer page, Integer pageSize);


    HouseqmStatisticCategoryIssueListRspMsgVo taskIssueRepairList(Integer projectId, Integer taskId, Integer areaId, Integer beginOn, Integer endOn, Integer timestamp, Integer planStatus, String source, Integer page, Integer pageSize);

    IssueRepairStatisticVo projectIssueRepair(Integer projectId, String source, Integer areaId, Integer beginOn, Integer endOn, Integer timestamp);

    HouseQmCheckTaskHouseStatInfoVo getHouseQmHouseQmCheckTaskHouseStatByTaskId(Integer prodectId, Integer taskId, Integer areaId);

    RepossessionTasksStatusInfoVo getRepossessionTasksStatusInfo(Integer projectId,List<Integer> taskIds,Integer areaId);

    StatHouseqmCompleteDailyRspVo searchRepossessionStatusCompleteDaily(Integer project_id, List<Integer> taskIds, int beginOn, int endOn, Integer page, Integer page_size);

    AreaMapVo CreateAreasMapByAreaList(List<Area> areaList);

}
