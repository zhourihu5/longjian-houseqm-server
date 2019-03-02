package com.longfor.longjian.houseqm.app.service;

import java.util.Date;


/**
 * @author Houyan
 * @date 2018/12/25 0025 14:23
 */
public interface ICheckUpdateService {
    Date getHouseqmCheckTaskLastUpdateAtByTaskId(Integer taskId);

    Integer getHouseqmCheckTaskIssueLastId(Integer uid, Integer taskId, Date issueUpdateTime);

    Integer getHouseQmCheckTaskIssueLogLastId(Integer uid, Integer taskId, Date issueLogUpdateTime);

    Date getHouseQmCheckTaskIssueUserLastUpdateTime(Integer taskId);

    Date getHouseQmCheckTaskLastUpdateTime(Integer taskId);

}
