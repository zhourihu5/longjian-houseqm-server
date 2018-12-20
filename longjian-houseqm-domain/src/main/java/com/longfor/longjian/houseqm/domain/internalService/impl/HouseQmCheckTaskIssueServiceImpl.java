package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.*;
import com.longfor.longjian.houseqm.domain.internalService.IHouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.po.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HouseQmCheckTaskIssueServiceImpl implements IHouseQmCheckTaskIssueService {
    @Autowired
    private HouseQmCheckTaskIssueMapper houseQmCheckTaskIssueMapper;
    @Autowired
    private UserInHouseQmCheckTaskMapper userInHouseQmCheckTaskMapper;
    @Autowired
    private HouseQmCheckTaskIssueUserMapper houseQmCheckTaskIssueUserMapper;

    @Autowired
    private HouseQmCheckTaskSquadMapper houseQmCheckTaskSquadMapper;
    @Autowired
    private HouseQmCheckTaskIssueAttachmentMapper houseQmCheckTaskIssueAttachmentMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByTaskIdUuidIn(Integer task_id, List<String> uuids) {
        return houseQmCheckTaskIssueMapper.searchHouseQmCheckTaskIssueByTaskIdUuidIn(task_id, uuids);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssue> searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit, Integer checker) {
        List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = new ArrayList<>();
        try {
            List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.searchByTaskIdUserIdRoleType(userId, task_id, checker);
            List<Integer> squadIds = new ArrayList<>();
            List<Integer> userIds = new ArrayList<>();
            userInHouseQmCheckTasks.forEach(userInHouseQmCheckTask -> {
                squadIds.add(userInHouseQmCheckTask.getSquadId());
            });
            List<UserInHouseQmCheckTask> userInHouseQmCheckTaskSearchSquadIdsList = userInHouseQmCheckTaskMapper.searchBySquadIdIn(squadIds);
            userInHouseQmCheckTaskSearchSquadIdsList.forEach(userInHouseQmCheckTask -> {
                userIds.add(userInHouseQmCheckTask.getUserId());
            });
            if (userIds.size() == 0) {
                userIds.add(userId);
            }
            userIds.add(userId);
            houseQmCheckTaskIssues = houseQmCheckTaskIssueMapper.searchByConditionOrderByPageUnscoped(task_id, last_id, timestamp, userIds, userId, start, limit);
        } catch (Exception e) {
            log.error("error" + e);
        }

        return houseQmCheckTaskIssues;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueUser> searchHouseQmCheckTaskIssueUserByTaskIdLastIdUpdateAtGt(Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit) {
        try {
            List<HouseQmCheckTaskIssueUser> houseQmCheckTaskIssueUsers = houseQmCheckTaskIssueUserMapper.searchByConditionOrderByPageUnscoped(task_id, last_id, timestamp, start, limit);
            return houseQmCheckTaskIssueUsers;
        } catch (Exception e) {
            log.error("error:" + e);
        }
        return null;
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<HouseQmCheckTaskIssueAttachment> searchHouseQmCheckTaskIssueAttachmentByMyIdTaskIdLastIdUpdateAtGt(Integer userId, Integer task_id, Integer last_id, Integer timestamp, Integer start, Integer limit,Integer privateInt,Integer publicInt ) {
        try {
            List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskMapper.searchByCondition(task_id, userId);
            List<Integer> squadIds = new ArrayList<>();
            userInHouseQmCheckTasks.forEach(userInHouseQmCheckTask -> {
                if (userInHouseQmCheckTask.getSquadId() != null) {
                    squadIds.add(userInHouseQmCheckTask.getSquadId());
                }
            });
            List<HouseQmCheckTaskSquad> houseQmCheckTaskSquads = null;
            List<Integer> existsSquadIds = new ArrayList<>();
            if (squadIds.size() > 0) {
                //找出所有组的，确保其有效
                houseQmCheckTaskSquads = houseQmCheckTaskSquadMapper.searchByInId(squadIds);
                houseQmCheckTaskSquads.forEach(houseQmCheckTaskSquad -> {
                    existsSquadIds.add(houseQmCheckTaskSquad.getId());
                });
            }
            //再根据组ID获取相关的组用户信息
            List<UserInHouseQmCheckTask> userInHouseQmCheckTaskSquadIdInList = userInHouseQmCheckTaskMapper.searchByTaskIdSquadIdIn(task_id, existsSquadIds);
            List<Integer> userIds = new ArrayList<>();
            userInHouseQmCheckTaskSquadIdInList.forEach(userInHouseQmCheckTask -> {
                userIds.add(userInHouseQmCheckTask.getUserId());
            });
            List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments=houseQmCheckTaskIssueAttachmentMapper.searchByConditionOrderByPageUnscoped(task_id,userId,timestamp,userIds,privateInt,publicInt,start,limit);
            return  houseQmCheckTaskIssueAttachments;
        } catch (Exception e) {
            log.error("error:" + e);
        }
        return null;
    }
}
