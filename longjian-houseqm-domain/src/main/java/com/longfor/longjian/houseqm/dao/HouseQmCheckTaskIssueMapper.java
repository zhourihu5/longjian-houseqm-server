package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueMapper extends LFMySQLMapper<HouseQmCheckTaskIssue> {
    /**
     *
     * @param issueUuids
     * @param timestamp
     * @param deleted
     * @return
     */
    public List<HouseQmCheckTaskIssue> selectByIssueUuidsAndclientCreateAt(@Param("issueUuids") Set<String> issueUuids,@Param("timestamp") int timestamp,@Param("deleted") String deleted);

    /**
     *
     * @param projectId
     * @param taskIds
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskIdIn(@Param("projectId") int projectId,@Param("taskIds") List<Integer> taskIds,@Param("deleted") String deleted);

    /**
     *
     * @param projectId
     * @param taskIds
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectCreateAtByProjectIdAndTaskIdsIn(@Param("projectId") int projectId,@Param("taskIds") List<Integer> taskIds,@Param("deleted") String deleted);

    /**
     *
     * @param projectId
     * @param taskIds
     * @param date
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskIdAndClientCreateAt(@Param("projectId") int projectId,@Param("taskIds") List<Integer> taskIds,@Param("date") String date,@Param("deleted") String deleted);

    /**
     *
     * @param projectId
     * @param taskIds
     * @param date
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskIdsAndClientCreateAt(@Param("projectId") int projectId,@Param("taskIds") List<Integer> taskIds,@Param("date") String date,@Param("deleted") String deleted);

    /**
     * 根据项目id/任务id查询
     * @param projectId
     * @param taskId
     * @param deleted
     * @return
     */
    public List<CheckerIssueStat> selectByProjectIdAndTaskId(@Param("projectId") Integer projectId,@Param("taskId") Integer taskId,@Param("deleted") String deleted);

}