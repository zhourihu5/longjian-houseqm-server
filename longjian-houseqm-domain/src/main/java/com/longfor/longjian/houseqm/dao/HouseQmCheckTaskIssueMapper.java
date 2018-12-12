package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
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
}