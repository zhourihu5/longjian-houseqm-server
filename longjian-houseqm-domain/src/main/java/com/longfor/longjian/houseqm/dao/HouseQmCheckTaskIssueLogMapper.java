package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueLogMapper extends LFMySQLMapper<HouseQmCheckTaskIssueLog> {

    /**
     *
     * @param issueUuids
     * @param deleted
     * @return
     */
    public List<HouseQmCheckTaskIssueLog> selectByIssueUuid(@Param("issueUuids") Set<String> issueUuids,@Param("deleted") String deleted);

    /**
     *
     * @param userIds
     * @param task_id
     * @param last_id
     * @param timestamp
     * @param start
     * @param limit
     * @return
     */
    List<HouseQmCheckTaskIssueLog> searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(@Param("userId") Integer userId,@Param("userIds")List<Integer> userIds,@Param("task_id") Integer task_id, @Param("last_id")Integer last_id, @Param("timestamp")Integer timestamp, @Param("start")Integer start, @Param("limit")Integer limit);
}