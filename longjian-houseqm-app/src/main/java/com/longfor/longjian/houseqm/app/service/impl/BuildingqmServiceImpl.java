package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.CategoryClsTypeEnum;
import com.longfor.longjian.common.consts.ModuleInfoEnum;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.req.TaskEditReq;
import com.longfor.longjian.houseqm.app.req.TaskReq;
import com.longfor.longjian.houseqm.app.service.IBuildingqmService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskConfig;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.JsonUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:24
 */

@Repository
@Service
@Slf4j
public class BuildingqmServiceImpl implements IBuildingqmService {

    @Resource
    UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;

    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    HouseQmCheckTaskSquadService houseQmCheckTaskSquadService;

    @Resource
    HouseQmCheckTaskIssueUserService houseQmCheckTaskIssueUserService;

    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    @Resource
    HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;

    @Resource
    HouseQmCheckTaskIssueAttachmentService houseQmCheckTaskIssueAttachmentService;
    @Resource
    TasksService taskService;
    @Resource
    PushStrategyAssignTimeService pushStrategyAssignTimeService;
    @Resource
    PushStrategyCategoryOverdueService pushStrategyCategoryOverdueService;
    @Resource
    PushStrategyCategoryThresholdService pushStrategyCategoryThresholdService;

    /**
     * @param userId
     * @return
     */
    public TaskListVo myTaskList(Integer userId) {

        List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskService.searchByUserId(userId);
        Set<Integer> taskIds = Sets.newHashSet();

        for (UserInHouseQmCheckTask task : userInHouseQmCheckTasks) {
            taskIds.add(task.getTaskId());
        }

        Map<Integer, ApiBuildingQmCheckTaskConfig> apiBuildingQmCheckTaskConfigMap = Maps.newHashMap();
        List<HouseQmCheckTask> houseQmCheckTasks = houseQmCheckTaskService.selectByTaskIds(taskIds);

        fullTaskConfigVO(apiBuildingQmCheckTaskConfigMap, houseQmCheckTasks);
        List<HouseQmCheckTask> allHouseQmCheckTasks = houseQmCheckTaskService.selectByTaskIdsEvenDeleted(taskIds);
        List<TaskVo> vos = Lists.newArrayList();
        fullAllTaskConfigVO(apiBuildingQmCheckTaskConfigMap, allHouseQmCheckTasks, vos);
        TaskListVo taskListVo = new TaskListVo();
        taskListVo.setTask_list(vos);

        return taskListVo;
    }

    /**
     * @param taskIdsStr
     * @return
     */
    public TaskMemberListVo taskSquadsMembers(String taskIdsStr) {

        TaskMemberListVo taskMemberListVo = new TaskMemberListVo();
        if (StringUtils.isEmpty(taskIdsStr)) {
            return taskMemberListVo;
        }
        Set<Integer> taskIds = Sets.newHashSet();
        String[] ids = taskIdsStr.split(",");
        for (String id : ids) {
            taskIds.add(Integer.parseInt(id));
        }
        if (CollectionUtils.isEmpty(taskIds)) {
            return taskMemberListVo;
        }

        List<UserInHouseQmCheckTask> allUserTasks = userInHouseQmCheckTaskService.selectByTaskIdsEvenDeleted(taskIds);
        List<HouseQmCheckTaskSquad> allHouseQmCheckTasks = houseQmCheckTaskSquadService.selectByTaskIdsEvenDeleted(taskIds);

        List<TaskMemberListVo.MemberVo> memberListVo = Lists.newArrayList();
        List<TaskMemberListVo.SquadVo> squaListVo = Lists.newArrayList();

        for (HouseQmCheckTaskSquad task : allHouseQmCheckTasks) {
            TaskMemberListVo.SquadVo vo = taskMemberListVo.new SquadVo();
            vo.setId(task.getId());
            vo.setProject_id(task.getProjectId());
            vo.setTask_id(task.getTaskId());
            vo.setSquad_type(task.getSquadType());
            vo.setName(task.getName());
            vo.setUpdate_at((int) (DateUtil.datetimeToTimeStamp(task.getUpdateAt())));
            vo.setDelete_at((int) (DateUtil.datetimeToTimeStamp(task.getDeleteAt())));

            squaListVo.add(vo);
        }

        for (UserInHouseQmCheckTask task : allUserTasks) {
            TaskMemberListVo.MemberVo vo = taskMemberListVo.new MemberVo();
            vo.setId(task.getId());
            vo.setSquad_id(task.getSquadId());
            vo.setUser_id(task.getUserId());
            vo.setRole_type(task.getRoleType());
            vo.setCan_approve(task.getCanApprove());
            vo.setCan_direct_approve(task.getCanDirectApprove());
            vo.setCan_reassign(task.getCanReassign());
            vo.setTask_id(task.getTaskId());
            vo.setUpdate_at((int) (DateUtil.datetimeToTimeStamp(task.getUpdateAt())));
            vo.setDelete_at((int) (DateUtil.datetimeToTimeStamp(task.getDeleteAt())));
            memberListVo.add(vo);
        }

        taskMemberListVo.setMember_list(memberListVo);
        taskMemberListVo.setSquad_list(squaListVo);

        return taskMemberListVo;
    }


    /**
     * @param userId
     * @param taskId
     * @param timestamp
     * @return
     */
    public MyIssuePatchListVo myIssuePathList(int userId, int taskId, int timestamp) {
        MyIssuePatchListVo myIssuePatchListVo = new MyIssuePatchListVo();
        myIssuePatchListVo.setLog_list(Lists.newArrayList());
        myIssuePatchListVo.setAttachment_list(Lists.newArrayList());

        // 获取所有问题的uuid
        List<HouseQmCheckTaskIssueUser> houseQmCheckTaskIssueUsers = houseQmCheckTaskIssueUserService.searchByUserIdAndTaskIdAndCreateAt(userId, taskId, timestamp);
        Set<String> issueUuids = Sets.newHashSet();
        for (HouseQmCheckTaskIssueUser user : houseQmCheckTaskIssueUsers) {
            issueUuids.add(user.getIssueUuid());
        }
        // 如果issueUuids为空直接返回
        if (issueUuids.isEmpty()) {
            return myIssuePatchListVo;
        }
        // 获取问题的uuid
        List<HouseQmCheckTaskIssue> taskIssues = houseQmCheckTaskIssueService.searchByIssueUuidsAndclientCreateAt(issueUuids, timestamp);
        Set<String> taskIssueUuids = Sets.newHashSet();
        Map<String, HouseQmCheckTaskIssue> issueMap = Maps.newHashMap();
        for (HouseQmCheckTaskIssue taskIssue : taskIssues) {
            issueMap.put(taskIssue.getUuid(), taskIssue);
            taskIssueUuids.add(taskIssue.getUuid());
        }
        if (taskIssueUuids.isEmpty()) {
            return myIssuePatchListVo;
        }
        //获取问题日志信息
        List<HouseQmCheckTaskIssueLog> houseQmCheckTaskIssueLogs = houseQmCheckTaskIssueLogService.searchByIssueUuid(taskIssueUuids);
        List<MyIssuePatchListVo.LogVo> logs = Lists.newArrayList();
        for (HouseQmCheckTaskIssueLog issueLog : houseQmCheckTaskIssueLogs) {
            MyIssuePatchListVo.LogVo logVo = myIssuePatchListVo.new LogVo();
            logVo.setId(issueLog.getId());
            logVo.setProjectId(issueLog.getProjectId());
            logVo.setTaskId(issueLog.getTaskId());
            logVo.setUuid(issueLog.getUuid());
            logVo.setIssueUuid(issueLog.getIssueUuid());
            logVo.setSenderId(issueLog.getSenderId());
            logVo.setDesc(issueLog.getDesc());
            logVo.setStatus(issueLog.getStatus());
            logVo.setAttachmentMd5List(issueLog.getAttachmentMd5List());
            logVo.setAudioMd5List(issueLog.getAudioMd5List());
            logVo.setMemoAudioMd5List(issueLog.getMemoAudioMd5List());
            logVo.setClientCreateAt(DateUtil.datetimeToTimeStamp(issueLog.getClientCreateAt()));

            JSONObject dic_detail = JSONObject.parseObject(issueLog.getDetail());
            MyIssuePatchListVo.LogDetailVo detail = myIssuePatchListVo.new LogDetailVo();
            if (issueMap.get(issueLog.getIssueUuid()) != null) {
                detail.setTitle(issueMap.get(issueLog.getIssueUuid()).getTitle());
                detail.setArea_id(issueMap.get(issueLog.getIssueUuid()).getAreaId());
                detail.setPos_x(issueMap.get(issueLog.getIssueUuid()).getPosX());
                detail.setPos_y(issueMap.get(issueLog.getIssueUuid()).getPosY());
                detail.setTyp(issueMap.get(issueLog.getIssueUuid()).getTyp());
            }
            detail.setPlan_end_on(dic_detail.getIntValue("PlanEndOn"));
            detail.setEnd_on(dic_detail.getIntValue("EndOn"));
            detail.setRepairer_id(dic_detail.getIntValue("RepairerId"));
            detail.setRepairer_follower_ids(dic_detail.getString("RepairerFollowerIds"));
            detail.setCondition(dic_detail.getIntValue("Condition"));
            detail.setCategory_cls(dic_detail.getIntValue("CategoryCls"));
            detail.setCategory_key(dic_detail.getString("CategoryKey"));
            detail.setCheck_item_key(dic_detail.getString("CheckItemKey"));
            detail.setIssue_reason(dic_detail.getIntValue("IssueReason"));
            detail.setIssue_reason_detail(dic_detail.getString("IssueReasonDetail"));
            detail.setIssue_suggest(dic_detail.getString("IssueSuggest"));
            detail.setPotential_risk(dic_detail.getString("PotentialRisk"));
            detail.setPreventive_action_detail(dic_detail.getString("PreventiveActionDetail"));
            logVo.setDetail(detail);
            logVo.setUpdateAt(DateUtil.datetimeToTimeStamp(issueLog.getUpdateAt()));
            logVo.setDeleteAt(DateUtil.datetimeToTimeStamp(issueLog.getDeleteAt()));
            logs.add(logVo);
        }
        if (!logs.isEmpty()) myIssuePatchListVo.setLog_list(logs);
        //获取问题附件信息
        List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments = houseQmCheckTaskIssueAttachmentService.searchByIssueUuid(taskIssueUuids);
        List<MyIssuePatchListVo.AttachmentVo> attachments = Lists.newArrayList();
        for (HouseQmCheckTaskIssueAttachment attachment : houseQmCheckTaskIssueAttachments) {
            MyIssuePatchListVo.AttachmentVo attachmentVo = myIssuePatchListVo.new AttachmentVo();
            attachmentVo.setId(attachment.getId());
            attachmentVo.setProject_id(attachment.getProjectId());
            attachmentVo.setTask_id(attachment.getTaskId());
            attachmentVo.setIssue_uuid(attachment.getIssueUuid());
            attachmentVo.setUser_id(attachment.getUserId());
            attachmentVo.setPublic_type(attachment.getPublicType());
            attachmentVo.setAttachment_type(attachment.getAttachmentType());
            attachmentVo.setMd5(attachment.getMd5());
            attachmentVo.setStatus(attachment.getStatus());
            attachmentVo.setUpdate_at(DateUtil.datetimeToTimeStamp(attachment.getUpdateAt()));
            attachmentVo.setDelete_at(DateUtil.datetimeToTimeStamp(attachment.getDeleteAt()));
            attachments.add(attachmentVo);
        }
        if (!attachments.isEmpty()) myIssuePatchListVo.setAttachment_list(attachments);
        return myIssuePatchListVo;
    }

    @Override
    public void create(Integer uid, TaskReq taskReq) {
        List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(taskReq.getArea_ids(), ",");
        if (CollectionUtils.isEmpty(areaIds)) {
            throw new LjBaseRuntimeException(252, "区域不能为空");
        }
        List<Integer> areaTypes = StringSplitToListUtil.splitToIdsComma(taskReq.getArea_types(), ",");
        if (CollectionUtils.isEmpty(areaTypes)) {
            throw new LjBaseRuntimeException(256, "区域类型不能为空");
        }
        List<ApiBuildingQmCheckTaskSquadObjVo> groupsInfo = unmarshCheckerGroups(taskReq.getChecker_groups());
        if (CollectionUtils.isEmpty(groupsInfo)) {
            throw new LjBaseRuntimeException(260, "检查人组不能为空");
        }
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroups = createCheckerGroups(groupsInfo);
        List<Integer> repairerIds = StringSplitToListUtil.splitToIdsComma(taskReq.getRepairer_ids(), ",");
        if (CollectionUtils.isEmpty(repairerIds)) {
            throw new LjBaseRuntimeException(264, "整改人不能为空");
        }
        List<ApiBuildingQmTaskMemberGroupVo> repairerGroups = createRepairerGroups("整改人组", repairerIds);

        ConfigVo config = unmarshPushStrategy(taskReq.getPush_strategy_config());
        String planBeginOn = taskReq.getPlan_begin_on() + " 00:00:00";
        String planEndOn = taskReq.getPlan_end_on() + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date begin = sdf.parse(planBeginOn);
            Date endon = sdf.parse(planEndOn);
            if (DateUtil.datetimeToTimeStamp(endon) < DateUtil.datetimeToTimeStamp(begin)) {
                throw new LjBaseRuntimeException(277, "计划结束时间有误");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Execute(uid, taskReq, areaIds, areaTypes, planBeginOn, planEndOn, checkerGroups, repairerGroups, config);


    }

    @Override
    public List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId) {
      return  houseQmCheckTaskSquadService.searchHouseqmCheckTaskSquad(projectId,taskId);
    }

    @Override
    public void edit(Integer uid, TaskEditReq taskEditReq) {
        List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(taskEditReq.getArea_ids(), ",");
        if (CollectionUtils.isEmpty(areaIds)) {
            throw new LjBaseRuntimeException(305, "区域不能为空");
        }
        List<Integer> areaTypes = StringSplitToListUtil.splitToIdsComma(taskEditReq.getArea_types(), ",");
        if (CollectionUtils.isEmpty(areaTypes)) {
            throw new LjBaseRuntimeException(310, "区域类型不能为空");
        }
        List<ApiBuildingQmCheckTaskSquadObjVo> groupsInfo = unmarshCheckerGroups(taskEditReq.getChecker_groups());
        if (CollectionUtils.isEmpty(groupsInfo)) {
            throw new LjBaseRuntimeException(315, "检查人组不能为空");
        }
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroups = createCheckerGroups(groupsInfo);
        List<Integer> repairerIds = StringSplitToListUtil.splitToIdsComma(taskEditReq.getRepairer_ids(), ",");
        if (CollectionUtils.isEmpty(repairerIds)) {
            throw new LjBaseRuntimeException(318, "整改人不能为空");
        }
        List<ApiBuildingQmTaskMemberGroupVo> repairerGroups = createRepairerGroups("整改人组", repairerIds);

        ConfigVo config = unmarshPushStrategy(taskEditReq.getPush_strategy_config());
        String planBeginOn = taskEditReq.getPlan_begin_on() + " 00:00:00";
        String planEndOn = taskEditReq.getPlan_end_on() + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date begin = sdf.parse(planBeginOn);
            Date endon = sdf.parse(planEndOn);
            if (DateUtil.datetimeToTimeStamp(endon) < DateUtil.datetimeToTimeStamp(begin)) {
                throw new LjBaseRuntimeException(331, "计划结束时间有误");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private List<ApiBuildingQmTaskMemberGroupVo> createCheckerGroups(List<ApiBuildingQmCheckTaskSquadObjVo> groupsInfo) {
        ArrayList<ApiBuildingQmTaskMemberGroupVo> objects = Lists.newArrayList();
        for (int i = 0; i < groupsInfo.size(); i++) {
            List<Integer> userIds = StringSplitToListUtil.splitToIdsComma(groupsInfo.get(i).getUser_ids(), ",");
            List<Integer> approveIds = StringSplitToListUtil.splitToIdsComma(groupsInfo.get(i).getApprove_ids(), ",");
            List<Integer> directApproveIds = StringSplitToListUtil.splitToIdsComma(groupsInfo.get(i).getDirect_approve_ids(), ",");
            List<Integer> reassignIds = StringSplitToListUtil.splitToIdsComma(groupsInfo.get(i).getReassign_ids(), ",");
            ApiBuildingQmTaskMemberGroupVo groupVo = new ApiBuildingQmTaskMemberGroupVo();
            groupVo.setGroup_id(groupsInfo.get(i).getId());
            groupVo.setGroup_name(groupsInfo.get(i).getName());
            groupVo.setGroup_role(CheckTaskRoleType.Checker.getValue());
            //去重
            groupVo.setUser_ids((Integer) CollectionUtil.removeDuplicate(userIds).get(0));
            groupVo.setApprove_ids((Integer) CollectionUtil.removeDuplicate(approveIds).get(0));
            groupVo.setDirect_approve_ids((Integer) CollectionUtil.removeDuplicate(directApproveIds).get(0));
            groupVo.setReassign_ids((Integer) CollectionUtil.removeDuplicate(reassignIds).get(0));
            objects.add(groupVo);

        }
        return objects;
    }

    private void Execute(Integer uid, TaskReq taskReq, List<Integer> areaIds, List<Integer> areaTypes, String planBeginOn, String planEndOn, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups, List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, ConfigVo config) {
        Task task = new Task();
        task.setName(taskReq.getName());
        task.setProjectId(taskReq.getProject_id());
        task.setCreatorId(uid);
        task.setTyp(taskReq.getCategory_cls());
        int One = taskService.add(task);
        if (One <= 0) {
            log.info("create task failed");
            throw new LjBaseRuntimeException(296, "'创建任务失败'");
        }
        HashMap<String, Object> config_map = Maps.newHashMap();
        config_map.put("repairer_refund_permission", taskReq.getRepairer_refund_permission());
        config_map.put("repairer_follower_permission", taskReq.getRepairer_follower_permission());
        config_map.put("checker_approve_permission", taskReq.getChecker_approve_permission());
        config_map.put("repaired_picture_status", taskReq.getRepaired_picture_status());
        config_map.put("issue_desc_status", taskReq.getIssue_desc_status());
        config_map.put("issue_default_desc", taskReq.getIssue_default_desc());
        String config_info = JsonUtil.GsonString(config_map);
        HouseQmCheckTask houseQmCheckTask = new HouseQmCheckTask();
        houseQmCheckTask.setProjectId(taskReq.getProject_id());
        houseQmCheckTask.setTaskId(One);
        houseQmCheckTask.setName(taskReq.getName());
        houseQmCheckTask.setStatus(CheckTaskStatus.UnFinish.getValue());
        houseQmCheckTask.setCategoryCls(taskReq.getCategory_cls());
        houseQmCheckTask.setRootCategoryKey(taskReq.getRoot_category_key());
        houseQmCheckTask.setConfigInfo(config_info);
        houseQmCheckTask.setEndOn(new Date(0));//1970-01-01 08:00:00
        houseQmCheckTask.setCreator(uid);
        houseQmCheckTask.setEditor(0);
        houseQmCheckTask.setPlanBeginOn(stringToDate(planBeginOn));
        houseQmCheckTask.setPlanEndOn(stringToDate(planEndOn));
        houseQmCheckTask.setAreaIds(areaIds.toString());
        houseQmCheckTask.setAreaTypes(areaTypes.toString());
        //返回主键
        int one = houseQmCheckTaskService.add(houseQmCheckTask);
        HouseQmCheckTask checktaskObj=  houseQmCheckTaskService.selectById(one);
        if (one <= 0) {
            log.info("create check task failed");
            throw new LjBaseRuntimeException(325, "创建检查任务失败");
        }

        for (int i = 0; i < checkerGroups.size(); i++) {//group
            HouseQmCheckTaskSquad squad = new HouseQmCheckTaskSquad();
            squad.setProjectId(taskReq.getProject_id());
            squad.setTaskId(One);
            squad.setName(checkerGroups.get(i).getGroup_name());
            squad.setSquadType(checkerGroups.get(i).getGroup_role());
            //返回主键
            int squadInfo = houseQmCheckTaskSquadService.add(squad);
            if (squadInfo <= 0) {
                log.info("create task squad failed");
                throw new LjBaseRuntimeException(361, "'创建任务组失败'");
            }
            Integer user_ids = checkerGroups.get(i).getUser_ids();

            Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
            Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
            Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
            if (user_ids.equals(checkerGroups.get(i).getApprove_ids())) {
                canApprove = CheckTaskRoleCanApproveType.Yes.getValue();
            }
            if (user_ids.equals(checkerGroups.get(i).getApprove_ids()) && user_ids.equals(checkerGroups.get(i).getDirect_approve_ids())) {
                canDirectApprove = CheckTaskRoleCanDirectApproveType.Yes.getValue();
            }
            if (user_ids.equals(checkerGroups.get(i).getReassign_ids())) {
                canReassign = CheckTaskRoleCanReassignType.Yes.getValue();
            }
            UserInHouseQmCheckTask qmCheckTask = new UserInHouseQmCheckTask();
            qmCheckTask.setSquadId(squadInfo);
            qmCheckTask.setUserId(user_ids);
            qmCheckTask.setRoleType(checkerGroups.get(i).getGroup_role());
            qmCheckTask.setProjectId(taskReq.getProject_id());
            qmCheckTask.setTaskId(One);
            qmCheckTask.setCanApprove(canApprove);
            qmCheckTask.setCanDirectApprove(canDirectApprove);
            qmCheckTask.setCanReassign(canReassign);
            int num = userInHouseQmCheckTaskService.add(qmCheckTask);
            if (num <= 0) {
                log.info("create task user failed");
                throw new LjBaseRuntimeException(389, "创建任务组人员失败");
            }
        }

        for (int i = 0; i < repairerGroups.size(); i++) {//group
            //返回主键
            HouseQmCheckTaskSquad squad = new HouseQmCheckTaskSquad();
            squad.setProjectId(taskReq.getProject_id());
            squad.setTaskId(One);
            squad.setName(checkerGroups.get(i).getGroup_name());
            squad.setSquadType(checkerGroups.get(i).getGroup_role());
            int squadInfo = houseQmCheckTaskSquadService.add(squad);
            if (squadInfo <= 0) {
                log.info("create task squad failed");
                throw new LjBaseRuntimeException(402, "'创建任务组失败'");
            }
            Integer userId = checkerGroups.get(i).getUser_ids();

            Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
            Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
            Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();

            UserInHouseQmCheckTask qmCheckTask = new UserInHouseQmCheckTask();
            qmCheckTask.setSquadId(squadInfo);
            qmCheckTask.setUserId(userId);
            qmCheckTask.setRoleType(checkerGroups.get(i).getGroup_role());
            qmCheckTask.setProjectId(taskReq.getProject_id());
            qmCheckTask.setTaskId(One);
            qmCheckTask.setCanApprove(canApprove);
            qmCheckTask.setCanDirectApprove(canDirectApprove);
            qmCheckTask.setCanReassign(canReassign);
            int num = userInHouseQmCheckTaskService.add(qmCheckTask);
            if (num <= 0) {
                log.info("create task user failed");
                throw new LjBaseRuntimeException(422, "创建任务组人员失败");
            }
        }

        //  # 指定日期发起推送配置
        if (config.getConfig_assign_time() != null) {
            PushStrategyAssignTime pushStrategyAssignTime = new PushStrategyAssignTime();
            pushStrategyAssignTime.setProjectId(taskReq.getProject_id());
            pushStrategyAssignTime.setTaskId(One);
            pushStrategyAssignTime.setModuleId(convertCategoryCls(taskReq.getCategory_cls()));
            pushStrategyAssignTime.setTyp(1);
            pushStrategyAssignTime.setPushTime(stringToDate(config.getConfig_assign_time().getPush_time()));
            pushStrategyAssignTime.setUserIds(config.getConfig_assign_time().getUser_ids());
            int Num = pushStrategyAssignTimeService.add(pushStrategyAssignTime);
            if (Num <= 0) {
                log.info("PushStrategyAssignTimeDao().add failed");
            }


        }
        //  # 超期问题发起推送配置
        if (config.getConfig_category_overdue() != null) {
            PushStrategyCategoryOverdue pushStrategyCategoryOverdue = new PushStrategyCategoryOverdue();
            pushStrategyCategoryOverdue.setProjectId(taskReq.getProject_id());
            pushStrategyCategoryOverdue.setTaskId(One);
            pushStrategyCategoryOverdue.setModuleId(convertCategoryCls(taskReq.getCategory_cls()));
            pushStrategyCategoryOverdue.setTyp(1);
            pushStrategyCategoryOverdue.setCategoryKeys(config.getConfig_category_overdue().getCategory_keys());
            pushStrategyCategoryOverdue.setUserIds(config.getConfig_category_overdue().getUser_ids());
            pushStrategyCategoryOverdue.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(checktaskObj.getPlanEndOn())+ ( 30 * 24 * 60 * 60)," yyyy-MM-dd-HH-mm-ss"));
            int Num = pushStrategyCategoryOverdueService.add(pushStrategyCategoryOverdue);
            if (Num <= 0) {
                log.info("PushStrategyCategoryOverdueDao().add failed");
            }

        }
        //  # 高发问题发起推送配置
        if (config.getConfig_category_threshold() != null) {
            PushStrategyCategoryThreshold pushStrategyCategoryThreshold = new PushStrategyCategoryThreshold();
            pushStrategyCategoryThreshold.setProjectId(taskReq.getProject_id());
            pushStrategyCategoryThreshold.setTaskId(One);
            pushStrategyCategoryThreshold.setModuleId(convertCategoryCls(taskReq.getCategory_cls()));
            pushStrategyCategoryThreshold.setTyp(1);
            pushStrategyCategoryThreshold.setCategoryKeys(config.getConfig_category_threshold().getCategory_keys());
            pushStrategyCategoryThreshold.setUserIds(config.getConfig_category_threshold().getUser_ids());
            pushStrategyCategoryThreshold.setThreshold(config.getConfig_category_threshold().getThreshold());
            pushStrategyCategoryThreshold.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(checktaskObj.getPlanEndOn())+ ( 30 * 24 * 60 * 60)," yyyy-MM-dd-HH-mm-ss"));
            int Num = pushStrategyCategoryThresholdService.add(pushStrategyCategoryThreshold);
            if (Num <= 0) {
                log.info("PushStrategyCategoryOverdueDao().add failed");
            }

        }


    }

    private Integer convertCategoryCls(Integer category_cls) {
        CategoryClsTypeEnum[] values = CategoryClsTypeEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (category_cls.equals(values[i].getValue())) {
                return ModuleInfoEnum.GCGL.getValue();
            }
        }
        return null;
    }


    /**
     * string 转date
     *
     * @param time
     * @return
     */
    private Date stringToDate(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ConfigVo unmarshPushStrategy(String push_strategy_config) {
        ConfigVo configVo = new ConfigVo();
        if (StringUtils.isEmpty(push_strategy_config)) {
            return new ConfigVo();
        }
        Map<String, Object> push_strategys_config =   JSON.parseObject(push_strategy_config,Map.class);
        Map assign_time = (Map) push_strategys_config.get("assign_time");
        Map category_overdue = (Map) push_strategys_config.get("category_overdue");
        Map category_threshold = (Map) push_strategys_config.get("category_threshold");
        if (assign_time != null) {
            String push_time = (String) assign_time.get("push_time");
            String user_ids = (String) assign_time.get("user_ids");
            if (push_time.length() > 0 && user_ids.length() > 0) {
                ConfigVo.ApiPushStrategyAssignTime config_assign_time = new ConfigVo().new ApiPushStrategyAssignTime();
                config_assign_time.setPush_time(push_time);
                config_assign_time.setUser_ids(user_ids);
                configVo.setConfig_assign_time(config_assign_time);
            }
            if (category_overdue != null) {
                String category_keys = (String) category_overdue.get("category_keys");
                String userIds = (String) category_overdue.get("user_ids");
                if (category_keys.length() > 0 && userIds.length() > 0) {
                    ConfigVo.ApiPushStrategyCategoryOverdue config_category_overdue = new ConfigVo().new ApiPushStrategyCategoryOverdue();
                    config_category_overdue.setCategory_keys(category_keys);
                    config_category_overdue.setUser_ids(userIds);
                    configVo.setConfig_category_overdue(config_category_overdue);
                }

            }
            if (category_threshold != null) {
                String category_keys = (String) category_threshold.get("category_keys");
                String userIds = (String) category_threshold.get("user_ids");
                Integer threshold = (Integer) category_threshold.get("threshold");
                if (category_keys.length() > 0 && userIds.length() > 0 && threshold > 0) {
                    ConfigVo.ApiPushStrategyCategoryThreshold config_category_threshold = new ConfigVo().new ApiPushStrategyCategoryThreshold();
                    config_category_threshold.setCategory_keys(category_keys);
                    config_category_threshold.setUser_ids(userIds);
                    config_category_threshold.setThreshold(threshold);
                    configVo.setConfig_category_threshold(config_category_threshold);
                }

            }

        }
        return configVo;
    }

    private List<ApiBuildingQmTaskMemberGroupVo> createRepairerGroups(String name, List<Integer> repairerIds) {
        ArrayList<ApiBuildingQmTaskMemberGroupVo> result = Lists.newArrayList();
        ApiBuildingQmTaskMemberGroupVo vo = new ApiBuildingQmTaskMemberGroupVo();
        vo.setGroup_name(name);
        vo.setGroup_role(CheckTaskRoleType.Repairer.getValue());
        List<Integer> list = CollectionUtil.removeDuplicate(repairerIds);
        for (int i = 0; i < list.size(); i++) {
            vo.setUser_ids(list.get(i));
        }
        result.add(vo);
        return result;
    }

    private List<ApiBuildingQmCheckTaskSquadObjVo> unmarshCheckerGroups(String checker_groups) {
        Map<String, Object> checkergroups = JSON.parseObject(checker_groups,Map.class);
        ApiBuildingQmCheckTaskSquadObjVo objVo = new ApiBuildingQmCheckTaskSquadObjVo();
        objVo.setId((Integer) checkergroups.get("id"));
        objVo.setName((String) checkergroups.get("name"));
        if (objVo.getName() == null) {
            log.info("name not exist, data='unmarshCheckerGroups'");
            throw new LjBaseRuntimeException(275, "name not exist");
        }
        objVo.setUser_ids((String) checkergroups.get("user_ids"));
        if (objVo.getUser_ids() == null) {
            log.info("name not exist, data='unmarshCheckerGroups'");
            throw new LjBaseRuntimeException(280, "user_ids not exist");
        }
        objVo.setApprove_ids((String) checkergroups.get("approve_ids"));
        if (objVo.getApprove_ids() == null) {
            log.info("name not exist, data='unmarshCheckerGroups'");
            throw new LjBaseRuntimeException(285, "approve_ids not exist");
        }
        objVo.setDirect_approve_ids((String) checkergroups.get("direct_approve_ids"));
        if (objVo.getDirect_approve_ids() == null) {
            log.info("name not exist, data='unmarshCheckerGroups'");
            throw new LjBaseRuntimeException(290, "direct_approve_ids not exist");
        }
        objVo.setReassign_ids((String) checkergroups.get("reassign_ids"));
        ArrayList<ApiBuildingQmCheckTaskSquadObjVo> result = Lists.newArrayList();
        result.add(objVo);
        return result;

    }


    /**
     * @param maps
     * @param houseQmCheckTasks
     */
    private void fullAllTaskConfigVO(Map<Integer, ApiBuildingQmCheckTaskConfig> maps,
                                     List<HouseQmCheckTask> houseQmCheckTasks, List<TaskVo> vos) {

        if (CollectionUtils.isEmpty(houseQmCheckTasks)) {
            return;
        }

        for (HouseQmCheckTask item : houseQmCheckTasks) {

            TaskVo task = new TaskVo();
            task.setTask_id(item.getTaskId());
            task.setProject_id(item.getProjectId());
            task.setName(item.getName());
            task.setStatus(item.getStatus());
            task.setCategory_cls(item.getCategoryCls());
            task.setRoot_category_key(item.getRootCategoryKey());
            task.setArea_ids(item.getAreaIds());
            // 有可能是area_type?
            task.setArea_types(item.getAreaTypes());
            task.setPlan_begin_on((int) (item.getPlanBeginOn().getTime() / 1000));
            task.setPlan_end_on((int) (item.getPlanEndOn().getTime() / 1000));
            task.setCreate_at((int) (item.getCreateAt().getTime() / 1000));
            task.setUpdate_at((int) (item.getUpdateAt().getTime() / 1000));
            task.setDelete_at(DateUtil.datetimeToTimeStamp(item.getDeleteAt()));

            if (maps.containsKey(task.getTask_id())) {

                ApiBuildingQmCheckTaskConfig cfg = maps.get(task.getTask_id());
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
                task.setIssue_default_desc("该问题无文字描述");
            }
            vos.add(task);
        }
    }


    /**
     * @param maps
     * @param houseQmCheckTasks
     */
    private void fullTaskConfigVO(Map<Integer, ApiBuildingQmCheckTaskConfig> maps,
                                  List<HouseQmCheckTask> houseQmCheckTasks) {

        if (CollectionUtils.isEmpty(houseQmCheckTasks)) {
            return;
        }

        for (HouseQmCheckTask task : houseQmCheckTasks) {

            ApiBuildingQmCheckTaskConfig item = new ApiBuildingQmCheckTaskConfig();
            if (StringUtils.isEmpty(task.getConfigInfo())) {

                item.setRepairer_refund_permission(CheckTaskRepairerRefundPermission.No.getValue());
                item.setRepairer_follower_permission(CheckTaskRepairerFollowerPermission.CompleteRepair.getValue());
                item.setChecker_approve_permission(CheckerApprovePermission.No.getValue());
                item.setRepaired_picture_status(CheckTaskRepairedPictureEnum.UnForcePicture.getValue());
                item.setIssue_desc_status(CheckTaskIssueDescEnum.Arbitrary.getValue());
                item.setIssue_default_desc("(该问题无文字描述)");
            } else {
                JSONObject configData = JSON.parseObject(task.getConfigInfo());
                item.setRepairer_refund_permission(getValueOrDefault(configData, "repairer_refund_permission",
                        CheckTaskRepairerRefundPermission.No.getValue()));
                item.setRepairer_follower_permission(getValueOrDefault(configData, "repairer_follower_permission",
                        CheckTaskRepairerFollowerPermission.CompleteRepair.getValue()));
                item.setChecker_approve_permission(getValueOrDefault(configData, "checker_approve_permission",
                        CheckerApprovePermission.No.getValue()));
                item.setRepaired_picture_status(getValueOrDefault(configData, "repaired_picture_status",
                        CheckTaskRepairedPictureEnum.UnForcePicture.getValue()));
                item.setIssue_desc_status(getValueOrDefault(configData, "issue_desc_status",
                        CheckTaskIssueDescEnum.Arbitrary.getValue()));
                item.setIssue_default_desc(getStringValueOrDefault(configData, "issue_default_desc", "(该问题无文字描述)"));
            }

            maps.put(task.getTaskId(), item);
        }
    }

    /**
     * @param configData
     * @param name
     * @param defautValue
     * @return
     */
    private Integer getValueOrDefault(JSONObject configData, String name, Integer defautValue) {
        Integer obj = configData.getInteger(name);
        if (obj == null) {
            return defautValue;
        } else {
            return obj;
        }
    }

    /**
     * @param configData
     * @param name
     * @param defautValue
     * @return
     */
    private String getStringValueOrDefault(JSONObject configData, String name, String defautValue) {

        String obj = configData.getString(name);

        if (configData.get(name) == null) {
            return defautValue;
        } else {
            return obj;
        }
    }


}
