package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseQmCheckTaskIssueUserMapper extends LFMySQLMapper<HouseQmCheckTaskIssueUser> {


    List<HouseQmCheckTaskIssueUser> searchByConditionOrderByPageUnscoped(@Param("task_id") Integer taskId, @Param("last_id") Integer lastId, @Param("timestamp") Integer timestamp, @Param("start") Integer start, @Param("limit") Integer limit);

    List<HouseQmCheckTaskIssueUser> selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(@Param("uid") Integer uid, @Param("taskId") Integer taskId, @Param("deleted") String aFalse);

}