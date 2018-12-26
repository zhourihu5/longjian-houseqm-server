package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.po.HouseQmCheckTask;

import java.util.Date;


/**
 * @author Houyan
 * @date 2018/12/25 0025 14:23
 */
public interface ICheckUpdateService {
    Date getHouseqmCheckTaskLastUpdateAtByTaskId(Integer taskId);

    Integer getHouseqmCheckTaskIssueLastId(Integer uid, Integer task_id, Date issueUpdateTime);

    Integer getHouseQmCheckTaskIssueLogLastId(Integer uid, Integer task_id, Date issueLogUpdateTime);

    Date getHouseQmCheckTaskIssueUserLastUpdateTime(Integer task_id);

    Date getHouseQmCheckTaskLastUpdateTime(Integer task_id);

}
