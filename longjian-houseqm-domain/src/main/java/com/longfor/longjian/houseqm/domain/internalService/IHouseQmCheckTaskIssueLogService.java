package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueLog;

import java.util.List;

public interface IHouseQmCheckTaskIssueLogService {
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
