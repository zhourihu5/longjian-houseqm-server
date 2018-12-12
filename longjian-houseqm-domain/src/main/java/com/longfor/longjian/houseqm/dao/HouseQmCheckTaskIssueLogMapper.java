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
}