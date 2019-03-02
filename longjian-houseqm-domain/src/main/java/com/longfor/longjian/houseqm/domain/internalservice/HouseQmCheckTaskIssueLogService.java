package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssueLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueLogService {

    int deleteIssueLogByUuids(List<String> uuids);

    int addBatch(List<HouseQmCheckTaskIssueLog> hIssueLogs);

    List<HouseQmCheckTaskIssueLog> searchByIssueUuid(Set<String> issueUuids);

    List<HouseQmCheckTaskIssueLog> searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer taskId, Integer lastId, Integer timestamp, Integer limit,Integer start,Integer checker);


    HouseQmCheckTaskIssueLog selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(Integer taskId, List<String> uuids, Date issueLogUpdateTime);

    List<HouseQmCheckTaskIssueLog> selectByUuidAndNotDelete(String issueUuid);

    void add(HouseQmCheckTaskIssueLog newIssueLog);

    List<HouseQmCheckTaskIssueLog> selectByIssueUuIdAndStatusNotDel(String issueUuid, ArrayList<Integer> issueLogStatus);

    int selectByIssueUuIdAndStatusNotDelAndCount(String issueUuid, ArrayList<Integer> issueLogStatus);

    List<HouseQmCheckTaskIssueLog> selectByUuidsAndNotDelete(List<String> logUuids);

    List<HouseQmCheckTaskIssueLog> selectByIssueUuIdInAndStatus(List<String> issueUuids, Integer status);
}
