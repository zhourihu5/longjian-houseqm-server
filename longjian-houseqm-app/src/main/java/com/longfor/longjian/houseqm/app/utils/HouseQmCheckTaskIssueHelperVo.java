package com.longfor.longjian.houseqm.app.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.consts.EventQueueEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueLogStatus;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.kafka.KafkaProducer;
import com.longfor.longjian.houseqm.app.service.PushService;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmCheckTaskNotifyRecordVo;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.*;
import com.longfor.longjian.houseqm.consts.*;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.JsonUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.longfor.longjian.houseqm.util.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@Data
@Slf4j
@Transactional
public class HouseQmCheckTaskIssueHelperVo {

    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;
    @Resource
    private HouseQmCheckTaskNotifyRecordService houseQmCheckTaskNotifyRecordService;
    @Resource
    private PushService pushService;
    @Resource
    private AreaService areaService;
    @Resource
    private CheckItemV3Service checkItemV3Service;
    @Resource
    private CategoryV3Service categoryV3Service;
    @Resource
    private HouseQmCheckTaskIssueUserService houseQmCheckTaskIssueUserService;
    @Resource
    private HouseQmCheckTaskIssueAttachmentService houseQmCheckTaskIssueAttachmentService;

    private static final String CHANGED="changed";
    private static final String ISSUE_ROLE="issueRole";
    private static final String YMDHMS ="yyyy-MM-dd hh:mm:ss";
    private static  final String  HOUSEQM_ISSUE="houseqmissue";

    private Map<String, CategoryV3> categoryMap;
    private Map<Integer, Area> areaMap;
    private Map<String, CheckItemV3> checkItemMap;
    private Map<String, UserInIssue> issueMembers;
    private Map<Integer, HouseQmCheckTask> taskMap;
    private Map<String, HouseQmCheckTaskIssueVo> oldIssueMap;
    private Map<String, HouseQmCheckTaskIssueVo> needInsertIssueMap;
    private Map<String, HouseQmCheckTaskIssueVo> needUpdateIssueMap;
    private List<HouseQmCheckTaskIssueLogVo> issueLogs;
    private Map<String, Boolean> needDeleteAtIssueLogMap = new HashMap<>();//???????????????????????????delete_at?????????issue_log
    private List<HouseQmCheckTaskIssueAttachment> needInsertAttachement;
    private List<RemoveAttachement> needRemoveAttachement;
    private HouseQmCheckTaskIssueLogVo currentLog;
    private int currentProjectId;
    private List<ApiHouseQmCheckTaskReportRsp> droppedIssueLog;
    private List<ApiHouseQmCheckTaskReportRsp> droppedIssue;

    //???????????????
    public HouseQmCheckTaskIssueHelperVo init(int projectId) {

        this.oldIssueMap = Maps.newHashMap();
        this.needInsertIssueMap = Maps.newHashMap();
        this.needUpdateIssueMap = Maps.newHashMap();

        this.issueLogs = Lists.newArrayList();
        this.needDeleteAtIssueLogMap = Maps.newHashMap();
        this.taskMap = Maps.newHashMap();

        this.droppedIssue = Lists.newArrayList();
        this.droppedIssueLog = Lists.newArrayList();

        this.issueMembers = Maps.newHashMap();

        this.currentProjectId = projectId;

        return this;
    }

    //???????????????????????????issue_log
    public HouseQmCheckTaskIssueHelperVo start() {
        this.currentLog = new HouseQmCheckTaskIssueLogVo();
        return this;
    }

    //??????????????????
    public HouseQmCheckTaskIssueHelperVo setNormalField(Map<String,Object>map) {

        this.currentLog.setProjectId(this.currentProjectId);
        this.currentLog.setTaskId(Integer.parseInt(String.valueOf(map.get("taskId"))));
        this.currentLog.setUuid(String.valueOf(map.get("uuid")));
        this.currentLog.setIssueUuid(String.valueOf(map.get("issueUUId")));
        this.currentLog.setSenderId(Integer.parseInt(String.valueOf(map.get("senderId"))==null?"0":String.valueOf(map.get("senderId"))));
        this.currentLog.setDesc(String.valueOf(map.get("desc")));
        this.currentLog.setStatus(Integer.parseInt(String.valueOf(map.get("status"))));
        this.currentLog.setAttachmentMd5List(String.valueOf(map.get("str")));
        this.currentLog.setAudioMd5List(String.valueOf(map.get("eStr1")));
        this.currentLog.setMemoAudioMd5List(String.valueOf(map.get("eStr2")));
        this.currentLog.setClientCreateAt(DateUtil.timeStampToDate(Integer.parseInt(String.valueOf(map.get("nowTimestamp"))), YMDHMS));

        return this;
    }

    //??????Detail??????
    public HouseQmCheckTaskIssueHelperVo setDetailField(Map<String,Object>map) {

        HouseQmCheckTaskIssueLogDetailStruct detail = new HouseQmCheckTaskIssueLogDetailStruct();

        detail.setAreaId(Integer.parseInt(String.valueOf(map.get("areaId"))==null?"0":String.valueOf(map.get("areaId"))));
        detail.setPosX(Integer.parseInt(String.valueOf(map.get("posX"))==null?"0":String.valueOf(map.get("posX"))));
        detail.setPosY(Integer.parseInt(String.valueOf(map.get("posY"))==null?"0":String.valueOf(map.get("posY"))));

        detail.setPlanEndOn(Integer.parseInt(String.valueOf(map.get("planEndOn"))==null?"0":String.valueOf(map.get("planEndOn"))));
        detail.setEndOn(Integer.parseInt(String.valueOf(map.get("endOn"))==null?"0":String.valueOf(map.get("endOn"))));
        detail.setRepairerId(Integer.parseInt(String.valueOf(map.get("repairerId"))==null?"0":String.valueOf(map.get("repairerId"))));
        detail.setRepairerFollowerIds(String.valueOf(map.get("repairerFollowerIds")));

        detail.setCondition(Integer.parseInt(String.valueOf(map.get("condition"))==null?"0":String.valueOf(map.get("condition"))));
        detail.setCategoryCls(Integer.parseInt(String.valueOf(map.get("categoryCls"))==null?"0":String.valueOf(map.get("categoryCls"))));
        detail.setCheckItemKey(String.valueOf(map.get("checkItemKey")));
        detail.setCategoryKey(String.valueOf(map.get("categoryKey")));
        detail.setIssueReason(Integer.parseInt(String.valueOf(map.get("issueReason"))==null?"0":String.valueOf(map.get("issueReason"))));
        detail.setIssueReasonDetail(String.valueOf(map.get("issueReasonDetail")));
        detail.setIssueSuggest(String.valueOf(map.get("issueSuggest")));
        detail.setPotentialRisk(String.valueOf(map.get("potentialRisk")));
        detail.setPreventiveActionDetail(String.valueOf(map.get("preventiveActionDetail")));
        detail.setRemoveMemoAudioMd5List(String.valueOf(map.get("removeMemoAudioMd5List")));
        detail.setTyp(Integer.parseInt(String.valueOf(map.get("typ"))==null?"0":String.valueOf(map.get("typ"))));
        detail.setDrawingMD5(String.valueOf(map.get("drawingMd5")));

        // log.Debug(helper.currentLog.Detail)
        this.currentLog.setDetail(detail);
        return this;
    }
    @SuppressWarnings("squid:S3776")
    public HouseQmCheckTaskIssueHelperVo done() {
        //???log?????????insert?????????
        this.issueLogs.add(this.currentLog);
        String issueUuid = this.currentLog.getIssueUuid();
        HouseQmCheckTaskIssueVo issue;
        boolean inNew = false;
        boolean inOld = false;
        boolean inUpdate = false;
        boolean needCreate = false;

        // ????????????task??????
        HouseQmCheckTask has = this.taskMap.get(this.currentLog.getTaskId());
        if (has != null) {
            HouseQmCheckTask task = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskIdUnscoped(this.currentProjectId, this.currentLog.getTaskId());
            this.taskMap.put(this.currentLog.getTaskId(), task);
        }
        HouseQmCheckTask task = this.taskMap.get(this.currentLog.getTaskId());
        if (task != null&&task.getDeleteAt() != null) {
                log.debug("HouseQm Task Has Delete ");
                String name = ApiDropDataReasonEnum.HouseQmTaskRemoved.getName();
                int value = ApiDropDataReasonEnum.HouseQmTaskRemoved.getValue();
                this.setDroppedIssue(issueUuid, value, name);
                this.setDroppedIssueLog(this.currentLog.getUuid(), value, name);
                return this;
        }
        issue = this.needInsertIssueMap.get(issueUuid);
        if (issue != null) inNew = true;
        if (!inNew) {
            //??????????????????????????????issue?????????
            //?????????????????????????????????issue?????????
            issue = this.needUpdateIssueMap.get(issueUuid);
            if (issue != null) inUpdate = true;
            if (!inUpdate) {
                //?????????????????????????????????issue?????????
                //????????????????????????issue???????????????????????????
                issue = this.oldIssueMap.get(issueUuid);
                if (issue != null) inOld = true;
                if (!inOld) {
                    //?????????????????????
                    //????????????issueUuid??????????????????issue?????????
                    try {
                        issue = getIssueFromDb(issueUuid);
                    } catch (Exception e) {
                        log.warn("get houseqmissue error:" + e.getMessage() + ", issue_uuid: " + issueUuid);
                        this.setDroppedIssue(issueUuid, ApiDropDataReasonEnum.Other.getValue(), ApiDropDataReasonEnum.Other.getName());
                        return this;
                    }

                    if (issue == null) {
                        //??????????????????????????????????????????????????????
                        needCreate = true;
                    } else if (issue.getDeleteAt() != null) {
                        this.needDeleteAtIssueLogMap.put(this.currentLog.getUuid(), true);
                        log.warn("houseqmissue had been delete, issue_uuid:" + issueUuid + ", log_uuid", this.currentLog.getUuid());
                        return this;
                    } else {
                        //??????????????????
                        inOld = true;
                        this.oldIssueMap.put(issueUuid, issue);
                    }
                } else {
                    //else !inOld
                    //????????????????????????
                    //do nothing
                }
            } else {
                //else !inUpdate
                //????????????????????????????????????
                //do nothing
            }
        } else {
            //else !inNoew
            //?????????????????????????????????
            //do noting
        }

        //??????????????????
        if (needCreate) {
            Map<String, Object> map = initNewIssue();
            UserInIssue newIssueRole = (UserInIssue) map.get(ISSUE_ROLE);
            String tempIssueUid = this.currentLog.getIssueUuid();
            if (newIssueRole.getModified()) {
                this.issueMembers.put(tempIssueUid, newIssueRole);
            }
        } else if (inOld) {
            //???????????????????????? && ???????????????????????????issue?????????
            // ?????????????????????
            // ????????????issue???????????????????????????????????????????????????????????????
            HouseQmCheckTaskIssueVo tempIssue = this.oldIssueMap.get(issueUuid);
            String tempIssueUid = this.currentLog.getIssueUuid();
            // ???????????????????????????????????????
            if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(tempIssue.getStatus())) {
                setDroppedIssueLog(this.currentLog.getUuid(), ApiDropDataReasonEnum.Other.getValue(), "???????????????");
            } else {
                Map<String, Object> map = modifyIssue(tempIssue);
                Boolean changed = (Boolean) map.get(CHANGED);
                HouseQmCheckTaskIssueVo modifiedIssue = (HouseQmCheckTaskIssueVo) map.get(HOUSEQM_ISSUE);
                UserInIssue newIssueRole = (UserInIssue) map.get(ISSUE_ROLE);
                if (changed) {
                    this.needUpdateIssueMap.put(issueUuid, modifiedIssue);
                }
                if (newIssueRole.getModified()) {
                    this.issueMembers.put(tempIssueUid, newIssueRole);
                }

            }
        } else if (inUpdate) {
            //???????????????????????????????????????
            HouseQmCheckTaskIssueVo tempIssue = this.needUpdateIssueMap.get(issueUuid);
            // ???????????????????????????????????????
            if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(tempIssue.getStatus())) {
                setDroppedIssueLog(this.currentLog.getUuid(), ApiDropDataReasonEnum.Other.getValue(), "???????????????");
            } else {
                String tempIssueUid = this.currentLog.getIssueUuid();
                Map<String, Object> map = this.modifyIssue(tempIssue);
                Boolean changed = (Boolean) map.get(CHANGED);
                HouseQmCheckTaskIssueVo modifiedIssue = (HouseQmCheckTaskIssueVo) map.get(HOUSEQM_ISSUE);
                UserInIssue newIssueRole = (UserInIssue) map.get(ISSUE_ROLE);
                if (changed) {
                    this.needUpdateIssueMap.put(issueUuid, modifiedIssue);
                }
                if (newIssueRole.getModified()) {
                    this.issueMembers.put(tempIssueUid, newIssueRole);
                }
            }

        } else if (inNew) {
            //???????????????????????????????????????
            HouseQmCheckTaskIssueVo tempIssue = this.needInsertIssueMap.get(issueUuid);
            String tempIssueUid = this.currentLog.getIssueUuid();
            Map<String, Object> map = this.modifyIssue(tempIssue);
            Boolean changed = (Boolean) map.get(CHANGED);
            HouseQmCheckTaskIssueVo modifiedIssue = (HouseQmCheckTaskIssueVo) map.get(HOUSEQM_ISSUE);
            UserInIssue newIssueRole = (UserInIssue) map.get(ISSUE_ROLE);
            if (changed) {
                this.needUpdateIssueMap.put(issueUuid, modifiedIssue);
            }
            if (newIssueRole.getModified()) {
                this.issueMembers.put(tempIssueUid, newIssueRole);
            }
        }
        return this;
    }

    //??????
    @SuppressWarnings("squid:S3776")
    public void execute() {
        //???????????????????????????
        this.beforeExecute();
        for (HouseQmCheckTaskIssueVo issue : this.needInsertIssueMap.values()) {
            HouseQmCheckTaskIssue issue1 = new HouseQmCheckTaskIssue();
            issue1.setProjectId(issue.getProjectId());
            issue1.setTaskId(issue.getTaskId());
            issue1.setUuid(issue.getUuid());
            issue1.setSenderId(issue.getSenderId());
            issue1.setPlanEndOn(issue.getPlanEndOn());
            issue1.setEndOn(issue.getEndOn());
            issue1.setAreaId(issue.getAreaId());
            issue1.setAreaPathAndId(issue.getAreaPathAndId());
            issue1.setCategoryCls(issue.getCategoryCls());
            issue1.setCategoryKey(issue.getCategoryKey());
            issue1.setCategoryPathAndKey(issue.getCategoryPathAndKey());
            issue1.setCheckItemKey(issue.getCheckItemKey());
            issue1.setCheckItemPathAndKey(issue.getCheckItemPathAndKey());
            issue1.setDrawingMD5(issue.getDrawingMD5());
            issue1.setPosX(issue.getPosX());
            issue1.setPosY(issue.getPosY());
            issue1.setTitle(issue.getTitle());
            issue1.setTyp(issue.getTyp());
            issue1.setContent(issue.getContent());
            issue1.setCondition(issue.getCondition());
            issue1.setStatus(issue.getStatus());
            issue1.setRepairerId(issue.getRepairerId());
            issue1.setRepairerFollowerIds(issue.getRepairerFollowerIds());
            issue1.setClientCreateAt(issue.getClientCreateAt());
            issue1.setLastAssigner(issue.getLastAssigner());
            issue1.setLastAssignAt(issue.getLastAssignAt());
            issue1.setLastRepairer(issue.getLastRepairer());
            issue1.setLastRepairerAt(issue.getLastRepairerAt());
            issue1.setDestroyUser(issue.getDestroyUser());
            issue1.setDestroyAt(issue.getDestroyAt());
            issue1.setDeleteUser(issue.getDeleteUser());
            issue1.setDeleteTime(issue.getDeleteTime());
            issue1.setCreateAt(issue.getCreateAt());
            issue1.setUpdateAt(issue.getUpdateAt());
            issue1.setDeleteAt(issue.getDeleteAt());
            issue1.setAttachmentMd5List(issue.getAttachmentMd5List());
            issue1.setAudioMd5List(issue.getAudioMd5List());
            HouseQmCheckTaskIssueDetail detail = issue.getDetail();
            issue1.setDetail(JsonUtil.gsonString(detail));
            //??????
            houseQmCheckTaskIssueService.insertOneHouseQmCheckTaskIssue(issue1);
        }
        //??????issue
        for (HouseQmCheckTaskIssueVo issue : this.needUpdateIssueMap.values()) {
            HouseQmCheckTaskIssue issue1 = new HouseQmCheckTaskIssue();
            issue1.setId(issue.getId());
            issue1.setProjectId(issue.getProjectId());
            issue1.setTaskId(issue.getTaskId());
            issue1.setUuid(issue.getUuid());
            issue1.setSenderId(issue.getSenderId());
            issue1.setPlanEndOn(issue.getPlanEndOn());
            issue1.setEndOn(issue.getEndOn());
            issue1.setAreaId(issue.getAreaId());
            issue1.setAreaPathAndId(issue.getAreaPathAndId());
            issue1.setCategoryCls(issue.getCategoryCls());
            issue1.setCategoryKey(issue.getCategoryKey());
            issue1.setCategoryPathAndKey(issue.getCategoryPathAndKey());
            issue1.setCheckItemKey(issue.getCheckItemKey());
            issue1.setCheckItemPathAndKey(issue.getCheckItemPathAndKey());
            issue1.setDrawingMD5(issue.getDrawingMD5());
            issue1.setPosX(issue.getPosX());
            issue1.setPosY(issue.getPosY());
            issue1.setTitle(issue.getTitle());
            issue1.setTyp(issue.getTyp());
            issue1.setContent(issue.getContent());
            issue1.setCondition(issue.getCondition());
            issue1.setStatus(issue.getStatus());
            issue1.setAttachmentMd5List(issue.getAttachmentMd5List());
            issue1.setAudioMd5List(issue.getAudioMd5List());
            issue1.setRepairerId(issue.getRepairerId());
            issue1.setRepairerFollowerIds(issue.getRepairerFollowerIds());
            issue1.setClientCreateAt(issue.getClientCreateAt());
            issue1.setLastAssigner(issue.getLastAssigner());
            issue1.setLastAssignAt(issue.getLastAssignAt());
            issue1.setLastRepairer(issue.getLastRepairer());
            issue1.setLastRepairerAt(issue.getLastRepairerAt());
            issue1.setDestroyUser(issue.getDestroyUser());
            issue1.setDestroyAt(issue.getDestroyAt());
            issue1.setDeleteUser(issue.getDeleteUser());
            issue1.setDeleteTime(issue.getDeleteTime());
            HouseQmCheckTaskIssueDetail detail = issue.getDetail();
            issue1.setDetail(JsonUtil.gsonString(detail));
            houseQmCheckTaskIssueService.update(issue1);
        }
        //houseqmissue log ??????
        List<HouseQmCheckTaskIssueLog> hIssueLogs = Lists.newArrayList();
        for (HouseQmCheckTaskIssueLogVo issueLog : this.issueLogs) {
            HouseQmCheckTaskIssueLog hIssueLog = new HouseQmCheckTaskIssueLog();
            hIssueLog.setId(issueLog.getId());
            hIssueLog.setProjectId(issueLog.getProjectId());
            hIssueLog.setTaskId(issueLog.getTaskId());
            hIssueLog.setUuid(issueLog.getUuid());
            hIssueLog.setIssueUuid(issueLog.getIssueUuid());
            hIssueLog.setSenderId(issueLog.getSenderId());
            hIssueLog.setStatus(issueLog.getStatus());
            hIssueLog.setClientCreateAt(issueLog.getClientCreateAt());
            hIssueLog.setCreateAt(issueLog.getCreateAt());
            hIssueLog.setUpdateAt(issueLog.getUpdateAt());
            hIssueLog.setDeleteAt(issueLog.getDeleteAt());
            hIssueLog.setDesc(issueLog.getDesc());
            hIssueLog.setAttachmentMd5List(issueLog.getAttachmentMd5List());
            hIssueLog.setAudioMd5List(issueLog.getAudioMd5List());
            hIssueLog.setMemoAudioMd5List(issueLog.getMemoAudioMd5List());
            HouseQmCheckTaskIssueLogDetailStruct detail = issueLog.getDetail();
            String detailStr = JsonUtil.gsonString(detail);
            hIssueLog.setDetail(detailStr);
        }
        houseQmCheckTaskIssueLogService.addBatch(hIssueLogs);
        //??????????????????issue log
        List<String> needDeleteIssueLog = Lists.newArrayList();
        for (String tempIssueLogUuid : this.needDeleteAtIssueLogMap.keySet()) {
            needDeleteIssueLog.add(tempIssueLogUuid);
        }
        if (CollectionUtils.isNotEmpty(needDeleteIssueLog)) {
            houseQmCheckTaskIssueLogService.deleteIssueLogByUuids(needDeleteIssueLog);
        }

        // needDeleteIssueLog?????????map ????????????
        Map<String, Boolean> needDeleteIssueLogSet = Maps.newHashMap();
        for (String uuid : needDeleteIssueLog) {
            needDeleteIssueLogSet.put(uuid, true);//?????????????????????
        }
        Map<String, Boolean> needPushIds = Maps.newHashMap();
        for (HouseQmCheckTaskIssueLogVo issue : this.issueLogs) {
            if ((needDeleteIssueLogSet.get(issue.getIssueUuid()) != null && !needDeleteIssueLogSet.get(issue.getIssueUuid()).equals(true))
                    && (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(issue.getStatus()) ||
                    HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId().equals(issue.getStatus()) || issue.getDetail().getRepairerId() > 0)) {
                needPushIds.put(issue.getIssueUuid(), true);//????????????????????????????????????uuid??????
            }
        }

        log.info("cnt_of_helper_issue: ", this.issueLogs);
        log.info("cnt_of_helper_NeedUpdateIssueMap: ", this.needUpdateIssueMap);
        log.info("cnt_of_helper_NeedInsertIssueMap: ", this.needInsertIssueMap);
        log.info("cnt_of_needDeleteIssueLog: ", needDeleteIssueLog);
        log.info("need_push_id: ", needPushIds);
        List<HouseQmCheckTaskIssueVo> upushIssues = Lists.newArrayList();
        for (HouseQmCheckTaskIssueVo issue : this.needUpdateIssueMap.values()) {
            if (needPushIds.containsKey(issue.getUuid())) {
                upushIssues.add(issue);
            }
        }
        for (HouseQmCheckTaskIssueVo issue : this.needInsertIssueMap.values()) {
            if (needPushIds.containsKey(issue.getUuid())) {
                upushIssues.add(issue);
            }
        }
        //??????issue member
        List<HouseQmCheckTaskIssueUser> issueUsers = Lists.newArrayList();
        for (Map.Entry<String, UserInIssue> entry : this.issueMembers.entrySet()) {
            String issueUuid = entry.getKey();
            UserInIssue userInIssue = entry.getValue();
            for (Map.Entry<Integer, Integer> userEntry : userInIssue.getUserRole().entrySet()) {
                HouseQmCheckTaskIssueUser obj = new HouseQmCheckTaskIssueUser();
                obj.setTaskId(userInIssue.getTaskId());
                obj.setIssueUuid(issueUuid);
                obj.setUserId(userEntry.getKey());
                obj.setRoleType(userEntry.getValue());
                obj.setCreateAt(new Date());
                obj.setUpdateAt(new Date());
                issueUsers.add(obj);
            }
        }
        if (CollectionUtils.isNotEmpty(issueUsers)) {
            houseQmCheckTaskIssueUserService.insertBatch(issueUsers);
        }
        //??????issue attachment
        if (CollectionUtils.isNotEmpty(this.needInsertAttachement)) {
            houseQmCheckTaskIssueAttachmentService.inseretBatch(this.needInsertAttachement);
        }
        //?????? ?????? ??????
        if (CollectionUtils.isNotEmpty(this.needRemoveAttachement)) {
            for (RemoveAttachement obj : this.needRemoveAttachement) {
                houseQmCheckTaskIssueAttachmentService.deleteByIssueUuidMd5(obj.getIssueUuid(), obj.getMd5());
            }
        }
        // PUSH
        pushService.sendUPushByIssues(upushIssues);

        this.appendNotifyRecord(upushIssues);
        // PUSH Kafka Logs
        this.pushKafkaMsg();
    }
    @SuppressWarnings("squid:S3776")
    public HouseQmCheckTaskIssueHelperVo beforeExecute() {
        //????????????
        List<Integer> existsAreaIds = Lists.newArrayList();
        List<String> existsCategoryKeys = Lists.newArrayList();
        List<String> existsCheckItemKeys = Lists.newArrayList();
        for (HouseQmCheckTaskIssueVo tempIssue : this.needInsertIssueMap.values()) {
            existsAreaIds.add(tempIssue.getAreaId());
            existsCategoryKeys.add(tempIssue.getCategoryKey());
            existsCheckItemKeys.add(tempIssue.getCheckItemKey());
        }
        this.initArea(existsAreaIds).initCheckItem(existsCheckItemKeys).initCategory(existsCategoryKeys);
        for (HouseQmCheckTaskIssueVo tempIssue : this.needInsertIssueMap.values()) {
            tempIssue.setCategoryPathAndKey(this.getCategoryPathAndKey(tempIssue.getCategoryKey()));
            tempIssue.setCheckItemPathAndKey(this.getCheckItemPathAndKey(tempIssue.getCheckItemKey()));
            tempIssue.setAreaPathAndId(this.getAreaPathAndId(tempIssue.getAreaId()));
        }
        //????????????
        //???????????????????????????????????????????????????
        //??????3???log??? 1->??????A 2->??????A 3->??????????????????A
        //???????????????????????????3??????????????????A??????
        //?????????????????????????????????????????????????????????????????????????????????????????????
        this.needInsertAttachement = Lists.newArrayList();
        this.needRemoveAttachement = Lists.newArrayList();
        for (HouseQmCheckTaskIssueLogVo issueLog : this.issueLogs) {
            //????????????
            List<String> md5List = StringUtil.strToStrs(issueLog.getAudioMd5List(), ",");
            if (CollectionUtils.isNotEmpty(md5List)) {
                for (String md5 : md5List) {
                    HouseQmCheckTaskIssueAttachment obj = new HouseQmCheckTaskIssueAttachment();
                    obj.setProjectId(this.currentProjectId);
                    obj.setTaskId(issueLog.getTaskId());
                    obj.setIssueUuid(issueLog.getIssueUuid());
                    obj.setUserId(issueLog.getSenderId());
                    obj.setPublicType(HouseQmCheckTaskIssueAttachmentPublicTypeEnum.Public.getId());
                    obj.setAttachmentType(HouseQmCheckTaskIssueAttachmentAttachmentTypeEnum.Audio.getId());
                    obj.setMd5(md5);
                    obj.setStatus(HouseQmCheckTaskIssueAttachmentStatusEnum.Enable.getId());
                    obj.setClientCreateAt(issueLog.getClientCreateAt());
                    this.needInsertAttachement.add(obj);
                }
            }
            // ????????????
            List<String> memoMd5List = StringUtil.strToStrs(issueLog.getMemoAudioMd5List(), ",");
            if (CollectionUtils.isNotEmpty(memoMd5List)) {
                for (String md5 : memoMd5List) {
                    HouseQmCheckTaskIssueAttachment obj = new HouseQmCheckTaskIssueAttachment();
                    obj.setProjectId(this.currentProjectId);
                    obj.setTaskId(issueLog.getTaskId());
                    obj.setIssueUuid(issueLog.getIssueUuid());
                    obj.setUserId(issueLog.getSenderId());
                    obj.setPublicType(HouseQmCheckTaskIssueAttachmentPublicTypeEnum.Private.getId());
                    obj.setAttachmentType(HouseQmCheckTaskIssueAttachmentAttachmentTypeEnum.Audio.getId());
                    obj.setMd5(md5);
                    obj.setStatus(HouseQmCheckTaskIssueAttachmentStatusEnum.Enable.getId());
                    obj.setClientCreateAt(issueLog.getClientCreateAt());
                    this.needInsertAttachement.add(obj);
                }
            }
            // ????????????????????????
            List<String> removeMemoAudioMd5List = StringUtil.strToStrs(issueLog.getDetail().getRemoveMemoAudioMd5List(), ",");
            if (CollectionUtils.isNotEmpty(removeMemoAudioMd5List)) {
                for (String md5 : removeMemoAudioMd5List) {
                    RemoveAttachement obj = new RemoveAttachement();
                    obj.setIssueUuid(issueLog.getIssueUuid());
                    obj.setMd5(md5);
                    this.needRemoveAttachement.add(obj);
                }
            }
        }

        return this;
    }
    @SuppressWarnings("squid:S3776")
    public void pushKafkaMsg() {
        HouseQmIssueReportMsg msgPkg = new HouseQmIssueReportMsg();
        msgPkg.setCreated_issues(Lists.newArrayList());
        msgPkg.setAssigned_issues(Lists.newArrayList());
        msgPkg.setReformed_issues(Lists.newArrayList());
        msgPkg.setChecked_issues(Lists.newArrayList());

        for (HouseQmCheckTaskIssueVo issue : this.needInsertIssueMap.values()) {
            // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
            if (!CategoryClsTypeEnum.RCJC.getId().equals(issue.getCategoryCls())) return;
            msgPkg.appendCreated(
                    issue.getUuid(), issue.getProjectId(), issue.getTaskId(), issue.getSenderId(), 0,
                    issue.getAreaId(), issue.getAreaPathAndId(), issue.getCategoryKey(),
                    issue.getCategoryPathAndKey(), issue.getSenderId(), issue.getClientCreateAt());
            if (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(issue.getStatus())) {
                msgPkg.appendAssigned(
                        issue.getUuid(), issue.getProjectId(), issue.getTaskId(), issue.getSenderId(), issue.getRepairerId(),
                        issue.getAreaId(), issue.getAreaPathAndId(), issue.getCategoryKey(),
                        issue.getCategoryPathAndKey(), issue.getSenderId(), issue.getClientCreateAt()
                );
            }
        }
        for (HouseQmCheckTaskIssueLogVo log : this.issueLogs) {
            if (log != null) {
                if (log.getUuid() != null && this.needDeleteAtIssueLogMap.get(log.getUuid()) != null && this.needDeleteAtIssueLogMap.get(log.getUuid())) {
                    continue;
                }
                HouseQmCheckTaskIssueVo issue = this.needUpdateIssueMap.get(log.getIssueUuid());

                if (issue != null) {
                    // ???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (CategoryClsTypeEnum.RCJC.getId().equals(issue.getCategoryCls())) {
                        return;
                    }
                    HouseQmCheckTaskIssueStatusEnum e = null;
                    for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                        if (value.getId().equals(log.getStatus())) {
                            e = value;
                        }
                    }
                    if (e == null) {
                        continue;
                    }
                    switch (e) {
                        case AssignNoReform:
                            msgPkg.appendAssigned(
                                    issue.getUuid(), issue.getProjectId(), issue.getTaskId(), issue.getSenderId(), log.getDetail().getRepairerId(),
                                    issue.getAreaId(), issue.getAreaPathAndId(), issue.getCategoryKey(),
                                    issue.getCategoryPathAndKey(), log.getSenderId(), log.getClientCreateAt()
                            );
                            break;
                        case ReformNoCheck:
                            msgPkg.appendReformed(
                                    issue.getUuid(), issue.getProjectId(), issue.getTaskId(), issue.getSenderId(), log.getDetail().getRepairerId(),
                                    issue.getAreaId(), issue.getAreaPathAndId(), issue.getCategoryKey(),
                                    issue.getCategoryPathAndKey(), log.getSenderId(), log.getClientCreateAt()
                            );
                            break;
                        case CheckYes:
                            msgPkg.appendChecked(
                                    issue.getUuid(), issue.getProjectId(), issue.getTaskId(), issue.getSenderId(), issue.getRepairerId(),
                                    issue.getAreaId(), issue.getAreaPathAndId(), issue.getCategoryKey(),
                                    issue.getCategoryPathAndKey(), log.getSenderId(), log.getClientCreateAt()
                            );
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        //utils.KafkaProducer.Produce(consts.EventQueue.PKG_HOUSEQM_ISSUE_REPORTED.Value, msgPkg)
        KafkaProducer producer = new KafkaProducer();
        producer.produce(EventQueueEnum.PKG_HOUSEQM_ISSUE_REPORTED.getValue(), msgPkg);

    }

    public void appendNotifyRecord(List<HouseQmCheckTaskIssueVo> issueList) {
        int moduleId = 1001;
        // cache
        HouseqmSquadUserCache cache = new HouseqmSquadUserCache();
        cache.setCache(Maps.newHashMap());


        for (HouseQmCheckTaskIssueVo issue : issueList) {
            if (CategoryClsTypeEnum.RCJC.getId().equals(issue.getCategoryCls())) {
                continue;
            }
            HouseQmCheckTaskIssueStatusEnum e = null;
            for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                if (value.getId().equals(issue.getStatus())) {
                    e = value;
                }
            }
            if (e == null) {
                continue;
            }
            HouseqmCheckTaskNotifyRecordVo hcvo=new HouseqmCheckTaskNotifyRecordVo();
            hcvo.setProjectId(issue.getProjectId());
            hcvo.setTaskId(issue.getTaskId());
            hcvo.setSrcUserId(issue.getLastAssigner());
            hcvo.setIssueStatus(issue.getStatus());
            hcvo.setModuleId(moduleId);
            hcvo.setIssueId(issue.getId());
            hcvo.setExtraInfo("");
            switch (e) {
                case AssignNoReform:
                    getAssignNoReform(issue, hcvo);
                    break;
                case ReformNoCheck:
                    getReformNoCheck(cache, issue, hcvo);
                    break;
                default:
                    break;
            }
        }
    }

    private void getAssignNoReform(HouseQmCheckTaskIssueVo issue, HouseqmCheckTaskNotifyRecordVo hcvo) {
        List<Integer> userIds = Lists.newArrayList();
        userIds.add(issue.getRepairerId());
        List<Integer> fids = StringUtil.strToInts(issue.getRepairerFollowerIds(), ",");
        userIds.addAll(fids);
        hcvo.setDesUserIds(StringSplitToListUtil.dataToString(userIds, ","));
        houseQmCheckTaskNotifyRecordService.insertFull(hcvo);
    }

    private void getReformNoCheck(HouseqmSquadUserCache cache, HouseQmCheckTaskIssueVo issue, HouseqmCheckTaskNotifyRecordVo hcvo) {
        List<Integer> approveUserIds = cache.getResolveUserList(issue.getTaskId(), issue.getSenderId());
        hcvo.setDesUserIds(StringSplitToListUtil.dataToString(approveUserIds, ","));
        houseQmCheckTaskNotifyRecordService.insertFull(hcvo);
    }


    public String getCategoryPathAndKey(String categoryKey) {
        CategoryV3 categoryV3 = this.categoryMap.get(categoryKey);
        if (categoryV3 == null) return "";
        return categoryV3.getPath() + categoryV3.getKey() + "/";
    }

    public String getCheckItemPathAndKey(String checkItemKey) {
        CheckItemV3 checkItemV3 = this.checkItemMap.get(checkItemKey);
        if (checkItemV3 == null) return "";
        return checkItemV3.getPath() + checkItemV3.getKey() + "/";
    }

    public String getAreaPathAndId(int areaId) {
        Area area = this.areaMap.get(areaId);
        if (area == null) return "";
        return area.getPath() + area.getId() + "/";
    }

    //?????????????????????area??????
    public HouseQmCheckTaskIssueHelperVo initArea(List<Integer> areaIds) {
        this.areaMap = Maps.newHashMap();
        try {
            List<Area> areas = areaService.selectByAreaIds(areaIds);
            for (Area area : areas) {
                this.areaMap.put(area.getId(), area);
            }
        } catch (Exception e) {
            log.warn("helper init area error: " + e.getMessage());
            return this;
        }

        return this;
    }

    //?????????????????????checkitem??????
    public HouseQmCheckTaskIssueHelperVo initCheckItem(List<String> checkitemKeys) {
        List<CheckItemV3> checkItemV3s = null;
        try {
            checkItemV3s = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkitemKeys);
        } catch (Exception e) {
            log.warn("helper init checkitem error: " + e.getMessage());
            return this;
        }
        this.checkItemMap = checkItemV3s.stream().collect(Collectors.toMap(CheckItemV3::getKey, checkItemV3 -> checkItemV3));
        return this;
    }

    //?????????????????????category??????
    public HouseQmCheckTaskIssueHelperVo initCategory(List<String> categoryKeys) {
        List<CategoryV3> categoryV3s = null;
        try {
            categoryV3s = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeys);
        } catch (Exception e) {
            log.warn("helper init category error: " + e.getMessage());
            return this;
        }
        this.categoryMap = categoryV3s.stream().collect(Collectors.toMap(CategoryV3::getKey, categoryV3 -> categoryV3));
        return this;
    }
    @SuppressWarnings("squid:S3776")
    public Map<String, Object> modifyIssue(HouseQmCheckTaskIssueVo issue) {

        boolean changed = false;
        //????????????
        if (this.currentLog.getDetail().getTyp() != -1) {
            changed = true;
            issue.setTyp(this.currentLog.getDetail().getTyp());
        }
        //????????????
        Integer oldStatus = issue.getStatus();
        if (this.currentLog.getStatus() != -1) {
            //?????????????????????????????????issue???????????????
            if (!ignoreIssueLogStatus(this.currentLog.getStatus())) {
                changed = true;
                int newStatus = logToIssueStatus(this.currentLog.getStatus());
                if (newStatus > 0) {
                    issue.setStatus(newStatus);
                }
            }
            if (HouseQmCheckTaskIssueLogStatusEnum.ReformNoCheck.getId().equals(this.currentLog.getStatus())) {
                //????????????
                issue.setEndOn(this.currentLog.getClientCreateAt());
            } else if (HouseQmCheckTaskIssueLogStatusEnum.CheckYes.getId().equals(this.currentLog.getStatus())) {
                //??????
                issue.setDeleteUser(this.currentLog.getSenderId());
                issue.setDestroyAt(this.currentLog.getClientCreateAt());
                // ????????????????????????????????????????????????????????????????????????EndOn????????????
                if (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(oldStatus)) {
                    issue.setEndOn(this.currentLog.getClientCreateAt());
                }
            } else if (HouseQmCheckTaskIssueLogStatusEnum.AssignNoReform.getId().equals(this.currentLog.getStatus())) {
                //??????issue
                if (HouseQmCheckTaskIssueLogStatusEnum.AssignNoReform.getId().equals(this.currentLog.getStatus())) {
                    issue.setLastAssigner(this.currentLog.getSenderId());
                    issue.setLastAssignAt(this.currentLog.getClientCreateAt());
                }
            } else if (HouseQmCheckTaskIssueLogStatusEnum.UpdateIssueInfo.getId().equals(this.currentLog.getStatus())&&this.currentLog.getDesc().length() > 0) {
                    issue.setContent(issue.getContent() + ":" + this.currentLog.getDesc());
            }

        }
        //??????????????????????????????
        //?????????????????????
        if (this.currentLog.getDetail().getRepairerId() != -1) {
            changed = true;
            issue.setLastAssigner(this.currentLog.getDetail().getRepairerId());
            issue.setLastAssignAt(this.currentLog.getClientCreateAt());
        }
        //??????????????????
        if (this.currentLog.getDetail().getPlanEndOn() != -1) {
            changed = true;
            issue.setPlanEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getPlanEndOn(), YMDHMS));
        }
        //????????????
        if (this.currentLog.getDetail().getEndOn() != -1) {
            changed = true;
            issue.setEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getEndOn(), YMDHMS));
        }
        //????????????
        if (this.currentLog.getDetail().getCondition() != -1) {
            changed = true;
            issue.setCondition(this.currentLog.getDetail().getCondition());
        }
        //????????????
        boolean ok = false;
        for (HouseQmCheckTaskIssueTypeEnum value : HouseQmCheckTaskIssueTypeEnum.values()) {
            if (value.getId().equals(this.currentLog.getDetail().getTyp())) {
                ok = true;
            }
        }
        if (this.currentLog.getDetail().getTyp() != -1 && ok) {
            changed = true;
            issue.setTyp(this.currentLog.getDetail().getTyp());
        }
        UserInIssue issueRole = new UserInIssue();
        HashMap<Integer, Integer> userRole = Maps.newHashMap();
        issueRole.setUserRole(userRole);
        issueRole.setModified(false);
        if (-1 != this.currentLog.getDetail().getRepairerId() && !this.currentLog.getDetail().getRepairerFollowerIds().equals("-1")) {
            //?????????
            userRole.put(issue.getSenderId(), HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            //???????????????
            userRole.put(this.currentLog.getDetail().getRepairerId(), HouseQmUserInIssueRoleTypeEnum.Repairer.getId());
            //???????????????
            List<Integer> follower = StringUtil.strToInts(this.currentLog.getDetail().getRepairerFollowerIds(), ",");
            for (Integer uid : follower) {
                userRole.put(uid, HouseQmUserInIssueRoleTypeEnum.RepairerFollower.getId());
            }
            //??????issue??????????????????
            issue.setRepairerId(this.currentLog.getDetail().getRepairerId());
            if (CollectionUtils.isNotEmpty(follower)) {
                issue.setRepairerFollowerIds("," + StringSplitToListUtil.dataToString(follower, ",") + ",");
            } else {
                issue.setRepairerFollowerIds("");
            }
            issueRole.setTaskId(issue.getTaskId());
            issueRole.setModified(true);
            changed = true;
        }
        //???????????????detail??????
        HouseQmCheckTaskIssueDetail detail = new HouseQmCheckTaskIssueDetail();
        if (this.currentLog.getDetail().getIssueReason() != -1) {
            changed = true;
            detail.setIssueReason(this.currentLog.getDetail().getIssueReason());
        }
        if (!"-1".equals(this.currentLog.getDetail().getIssueReasonDetail())) {
            changed = true;
            detail.setIssueReasonDetail(this.currentLog.getDetail().getIssueReasonDetail());
        }
        if (!"-1".equals(this.currentLog.getDetail().getIssueSuggest())) {
            changed = true;
            detail.setIssueSuggest(this.currentLog.getDetail().getIssueSuggest());
        }
        if (!"-1".equals(this.currentLog.getDetail().getPotentialRisk())) {
            changed = true;
            detail.setPotentialRisk(this.currentLog.getDetail().getPotentialRisk());
        }
        if (!"-1".equals(this.currentLog.getDetail().getPreventiveActionDetail())) {
            changed = true;
            detail.setPreventiveActionDetail(this.currentLog.getDetail().getPreventiveActionDetail());
        }
        issue.setDetail(detail);


        HashMap<String, Object> map = Maps.newHashMap();
        map.put(HOUSEQM_ISSUE, issue);
        map.put(CHANGED, changed);
        map.put(ISSUE_ROLE, issueRole);

        return map;
    }

    public boolean ignoreIssueLogStatus(int logStatus) {
        HashMap<Integer, Boolean> ignoreStatus = Maps.newHashMap();
        ignoreStatus.put(HouseQmCheckTaskIssueLogStatusEnum.Repairing.getId(), true);
        ignoreStatus.put(HouseQmCheckTaskIssueLogStatusEnum.EditBaseInfo.getId(), true);
        return ignoreStatus.get(logStatus);
    }

    public Map<String, Object> initNewIssue() {
        HouseQmCheckTaskIssueVo issue = new HouseQmCheckTaskIssueVo();

        issue.setProjectId(this.currentProjectId);
        issue.setTaskId(this.currentLog.getTaskId());
        issue.setUuid(this.currentLog.getUuid());
        issue.setSenderId(this.currentLog.getSenderId());
        issue.setPlanEndOn(null);
        if (this.currentLog.getDetail().getPlanEndOn() != -1) {
            issue.setPlanEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getPlanEndOn(), YMDHMS));
        }
        if (this.currentLog.getDetail().getEndOn() != -1) {
            issue.setEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getEndOn(), YMDHMS));
        }
        issue.setCategoryCls(this.currentLog.getDetail().getCategoryCls());
        //??????category????????????
        issue.setCategoryKey(this.currentLog.getDetail().getCategoryKey());
        issue.setCheckItemKey(this.currentLog.getDetail().getCheckItemKey());

        //??????????????????
        issue.setAreaId(this.currentLog.getDetail().getAreaId());
        issue.setDrawingMD5(this.currentLog.getDetail().getDrawingMD5());
        if (this.currentLog.getDetail().getPosX() > 0) {
            issue.setPosX(this.currentLog.getDetail().getPosX());
        }
        if (this.currentLog.getDetail().getPosY() > 0) {
            issue.setPosY(this.currentLog.getDetail().getPosY());
        }
        issue.setTitle(this.currentLog.getDetail().getTitle());
        issue.setTyp(this.currentLog.getDetail().getTyp());
        issue.setContent(this.currentLog.getDesc());
        issue.setCondition(this.currentLog.getDetail().getCondition());
        issue.setStatus(logToIssueStatus(this.currentLog.getStatus()));
        issue.setAttachmentMd5List(this.currentLog.getAttachmentMd5List());
        issue.setAudioMd5List(this.currentLog.getAudioMd5List());
        issue.setClientCreateAt(this.currentLog.getClientCreateAt());

        //????????????????????????????????????????????????????????????????????????????????????????????????????????????
        //???????????????
        if (HouseQmCheckTaskIssueLogStatus.AssignNoReform.getValue().equals(this.currentLog.getStatus())) {
            issue.setLastAssigner(this.currentLog.getSenderId());
            issue.setLastAssignAt(this.currentLog.getClientCreateAt());
        }
        //?????????????????????
        if (this.currentLog.getDetail().getRepairerId() != -1) {
            issue.setLastAssigner(this.currentLog.getDetail().getRepairerId());
            issue.setLastAssignAt(this.currentLog.getClientCreateAt());
        }

        issue.setDestroyUser(0);
        issue.setDestroyAt(null);
        issue.setDeleteUser(0);
        issue.setDeleteTime(null);

        HouseQmCheckTaskIssueDetail detail = new HouseQmCheckTaskIssueDetail();
        detail.setIssueReason(this.currentLog.getDetail().getIssueReason());
        detail.setIssueReasonDetail(this.currentLog.getDetail().getIssueReasonDetail());
        detail.setIssueSuggest(this.currentLog.getDetail().getIssueSuggest());
        detail.setPotentialRisk(this.currentLog.getDetail().getPotentialRisk());
        detail.setPreventiveActionDetail(this.currentLog.getDetail().getPreventiveActionDetail());

        issue.setDetail(detail);
        UserInIssue issueRole = new UserInIssue();
        HashMap<Integer, Integer> userRole = Maps.newHashMap();
        issueRole.setModified(false);
        if (-1 != this.currentLog.getDetail().getRepairerId() && !this.currentLog.getDetail().getRepairerFollowerIds().equals("-1")) {
            //?????????
            userRole.put(issue.getSenderId(), HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            //???????????????
            userRole.put(this.currentLog.getDetail().getRepairerId(), HouseQmUserInIssueRoleTypeEnum.Repairer.getId());
            //???????????????
            List<Integer> follower = StringUtil.strToInts(this.currentLog.getDetail().getRepairerFollowerIds(), ",");
            for (Integer uid : follower) {
                userRole.put(uid, HouseQmUserInIssueRoleTypeEnum.RepairerFollower.getId());
            }
            //??????issue??????????????????
            issue.setRepairerId(this.currentLog.getDetail().getRepairerId());
            if (CollectionUtils.isNotEmpty(follower)) {
                issue.setRepairerFollowerIds("," + StringSplitToListUtil.dataToString(follower, ",") + ",");
            } else {
                issue.setRepairerFollowerIds("");
            }
            issueRole.setTaskId(this.currentLog.getTaskId());
            issueRole.setModified(true);

        }
        issueRole.setUserRole(userRole);
        HashMap<String, Object> map = Maps.newHashMap();
        map.put(HOUSEQM_ISSUE, issue);
        map.put(ISSUE_ROLE, issueRole);
        return map;
    }

    //
    public int logToIssueStatus(int logStatus) {
        HouseQmCheckTaskIssueLogStatusEnum e = null;
        for (HouseQmCheckTaskIssueLogStatusEnum value : HouseQmCheckTaskIssueLogStatusEnum.values()) {
            if (logStatus == value.getId()) e = value;
        }
        if (e == null) {
            return -1;
        }
        switch (e) {
            case NoProblem:
                return HouseQmCheckTaskIssueStatusEnum.NoProblem.getId();
            case ReformNoCheck:
                return HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId();
            case AssignNoReform:
            case Repairing:
                return HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId();
            case NoteNoAssign:
                return HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId();
            case CheckYes:
                return HouseQmCheckTaskIssueStatusEnum.CheckYes.getId();
            case Cancel:
                return HouseQmCheckTaskIssueStatusEnum.Cancel.getId();
                default:
                      return 0;
        }
    }

    //
    public HouseQmCheckTaskIssueVo getIssueFromDb(String uuid) {
        HouseQmCheckTaskIssue issue = houseQmCheckTaskIssueService.getByUuidUnscoped(uuid);
        HouseQmCheckTaskIssueVo taskIssueVo = new HouseQmCheckTaskIssueVo();
        taskIssueVo.setId(issue.getId());
        taskIssueVo.setProjectId(issue.getProjectId());
        taskIssueVo.setTaskId(issue.getTaskId());
        taskIssueVo.setUuid(issue.getUuid());
        taskIssueVo.setSenderId(issue.getSenderId());
        taskIssueVo.setPlanEndOn(issue.getPlanEndOn());
        taskIssueVo.setEndOn(issue.getEndOn());
        taskIssueVo.setAreaId(issue.getAreaId());
        taskIssueVo.setAreaPathAndId(issue.getAreaPathAndId());
        taskIssueVo.setCategoryCls(issue.getCategoryCls());
        taskIssueVo.setCategoryKey(issue.getCategoryKey());
        taskIssueVo.setCategoryPathAndKey(issue.getCategoryPathAndKey());
        taskIssueVo.setCheckItemKey(issue.getCheckItemKey());
        taskIssueVo.setCheckItemPathAndKey(issue.getCheckItemPathAndKey());
        taskIssueVo.setDrawingMD5(issue.getDrawingMD5());
        taskIssueVo.setPosX(issue.getPosX());
        taskIssueVo.setPosY(issue.getPosY());
        taskIssueVo.setTitle(issue.getTitle());
        taskIssueVo.setTyp(issue.getTyp());
        taskIssueVo.setContent(issue.getContent());
        taskIssueVo.setCondition(issue.getCondition());
        taskIssueVo.setStatus(issue.getStatus());
        taskIssueVo.setRepairerId(issue.getRepairerId());
        taskIssueVo.setRepairerFollowerIds(issue.getRepairerFollowerIds());
        taskIssueVo.setClientCreateAt(issue.getClientCreateAt());
        taskIssueVo.setLastAssigner(issue.getLastAssigner());
        taskIssueVo.setLastAssignAt(issue.getLastAssignAt());
        taskIssueVo.setLastRepairer(issue.getLastRepairer());
        taskIssueVo.setLastRepairerAt(issue.getLastRepairerAt());
        taskIssueVo.setDestroyUser(issue.getDestroyUser());
        taskIssueVo.setDestroyAt(issue.getDestroyAt());
        taskIssueVo.setDeleteUser(issue.getDeleteUser());
        taskIssueVo.setDeleteTime(issue.getDeleteTime());
        taskIssueVo.setCreateAt(issue.getCreateAt());
        taskIssueVo.setUpdateAt(issue.getUpdateAt());
        taskIssueVo.setDeleteAt(issue.getDeleteAt());
        taskIssueVo.setAttachmentMd5List(issue.getAttachmentMd5List());
        taskIssueVo.setAudioMd5List(issue.getAudioMd5List());
        String detailstr = issue.getDetail();
        HouseQmCheckTaskIssueDetail detail = JSON.parseObject(detailstr, HouseQmCheckTaskIssueDetail.class);
        taskIssueVo.setDetail(detail);

        return taskIssueVo;
    }

    //???????????????uuid?????????
    public HouseQmCheckTaskIssueHelperVo setDroppedIssue(String uuid, int reasonType, String reason) {

        ApiHouseQmCheckTaskReportRsp dropped = new ApiHouseQmCheckTaskReportRsp();
        dropped.setUuid(uuid);
        dropped.setReason_type(reasonType);
        dropped.setReason(reason);
        this.droppedIssue.add(dropped);
        return this;
    }

    //???????????????issue_log uuid?????????
    public HouseQmCheckTaskIssueHelperVo setDroppedIssueLog(String uuid, int reasonType, String reason) {
        ApiHouseQmCheckTaskReportRsp dropped = new ApiHouseQmCheckTaskReportRsp();
        dropped.setUuid(uuid);
        dropped.setReason_type(reasonType);
        dropped.setReason(reason);
        this.droppedIssueLog.add(dropped);
        return this;
    }


}
