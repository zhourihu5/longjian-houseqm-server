package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueAttachment;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueAttachmentMapper extends LFMySQLMapper<HouseQmCheckTaskIssueAttachment> {


    public List<HouseQmCheckTaskIssueAttachment> selectByIssueUuid(@Param("issueUuids") Set<String> issueUuids, @Param("deleted") String deleted);


    List<HouseQmCheckTaskIssueAttachment> searchByTaskIdAndSelfJoinOrderByIdASCPageUnscoped(@Param("task_id") Integer taskId, @Param("userId") Integer userId, @Param("timestamp") Integer timestamp, @Param("userIds") List<Integer> userIds, @Param("privateInt") Integer privateInt, @Param("publicInt") Integer publicInt, @Param("start") Integer start, @Param("limit") Integer limit);
}