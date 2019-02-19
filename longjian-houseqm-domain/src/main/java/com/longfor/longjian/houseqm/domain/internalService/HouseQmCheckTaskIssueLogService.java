package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueLogService {

    int deleteIssueLogByUuids(List<String> uuids);

    int addBatch(List<HouseQmCheckTaskIssueLog> hIssueLogs);

    List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids);
    /**
     *
     * @param userId
     * @param task_id
     * @param last_id
     * @param timestamp
     * @param limit
     * @return
     */
    List<HouseQmCheckTaskIssueLog> searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer limit,Integer start,Integer checker);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @param uuids
     * @param issueLogUpdateTime
     * @return com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueLog
     */
    HouseQmCheckTaskIssueLog selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(Integer task_id, List<String> uuids, Date issueLogUpdateTime);

    List<HouseQmCheckTaskIssueLog> selectByUuidAndNotDelete(String issueUuid);

    void add(HouseQmCheckTaskIssueLog new_issue_log);

    List<HouseQmCheckTaskIssueLog> selectByIssueUuIdAndStatusNotDel(String issueUuid, ArrayList<Integer> issueLogStatus);

    int selectByIssueUuIdAndStatusNotDelAndCount(String issueUuid, ArrayList<Integer> issueLogStatus);

    List<HouseQmCheckTaskIssueLog> selectByUuidsAndNotDelete(List<String> log_uuids);

    List<HouseQmCheckTaskIssueLog> selectByIssueUuIdInAndStatus(List<String> issue_uuids, Integer status);
}
