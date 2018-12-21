package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskIssueLogService {

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

}
