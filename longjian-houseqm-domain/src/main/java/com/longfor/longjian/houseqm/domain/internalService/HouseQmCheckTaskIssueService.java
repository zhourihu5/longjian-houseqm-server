package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAreaGroupModel;
import com.longfor.longjian.houseqm.po.IssueRepairCount;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueService {
    List<HouseQmCheckTaskIssue> searchByIssueUuidsAndclientCreateAt(Set<String> issueUuids, int timestamp);

    List<CheckerIssueStat> searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds);

    List<CheckerIssueStat> searchHouseQmCheckTaskIssueActiveDateByProjTaskIdIn(Integer projectId, List<Integer> taskIds);

    List<CheckerIssueStat> getIssueSituationDailyByProjTaskIdInDate(Integer projectId, List<Integer> taskIds,String date);

    List<CheckerIssueStat> searchByProjectIdAndTaskIdsAndClientCreateAt(Integer projectId, List<Integer> taskIds,String date);

    List<CheckerIssueStat> searchByProjectIdAndTaskId(Integer projectId,Integer taskId);

    List<HouseQmCheckTaskIssue> searchByTaskIdAndAreaPathAndIdLike(Integer taskId,String areaPath);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeInAndAreaPathAndIdLike(Integer taskId, List<Integer> types, String areaPathLike);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeIn(Integer taskId,List<Integer> types);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(Integer taskId,String areaPathLike);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskId(Integer taskId);

    List<IssueRepairCount> selectByProjectIdAndTaskIdAndTyeIn(Integer projectId, Integer taskId, List<Integer> types);

    List<HouseQmCheckTaskIssue> selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(Integer projectId, Integer taskId, List<Integer> areaIds);

}
