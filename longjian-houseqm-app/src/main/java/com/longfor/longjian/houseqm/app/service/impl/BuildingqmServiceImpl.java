package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import com.longfor.longjian.houseqm.app.utils.ExportUtils;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.export.NodeDataVo;
import com.longfor.longjian.houseqm.app.vo.export.NodeVo;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskConfig;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskMsg;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyAssignTime;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryOverdue;
import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryThreshold;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    private static final String ISSUEREASON="IssueReason";
    private static final String ISSUE_REASOND_ETAIL="IssueReasonDetail";
    private static final String ISSUE_SUGGEST="IssueSuggest";
    private static final String POTENTIAL_RISK="PotentialRisk";
    private static final String PREVENTIVE_ACTION_DETAIL="PreventiveActionDetail";
    private static final String YMDHMS="yyyy-MM-dd HH:mm:ss";
    private static final String AREA_IDS="areaIds";
    private static final String AREA_TYPES="areaTypes";
    private static final String PLAN_BEGIN_ON="planBeginOn";
    private static final String PLAN_END_ON="planEndOn";
    private static final String CHECKER_GROUPS ="checkerGroups";
    private static final String REPAIR_GROUPS ="repairerGroups";
    private static final String CONFIG="config";
    private static final String REPAIR_REFOUND_PERMISSION="repairer_refund_permission";
    private static final String REPAIR_FOLLOWER_PERMISSION= "repairer_follower_permission";
    private static final String CHECKER_APPROVE_PERMISSION=  "checker_approve_permission";
    private static final String REPAIR_PICTURE_STATUS= "repaired_picture_status";
    private static final String ISSUE_DESC_STATUS="issue_desc_status";
    private static final String ISSUE_DEFAULT_DESC= "issue_default_desc";
    private static final String CREATE_TASK_SQUAD_FAILAD="create task squad failed";
    private static final String CREATE_FAIL="创建任务组失败";
    private static final String CREATE_TASK_USER_FAILED="create task user failed";
    private static final String CREATE_TASK_PEOPLE_FAIL="创建任务组人员失败";
    private static final String PUSH_STRATEGY_CATEGORY_OVERDUE_ADD_FAIL ="PushStrategyCategoryOverdueDao().add failed";
    private static final String USER_IDS ="user_ids";
    private static final String NAME_NOT_EXIST= "name not exist, data='unmarshCheckerGroups'";
    private static final String QUESTION_NO_DESC= "(该问题无文字描述)";

    public static void setTaskProperties(TaskVo tvo, ApiBuildingQmCheckTaskMsg abm, Map<Integer, ApiBuildingQmCheckTaskConfig> taskMap, HouseQmCheckTask checkTask) {
        ApiBuildingQmCheckTaskMsg task = new ApiBuildingQmCheckTaskMsg();
        task.setProject_id(checkTask.getProjectId());
        task.setTask_id(checkTask.getTaskId());
        task.setName(checkTask.getName());
        task.setStatus(checkTask.getStatus());
        task.setCategory_cls(checkTask.getCategoryCls());
        task.setRoot_category_key(checkTask.getRootCategoryKey());
        task.setArea_ids(checkTask.getAreaIds());
        task.setArea_type(checkTask.getAreaTypes());

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
            task.setIssue_default_desc("该问题无文字描述");
        }
        task.setPlan_begin_on(DateUtil.datetimeToTimeStamp(checkTask.getPlanBeginOn()));
        task.setPlan_end_on(DateUtil.datetimeToTimeStamp(checkTask.getPlanEndOn()));
        task.setCreate_at(DateUtil.datetimeToTimeStamp(checkTask.getCreateAt()));
        task.setUpdate_at(DateUtil.datetimeToTimeStamp(checkTask.getUpdateAt()));
        task.setDelete_at(DateUtil.datetimeToTimeStamp(checkTask.getDeleteAt()));
        if (tvo != null) {
            try {
                BeanUtils.copyProperties(tvo, task);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        if (abm != null) {
            try {
                BeanUtils.copyProperties(abm, task);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public TaskListVo myTaskList(Integer userId) {

        List<UserInHouseQmCheckTask> userTasks = userInHouseQmCheckTaskService.searchByUserId(userId);
        Set<Integer> taskIds = Sets.newHashSet();

        for (UserInHouseQmCheckTask task : userTasks) {
            taskIds.add(task.getTaskId());
        }

        Map<Integer, ApiBuildingQmCheckTaskConfig> taskMap = creatTaskMap(taskIds);
        List<HouseQmCheckTask> houseqmTasks = houseQmCheckTaskService.selectByTaskIdsEvenDeleted(taskIds);
        List<TaskVo> vos = Lists.newArrayList();
        buildTaskVo(vos, houseqmTasks, taskMap);
        TaskListVo taskListVo = new TaskListVo();
        taskListVo.setTask_list(vos);

        return taskListVo;
    }

    private void buildTaskVo(List<TaskVo> vos, List<HouseQmCheckTask> houseqmTasks, Map<Integer, ApiBuildingQmCheckTaskConfig> taskMap) {
        for (HouseQmCheckTask item : houseqmTasks) {
            TaskVo task = new TaskVo();
            BuildingqmServiceImpl.setTaskProperties(task, null, taskMap, item);
            vos.add(task);
        }
    }

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
            logVo.setProject_id(issueLog.getProjectId());
            logVo.setTask_id(issueLog.getTaskId());
            logVo.setUuid(issueLog.getUuid());
            logVo.setIssue_uuid(issueLog.getIssueUuid());
            logVo.setSender_id(issueLog.getSenderId());
            logVo.setDesc(issueLog.getDesc());
            logVo.setStatus(issueLog.getStatus());
            logVo.setAttachment_md5_list(issueLog.getAttachmentMd5List());
            logVo.setAudio_md5_list(issueLog.getAudioMd5List());
            logVo.setMemo_audio_md5_list(issueLog.getMemoAudioMd5List());
            logVo.setClient_create_at(DateUtil.datetimeToTimeStamp(issueLog.getClientCreateAt()));

            JSONObject dicDetail = JSONObject.parseObject(issueLog.getDetail());
            MyIssuePatchListVo.LogDetailVo detail = myIssuePatchListVo.new LogDetailVo();
            if (issueMap.get(issueLog.getIssueUuid()) != null) {
                detail.setTitle(issueMap.get(issueLog.getIssueUuid()).getTitle());
                detail.setArea_id(issueMap.get(issueLog.getIssueUuid()).getAreaId());
                detail.setPos_x(issueMap.get(issueLog.getIssueUuid()).getPosX());
                detail.setPos_y(issueMap.get(issueLog.getIssueUuid()).getPosY());
                detail.setTyp(issueMap.get(issueLog.getIssueUuid()).getTyp());
            }
            detail.setPlan_end_on(dicDetail.getIntValue(PLAN_END_ON));
            detail.setEnd_on(dicDetail.getIntValue("EndOn"));
            detail.setRepairer_id(dicDetail.getIntValue("RepairerId"));
            detail.setRepairer_follower_ids(dicDetail.getString("RepairerFollowerIds"));
            detail.setCondition(dicDetail.getIntValue("Condition"));
            detail.setCategory_cls(dicDetail.getIntValue("CategoryCls"));
            detail.setCategory_key(dicDetail.getString("CategoryKey"));
            detail.setCheck_item_key(dicDetail.getString("CheckItemKey"));
            detail.setIssue_reason(dicDetail.getIntValue(ISSUEREASON));
            detail.setIssue_reason_detail(dicDetail.getString(ISSUE_REASOND_ETAIL));
            detail.setIssue_suggest(dicDetail.getString(ISSUE_SUGGEST));
            detail.setPotential_risk(dicDetail.getString(POTENTIAL_RISK));
            detail.setPreventive_action_detail(dicDetail.getString(PREVENTIVE_ACTION_DETAIL));
            logVo.setDetail(detail);
            logVo.setUpdate_at(DateUtil.datetimeToTimeStamp(issueLog.getUpdateAt()));
            logVo.setDelete_at(DateUtil.datetimeToTimeStamp(issueLog.getDeleteAt()));
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

    private Map<String, Object> prepareForCreateOrEdit(TaskReq taskReq) {
        Map<String, Object> paramMap = new HashMap<>();
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
        SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS);
        Date begin = null;
        Date endon = null;
        try {
            begin = sdf.parse(planBeginOn);
            endon = sdf.parse(planEndOn);
            if (DateUtil.datetimeToTimeStamp(endon) < DateUtil.datetimeToTimeStamp(begin)) {
                throw new LjBaseRuntimeException(-99, "计划结束时间有误");
            }
        } catch (ParseException e) {
            log.error("error:", e.getMessage());
        }
        paramMap.put(AREA_IDS, areaIds);
        paramMap.put(AREA_TYPES, areaTypes);
        paramMap.put(PLAN_BEGIN_ON, planBeginOn);
        paramMap.put(PLAN_END_ON, planEndOn);
        paramMap.put(CHECKER_GROUPS, checkerGroups);
        paramMap.put(REPAIR_GROUPS, repairerGroups);
        paramMap.put(CONFIG, config);
        paramMap.put("begin", begin);
        paramMap.put("endon", endon);
        return paramMap;
    }

    @Override
    public void create(Integer uid, TaskReq taskReq) {
        Map<String, Object> paramMap = prepareForCreateOrEdit(taskReq);
        execute(uid, taskReq, (List<Integer>) paramMap.get(AREA_IDS), (List<Integer>) paramMap.get(AREA_TYPES), (String) paramMap.get(PLAN_BEGIN_ON), (String) paramMap.get(PLAN_END_ON), (List<ApiBuildingQmTaskMemberGroupVo>) paramMap.get(CHECKER_GROUPS), (List<ApiBuildingQmTaskMemberGroupVo>) paramMap.get(REPAIR_GROUPS), (ConfigVo) paramMap.get(CONFIG));
    }

    @Override
    public List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId) {
        return houseQmCheckTaskSquadService.searchHouseqmCheckTaskSquad(projectId, taskId);
    }

    @Override
    public void edit(Integer uid, TaskEditReq taskEditReq) {
        TaskReq taskReq = new TaskReq();
        try {
            BeanUtils.copyProperties(taskReq, taskEditReq);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        Map<String, Object> paramMap = this.prepareForCreateOrEdit(taskReq);
        editExecute(paramMap, uid, taskEditReq);

    }

    private void beforeExecute(List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsAdd, List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsEdit, List<Object> checkerGroupsDel, List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser, List<UserInHouseQmCheckTask> needUpdateCheckTaskSquadUser, Map doNotNeedDeleteSquaduserPkId,Map<String,Object> paramMap) {
      /*  Integer uid = (Integer) paramMap.get("uid");
        List<Integer> areaIds= (List<Integer>) paramMap.get("areaIds");
        List<Integer> areaTypes= (List<Integer>) paramMap.get("areaTypes");
        String planBeginOn= (String) paramMap.get("planBeginOn");
        String planEndOn= (String) paramMap.get("planEndOn");
        ConfigVo config= (ConfigVo) paramMap.get("config");*/
        TaskEditReq taskEditReq= (TaskEditReq) paramMap.get("taskEditReq");
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroups= (List<ApiBuildingQmTaskMemberGroupVo>) paramMap.get("checkerGroups");
        List<ApiBuildingQmTaskMemberGroupVo> repairerGroups= (List<ApiBuildingQmTaskMemberGroupVo>) paramMap.get("repairerGroups");

        checkSquads(checkerGroupsDel, checkerGroupsEdit, checkerGroupsAdd, taskEditReq, checkerGroups);
        compareSquadCheckers(needUpdateCheckTaskSquadUser, needInsertCheckTaskSquadUser, checkerGroups, checkerGroupsDel, doNotNeedDeleteSquaduserPkId, taskEditReq);
        compareSquadRepairers(repairerGroups, taskEditReq, needInsertCheckTaskSquadUser, doNotNeedDeleteSquaduserPkId);
    }

    private void compareSquadCheckers(List<UserInHouseQmCheckTask> needUpdateCheckTaskSquadUser, List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups, List<Object> checkerGroupsDel, Map doNotNeedDeleteSquaduserPkId, TaskEditReq taskEditReq) {
        List<UserInHouseQmCheckTask> dbItems = userInHouseQmCheckTaskService.selectByTaskIdAndRoleType(taskEditReq.getTask_id(), CheckTaskRoleType.Checker.getValue());
        HashMap<Object, Map<Integer, UserInHouseQmCheckTask>> squadUserMap = Maps.newHashMap();
        for (UserInHouseQmCheckTask dbItem1 : dbItems) {
            if (!squadUserMap.containsKey(dbItem1.getSquadId())) {
                squadUserMap.put(dbItem1.getSquadId(), new HashMap<>());
            }
            squadUserMap.get(dbItem1.getSquadId()).put(dbItem1.getUserId(), dbItem1);
            //   # 初始化，初始值都是标记为需要删除的
            doNotNeedDeleteSquaduserPkId.put(dbItem1.getId(), false);
        }
        //  # 需要排除掉 新增 + 被删除的
        // # 但不能只判断小组信息有变动的组，因为只更新人员，是不会触发组信息变更的
        HashMap<Object, Object> ignoreSquadIdsMap = Maps.newHashMap();
        for (Object o : checkerGroupsDel) {
            ignoreSquadIdsMap.put(o, true);
        }
        //   # 排除新增的
        for (ApiBuildingQmTaskMemberGroupVo checkerGroup : checkerGroups) {
            Integer groupId = checkerGroup.getGroup_id();
            if (groupId.equals(0)) {
                continue;
            }
            //  # 排除被删除的
            if (ignoreSquadIdsMap.containsKey(checkerGroup.getGroup_id())) {
                continue;
            }
            List<Integer> userIds = checkerGroup.getUser_ids();
            for (Integer userId : userIds) {
                Integer squadId = checkerGroup.getGroup_id();
                Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
                if (checkerGroup.getApprove_ids().contains(userId)) {
                    canApprove = CheckTaskRoleCanApproveType.Yes.getValue();
                }
                if (checkerGroup.getDirect_approve_ids().contains(userId) && checkerGroup.getApprove_ids().contains(userId)) {
                    canDirectApprove = CheckTaskRoleCanDirectApproveType.Yes.getValue();
                }
                if (checkerGroup.getReassign_ids().contains(userId)) {
                    canReassign = CheckTaskRoleCanReassignType.Yes.getValue();
                }
                Map<Integer, UserInHouseQmCheckTask> map = squadUserMap.get(squadId);
                if (map != null && !map.containsKey(userId)) {
                    ApiBuildingQmTaskMemberInsertVo vo = new ApiBuildingQmTaskMemberInsertVo();
                    vo.setSquad_id(squadId);
                    vo.setGroup_role(CheckTaskRoleType.Checker.getValue());
                    vo.setUser_id(userId);
                    vo.setCan_approve(canApprove);
                    vo.setCan_direct_approve(canDirectApprove);
                    vo.setCan_reassign(canReassign);
                    needInsertCheckTaskSquadUser.add(vo);
                    continue;
                }
                //    # 将此记录标记为不需要删除
                if (map != null) {
                    UserInHouseQmCheckTask dbItem = map.get(userId);
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
                    vo.setGroup_role(CheckTaskRoleType.Repairer.getValue());
                    vo.setUser_id(repairerGroups.get(i).getUser_ids().get(j));
                    vo.setCan_approve(canApprove);
                    vo.setCan_direct_approve(canDirectApprove);
                    vo.setCan_reassign(canReassign);
                    needInsertCheckTaskSquadUser.add(vo);
                } else {
                    UserInHouseQmCheckTask o = (UserInHouseQmCheckTask) squadUserMap.get(repairerGroups.get(i).getUser_ids().get(j));
                    doNotNeedDeleteSquaduserPkId.put(o.getId(), true);
                }
            }
        }

    }

    private void checkSquads(List<Object> checkerGroupsDel, List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsEdit, List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsAdd, TaskEditReq taskEditReq, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups) {
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
        userIdRealNameInfo.forEach(item ->
                userIdRealNameMap.put(item.getUserId(), item.getRealName())
        );
        List<FileResource> fileMd5StoreKeyInfo = fileResourceService.searchFileResourceByFileMd5InAndNoDeleted(fileMd5s);
        HashMap<String, String> fileMd5StoreKeyMap = Maps.newHashMap();
        fileMd5StoreKeyInfo.forEach(item ->
                fileMd5StoreKeyMap.put(item.getFileMd5(), item.getStoreKey())
        );
        List<String> areaIds = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getAreaPathAndId().replace("//", "/"), "/", "/");
        ArrayList<Integer> areaIdsList = Lists.newArrayList();
        areaIds.forEach(item ->
            areaIdsList.add(Integer.valueOf(item))
        );
        List<Area> areaLeaveInfo = areaService.selectByAreaIds(areaIdsList);
        HashMap<Object, Object> areaIdNameMap = Maps.newHashMap();
        areaLeaveInfo.forEach(item ->
            areaIdNameMap.put(item.getId(), item.getName())
        );
        List<String> categoryKeys = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getCategoryPathAndKey().replace("//", "/"), "/", "/");
        List<CategoryV3> categoryKeysInfo = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeys);
        HashMap<String, String> categoryKeyNameMap = Maps.newHashMap();
        categoryKeysInfo.forEach(item ->
            categoryKeyNameMap.put(item.getKey(), item.getName())
        );
        List<String> checkItemKeys = StringSplitToListUtil.removeStartAndEndStrAndSplit(issueInfo.getCheckItemPathAndKey().replace("//", "/"), "/", "/");
        List<CheckItemV3> checkItemKeyNameInfo = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkItemKeys);
        HashMap<String, String> checkItemKeyNameMap = Maps.newHashMap();
        checkItemKeyNameInfo.forEach(item ->
            checkItemKeyNameMap.put(item.getKey(), item.getName())
        );

        int timestampZero = DateUtil.datetimeToTimeStamp(new Date(0));
        Map<String, Object> issueDetailJson = JSON.parseObject(issueInfo.getDetail(), Map.class);
        ApiHouseQmCheckTaskIssueDetailVo issueDetail = new ApiHouseQmCheckTaskIssueDetailVo();
        issueDetail.setIssue_reason((Integer) issueDetailJson.get(ISSUEREASON));
        issueDetail.setIssue_reason_detail((String) issueDetailJson.get(ISSUE_REASOND_ETAIL));
        issueDetail.setIssue_suggest((String) issueDetailJson.get(ISSUE_SUGGEST));
        issueDetail.setPotential_risk((String) issueDetailJson.get(POTENTIAL_RISK));
        issueDetail.setPreventive_action_detail((String) issueDetailJson.get(PREVENTIVE_ACTION_DETAIL));
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
        categoryKeys.forEach(item ->
            objects.add(categoryKeyNameMap.get(item))
        );
        issue.setCategory_path_name(objects);
        issue.setCheck_item_key(issueInfo.getCheckItemKey());
        issue.setCheck_item_path_and_key(issueInfo.getCheckItemPathAndKey());
        List<String> objects1 = Lists.newArrayList();
        checkItemKeys.forEach(item ->
            objects1.add(checkItemKeyNameMap.get(item))
        );
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
        issueAttachmentMd5List.forEach(item ->
            objects2.add(fileMd5StoreKeyMap.get(item))
        );
        issue.setAttachment_url_list(objects2);
        List<String> objects3 = Lists.newArrayList();
        issueAudioMd5List.forEach(item ->
            objects3.add(fileMd5StoreKeyMap.get(item))
        );
        issue.setAudio_url_list(objects3);
        issue.setAttachment_md5_list(issueInfo.getAttachmentMd5List());
        issue.setAudio_md5_list(issueInfo.getAudioMd5List());
        issue.setRepairer_name(userIdRealNameMap.get(issueInfo.getRepairerId()));
        List<String> objects4 = Lists.newArrayList();
        followerIds.forEach(item ->
            objects4.add(userIdRealNameMap.get(item))
        );
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
            logAttachmentMd5.forEach(item ->
                md5List.add(fileMd5StoreKeyMap.get(item))
            );
            singleLog.setAttachment_url_list(md5List);
            ArrayList<String> urlList = Lists.newArrayList();
            audioMd5List.forEach(item ->
                urlList.add(fileMd5StoreKeyMap.get(item))
            );
            singleLog.setAudio_url_list(urlList);
            ArrayList<String> audioUrlList = Lists.newArrayList();
            memoAudioMd5List.forEach(item ->
                audioUrlList.add(fileMd5StoreKeyMap.get(item))
            );
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

    private void editExecute(Map<String,Object> map, Integer uid, TaskEditReq taskEditReq) {

        List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsAdd = Lists.newArrayList();
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroupsEdit = Lists.newArrayList();
        List<Object> checkerGroupsDel = Lists.newArrayList();
        List<ApiBuildingQmTaskMemberInsertVo> needInsertCheckTaskSquadUser = Lists.newArrayList();
        List<UserInHouseQmCheckTask> needUpdateCheckTaskSquadUser = Lists.newArrayList();
        Map<Object, Object> doNotNeedDeleteSquaduserPkId = Maps.newHashMap();
        Map<String, Object> paramMap = Maps.newHashMap();
        List<ApiBuildingQmTaskMemberGroupVo> checkerGroups = (List<ApiBuildingQmTaskMemberGroupVo>) map.get(CHECKER_GROUPS);
        //List<ApiBuildingQmTaskMemberGroupVo> repairGroups = (List<ApiBuildingQmTaskMemberGroupVo>) map.get(REPAIR_GROUPS);
        ConfigVo config = (ConfigVo) map.get(CONFIG);
        paramMap.put("uid",uid);
        paramMap.put("taskEditReq",taskEditReq);
        paramMap.put("areaIds",map.get(AREA_IDS));
        paramMap.put("areaTypes",map.get(AREA_TYPES));
        paramMap.put("planBeginOn",map.get(PLAN_BEGIN_ON));
        paramMap.put("planEndOn",map.get(PLAN_END_ON));
        paramMap.put("checkerGroups",map.get(CHECKER_GROUPS));
        paramMap.put("repairerGroups",map.get(REPAIR_GROUPS));
        paramMap.put("config",config);

        beforeExecute(checkerGroupsAdd, checkerGroupsEdit, checkerGroupsDel, needInsertCheckTaskSquadUser, needUpdateCheckTaskSquadUser, doNotNeedDeleteSquaduserPkId, paramMap);
        //    # 更新验房任务
        HouseQmCheckTask taskInfo = houseQmCheckTaskService.selectByTaskId(taskEditReq.getTask_id());
        if (taskInfo == null) {
            throw new LjBaseRuntimeException(-99, "'任务信息不存在'");
        }
        taskInfo.setName(taskEditReq.getName());
        taskInfo.setAreaIds(StringUtils.join(map.get(AREA_IDS), ","));
        taskInfo.setAreaTypes(StringUtils.join(map.get(AREA_TYPES), ","));
        taskInfo.setPlanBeginOn((Date) map.get("begin"));
        taskInfo.setPlanEndOn((Date) map.get("endon"));
        taskInfo.setUpdateAt(new Date());
        taskInfo.setEditor(uid);
        if (taskInfo.getConfigInfo() == null) {
            HashMap<String, Object> configMap = Maps.newHashMap();
            configMap.put(REPAIR_REFOUND_PERMISSION, taskEditReq.getRepairer_refund_permission());
            configMap.put(REPAIR_FOLLOWER_PERMISSION, taskEditReq.getRepairer_follower_permission());
            configMap.put(CHECKER_APPROVE_PERMISSION, taskEditReq.getChecker_approve_permission());
            configMap.put(REPAIR_PICTURE_STATUS, taskEditReq.getRepaired_picture_status());
            configMap.put(ISSUE_DESC_STATUS, taskEditReq.getIssue_desc_status());
            configMap.put(ISSUE_DEFAULT_DESC, taskEditReq.getIssue_default_desc());
            taskInfo.setConfigInfo(JSON.toJSONString(configMap));
        } else {
            Map configInfo = JSON.parseObject(taskInfo.getConfigInfo(), Map.class);
            if (!taskEditReq.getRepairer_refund_permission().equals(configInfo.get(REPAIR_REFOUND_PERMISSION)) ||
                    !taskEditReq.getRepairer_follower_permission().equals(configInfo.get(REPAIR_FOLLOWER_PERMISSION)) ||
                    !taskEditReq.getChecker_approve_permission().equals(configInfo.get(CHECKER_APPROVE_PERMISSION)) ||
                    !taskEditReq.getRepaired_picture_status().equals(configInfo.get(REPAIR_PICTURE_STATUS)) ||
                    !taskEditReq.getIssue_desc_status().equals(configInfo.get(ISSUE_DESC_STATUS)) ||
                    !taskEditReq.getIssue_default_desc().equals(configInfo.get(ISSUE_DEFAULT_DESC))
            ) {
                configInfo.put(REPAIR_REFOUND_PERMISSION, taskEditReq.getRepairer_refund_permission());
                configInfo.put(REPAIR_FOLLOWER_PERMISSION, taskEditReq.getRepairer_follower_permission());
                configInfo.put(CHECKER_APPROVE_PERMISSION, taskEditReq.getChecker_approve_permission());
                configInfo.put(REPAIR_PICTURE_STATUS, taskEditReq.getRepaired_picture_status());
                configInfo.put(ISSUE_DESC_STATUS, taskEditReq.getIssue_desc_status());
                configInfo.put(ISSUE_DEFAULT_DESC, taskEditReq.getIssue_default_desc());
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
                    log.info(CREATE_TASK_SQUAD_FAILAD);
                    throw new LjBaseRuntimeException(-99, CREATE_FAIL);
                }
                List<Integer> userIds = checkerGroups.get(i).getUser_ids();
                for (int j = 0; j < userIds.size(); j++) {
                    Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                    Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                    Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
                    if (checkerGroups.get(i).getApprove_ids().contains(userIds.get(j))) {
                        canApprove = CheckTaskRoleCanApproveType.Yes.getValue();
                    }
                    if (checkerGroups.get(i).getApprove_ids().contains(userIds.get(j)) && checkerGroups.get(i).getDirect_approve_ids().contains(userIds.get(j))) {
                        canDirectApprove = CheckTaskRoleCanDirectApproveType.Yes.getValue();
                    }
                    if ((checkerGroups.get(i).getReassign_ids().contains(userIds.get(j)))) {
                        canReassign = CheckTaskRoleCanReassignType.Yes.getValue();
                    }
                    UserInHouseQmCheckTask qmCheckTask = new UserInHouseQmCheckTask();
                    qmCheckTask.setSquadId(squadInfo);
                    qmCheckTask.setUserId(userIds.get(j));
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
                        log.info(CREATE_TASK_USER_FAILED);
                        throw new LjBaseRuntimeException(-99, CREATE_TASK_PEOPLE_FAIL);
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
                    log.info(CREATE_TASK_USER_FAILED);
                    throw new LjBaseRuntimeException(-99, "增加人员信息失败");
                }
            }

        }

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


        //    #
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
                item.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(taskInfo.getPlanEndOn()) + (30 * 24 * 60 * 60), "yyyy-MM-dd HH-mm-ss"));
                int one = pushStrategyCategoryOverdueService.add(item);
                if (one <= 0) {
                    log.info(PUSH_STRATEGY_CATEGORY_OVERDUE_ADD_FAIL);
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
                int nums = pushStrategyCategoryThresholdService.add(item);
                if (nums <= 0) {
                    log.info(PUSH_STRATEGY_CATEGORY_OVERDUE_ADD_FAIL);
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

    private void execute(Integer uid, TaskReq taskReq, List<Integer> areaIds, List<Integer> areaTypes, String planBeginOn, String planEndOn, List<ApiBuildingQmTaskMemberGroupVo> checkerGroups, List<ApiBuildingQmTaskMemberGroupVo> repairerGroups, ConfigVo config) {
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
        HashMap<String, Object> configMap = Maps.newHashMap();
        configMap.put(REPAIR_REFOUND_PERMISSION, taskReq.getRepairer_refund_permission());
        configMap.put(REPAIR_FOLLOWER_PERMISSION, taskReq.getRepairer_follower_permission());
        configMap.put(CHECKER_APPROVE_PERMISSION, taskReq.getChecker_approve_permission());
        configMap.put(REPAIR_PICTURE_STATUS, taskReq.getRepaired_picture_status());
        configMap.put(ISSUE_DESC_STATUS, taskReq.getIssue_desc_status());
        configMap.put(ISSUE_DEFAULT_DESC, taskReq.getIssue_default_desc());
        String configInfo = JSONObject.toJSONString(configMap);
        HouseQmCheckTask houseQmCheckTask = new HouseQmCheckTask();
        houseQmCheckTask.setProjectId(taskReq.getProject_id());
        houseQmCheckTask.setTaskId(taskObj);
        houseQmCheckTask.setName(taskReq.getName());
        houseQmCheckTask.setStatus(CheckTaskStatus.UnFinish.getValue());
        houseQmCheckTask.setCategoryCls(taskReq.getCategory_cls());
        houseQmCheckTask.setRootCategoryKey(taskReq.getRoot_category_key());
        houseQmCheckTask.setConfigInfo(configInfo);
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
                log.info(CREATE_TASK_SQUAD_FAILAD);
                throw new LjBaseRuntimeException(-99, CREATE_FAIL);
            }
            List<Integer> userIds = checkerGroups.get(i).getUser_ids();
            for (int j = 0; j < userIds.size(); j++) {

                Integer canApprove = CheckTaskRoleCanApproveType.No.getValue();
                Integer canDirectApprove = CheckTaskRoleCanDirectApproveType.No.getValue();
                Integer canReassign = CheckTaskRoleCanReassignType.No.getValue();
                if (checkerGroups.get(i).getApprove_ids().contains(userIds.get(j))) {
                    canApprove = CheckTaskRoleCanApproveType.Yes.getValue();
                }
                if (checkerGroups.get(i).getApprove_ids().contains(userIds.get(j)) && checkerGroups.get(i).getDirect_approve_ids().contains(userIds.get(j))) {
                    canDirectApprove = CheckTaskRoleCanDirectApproveType.Yes.getValue();
                }
                if ((checkerGroups.get(i).getReassign_ids().contains(userIds.get(j)))) {
                    canReassign = CheckTaskRoleCanReassignType.Yes.getValue();
                }
                UserInHouseQmCheckTask qmCheckTask = new UserInHouseQmCheckTask();
                qmCheckTask.setSquadId(squadInfo);
                qmCheckTask.setUserId(userIds.get(j));
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
                    log.info(CREATE_TASK_USER_FAILED);
                    throw new LjBaseRuntimeException(-99, CREATE_TASK_PEOPLE_FAIL);
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
                log.info(CREATE_TASK_SQUAD_FAILAD);
                throw new LjBaseRuntimeException(-99, CREATE_FAIL);
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
                    throw new LjBaseRuntimeException(-99, CREATE_TASK_PEOPLE_FAIL);
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
            int nu = pushStrategyAssignTimeService.add(pushStrategyAssignTime);
            if (nu <= 0) {
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
            pushStrategyCategoryOverdue.setScanEndOn(DateUtil.timeStampToDate(DateUtil.datetimeToTimeStamp(checktaskObj.getPlanEndOn()) + (30 * 24 * 60 * 60), YMDHMS));
            int n = pushStrategyCategoryOverdueService.add(pushStrategyCategoryOverdue);
            if (n <= 0) {
                log.info(PUSH_STRATEGY_CATEGORY_OVERDUE_ADD_FAIL);
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
            int nn = pushStrategyCategoryThresholdService.add(pushStrategyCategoryThreshold);
            if (nn <= 0) {
                log.info(PUSH_STRATEGY_CATEGORY_OVERDUE_ADD_FAIL);
            }

        }


    }

    private Integer convertCategoryCls(Integer categoryCls) {
        CategoryClsTypeEnum[] values = CategoryClsTypeEnum.values();
        for (int i = 0; i < values.length; i++) {
            if (categoryCls.equals(values[i].getValue())) {
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
        SimpleDateFormat sdf = new SimpleDateFormat(YMDHMS);
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            log.error("error:", e.getMessage());
        }
        return null;
    }

    private ConfigVo unmarshPushStrategy(String pushStrategyConfig) {
        ConfigVo configVo = new ConfigVo();
        if (StringUtils.isEmpty(pushStrategyConfig)) {
            return new ConfigVo();
        }
        Map<String, Object> pushStrategysConfig = JSON.parseObject(pushStrategyConfig, Map.class);
        Map assignTime = (Map) pushStrategysConfig.get("assign_time");
        Map categoryOverdue = (Map) pushStrategysConfig.get("category_overdue");
        Map categoryThreshold = (Map) pushStrategysConfig.get("category_threshold");
        if (assignTime != null) {
            String pushTime = (String) assignTime.get("push_time");
            String userIds = (String) assignTime.get(USER_IDS);
            if (pushTime.length() > 0 && userIds.length() > 0) {
                ConfigVo.ApiPushStrategyAssignTime configAssignTime = new ConfigVo().new ApiPushStrategyAssignTime();
                configAssignTime.setPush_time(pushTime);
                configAssignTime.setUser_ids(userIds);
                configVo.setConfig_assign_time(configAssignTime);
            }
            if (categoryOverdue != null) {
                String categoryKeys = (String) categoryOverdue.get("category_keys");
                String uIds = (String) categoryOverdue.get(USER_IDS);
                if (StringUtils.isNotBlank(categoryKeys) && StringUtils.isNotBlank(uIds)) {
                    ConfigVo.ApiPushStrategyCategoryOverdue configCategoryOverdue = new ConfigVo().new ApiPushStrategyCategoryOverdue();
                    configCategoryOverdue.setCategory_keys(categoryKeys);
                    configCategoryOverdue.setUser_ids(userIds);
                    configVo.setConfig_category_overdue(configCategoryOverdue);
                }

            }
            if (categoryThreshold != null) {
                String categoryKeys = (String) categoryThreshold.get("category_keys");
                String usIds = (String) categoryThreshold.get(USER_IDS);
                Integer threshold = (Integer) categoryThreshold.get("threshold");
                if (categoryKeys.length() > 0 && usIds.length() > 0 && threshold > 0) {
                    ConfigVo.ApiPushStrategyCategoryThreshold configCategoryThreshold = new ConfigVo().new ApiPushStrategyCategoryThreshold();
                    configCategoryThreshold.setCategory_keys(categoryKeys);
                    configCategoryThreshold.setUser_ids(usIds);
                    configCategoryThreshold.setThreshold(threshold);
                    configVo.setConfig_category_threshold(configCategoryThreshold);
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

    private List<ApiBuildingQmCheckTaskSquadObjVo> unmarshCheckerGroups(String checkerGroups) {
        List<Map<String, Object>> list = JSON.parseObject(checkerGroups, List.class);
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
                log.info(NAME_NOT_EXIST);
                throw new LjBaseRuntimeException(-99, "name not exist");
            }
            objVo.setUser_ids((String) checkergroups.get(USER_IDS));
            if (objVo.getUser_ids() == null) {
                log.info(NAME_NOT_EXIST);
                throw new LjBaseRuntimeException(-99, "user_ids not exist");
            }
            objVo.setApprove_ids((String) checkergroups.get("approve_ids"));
            if (objVo.getApprove_ids() == null) {
                log.info(NAME_NOT_EXIST);
                throw new LjBaseRuntimeException(-99, "approve_ids not exist");
            }
            objVo.setDirect_approve_ids((String) checkergroups.get("direct_approve_ids"));
            if (objVo.getDirect_approve_ids() == null) {
                log.info(NAME_NOT_EXIST);
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
                item.setRepairer_refund_permission(getValueOrDefault(configData, REPAIR_REFOUND_PERMISSION,
                        CheckTaskRepairerRefundPermission.No.getValue()));
                item.setRepairer_follower_permission(getValueOrDefault(configData, REPAIR_FOLLOWER_PERMISSION,
                        CheckTaskRepairerFollowerPermission.CompleteRepair.getValue()));
                item.setChecker_approve_permission(getValueOrDefault(configData, CHECKER_APPROVE_PERMISSION,
                        CheckerApprovePermission.No.getValue()));
                item.setRepaired_picture_status(getValueOrDefault(configData, REPAIR_PICTURE_STATUS,
                        CheckTaskRepairedPictureEnum.UnForcePicture.getValue()));
                item.setIssue_desc_status(getValueOrDefault(configData, ISSUE_DESC_STATUS,
                        CheckTaskIssueDescEnum.Arbitrary.getValue()));
                item.setIssue_default_desc(getStringValueOrDefault(configData, ISSUE_DEFAULT_DESC, QUESTION_NO_DESC));
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


    private String getStringValueOrDefault(JSONObject configData, String name, String defautValue) {

        String obj = configData.getString(name);

        if (configData.get(name) == null) {
            return defautValue;
        } else {
            return obj;
        }
    }


    @Override
    public Map<String, Object> issuestatisticexport(Integer categoryCls, String items, HttpServletResponse response) {
        Integer result = 0;
        String message = "success";
        List<NodeDataVo> dataList = Lists.newArrayList();
        HashMap<String, NodeDataVo> dataMap = Maps.newHashMap();
        JSONArray jsonArray = JSON.parseArray(items);
        List<Map> itemsList = jsonArray.toJavaList(Map.class);
        for (Map<String, Object> item : itemsList) {
            NodeDataVo nodeDataVo = new NodeDataVo();
            nodeDataVo.setKey((String) item.get("key"));
            nodeDataVo.setParent_key((String) item.get("parent_key"));
            nodeDataVo.setIssue_count((Integer) item.get("issue_count"));
            nodeDataVo.setName((String) item.get("name"));
            nodeDataVo.setValid_node(true);
            nodeDataVo.setPath_name(nodeDataVo.getKey() + "/");
            nodeDataVo.getPath_keys().add(0, nodeDataVo.getKey());
            if (StringUtils.isBlank(nodeDataVo.getKey())
//                    || StringUtils.isBlank(nodeDataVo.getParent_key())
                    || nodeDataVo.getIssue_count() == null
                    || StringUtils.isBlank(nodeDataVo.getName())) {
                continue;
            }
            dataList.add(nodeDataVo);
            dataMap.put(nodeDataVo.getKey(), nodeDataVo);
        }
        int maxCol = 0;
        for (NodeDataVo item : dataList) {
            String parentKey = item.getParent_key();
            log.info("item={}", JSON.toJSONString(item));
            while (parentKey.length() > 0) {
                item.setPath_name(String.format("%s/%s", parentKey, item.getPath_name()));
                item.getPath_keys().add(0, parentKey);
                log.info("parentKey={}", parentKey);
                if (dataMap.containsKey(parentKey)) {
                    log.info("valid");
                    parentKey = dataMap.get(parentKey).getParent_key();
                } else {
                    log.info("not valid");
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
            if (!obj.getValid_node()) {
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
                        Integer childCount = obj.getChild_count();
                        childCount += 1;
                        obj.setChild_count(childCount);
                    }
                }
            }
            dataMap.get(obj.getKey()).setChild_count(obj.getChild_count());
        }
        List<NodeVo> nodeTree = Lists.newArrayList();
        log.info("dataList={},itemsList={}", JSON.toJSONString(dataList), JSON.toJSONString(itemsList));
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
        log.info("nodeTree={},maxCol={}", JSON.toJSONString(nodeTree), maxCol);
        SXSSFWorkbook wb = ExportUtils.exportIssueStatisticExcel(nodeTree, maxCol);

        String dt = DateUtil.getNowTimeStr("yyyyMMddHHmmss");
        String categoryName = CategoryClsTypeEnum.getName(categoryCls);
        if (categoryName == null) categoryName = "工程检查";
        String fileName = String.format("%s_问题详情_%s.xlsx", categoryName, dt);

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
                item.setIssue_default_desc(QUESTION_NO_DESC);
            } else {
                JSONObject configData = JSON.parseObject(task.getConfigInfo());
                item.setRepairer_refund_permission(configData.getIntValue(REPAIR_REFOUND_PERMISSION));
                item.setRepairer_follower_permission(configData.getIntValue(REPAIR_FOLLOWER_PERMISSION));
                item.setChecker_approve_permission(configData.getIntValue(CHECKER_APPROVE_PERMISSION));
                item.setRepaired_picture_status(configData.getIntValue(REPAIR_PICTURE_STATUS));
                item.setIssue_desc_status(configData.getIntValue(ISSUE_DESC_STATUS));
                item.setIssue_default_desc(configData.getString(ISSUE_DEFAULT_DESC));
            }
            taskMap.put(task.getTaskId(), item);

        }
        return taskMap;
    }
}
