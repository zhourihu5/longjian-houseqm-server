package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.houseqm.app.service.ITaskListService;
import com.longfor.longjian.houseqm.app.vo.TaskList2Vo;
import com.longfor.longjian.houseqm.app.vo.TaskPushStrategyVo;
import com.longfor.longjian.houseqm.app.vo.TaskRoleListVo;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskConfig;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskMsg;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Houyan
 * @date 2018/12/13 0013 10:49
 */
@Repository
@Service
@Slf4j
public class TaskListServiceImpl implements ITaskListService {

    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    TeamService teamService;

    @Resource
    PushStrategyAssignTimeService pushStrategyAssignTimeService;

    @Resource
    PushStrategyCategoryOverdueService pushStrategyCategoryOverdueService;

    @Resource
    PushStrategyCategoryThresholdService pushStrategyCategoryThresholdService;

    @Resource
    UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;

    @Resource
    UserService userService;

    /**
     * @param teamId
     * @param projectId
     * @param categoryCls
     * @param status
     * @return
     */
    public TaskList2Vo list(int teamId, int projectId, int categoryCls, int status) {
        TaskList2Vo taskListVo = new TaskList2Vo();
        List<ApiBuildingQmCheckTaskMsg> list = Lists.newArrayList();
        HouseQmCheckTask houseQmCheckTask = new HouseQmCheckTask();
        houseQmCheckTask.setProjectId(projectId);
        houseQmCheckTask.setCategoryCls(categoryCls);
        houseQmCheckTask.setStatus(status);
        List<HouseQmCheckTask> houseQmCheckTasks = houseQmCheckTaskService.selectByProjectIdAndCategoryClsAndStatus(houseQmCheckTask);
        if (houseQmCheckTasks.isEmpty()) {
            return taskListVo;
        }
        Set<Integer> taskIds = Sets.newHashSet();
        for (HouseQmCheckTask qmCheckTask : houseQmCheckTasks) {
            taskIds.add(qmCheckTask.getTaskId());
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

        for (HouseQmCheckTask checkTask : houseQmCheckTasks) {
            ApiBuildingQmCheckTaskMsg task = new ApiBuildingQmCheckTaskMsg();
            //TaskVo task = new TaskVo();
            task.setProject_id(checkTask.getProjectId());
            task.setTask_id(checkTask.getTaskId());
            task.setName(checkTask.getName());
            task.setStatus(checkTask.getStatus());
            task.setCategory_cls(checkTask.getCategoryCls());
            task.setRoot_category_key(checkTask.getRootCategoryKey());
            task.setArea_ids(checkTask.getAreaIds());
            task.setArea_types(checkTask.getAreaTypes());

            if (taskMap.containsKey(task.getTask_id())) {
                ApiBuildingQmCheckTaskConfig cfg = taskMap.get(task.getTask_id());
                task.setRepairer_refund_permission(cfg.getRepairer_refund_permission());
                task.setRepairer_follower_permission(cfg.getRepairer_follower_permission());
                task.setChecker_approve_permission(cfg.getChecker_approve_permission());
                task.setRepaired_picture_status(cfg.getRepaired_picture_status());
                task.setIssue_desc_status(cfg.getIssue_desc_status());
                task.setIssue_default_desc(cfg.getIssue_default_desc());
            } else {
                task.setRepairer_refund_permission(CheckTaskRepairerRefundPermission.No.getValue());
                task.setRepairer_follower_permission(CheckTaskRepairerFollowerPermission.CompleteRepair.getValue());
                task.setChecker_approve_permission(CheckerApprovePermission.No.getValue());
                task.setRepaired_picture_status(CheckTaskRepairedPictureEnum.UnForcePicture.getValue());
                task.setIssue_desc_status(CheckTaskIssueDescEnum.Arbitrary.getValue());
                task.setIssue_default_desc("(该问题无文字描述)");
            }
            task.setPlan_begin_on(DateUtil.datetimeToTimeStamp(checkTask.getPlanBeginOn()));
            task.setPlan_end_on(DateUtil.datetimeToTimeStamp(checkTask.getPlanEndOn()));
            task.setCreate_at(DateUtil.datetimeToTimeStamp(checkTask.getCreateAt()));
            task.setUpdate_at(DateUtil.datetimeToTimeStamp(checkTask.getUpdateAt()));
            task.setDelete_at(DateUtil.datetimeToTimeStamp(checkTask.getDeleteAt()));

            HashMap<String, Map> pushStrategy = Maps.newHashMap();
            if (assignTimeMap.containsKey(task.getTask_id())) {
                HashMap<String, Object> assignTime = Maps.newHashMap();
                String pushTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(assignTimeMap.get(task.getTask_id()).getPushTime());
                assignTime.put("push_time", pushTime);
                assignTime.put("user_ids", assignTimeMap.get(task.getTask_id()).getUserIds());
                pushStrategy.put("assign_time", assignTime);
            }
            if (categoryOverdueMap.containsKey(task.getTask_id())) {
                HashMap<String, Object> categoryOverdue = Maps.newHashMap();
                categoryOverdue.put("category_keys", categoryOverdueMap.get(task.getTask_id()).getCategoryKeys());
                categoryOverdue.put("user_ids", categoryOverdueMap.get(task.getTask_id()).getUserIds());
                pushStrategy.put("category_overdue", categoryOverdue);
            }

            if (categoryThresholdMap.containsKey(task.getTask_id())) {
                HashMap<Object, Object> categoryThreshold = Maps.newHashMap();
                categoryThreshold.put("category_keys", categoryThresholdMap.get(task.getTask_id()).getCategoryKeys());
                categoryThreshold.put("user_ids", categoryThresholdMap.get(task.getTask_id()).getUserIds());
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

    /**
     * @param userIds
     * @return
     */
    private Map<Integer, User> creatUsersMap(List<Integer> userIds) {
        List<User> userList = userService.searchByUserIdInAndNoDeleted(userIds);
        HashMap<Integer, User> userDict = Maps.newHashMap();
        for (User user : userList) {
            userDict.put(user.getUserId(), user);
        }
        return userDict;
    }

    /**
     * @param taskIds
     * @return
     */
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
                taskMap.put(task.getTaskId(), item);
            } else {
                JSONObject configData = JSON.parseObject(task.getConfigInfo());
                item.setRepairer_refund_permission(configData.getIntValue("repairer_refund_permission"));
                item.setRepairer_follower_permission(configData.getIntValue("repairer_follower_permission"));
                item.setChecker_approve_permission(configData.getIntValue("checker_approve_permission"));
                item.setRepaired_picture_status(configData.getIntValue("repaired_picture_status"));
                item.setIssue_desc_status(configData.getIntValue("issue_desc_status"));
                item.setIssue_default_desc(configData.getString("issue_default_desc"));
                taskMap.put(task.getTaskId(), item);
            }
        }
        return taskMap;
    }

    /**
     * @param teamId
     * @return
     */
    private Team getTopTeam(int teamId) {
        Team team = null;
        for (int i = 0; i < 15; i++) {
            team = teamService.selectByTeamId(teamId);
            if (team == null || team.getParentTeamId() == 0) {
                break;
            }
            teamId = team.getParentTeamId();
        }
        return team;
    }

    @Value("${spe.team_group_100194.export_issue}")
    String svrCfg;

    @Value("team_group_100194")
    String teamGrop;
    /**
     * @param teamId
     * @return
     */
    private String getExportIssueConfig(int teamId) {
        //读取生成配置的auto.yaml
        String export_issue=null;
        if (teamGrop.equals("team_group_" + teamId)){
            export_issue= svrCfg;
        }else {
            export_issue="";
        }
        return export_issue;
    }

    /**
     * @param taskIds
     * @return
     */
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
