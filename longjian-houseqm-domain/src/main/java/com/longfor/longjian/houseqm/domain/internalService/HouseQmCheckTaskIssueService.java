package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.dto.CheckerIssueStatusStatDto;
import com.longfor.longjian.houseqm.dto.RepaireIssueStatusStatDto;
import com.longfor.longjian.houseqm.po.*;

import java.util.*;

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

    List<HouseQmCheckTaskIssue> selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(Integer projectId, Integer taskId, List<Integer> areaIds);
    /**
     *
     * @param task_id
     * @param uuids
     * @return
     */
    List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByTaskIdUuidIn(Integer task_id, List<String> uuids);

    /**
     *
     * @param userId
     * @param task_id
     * @param last_id
     * @param timestamp
     * @param start
     * @param limit
     * @return
     */
    List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit,Integer checker);

    List<IssueRepairCount> selectByProjectIdAndTaskIdAndTyeInAndDongTai(Map<String,Object> map);
    /**
     *
     * @param task_id
     * @param last_id
     * @param timestamp
     * @param start
     * @param limit
     * @return
     */
    List<HouseQmCheckTaskIssueUser> searchHouseQmCheckTaskIssueUserByTaskIdLastIdUpdateAtGt(Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit);

    /**
     *
     * @param userId
     * @param task_id
     * @param last_id
     * @param timestamp
     * @param start
     * @param limit
     * @return
     */
    List<HouseQmCheckTaskIssueAttachment> searchHouseQmCheckTaskIssueAttachmentByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit,Integer privateInt,Integer publicInt);

    Integer searchTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String,Object> map);

    List<HouseQmCheckTaskIssue> searchByPageAndProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String,Object> map);

    /**
     *
     * @author hy
     * @date 2018/12/22 0022
     * @param map
     * @return java.lang.Integer
     */
    Integer selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai(Map<String,Object> map);

    /**
     *
     * @author hy
     * @date 2018/12/22 0022
     * @param map
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     */
    List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(Map<String,Object> map);

    ArrayList<HouseQmCheckTaskIssue> houseQmCheckTaskIssueByProTaskIdAreaidBegin(Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns, List<Integer> types);

    List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(HashMap<String, Object> condiMap);
    /**
     *
     * @author hy
     * @date 2018/12/24 0024
     * @param condiMap
     * @return java.util.List<com.longfor.longjian.houseqm.po.IssueRepairCount>
     */
    List<IssueRepairCount> selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(HashMap<String, Object> condiMap);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param
     * @param task_id
     * @param issueUpdateTime
     * @param userIds
     * @param issueUuids
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     */
    List<HouseQmCheckTaskIssue> selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(Integer task_id, Date issueUpdateTime, List<Integer> userIds, List<String> issueUuids);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param userIds
     * @param task_id
     * @param issueUuids
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     */
    List<HouseQmCheckTaskIssue> selectUuidBySenderIdInOrTaskIdAndUuidIn(List<Integer> userIds, Integer task_id, List<String> issueUuids);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAreaPathAndIdAndStatusIn(HashMap<String, Object> map);

    int removeHouseQmCheckTaskIssueByProjectIdAndTaskId(Integer project_id, Integer task_id);

    List<CheckerIssueStatusStatDto> searchCheckerIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi);


    List<RepaireIssueStatusStatDto> searchRepaireIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi);

    List<HouseQmCheckTaskIssue> searchByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(Integer project_id, Integer category_cls, String areaPath);

    HouseQmCheckTaskIssue selectByUuidAndNotDelete(String issueUuid);

    HouseQmCheckTaskIssue getIssueByProjectIdAndUuid(Integer projectId, String issueUuid);

    void update(HouseQmCheckTaskIssue issue_info);

    List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProIdAndIdAndStatus(Integer projectId, List<Integer> issueIds, ArrayList<Integer> statusList);
}
