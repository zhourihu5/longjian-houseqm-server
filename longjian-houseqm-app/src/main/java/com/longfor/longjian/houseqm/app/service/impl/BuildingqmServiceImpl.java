package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.*;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.kafka.KafkaProducer;
import com.longfor.longjian.houseqm.app.req.TaskEditReq;
import com.longfor.longjian.houseqm.app.req.TaskReq;
import com.longfor.longjian.houseqm.app.service.IBuildingqmService;
import com.longfor.longjian.houseqm.app.utils.ExportUtils;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.export.NodeDataVo;
import com.longfor.longjian.houseqm.app.vo.export.NodeVo;
import com.longfor.longjian.houseqm.consts.DropDataReasonEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskConfig;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyAssignTime;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryOverdue;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryThreshold;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static javax.swing.UIManager.get;

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
    @Resource
    UserService userService;
    @Resource
    FileResourceService fileResourceService;
    @Resource
    AreaService areaService;
    @Resource
    CategoryV3Service categoryV3Service;
    @Resource
    CheckItemV3Service checkItemV3Service;
    @Resource
    HouseQmCheckTaskNotifyRecordService houseQmCheckTaskNotifyRecordService;
    @Resource
    private KafkaProducer kafkaProducer;
    @Resource
    private IssueServiceImpl issueService;
   /* @Resource
    UmPushUtil umPushUtil;
    @Resource
    XmPushUtil xmPushUtil;*/


    /**
     * @param userId
     * @return
     */
    public TaskListVo myTaskList(Integer userId) {

        List<UserInHouseQmCheckTask> user_tasks = userInHouseQmCheckTaskService.searchByUserId(userId);
        Set<Integer> taskIds = Sets.newHashSet();

        for (UserInHouseQmCheckTask task : user_tasks) {
            taskIds.add(task.getTaskId());
        }

        Map<Integer, ApiBuildingQmCheckTaskConfig> task_map = creatTaskMap(taskIds);
        List<HouseQmCheckTask> houseqm_tasks = houseQmCheckTaskService.selectByTaskIdsEvenDeleted(taskIds);
        List<TaskVo> vos = Lists.newArrayList();
        for (HouseQmCheckTask item : houseqm_tasks) {
            TaskVo task = new TaskVo();
            task.setTask_id(item.getTaskId());
            task.setProject_id(item.getProjectId());
            task.setName(item.getName());
            task.setStatus(item.getStatus());
            task.setCategory_cls(item.getCategoryCls());
            task.setRoot_category_key(item.getRootCategoryKey());
            task.setArea_ids(item.getAreaIds());
            task.setArea_type(item.getAreaTypes());
            task.setPlan_begin_on((int) (item.getPlanBeginOn().getTime() / 1000));
            task.setPlan_end_on((int) (item.getPlanEndOn().getTime() / 1000));
            task.setCreate_at((int) (item.getCreateAt().getTime() / 1000));
            task.setUpdate_at((int) (item.getUpdateAt().getTime() / 1000));
            task.setDelete_at(DateUtil.datetimeToTimeStamp(item.getDeleteAt()));

            if (task_map.containsKey(task.getTask_id())) {

                ApiBuildingQmCheckTaskConfig cfg = task_map.get(task.getTask_id());
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


        //fullAllTaskConfigVO(task_map, houseqm_tasks, vos);
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
            throw new LjBaseRuntimeException(-99, "区域不能为空");
        }
        List<Integer> areaTypes = StringSplitToListUtil.splitToIdsComma(taskReq.getArea_types(), ",");
        if (CollectionUtils.isEmpty(areaTypes)) {
            throw new LjBaseRuntimeException(-99, "区域类型不能为空");
        }
        List<ApiBuildingQmCheckTaskSquadObjVo> groupsInfo = unmarshCheckerGroups(taskReq.getChecker_groups());
        if (CollectionUtils.isEmpty(groupsInfo)) {
            throw new LjBaseRuntimeException(-99, "检查人组不能为空");
        }
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroups = createCheckerGroups(groupsInfo);
        List<Integer> repairerIds = StringSplitToListUtil.splitToIdsComma(taskReq.getRepairer_ids(), ",");
        if (CollectionUtils.isEmpty(repairerIds)) {
            throw new LjBaseRuntimeException(-99, "整改人不能为空");
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
                throw new LjBaseRuntimeException(-99, "计划结束时间有误");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Execute(uid, taskReq, areaIds, areaTypes, planBeginOn, planEndOn, checkerGroups, repairerGroups, config);


    }

    @Override
    public List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId) {
        return houseQmCheckTaskSquadService.searchHouseqmCheckTaskSquad(projectId, taskId);
    }

    @Override
    public void edit(Integer uid, TaskEditReq taskEditReq) {
        List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(taskEditReq.getArea_ids(), ",");
        if (CollectionUtils.isEmpty(areaIds)) {
            throw new LjBaseRuntimeException(-99, "区域不能为空");
        }
        List<Integer> areaTypes = StringSplitToListUtil.splitToIdsComma(taskEditReq.getArea_types(), ",");
        if (CollectionUtils.isEmpty(areaTypes)) {
            throw new LjBaseRuntimeException(-99, "区域类型不能为空");
        }
        List<ApiBuildingQmCheckTaskSquadObjVo> groupsInfo = unmarshCheckerGroups(taskEditReq.getChecker_groups());
        if (CollectionUtils.isEmpty(groupsInfo)) {
            throw new LjBaseRuntimeException(-99, "检查人组不能为空");
        }
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroups = createCheckerGroups(groupsInfo);
        List<Integer> repairerIds = StringSplitToListUtil.splitToIdsComma(taskEditReq.getRepairer_ids(), ",");
        if (CollectionUtils.isEmpty(repairerIds)) {
            throw new LjBaseRuntimeException(-99, "整改人不能为空");
        }
        List<ApiBuildingQmTaskMemberGroupVo> repairerGroups = createRepairerGroups("整改人组", repairerIds);

        ConfigVo config = unmarshPushStrategy(taskEditReq.getPush_strategy_config());
        String planBeginOn = taskEditReq.getPlan_begin_on() + " 00:00:00";
        String planEndOn = taskEditReq.getPlan_end_on() + " 23:59:59";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date endon = null;
        try {
            begin = sdf.parse(planBeginOn);
            endon = sdf.parse(planEndOn);
            if (DateUtil.datetimeToTimeStamp(endon) < DateUtil.datetimeToTimeStamp(begin)) {
                throw new LjBaseRuntimeException(-99, "计划结束时间有误");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editExecute(begin, endon, uid, taskEditReq, areaIds, areaTypes, planBeginOn, planEndOn, checkerGroups, repairerGroups, config);

    }

    private void beforeExecute(List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsAdd, List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsEdit, List<Object> checkerGroupsDel, List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser, List<UserInHouseQmCheckTask> needUpdateCheckTaskSquadUser, Map doNotNeedDeleteSquaduserPkId, Integer uid, TaskEditReq taskEditReq, List<Integer> areaIds, List<Integer> areaTypes, String planBeginOn, String planEndOn, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups, List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, ConfigVo config) {
        checkSquads(needUpdateCheckTaskSquadUser, needInsertCheckTaskSquadUser, doNotNeedDeleteSquaduserPkId, checkerGroupsDel, checkerGroupsEdit, checkerGroupsAdd, uid, taskEditReq, areaIds, areaTypes, planBeginOn, planEndOn, checkerGroups, repairerGroups, config);
        compareSquadCheckers(repairerGroups, needUpdateCheckTaskSquadUser, needInsertCheckTaskSquadUser, checkerGroups, checkerGroupsDel, doNotNeedDeleteSquaduserPkId, taskEditReq);
        compareSquadRepairers(repairerGroups, taskEditReq, needInsertCheckTaskSquadUser, doNotNeedDeleteSquaduserPkId);
    }

    private void compareSquadCheckers(List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, List<UserInHouseQmCheckTask> needUpdateCheckTaskSquadUser, List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups, List<Object> checkerGroupsDel, Map doNotNeedDeleteSquaduserPkId, TaskEditReq taskEditReq) {
        List<UserInHouseQmCheckTask> dbItems = userInHouseQmCheckTaskService.selectByTaskIdAndRoleType(taskEditReq.getTask_id(), CheckTaskRoleType.Checker.getValue());
        HashMap<Object, Map<Integer, UserInHouseQmCheckTask>> squadUserMap = Maps.newHashMap();
        for (int i = 0; i < dbItems.size(); i++) {
            if (!squadUserMap.containsKey(dbItems.get(i).getSquadId())) {
                squadUserMap.put(dbItems.get(i).getSquadId(), new HashMap<Integer, UserInHouseQmCheckTask>());
            }
            squadUserMap.get(dbItems.get(i).getSquadId()).put(dbItems.get(i).getUserId(), dbItems.get(i));
            //   # 初始化，初始值都是标记为需要删除的
            doNotNeedDeleteSquaduserPkId.put(dbItems.get(i).getId(), false);
        }
        //  # 需要排除掉 新增 + 被删除的
        // # 但不能只判断小组信息有变动的组，因为只更新人员，是不会触发组信息变更的
        HashMap<Object, Object> ignoreSquadIdsMap = Maps.newHashMap();
        for (int i = 0; i < checkerGroupsDel.size(); i++) {
            ignoreSquadIdsMap.put(checkerGroupsDel.get(i), true);
        }
        //   # 排除新增的
        for (int i = 0; i < checkerGroups.size(); i++) {
            Integer groupId = checkerGroups.get(i).getGroup_id();
            if (groupId.equals(0)) {
                continue;
            }
            //  # 排除被删除的
            if (ignoreSquadIdsMap.containsKey(checkerGroups.get(i).getGroup_id())) {
                continue;
            }
            List<Integer> userIds = checkerGroups.get(i).getUser_ids();
            for (int j = 0; j < userIds.size(); j++) {
                Integer squadId = checkerGroups.get(i).getGroup_id();
                Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
                if (checkerGroups.get(i).getApprove_ids().contains(userIds.get(j))) {
                    canApprove = CheckTaskRoleCanApproveType.Yes.getValue();
                }
                if (checkerGroups.get(i).getDirect_approve_ids().contains(userIds.get(j)) && checkerGroups.get(i).getApprove_ids().contains(userIds.get(j))) {
                    canDirectApprove = CheckTaskRoleCanDirectApproveType.Yes.getValue();
                }
                if (checkerGroups.get(i).getReassign_ids().contains(userIds.get(j))) {
                    canReassign = CheckTaskRoleCanReassignType.Yes.getValue();
                }
                Map<Integer, UserInHouseQmCheckTask> map = squadUserMap.get(squadId);
                if (map != null && !map.containsKey(userIds.get(j))) {
                    ApiBuildingQmTaskMemberInsertVo vo = new ApiBuildingQmTaskMemberInsertVo();
                    vo.setSquad_id(squadId);
                    vo.setGroup_role(CheckTaskRoleType.Checker.getValue());
                    vo.setUser_id(userIds.get(j));
                    vo.setCan_approve(canApprove);
                    vo.setCan_direct_approve(canDirectApprove);
                    vo.setCan_reassign(canReassign);
                    needInsertCheckTaskSquadUser.add(vo);
                    continue;
                }
                //    # 将此记录标记为不需要删除
                if (map != null) {
                    UserInHouseQmCheckTask dbItem = map.get(userIds.get(j));
                    doNotNeedDeleteSquaduserPkId.put(dbItem.getId(), true);
                    if (!dbItem.getCanApprove().equals(canApprove) ||
                            !dbItem.getCanDirectApprove().equals(canDirectApprove) ||
                            !dbItem.getCanReassign().equals(canReassign)
                    ) {
                        dbItem.setCanApprove(canApprove);
                        dbItem.setCanDirectApprove(canDirectApprove);
                        dbItem.setCanReassign(canReassign);
                        needUpdateCheckTaskSquadUser.add(dbItem);
                    }
                }
            }

        }
    }


    private void compareSquadRepairers(List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, TaskEditReq taskEditReq, List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser, Map doNotNeedDeleteSquaduserPkId) {
        List<UserInHouseQmCheckTask> dbItems = userInHouseQmCheckTaskService.selectByTaskIdAndRoleType(taskEditReq.getTask_id(), CheckTaskRoleType.Repairer.getValue());
        Integer squadId = 0;
        HashMap<Integer, Object> squadUserMap = Maps.newHashMap();
        for (int i = 0; i < dbItems.size(); i++) {
            squadId = dbItems.get(i).getSquadId();
            squadUserMap.put(dbItems.get(i).getUserId(), dbItems.get(i));
            doNotNeedDeleteSquaduserPkId.put(dbItems.get(i).getId(), false);
        }
        for (int i = 0; i < repairerGroups.size(); i++) {
            for (int j = 0; j < repairerGroups.get(i).getUser_ids().size(); j++) {
                if (!squadUserMap.containsKey(repairerGroups.get(i).getUser_ids().get(j))) {
                    Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                    Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                    Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
                    ApiBuildingQmTaskMemberInsertVo vo = new ApiBuildingQmTaskMemberInsertVo();
                    vo.setSquad_id(squadId);
                    vo.setGroup_role(CheckTaskRoleType.Checker.getValue());
                    vo.setUser_id(repairerGroups.get(i).getUser_ids().get(j));
                    vo.setCan_approve(canApprove);
                    vo.setCan_direct_approve(canDirectApprove);
                    vo.setCan_reassign(canReassign);
                    needInsertCheckTaskSquadUser.add(vo);
                    continue;
                } else {
                    UserInHouseQmCheckTask o = (UserInHouseQmCheckTask) squadUserMap.get(repairerGroups.get(i).getUser_ids().get(j));
                    doNotNeedDeleteSquaduserPkId.put(o.getId(), true);
                }
            }
        }

    }

    private void checkSquads(List<UserInHouseQmCheckTask> needUpdateCheckTaskSquadUser, List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser, Map doNotNeedDeleteSquaduserPkId, List<Object> checkerGroupsDel, List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsEdit, List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsAdd, Integer uid, TaskEditReq taskEditReq, List<Integer> areaIds, List<Integer> areaTypes, String planBeginOn, String planEndOn, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups, List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, ConfigVo config) {
        HashMap<Object, ApiBuildingQmTaskMemberGroupVo> squadMap = Maps.newHashMap();
        for (int i = 0; i < checkerGroups.size(); i++) {
            if (checkerGroups.get(i).getGroup_id().equals(0)) {
                checkerGroupsAdd.add(checkerGroups.get(i));
            } else {
                squadMap.put(checkerGroups.get(i).getGroup_id(), checkerGroups.get(i));
            }
        }

        List<HouseQmCheckTaskSquad> dbItems = houseQmCheckTaskSquadService.selectByProjectIdAndTaskIdAndSquadType(taskEditReq.getProject_id(), taskEditReq.getTask_id(), CheckTaskRoleType.Checker.getValue());
        for (int i = 0; i < dbItems.size(); i++) {
            if (squadMap.containsKey(dbItems.get(i).getId())) {
                if (!squadMap.get(dbItems.get(i).getId()).getGroup_name().equals(dbItems.get(i).getName())) {
                    checkerGroupsEdit.add(squadMap.get(dbItems.get(i).getId()));
                }
            } else {
                checkerGroupsDel.add(dbItems.get(i).getId());
            }
        }
    }


    @Override
    public ApiIssueLogVo getIssueListLogByLastIdAndUpdataAt(Integer taskId, Integer timestamp, String issueUuid) {
        HouseQmCheckTaskIssue issueInfo = houseQmCheckTaskIssueService.selectByTaskIdAndUuidAndNotDel(taskId, issueUuid);
        ApiIssueLogVo.ApiIssueLogInfoIssueRsp issue = new ApiIssueLogVo().new ApiIssueLogInfoIssueRsp();
        ArrayList<ApiIssueLogVo.ApiIssueLogListRsp> issueLogList = Lists.newArrayList();
        if (issueInfo == null) {
            throw new LjBaseRuntimeException(-99, "没找到此问题");
        }
        ArrayList<Integer> usersId = Lists.newArrayList();
        usersId.add(issueInfo.getSenderId());
        usersId.add(issueInfo.getRepairerId());
        usersId.add(issueInfo.getLastAssigner());
        usersId.add(issueInfo.getLastRepairer());
        usersId.add(issueInfo.getDeleteUser());
        usersId.add(issueInfo.getDeleteUser());

        List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getRepairerFollowerIds().replace(",,", ","), ",", ",");
        ArrayList<Integer> followerIds = Lists.newArrayList();
        for (String id : list) {
            followerIds.add(Integer.valueOf(id));
        }
        followerIds.forEach(item -> {
            if (item < 0) {
                followerIds.remove(item);
            }
        });
        if (CollectionUtils.isNotEmpty(followerIds)) {
            usersId.addAll(followerIds);
        }
        ArrayList<String> fileMd5s = Lists.newArrayList();
        if (!issueInfo.getDrawingMD5().equals("")) {

            fileMd5s.add(issueInfo.getDrawingMD5());
        } else {
            fileMd5s.clear();
        }
        List<String> issueAttachmentMd5List = null;
        if (!issueInfo.getAttachmentMd5List().equals("")) {
            issueAttachmentMd5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getAttachmentMd5List().replace(",,", ","), ",", ",");
            fileMd5s.addAll(issueAttachmentMd5List);
        } else {
            issueAttachmentMd5List = Lists.newArrayList();
        }
        List<String> issueAudioMd5List = null;
        if (!issueInfo.getAudioMd5List().equals("")) {
            issueAudioMd5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getAttachmentMd5List().replace(",,", ","), ",", ",");
            fileMd5s.addAll(issueAudioMd5List);
        } else {
            issueAudioMd5List = Lists.newArrayList();
        }
        //  # issue log
        List<HouseQmCheckTaskIssueLog> issueLogInfo = houseQmCheckTaskIssueLogService.selectByUuidAndNotDelete(issueUuid);
        issueLogInfo.forEach(item -> {
            if (!item.getAttachmentMd5List().equals("")) {
                List<String> list1 = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getAttachmentMd5List().replace(",,", ","), ",", ",");
                fileMd5s.addAll(list1);
            }
            if (!item.getAudioMd5List().equals("")) {
                List<String> list1 = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getAudioMd5List().replace(",,", ","), ",", ",");
                fileMd5s.addAll(list1);
            }
            if (!item.getMemoAudioMd5List().equals("")) {
                List<String> list1 = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getMemoAudioMd5List().replace(",,", ","), ",", ",");
                fileMd5s.addAll(list1);
            }
            usersId.add(item.getSenderId());

        });
        //去重
        List<Integer> usersIds = CollectionUtil.removeDuplicate(usersId);
        List<User> userIdRealNameInfo = userService.searchByUserIdInAndNoDeleted(usersIds);
        HashMap<Integer, String> userIdRealNameMap = Maps.newHashMap();
        userIdRealNameInfo.forEach(item -> {
            userIdRealNameMap.put(item.getUserId(), item.getRealName());
        });
        List<FileResource> fileMd5StoreKeyInfo = fileResourceService.searchFileResourceByFileMd5InAndNoDeleted(fileMd5s);
        HashMap<String, String> fileMd5StoreKeyMap = Maps.newHashMap();
        fileMd5StoreKeyInfo.forEach(item -> {
            /*  i.store_key.split("://")[-1]*/
            fileMd5StoreKeyMap.put(item.getFileMd5(), item.getStoreKey());
        });
        List<String> areaIds = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getAreaPathAndId().replace("//", "/"), "/", "/");
        ArrayList<Integer> areaIdsList = Lists.newArrayList();
        areaIds.forEach(item -> {
            areaIdsList.add(Integer.valueOf(item));
        });
        List<Area> areaLeaveInfo = areaService.selectByAreaIds(areaIdsList);
        HashMap<Object, Object> areaIdNameMap = Maps.newHashMap();
        areaLeaveInfo.forEach(item -> {
            areaIdNameMap.put(item.getId(), item.getName());
        });
        List<String> categoryKeys = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getCategoryPathAndKey().replace("//", "/"), "/", "/");
        List<CategoryV3> categoryKeysInfo = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeys);
        HashMap<String, String> categoryKeyNameMap = Maps.newHashMap();
        categoryKeysInfo.forEach(item -> {
            categoryKeyNameMap.put(item.getKey(), item.getName());
        });
        List<String> checkItemKeys = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getCheckItemPathAndKey().replace("//", "/"), "/", "/");
        List<CheckItemV3> checkItemKeyNameInfo = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkItemKeys);
        HashMap<String, String> checkItemKeyNameMap = Maps.newHashMap();
        checkItemKeyNameInfo.forEach(item -> {
            checkItemKeyNameMap.put(item.getKey(), item.getName());
        });

        int timestampZero = DateUtil.datetimeToTimeStamp(new Date(0));
        Map<String, Object> issueDetailJson = JSON.parseObject(issueInfo.getDetail(), Map.class);
        ApiHouseQmCheckTaskIssueDetailVo issueDetail = new ApiHouseQmCheckTaskIssueDetailVo();
        issueDetail.setIssue_reason((Integer) issueDetailJson.get("IssueReason"));
        issueDetail.setIssue_reason_detail((String) issueDetailJson.get("IssueReasonDetail"));
        issueDetail.setIssue_suggest((String) issueDetailJson.get("IssueSuggest"));
        issueDetail.setPotential_risk((String) issueDetailJson.get("PotentialRisk"));
        issueDetail.setPreventive_action_detail((String) issueDetailJson.get("PreventiveActionDetail"));
        issue.setId(issueInfo.getId());
        issue.setProject_id(issueInfo.getProjectId());
        issue.setTask_id(issueInfo.getTaskId());
        issue.setUuid(issueInfo.getUuid());
        issue.setSender_name(userIdRealNameMap.get(issueInfo.getSenderId()));


        if (issueInfo.getPlanEndOn() != null && DateUtil.datetimeToTimeStamp(issueInfo.getPlanEndOn()) > timestampZero) {
            issue.setPlan_end_on(DateUtil.datetimeToTimeStamp(issueInfo.getPlanEndOn()));
        } else {
            issue.setPlan_end_on(0);
        }
        if (issueInfo.getEndOn() != null && DateUtil.datetimeToTimeStamp(issueInfo.getEndOn()) > timestampZero) {
            issue.setEnd_on(DateUtil.datetimeToTimeStamp(issueInfo.getEndOn()));
        } else {
            issue.setEnd_on(0);
        }
        issue.setArea_id(issueInfo.getAreaId());
        issue.setArea_path_and_id(issueInfo.getAreaPathAndId());
        List<Object> list1 = Lists.newArrayList();
        areaIdsList.forEach(item -> {
            if (areaIdNameMap.containsKey(item)) {
                list1.add(areaIdNameMap.get(item));
            }
        });
        issue.setArea_path_name(list1);
        issue.setCategory_cls(issueInfo.getCategoryCls());
        issue.setCategory_key(issueInfo.getCategoryKey());
        issue.setCategory_path_and_key(issueInfo.getCategoryPathAndKey());
        List<String> objects = Lists.newArrayList();
        categoryKeys.forEach(item -> {
            objects.add(categoryKeyNameMap.get(item));
        });
        issue.setCategory_path_name(objects);
        issue.setCheck_item_key(issueInfo.getCheckItemKey());
        issue.setCheck_item_path_and_key(issueInfo.getCheckItemPathAndKey());
        List<String> objects1 = Lists.newArrayList();
        checkItemKeys.forEach(item -> {
            objects1.add(checkItemKeyNameMap.get(item));
        });
        issue.setCheck_item_path_name(objects1);
        issue.setDrawing_url(fileMd5StoreKeyMap.get(issueInfo.getDrawingMD5()));
        issue.setDrawing_md5(issueInfo.getDrawingMD5());
        issue.setPos_x(issueInfo.getPosX());
        issue.setPos_y(issueInfo.getPosY());
        issue.setTitle(issueInfo.getTitle());
        issue.setTyp(issueInfo.getTyp());
        issue.setContent(issueInfo.getContent());
        issue.setCondition(issueInfo.getCondition());
        issue.setStatus(issueInfo.getStatus());
        List<String> objects2 = Lists.newArrayList();
        issueAttachmentMd5List.forEach(item -> {
            objects2.add(fileMd5StoreKeyMap.get(item));
        });
        issue.setAttachment_url_list(objects2);
        List<String> objects3 = Lists.newArrayList();
        issueAudioMd5List.forEach(item -> {
            objects3.add(fileMd5StoreKeyMap.get(item));
        });
        issue.setAudio_url_list(objects3);
        issue.setAttachment_md5_list(issueInfo.getAttachmentMd5List());
        issue.setAudio_md5_list(issueInfo.getAudioMd5List());
        issue.setRepairer_name(userIdRealNameMap.get(issueInfo.getRepairerId()));
        List<String> objects4 = Lists.newArrayList();
        followerIds.forEach(item -> {
            objects4.add(userIdRealNameMap.get(item));
        });
        issue.setRepairer_follower_names(objects4);
        if (issueInfo.getClientCreateAt() != null && DateUtil.datetimeToTimeStamp(issueInfo.getClientCreateAt()) > timestampZero) {
            issue.setClient_create_at(DateUtil.datetimeToTimeStamp(issueInfo.getClientCreateAt()));
        } else {
            issue.setClient_create_at(0);
        }
        issue.setLast_assigner_name(userIdRealNameMap.get(issueInfo.getLastAssigner()));
        if (issueInfo.getLastAssignAt() != null && DateUtil.datetimeToTimeStamp(issueInfo.getLastAssignAt()) > timestampZero) {
            issue.setLast_assigner_at(DateUtil.datetimeToTimeStamp(issueInfo.getLastAssignAt()));
        } else {
            issue.setLast_assigner_at(0);
        }
        issue.setLast_repairer_name(userIdRealNameMap.get(issueInfo.getRepairerId()));
        if (issueInfo.getLastRepairerAt() != null && DateUtil.datetimeToTimeStamp(issueInfo.getLastRepairerAt()) > timestampZero) {
            issue.setLast_repairer_at(DateUtil.datetimeToTimeStamp(issueInfo.getLastRepairerAt()));
        } else {
            issue.setLast_repairer_at(0);
        }
        issue.setDestroy_user_name(userIdRealNameMap.get(issueInfo.getDestroyUser()));
        if (issueInfo.getDestroyAt() != null && DateUtil.datetimeToTimeStamp(issueInfo.getDestroyAt()) > timestampZero) {
            issue.setDestroy_at(DateUtil.datetimeToTimeStamp(issueInfo.getDestroyAt()));
        } else {
            issue.setDestroy_at(0);
        }
        if (userIdRealNameMap.get(issueInfo.getDeleteUser()) != null) {
            issue.setDelete_user_name(userIdRealNameMap.get(issueInfo.getDeleteUser()));
        } else {
            issue.setDelete_user_name("");
        }
        if (issueInfo.getDeleteTime() != null && DateUtil.datetimeToTimeStamp(issueInfo.getDeleteTime()) > timestampZero) {
            issue.setDelete_time(DateUtil.datetimeToTimeStamp(issueInfo.getDeleteTime()));
        } else {
            issue.setDelete_time(0);
        }
        issue.setDetail(issueDetail);
        if (issueInfo.getUpdateAt() != null && DateUtil.datetimeToTimeStamp(issueInfo.getUpdateAt()) > timestampZero) {
            issue.setUpdate_at(DateUtil.datetimeToTimeStamp(issueInfo.getUpdateAt()));
        } else {
            issue.setUpdate_at(0);
        }
        if (issueInfo.getDeleteAt() != null && DateUtil.datetimeToTimeStamp(issueInfo.getDeleteAt()) > timestampZero) {
            issue.setDelete_at(DateUtil.datetimeToTimeStamp(issueInfo.getDeleteAt()));
        } else {
            issue.setDelete_at(0);
        }
        for (int i = 0; i < issueLogInfo.size(); i++) {
            if (issueLogInfo.get(i).getAttachmentMd5List().equals("") && issueLogInfo.get(i).getAudioMd5List().equals("") && issueLogInfo.get(i).getDesc().equals("")) {
                continue;
            }
            List<String> logAttachmentMd5 = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueLogInfo.get(i).getAttachmentMd5List().replace(",,", ","), ",", ",");
            if (logAttachmentMd5.contains("")) {
                logAttachmentMd5.clear();
            }
            List<String> audioMd5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueLogInfo.get(i).getAudioMd5List().replace(",,", ","), ",", ",");
            if (audioMd5List.contains("")) {
                audioMd5List.clear();
            }
            List<String> memoAudioMd5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueLogInfo.get(i).getMemoAudioMd5List().replace(",,", ","), ",", ",");
            if (memoAudioMd5List.contains("")) {
                memoAudioMd5List.clear();
            }
            ApiIssueLogVo.ApiIssueLogListRsp singleLog = new ApiIssueLogVo().new ApiIssueLogListRsp();
            singleLog.setId(issueLogInfo.get(i).getId());
            singleLog.setProject_id(issueLogInfo.get(i).getProjectId());
            singleLog.setTask_id(issueLogInfo.get(i).getTaskId());
            singleLog.setUuid(issueLogInfo.get(i).getUuid());
            singleLog.setIssue_uuid(issueLogInfo.get(i).getIssueUuid());
            singleLog.setSender_name(userIdRealNameMap.get(issueLogInfo.get(i).getSenderId()));
            singleLog.setDesc(issueLogInfo.get(i).getDesc());
            singleLog.setStatus(issueLogInfo.get(i).getStatus());
            singleLog.setAttachment_md5_list(issueLogInfo.get(i).getAttachmentMd5List());
            singleLog.setAudio_md5_list(issueLogInfo.get(i).getAudioMd5List());
            singleLog.setMemo_audio_md5_list(issueLogInfo.get(i).getMemoAudioMd5List());
            ArrayList<String> md5List = Lists.newArrayList();
            logAttachmentMd5.forEach(item -> {
                md5List.add(fileMd5StoreKeyMap.get(item));
            });
            singleLog.setAttachment_url_list(md5List);
            ArrayList<String> urlList = Lists.newArrayList();
            audioMd5List.forEach(item -> {
                urlList.add(fileMd5StoreKeyMap.get(item));
            });
            singleLog.setAudio_url_list(urlList);
            ArrayList<String> audioUrlList = Lists.newArrayList();
            memoAudioMd5List.forEach(item -> {
                audioUrlList.add(fileMd5StoreKeyMap.get(item));
            });
            singleLog.setMemo_audio_url_list(audioUrlList);

            if (issueLogInfo.get(i).getClientCreateAt() != null && DateUtil.datetimeToTimeStamp(issueLogInfo.get(i).getClientCreateAt()) > timestampZero) {
                singleLog.setClient_create_at(DateUtil.datetimeToTimeStamp(issueLogInfo.get(i).getClientCreateAt()));
            } else {
                singleLog.setClient_create_at(0);
            }

            if (issueLogInfo.get(i).getUpdateAt() != null && DateUtil.datetimeToTimeStamp(issueLogInfo.get(i).getUpdateAt()) > timestampZero) {
                singleLog.setUpdate_at(DateUtil.datetimeToTimeStamp(issueLogInfo.get(i).getUpdateAt()));
            } else {
                singleLog.setUpdate_at(0);
            }
            issueLogList.add(singleLog);
        }
        ApiIssueLogVo vo = new ApiIssueLogVo();
        vo.setIssue(issue);
        vo.setIssue_log_list(issueLogList);
        return vo;
    }

    @Override
    public ReportIssueVo reportIssue(Integer uid, Integer projectId, String data) {
        ArrayList<ReportIssueVo.ApiHouseQmCheckTaskReportMsg> dropped = Lists.newArrayList();
        UnmarshReportIssueRequestBody issueRequestBody = unmarshReportIssueRequest(data);
        List<String> dropLogUuids = checkLogUuid(issueRequestBody.getLog_uuids());
        IssueMapBody issueMapBody = createIcssueMap(issueRequestBody.getIssue_uuids());

        HashMap<String, HouseQmCheckTaskIssue> issueInsertMap = Maps.newHashMap();
        HashMap<Object, HouseQmCheckTaskIssue> issueUpdateMap = Maps.newHashMap();
        HashMap<String, ApiUserRoleInIssue> issueRoleMap = Maps.newHashMap();
        HashMap<String, HouseQmCheckTaskIssueAttachment> attachmentInsertMap = Maps.newHashMap();
        ArrayList<Object> attachmentRemoveList = Lists.newArrayList();
        ArrayList<HouseQmCheckTaskIssueLog> logInsertList = Lists.newArrayList();
        ArrayList<HouseQmCheckTaskNotifyRecord> notifyList = Lists.newArrayList();
        ArrayList<Object> pushList = Lists.newArrayList();
        HashMap<HouseQmCheckTaskIssue, ApiRefundInfo> refundMap = Maps.newHashMap();
        ArrayList<Integer> taskIds = Lists.newArrayList();
        List<ApiHouseQmCheckTaskIssueLogInfo> issueLogs = issueRequestBody.getIssue_logs();

        List deleteIssueUuids = issueMapBody.getDelete_issue_uuids();
        for (ApiHouseQmCheckTaskIssueLogInfo item : issueLogs) {
            //  # log已经提交过，直接忽略这些log
            if (dropLogUuids.contains(item.getUuid()) || deleteIssueUuids.contains(item.getIssue_uuid())) {
                ReportIssueVo.ApiHouseQmCheckTaskReportMsg msg = new ReportIssueVo().new ApiHouseQmCheckTaskReportMsg();
                msg.setUuid(item.getUuid());
                msg.setReason_type(DropDataReasonEnum.HouseQmIssueLogUuidExists.getId());
                msg.setReason(DropDataReasonEnum.HouseQmIssueLogUuidExists.getValue());
                dropped.add(msg);
                continue;
            }

            if (!taskIds.contains(item.getTask_id()) && item.getTask_id() > 0) {
                taskIds.add(item.getTask_id());
            }
            // # 公有录音
/*
            List<Integer> md5List = StringSplitToListUtil.splitToIdsComma(item.getAudio_md5_list(), ",");
*/
            List<String> md5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getAudio_md5_list(), ",", ",");
            if (CollectionUtils.isNotEmpty(md5List)) {
                md5List.forEach(items -> {
                    HouseQmCheckTaskIssueAttachment attachment = new HouseQmCheckTaskIssueAttachment();
                    attachment.setProjectId(projectId);
                    attachment.setTaskId(item.getTask_id());
                    attachment.setIssueUuid(item.getIssue_uuid());
                    attachment.setUserId(item.getSender_id());
                    attachment.setPublicType(CheckTaskIssueAttachmentPublicType.Public.getValue());
                    attachment.setAttachmentType(CheckTaskIssueAttachmentAttachmentType.Audio.getValue());
                    attachment.setMd5(String.valueOf(items));
                    attachment.setStatus(CheckTaskIssueAttachmentStatus.Enable.getValue());
                    attachment.setClientCreateAt(DateUtil.transForDate(item.getClient_create_at()));
                    attachmentInsertMap.put(items, attachment);
                });
            }
            //   # 私有录音
            List<String> memoMd5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getAudio_md5_list(), ",", ",");
            if (CollectionUtils.isNotEmpty(memoMd5List)) {
                memoMd5List.forEach(md5 -> {
                    HouseQmCheckTaskIssueAttachment attachment = new HouseQmCheckTaskIssueAttachment();
                    attachment.setProjectId(projectId);
                    attachment.setTaskId(item.getTask_id());
                    attachment.setIssueUuid(item.getIssue_uuid());
                    attachment.setUserId(item.getSender_id());
                    attachment.setPublicType(CheckTaskIssueAttachmentPublicType.Private.getValue());
                    attachment.setAttachmentType(CheckTaskIssueAttachmentAttachmentType.Audio.getValue());
                    attachment.setMd5(md5);
                    attachment.setStatus(CheckTaskIssueAttachmentStatus.Enable.getValue());
                    attachment.setClientCreateAt(DateUtil.transForDate(item.getClient_create_at()));
                    attachmentInsertMap.put(md5, attachment);
                });
            }
            //  # 要移除的私有录音
            for (ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo detail : item.getDetail()) {
                List<String> removeMemoMd5List = StringSplitToListUtil.removeStartAndEndStrAndSplit(detail.getRemove_memo_audio_md5_list(), ",", ",");
                if (CollectionUtils.isNotEmpty(removeMemoMd5List)) {
                    for (String memo : removeMemoMd5List) {
                        if (attachmentInsertMap.containsKey(memo)) {
                            attachmentInsertMap.remove(memo);
                            continue;
                        }
                        attachmentRemoveList.add(memo);
                    }
                }
            }
            //    # log已加入需要新建的issue队列里面，则更新issue的信息
            if (issueInsertMap.containsKey(item.getIssue_uuid())) {
                HouseQmCheckTaskIssue issue = (HouseQmCheckTaskIssue) issueInsertMap.get(item.getIssue_uuid());
                // # 已经销项的问题不再能够修改
                if (issue.getStatus().equals(CheckTaskIssueStatus.CheckYes.getValue())) {
                    ReportIssueVo.ApiHouseQmCheckTaskReportMsg msg = new ReportIssueVo().new ApiHouseQmCheckTaskReportMsg();
                    msg.setUuid(item.getUuid());
                    msg.setReason_type(DropDataReasonEnum.Other.getId());
                    msg.setReason("问题已销项");
                    dropped.add(msg);
                } else {
                    Map detail = JSON.parseObject(issue.getDetail(), Map.class);
                    String checkItemMD5 = (String) detail.get("CheckItemMD5");

                    List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail1 = item.getDetail();
                    for (int i = 0; i < detail1.size(); i++) {
                        if (isCheckItemChange(issue, item) || StringUtils.isEmpty(checkItemMD5) || StringUtils.isEmpty(detail1.get(i).getCheck_item_md5()) || detail1.get(i).getCheck_item_md5().equals(checkItemMD5)) {
                            Map<String,Object> modifyIssueMap = modifyIssue(refundMap, issueRoleMap, issue, item, true);
                            HouseQmCheckTaskIssue issues = (HouseQmCheckTaskIssue) modifyIssueMap.get("issue");
                            refundMap = (HashMap<HouseQmCheckTaskIssue, ApiRefundInfo>)modifyIssueMap.get("refundMap");
                            issueInsertMap.put(item.getIssue_uuid(), issues);
                            logInsertList.add(createIssueLog(projectId, item));

                        } else {
                            log.info("check_item info error, issue.check_item_md5=" + checkItemMD5 + ", log.check_item_md5=" + detail1.get(i).getCheck_item_md5() + "");
                        }
                    }
                }

            } else {
                //   # 如果issue已经存在DB中，则更新DB中的issue信息
                if (issueMapBody.getExist_issue_map().containsKey(item.getIssue_uuid())) {
                    HouseQmCheckTaskIssue issue = (HouseQmCheckTaskIssue) issueMapBody.getExist_issue_map().get(item.getIssue_uuid());
                    // # 已经销项的问题不再能够修改
                    if (issue.getStatus().equals(CheckTaskIssueStatus.CheckYes.getValue())) {
                        ReportIssueVo.ApiHouseQmCheckTaskReportMsg msg = new ReportIssueVo().new ApiHouseQmCheckTaskReportMsg();
                        msg.setUuid(item.getUuid());
                        msg.setReason("问题已销项");
                        msg.setReason_type(DropDataReason.Other.getValue());
                        dropped.add(msg);
                    } else {
                        Map detail = JSON.parseObject(issue.getDetail(), Map.class);
                        String checkItemMD5 = "";
                        if (StringUtils.isNotBlank((String) detail.get("CheckItemMD5"))) {
                            checkItemMD5 = (String) detail.get("CheckItemMD5");
                        } else {
                            checkItemMD5 = "";
                        }
                        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail1 = item.getDetail();
                        for (int i = 0; i < detail1.size(); i++) {
                            if (isCheckItemChange(issue, item) || StringUtils.isEmpty(checkItemMD5) || StringUtils.isEmpty(detail1.get(i).getCheck_item_md5()) || detail1.get(i).getCheck_item_md5().equals(checkItemMD5)) {
                                Map<String,Object> modifyIssueMap = modifyIssue(refundMap, issueRoleMap, issue, item, null);
                                HouseQmCheckTaskIssue issues = (HouseQmCheckTaskIssue) modifyIssueMap.get("issue");
                                refundMap = (HashMap<HouseQmCheckTaskIssue, ApiRefundInfo>)modifyIssueMap.get("refundMap");
                                issueUpdateMap.put(item.getIssue_uuid(), issues);
                                logInsertList.add(createIssueLog(projectId, item));

                            } else {
                                log.info("check_item info error, issue.check_item_md5=" + checkItemMD5 + ", log.check_item_md5=" + detail1.get(i).getCheck_item_md5() + "");
                            }
                        }
                    }
                } else {
                    HouseQmCheckTaskIssue issueLog = createIssue(issueRoleMap, uid, projectId, item);
                    issueInsertMap.put(item.getIssue_uuid(), issueLog);
                    logInsertList.add(createIssueLog(projectId, item));
                }
            }
        }

        // # 获取相关任务检查人组信息
        Map<Integer, Map<Integer, Map<Integer, Integer>>> checkerMap = createCheckerMap(taskIds);

        //  # 处理新增问题
        for (Map.Entry<String, HouseQmCheckTaskIssue> entry : issueInsertMap.entrySet()) {
            HouseQmCheckTaskIssue issue = (HouseQmCheckTaskIssue) entry.getValue();
            Integer res = houseQmCheckTaskIssueService.add(issue);
            if (res==null) {
                log.info("insert new issue failed, data=" + JSON.toJSONString(issue) + "");
            }else {
                issue.setId(res);
            }
            // # 写入推送记录
            if (CheckTaskIssueStatus.NoteNoAssign.getValue().equals(issue.getStatus())) {
                List<Integer> desUserIds = getIssueCheckerList(checkerMap, issue, null);
                desUserIds.forEach(userId -> {
                    pushList.add(userId);
                });
                if (CollectionUtils.isNotEmpty(desUserIds)) {
                    HouseQmCheckTaskNotifyRecord itemNotify = new HouseQmCheckTaskNotifyRecord();
                    itemNotify.setProjectId(issue.getProjectId());
                    itemNotify.setTaskId(issue.getTaskId());
                    itemNotify.setSrcUserId(0);
                    itemNotify.setDesUserIds(StringUtils.join(desUserIds, ","));
                    itemNotify.setModuleId(ModuleInfoEnum.GCGL.getValue());
                    itemNotify.setIssueId(issue.getId());
                    itemNotify.setIssueStatus(CheckTaskIssueStatus.NoteNoAssign.getValue());
                    itemNotify.setExtraInfo("");
                    notifyList.add(itemNotify);
                }
            } else if (CheckTaskIssueStatus.AssignNoReform.getValue().equals(issue.getStatus())) {
                ArrayList<Integer> desUserIds = Lists.newArrayList();
                if (issue.getRepairerId() > 0) {
                    desUserIds.add(issue.getRepairerId());
                    pushList.add(issue.getRepairerId());
                }
                List<Integer> idsComma = StringSplitToListUtil.splitToIdsComma(issue.getRepairerFollowerIds(), ",");
                idsComma.forEach(user -> {
                    if (user > 0 && !desUserIds.contains(user)) {
                        desUserIds.add(user);
                        pushList.add(user);
                    }
                });
                if (CollectionUtils.isNotEmpty(desUserIds)) {
                    HouseQmCheckTaskNotifyRecord itemNotify = new HouseQmCheckTaskNotifyRecord();
                    itemNotify.setProjectId(issue.getProjectId());
                    itemNotify.setTaskId(issue.getTaskId());
                    itemNotify.setSrcUserId(0);
                    itemNotify.setDesUserIds(StringUtils.join(desUserIds, ","));
                    itemNotify.setModuleId(ModuleInfoEnum.GCGL.getValue());
                    itemNotify.setIssueId(issue.getId());
                    itemNotify.setIssueStatus(CheckTaskIssueStatus.AssignNoReform.getValue());
                    itemNotify.setExtraInfo("");
                    notifyList.add(itemNotify);
                }
            }
        }

        //  # 处理更新问题
        for (Map.Entry<Object, HouseQmCheckTaskIssue> entry : issueUpdateMap.entrySet()) {
            HouseQmCheckTaskIssue issue = (HouseQmCheckTaskIssue) entry.getValue();
            try {
                houseQmCheckTaskIssueService.update(issue);
            } catch (Exception e) {
                log.info("insert new issue failed, data=" + JSON.toJSONString(issue) + "");
                e.printStackTrace();
            }
            // # 写入推送记录
            Map<Object, Map> notifyStatMap = issueMapBody.getNotify_stat_map();
            if (CheckTaskIssueStatus.AssignNoReform.getValue().equals(issue.getStatus())) {
                List<Integer> desUserIds = Lists.newArrayList();

                if (issue.getRepairerId() > 0 && (!issue.getRepairerId().equals(notifyStatMap.get(issue.getUuid()).get("repairerId")) || CheckTaskIssueStatus.ReformNoCheck.getValue().equals(notifyStatMap.get(issue.getUuid()).get("status")))) {
                    desUserIds.add(issue.getRepairerId());
                    pushList.add(issue.getRepairerId());
                }
                List<Integer> idsComma = StringSplitToListUtil.splitToIdsComma(issue.getRepairerFollowerIds(), ",");
                idsComma.forEach(user -> {
                    List splitToIdsComma = (List) notifyStatMap.get(issue.getUuid()).get("splitToIdsComma");
                    if (user > 0 && (splitToIdsComma.contains(user) || CheckTaskIssueStatus.ReformNoCheck.getValue().equals(notifyStatMap.get(issue.getUuid()).get("status")))) {
                        desUserIds.add(user);
                        pushList.add(user);
                    }
                });
                if (CollectionUtils.isNotEmpty(desUserIds)) {
                    HouseQmCheckTaskNotifyRecord itemNotify = new HouseQmCheckTaskNotifyRecord();
                    itemNotify.setProjectId(issue.getProjectId());
                    itemNotify.setTaskId(issue.getTaskId());
                    itemNotify.setSrcUserId(0);
                    itemNotify.setDesUserIds(StringUtils.join(desUserIds, ","));
                    itemNotify.setModuleId(ModuleInfoEnum.GCGL.getValue());
                    itemNotify.setIssueId(issue.getId());
                    itemNotify.setIssueStatus(CheckTaskIssueStatus.NoteNoAssign.getValue());
                    itemNotify.setExtraInfo("");
                    notifyList.add(itemNotify);
                }
            } else if (CheckTaskIssueStatus.ReformNoCheck.getValue().equals(issue.getStatus()) &&
                    CheckTaskIssueStatus.ReformNoCheck.getValue().equals(notifyStatMap.get(issue.getUuid()).get("status"))) {
                ArrayList<Integer> desUserIds = getIssueCheckerList(checkerMap, issue, true);
                desUserIds.forEach(userId -> {
                    pushList.add(userId);
                });
                if (CollectionUtils.isNotEmpty(desUserIds)) {
                    HouseQmCheckTaskNotifyRecord itemNotify = new HouseQmCheckTaskNotifyRecord();
                    itemNotify.setProjectId(issue.getProjectId());
                    itemNotify.setTaskId(issue.getTaskId());
                    itemNotify.setSrcUserId(0);
                    itemNotify.setDesUserIds(StringUtils.join(desUserIds, ","));
                    itemNotify.setModuleId(ModuleInfoEnum.GCGL.getValue());
                    itemNotify.setIssueId(issue.getId());
                    itemNotify.setIssueStatus(CheckTaskIssueStatus.ReformNoCheck.getValue());
                    itemNotify.setExtraInfo("");
                    notifyList.add(itemNotify);
                }
            }
        }

        //    # 处理新增问题人员角色
        for (Map.Entry<String, ApiUserRoleInIssue> entry : issueRoleMap.entrySet()) {
            Map<ApiUserRoleInIssue.RoleUser, Boolean> role = issueRoleMap.get(entry.getKey()).getUser_role();
            for (Map.Entry<ApiUserRoleInIssue.RoleUser, Boolean> entrys : role.entrySet()) {
                HouseQmCheckTaskIssueUser houseQmCheckTaskIssueUser = new HouseQmCheckTaskIssueUser();
                houseQmCheckTaskIssueUser.setTaskId(issueRoleMap.get(entry.getKey()).getTask_id());
                houseQmCheckTaskIssueUser.setIssueUuid(entry.getKey());
                houseQmCheckTaskIssueUser.setUserId(entrys.getKey().getUser_id());
                houseQmCheckTaskIssueUser.setRoleType(entrys.getKey().getRole_type());
                try {
                    houseQmCheckTaskIssueUserService.add(houseQmCheckTaskIssueUser);
                } catch (Exception e) {
                    log.info("insert new role failed, data=" + JSON.toJSONString(houseQmCheckTaskIssueUser) + "");
                    e.printStackTrace();
                }
            }
        }
        //  # 处理新增附件
        for (Map.Entry<String, HouseQmCheckTaskIssueAttachment> entry : attachmentInsertMap.entrySet()) {
            int one = houseQmCheckTaskIssueAttachmentService.add(entry.getValue());
            if (one <= 0) {
                log.info("insert new attachment failed, data=" + JSON.toJSONString(entry.getValue()) + "");
            }
        }
        // # 处理移除附件
        attachmentRemoveList.forEach(attachment -> {
            HouseQmCheckTaskIssueAttachment issueAttachment = houseQmCheckTaskIssueAttachmentService.selectByMd5AndNotDel(attachment);
            if (issueAttachment == null) {
                log.info("remove attachment failed, md5=" + attachment + "");
            }
        });
        //# 处理新增问题日志
        logInsertList.forEach(loginsert -> {
            try {
                houseQmCheckTaskIssueLogService.add(loginsert);
            } catch (Exception e) {
                log.info("insert new log failed, data=" + JSON.toJSONString(logInsertList) + "");
                e.printStackTrace();
            }
        });
        //     # 处理退单情况融合推送
        for (Map.Entry<HouseQmCheckTaskIssue, ApiRefundInfo> entry : refundMap.entrySet()) {
            List<Integer> desUserIds = getIssueCheckerList(checkerMap, entry.getKey(), null);
            if (CollectionUtils.isNotEmpty(desUserIds)) {
                HouseQmCheckTaskNotifyRecord itemNotify = new HouseQmCheckTaskNotifyRecord();
                itemNotify.setProjectId(entry.getKey().getProjectId());
                itemNotify.setTaskId(entry.getKey().getTaskId());
                itemNotify.setSrcUserId(0);
                itemNotify.setDesUserIds(StringUtils.join(desUserIds, ","));
                itemNotify.setModuleId(ModuleInfoEnum.GCGL.getValue());
                itemNotify.setIssueId(entry.getKey().getId());
                itemNotify.setIssueStatus(CheckTaskIssueStatus.NoteNoAssign.getValue());
                itemNotify.setExtraInfo("");
                notifyList.add(itemNotify);
            }
        }
        // # 处理融合消息中心推送通知
        if (CollectionUtils.isNotEmpty(notifyList)) {
            ArrayList<HouseQmCheckTaskNotifyRecord> dataSource = Lists.newArrayList();
            notifyList.forEach(notify -> {
                HouseQmCheckTaskNotifyRecord houseQmCheckTaskNotifyRecord = new HouseQmCheckTaskNotifyRecord();
                houseQmCheckTaskNotifyRecord.setProjectId(notify.getProjectId());
                houseQmCheckTaskNotifyRecord.setTaskId(notify.getTaskId());
                houseQmCheckTaskNotifyRecord.setSrcUserId(notify.getSrcUserId());
                houseQmCheckTaskNotifyRecord.setDesUserIds(notify.getDesUserIds());
                houseQmCheckTaskNotifyRecord.setModuleId(notify.getModuleId());
                houseQmCheckTaskNotifyRecord.setIssueId(notify.getIssueId());
                houseQmCheckTaskNotifyRecord.setIssueStatus(notify.getIssueStatus());
                houseQmCheckTaskNotifyRecord.setExtraInfo(notify.getExtraInfo());
                houseQmCheckTaskNotifyRecord.setCreateAt(new Date());
                houseQmCheckTaskNotifyRecord.setUpdateAt(new Date());
                dataSource.add(houseQmCheckTaskNotifyRecord);
            });
            houseQmCheckTaskNotifyRecordService.addMany(dataSource);
        }
        //  # 处理代办事项消息推送
        if (CollectionUtils.isNotEmpty(pushList)) {
            String title = "新的待处理问题";
            String msg = "您在［工程检查］有新的待处理问题，请进入App同步更新。";
            ////todo 消息推送
              /*   notify_srv = NotifyMessage()
                 notify_srv.push_base_message(0, push_list, title, msg)
                 task_id, des_user_ids, title, description
                 //安卓推送
                 umPushUtil.sendAndroidCustomizedcast();
                 //苹果推送
                 umPushUtil.sendIOSCustomizedcast();
                 //小米推送
                 xmPushUtil.sendMessageToUserAccounts();*/
            ArrayList<String> ids = Lists.newArrayList();
            for (Object push : pushList) {
                ids.add((Integer) push + "");
            }
            issueService.pushBaseMessage(0, ids, title, msg);

        }
        //   # 处理退单消息推送
        ArrayList<Integer> ids = Lists.newArrayList();
        if (refundMap.size() > 0) {
            for (Map.Entry<HouseQmCheckTaskIssue, ApiRefundInfo> entry : refundMap.entrySet()) {
                ids.add(refundMap.get(entry.getKey()).getRepairer());
            }
            Map userMap = createUsersMap(ids);
            String title = "新的待处理问题";
            for (Map.Entry<HouseQmCheckTaskIssue, ApiRefundInfo> entry : refundMap.entrySet()) {
                String msg = "[],退回了一条问题，请进入[工程检查]App跟进处理";
                ////todo 消息推送
                   /*  notify_srv = NotifyMessage();
                     notify_srv.push_base_message(0, push_list, title, msg);
                     //安卓推送
                     umPushUtil.sendAndroidCustomizedcast();
                     //苹果推送
                     umPushUtil.sendIOSCustomizedcast();
                     //小米推送
                     xmPushUtil.sendMessageToUserAccounts();*/
                ArrayList<String> checker = Lists.newArrayList();
                ApiRefundInfo info = refundMap.get(entry);
                if (info != null) checker.add(info.getChecker() + "");
                issueService.pushBaseMessage(0, checker, title, msg);
            }
        }
        // # 处理kafka数据统计消息
        ArrayList<ApiHouseQmIssue> kafkaCreated = Lists.newArrayList();
        ArrayList<ApiHouseQmIssue> kafkaAssigned = Lists.newArrayList();
        ArrayList<ApiHouseQmIssue> kafkaReformed = Lists.newArrayList();
        ArrayList<ApiHouseQmIssue> kafkaChecked = Lists.newArrayList();
        for (Map.Entry<String, HouseQmCheckTaskIssue> entry : issueInsertMap.entrySet()) {
            HouseQmCheckTaskIssue issue = entry.getValue();
            if (!issue.getCategoryCls().equals(CategoryClsTypeEnum.RCJC.getValue())) {
                continue;
            }
            ApiHouseQmIssue apiHouseQmIssue = new ApiHouseQmIssue();
            apiHouseQmIssue.setUuid(issue.getUuid());
            apiHouseQmIssue.setProj_id(issue.getProjectId());
            apiHouseQmIssue.setTask_id(issue.getTaskId());
            apiHouseQmIssue.setChecker_id(issue.getSenderId());
            apiHouseQmIssue.setRepairer_id(0);
            apiHouseQmIssue.setArea_id(issue.getAreaId());
            apiHouseQmIssue.setArea_path_and_id(issue.getAreaPathAndId());
            apiHouseQmIssue.setCategory_key(issue.getCategoryKey());
            apiHouseQmIssue.setCategory_path_and_key(issue.getCategoryPathAndKey());
            apiHouseQmIssue.setSender_id(issue.getSenderId());
            apiHouseQmIssue.setTimestamp(DateUtil.datetimeToTimeStamp(issue.getClientCreateAt()));
            kafkaCreated.add(apiHouseQmIssue);
            if (issue.getStatus().equals(CheckTaskIssueStatus.AssignNoReform.getValue())) {
                ApiHouseQmIssue apiHouseQm = new ApiHouseQmIssue();
                apiHouseQm.setUuid(issue.getUuid());
                apiHouseQm.setProj_id(issue.getProjectId());
                apiHouseQm.setTask_id(issue.getTaskId());
                apiHouseQm.setChecker_id(issue.getSenderId());
                apiHouseQm.setRepairer_id(issue.getRepairerId());
                apiHouseQm.setArea_id(issue.getAreaId());
                apiHouseQm.setArea_path_and_id(issue.getAreaPathAndId());
                apiHouseQm.setCategory_key(issue.getCategoryKey());
                apiHouseQm.setCategory_path_and_key(issue.getCategoryPathAndKey());
                apiHouseQm.setSender_id(issue.getSenderId());
                apiHouseQm.setTimestamp(DateUtil.datetimeToTimeStamp(issue.getClientCreateAt()));
                kafkaAssigned.add(apiHouseQm);
            }
        }
        for (ApiHouseQmCheckTaskIssueLogInfo log : issueLogs) {
            if (!issueUpdateMap.containsKey(log.getIssue_uuid())) {
                continue;
            }
            HouseQmCheckTaskIssue issue = issueUpdateMap.get(log.getIssue_uuid());
            if (!issue.getCategoryCls().equals(CategoryClsTypeEnum.RCJC.getValue())) {
                continue;
            }
            if (log.getStatus().equals(CheckTaskIssueStatus.AssignNoReform.getValue())) {
                ApiHouseQmIssue apiHouseQm = new ApiHouseQmIssue();
                apiHouseQm.setUuid(issue.getUuid());
                apiHouseQm.setProj_id(issue.getProjectId());
                apiHouseQm.setTask_id(issue.getTaskId());
                apiHouseQm.setChecker_id(issue.getSenderId());
                apiHouseQm.setRepairer_id(issue.getRepairerId());
                apiHouseQm.setArea_id(issue.getAreaId());
                apiHouseQm.setArea_path_and_id(issue.getAreaPathAndId());
                apiHouseQm.setCategory_key(issue.getCategoryKey());
                apiHouseQm.setCategory_path_and_key(issue.getCategoryPathAndKey());
                apiHouseQm.setSender_id(log.getSender_id());
                apiHouseQm.setTimestamp(log.getClient_create_at());
                kafkaAssigned.add(apiHouseQm);

            } else if (log.getStatus().equals(CheckTaskIssueStatus.ReformNoCheck.getValue())) {
                ApiHouseQmIssue apiHouseQm = new ApiHouseQmIssue();
                apiHouseQm.setUuid(issue.getUuid());
                apiHouseQm.setProj_id(issue.getProjectId());
                apiHouseQm.setTask_id(issue.getTaskId());
                apiHouseQm.setChecker_id(issue.getSenderId());
                apiHouseQm.setRepairer_id(issue.getRepairerId());
                apiHouseQm.setArea_id(issue.getAreaId());
                apiHouseQm.setArea_path_and_id(issue.getAreaPathAndId());
                apiHouseQm.setCategory_key(issue.getCategoryKey());
                apiHouseQm.setCategory_path_and_key(issue.getCategoryPathAndKey());
                apiHouseQm.setSender_id(log.getSender_id());
                apiHouseQm.setTimestamp(log.getClient_create_at());
                kafkaReformed.add(apiHouseQm);
            } else if (log.getStatus().equals(CheckTaskIssueStatus.CheckYes.getValue())) {
                ApiHouseQmIssue apiHouseQm = new ApiHouseQmIssue();
                apiHouseQm.setUuid(issue.getUuid());
                apiHouseQm.setProj_id(issue.getProjectId());
                apiHouseQm.setTask_id(issue.getTaskId());
                apiHouseQm.setChecker_id(issue.getSenderId());
                apiHouseQm.setRepairer_id(issue.getRepairerId());
                apiHouseQm.setArea_id(issue.getAreaId());
                apiHouseQm.setArea_path_and_id(issue.getAreaPathAndId());
                apiHouseQm.setCategory_key(issue.getCategoryKey());
                apiHouseQm.setCategory_path_and_key(issue.getCategoryPathAndKey());
                apiHouseQm.setSender_id(log.getSender_id());
                apiHouseQm.setTimestamp(log.getClient_create_at());
                kafkaChecked.add(apiHouseQm);
            }
        }
        ApiHouseQmIssueReport report = new ApiHouseQmIssueReport();
        report.setCreated_issues(kafkaCreated);
        report.setAssigned_issues(kafkaAssigned);
        report.setReformed_issues(kafkaReformed);
        report.setChecked_issues(kafkaChecked);
        //kafka消息推送
        kafkaProducer.produce(EventQueueEnum.PKG_HOUSEQM_ISSUE_REPORTED.getValue(), report);

        ReportIssueVo vo = new ReportIssueVo();
        vo.setDropped(dropped);
        return vo;
    }

    @Data
    @NoArgsConstructor
    public class ApiHouseQmIssueReport {
        private List<ApiHouseQmIssue> created_issues;
        private List<ApiHouseQmIssue> assigned_issues;
        private List<ApiHouseQmIssue> reformed_issues;
        private List<ApiHouseQmIssue> checked_issues;
    }

    @Data
    @NoArgsConstructor
    public class ApiHouseQmIssue {
        private String uuid;
        private Integer proj_id;
        private Integer task_id;
        private Integer checker_id;
        private Integer repairer_id;
        private Integer area_id;
        private String area_path_and_id;
        private String category_key;
        private String category_path_and_key;
        private Integer sender_id;
        private Integer timestamp;
    }

    private ArrayList<Integer> getIssueCheckerList(Map<Integer, Map<Integer, Map<Integer, Integer>>> checkerMap, HouseQmCheckTaskIssue issue, Boolean b) {
        ArrayList<Integer> desUserIds = Lists.newArrayList();
        if (!checkerMap.containsKey(issue.getTaskId())) {
            return Lists.newArrayList();
        }
        if (!checkerMap.get(issue.getTaskId()).containsKey(issue.getSenderId())) {
            return Lists.newArrayList();
        }
        ArrayList<Object> squadIds = Lists.newArrayList();
        Map<Integer, Integer> map = checkerMap.get(issue.getTaskId()).get(issue.getSenderId());
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (squadIds.contains(entry.getKey())) {
                squadIds.add(entry.getValue());
            }
        }
        if (CollectionUtils.isEmpty(squadIds)) {
            return desUserIds;
        }
        Map<Integer, Map<Integer, Integer>> roleUsers = checkerMap.get(issue.getTaskId());
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : roleUsers.entrySet()) {
            Map<Integer, Integer> value = entry.getValue();
            for (Map.Entry<Integer, Integer> entrys : value.entrySet()) {
                if (squadIds.contains(entrys.getKey())) {
                    if (b && !roleUsers.get(entry.getKey()).get(entrys.getKey()).equals(CheckTaskRoleCanApproveType.Yes.getValue())) {
                        continue;
                    }
                    if (entry.getKey() > 0 && !desUserIds.contains(entry.getKey())) {
                        desUserIds.add(entry.getKey());
                    }
                }
            }

        }
        return desUserIds;
    }

    private Map createUsersMap(List<Integer> ids) {
        Map<Integer, User> map = userService.selectByIds(ids);
        return map;
    }

    @Data
    @NoArgsConstructor
    public class ApiRefundInfo {
        private Integer repairer;
        private Integer checker;
    }

    private Map<String, Object> modifyIssue(Map<HouseQmCheckTaskIssue, ApiRefundInfo> refundMap, HashMap<String, ApiUserRoleInIssue> issueRoleMap, HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item, Boolean b) {
        //  # 判断是否修改检查项
        if (isCheckItemChange(issue, item)) {
            log.info("check_item info need to update");
            return reassignIssue(issueRoleMap, issue, item);
        }
        //  # 如果是退单情况
        if (b == null && convertLogStatus(item.getStatus()).equals(CheckTaskIssueStatus.NoteNoAssign.getValue()) && issue.getStatus().equals(CheckTaskIssueStatus.AssignNoReform)) {
            log.info("refund issue");
            if (issue.getRepairerId() > 0 && issue.getSenderId() > 0) {
                ApiRefundInfo info = new ApiRefundInfo();
                info.setRepairer(issue.getRepairerId());
                info.setChecker(issue.getSenderId());
                refundMap.put(issue, info);
            }
            return refundIssue(issueRoleMap, issue, item);
        }
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail = item.getDetail();
        detail.forEach(detailInfo -> {
            if (detailInfo.getTyp() != -1) {
                issue.setTyp(detailInfo.getTyp());
            }
            Integer oldStatus = issue.getStatus();
            if (item.getStatus() != -1) {
                //  # 操作状态是否为需要修改issue状态的操作
                if (!item.getStatus().equals(CheckTaskIssueLogStatus.Repairing.getValue()) || !item.getStatus().equals(CheckTaskIssueLogStatus.EditBaseInfo.getValue())) {
                    Integer newStatus = convertLogStatus(item.getStatus());
                    if (newStatus > 0) {
                        issue.setStatus(newStatus);
                    }
                }
                if (item.getStatus().equals(CheckTaskIssueLogStatus.ReformNoCheck.getValue())) {
                    issue.setEndOn(DateUtil.transForDate(item.getClient_create_at()));
                } else if (item.getStatus().equals(CheckTaskIssueLogStatus.CheckYes.getValue())) {
                    issue.setDestroyUser(item.getSender_id());
                    issue.setDestroyAt(DateUtil.transForDate(item.getClient_create_at()));
                    if (oldStatus.equals(CheckTaskIssueStatus.NoteNoAssign.getValue()) || oldStatus.equals(CheckTaskIssueStatus.AssignNoReform.getValue())) {
                        issue.setEndOn(DateUtil.transForDate(item.getClient_create_at()));
                    }
                } else if (item.getStatus().equals(CheckTaskIssueLogStatus.AssignNoReform.getValue())) {
                    issue.setLastAssigner(item.getSender_id());
                    issue.setLastAssignAt(DateUtil.transForDate(item.getClient_create_at()));
                } else if (item.getStatus().equals(CheckTaskIssueLogStatus.UpdateIssueInfo.getValue())) {
                    if (CollectionUtils.isNotEmpty(item.getDetail())) {
                        issue.setContent(issue.getContent() + item.getDesc());
                    }
                }
            }
            //  # 最后整改负责人
            if (detailInfo.getRepairer_id() != -1) {
                issue.setRepairerId(detailInfo.getRepairer_id());
                issue.setLastRepairer(detailInfo.getRepairer_id());
                issue.setLastRepairerAt(DateUtil.transForDate(item.getClient_create_at()));
            }
            //    # 计划结束时间
            if (detailInfo.getPlan_end_on() > 0) {
                issue.setPlanEndOn(DateUtil.transForDate(detailInfo.getPlan_end_on()));
            }
            // # 严重程度
            if (detailInfo.getCondition() != -1) {
                issue.setCondition(detailInfo.getCondition());
            }
            // # 问题类型
            CheckTaskIssueType[] values = CheckTaskIssueType.values();
            List<CheckTaskIssueType> checkTaskIssueTypes = Arrays.asList(values);
            if (detailInfo.getTyp() != 1 && checkTaskIssueTypes.contains(detailInfo.getTyp())) {
                issue.setTyp(detailInfo.getTyp());
            }
            ApiUserRoleInIssue roleItem = null;
            if (issueRoleMap.containsKey(issue.getUuid())) {
                roleItem = issueRoleMap.get(issue.getUuid());
            } else {
                roleItem = new ApiUserRoleInIssue();
            }
            roleItem.setTask_id(item.getTask_id());
            if (detailInfo.getRepairer_id() > 0 && !detailInfo.getRepairer_follower_ids().equals("-1")) {
                //   # 检查人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> map = Maps.newHashMap();
                map.put(roleUser(issue.getSenderId(), UserInIssueRoleType.Checker.getValue()), true);
                roleItem.setUser_role(map);
                //     # 整改负责人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> hashMap = Maps.newHashMap();
                map.put(roleUser(detailInfo.getRepairer_id(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(hashMap);
                // # 整改参与人
                List<Integer> followerIds = StringSplitToListUtil.splitToIdsComma(detailInfo.getRepairer_follower_ids(), ",");
                for (Integer followerId : followerIds) {
                    HashMap<ApiUserRoleInIssue.RoleUser, Boolean> newHashMap = Maps.newHashMap();
                    newHashMap.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(newHashMap);
                }
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detailInfo.getRepairer_id());
                if (detailInfo.getRepairer_follower_ids().length() > 0) {
                    issue.setRepairerFollowerIds(detailInfo.getRepairer_follower_ids());
                } else {
                    issue.setRepairerFollowerIds("");
                }
            }
            Map<String, Object> map = JSON.parseObject(issue.getDetail(), Map.class);
            // # 编辑问题的detail字段
            if (!detailInfo.getCheck_item_md5().equals("") || !detailInfo.getCheck_item_md5().equals("-1")) {
                map.put("CheckItemMD5", detailInfo.getCheck_item_md5());
            }
            if (detailInfo.getIssue_reason() != -1 || detailInfo.getIssue_reason() != 0) {
                map.put("IssueReason", detailInfo.getIssue_reason());
            }
            if (!detailInfo.getIssue_reason_detail().equals("") || !detailInfo.getIssue_reason_detail().equals("-1")) {
                map.put("IssueReasonDetail", detailInfo.getIssue_reason_detail());
            }
            if (!detailInfo.getIssue_suggest().equals("") || !detailInfo.getIssue_suggest().equals("-1")) {
                map.put("IssueSuggest", detailInfo.getIssue_suggest());
            }
            if (!detailInfo.getPotential_risk().equals("") || !detailInfo.getPotential_risk().equals("-1")) {
                map.put("PotentialRisk", detailInfo.getPotential_risk());
            }
            if (!detailInfo.getPreventive_action_detail().equals("") || !detailInfo.getPreventive_action_detail().equals("-1")) {
                map.put("PreventiveActionDetail", detailInfo.getPreventive_action_detail());
            }
            issue.setDetail(JSON.toJSONString(map));
        });
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put("issue",issue);
        resmap.put("refundMap",refundMap);
        return resmap;
    }

    private Map<String, Object> refundIssue(HashMap<String, ApiUserRoleInIssue> issueRoleMap, HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item) {
        issue.setRepairerId(0);
        issue.setRepairerFollowerIds("");
        issue.setLastRepairer(0);
        issue.setLastRepairerAt(DateUtil.strToDate("0001-01-01 00:00:00", "yyyy-MM-dd-HH-mm-ss"));
        issue.setPlanEndOn(new Date(0));
        Integer newStatus = convertLogStatus(item.getStatus());
        if (newStatus > 0) {
            issue.setStatus(newStatus);
        }
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail = item.getDetail();
        detail.forEach(detailInfo -> {
            //  # 最后整改负责人
            if (detailInfo.getRepairer_id() != -1) {
                issue.setRepairerId(detailInfo.getRepairer_id());
                issue.setLastRepairer(detailInfo.getRepairer_id());
                issue.setLastRepairerAt(DateUtil.transForDate(item.getClient_create_at()));
            }
            //    # 计划结束时间
            if (detailInfo.getPlan_end_on() > 0) {
                issue.setPlanEndOn(DateUtil.transForDate(detailInfo.getPlan_end_on()));
            }
            // # 严重程度
            if (detailInfo.getCondition() != -1) {
                issue.setCondition(detailInfo.getCondition());
            }
            // # 问题类型
            CheckTaskIssueType[] values = CheckTaskIssueType.values();
            List<CheckTaskIssueType> checkTaskIssueTypes = Arrays.asList(values);
            if (detailInfo.getTyp() != 1 && checkTaskIssueTypes.contains(detailInfo.getTyp())) {
                issue.setTyp(detailInfo.getTyp());
            }
            ApiUserRoleInIssue roleItem = null;
            if (issueRoleMap.containsKey(issue.getUuid())) {
                roleItem = issueRoleMap.get(issue.getUuid());
            } else {
                roleItem = new ApiUserRoleInIssue();
            }
            roleItem.setTask_id(item.getTask_id());
            if (detailInfo.getRepairer_id() > 0 && !detailInfo.getRepairer_follower_ids().equals("-1")) {
                //   # 检查人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> map = Maps.newHashMap();
                map.put(roleUser(issue.getSenderId(), UserInIssueRoleType.Checker.getValue()), true);
                roleItem.setUser_role(map);
                //     # 整改负责人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> hashMap = Maps.newHashMap();
                map.put(roleUser(detailInfo.getRepairer_id(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(hashMap);
                // # 整改参与人
                List<Integer> followerIds = StringSplitToListUtil.splitToIdsComma(detailInfo.getRepairer_follower_ids(), ",");
                for (Integer followerId : followerIds) {
                    HashMap<ApiUserRoleInIssue.RoleUser, Boolean> newHashMap = Maps.newHashMap();
                    newHashMap.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(newHashMap);
                }
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detailInfo.getRepairer_id());
                if (detailInfo.getRepairer_follower_ids().length() > 0) {
                    issue.setRepairerFollowerIds(detailInfo.getRepairer_follower_ids());
                } else {
                    issue.setRepairerFollowerIds("");
                }
            }
            Map map = JSON.parseObject(issue.getDetail(), Map.class);
            // # 编辑问题的detail字段
            if (!detailInfo.getCheck_item_md5().equals("") || !detailInfo.getCheck_item_md5().equals("-1")) {
                map.put("CheckItemMD5", detailInfo.getCheck_item_md5());
            }
            if (detailInfo.getIssue_reason() != -1 || detailInfo.getIssue_reason() != 0) {
                map.put("IssueReason", detailInfo.getIssue_reason());
            }
            if (!detailInfo.getIssue_reason_detail().equals("") || !detailInfo.getIssue_reason_detail().equals("-1")) {
                map.put("IssueReasonDetail", detailInfo.getIssue_reason_detail());
            }
            if (!detailInfo.getIssue_suggest().equals("") || !detailInfo.getIssue_suggest().equals("-1")) {
                map.put("IssueSuggest", detailInfo.getIssue_suggest());
            }
            if (!detailInfo.getPotential_risk().equals("") || !detailInfo.getPotential_risk().equals("-1")) {
                map.put("PotentialRisk", detailInfo.getPotential_risk());
            }
            if (!detailInfo.getPreventive_action_detail().equals("") || !detailInfo.getPreventive_action_detail().equals("-1")) {
                map.put("PreventiveActionDetail", detailInfo.getPreventive_action_detail());
            }
            issue.setDetail(JSON.toJSONString(map));
        });
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put("issue",issue);
        resmap.put("refundMap", Maps.newHashMap());
        return resmap;
    }

    private Map<String, Object> reassignIssue(HashMap<String, ApiUserRoleInIssue> issueRoleMap, HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item) {
        issue.setRepairerId(0);
        issue.setRepairerFollowerIds("");
        issue.setLastRepairer(0);
        issue.setLastRepairerAt(DateUtil.strToDate("0001-01-01 00:00:00", "yyyy-MM-dd-HH-mm-ss"));
        issue.setPlanEndOn(new Date(0));
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail = item.getDetail();
        detail.forEach(detailInfo -> {
            if (!detailInfo.getCategory_key().equals("-1")) {
                issue.setCategoryKey(detailInfo.getCategory_key());
                issue.setCategoryPathAndKey(getCategoryPathAndKey(issue.getCategoryKey()));
            }
            if (!detailInfo.getCheck_item_key().equals("-1")) {
                issue.setCheckItemKey(detailInfo.getCheck_item_key());
                issue.setCheckItemPathAndKey(getCheckItemPathAndKey(issue.getCheckItemKey()));
            }
            Integer newStatus = convertLogStatus(item.getStatus());
            if (newStatus > 0) {
                issue.setStatus(newStatus);
            }
            //  # 最后整改负责人
            if (detailInfo.getRepairer_id() != -1) {
                issue.setRepairerId(detailInfo.getRepairer_id());
                issue.setLastRepairer(detailInfo.getRepairer_id());
                issue.setLastRepairerAt(DateUtil.transForDate(item.getClient_create_at()));
            }
            //    # 计划结束时间
            if (detailInfo.getPlan_end_on() > 0) {
                issue.setPlanEndOn(DateUtil.transForDate(detailInfo.getPlan_end_on()));
            }
            // # 严重程度
            if (detailInfo.getCondition() != -1) {
                issue.setCondition(detailInfo.getCondition());
            }
            // # 问题类型
            CheckTaskIssueType[] values = CheckTaskIssueType.values();
            List<CheckTaskIssueType> checkTaskIssueTypes = Arrays.asList(values);
            if (detailInfo.getTyp() != 1 && checkTaskIssueTypes.contains(detailInfo.getTyp())) {
                issue.setTyp(detailInfo.getTyp());
            }
            ApiUserRoleInIssue roleItem = null;
            if (issueRoleMap.containsKey(issue.getUuid())) {
                roleItem = issueRoleMap.get(issue.getUuid());
            } else {
                roleItem = new ApiUserRoleInIssue();
            }
            roleItem.setTask_id(item.getTask_id());
            if (detailInfo.getRepairer_id() > 0 && !detailInfo.getRepairer_follower_ids().equals("-1")) {
                //   # 检查人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> map = Maps.newHashMap();
                map.put(roleUser(issue.getSenderId(), UserInIssueRoleType.Checker.getValue()), true);
                roleItem.setUser_role(map);
                //     # 整改负责人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> hashMap = Maps.newHashMap();
                map.put(roleUser(detailInfo.getRepairer_id(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(hashMap);
                // # 整改参与人
                List<Integer> followerIds = StringSplitToListUtil.splitToIdsComma(detailInfo.getRepairer_follower_ids(), ",");
                for (Integer followerId : followerIds) {
                    HashMap<ApiUserRoleInIssue.RoleUser, Boolean> newHashMap = Maps.newHashMap();
                    newHashMap.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(newHashMap);
                }
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detailInfo.getRepairer_id());
                if (detailInfo.getRepairer_follower_ids().length() > 0) {
                    issue.setRepairerFollowerIds(detailInfo.getRepairer_follower_ids());
                } else {
                    issue.setRepairerFollowerIds("");
                }
            }
            Map map = JSON.parseObject(issue.getDetail(), Map.class);
            // # 编辑问题的detail字段
            if (!detailInfo.getCheck_item_md5().equals("") || !detailInfo.getCheck_item_md5().equals("-1")) {
                map.put("CheckItemMD5", detailInfo.getCheck_item_md5());
            }
            if (detailInfo.getIssue_reason() != -1 || detailInfo.getIssue_reason() != 0) {
                map.put("IssueReason", detailInfo.getIssue_reason());
            }
            if (!detailInfo.getIssue_reason_detail().equals("") || !detailInfo.getIssue_reason_detail().equals("-1")) {
                map.put("IssueReasonDetail", detailInfo.getIssue_reason_detail());
            }
            if (!detailInfo.getIssue_suggest().equals("") || !detailInfo.getIssue_suggest().equals("-1")) {
                map.put("IssueSuggest", detailInfo.getIssue_suggest());
            }
            if (!detailInfo.getPotential_risk().equals("") || !detailInfo.getPotential_risk().equals("-1")) {
                map.put("PotentialRisk", detailInfo.getPotential_risk());
            }
            if (!detailInfo.getPreventive_action_detail().equals("") || !detailInfo.getPreventive_action_detail().equals("-1")) {
                map.put("PreventiveActionDetail", detailInfo.getPreventive_action_detail());
            }
            issue.setDetail(JSON.toJSONString(map));
        });
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put("issue",issue);
        resmap.put("refundMap", Maps.newHashMap());
        return resmap;
    }

    private boolean isCheckItemChange(HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item) {
        ArrayList<Integer> objects = Lists.newArrayList();
        objects.add(CheckTaskIssueStatus.NoteNoAssign.getValue());
        objects.add(CheckTaskIssueStatus.AssignNoReform.getValue());
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail = item.getDetail();
        if (objects.contains(issue.getStatus())) {
            for (int i = 0; i < detail.size(); i++) {
                if (!detail.get(i).getCategory_key().equals("") || detail.get(i).getCategory_key().equals("-1") || detail.get(i).getCheck_item_key().equals("") || detail.get(i).getCheck_item_key().equals("-1")) {
                    if (!issue.getCategoryKey().equals(detail.get(i).getCategory_key()) || !issue.getCheckItemKey().equals(detail.get(i).getCheck_item_key())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Map<Integer, Map<Integer, Map<Integer, Integer>>> createCheckerMap(ArrayList<Integer> taskIds) {
        if (CollectionUtils.isEmpty(taskIds)) {
            return Maps.newHashMap();
        }
        List<UserInHouseQmCheckTask> lst = userInHouseQmCheckTaskService.selectByTaskIdInAndRoleTypeNotDel(taskIds, CheckTaskRoleType.Checker.getValue());
        HashMap<Integer, Map<Integer, Map<Integer, Integer>>> resultDict = Maps.newHashMap();
        lst.forEach(item -> {
            if (!resultDict.containsKey(item.getTaskId())) {
                HashMap<Integer, Integer> map = Maps.newHashMap();
                map.put(item.getSquadId(), item.getCanApprove());
                HashMap<Integer, Map<Integer, Integer>> hashMap = Maps.newHashMap();
                hashMap.put(item.getUserId(), map);
                resultDict.put(item.getTaskId(), hashMap);
            } else {
                Map<Integer, Map<Integer, Integer>> integerMapMap = resultDict.get(item.getTaskId());
                if (!integerMapMap.containsKey(item.getUserId())) {

/*
                    resultDict.get(item.getTaskId()).get(item.getUserId()).put(item.getSquadId(), item.getCanApprove());
*/
                    HashMap<Integer, Integer> map = Maps.newHashMap();
                    map.put(item.getSquadId(), item.getCanApprove());
                    resultDict.get(item.getTaskId()).put(item.getUserId(), map);
                } else {
                    if (!resultDict.get(item.getTaskId()).get(item.getUserId()).containsKey(item.getSquadId())) {
                        resultDict.get(item.getTaskId()).get(item.getUserId()).put(item.getSquadId(), item.getCanApprove());
                    }
                }
            }
        });
        return resultDict;
    }

    private HouseQmCheckTaskIssue createIssue(HashMap<String, ApiUserRoleInIssue> issueRoleMap, Integer uid, Integer prijectId, ApiHouseQmCheckTaskIssueLogInfo log) {
        //  # 创建一条新的issue
        HouseQmCheckTaskIssue issue = new HouseQmCheckTaskIssue();
        issue.setProjectId(prijectId);
        issue.setTaskId(log.getTask_id());
        issue.setUuid(log.getIssue_uuid());
        issue.setSenderId(uid);
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail1 = log.getDetail();
        detail1.forEach(detail -> {
            if (detail.getPlan_end_on() != -1) {
                issue.setPlanEndOn(DateUtil.transForDate(detail.getPlan_end_on()));
            }
            if (detail.getEnd_on() != -1) {
                issue.setEndOn(DateUtil.transForDate(detail.getEnd_on()));
            }
            issue.setCategoryCls(detail.getCategory_cls());
            issue.setCategoryKey(detail.getCategory_key());
            issue.setCategoryPathAndKey(getCategoryPathAndKey(issue.getCategoryKey()));
            issue.setCheckItemKey(detail.getCheck_item_key());
            issue.setCheckItemPathAndKey(getCheckItemPathAndKey(issue.getCheckItemKey()));
            issue.setAreaId(detail.getArea_id());
            issue.setAreaPathAndId(getAreaPathAndId(issue.getAreaId()));
            issue.setDrawingMD5(detail.getDrawing_md5());
            if (detail.getPos_x() > 0) {
                issue.setPosX(detail.getPos_x());
            } else {
                issue.setPosX(-1);
            }
            if (detail.getPos_y() > 0) {
                issue.setPosY(detail.getPos_y());
            } else {
                issue.setPosY(-1);
            }
            if (StringUtils.isNotBlank(detail.getTitle()) && !detail.getTitle().equals("-1")) {
                issue.setTitle(detail.getTitle());
            } else {
                issue.setTitle("");
            }
            issue.setTyp(detail.getTyp());
            issue.setContent(log.getDesc());
            issue.setCondition(detail.getCondition());
            issue.setStatus(convertLogStatus(log.getStatus()));
            issue.setAttachmentMd5List(log.getAttachment_md5_list());
            issue.setAudioMd5List(log.getAudio_md5_list());
            issue.setClientCreateAt(DateUtil.transForDate(log.getClient_create_at()));
            if (log.getStatus().equals(CheckTaskIssueLogStatus.AssignNoReform.getValue())) {
                issue.setLastAssigner(log.getSender_id());
                issue.setLastAssignAt(DateUtil.transForDate(log.getClient_create_at()));
            }
            if (!detail.getRepairer_id().equals("-1")) {
                issue.setRepairerId(detail.getRepairer_id());
                issue.setLastRepairer(detail.getRepairer_id());
                issue.setLastRepairerAt(DateUtil.transForDate(log.getClient_create_at()));
            }
            if (!detail.getRepairer_follower_ids().equals("-1")) {
                issue.setRepairerFollowerIds(detail.getRepairer_follower_ids());
            }
            issue.setRepairerFollowerIds("");
            issue.setDestroyUser(0);
            issue.setDeleteUser(0);
            HashMap<Object, Object> details = Maps.newHashMap();
            details.put("CheckItemMD5", "");
            if (detail.getIssue_reason() != null && detail.getIssue_reason() != -1) {
                details.put("IssueReason", detail.getIssue_reason());
            } else {
                details.put("IssueReason", "");
            }
            if (detail.getIssue_reason_detail() != null && !detail.getIssue_reason_detail().equals("-1")) {
                details.put("IssueReasonDetail", detail.getIssue_reason_detail());
            } else {
                details.put("IssueReasonDetail", "");
            }
            if (detail.getIssue_suggest() != null && !detail.getIssue_suggest().equals("-1")) {
                details.put("IssueSuggest", detail.getIssue_suggest());
            } else {
                details.put("IssueSuggest", "");
            }
            if (StringUtils.isNotEmpty(detail.getPotential_risk()) && !detail.getPotential_risk().equals("-1")) {
                details.put("PotentialRisk", detail.getPotential_risk());
            } else {
                details.put("PotentialRisk", "");
            }
            if (StringUtils.isNotEmpty(detail.getPreventive_action_detail()) && !detail.getPreventive_action_detail().equals("-1")) {
                details.put("PreventiveActionDetail", detail.getPreventive_action_detail());
            } else {
                details.put("PreventiveActionDetail", "");
            }
            issue.setDetail(JSON.toJSONString(detail));
            ApiUserRoleInIssue roleItem = new ApiUserRoleInIssue();
            roleItem.setTask_id(log.getTask_id());
            if (detail.getRepairer_id() > 0 && !detail.getRepairer_follower_ids().equals("-1")) {
                //   # 检查人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> map = Maps.newHashMap();
                map.put(roleUser(issue.getSenderId(), UserInIssueRoleType.Checker.getValue()), true);
                roleItem.setUser_role(map);
                //     # 整改负责人
                HashMap<ApiUserRoleInIssue.RoleUser, Boolean> hashMap = Maps.newHashMap();
                map.put(roleUser(issue.getRepairerId(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(hashMap);
                // # 整改参与人
                List<Integer> followerIds = StringSplitToListUtil.splitToIdsComma(detail.getRepairer_follower_ids(), ",");
                followerIds.forEach(followerId -> {
                    HashMap<ApiUserRoleInIssue.RoleUser, Boolean> newHashMap = Maps.newHashMap();
                    newHashMap.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(newHashMap);
                });
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detail.getRepairer_id());
                if (detail.getRepairer_follower_ids().length() > 0) {
                    issue.setRepairerFollowerIds(detail.getRepairer_follower_ids());
                } else {
                    issue.setRepairerFollowerIds("");
                }
            }
        });


        return issue;
    }

    private Integer convertLogStatus(Integer status) {
        HashMap<Integer, Integer> switcher = Maps.newHashMap();
        switcher.put(CheckTaskIssueLogStatus.NoProblem.getValue(), CheckTaskIssueStatus.NoProblem.getValue());
        switcher.put(CheckTaskIssueLogStatus.ReformNoCheck.getValue(), CheckTaskIssueStatus.ReformNoCheck.getValue());
        switcher.put(CheckTaskIssueLogStatus.AssignNoReform.getValue(), CheckTaskIssueStatus.AssignNoReform.getValue());
        switcher.put(CheckTaskIssueLogStatus.Repairing.getValue(), CheckTaskIssueStatus.AssignNoReform.getValue());
        switcher.put(CheckTaskIssueLogStatus.NoteNoAssign.getValue(), CheckTaskIssueStatus.NoteNoAssign.getValue());
        switcher.put(CheckTaskIssueLogStatus.CheckYes.getValue(), CheckTaskIssueStatus.CheckYes.getValue());
        switcher.put(CheckTaskIssueLogStatus.Cancel.getValue(), CheckTaskIssueStatus.Cancel.getValue());
        if (switcher.get(status) != null) {
            return switcher.get(status);
        } else {
            return 0;
        }

    }

    private String getAreaPathAndId(Integer areaId) {
        String areaPathAndId = "";
        Area area = areaService.selectById(areaId);
        if (area != null) {
            return area.getPath() + areaId;
        }
        return areaPathAndId;
    }

    private String getCheckItemPathAndKey(String checkItemKey) {
        String checkItemPathAndKey = "";
        CheckItemV3 checkItem = checkItemV3Service.selectByKeyNotDel(checkItemKey);
        if (checkItem != null) {
            return checkItem.getPath() + checkItemKey;
        }
        return checkItemPathAndKey;

    }

    private String getCategoryPathAndKey(String categoryKey) {
        String categoryPathAndKey = "";
        CategoryV3 category = categoryV3Service.selectByKeyNotDel(categoryKey);
        if (category != null) {
            return category.getPath() + categoryKey;
        }
        return categoryPathAndKey;
    }

    private ApiUserRoleInIssue.RoleUser roleUser(Integer senderId, Integer value) {
        ApiUserRoleInIssue.RoleUser roleUser = new ApiUserRoleInIssue().new RoleUser();
        roleUser.setUser_id(senderId);
        roleUser.setRole_type(value);
        return roleUser;
    }

    private HouseQmCheckTaskIssueLog createIssueLog(Integer projectId, ApiHouseQmCheckTaskIssueLogInfo log) {
        // # 创建一条新的issue_log
        HouseQmCheckTaskIssueLog issueLog = new HouseQmCheckTaskIssueLog();
        issueLog.setProjectId(projectId);
        issueLog.setTaskId(log.getTask_id());
        issueLog.setUuid(log.getUuid());
        issueLog.setIssueUuid(log.getIssue_uuid());
        issueLog.setSenderId(log.getSender_id());
        issueLog.setDesc(log.getDesc());
        issueLog.setStatus(log.getStatus());
        issueLog.setAttachmentMd5List(log.getAttachment_md5_list());
        issueLog.setAudioMd5List(log.getAudio_md5_list());
        issueLog.setMemoAudioMd5List(log.getMemo_audio_md5_list());
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail = log.getDetail();
        HashMap<String, Object> detailMap = null;
        for (ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo item : detail) {
            detailMap = Maps.newHashMap();
            detailMap.put("PlanEndOn", item.getPlan_end_on());
            detailMap.put("EndOn", item.getEnd_on());
            detailMap.put("RepairerId", item.getRepairer_id());
            detailMap.put("RepairerFollowerIds", item.getRepairer_follower_ids());
            detailMap.put("Condition", item.getCondition());
            detailMap.put("AreaId", item.getArea_id());
            detailMap.put("PosX", item.getPos_x());
            detailMap.put("PosY", item.getPos_y());
            detailMap.put("Typ", item.getTyp());
            detailMap.put("Title", item.getTitle());
            detailMap.put("CheckItemKey", item.getCheck_item_key());
            detailMap.put("CategoryCls", item.getCategory_cls());
            detailMap.put("CategoryKey", item.getCategory_key());
            detailMap.put("DrawingMD5", item.getDrawing_md5());
            detailMap.put("RemoveMemoAudioMd5List", item.getRemove_memo_audio_md5_list());
            detailMap.put("CheckItemMD5", item.getCheck_item_md5());
            detailMap.put("IssueReason", item.getIssue_reason());
            detailMap.put("IssueReasonDetail", item.getIssue_reason_detail());
            detailMap.put("IssueSuggest", item.getIssue_suggest());
            detailMap.put("PotentialRisk", item.getPotential_risk());
            detailMap.put("PreventiveActionDetail", item.getPreventive_action_detail());
        }
        issueLog.setDetail(JSON.toJSONString(detailMap));
        issueLog.setClientCreateAt(DateUtil.transForDate(log.getClient_create_at()));
        return issueLog;
    }

    private IssueMapBody createIcssueMap(List<String> issueUuids) {

        Map<String, HouseQmCheckTaskIssue> resultDict = Maps.newHashMap();
        Map<String, Map<String, Object>> notifyStatDict = Maps.newHashMap();
        List<String> deleteIssueUuids = Lists.newArrayList();

        if (CollectionUtils.isEmpty(issueUuids)) {
            IssueMapBody body = new IssueMapBody();
            body.setDelete_issue_uuids(deleteIssueUuids);
            body.setExist_issue_map(resultDict);
            body.setNotify_stat_map(notifyStatDict);
            return body;
        }
        List<HouseQmCheckTaskIssue> lst = houseQmCheckTaskIssueService.selectByUuids(issueUuids);
        lst.forEach(item -> {
            if (datetimeZero(item.getDeleteAt())) {
                resultDict.put(item.getUuid(), item);
                notifyStatDict.put(item.getUuid(), ApiNotifyStat(item.getStatus(), item.getRepairerId(), StringSplitToListUtil.splitToIdsComma(item.getRepairerFollowerIds(), ",")));
            } else {
                deleteIssueUuids.add(item.getUuid());
            }
        });
        IssueMapBody issueMapBody = new IssueMapBody();
        issueMapBody.setNotify_stat_map(notifyStatDict);
        issueMapBody.setDelete_issue_uuids(deleteIssueUuids);
        issueMapBody.setExist_issue_map(resultDict);
        return issueMapBody;
    }

    private Map<String, Object> ApiNotifyStat(Integer status, Integer repairerId, List<Integer> repairer_follower_ids) {
        status = 0;
        repairerId = 0;
        repairer_follower_ids = Lists.newArrayList();
        Map<String, Object> map = Maps.newHashMap();
        map.put("status", status);
        map.put("repairerId", repairerId);
        map.put("splitToIdsComma", repairer_follower_ids);
        return map;
    }

    private boolean datetimeZero(Date deleteAt) {
        if (deleteAt == null || new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deleteAt).equals("0001-01-01 00:00:00") || new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(deleteAt).equals("") || DateUtil.datetimeToTimeStamp(deleteAt) <= DateUtil.datetimeToTimeStamp(new Date(0))) {
            return true;
        } else {
            return false;
        }

    }

    private List checkLogUuid(List<String> log_uuids) {
        if (CollectionUtils.isEmpty(log_uuids)) {
            return Lists.newArrayList();
        }
        List<HouseQmCheckTaskIssueLog> lst = houseQmCheckTaskIssueLogService.selectByUuidsAndNotDelete(log_uuids);
        ArrayList<String> result = Lists.newArrayList();
        lst.forEach(item -> {
            if (!result.contains(item.getUuid())) {
                result.add(item.getUuid());
            }
        });
        return result;
    }

    private UnmarshReportIssueRequestBody unmarshReportIssueRequest(String data) {
        List<String> logUuids = Lists.newArrayList();
        List<String> issueUuids = Lists.newArrayList();
        List<ApiHouseQmCheckTaskIssueLogInfo> issueLogs = Lists.newArrayList();
        JSONArray objects = JSON.parseArray(data);
        List<Map> reportlist = objects.toJavaList(Map.class);
        UnmarshReportIssueRequestBody body = new UnmarshReportIssueRequestBody();
        reportlist.forEach(item -> {
            ApiHouseQmCheckTaskIssueLogInfo items = new ApiHouseQmCheckTaskIssueLogInfo();
            items.setUuid((String) item.get("uuid"));
            if (StringUtils.isBlank(items.getUuid())) {
                log.info("uuid not exist, data=" + data + "");
                throw new LjBaseRuntimeException(646, "uuid not exist");
            }
            items.setTask_id((Integer) item.get("task_id"));
            if (items.getUuid() == null) {
                log.info("task_id not exist, data=" + data + "");
                throw new LjBaseRuntimeException(651, "task_id not exist");
            }
            items.setIssue_uuid((String) item.get("issue_uuid"));
            if (StringUtils.isBlank(items.getIssue_uuid())) {
                log.info("issue_uuid not exist, data=" + data + "");
                throw new LjBaseRuntimeException(656, "issue_uuid not exist");
            }
            if ((Integer) item.get("sender_id") != null) {
                items.setSender_id((Integer) item.get("sender_id"));
            } else {
                items.setSender_id(-1);
            }
            if (StringUtils.isNotBlank((String) item.get("desc"))) {
                items.setDesc((String) item.get("desc"));
            } else {
                items.setDesc("");
            }
            if ((Integer) item.get("status") != null) {
                items.setStatus((Integer) item.get("status"));
            } else {
                items.setStatus(-1);
            }
            if (StringUtils.isNotBlank((String) item.get("attachment_md5_list"))) {
                items.setAttachment_md5_list((String) item.get("attachment_md5_list"));
            } else {
                items.setAttachment_md5_list("");
            }
            if (StringUtils.isNotBlank((String) item.get("audio_md5_list"))) {
                items.setAudio_md5_list((String) item.get("audio_md5_list"));
            } else {
                items.setAudio_md5_list("");
            }
            if (StringUtils.isNotBlank((String) item.get("memo_audio_md5_list"))) {
                items.setMemo_audio_md5_list((String) item.get("memo_audio_md5_list"));
            } else {
                items.setMemo_audio_md5_list("");
            }
            if ((Integer) item.get("client_create_at") != null) {
                items.setClient_create_at((Integer) item.get("client_create_at"));
            } else {
                items.setClient_create_at(-1);
            }
            ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo info = new ApiHouseQmCheckTaskIssueLogInfo().new ApiHouseQmCheckTaskIssueLogDetailInfo();
            Map detail = (Map) item.get("detail");
            if (detail != null) {
                if ((Integer) detail.get("pos_y") != null) {
                    info.setPos_y((Integer) detail.get("pos_y"));
                } else {
                    info.setPos_y((Integer) detail.get(-1));
                }

                if ((Integer) detail.get("typ") != null) {
                    info.setTyp((Integer) detail.get("typ"));
                } else {
                    info.setTyp(-1);
                }

                if ((Integer) detail.get("plan_end_on") != null) {
                    info.setPlan_end_on((Integer) detail.get("plan_end_on"));
                } else {
                    info.setPlan_end_on(-1);
                }

                if ((Integer) detail.get("end_on") != null) {
                    info.setEnd_on((Integer) detail.get("end_on"));
                } else {
                    info.setEnd_on(-1);
                }

                if ((Integer) detail.get("repairer_id") != null) {
                    info.setRepairer_id((Integer) detail.get("repairer_id"));
                } else {
                    info.setRepairer_id(-1);
                }

                if (StringUtils.isNotBlank((String) detail.get("repairer_follower_ids"))) {
                    info.setRepairer_follower_ids((String) detail.get("repairer_follower_ids"));
                } else {
                    info.setRepairer_follower_ids("");
                }

                if ((Integer) detail.get("condition") != null) {
                    info.setCondition((Integer) detail.get("condition"));
                } else {
                    info.setCondition(-1);
                }

                if ((Integer) detail.get("category_cls") != null) {
                    info.setCategory_cls((Integer) detail.get("category_cls"));
                } else {
                    info.setCategory_cls(-1);
                }

                if (StringUtils.isNotBlank((String) detail.get("category_key"))) {
                    info.setCategory_key((String) detail.get("category_key"));
                } else {
                    info.setCategory_key("");
                }

                if (StringUtils.isNotBlank((String) detail.get("drawing_md5"))) {
                    info.setDrawing_md5((String) detail.get("drawing_md5"));
                } else {
                    info.setDrawing_md5("");
                }

                if (StringUtils.isNotBlank((String) detail.get("check_item_key"))) {
                    info.setCheck_item_key((String) detail.get("check_item_key"));
                } else {
                    info.setCheck_item_key((String) detail.get("check_item_key"));
                }

                if (StringUtils.isNotBlank((String) detail.get("remove_memo_audio_md5_list"))) {
                    info.setRemove_memo_audio_md5_list((String) detail.get("remove_memo_audio_md5_list"));
                } else {
                    info.setRemove_memo_audio_md5_list((String) detail.get("remove_memo_audio_md5_list"));
                }

                if (StringUtils.isNotBlank((String) detail.get("title"))) {
                    info.setTitle((String) detail.get("title"));
                } else {
                    info.setTitle((String) detail.get("title"));
                }

                if (StringUtils.isNotBlank((String) detail.get("check_item_md5"))) {
                    info.setCheck_item_md5((String) detail.get("check_item_md5"));
                } else {
                    info.setCheck_item_md5((String) detail.get("check_item_md5"));
                }

                if ((Integer) detail.get("issue_reason") != null) {
                    info.setIssue_reason((Integer) detail.get("issue_reason"));
                } else {
                    info.setIssue_reason(-1);
                }

                if (StringUtils.isNotBlank((String) detail.get("issue_reason_detail"))) {
                    info.setIssue_reason_detail((String) detail.get("issue_reason_detail"));
                } else {
                    info.setIssue_reason_detail("");
                }

                if (StringUtils.isNotBlank((String) detail.get("issue_suggest"))) {
                    info.setIssue_suggest((String) detail.get("issue_suggest"));
                } else {
                    info.setIssue_suggest("");
                }

                if (StringUtils.isNotBlank((String) detail.get("potential_risk"))) {
                    info.setPotential_risk((String) detail.get("potential_risk"));
                } else {
                    info.setPotential_risk("");
                }
                if (StringUtils.isNotBlank((String) detail.get("preventive_action_detail"))) {
                    info.setPreventive_action_detail((String) detail.get("preventive_action_detail"));
                } else {
                    info.setPreventive_action_detail("");
                }
            }
            List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> objects1 = Lists.newArrayList();
            objects1.add(info);
            items.setDetail(objects1);
            if (!logUuids.contains(items.getUuid())) {
                logUuids.add(items.getUuid());
            }
            if (!issueUuids.contains(items.getIssue_uuid())) {
                issueUuids.add(items.getIssue_uuid());
            }
            issueLogs.add(items);

            body.setIssue_logs(issueLogs);
            body.setIssue_uuids(issueUuids);
            body.setLog_uuids(logUuids);


        });
        return body;
    }

    private void editExecute(Date begin, Date endon, Integer uid, TaskEditReq taskEditReq, List<Integer> areaIds, List<Integer> areaTypes,
                             String planBeginOn, String planEndOn, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups,
                             List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, ConfigVo config) {
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsAdd = Lists.newArrayList();
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsEdit = Lists.newArrayList();
        List<Object> checkerGroupsDel = Lists.newArrayList();
        List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser = Lists.newArrayList();
        List<UserInHouseQmCheckTask> needUpdateCheckTaskSquadUser = Lists.newArrayList();
        Map<Object, Object> doNotNeedDeleteSquaduserPkId = Maps.newHashMap();
        beforeExecute(checkerGroupsAdd, checkerGroupsEdit, checkerGroupsDel, needInsertCheckTaskSquadUser, needUpdateCheckTaskSquadUser, doNotNeedDeleteSquaduserPkId, uid, taskEditReq, areaIds, areaTypes, planBeginOn, planEndOn, checkerGroups, repairerGroups, config);
        //    # 更新验房任务
        HouseQmCheckTask taskInfo = houseQmCheckTaskService.selectByTaskId(taskEditReq.getTask_id());
        if (taskInfo == null) {
            throw new LjBaseRuntimeException(-99, "'任务信息不存在'");
        }
        taskInfo.setName(taskEditReq.getName());
        taskInfo.setAreaIds(StringUtils.join(areaIds, ","));
        taskInfo.setAreaTypes(StringUtils.join(areaTypes, ","));
        taskInfo.setPlanBeginOn(begin);
        taskInfo.setPlanEndOn(endon);
        taskInfo.setUpdateAt(new Date());
        taskInfo.setEditor(uid);
        if (taskInfo.getConfigInfo() == null) {
            HashMap<String, Object> configMap = Maps.newHashMap();
            configMap.put("repairer_refund_permission", taskEditReq.getRepairer_refund_permission());
            configMap.put("repairer_follower_permission", taskEditReq.getRepairer_follower_permission());
            configMap.put("checker_approve_permission", taskEditReq.getChecker_approve_permission());
            configMap.put("repaired_picture_status", taskEditReq.getRepaired_picture_status());
            configMap.put("issue_desc_status", taskEditReq.getIssue_desc_status());
            configMap.put("issue_default_desc", taskEditReq.getIssue_default_desc());
            taskInfo.setConfigInfo(JSON.toJSONString(configMap));
        } else {
            Map configInfo = JSON.parseObject(taskInfo.getConfigInfo(), Map.class);
            if (!taskEditReq.getRepairer_refund_permission().equals(configInfo.get("repairer_refund_permission")) ||
                    !taskEditReq.getRepairer_follower_permission().equals(configInfo.get("repairer_follower_permission")) ||
                    !taskEditReq.getChecker_approve_permission().equals(configInfo.get("checker_approve_permission")) ||
                    !taskEditReq.getRepaired_picture_status().equals(configInfo.get("repaired_picture_status")) ||
                    !taskEditReq.getIssue_desc_status().equals(configInfo.get("issue_desc_status")) ||
                    !taskEditReq.getIssue_default_desc().equals(configInfo.get("issue_default_desc"))
            ) {
                configInfo.put("repairer_refund_permission", taskEditReq.getRepairer_refund_permission());
                configInfo.put("repairer_follower_permission", taskEditReq.getRepairer_follower_permission());
                configInfo.put("checker_approve_permission", taskEditReq.getChecker_approve_permission());
                configInfo.put("repaired_picture_status", taskEditReq.getRepaired_picture_status());
                configInfo.put("issue_desc_status", taskEditReq.getIssue_desc_status());
                configInfo.put("issue_default_desc", taskEditReq.getIssue_default_desc());
                taskInfo.setConfigInfo(JSON.toJSONString(configInfo));
            }
        }
        //修改
        int add = houseQmCheckTaskService.update(taskInfo);
        if (add < 0) {
            throw new LjBaseRuntimeException(-99, "'任务信息更新失败'");
        }
        //# 更新检查人组信息
        // # 新增人组 及其 人员
        if (CollectionUtils.isNotEmpty(checkerGroupsAdd)) {
            for (int i = 0; i < checkerGroupsAdd.size(); i++) {
                HouseQmCheckTaskSquad squad = new HouseQmCheckTaskSquad();
                squad.setProjectId(taskEditReq.getProject_id());
                squad.setTaskId(taskEditReq.getTask_id());
                squad.setSquadType(checkerGroupsAdd.get(i).getGroup_role());
                squad.setName(checkerGroupsAdd.get(i).getGroup_name());
                squad.setCreateAt(new Date());
                squad.setUpdateAt(new Date());
                int squadInfo = houseQmCheckTaskSquadService.add(squad);
                if (squadInfo <= 0) {
                    log.info("create task squad failed");
                    throw new LjBaseRuntimeException(-99, "'创建任务组失败'");
                }
                List<Integer> user_ids = checkerGroups.get(i).getUser_ids();
                for (int j = 0; j < user_ids.size(); j++) {
                    Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                    Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                    Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
                    if (checkerGroups.get(i).getApprove_ids().contains(user_ids.get(j))) {
                        canApprove = CheckTaskRoleCanApproveType.Yes.getValue();
                    }
                    if (checkerGroups.get(i).getApprove_ids().contains(user_ids.get(j)) && checkerGroups.get(i).getDirect_approve_ids().contains(user_ids.get(j))) {
                        canDirectApprove = CheckTaskRoleCanDirectApproveType.Yes.getValue();
                    }
                    if ((checkerGroups.get(i).getReassign_ids().contains(user_ids.get(j)))) {
                        canReassign = CheckTaskRoleCanReassignType.Yes.getValue();
                    }
                    UserInHouseQmCheckTask qmCheckTask = new UserInHouseQmCheckTask();
                    qmCheckTask.setSquadId(squadInfo);
                    qmCheckTask.setUserId(user_ids.get(j));
                    qmCheckTask.setRoleType(checkerGroups.get(i).getGroup_role());
                    qmCheckTask.setProjectId(taskEditReq.getProject_id());
                    qmCheckTask.setTaskId(taskEditReq.getTask_id());
                    qmCheckTask.setCanApprove(canApprove);
                    qmCheckTask.setCanDirectApprove(canDirectApprove);
                    qmCheckTask.setCanReassign(canReassign);
                    qmCheckTask.setCreateAt(new Date());
                    qmCheckTask.setUpdateAt(new Date());
                    int num = userInHouseQmCheckTaskService.add(qmCheckTask);
                    if (num <= 0) {
                        log.info("create task user failed");
                        throw new LjBaseRuntimeException(-99, "创建任务组人员失败");
                    }
                }
            }
        }
        //    # 删除人组 及其 人员
        if (CollectionUtils.isNotEmpty(checkerGroupsDel)) {
            for (int i = 0; i < checkerGroupsDel.size(); i++) {
                HouseQmCheckTaskSquad dbItem = houseQmCheckTaskSquadService.selectById((Integer) checkerGroupsDel.get(i));
                if (dbItem != null) {
                    int one = houseQmCheckTaskSquadService.delete(dbItem);
                    if (one <= 0) {
                        log.info("HouseQmCheckTaskSquadDao().delete failed, squad_id=" + checkerGroupsDel.get(i) + "");
                        throw new LjBaseRuntimeException(-99, "删除人组失败");
                    }
                }
                List<UserInHouseQmCheckTask> userlist = userInHouseQmCheckTaskService.selectBysquadIdAndtaskId(checkerGroupsDel.get(i), taskEditReq.getTask_id());
                for (int j = 0; j < userlist.size(); j++) {
                    int one = userInHouseQmCheckTaskService.delete(userlist.get(j));
                    if (one <= 0) {
                        log.info("HouseQmCheckTaskDao().delete failed, squad_id=" + checkerGroupsDel.get(i) + "");
                        throw new LjBaseRuntimeException(-99, "删除人组失败");
                    }

                }


            }
        }
//    # 更新人组 及其 人员
        if (CollectionUtils.isNotEmpty(checkerGroupsEdit)) {
            for (int i = 0; i < checkerGroupsEdit.size(); i++) {
                HouseQmCheckTaskSquad dbItem = houseQmCheckTaskSquadService.selectById(checkerGroupsEdit.get(i).getGroup_id());
                if (dbItem != null) {
                    dbItem.setProjectId(taskEditReq.getProject_id());
                    dbItem.setTaskId(taskEditReq.getTask_id());
                    dbItem.setSquadType(CheckTaskRoleType.Checker.getValue());
                    dbItem.setName(checkerGroupsEdit.get(i).getGroup_name());
                    dbItem.setUpdateAt(new Date());
                    int one = houseQmCheckTaskSquadService.update(dbItem);
                    if (one <= 0) {
                        log.info("HouseQmCheckTaskSquadDao().update failed");
                        throw new LjBaseRuntimeException(-99, "更新人组失败");
                    }
                }
            }
        }
// # 更新有变动的成员信息
        if (CollectionUtils.isNotEmpty(needUpdateCheckTaskSquadUser)) {
            for (int i = 0; i < needUpdateCheckTaskSquadUser.size(); i++) {
                UserInHouseQmCheckTask dbItem = userInHouseQmCheckTaskService.selectBysquadIdAnduserIdAndtaskIdAndNotDel(needUpdateCheckTaskSquadUser.get(i).getSquadId(), needUpdateCheckTaskSquadUser.get(i).getUserId(), needUpdateCheckTaskSquadUser.get(i).getTaskId());
                dbItem.setCanApprove(needUpdateCheckTaskSquadUser.get(i).getCanApprove());
                dbItem.setCanDirectApprove(needUpdateCheckTaskSquadUser.get(i).getCanDirectApprove());
                dbItem.setCanReassign(needUpdateCheckTaskSquadUser.get(i).getCanReassign());
                dbItem.setUpdateAt(new Date());
                int one = userInHouseQmCheckTaskService.update(dbItem);
                if (one <= 0) {
                    log.info("UserInHouseQmCheckTaskDao().update failed");
                    throw new LjBaseRuntimeException(457, "更新变动成员信息失败");
                }
            }
        }


// # 要增加的人员信息
        if (!CollectionUtils.isEmpty(needInsertCheckTaskSquadUser)) {
            for (int i = 0; i < needInsertCheckTaskSquadUser.size(); i++) {
                UserInHouseQmCheckTask item = new UserInHouseQmCheckTask();
                item.setSquadId(needInsertCheckTaskSquadUser.get(i).getSquad_id());
                item.setUserId(needInsertCheckTaskSquadUser.get(i).getUser_id());
                item.setRoleType(needInsertCheckTaskSquadUser.get(i).getGroup_role());
                item.setProjectId(taskEditReq.getProject_id());
                item.setTaskId(taskEditReq.getTask_id());
                item.setCreateAt(new Date());
                item.setUpdateAt(new Date());
                item.setCanApprove(needInsertCheckTaskSquadUser.get(i).getCan_approve());
                item.setCanDirectApprove(needInsertCheckTaskSquadUser.get(i).getCan_direct_approve());
                item.setCanReassign(needInsertCheckTaskSquadUser.get(i).getCan_reassign());
                int one = userInHouseQmCheckTaskService.add(item);
                if (one <= 0) {
                    log.info("create task user failed");
                    throw new LjBaseRuntimeException(-99, "增加人员信息失败");
                }
            }

        }

//        log.info("doNotNeedDeleteSquaduserPkId={}",JSON.toJSONString(doNotNeedDeleteSquaduserPkId));
        //  # 要删除的人员信息
        if (doNotNeedDeleteSquaduserPkId.size() > 0) {
            ArrayList<Object> needDeleteIds = Lists.newArrayList();
            for (Map.Entry<Object, Object> entry : doNotNeedDeleteSquaduserPkId.entrySet()) {
                boolean notDelete = (boolean) entry.getValue();
                if (!notDelete) {
                    needDeleteIds.add(entry.getKey());
                }
            }
            if (CollectionUtils.isNotEmpty(needDeleteIds)) {
                for (int i = 0; i < needDeleteIds.size(); i++) {
                    List<UserInHouseQmCheckTask> userlist = userInHouseQmCheckTaskService.selectByIdAndTaskId(needDeleteIds.get(i), taskEditReq.getTask_id());
                    for (int j = 0; j < userlist.size(); j++) {
                        int one = userInHouseQmCheckTaskService.delete(userlist.get(j));
                        if (one <= 0) {
                            log.info("UserInHouseQmCheckTaskDao().delete failed");
                            throw new LjBaseRuntimeException(-99, "删除人员失败");
                        }
                    }
                }

            }

        }


        //  # 指定日期发起推送配置
        PushStrategyAssignTime dbConfigAssignTime = pushStrategyAssignTimeService.selectByIdAndNotDel(taskEditReq.getTask_id());
        if (config.getConfig_assign_time() != null) {
            if (dbConfigAssignTime == null) {
                PushStrategyAssignTime item = new PushStrategyAssignTime();
                item.setProjectId(taskEditReq.getProject_id());
                item.setTaskId(taskEditReq.getTask_id());
                item.setModuleId(convertCategoryCls(taskInfo.getCategoryCls()));
                item.setTyp(1);
                item.setCreateAt(new Date());
                item.setUpdateAt(new Date());
                item.setPushTime(stringToDate(config.getConfig_assign_time().getPush_time()));
                item.setUserIds(config.getConfig_assign_time().getUser_ids());
                int one = pushStrategyAssignTimeService.add(item);
                if (one <= 0) {
                    log.info("PushStrategyAssignTimeDao().add failed");
                }
            } else if (!dbConfigAssignTime.getPushTime().equals(stringToDate(config.getConfig_assign_time().getPush_time())) ||
                    !dbConfigAssignTime.getUserIds().equals(config.getConfig_assign_time().getUser_ids())
            ) {
                dbConfigAssignTime.setPushTime(stringToDate(config.getConfig_assign_time().getPush_time()));
                dbConfigAssignTime.setUserIds(config.getConfig_assign_time().getUser_ids());
                dbConfigAssignTime.setUpdateAt(new Date());
                int one = pushStrategyAssignTimeService.update(dbConfigAssignTime);
                if (one == 0) {
                    log.info("PushStrategyAssignTimeDao().update failed");
                }
            } else {
                log.info("task push strategy assign time config not change");
            }

        } else {
            if (dbConfigAssignTime != null) {
                int one = pushStrategyAssignTimeService.delete(dbConfigAssignTime);
                if (one <= 0) {
                    log.info("PushStrategyAssignTimeDao().delete failed");
                }

            } else {
                log.info("task push strategy assign time config not set");
            }
        }


        //    # 超期问题发起推送配置
        PushStrategyCategoryOverdue dbConfigCategoryOverdue = pushStrategyCategoryOverdueService.selectByTaskIdAndNotDel(taskEditReq.getTask_id());
        if (config.getConfig_category_overdue() != null) {
            if (dbConfigCategoryOverdue == null) {
                PushStrategyCategoryOverdue item = new PushStrategyCategoryOverdue();

                item.setProjectId(taskEditReq.getProject_id());
                item.setTaskId(taskEditReq.getTask_id());
                item.setModuleId(convertCategoryCls(taskInfo.getCategoryCls()));
                item.setTyp(1);
                item.setCreateAt(new Date());
                item.setUpdateAt(new Date());
                item.setCategoryKeys(config.getConfig_category_overdue().getCategory_keys());
                item.setUserIds(config.getConfig_category_overdue().getUser_ids());
                item.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(taskInfo.getPlanEndOn()) + (30 * 24 * 60 * 60), " yyyy-MM-dd HH-mm-ss"));
                int one = pushStrategyCategoryOverdueService.add(item);
                if (one <= 0) {
                    log.info("PushStrategyCategoryOverdueDao().add failed");
                }

            } else if (!dbConfigCategoryOverdue.getCategoryKeys().equals(config.getConfig_category_overdue().getCategory_keys()) ||
                    !dbConfigCategoryOverdue.getUserIds().equals(config.getConfig_category_overdue().getUser_ids())
            ) {
                dbConfigCategoryOverdue.setCategoryKeys(config.getConfig_category_overdue().getCategory_keys());
                dbConfigCategoryOverdue.setUserIds(config.getConfig_category_overdue().getUser_ids());
                dbConfigCategoryOverdue.setUpdateAt(new Date());
                int one = pushStrategyCategoryOverdueService.update(dbConfigCategoryOverdue);
                if (one <= 0) {
                    log.info("PushStrategyCategoryOverdueDao().update failed");
                }

            } else {
                if (dbConfigCategoryOverdue != null) {
                    int one = pushStrategyCategoryOverdueService.delete(dbConfigCategoryOverdue);
                    if (one <= 0) {
                        log.info("PushStrategyCategoryOverdueDao().delete failed");
                    }
                } else {
                    log.info("task push strategy category overdue config not set");
                }
            }
        }


        // # 高发问题发起推送配置
        PushStrategyCategoryThreshold dbConfigCategoryThreshold = pushStrategyCategoryThresholdService.selectTaskIdAndNotDel(taskEditReq.getTask_id());
        if (config.getConfig_category_threshold() != null) {
            if (dbConfigCategoryThreshold == null) {
                PushStrategyCategoryThreshold item = new PushStrategyCategoryThreshold();
                item.setProjectId(taskEditReq.getProject_id());
                item.setTaskId(taskEditReq.getTask_id());
                item.setModuleId(convertCategoryCls(taskInfo.getCategoryCls()));
                item.setTyp(1);
                item.setCreateAt(new Date());
                item.setUpdateAt(new Date());
                item.setCategoryKeys(config.getConfig_category_threshold().getCategory_keys());
                item.setUserIds(config.getConfig_category_threshold().getUser_ids());
                item.setThreshold(config.getConfig_category_threshold().getThreshold());
                item.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(taskInfo.getPlanEndOn()) + (30 * 24 * 60 * 60), " yyyy-MM-dd HH-mm-ss"));
                int Num = pushStrategyCategoryThresholdService.add(item);
                if (Num <= 0) {
                    log.info("PushStrategyCategoryOverdueDao().add failed");
                }

            } else if (!dbConfigCategoryThreshold.getCategoryKeys().equals(config.getConfig_category_threshold().getCategory_keys()) ||
                    !dbConfigCategoryThreshold.getUserIds().equals(config.getConfig_category_threshold().getUser_ids()) ||
                    !dbConfigCategoryThreshold.getThreshold().equals(config.getConfig_category_threshold().getThreshold())
            ) {
                dbConfigCategoryThreshold.setCategoryKeys(config.getConfig_category_threshold().getCategory_keys());
                dbConfigCategoryThreshold.setUserIds(config.getConfig_category_threshold().getUser_ids());
                dbConfigCategoryThreshold.setThreshold(config.getConfig_category_threshold().getThreshold());
                dbConfigCategoryThreshold.setUpdateAt(new Date());
                int one = pushStrategyCategoryThresholdService.update(dbConfigCategoryThreshold);
                if (one <= 0) {
                    log.info("PushStrategyCategoryThresholdDao().update failed");
                }
            } else {
                if (dbConfigCategoryThreshold != null) {
                    int one = pushStrategyCategoryThresholdService.delete(dbConfigCategoryThreshold);
                    if (one <= 0) {
                        log.info("PushStrategyCategoryThresholdDao().delete failed");
                    }
                } else {
                    log.info("task push strategy category threshold config not set");
                }

            }
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
            groupVo.setUser_ids(CollectionUtil.removeDuplicate(userIds));
            groupVo.setApprove_ids(CollectionUtil.removeDuplicate(approveIds));
            groupVo.setDirect_approve_ids(CollectionUtil.removeDuplicate(directApproveIds));
            groupVo.setReassign_ids(CollectionUtil.removeDuplicate(reassignIds));
            objects.add(groupVo);

        }
        return objects;
    }

    private void Execute(Integer uid, TaskReq taskReq, List<Integer> areaIds, List<Integer> areaTypes, String planBeginOn, String planEndOn, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups, List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, ConfigVo config) {
        Task task = new Task();
        task.setName(taskReq.getName());
        task.setProjectId(taskReq.getProject_id());
        task.setCreatorId(uid);
        task.setCreateAt(new Date());
        task.setUpdateAt(new Date());
        task.setTyp(taskReq.getCategory_cls());
        int taskObj = taskService.add(task);
        if (taskObj <= 0) {
            log.info("create task failed");
            throw new LjBaseRuntimeException(-99, "'创建任务失败'");
        }
        HashMap<String, Object> config_map = Maps.newHashMap();
        config_map.put("repairer_refund_permission", taskReq.getRepairer_refund_permission());
        config_map.put("repairer_follower_permission", taskReq.getRepairer_follower_permission());
        config_map.put("checker_approve_permission", taskReq.getChecker_approve_permission());
        config_map.put("repaired_picture_status", taskReq.getRepaired_picture_status());
        config_map.put("issue_desc_status", taskReq.getIssue_desc_status());
        config_map.put("issue_default_desc", taskReq.getIssue_default_desc());
        String config_info = JSONObject.toJSONString(config_map);
        HouseQmCheckTask houseQmCheckTask = new HouseQmCheckTask();
        houseQmCheckTask.setProjectId(taskReq.getProject_id());
        houseQmCheckTask.setTaskId(taskObj);
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
        houseQmCheckTask.setCreateAt(new Date());
        houseQmCheckTask.setUpdateAt(new Date());
        String stringAreaIds = areaIds.toString();
        String s = StringUtils.removeStart(stringAreaIds, "[");
        String s1 = StringUtils.removeEnd(s, "]");
        houseQmCheckTask.setAreaIds(s1.replaceAll(" ", ""));
        String stringAreaTypes = areaTypes.toString();
        String s2 = StringUtils.removeStart(stringAreaTypes, "[");
        String s3 = StringUtils.removeEnd(s2, "]");
        houseQmCheckTask.setAreaTypes(s3.replaceAll(" ", ""));
        //返回主键
        int one = houseQmCheckTaskService.add(houseQmCheckTask);
        HouseQmCheckTask checktaskObj = houseQmCheckTaskService.selectById(one);
        if (one <= 0) {
            log.info("create check task failed");
            throw new LjBaseRuntimeException(-99, "创建检查任务失败");
        }

        for (int i = 0; i < checkerGroups.size(); i++) {//group
            HouseQmCheckTaskSquad squad = new HouseQmCheckTaskSquad();
            squad.setProjectId(taskReq.getProject_id());
            squad.setTaskId(taskObj);
            squad.setCreateAt(new Date());
            squad.setUpdateAt(new Date());
            squad.setName(checkerGroups.get(i).getGroup_name());
            squad.setSquadType(checkerGroups.get(i).getGroup_role());
            //返回主键
            int squadInfo = houseQmCheckTaskSquadService.add(squad);
            if (squadInfo <= 0) {
                log.info("create task squad failed");
                throw new LjBaseRuntimeException(-99, "'创建任务组失败'");
            }
            List<Integer> user_ids = checkerGroups.get(i).getUser_ids();
            for (int j = 0; j < user_ids.size(); j++) {

                Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
                if (checkerGroups.get(i).getApprove_ids().contains(user_ids.get(j))) {
                    canApprove = CheckTaskRoleCanApproveType.Yes.getValue();
                }
                if (checkerGroups.get(i).getApprove_ids().contains(user_ids.get(j)) && checkerGroups.get(i).getDirect_approve_ids().contains(user_ids.get(j))) {
                    canDirectApprove = CheckTaskRoleCanDirectApproveType.Yes.getValue();
                }
                if ((checkerGroups.get(i).getReassign_ids().contains(user_ids.get(j)))) {
                    canReassign = CheckTaskRoleCanReassignType.Yes.getValue();
                }
                UserInHouseQmCheckTask qmCheckTask = new UserInHouseQmCheckTask();
                qmCheckTask.setSquadId(squadInfo);
                qmCheckTask.setUserId(user_ids.get(j));
                qmCheckTask.setRoleType(checkerGroups.get(i).getGroup_role());
                qmCheckTask.setProjectId(taskReq.getProject_id());
                qmCheckTask.setTaskId(taskObj);
                qmCheckTask.setCanApprove(canApprove);
                qmCheckTask.setCreateAt(new Date());
                qmCheckTask.setUpdateAt(new Date());
                qmCheckTask.setCanDirectApprove(canDirectApprove);
                qmCheckTask.setCanReassign(canReassign);
                int num = userInHouseQmCheckTaskService.add(qmCheckTask);
                if (num <= 0) {
                    log.info("create task user failed");
                    throw new LjBaseRuntimeException(-99, "创建任务组人员失败");
                }

            }


        }

        for (int i = 0; i < repairerGroups.size(); i++) {//group
            //返回主键
            HouseQmCheckTaskSquad squad = new HouseQmCheckTaskSquad();
            squad.setProjectId(taskReq.getProject_id());
            squad.setTaskId(taskObj);
            squad.setName(repairerGroups.get(i).getGroup_name());
            squad.setSquadType(repairerGroups.get(i).getGroup_role());
            squad.setCreateAt(new Date());
            squad.setUpdateAt(new Date());
            int squadInfo = houseQmCheckTaskSquadService.add(squad);
            if (squadInfo <= 0) {
                log.info("create task squad failed");
                throw new LjBaseRuntimeException(-99, "'创建任务组失败'");
            }
            List<Integer> userId = repairerGroups.get(i).getUser_ids();
            for (int j = 0; j < userId.size(); j++) {
                Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();

                UserInHouseQmCheckTask qmCheckTask = new UserInHouseQmCheckTask();
                qmCheckTask.setSquadId(squadInfo);
                qmCheckTask.setUserId(userId.get(j));
                qmCheckTask.setRoleType(repairerGroups.get(i).getGroup_role());
                qmCheckTask.setProjectId(taskReq.getProject_id());
                qmCheckTask.setTaskId(taskObj);
                qmCheckTask.setCreateAt(new Date());
                qmCheckTask.setUpdateAt(new Date());
                qmCheckTask.setCanApprove(canApprove);
                qmCheckTask.setCanDirectApprove(canDirectApprove);
                qmCheckTask.setCanReassign(canReassign);
                int num = userInHouseQmCheckTaskService.add(qmCheckTask);
                if (num <= 0) {
                    log.info("create task user failed");
                    throw new LjBaseRuntimeException(-99, "创建任务组人员失败");
                }
            }


        }

        //  # 指定日期发起推送配置
        if (config.getConfig_assign_time() != null) {
            PushStrategyAssignTime pushStrategyAssignTime = new PushStrategyAssignTime();
            pushStrategyAssignTime.setProjectId(taskReq.getProject_id());
            pushStrategyAssignTime.setTaskId(taskObj);
            pushStrategyAssignTime.setModuleId(convertCategoryCls(taskReq.getCategory_cls()));
            pushStrategyAssignTime.setTyp(1);
            pushStrategyAssignTime.setCreateAt(new Date());
            pushStrategyAssignTime.setUpdateAt(new Date());
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
            pushStrategyCategoryOverdue.setTaskId(taskObj);
            pushStrategyCategoryOverdue.setModuleId(convertCategoryCls(taskReq.getCategory_cls()));
            pushStrategyCategoryOverdue.setTyp(1);
            pushStrategyCategoryOverdue.setCreateAt(new Date());
            pushStrategyCategoryOverdue.setUpdateAt(new Date());
            pushStrategyCategoryOverdue.setCategoryKeys(config.getConfig_category_overdue().getCategory_keys());
            pushStrategyCategoryOverdue.setUserIds(config.getConfig_category_overdue().getUser_ids());
            pushStrategyCategoryOverdue.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(checktaskObj.getPlanEndOn()) + (30 * 24 * 60 * 60), " yyyy-MM-dd HH-mm-ss"));
            int Num = pushStrategyCategoryOverdueService.add(pushStrategyCategoryOverdue);
            if (Num <= 0) {
                log.info("PushStrategyCategoryOverdueDao().add failed");
            }

        }
        //  # 高发问题发起推送配置
        if (config.getConfig_category_threshold() != null) {
            PushStrategyCategoryThreshold pushStrategyCategoryThreshold = new PushStrategyCategoryThreshold();
            pushStrategyCategoryThreshold.setProjectId(taskReq.getProject_id());
            pushStrategyCategoryThreshold.setTaskId(taskObj);
            pushStrategyCategoryThreshold.setModuleId(convertCategoryCls(taskReq.getCategory_cls()));
            pushStrategyCategoryThreshold.setTyp(1);
            pushStrategyCategoryThreshold.setCategoryKeys(config.getConfig_category_threshold().getCategory_keys());
            pushStrategyCategoryThreshold.setUserIds(config.getConfig_category_threshold().getUser_ids());
            pushStrategyCategoryThreshold.setThreshold(config.getConfig_category_threshold().getThreshold());
            pushStrategyCategoryThreshold.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(checktaskObj.getPlanEndOn()) + (30 * 24 * 60 * 60), " yyyy-MM-dd HH-mm-ss"));
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
        Map<String, Object> push_strategys_config = JSON.parseObject(push_strategy_config, Map.class);
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
        vo.setUser_ids(list);
        result.add(vo);
        return result;
    }

    private List<ApiBuildingQmCheckTaskSquadObjVo> unmarshCheckerGroups(String checker_groups) {
        List<Map<String, Object>> list = JSON.parseObject(checker_groups, List.class);
        ArrayList<ApiBuildingQmCheckTaskSquadObjVo> result = Lists.newArrayList();
        list.forEach(checkergroups -> {
            ApiBuildingQmCheckTaskSquadObjVo objVo = new ApiBuildingQmCheckTaskSquadObjVo();
            if ((Integer) checkergroups.get("id") != null) {
                objVo.setId((Integer) checkergroups.get("id"));
            } else {
                objVo.setId(0);
            }
            objVo.setName((String) checkergroups.get("name"));
            if (objVo.getName() == null) {
                log.info("name not exist, data='unmarshCheckerGroups'");
                throw new LjBaseRuntimeException(-99, "name not exist");
            }
            objVo.setUser_ids((String) checkergroups.get("user_ids"));
            if (objVo.getUser_ids() == null) {
                log.info("name not exist, data='unmarshCheckerGroups'");
                throw new LjBaseRuntimeException(-99, "user_ids not exist");
            }
            objVo.setApprove_ids((String) checkergroups.get("approve_ids"));
            if (objVo.getApprove_ids() == null) {
                log.info("name not exist, data='unmarshCheckerGroups'");
                throw new LjBaseRuntimeException(-99, "approve_ids not exist");
            }
            objVo.setDirect_approve_ids((String) checkergroups.get("direct_approve_ids"));
            if (objVo.getDirect_approve_ids() == null) {
                log.info("name not exist, data='unmarshCheckerGroups'");
                throw new LjBaseRuntimeException(-99, "direct_approve_ids not exist");
            }

            objVo.setReassign_ids((String) checkergroups.get("reassign_ids"));
            result.add(objVo);
        });

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
            task.setArea_type(item.getAreaTypes());
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

    @Override
    public Map<String, Object> issuestatisticexport(String category_cls, String items) {
        Integer result = 0;
        String message = "success";
        List<NodeDataVo> dataList = Lists.newArrayList();
        HashMap<String, NodeDataVo> dataMap = Maps.newHashMap();
        JSONArray jsonArray = JSON.parseArray(items);
        List<Map> itemsList = jsonArray.toJavaList(Map.class);
        List<String> pathKeys = Lists.newArrayList();
        for (Map<String, Object> item : itemsList) {
            NodeDataVo nodeDataVo = new NodeDataVo();
            nodeDataVo.setKey((String) item.get("key"));
            nodeDataVo.setParent_key((String) item.get("parent_key"));
            nodeDataVo.setIssue_count((Integer) item.get("issue_count"));
            nodeDataVo.setName((String) item.get("name"));
            nodeDataVo.setValid_node(true);
            nodeDataVo.setPath_name(nodeDataVo.getKey() + "/");
            pathKeys.add(0, nodeDataVo.getKey());
            nodeDataVo.setPath_keys(pathKeys);
            if (StringUtils.isBlank(nodeDataVo.getKey()) || StringUtils.isBlank(nodeDataVo.getParent_key()) || nodeDataVo.getIssue_count() == null || StringUtils.isBlank(nodeDataVo.getName())) {
                continue;
            }
            dataList.add(nodeDataVo);
            dataMap.put(nodeDataVo.getKey(), nodeDataVo);
        }
        int maxCol = 0;
        ArrayList<String> path_key = Lists.newArrayList();
        for (NodeDataVo item : dataList) {
            String parentKey = item.getParent_key();
            while (parentKey.length() > 0) {
                item.setPath_name(String.format("%s/%s", parentKey, item.getPath_name()));
                path_key.add(0, parentKey);
                item.setPath_keys(path_key);
                if (dataMap.containsKey(parentKey)) {
                    parentKey = dataMap.get(parentKey).getParent_key();
                } else {
                    item.setValid_node(false);
                    break;
                }
            }
            dataMap.get(item.getKey()).setPath_name(item.getPath_name());
            dataMap.get(item.getKey()).setPath_keys(item.getPath_keys());
            dataMap.get(item.getKey()).setValid_node(item.getValid_node());
            if (item.getPath_keys().size() > maxCol) {
                maxCol = item.getPath_keys().size();
            }
        }
        for (NodeDataVo obj : dataList) {
            if (obj.getValid_node() == false) {
                continue;
            }
            for (NodeDataVo item : dataList) {
                if (item.getPath_name().indexOf(obj.getPath_name()) == 0) {
                    boolean isLast = true;
                    for (NodeDataVo temp : dataList) {
                        if (temp.getPath_name().indexOf(item.getPath_name()) == 0 && temp.getPath_name().length() != item.getPath_name().length()) {
                            isLast = false;
                            break;
                        }
                    }
                    if (isLast) {
                        Integer child_count = obj.getChild_count();
                        child_count += 1;
                    }
                }
            }
            dataMap.get(obj.getKey()).setChild_count(obj.getChild_count());
        }
        List<NodeVo> nodeTree = Lists.newArrayList();
        for (int i = 1; i < maxCol + 1; i++) {
            for (NodeDataVo item : dataList) {
                if (!item.getValid_node()) {
                    continue;
                }
                if (item.getPath_keys().size() != i) {
                    continue;
                }
                List<NodeVo> nodeList = nodeTree;
                Integer nodeCol = 0;
                while (true) {
                    boolean existNode = false;
                    for (NodeVo o : nodeList) {
                        if (item.getPath_keys().get(nodeCol).equals(o.getData().getKey())) {
                            nodeList = o.getChild_list();
                            existNode = true;
                            nodeCol += 1;
                            break;
                        }
                    }
                    if (!existNode) {
                        nodeList.add(new NodeVo(item));
                        break;
                    }
                }
            }
        }
        SXSSFWorkbook wb = ExportUtils.exportIssueStatisticExcel(nodeTree, maxCol);
        //path = ret.get('path', '')
        //        dt = datetime.datetime.strftime(datetime.datetime.now(), '%Y%m%d%H%M%S')
        //        category_name = CategoryClsType.get_title(category_cls, u'工程检查')
        //        filename = u'%s_问题详情_%s.xlsx' % (category_name, dt)


        String dt = DateUtil.getNowTimeStr("yyyyMMddHHmmss");
        String category_name = CategoryClsTypeEnum.getName(Integer.valueOf(category_cls));
        if (category_name == null) category_name = "工程检查";
        String fileName = String.format("%s_问题详情_%s.xlsx", category_name, dt);
        Map<String, Object> map = Maps.newHashMap();
        map.put("fileName", fileName);
        map.put("workbook", wb);
        map.put("result", result);
        map.put("message", message);
        return map;
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

}
