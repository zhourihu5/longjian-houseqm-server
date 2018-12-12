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

}