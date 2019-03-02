package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.dto.CheckerIssueStatusStatDto;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueListDto;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueDto;
import com.longfor.longjian.houseqm.dto.RepaireIssueStatusStatDto;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueAttachment;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueUser;

import java.util.*;

public interface HouseQmCheckTaskIssueService {

    List<HouseQmCheckTaskIssue> searchByTaskIdAndAreaPathAndIdRegexp(int taskId, String regexp);

    Integer countByProjIdAndTaskIdAndTypInGroupByCategoryPathAndKeyAndCheckItemKey(Integer projectId, Integer taskId, List<Integer> typs, Integer areaId, Date beginOn, Date endOn);

    List<HouseQmCheckTaskIssue> searchByProjIdAndTaskIdAndTypInGroupByCategoryPathAndKeyAndCheckItemKey(Map<String, Object> condi);

    List<HouseQmCheckTaskIssue> searchByTaskIdInGroupByTaskIdAndStatus(List<Integer> taskIds);

    List<HouseQmCheckTaskIssue> searchByProjIdAndCategoryClsInAndRepairerIdAndClientCreateAtAndTypInAndStatusInAndTaskIdOrderByClientCreateAt(Integer projectId, List<Integer> categoryClsList, String statBegin, String statEnd, List<Integer> typs, List<Integer> status, Integer taskId, List<Integer> myTaskIds);

    List<HouseQmCheckTaskIssue> searchByProjIdAndCategoryClsInAndSenderIdAndClientCreateAtAndTypAndTaskIdOrderByClientCreateAt(Integer projectId, List<Integer> categoryClsList, String statBegin, String statEnd, List<Integer> typs, Integer taskId, List<Integer> myTaskIds);

    List<HouseQmCheckTaskIssue> searchByProjectIdAndCategoryClsInAndTaskIdInAndAreaPathAndIdLike(Integer projectId, List<Integer> categoryClsList, List<Integer> taskIds, String areaPath);

    List<HouseQmCheckTaskIssue> searchByProjIdAndTaskIdAndAreaIdInAndRepairedIdAndClientCreateAt(Integer projectId, Integer taskId, List<Integer> subAreaIds, Integer repairerId, Date beginOn, Date endOn);

    int deleteHouseQmCheckTaskIssueByProjUuid(Integer projectId, String issueUuid);

    int insertOneHouseQmCheckTaskIssue(HouseQmCheckTaskIssue issue);

    List<HouseQmCheckTaskIssue> searchByProjIdAndUuidIn(Integer projectId, List<String> uuids);

    List<HouseQmCheckTaskIssue> searchByIssueUuidsAndclientCreateAt(Set<String> issueUuids, int timestamp);

    List<CheckerIssueStat> searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds);

    List<CheckerIssueStat> searchHouseQmCheckTaskIssueActiveDateByProjTaskIdIn(Integer projectId, List<Integer> taskIds);

    List<CheckerIssueStat> getIssueSituationDailyByProjTaskIdInDate(Integer projectId, List<Integer> taskIds, String date);

    List<CheckerIssueStat> searchByProjectIdAndTaskIdsAndClientCreateAt(Integer projectId, List<Integer> taskIds, String date);

    List<CheckerIssueStat> searchByProjectIdAndTaskId(Integer projectId, Integer taskId);

    List<HouseQmCheckTaskIssue> searchByTaskIdAndAreaPathAndIdLike(Integer taskId, String areaPath);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeInAndAreaPathAndIdLike(Boolean onlyIssue,Integer taskId, List<Integer> types, Integer areaId);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeIn(Integer taskId, List<Integer> types);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(Integer taskId, String areaPathLike);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskId(Integer taskId);

    List<HouseQmCheckTaskIssue> selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(Integer projectId, Integer taskId, List<Integer> areaIds);


    List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByTaskIdUuidIn(Integer taskId, List<String> uuids);


    List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer taskId, Integer lastId, Integer timestamp, Integer start, Integer limit, Integer checker);

    List<IssueRepairCount> selectByProjectIdAndTaskIdAndTyeInAndDongTai(Map<String, Object> map);


    List<HouseQmCheckTaskIssueUser> searchHouseQmCheckTaskIssueUserByTaskIdLastIdUpdateAtGt(Integer taskId, Integer lastId, Integer timestamp, Integer start, Integer limit);

    List<HouseQmCheckTaskIssueAttachment> searchHouseQmCheckTaskIssueAttachmentByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer taskId, Integer lastId, Integer timestamp, Integer start, Integer limit, Integer privateInt, Integer publicInt);

    List<HouseQmCheckTaskIssue> searchByProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map);

    Integer searchTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map);

    List<HouseQmCheckTaskIssue> searchByPageAndProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map);

    Integer selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai(Map<String, Object> map);
    HouseQmCheckTaskIssueListDto selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai2(Integer projectId, Integer taskId, List<Integer> categoryClsList, Integer areaId, Integer planStatus, Date beginOn, Date endOn, Integer page, Integer pageSize);

    List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(Map<String, Object> map);

    List<HouseQmCheckTaskIssue> houseQmCheckTaskIssueByProTaskIdAreaidBegin(Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns, List<Integer> types);

    HouseQmCheckTaskIssueDto searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(HashMap<String, Object> condiMap);

    List<IssueRepairCount> selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(HashMap<String, Object> condiMap);

    HouseQmCheckTaskIssue selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(Integer taskId, Date issueUpdateTime, List<Integer> userIds, List<String> issueUuids);

    List<HouseQmCheckTaskIssue> selectUuidBySenderIdInOrTaskIdAndUuidIn(List<Integer> userIds, Integer taskId, List<String> issueUuids);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAreaPathAndIdAndStatusIn(HashMap<String, Object> map);

    int removeHouseQmCheckTaskIssueByProjectIdAndTaskId(Integer projectId, Integer taskId);

    List<CheckerIssueStatusStatDto> searchCheckerIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi);


    List<RepaireIssueStatusStatDto> searchRepaireIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi);

    List<HouseQmCheckTaskIssue> searchByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(Integer projectId, Integer categoryCls, String areaPath);

    HouseQmCheckTaskIssue selectByUuidAndNotDelete(String issueUuid);

    HouseQmCheckTaskIssue getByUuidUnscoped(String uuid);

    HouseQmCheckTaskIssue getIssueByProjectIdAndUuid(Integer projectId, String issueUuid);

    void update(HouseQmCheckTaskIssue issueInfo);

    List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProIdAndIdAndStatus(Integer projectId, List<Integer> issueIds, ArrayList<Integer> statusList);

    HouseQmCheckTaskIssue selectByTaskIdAndUuidAndNotDel(Integer taskId, String issueUuid);

    Integer add(HouseQmCheckTaskIssue issue);

    List<HouseQmCheckTaskIssue> selectByUuids(List<String> issueUuids);
}
