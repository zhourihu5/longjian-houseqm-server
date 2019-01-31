package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.consts.HouseQmCheckTaskRoleType;
import com.longfor.longjian.houseqm.app.service.ICheckUpdateService;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Houyan
 * @date 2018/12/25 0025 14:25
 */
@Repository
@Service
@Slf4j
public class CheckUpdateServiceImpl implements ICheckUpdateService {

    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;

    @Resource
    HouseQmCheckTaskIssueUserService houseQmCheckTaskIssueUserService;

    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    @Resource
    HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;

    /**
     * @param taskId
     * @return java.util.Date
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    public Date getHouseqmCheckTaskLastUpdateAtByTaskId(Integer taskId) {
        HouseQmCheckTask info = houseQmCheckTaskService.selectUpdateAtByTaskIdAndNoDeleted(taskId);
        if (info != null) return info.getUpdateAt();
        else return null;
    }

    /**
     * @param uid
     * @param task_id
     * @param issueUpdateTime
     * @return java.lang.Integer
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    public Integer getHouseqmCheckTaskIssueLastId(Integer uid, Integer task_id, Date issueUpdateTime) {
        Integer checker = HouseQmCheckTaskRoleType.Checker.getValue();
        List<UserInHouseQmCheckTask> checkTaskSquadInfo = userInHouseQmCheckTaskService.selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(checker, uid, task_id);
        List<Integer> squadIds = Lists.newArrayList();
        checkTaskSquadInfo.forEach(i -> {
            squadIds.add(i.getSquadId());
        });

        List<UserInHouseQmCheckTask> checkTaskUserIdInfo = userInHouseQmCheckTaskService.selectUserIdBySquadIdInAndNoDeleted(squadIds);
        List<Integer> userIds = Lists.newArrayList();
        checkTaskUserIdInfo.forEach(i -> {
            userIds.add(i.getUserId());
        });
        userIds.add(uid);

        List<HouseQmCheckTaskIssueUser> issueUserIssueUuidInfo = houseQmCheckTaskIssueUserService.selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(uid, task_id);
        List<String> issueUuids = Lists.newArrayList();
        issueUserIssueUuidInfo.forEach(i -> {
            issueUuids.add(i.getIssueUuid());
        });

        HouseQmCheckTaskIssue taskIssueInfo = houseQmCheckTaskIssueService.selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(task_id, issueUpdateTime, userIds, issueUuids);
        if (taskIssueInfo != null) return taskIssueInfo.getId();
        else return 0;
    }

    /**
     * @param uid
     * @param task_id
     * @param issueLogUpdateTime
     * @return java.lang.Integer
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    public Integer getHouseQmCheckTaskIssueLogLastId(Integer uid, Integer task_id, Date issueLogUpdateTime) {
        Integer checker = HouseQmCheckTaskRoleType.Checker.getValue();
        List<UserInHouseQmCheckTask> checkTaskSquadInfo = userInHouseQmCheckTaskService.selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(checker, uid, task_id);
        List<Integer> squadIds = Lists.newArrayList();
        checkTaskSquadInfo.forEach(i -> {
            squadIds.add(i.getSquadId());
        });

        List<UserInHouseQmCheckTask> checkTaskUserIdInfo = userInHouseQmCheckTaskService.selectUserIdBySquadIdInAndNoDeleted(squadIds);
        List<Integer> userIds = Lists.newArrayList();
        checkTaskUserIdInfo.forEach(i -> {
            userIds.add(i.getUserId());
        });
        userIds.add(uid);

        List<HouseQmCheckTaskIssueUser> userIssueUuidInfo = houseQmCheckTaskIssueUserService.selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(uid, task_id);
        List<String> issueUuids = Lists.newArrayList();
        userIssueUuidInfo.forEach(i -> {
            issueUuids.add(i.getIssueUuid());
        });
        List<HouseQmCheckTaskIssue> taskIssueUuidInfo = houseQmCheckTaskIssueService.selectUuidBySenderIdInOrTaskIdAndUuidIn(userIds, task_id, issueUuids);
        List<String> uuids = taskIssueUuidInfo.stream().map(HouseQmCheckTaskIssue::getUuid).collect(Collectors.toList());
        HouseQmCheckTaskIssueLog issueLogInfo = houseQmCheckTaskIssueLogService.selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(task_id, uuids, issueLogUpdateTime);
        if (issueLogInfo != null) return issueLogInfo.getId();
        else return 0;
    }

    /**
     * @param task_id
     * @return java.util.Date
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    public Date getHouseQmCheckTaskIssueUserLastUpdateTime(Integer task_id) {
        List<HouseQmCheckTaskIssueUser> issueUserUpdateInfo = houseQmCheckTaskIssueUserService.selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(task_id);
        if (issueUserUpdateInfo != null) return issueUserUpdateInfo.get(0).getUpdateAt();
        else return DateUtil.timeStampToDate(0, "yyyy-MM-dd");
    }

    /**
     * @param task_id
     * @return java.util.Date
     * @author hy
     * @date 2018/12/25 0025
     */
    @Override
    public Date getHouseQmCheckTaskLastUpdateTime(Integer task_id) {
        List<UserInHouseQmCheckTask> issueUpdateInfo = userInHouseQmCheckTaskService.selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(task_id);
        if (issueUpdateInfo != null) return issueUpdateInfo.get(0).getUpdateAt();
        else return DateUtil.timeStampToDate(0, "yyyy-MM-dd");
    }


}
