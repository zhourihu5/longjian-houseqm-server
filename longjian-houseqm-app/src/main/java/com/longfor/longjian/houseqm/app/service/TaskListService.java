package com.longfor.longjian.houseqm.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.houseqm.app.vo.TaskList2Vo;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.app.vo.TaskPushStrategyVo;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskConfig;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskMsg;
import com.longfor.longjian.houseqm.po.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Houyan
 * @date 2018/12/13 0013 10:49
 */
@Repository
@Service
@Slf4j
public class TaskListService {

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

    /**
     *
     * @param teamId
     * @param projectId
     * @param categoryCls
     * @param status
     * @return
     */
    public TaskList2Vo list(int teamId,int projectId,int categoryCls,int status){
        TaskList2Vo taskListVo = new TaskList2Vo();
        List<ApiBuildingQmCheckTaskMsg> list = Lists.newArrayList();
        HouseQmCheckTask houseQmCheckTask = new HouseQmCheckTask();
        houseQmCheckTask.setProjectId(projectId);
        houseQmCheckTask.setCategoryCls(categoryCls);
        houseQmCheckTask.setStatus(status);
        List<HouseQmCheckTask> houseQmCheckTasks = houseQmCheckTaskService.selectByProjectIdAndCategoryClsAndStatus(houseQmCheckTask);
        if (houseQmCheckTasks.isEmpty()){
            return taskListVo;
        }
        Set<Integer> taskIds = Sets.newHashSet();
        for (HouseQmCheckTask qmCheckTask : houseQmCheckTasks) {
            taskIds.add(qmCheckTask.getTaskId());
        }
        Map<Integer, ApiBuildingQmCheckTaskConfig> taskMap = creatTaskMap(taskIds);
        Team team = getTopTeam(teamId);
        //输出问题配置
        String exportIssueConfig = getExportIssueConfig(teamId);
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

            if (taskMap.containsKey(task.getTask_id())){
                ApiBuildingQmCheckTaskConfig cfg = taskMap.get(task.getTask_id());
                task.setRepairer_refund_permission(cfg.getRepairer_refund_permission());
                task.setRepairer_follower_permission(cfg.getRepairer_follower_permission());
                task.setChecker_approve_permission(cfg.getChecker_approve_permission());
                task.setRepaired_picture_status(cfg.getRepaired_picture_status());
                task.setIssue_desc_status(cfg.getIssue_desc_status());
                task.setIssue_default_desc(cfg.getIssue_default_desc());
            }else {
                task.setRepairer_refund_permission(CheckTaskRepairerRefundPermission.No.getValue());
                task.setRepairer_follower_permission(CheckTaskRepairerFollowerPermission.CompleteRepair.getValue());
                task.setChecker_approve_permission(CheckerApprovePermission.No.getValue());
                task.setRepaired_picture_status(CheckTaskRepairedPictureEnum.UnForcePicture.getValue());
                task.setIssue_desc_status(CheckTaskIssueDescEnum.Arbitrary.getValue());
                task.setIssue_default_desc("(该问题无文字描述)");
            }
            task.setPlan_begin_on((int) (checkTask.getPlanBeginOn().getTime()/1000));
            task.setPlan_end_on((int)(checkTask.getPlanEndOn().getTime()/1000));
            task.setCreate_at((int)(checkTask.getCreateAt().getTime()/1000));
            task.setUpdate_at((int)(checkTask.getUpdateAt().getTime()/1000));
            task.setDelete_at((int)(checkTask.getDeleteAt().getTime()/1000));

            HashMap<String, Map> pushStrategy = Maps.newHashMap();
            if (assignTimeMap.containsKey(task.getTask_id())){
                HashMap<String, Object> assignTime = Maps.newHashMap();
                String pushTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(assignTimeMap.get(task.getTask_id()).getPushTime());
                assignTime.put("push_time", pushTime);
                assignTime.put("user_ids",assignTimeMap.get(task.getTask_id()).getUserIds());
                pushStrategy.put("assign_time", assignTime);
            }
            if (categoryOverdueMap.containsKey(task.getTask_id())) {
                HashMap<String, Object> categoryOverdue = Maps.newHashMap();
                categoryOverdue.put("category_keys", categoryOverdueMap.get(task.getTask_id()).getCategoryKeys());
                categoryOverdue.put("user_ids", categoryOverdueMap.get(task.getTask_id()).getUserIds());
                pushStrategy.put("category_overdue", categoryOverdue);
            }

            if (categoryThresholdMap.containsKey(task.getTask_id())){
                HashMap<Object, Object> categoryThreshold = Maps.newHashMap();
                categoryThreshold.put("category_keys", categoryThresholdMap.get(task.getTask_id()).getCategoryKeys());
                categoryThreshold.put("user_ids", categoryThresholdMap.get(task.getTask_id()).getUserIds());
                categoryThreshold.put("threshold", categoryThresholdMap.get(task.getTask_id()).getThreshold());
                pushStrategy.put("category_threshold", categoryThreshold);
            }
            String pushStrategyStr = JSONObject.toJSONString(pushStrategy);
            task.setPush_strategy_config(pushStrategyStr);
            //判断输出问题配置信息是否为null
            if (exportIssueConfig!=""||exportIssueConfig!=null){
                String url=exportIssueConfig+"?project_id="+projectId+"&task_id="+task.getTask_id();
                task.getExtra_ops().setExport_issue(url);
            }

            list.add(task);
        }

        taskListVo.setTask_list(list);
        return taskListVo;
    }

    /**
     *
     * @param taskIds
     * @return
     */
    private Map<Integer, ApiBuildingQmCheckTaskConfig> creatTaskMap(Set<Integer> taskIds){
        HashMap<Integer, ApiBuildingQmCheckTaskConfig> taskMap = Maps.newHashMap();
        List<HouseQmCheckTask> taskList = houseQmCheckTaskService.selectByTaskIds(taskIds);
        for (HouseQmCheckTask task : taskList) {
            ApiBuildingQmCheckTaskConfig item = new ApiBuildingQmCheckTaskConfig();
            if (task.getConfigInfo()==null){
                item.setRepairer_refund_permission(CheckTaskRepairerRefundPermission.No.getValue());
                item.setRepairer_follower_permission(CheckTaskRepairerFollowerPermission.CompleteRepair.getValue());
                item.setChecker_approve_permission(CheckerApprovePermission.No.getValue());
                item.setRepaired_picture_status(CheckTaskRepairedPictureEnum.UnForcePicture.getValue());
                item.setIssue_desc_status(CheckTaskIssueDescEnum.Arbitrary.getValue());
                item.setIssue_default_desc("(该问题无文字描述)");
                taskMap.put(task.getTaskId(), item);
            }else {
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
     *
     * @param teamId
     * @return
     */
    private Team getTopTeam(int teamId){
        Team team=null;
        for (int i=0;i<15;i++){
            team= teamService.selectByTeamId(teamId);
            if(team==null||team.getParentTeamId()==0){
                break;
            }
            teamId=team.getParentTeamId();
        }
        return team;
    }

    /**
     * 
     * @param teamId
     * @return
     */
    private String getExportIssueConfig(int teamId){

        return "";
    }

    /**
     * 
     * @param taskIds
     * @return
     */
    private TaskPushStrategyVo creatTaskPushStrategyMap(Set<Integer> taskIds){
        TaskPushStrategyVo taskPushStrategyVo = new TaskPushStrategyVo();
        HashMap<Integer, PushStrategyAssignTime> assignTimeMap = Maps.newHashMap();
        List<PushStrategyAssignTime> pushStrategyAssignTimeList =pushStrategyAssignTimeService.searchByTaskIds(taskIds);
        for (PushStrategyAssignTime item : pushStrategyAssignTimeList) {
            assignTimeMap.put(item.getTaskId(), item);
        }
        HashMap<Integer, PushStrategyCategoryOverdue> strategyCategoryOverdueMap = Maps.newHashMap();
        List<PushStrategyCategoryOverdue> pushStrategyCategoryOverdueList=pushStrategyCategoryOverdueService.searchByTaskIds(taskIds);
        for (PushStrategyCategoryOverdue item : pushStrategyCategoryOverdueList) {
            strategyCategoryOverdueMap.put(item.getTaskId(), item);
        }
        List<PushStrategyCategoryThreshold> pushStrategyCategoryThresholdList=pushStrategyCategoryThresholdService.searchByTaskIds(taskIds);
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
