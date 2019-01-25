package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAttachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueAttachmentMapper extends LFMySQLMapper<HouseQmCheckTaskIssueAttachment> {

    /**
     *
     * @param issueUuids
     * @param deleted
     * @return
     */
    public List<HouseQmCheckTaskIssueAttachment> selectByIssueUuid(@Param("issueUuids") Set<String> issueUuids,@Param("deleted") String deleted);

    /**
     *
     * @param task_id
     * @param userId
     * @param userIds
     * @param privateInt
     * @param publicInt
     * @param start
     * @param limit
     * @return
     */
    List<HouseQmCheckTaskIssueAttachment> searchByTaskIdAndSelfJoinOrderByIdASCPageUnscoped(@Param("task_id") Integer task_id, @Param("userId") Integer userId,@Param("timestamp")Integer timestamp, @Param("userIds") List<Integer> userIds, @Param("privateInt") Integer privateInt, @Param("publicInt") Integer publicInt, @Param("start") Integer start, @Param("limit") Integer limit);
}