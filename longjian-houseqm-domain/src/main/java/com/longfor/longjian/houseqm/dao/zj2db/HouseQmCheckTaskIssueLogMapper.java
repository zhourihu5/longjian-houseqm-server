package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueLog;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
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


    List<HouseQmCheckTaskIssueLog> searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(@Param("userId") Integer userId,@Param("userIds")List<Integer> userIds,@Param("task_id") Integer taskId, @Param("last_id")Integer lastId, @Param("timestamp")Integer timestamp, @Param("start")Integer start, @Param("limit")Integer limit);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param taskId
     * @param uuids
     * @param issueLogUpdateTime
     * @param aFalse
     * @return com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueLog
     */
    List<HouseQmCheckTaskIssueLog> selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(@Param("taskId") Integer taskId,@Param("uuids") List<String> uuids,@Param("updateAt") Date issueLogUpdateTime,@Param("deleted") String aFalse);

}