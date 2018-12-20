package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAreaGroupModel;
import com.longfor.longjian.houseqm.po.IssueRepairCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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
     *
     * @param taskId
     * @param types
     * @param areaPathLike
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeInAndAreaPathAndIdLike(@Param("taskId") Integer taskId, @Param("types") List<Integer> types, @Param("areaPathLike") String areaPathLike,@Param("deleted") String deleted);

    /**
     *
     * @param taskId
     * @param types
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskIdAndTyeIn(@Param("taskId")Integer taskId,  @Param("types")List<Integer> types, @Param("deleted")String deleted);

    /**
     *
     * @param taskId
     * @param areaPathLike
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(@Param("taskId")Integer taskId, @Param("areaPathLike")String areaPathLike, @Param("deleted")String deleted);

    /**
     *
     * @param taskId
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssueAreaGroupModel> selectByTaskId(@Param("taskId")Integer taskId,@Param("deleted") String deleted);

    /**
     *
     * @param projectId
     * @param taskId
     * @param types
     * @param deleted
     * @return
     */
    List<IssueRepairCount> selectByProjectIdAndTaskIdAndTypeIn(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId, @Param("types") List<Integer> types,@Param("deleted") String deleted);

    /**
     *
     * @param projectId
     * @param taskId
     * @param areaIds
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskIssue> selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(@Param("projectId") Integer projectId,@Param("taskId") Integer taskId, @Param("areaIds") List<Integer> areaIds,@Param("deleted") String deleted);

}