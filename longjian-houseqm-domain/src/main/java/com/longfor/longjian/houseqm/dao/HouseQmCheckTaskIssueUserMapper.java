package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseQmCheckTaskIssueUserMapper extends LFMySQLMapper<HouseQmCheckTaskIssueUser> {
    /**
     *
     * @param userId
     * @param taskId
     * @param timestamp
     * @param deleted
     * @return
     */
    public List<HouseQmCheckTaskIssueUser> selectByUserIdAndTaskIdAndCreateAt(@Param("userId") int userId, @Param("taskId") int taskId,@Param("timestamp") int timestamp,@Param("deleted") String deleted);

    /**
     *
     * @param task_id
     * @param last_id
     * @param timestamp
     * @param orderBy
     * @param start
     * @param limit
     * @return
     */
    List<HouseQmCheckTaskIssueUser> searchByConditionOrderByPageUnscoped(@Param("task_id") Integer task_id,@Param("last_id") Integer last_id, @Param("timestamp")Integer timestamp, @Param("start") Integer start, @Param("limit") Integer limit);
}