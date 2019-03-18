package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.service.ITaskListService;
import com.longfor.longjian.houseqm.app.vo.TaskList2Vo;
import com.longfor.longjian.houseqm.app.vo.TaskPushStrategyVo;
import com.longfor.longjian.houseqm.app.vo.TaskRoleListVo;
import com.longfor.longjian.houseqm.app.vo.task.CheckTaskIssueTypeStatInfo;
import com.longfor.longjian.houseqm.consts.ErrorEnum;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskConfig;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskMsg;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyAssignTime;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryOverdue;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryThreshold;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Houyan
 * @date 2018/12/13 0013 10:49
 */
@Repository
@Service
@Slf4j
public class TaskListServiceImpl implements ITaskListService {

    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private TeamService teamService;
    @Resource
    private PushStrategyAssignTimeService pushStrategyAssignTimeService;
    @Resource
    private PushStrategyCategoryOverdueService pushStrategyCategoryOverdueService;
    @Resource
    private PushStrategyCategoryThresholdService pushStrategyCategoryThresholdService;
    @Resource
    private UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;
    @Resource
    private UserService userService;
    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Value("${spe.team_group_100194.export_issue}")
    private String svrCfg;
    @Value("team_group_100194")
    private String teamGrop;
    private static final String USER_IDS="user_ids";

    // 通过taskIds获取以任务为索引的问题累计统计的map
    @Override
    public Map<Integer, CheckTaskIssueTypeStatInfo> searchTaskIssueStatMapByTaskIds(List<Integer> taskIds) {
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByTaskIdInGroupByTaskIdAndStatus(taskIds);
        Map<Integer, CheckTaskIssueTypeStatInfo> statMap = Maps.newHashMap();
        HashMap<Integer, HouseQmCheckTaskIssueStatusEnum> map = Maps.newHashMap();
        map.put(HouseQmCheckTaskIssueStatusEnum.NoProblem.getId(),HouseQmCheckTaskIssueStatusEnum.NoProblem);
        map.put(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId(),HouseQmCheckTaskIssueStatusEnum.NoteNoAssign);
        map.put(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId(),HouseQmCheckTaskIssueStatusEnum.AssignNoReform);
        map.put(HouseQmCheckTaskIssueStatusEnum.Repairing.getId(),HouseQmCheckTaskIssueStatusEnum.Repairing);
        map.put(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId(),HouseQmCheckTaskIssueStatusEnum.ReformNoCheck);
        map.put(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId(),HouseQmCheckTaskIssueStatusEnum.CheckYes);
        map.put(HouseQmCheckTaskIssueStatusEnum.Cancel.getId(),HouseQmCheckTaskIssueStatusEnum.Cancel);

        for (HouseQmCheckTaskIssue res : issues) {
            if (!statMap.containsKey(res.getTaskId())) {
                CheckTaskIssueTypeStatInfo value = new CheckTaskIssueTypeStatInfo();
                value.setIssueCount(0);
                value.setRecordCount(0);
                value.setIssueRecordedCount(0);
                value.setIssueAssignedCount(0);
                value.setIssueRepairedCount(0);
                value.setIssueApprovededCount(0);

                statMap.put(res.getTaskId(), value);
            }

            CheckTaskIssueTypeStatInfo result = statMap.get(res.getTaskId());
            handleStatistics(res, result, map.get(res.getStatus()));
        }
        return statMap;
    }

    private void handleStatistics(HouseQmCheckTaskIssue res, CheckTaskIssueTypeStatInfo result, HouseQmCheckTaskIssueStatusEnum e) {
        if (e != null) {
            //处理详细统计数
            switch (e) {
                //已记录未分配
                case NoteNoAssign:
                    result.setIssueRecordedCount(res.getPosX());
                    break;
                //已分配未整改
                case AssignNoReform:
                    result.setIssueAssignedCount(res.getPosX());
                    break;
                //已整改未验收
                case ReformNoCheck:
                    result.setIssueRepairedCount(res.getPosX());
                    break;
                //已验收
                case CheckYes:
                    result.setIssueApprovededCount(res.getPosX());
                    break;
                default:
                    break;
            }
            //处理状态统计
            switch (e) {
                case NoProblem:
                    result.setRecordCount(result.getRecordCount() + res.getPosX());
                    break;
                case NoteNoAssign:
                case AssignNoReform:
                case ReformNoCheck:
                case CheckYes:
                    result.setIssueCount(result.getIssueCount() + res.getPosX());
                    break;
                default:
                    break;
            }

        }
    }

    public TaskList2Vo list(int teamId, int projectId, int categoryCls, int status) {
        TaskList2Vo taskListVo = new TaskList2Vo();
        List<ApiBuildingQmCheckTaskMsg> list = Lists.newArrayList();
        HouseQmCheckTask houseQmCheckTask = new HouseQmCheckTask();
        houseQmCheckTask.setProjectId(projectId);
        houseQmCheckTask.setCategoryCls(categoryCls);
        houseQmCheckTask.setStatus(status);
        List<HouseQmCheckTask> checkTaskList = houseQmCheckTaskService.selectByProjectIdAndCategoryClsAndStatus(houseQmCheckTask);
        if (checkTaskList.isEmpty()) {
            return taskListVo;
        }
        Set<Integer> taskIds = Sets.newHashSet();
        for (HouseQmCheckTask item : checkTaskList) {
            taskIds.add(item.getTaskId());
        }
        Map<Integer, ApiBuildingQmCheckTaskConfig> taskMap = creatTaskMap(taskIds);
        Team team = getTopTeam(teamId);
        //输出问题配置
        String exportIssueConfig = getExportIssueConfig(team.getTeamId());
        //任务推送策略
        TaskPushStrategyVo pushStrategyVo = creatTaskPushStrategyMap(taskIds);
        Map<Integer, PushStrategyAssignTime> assignTimeMap = pushStrategyVo.getAssignTimeMap();
        Map<Integer, PushStrategyCategoryOverdue> categoryOverdueMap = pushStrategyVo.getCategoryOverdueMap();
        Map<Integer, PushStrategyCategoryThreshold> categoryThresholdMap = pushStrategyVo.getCategoryThresholdMap();

        for (HouseQmCheckTask checkTask : checkTaskList) {
            ApiBuildingQmCheckTaskMsg task = new ApiBuildingQmCheckTaskMsg();
            BuildingqmServiceImpl.setTaskProperties(null, task, taskMap, checkTask);

            HashMap<String, Map> pushStrategy = Maps.newHashMap();
            if (assignTimeMap.containsKey(task.getTask_id())) {
                HashMap<String, Object> assignTime = Maps.newHashMap();
                String pushTime = DateUtil.dateToString(assignTimeMap.get(task.getTask_id()).getPushTime(), "yyyy-MM-dd HH:mm:ss");
                assignTime.put("push_time", pushTime);
                assignTime.put(USER_IDS, assignTimeMap.get(task.getTask_id()).getUserIds());
                pushStrategy.put("assign_time", assignTime);
            }
            if (categoryOverdueMap.containsKey(task.getTask_id())) {
                HashMap<String, Object> categoryOverdue = Maps.newHashMap();
                categoryOverdue.put("category_keys", categoryOverdueMap.get(task.getTask_id()).getCategoryKeys());
                categoryOverdue.put(USER_IDS, categoryOverdueMap.get(task.getTask_id()).getUserIds());
                pushStrategy.put("category_overdue", categoryOverdue);
            }

            if (categoryThresholdMap.containsKey(task.getTask_id())) {
                HashMap<Object, Object> categoryThreshold = Maps.newHashMap();
                categoryThreshold.put("category_keys", categoryThresholdMap.get(task.getTask_id()).getCategoryKeys());
                categoryThreshold.put(USER_IDS, categoryThresholdMap.get(task.getTask_id()).getUserIds());
                categoryThreshold.put("threshold", categoryThresholdMap.get(task.getTask_id()).getThreshold());
                pushStrategy.put("category_threshold", categoryThreshold);
            }
            String pushStrategyStr = JSONObject.toJSONString(pushStrategy);
            task.setPush_strategy_config(pushStrategyStr);
            //判断输出问题配置信息是否为null
            if (exportIssueConfig != null) {
                String url = exportIssueConfig + "?project_id=" + projectId + "&task_id=" + task.getTask_id();
                task.getExtra_ops().setExport_issue(url);
            }

            list.add(task);
        }

        taskListVo.setTask_list(list);
        return taskListVo;
    }

    @Override
    public TaskRoleListVo taskRole(Integer taskId) {
        List<UserInHouseQmCheckTask> userList = userInHouseQmCheckTaskService.searchByTaskIdAndNoDeleted(taskId);
        List<Integer> userIds = Lists.newArrayList();
        for (UserInHouseQmCheckTask user : userList) {
            userIds.add(user.getUserId());
        }
        Map<Integer, User> userMap = creatUsersMap(userIds);
        TaskRoleListVo taskRoleListVo = new TaskRoleListVo();
        List<TaskRoleListVo.TaskRoleVo> list = Lists.newArrayList();
        for (UserInHouseQmCheckTask user : userList) {
            TaskRoleListVo.TaskRoleVo item = taskRoleListVo.new TaskRoleVo();
            item.setId(user.getId());
            item.setUser_id(user.getUserId());
            item.setRole_type(user.getRoleType());
            item.setCan_approve(user.getCanApprove());
            item.setCan_direct_approve(user.getCanDirectApprove());
            item.setCan_reassign(user.getCanReassign());
            item.setTask_id(user.getTaskId());
            item.setSquad_id(user.getSquadId());
            if (userMap.containsKey(user.getUserId())) {
                item.setReal_name(userMap.get(user.getUserId()).getRealName());
            }
            list.add(item);
        }
        taskRoleListVo.setRole_list(list);
        return taskRoleListVo;
    }

    private Map<Integer, User> creatUsersMap(List<Integer> userIds) {
        List<User> userList = userService.searchByUserIdInAndNoDeleted(userIds);
        HashMap<Integer, User> userDict = Maps.newHashMap();
        for (User user : userList) {
            userDict.put(user.getUserId(), user);
        }
        return userDict;
    }

    private Map<Integer, ApiBuildingQmCheckTaskConfig> creatTaskMap(Set<Integer> taskIds) {
        HashMap<Integer, ApiBuildingQmCheckTaskConfig> taskMap = Maps.newHashMap();
        List<HouseQmCheckTask> taskList = houseQmCheckTaskService.selectByTaskIds(taskIds);
        for (HouseQmCheckTask task : taskList) {
            ApiBuildingQmCheckTaskConfig item = new ApiBuildingQmCheckTaskConfig();
            if (task.getConfigInfo() == null) {
                item.setRepairer_refund_permission(CheckTaskRepairerRefundPermission.No.getValue());
                item.setRepairer_follower_permission(CheckTaskRepairerFollowerPermission.CompleteRepair.getValue());
                item.setChecker_approve_permission(CheckerApprovePermission.No.getValue());
                item.setRepaired_picture_status(CheckTaskRepairedPictureEnum.UnForcePicture.getValue());
                item.setIssue_desc_status(CheckTaskIssueDescEnum.Arbitrary.getValue());
                item.setIssue_default_desc("(该问题无文字描述)");
            } else {
                JSONObject configData = JSON.parseObject(task.getConfigInfo());
                item.setRepairer_refund_permission(configData.getIntValue("repairer_refund_permission"));
                item.setRepairer_follower_permission(configData.getIntValue("repairer_follower_permission"));
                item.setChecker_approve_permission(configData.getIntValue("checker_approve_permission"));
                item.setRepaired_picture_status(configData.getIntValue("repaired_picture_status"));
                item.setIssue_desc_status(configData.getIntValue("issue_desc_status"));
                item.setIssue_default_desc(configData.getString("issue_default_desc"));
            }
            taskMap.put(task.getTaskId(), item);

        }
        return taskMap;
    }

    public Team getTopTeam(int teamId) {
        Team team = null;
        for (int i = 0; i < 15; i++) {
            team = teamService.selectByTeamId(teamId);
            if (team == null || team.getParentTeamId().equals(0)) {
                break;
            }
            teamId = team.getParentTeamId();
        }
        if (team == null || team.getParentTeamId() > 0) {
            log.warn(ErrorEnum.TEAM_PARENT_RECIRCLE.getMessage());
            throw new LjBaseRuntimeException(ErrorEnum.TEAM_PARENT_RECIRCLE.getCode(), ErrorEnum.TEAM_PARENT_RECIRCLE.getMessage());
        }
        return team;
    }

    private String getExportIssueConfig(int teamId) {
        String exportIssue = null;
        if (teamGrop.equals("team_group_" + teamId)) {
            exportIssue = svrCfg;
        }
        return exportIssue;
    }

    private TaskPushStrategyVo creatTaskPushStrategyMap(Set<Integer> taskIds) {
        TaskPushStrategyVo taskPushStrategyVo = new TaskPushStrategyVo();
        HashMap<Integer, PushStrategyAssignTime> assignTimeMap = Maps.newHashMap();
        List<PushStrategyAssignTime> pushStrategyAssignTimeList = pushStrategyAssignTimeService.searchByTaskIds(taskIds);
        for (PushStrategyAssignTime item : pushStrategyAssignTimeList) {
            assignTimeMap.put(item.getTaskId(), item);
        }
        HashMap<Integer, PushStrategyCategoryOverdue> strategyCategoryOverdueMap = Maps.newHashMap();
        List<PushStrategyCategoryOverdue> pushStrategyCategoryOverdueList = pushStrategyCategoryOverdueService.searchByTaskIds(taskIds);
        for (PushStrategyCategoryOverdue item : pushStrategyCategoryOverdueList) {
            strategyCategoryOverdueMap.put(item.getTaskId(), item);
        }
        List<PushStrategyCategoryThreshold> pushStrategyCategoryThresholdList = pushStrategyCategoryThresholdService.searchByTaskIds(taskIds);
        HashMap<Integer, PushStrategyCategoryThreshold> strategyCategoryThresholdMap = Maps.newHashMap();
        for (PushStrategyCategoryThreshold item : pushStrategyCategoryThresholdList) {
            strategyCategoryThresholdMap.put(item.getTaskId(), item);
        }

        taskPushStrategyVo.setAssignTimeMap(assignTimeMap);
        taskPushStrategyVo.setCategoryOverdueMap(strategyCategoryOverdueMap);
        taskPushStrategyVo.setCategoryThresholdMap(strategyCategoryThresholdMap);
        return taskPushStrategyVo;
    }

}
