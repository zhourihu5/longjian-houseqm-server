package com.longfor.longjian.houseqm.app.vo;

import java.util.Date;

import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueLogStatus;
import com.longfor.longjian.houseqm.app.vo.issue.HouseQmCheckTaskIssueDetail;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.vo.issue.*;
import com.longfor.longjian.houseqm.consts.ApiDropDataReasonEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueLogStatusEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.consts.HouseQmUserInIssueRoleTypeEnum;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.CheckItemV3Service;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.JsonUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.sun.org.apache.regexp.internal.RE;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmCheckTaskIssueHelperVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 15:09
 */
@Data
@NoArgsConstructor
@Slf4j
public class HouseQmCheckTaskIssueHelperVo implements Serializable {

    //Error error
    private Error error;

    //	houseQmCheckTaskSvc isvc.HouseqmSvc
    //	areaSvc             isvc.AreaSvc
    //	checkItemSvc        isvc.CheckItemSvc
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private AreaService areaService;
    @Resource
    private CheckItemV3Service checkItemV3Service;

    private Map<String, CategoryV3> categoryMap;
    private Map<Integer, Area> areaMap;
    private Map<String, CheckItemV3> checkItemMap;

    private Map<String, UserInIssue> issueMembers;
    private Map<Integer, HouseQmCheckTask> taskMap;

    private Map<String, HouseQmCheckTaskIssueVo> oldIssueMap;
    private Map<String, HouseQmCheckTaskIssueVo> needInsertIssueMap;
    private Map<String, HouseQmCheckTaskIssueVo> needUpdateIssueMap;
    // NeedDeleteIssueMap map[string]*m.HouseQmCheckTaskIssue

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

        //	helper.houseQmCheckTaskSvc = loader.SvcHouseqm()
        //	helper.areaSvc = loader.SvcArea()
        //	helper.checkItemSvc = loader.SvcCheckItem()

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

        // helper.currentLog.Detail = m.HouseQmCheckTaskIssueLogDetailStruct { }
        this.currentLog.setDetail(new HouseQmCheckTaskIssueLogDetailStruct());
        this.currentLog.getDetail().setAreaId(areaId);
        this.currentLog.getDetail().setPosX(posX);
        this.currentLog.getDetail().setPosY(posY);

        this.currentLog.getDetail().setPlanEndOn(planEndOn);
        this.currentLog.getDetail().setEndOn(endOn);
        this.currentLog.getDetail().setRepairerId(repairerId);
        this.currentLog.getDetail().setRepairerFollowerIds(repairerFollowerIds);

        this.currentLog.getDetail().setCondition(condition);
        this.currentLog.getDetail().setCategoryCls(categoryCls);
        this.currentLog.getDetail().setCheckItemKey(checkItemKey);
        this.currentLog.getDetail().setCategoryKey(categoryKey);
        this.currentLog.getDetail().setIssueReason(issueReason);

        this.currentLog.getDetail().setIssueReasonDetail(issueReasonDetail);
        this.currentLog.getDetail().setIssueSuggest(issueSuggest);
        this.currentLog.getDetail().setPotentialRisk(potentialRisk);
        this.currentLog.getDetail().setPreventiveActionDetail(preventiveActionDetail);
        this.currentLog.getDetail().setRemoveMemoAudioMd5List(removeMemoAudioMd5List);
        this.currentLog.getDetail().setTyp(typ);
        this.currentLog.getDetail().setDrawingMD5(drawingMd5);

        // log.Debug(helper.currentLog.Detail)
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
        if (task.getDeleteAt() != null) {
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
                        log.warn("get issue error:" + e.getMessage() + ", issue_uuid: " + issueUuid);
                        this.setDroppedIssue(issueUuid, ApiDropDataReasonEnum.Other.getValue(), ApiDropDataReasonEnum.Other.getName());
                        return this;
                    }

                    if (issue == null) {
                        //如果也不存在于数据库，则需要创建新的
                        needCreate = true;
                    } else if (issue.getDeleteAt() != null) {
                        this.needDeleteAtIssueLogMap.put(this.currentLog.getUuid(), true);
                        log.warn("issue had been delete, issue_uuid:" + issueUuid + ", log_uuid", this.currentLog.getUuid());
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
            HouseQmCheckTaskIssueVo newIssue = (HouseQmCheckTaskIssueVo) map.get("issue");
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
                // todo modifyIssue(tempIssue);
            }
        } else if (inUpdate) {
            //已经存在于需要更新的列表中
            HouseQmCheckTaskIssueVo tempIssue = this.needUpdateIssueMap.get(issueUuid);
            // 已经销项的问题不再能够修改
            if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(tempIssue.getStatus())) {
                setDroppedIssueLog(this.currentLog.getUuid(), ApiDropDataReasonEnum.Other.getValue(), "问题已销项");
            } else {
                String tempIssueUid = this.currentLog.getIssueUuid();
                //todo modifyIssue(tempIssue);
            }

        } else if (inNew) {
            //已经存在于需要新增的列表中
            HouseQmCheckTaskIssueVo tempIssue = this.needInsertIssueMap.get(issueUuid);
            String tempIssueUid = this.currentLog.getIssueUuid();
            //todo modifyIssue(tempIssue);
        }
        return this;
    }

    //执行
    public void execute() {

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
            if (!ignoreIssueLogStatus(this.currentLog.getStatus())){
                changed=true;
                int newStatus = logToIssueStatus(this.currentLog.getStatus());
                if (newStatus>0){
                    issue.setStatus(newStatus);
                }
            }
            //todo 2019/1/10 ing

        }
        //这里不与状态一同判断
        //最后整改负责人

        HashMap<String, Object> map = Maps.newHashMap();

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
        map.put("issue", issue);
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
        HouseQmCheckTaskIssueDetail detail = new HouseQmCheckTaskIssueDetail();
        String detailstr = issue.getDetail();
        //{"IssueReason":0,"IssueReasonDetail":"","IssueSuggest":"","PotentialRisk":"","PreventiveActionDetail":""}
        Map<String, Object> map = JsonUtil.GsonToMaps(detailstr);

        detail.setIssueReason((Integer) map.get("IssueReason"));
        detail.setIssueReasonDetail((String) map.get("IssueReasonDetail"));
        detail.setIssueSuggest((String) map.get("IssueSuggest"));
        detail.setPotentialRisk((String) map.get("PotentialRisk"));
        detail.setPreventiveActionDetail((String) map.get("PreventiveActionDetail"));


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
