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
}