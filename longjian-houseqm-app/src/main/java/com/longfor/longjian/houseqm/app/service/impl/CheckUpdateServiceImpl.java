package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.consts.HouseQmCheckTaskRoleType;
import com.longfor.longjian.houseqm.app.service.ICheckUpdateService;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
    @Override
    public Integer getHouseqmCheckTaskIssueLastId(Integer uid, Integer taskId, Date issueUpdateTime) {
        Integer checker = HouseQmCheckTaskRoleType.Checker.getValue();
        List<UserInHouseQmCheckTask> checkTaskSquadInfo = userInHouseQmCheckTaskService.selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(checker, uid, taskId);
        List<Integer> squadIds = Lists.newArrayList();
        checkTaskSquadInfo.forEach(i ->
            squadIds.add(i.getSquadId())
        );

        List<UserInHouseQmCheckTask> checkTaskUserIdInfo = userInHouseQmCheckTaskService.selectUserIdBySquadIdInAndNoDeleted(squadIds);
        List<Integer> userIds = Lists.newArrayList();
        checkTaskUserIdInfo.forEach(i ->
            userIds.add(i.getUserId())
        );
        userIds.add(uid);

        List<HouseQmCheckTaskIssueUser> issueUserIssueUuidInfo = houseQmCheckTaskIssueUserService.selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(uid, taskId);
        List<String> issueUuids = Lists.newArrayList();
        issueUserIssueUuidInfo.forEach(i ->
            issueUuids.add(i.getIssueUuid())
        );

        HouseQmCheckTaskIssue taskIssueInfo = houseQmCheckTaskIssueService.selectIdByTaskIdAndIdGtAndUpdateAtGtAndSenderIdInOrUuidInAndNoDeletedOrderById(taskId, issueUpdateTime, userIds, issueUuids);
        if (taskIssueInfo != null) return taskIssueInfo.getId();
        else return 0;
    }

    @Override
    public Integer getHouseQmCheckTaskIssueLogLastId(Integer uid, Integer taskId, Date issueLogUpdateTime) {
        Integer checker = HouseQmCheckTaskRoleType.Checker.getValue();
        List<UserInHouseQmCheckTask> checkTaskSquadInfo = userInHouseQmCheckTaskService.selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(checker, uid, taskId);
        List<Integer> squadIds = Lists.newArrayList();
        checkTaskSquadInfo.forEach(i ->
            squadIds.add(i.getSquadId())
        );

        List<UserInHouseQmCheckTask> checkTaskUserIdInfo = userInHouseQmCheckTaskService.selectUserIdBySquadIdInAndNoDeleted(squadIds);
        List<Integer> userIds = Lists.newArrayList();
        checkTaskUserIdInfo.forEach(i ->
            userIds.add(i.getUserId())
        );
        userIds.add(uid);

        List<HouseQmCheckTaskIssueUser> userIssueUuidInfo = houseQmCheckTaskIssueUserService.selectIssueUUIDByUserIdAndTaskIdAndNoDeleted(uid, taskId);
        List<String> issueUuids = Lists.newArrayList();
        userIssueUuidInfo.forEach(i ->
            issueUuids.add(i.getIssueUuid())
        );
        List<HouseQmCheckTaskIssue> taskIssueUuidInfo = houseQmCheckTaskIssueService.selectUuidBySenderIdInOrTaskIdAndUuidIn(userIds, taskId, issueUuids);
        List<String> uuids = taskIssueUuidInfo.stream().map(HouseQmCheckTaskIssue::getUuid).collect(Collectors.toList());
        HouseQmCheckTaskIssueLog issueLogInfo = houseQmCheckTaskIssueLogService.selectIdByTaskIdAndIdAndUuidInAndUpdateAtGtAndNoDeletedOrderById(taskId, uuids, issueLogUpdateTime);
        if (issueLogInfo != null) return issueLogInfo.getId();
        else return 0;
    }

    @Override
    public Date getHouseQmCheckTaskIssueUserLastUpdateTime(Integer taskId) {
        HouseQmCheckTaskIssueUser issueUserUpdateInfo = houseQmCheckTaskIssueUserService.selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(taskId);
        if (issueUserUpdateInfo != null) return issueUserUpdateInfo.getUpdateAt();
        else return DateUtil.timeStampToDate(0, "yyyy-MM-dd");
    }

    @Override
    public Date getHouseQmCheckTaskLastUpdateTime(Integer taskId) {
        List<UserInHouseQmCheckTask> issueUpdateInfo = userInHouseQmCheckTaskService.selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(taskId);
        if (!CollectionUtils.isEmpty(issueUpdateInfo)) return issueUpdateInfo.get(0).getUpdateAt();
        else return DateUtil.timeStampToDate(0, "yyyy-MM-dd");
    }


}
