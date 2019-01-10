package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.dto.CheckerIssueStatusStatDto;
import com.longfor.longjian.houseqm.dto.RepaireIssueStatusStatDto;
import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAreaGroupModel;
import com.longfor.longjian.houseqm.po.IssueRepairCount;
import org.apache.ibatis.annotations.Param;

import java.util.*;

public interface HouseQmCheckTaskIssueMapper extends LFMySQLMapper<HouseQmCheckTaskIssue> {
    /**
     * @param issueUuids
     * @param timestamp
     * @param deleted
     * @return
     */
    public List<HouseQmCheckTaskIssue> selectByIssueUuidsAndclientCreateAt(@Param("issueUuids") Set<String> issueUuids, @Param("timestamp") int timestamp, @Param("deleted") String deleted);

    /**
     * @param projectId
     * @param taskIds
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskIdIn(@Param("projectId") int projectId, @Param("taskIds") List<Integer> taskIds, @Param("deleted") String deleted);

    /**
     * @param projectId
     * @param taskIds
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectCreateAtByProjectIdAndTaskIdsIn(@Param("projectId") int projectId, @Param("taskIds") List<Integer> taskIds, @Param("deleted") String deleted);

    /**
     * @param projectId
     * @param taskIds
     * @param date
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskIdAndClientCreateAt(@Param("projectId") int projectId, @Param("taskIds") List<Integer> taskIds, @Param("date") String date, @Param("deleted") String deleted);

    /**
     * @param projectId
     * @param taskIds
     * @param date
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskIdsAndClientCreateAt(@Param("projectId") int projectId, @Param("taskIds") List<Integer> taskIds, @Param("date") String date, @Param("deleted") String deleted);

    /**
     * 根据项目id/任务id查询
     *
     * @param projectId
     * @param taskId
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskId(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId, @Param("deleted") String deleted);

    /**
     * 根据任务id 、area_path_and_id 模糊查询，取未删除的数据
     *
     * @param taskId
     * @param areaPath
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssue> selectByTaskIdAndAreaPathAndIdLike(@Param("taskId") Integer taskId, @Param("areaPath") String areaPath, @Param("deleted") String deleted);

    /**
     * @param taskId
     * @param types
     * @param areaPathLike
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeInAndAreaPathAndIdLike(@Param("taskId") Integer taskId, @Param("types") List<Integer> types, @Param("areaPathLike") String areaPathLike, @Param("deleted") String deleted);

    /**
     * @param taskId
     * @param types
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeIn(@Param("taskId") Integer taskId, @Param("types") List<Integer> types, @Param("deleted") String deleted);

    /**
     * @param taskId
     * @param areaPathLike
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(@Param("taskId") Integer taskId, @Param("areaPathLike") String areaPathLike, @Param("deleted") String deleted);

    /**
     * @param taskId
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskId(@Param("taskId") Integer taskId, @Param("deleted") String deleted);

    /**
     * @param map
     * @return
     */
    List<IssueRepairCount> selectByProjectIdAndTaskIdAndTyeInAndDongTai(Map<String, Object> map);

    /**
     * @param task_id
     * @param uuids
     * @return
     */
    List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByTaskIdUuidIn(@Param("task_id") Integer task_id, @Param("uuids") List<String> uuids);

    /**
     * @param task_id
     * @param last_id
     * @param timestamp
     * @param userIds
     * @param userId
     * @return
     */
    List<HouseQmCheckTaskIssue> searchByConditionOrderByPageUnscoped(@Param("task_id") Integer task_id, @Param("last_id") Integer last_id, @Param("timestamp") Integer timestamp, @Param("userIds") List<Integer> userIds, @Param("userId") Integer userId, @Param("start") Integer start, @Param("limit") Integer limit);

    /**
     * @param projectId
     * @param taskId
     * @param areaIds
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssue> selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId, @Param("areaIds") List<Integer> areaIds, @Param("deleted") String deleted);

    /**
     * @param map
     * @return java.lang.Integer
     * @author hy
     * @date 2018/12/21 0021
     */
    Integer selectTotalByProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map);

    /**
     * @param map
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     * @author hy
     * @date 2018/12/21 0021
     */
    List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByPageAndProjectIdAndCategoryClsAndNoDeletedAndDongTai(Map<String, Object> map);

    ArrayList<HouseQmCheckTaskIssue> searchhouseQmCheckTaskIssueByProTaskIdAreaidBegin(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId, @Param("areaId") Integer areaId, @Param("begin") Date begin, @Param("endOns") Date endOns, @Param("types") List<Integer> types, @Param("deleted") String deleted);

    List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(HashMap<String, Object> condiMap);


    /**
     * @param condiMap
     * @return java.util.List<com.longfor.longjian.houseqm.po.IssueRepairCount>
     * @author hy
     * @date 2018/12/24 0024
     */
    List<IssueRepairCount> selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(HashMap<String, Object> condiMap);

    /**
     * @param
     * @param task_id
     * @param issueUpdateTime
     * @param userIds
     * @param issueUuids
     * @param aFalse
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     * @author hy
     * @date 2018/12/25 0025
     */
    List<HouseQmCheckTaskIssue> selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(@Param("taskId") Integer task_id, @Param("updateAtTime") Date issueUpdateTime, @Param("userIds") List<Integer> userIds, @Param("uuids") List<String> issueUuids, @Param("deleted") String aFalse);

    /**
     * @param userIds
     * @param task_id
     * @param issueUuids
     * @param aFalse
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     * @author hy
     * @date 2018/12/25 0025
     */
    List<HouseQmCheckTaskIssue> selectUuidBySenderIdInOrTaskIdAndUuidIn(@Param("userIds") List<Integer> userIds, @Param("taskId") Integer task_id, @Param("uuids") List<String> issueUuids, @Param("deleted") String aFalse);

    /**
     * @param map
     * @return java.lang.Integer
     * @author hy
     * @date 2018/12/26 0026
     */
    Integer selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai(Map<String, Object> map);

    /**
     * @param map
     * @return java.util.List<com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue>
     * @author hy
     * @date 2018/12/26 0026
     */
    List<HouseQmCheckTaskIssue> selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(Map<String, Object> map);

    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAreaPathAndIdAndStatusIn(HashMap<String, Object> map);

    List<CheckerIssueStatusStatDto> selectCheckerIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi);

    List<RepaireIssueStatusStatDto> selectRepaireIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(Map<String, Object> condi);

    List<HouseQmCheckTaskIssue> selectByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(@Param("project_id") Integer project_id, @Param("category_cls") Integer category_cls, @Param("area_path_and_id") String area_path_and_id);
}