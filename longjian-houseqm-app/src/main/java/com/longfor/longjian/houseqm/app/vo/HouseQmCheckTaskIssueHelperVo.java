package com.longfor.longjian.houseqm.app.vo;

import com.alibaba.fastjson.JSON;
import com.longfor.longjian.common.consts.EventQueueEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueLogStatus;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.kafka.KafkaProducer;
import com.longfor.longjian.houseqm.app.service.PushService;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.HouseQmCheckTaskIssueDetail;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.vo.houseqmissue.*;
import com.longfor.longjian.houseqm.consts.*;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.JsonUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmCheckTaskIssueHelperVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 15:09
 */
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

    private Error error;
    private Map<String, CategoryV3> categoryMap;
    private Map<Integer, Area> areaMap;
    private Map<String, CheckItemV3> checkItemMap;
    private Map<String, UserInIssue> issueMembers;
    private Map<Integer, HouseQmCheckTask> taskMap;
    private Map<String, HouseQmCheckTaskIssueVo> oldIssueMap;
    private Map<String, HouseQmCheckTaskIssueVo> needInsertIssueMap;
    private Map<String, HouseQmCheckTaskIssueVo> needUpdateIssueMap;
    private List<HouseQmCheckTaskIssueLogVo> issueLogs;
    private Map<String, Boolean> needDeleteAtIssueLogMap;//需要在入库后就打上delete_at标签的issue_log
    private List<HouseQmCheckTaskIssueAttachment> needInsertAttachement;
    private List<RemoveAttachement> needRemoveAttachement;
    private HouseQmCheckTaskIssueLogVo currentLog;
    private int currentProjectId;
    private List<ApiHouseQmCheckTaskReportRsp> droppedIssueLog;
    private List<ApiHouseQmCheckTaskReportRsp> droppedIssue;

    //初始化属性
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

        this.error = null;

        return this;
    }

    //重新开始录入一个新issue_log
    public HouseQmCheckTaskIssueHelperVo start() {
        this.currentLog = new HouseQmCheckTaskIssueLogVo();
        return this;
    }

    //设置普通字段
    public HouseQmCheckTaskIssueHelperVo setNormalField(int taskId, String uuid, String issueUuid, int senderId, String desc, int status,
                                                        String attachmentMd5List, String audioMd5List, int clientCreateAt, String memoAudioMd5List) {

        this.currentLog.setProjectId(this.currentProjectId);
        this.currentLog.setTaskId(taskId);
        this.currentLog.setUuid(uuid);
        this.currentLog.setIssueUuid(issueUuid);
        this.currentLog.setSenderId(senderId);
        this.currentLog.setDesc(desc);
        this.currentLog.setStatus(status);
        this.currentLog.setAttachmentMd5List(attachmentMd5List);
        this.currentLog.setAudioMd5List(audioMd5List);
        this.currentLog.setMemoAudioMd5List(memoAudioMd5List);
        this.currentLog.setClientCreateAt(DateUtil.timeStampToDate(clientCreateAt, "yyyy-MM-dd hh:mm:ss"));

        return this;
    }

    //设置Detail字段
    public HouseQmCheckTaskIssueHelperVo setDetailField(int areaId, int posX, int posY,
                                                        int planEndOn, int endOn, int repairerId, String repairerFollowerIds, int condition,
                                                        int categoryCls, String checkItemKey, String categoryKey, String drawingMd5,
                                                        int issueReason, String issueReasonDetail, String issueSuggest,
                                                        String potentialRisk, String preventiveActionDetail, String removeMemoAudioMd5List, int typ) {

        HouseQmCheckTaskIssueLogDetailStruct detail = new HouseQmCheckTaskIssueLogDetailStruct();

        detail.setAreaId(areaId);
        detail.setPosX(posX);
        detail.setPosY(posY);

        detail.setPlanEndOn(planEndOn);
        detail.setEndOn(endOn);
        detail.setRepairerId(repairerId);
        detail.setRepairerFollowerIds(repairerFollowerIds);

        detail.setCondition(condition);
        detail.setCategoryCls(categoryCls);
        detail.setCheckItemKey(checkItemKey);
        detail.setCategoryKey(categoryKey);
        detail.setIssueReason(issueReason);

        detail.setIssueReasonDetail(issueReasonDetail);
        detail.setIssueSuggest(issueSuggest);
        detail.setPotentialRisk(potentialRisk);
        detail.setPreventiveActionDetail(preventiveActionDetail);
        detail.setRemoveMemoAudioMd5List(removeMemoAudioMd5List);
        detail.setTyp(typ);
        detail.setDrawingMD5(drawingMd5);

        // log.Debug(helper.currentLog.Detail)
        this.currentLog.setDetail(detail);
        return this;
    }

    public HouseQmCheckTaskIssueHelperVo done() {
        //将log加入到insert列表中
        this.issueLogs.add(this.currentLog);
        String issueUuid = this.currentLog.getIssueUuid();
        HouseQmCheckTaskIssueVo issue = new HouseQmCheckTaskIssueVo();
        boolean inNew = false;
        boolean inOld = false;
        boolean inUpdate = false;
        boolean needCreate = false;

        // 获取缓存task信息
        HouseQmCheckTask has = this.taskMap.get(this.currentLog.getTaskId());
        if (has != null) {
            HouseQmCheckTask task = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskIdUnscoped(this.currentProjectId, this.currentLog.getTaskId());
            this.taskMap.put(this.currentLog.getTaskId(), task);
        }
        HouseQmCheckTask task = this.taskMap.get(this.currentLog.getTaskId());
        if (task!=null){
            if (task.getDeleteAt() != null) {
                log.debug("HouseQm Task Has Delete ");
                String name = ApiDropDataReasonEnum.HouseQmTaskRemoved.getName();
                int value = ApiDropDataReasonEnum.HouseQmTaskRemoved.getValue();
                this.setDroppedIssue(issueUuid, value, name);
                this.setDroppedIssueLog(this.currentLog.getUuid(), value, name);
                return this;
            }
        }
        issue = this.needInsertIssueMap.get(issueUuid);
        if (issue != null) inNew = true;
        if (!inNew) {
            //如果不存在于新上报的issue列表中
            //看是否存在于需要更新的issue列表中
            issue = this.needUpdateIssueMap.get(issueUuid);
            if (issue != null) inUpdate = true;
            if (!inUpdate) {
                //如果不存在于需要更新的issue列表中
                //尝试从已读取过的issue的缓存列表中读出它
                issue = this.oldIssueMap.get(issueUuid);
                if (issue != null) inOld = true;
                if (!inOld) {
                    //不存在于缓存中
                    //尝试根据issueUuid在数据库中将issue读出来
                    try {
                        issue = getIssueFromDb(issueUuid);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.warn("get houseqmissue error:" + e.getMessage() + ", issue_uuid: " + issueUuid);
                        this.setDroppedIssue(issueUuid, ApiDropDataReasonEnum.Other.getValue(), ApiDropDataReasonEnum.Other.getName());
                        return this;
                    }

                    if (issue == null) {
                        //如果也不存在于数据库，则需要创建新的
                        needCreate = true;
                    } else if (issue.getDeleteAt() != null) {
                        this.needDeleteAtIssueLogMap.put(this.currentLog.getUuid(), true);
                        log.warn("houseqmissue had been delete, issue_uuid:" + issueUuid + ", log_uuid", this.currentLog.getUuid());
                        return this;
                    } else {
                        //存在于数据库
                        inOld = true;
                        this.oldIssueMap.put(issueUuid, issue);
                    }
                } else {
                    //else !inOld
                    //已在数据库读取了
                    //do nothing
                }
            } else {
                //else !inUpdate
                //已存在于需要更新的列表中
                //do nothing
            }
        } else {
            //else !inNoew
            //存在于需要创建的列表中
            //do noting
        }

        //需要新创建的
        if (needCreate) {
            Map<String, Object> map = initNewIssue();
            HouseQmCheckTaskIssueVo newIssue = (HouseQmCheckTaskIssueVo) map.get("houseqmissue");
            UserInIssue newIssueRole = (UserInIssue) map.get("issueRole");
            String tempIssueUid = this.currentLog.getIssueUuid();
            if (newIssueRole.isModified()) {
                this.issueMembers.put(tempIssueUid, newIssueRole);
            }
        } else if (inOld) {
            //已存在于数据库中 && 未存在于需要更新的issue列表中
            // 此为折中方案：
            // 直接使用issue中读取的数据，在高并发情况下，可能会有问题
            HouseQmCheckTaskIssueVo tempIssue = this.oldIssueMap.get(issueUuid);
            String tempIssueUid = this.currentLog.getIssueUuid();
            // 已经销项的问题不再能够修改
            if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(tempIssue.getStatus())) {
                setDroppedIssueLog(this.currentLog.getUuid(), ApiDropDataReasonEnum.Other.getValue(), "问题已销项");
            } else {
                Map<String, Object> map = modifyIssue(tempIssue);
                Boolean changed = (Boolean) map.get("changed");
                HouseQmCheckTaskIssueVo modifiedIssue = (HouseQmCheckTaskIssueVo) map.get("houseqmissue");
                UserInIssue newIssueRole = (UserInIssue) map.get("issueRole");
                if (changed) {
                    this.needUpdateIssueMap.put(issueUuid, modifiedIssue);
                }
                if (newIssueRole.isModified()) {
                    this.issueMembers.put(tempIssueUid, newIssueRole);
                }

            }
        } else if (inUpdate) {
            //已经存在于需要更新的列表中
            HouseQmCheckTaskIssueVo tempIssue = this.needUpdateIssueMap.get(issueUuid);
            // 已经销项的问题不再能够修改
            if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(tempIssue.getStatus())) {
                setDroppedIssueLog(this.currentLog.getUuid(), ApiDropDataReasonEnum.Other.getValue(), "问题已销项");
            } else {
                String tempIssueUid = this.currentLog.getIssueUuid();
                Map<String, Object> map = this.modifyIssue(tempIssue);
                Boolean changed = (Boolean) map.get("changed");
                HouseQmCheckTaskIssueVo modifiedIssue = (HouseQmCheckTaskIssueVo) map.get("houseqmissue");
                UserInIssue newIssueRole = (UserInIssue) map.get("issueRole");
                if (changed) {
                    this.needUpdateIssueMap.put(issueUuid, modifiedIssue);
                }
                if (newIssueRole.isModified()) {
                    this.issueMembers.put(tempIssueUid, newIssueRole);
                }
            }

        } else if (inNew) {
            //已经存在于需要新增的列表中
            HouseQmCheckTaskIssueVo tempIssue = this.needInsertIssueMap.get(issueUuid);
            String tempIssueUid = this.currentLog.getIssueUuid();
            Map<String, Object> map = this.modifyIssue(tempIssue);
            Boolean changed = (Boolean) map.get("changed");
            HouseQmCheckTaskIssueVo modifiedIssue = (HouseQmCheckTaskIssueVo) map.get("houseqmissue");
            UserInIssue newIssueRole = (UserInIssue) map.get("issueRole");
            if (changed) {
                this.needUpdateIssueMap.put(issueUuid, modifiedIssue);
            }
            if (newIssueRole.isModified()) {
                this.issueMembers.put(tempIssueUid, newIssueRole);
            }
        }
        return this;
    }

    //执行
    public void execute() throws Exception {
        //执行前补全各种数据
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
            issue1.setDetail(JsonUtil.GsonString(detail));
            //插入
            int affect = houseQmCheckTaskIssueService.insertOneHouseQmCheckTaskIssue(issue1);
        }
        //更新issue
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
            issue1.setDetail(JsonUtil.GsonString(detail));
            houseQmCheckTaskIssueService.update(issue1);
        }
        //houseqmissue log 入库
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
            String detailStr = JsonUtil.GsonString(detail);
            hIssueLog.setDetail(detailStr);
        }
        houseQmCheckTaskIssueLogService.addBatch(hIssueLogs);
        //处理要删除的issue log
        List<String> needDeleteIssueLog = Lists.newArrayList();
        for (String tempIssueLogUuid : this.needDeleteAtIssueLogMap.keySet()) {
            needDeleteIssueLog.add(tempIssueLogUuid);
        }
        if (needDeleteIssueLog.size() > 0) {
            houseQmCheckTaskIssueLogService.deleteIssueLogByUuids(needDeleteIssueLog);
        }

        // TODO uuid or id go源码写的todo 不太明白 ?
        // needDeleteIssueLog转换为map 用于查询
        Map<String, Boolean> needDeleteIssueLogSet = Maps.newHashMap();
        for (String uuid : needDeleteIssueLog) {
            needDeleteIssueLogSet.put(uuid, true);//得到删除的集合
        }
        Map<String, Boolean> needPushIds = Maps.newHashMap();
        for (HouseQmCheckTaskIssueLogVo issue : this.issueLogs) {
            if ((needDeleteIssueLogSet.get(issue.getIssueUuid())!=null&&!needDeleteIssueLogSet.get(issue.getIssueUuid()).equals(true))
                    && (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(issue.getStatus()) ||
                    HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId().equals(issue.getStatus()) || issue.getDetail().getRepairerId() > 0)) {
                needPushIds.put(issue.getIssueUuid(), true);//排除删除，得到需要推送的uuid集合
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
        //处理issue member
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
        if (issueUsers.size() > 0) {
            houseQmCheckTaskIssueUserService.insertBatch(issueUsers);
        }
        //处理issue attachment
        if (this.needInsertAttachement.size() > 0) {
            houseQmCheckTaskIssueAttachmentService.inseretBatch(this.needInsertAttachement);
        }
        //移除 私有 附件
        if (this.needRemoveAttachement.size() > 0) {
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

    public HouseQmCheckTaskIssueHelperVo beforeExecute() {
        //补全数据
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
        //处理附件
        //这里的处理方式不严谨，存在一个隐患
        //如果3个log中 1->添加A 2->删除A 3->又重新添加了A
        //这样子的话就会导致3中重新添加的A无效
        //但因为客户端不存在选择录音添加，所以暂时不考虑更严谨的处理方式
        this.needInsertAttachement = Lists.newArrayList();
        this.needRemoveAttachement = Lists.newArrayList();
        for (HouseQmCheckTaskIssueLogVo issueLog : this.issueLogs) {
            //公有录音
            List<String> md5List = StringSplitToListUtil.splitToStringComma(issueLog.getAudioMd5List(), ",");
            if (md5List.size() > 0) {
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
            // 私有录音
            List<String> memoMd5List = StringSplitToListUtil.splitToStringComma(issueLog.getMemoAudioMd5List(), ",");
            if (memoMd5List.size() > 0) {
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
            // 要移除的私有录音
            List<String> removeMemoAudioMd5List = StringSplitToListUtil.splitToStringComma(issueLog.getDetail().getRemoveMemoAudioMd5List(), ",");
            if (removeMemoAudioMd5List.size() > 0) {
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

    public void pushKafkaMsg() {
        HouseQmIssueReportMsg msgPkg = new HouseQmIssueReportMsg();
        msgPkg.setCreated_issues(Lists.newArrayList());
        msgPkg.setAssigned_issues(Lists.newArrayList());
        msgPkg.setReformed_issues(Lists.newArrayList());
        msgPkg.setChecked_issues(Lists.newArrayList());

        for (HouseQmCheckTaskIssueVo issue : this.needInsertIssueMap.values()) {
            // 如果某条问题不是工程的日常检查问题，说明是另外类型的推送问题，不需要处理。目前只支持工程部门的统计
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
            if (this.needDeleteAtIssueLogMap.get(log.getUuid())) {
                continue;
            }
            HouseQmCheckTaskIssueVo issue = this.needUpdateIssueMap.get(log.getIssueUuid());

            if (issue != null) {
                // 如果某条问题不是工程的日常检查问题，说明是另外类型的推送问题，不需要处理。目前只支持工程部门的统计
                if (CategoryClsTypeEnum.RCJC.getId().equals(issue.getCategoryCls())) {
                    return;
                }
                HouseQmCheckTaskIssueStatusEnum e = null;
                for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                    if (value.getId().equals(log.getStatus())) {
                        e = value;
                    }
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
        //utils.KafkaProducer.Produce(consts.EventQueue.PKG_HOUSEQM_ISSUE_REPORTED.Value, msgPkg)
        KafkaProducer producer = new KafkaProducer();
        producer.produce(EventQueueEnum.PKG_HOUSEQM_ISSUE_REPORTED.getValue(),msgPkg);

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
            switch (e) {
                case AssignNoReform: {
                    List<Integer> userIds = Lists.newArrayList();
                    userIds.add(issue.getRepairerId());
                    List<Integer> fids = StringSplitToListUtil.strToInts(issue.getRepairerFollowerIds(), ",");
                    userIds.addAll(fids);
                    houseQmCheckTaskNotifyRecordService.insertFull(issue.getProjectId(), issue.getTaskId(), issue.getLastAssigner(), StringSplitToListUtil.dataToString(userIds, ","), moduleId, issue.getId(), issue.getStatus(), "");
                    break;
                }
                case ReformNoCheck: {
                    List<Integer> approveUserIds = cache.getResolveUserList(issue.getTaskId(), issue.getSenderId());
                    houseQmCheckTaskNotifyRecordService.insertFull(issue.getProjectId(), issue.getTaskId(), issue.getLastAssigner(), StringSplitToListUtil.dataToString(approveUserIds, ","), moduleId, issue.getId(), issue.getStatus(), "");
                    break;
                }
                default:
                    break;
            }
        }
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

    //初始化所需要的area信息
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

    //初始化所需要的checkitem信息
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

    //初始化所需要的category信息
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

    public Map<String, Object> modifyIssue(HouseQmCheckTaskIssueVo issue) {

        boolean changed = false;
        //问题类型
        if (this.currentLog.getDetail().getTyp() != -1) {
            changed = true;
            issue.setTyp(this.currentLog.getDetail().getTyp());
        }
        //问题状态
        Integer oldStatus = issue.getStatus();
        if (this.currentLog.getStatus() != -1) {
            //操作状态是否为需要修改issue状态的操作
            if (!ignoreIssueLogStatus(this.currentLog.getStatus())) {
                changed = true;
                int newStatus = logToIssueStatus(this.currentLog.getStatus());
                if (newStatus > 0) {
                    issue.setStatus(newStatus);
                }
            }
            if (HouseQmCheckTaskIssueLogStatusEnum.ReformNoCheck.getId().equals(this.currentLog.getStatus())) {
                //整改完成
                issue.setEndOn(this.currentLog.getClientCreateAt());
            } else if (HouseQmCheckTaskIssueLogStatusEnum.CheckYes.getId().equals(this.currentLog.getStatus())) {
                //消项
                issue.setDeleteUser(this.currentLog.getSenderId());
                issue.setDestroyAt(this.currentLog.getClientCreateAt());
                // 销项前问题状态是待整改的，表示是直接销项，需要把EndOn补充上去
                if (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(oldStatus)) {
                    issue.setEndOn(this.currentLog.getClientCreateAt());
                }
            } else if (HouseQmCheckTaskIssueLogStatusEnum.AssignNoReform.getId().equals(this.currentLog.getStatus())) {
                //分配issue
                if (HouseQmCheckTaskIssueLogStatusEnum.AssignNoReform.getId().equals(this.currentLog.getStatus())) {
                    issue.setLastAssigner(this.currentLog.getSenderId());
                    issue.setLastAssignAt(this.currentLog.getClientCreateAt());
                }
            } else if (HouseQmCheckTaskIssueLogStatusEnum.UpdateIssueInfo.getId().equals(this.currentLog.getStatus())) {
                if (this.currentLog.getDesc().length() > 0) {
                    issue.setContent(issue.getContent() + ":" + this.currentLog.getDesc());
                }
            }

        }
        //这里不与状态一同判断
        //最后整改负责人
        if (this.currentLog.getDetail().getRepairerId() != -1) {
            changed = true;
            issue.setLastAssigner(this.currentLog.getDetail().getRepairerId());
            issue.setLastAssignAt(this.currentLog.getClientCreateAt());
        }
        //计划结束时间
        if (this.currentLog.getDetail().getPlanEndOn() != -1) {
            changed = true;
            issue.setPlanEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getPlanEndOn(), "yyyy-MM-dd hh:mm:ss"));
        }
        //结束时间
        if (this.currentLog.getDetail().getEndOn() != -1) {
            changed = true;
            issue.setEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getEndOn(), "yyyy-MM-dd hh:mm:ss"));
        }
        //严重程度
        if (this.currentLog.getDetail().getCondition() != -1) {
            changed = true;
            issue.setCondition(this.currentLog.getDetail().getCondition());
        }
        //问题类型
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
            //检查人
            userRole.put(issue.getSenderId(), HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            //整改负责人
            userRole.put(this.currentLog.getDetail().getRepairerId(), HouseQmUserInIssueRoleTypeEnum.Repairer.getId());
            //整改参与人
            List<Integer> follower = StringSplitToListUtil.strToInts(this.currentLog.getDetail().getRepairerFollowerIds(), ",");
            for (Integer uid : follower) {
                userRole.put(uid, HouseQmUserInIssueRoleTypeEnum.RepairerFollower.getId());
            }
            //写入issue中的冗余字段
            issue.setRepairerId(this.currentLog.getDetail().getRepairerId());
            if (follower.size() > 0) {
                issue.setRepairerFollowerIds("," + StringSplitToListUtil.dataToString(follower, ",") + ",");
            } else {
                issue.setRepairerFollowerIds("");
            }
            issueRole.setTaskId(issue.getTaskId());
            issueRole.setModified(true);
            changed = true;
        }
        //编辑问题的detail字段
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
        map.put("houseqmissue", issue);
        map.put("changed", changed);
        map.put("issueRole", issueRole);

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
            issue.setPlanEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getPlanEndOn(), "yyyy-MM-dd hh:mm:ss"));
        }
        if (this.currentLog.getDetail().getEndOn() != -1) {
            issue.setEndOn(DateUtil.timeStampToDate(this.currentLog.getDetail().getEndOn(), "yyyy-MM-dd hh:mm:ss"));
        }
        issue.setCategoryCls(this.currentLog.getDetail().getCategoryCls());
        //完整category路径后补
        issue.setCategoryKey(this.currentLog.getDetail().getCategoryKey());
        issue.setCheckItemKey(this.currentLog.getDetail().getCheckItemKey());

        //完整路径后补
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

        //这两项不合在一起写的考虑：有可能只调整了整改跟进人，而没有变更整改负责人
        //最后指派人
        if (HouseQmCheckTaskIssueLogStatus.AssignNoReform.getValue().equals(this.currentLog.getStatus())) {
            issue.setLastAssigner(this.currentLog.getSenderId());
            issue.setLastAssignAt(this.currentLog.getClientCreateAt());
        }
        //最后整改负责人
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
            //检查人
            userRole.put(issue.getSenderId(), HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            //整改负责人
            userRole.put(this.currentLog.getDetail().getRepairerId(), HouseQmUserInIssueRoleTypeEnum.Repairer.getId());
            //整改参与人
            List<Integer> follower = StringSplitToListUtil.strToInts(this.currentLog.getDetail().getRepairerFollowerIds(), ",");
            for (Integer uid : follower) {
                userRole.put(uid, HouseQmUserInIssueRoleTypeEnum.RepairerFollower.getId());
            }
            //写入issue中的冗余字段
            issue.setRepairerId(this.currentLog.getDetail().getRepairerId());
            if (follower.size() > 0) {
                issue.setRepairerFollowerIds("," + StringSplitToListUtil.dataToString(follower, ",") + ",");
            } else {
                issue.setRepairerFollowerIds("");
            }
            issueRole.setTaskId(this.currentLog.getTaskId());
            issueRole.setModified(true);

        }
        issueRole.setUserRole(userRole);
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("houseqmissue", issue);
        map.put("issueRole", issueRole);
        return map;
    }

    //
    public int logToIssueStatus(int logStatus) {
        HouseQmCheckTaskIssueLogStatusEnum e = null;
        for (HouseQmCheckTaskIssueLogStatusEnum value : HouseQmCheckTaskIssueLogStatusEnum.values()) {
            if (logStatus == value.getId()) e = value;
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
        }
        return 0;
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
        //{"IssueReason":0,"IssueReasonDetail":"","IssueSuggest":"","PotentialRisk":"","PreventiveActionDetail":""}
        /*HouseQmCheckTaskIssueDetail detail = new HouseQmCheckTaskIssueDetail();
        Map<String, Object> map = JsonUtil.GsonToMaps(detailstr);
        detail.setIssueReason((Integer) map.get("IssueReason"));
        detail.setIssueReasonDetail((String) map.get("IssueReasonDetail"));
        detail.setIssueSuggest((String) map.get("IssueSuggest"));
        detail.setPotentialRisk((String) map.get("PotentialRisk"));
        detail.setPreventiveActionDetail((String) map.get("PreventiveActionDetail"));*/
        HouseQmCheckTaskIssueDetail detail = JSON.parseObject(detailstr, HouseQmCheckTaskIssueDetail.class);

        taskIssueVo.setDetail(detail);


        return taskIssueVo;
    }

    //设置舍弃的uuid及原因
    public HouseQmCheckTaskIssueHelperVo setDroppedIssue(String uuid, int reasonType, String reason) {

        ApiHouseQmCheckTaskReportRsp dropped = new ApiHouseQmCheckTaskReportRsp();
        dropped.setUuid(uuid);
        dropped.setReason_type(reasonType);
        dropped.setReason(reason);
        this.droppedIssue.add(dropped);
        return this;
    }

    //设置舍弃的issue_log uuid及原因
    public HouseQmCheckTaskIssueHelperVo setDroppedIssueLog(String uuid, int reasonType, String reason) {
        ApiHouseQmCheckTaskReportRsp dropped = new ApiHouseQmCheckTaskReportRsp();
        dropped.setUuid(uuid);
        dropped.setReason_type(reasonType);
        dropped.setReason(reason);
        this.droppedIssueLog.add(dropped);
        return this;
    }


}
