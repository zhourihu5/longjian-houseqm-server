package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.consts.*;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.kafka.KafkaProducer;
import com.longfor.longjian.houseqm.app.service.ScanMsgPushService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.consts.DropDataReasonEnum;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import com.longfor.longjian.houseqm.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
@Service
@Slf4j
public class ReportIssueService {

    @Resource
    UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;
    @Resource
    HouseQmCheckTaskIssueUserService houseQmCheckTaskIssueUserService;
    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;
    @Resource
    HouseQmCheckTaskIssueAttachmentService houseQmCheckTaskIssueAttachmentService;
    @Resource
    AreaService areaService;
    @Resource
    CategoryV3Service categoryV3Service;
    @Resource
    CheckItemV3Service checkItemV3Service;
    @Resource
    HouseQmCheckTaskNotifyRecordService houseQmCheckTaskNotifyRecordService;
    @Resource
    ScanMsgPushService scanMsgPushService;
    @Resource
    private KafkaProducer kafkaProducer;

    private static final String ISSUEREASON="IssueReason";
    private static final String ISSUE_REASOND_ETAIL="IssueReasonDetail";
    private static final String ISSUE_SUGGEST="IssueSuggest";
    private static final String POTENTIAL_RISK="PotentialRisk";
    private static final String PREVENTIVE_ACTION_DETAIL="PreventiveActionDetail";
    private static final String YMDHMS="yyyy-MM-dd HH:mm:ss";
    private static final String PLAN_END_ON="planEndOn";
    private static final String CHECK_ITEM_MD5="CheckItemMD5";
    private static final String ISSUE="issue";
    private static final String  REFUNDMAP="refundMap";
    private static final String STATUS  ="status";
    private static final String   START_VALUE="0001-01-01 00:00:00";

    @Transactional
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
                HouseQmCheckTaskIssue issue =  issueInsertMap.get(item.getIssue_uuid());
                // # 已经销项的问题不再能够修改
                if (issue.getStatus().equals(CheckTaskIssueStatus.CheckYes.getValue())) {
                    ReportIssueVo.ApiHouseQmCheckTaskReportMsg msg = new ReportIssueVo().new ApiHouseQmCheckTaskReportMsg();
                    msg.setUuid(item.getUuid());
                    msg.setReason_type(DropDataReasonEnum.Other.getId());
                    msg.setReason("问题已销项");
                    dropped.add(msg);
                } else {
                    Map detail = JSON.parseObject(issue.getDetail(), Map.class);
                    String checkItemMD5 = (String) detail.get(CHECK_ITEM_MD5)!=null?(String) detail.get(CHECK_ITEM_MD5):"";

                    List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail1 = item.getDetail();
                    for (int i = 0; i < detail1.size(); i++) {
                        if (isCheckItemChange(issue, item) || StringUtils.isEmpty(checkItemMD5) || StringUtils.isEmpty(detail1.get(i).getCheck_item_md5()) || detail1.get(i).getCheck_item_md5().equals(checkItemMD5)) {
                            Map<String, Object> modifyIssueMap = modifyIssue(refundMap, issueRoleMap, issue, item, true);
                            HouseQmCheckTaskIssue issues = (HouseQmCheckTaskIssue) modifyIssueMap.get(ISSUE);
                            refundMap = (HashMap<HouseQmCheckTaskIssue, ApiRefundInfo>) modifyIssueMap.get(REFUNDMAP);
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
                        String checkItemMD5 = (String) detail.get(CHECK_ITEM_MD5)!=null?(String) detail.get(CHECK_ITEM_MD5):"";
                        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail1 = item.getDetail();
                        for (int i = 0; i < detail1.size(); i++) {
                            if (isCheckItemChange(issue, item) || StringUtils.isEmpty(checkItemMD5) || StringUtils.isEmpty(detail1.get(i).getCheck_item_md5()) || detail1.get(i).getCheck_item_md5().equals(checkItemMD5)) {
                                Map<String, Object> modifyIssueMap = modifyIssue(refundMap, issueRoleMap, issue, item, false);
                                HouseQmCheckTaskIssue issues = (HouseQmCheckTaskIssue) modifyIssueMap.get(ISSUE);
                                refundMap = (HashMap<HouseQmCheckTaskIssue, ApiRefundInfo>) modifyIssueMap.get(REFUNDMAP);
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

        try {
            //  # 处理新增问题
            for (Map.Entry<String, HouseQmCheckTaskIssue> entry : issueInsertMap.entrySet()) {
                HouseQmCheckTaskIssue issue = entry.getValue();
                Integer res = houseQmCheckTaskIssueService.add(issue);
                if (res == null) {
                    log.info("insert new issue failed, data=" + JSON.toJSONString(issue) + "");
                } else {
                    issue.setId(res);
                }
                // # 写入推送记录
                if (CheckTaskIssueStatus.NoteNoAssign.getValue().equals(issue.getStatus())) {
                    List<Integer> desUserIds = getIssueCheckerList(checkerMap, issue, false);

                    pushList.addAll(desUserIds);
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
                    List<Integer> idsComma = StringUtil.strToInts(issue.getRepairerFollowerIds(), ",");
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
        } catch (Exception e) {
            log.error("新增问题异常"+e.getMessage());
            throw new LjBaseRuntimeException(500,e.getMessage());
        }

        //  # 处理更新问题
        for (Map.Entry<Object, HouseQmCheckTaskIssue> entry : issueUpdateMap.entrySet()) {
            HouseQmCheckTaskIssue issue =entry.getValue();
            try {
                houseQmCheckTaskIssueService.update(issue);
            } catch (Exception e) {
                log.info("insert new issue failed, data=" + JSON.toJSONString(issue) + "");
            }
            // # 写入推送记录
            Map<Object, Map> notifyStatMap = issueMapBody.getNotify_stat_map();
            if (CheckTaskIssueStatus.AssignNoReform.getValue().equals(issue.getStatus())) {
                List<Integer> desUserIds = Lists.newArrayList();

                if (issue.getRepairerId() > 0 && (!issue.getRepairerId().equals(notifyStatMap.get(issue.getUuid()).get("repairerId")) || CheckTaskIssueStatus.ReformNoCheck.getValue().equals(notifyStatMap.get(issue.getUuid()).get(STATUS)))) {
                    desUserIds.add(issue.getRepairerId());
                    pushList.add(issue.getRepairerId());
                }
                List<Integer> idsComma = StringUtil.strToInts(issue.getRepairerFollowerIds(), ",");
                List splitToIdsComma = (List) notifyStatMap.get(issue.getUuid()).get("repairerFollowerIds");
                idsComma.forEach(user -> {
                    if (user > 0 && (!splitToIdsComma.contains(user) || CheckTaskIssueStatus.ReformNoCheck.getValue().equals(notifyStatMap.get(issue.getUuid()).get(STATUS)))) {
                        desUserIds.add(user);
                        pushList.add(user);
                    }
                });
                List<Integer> list = CollectionUtil.removeDuplicate(desUserIds);
                if (CollectionUtils.isNotEmpty(list)) {
                    HouseQmCheckTaskNotifyRecord itemNotify = new HouseQmCheckTaskNotifyRecord();
                    itemNotify.setProjectId(issue.getProjectId());
                    itemNotify.setTaskId(issue.getTaskId());
                    itemNotify.setSrcUserId(0);
                    itemNotify.setDesUserIds(StringUtils.join(list, ","));
                    itemNotify.setModuleId(ModuleInfoEnum.GCGL.getValue());
                    itemNotify.setIssueId(issue.getId());
                    itemNotify.setIssueStatus(CheckTaskIssueStatus.AssignNoReform.getValue());
                    itemNotify.setExtraInfo("");
                    notifyList.add(itemNotify);
                }
            } else if (CheckTaskIssueStatus.ReformNoCheck.getValue().equals(issue.getStatus()) &&
                    !CheckTaskIssueStatus.ReformNoCheck.getValue().equals(notifyStatMap.get(issue.getUuid()).get(STATUS))) {
                ArrayList<Integer> desUserIds = getIssueCheckerList(checkerMap, issue, true);
                pushList.addAll(desUserIds);
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
            if (issueAttachment!=null){
                int delete = houseQmCheckTaskIssueAttachmentService.delete(issueAttachment);
                if (delete <=0) {
                    log.info("remove attachment failed, md5=" + attachment + "");
                }
            }
        });
        //# 处理新增问题日志
        logInsertList.forEach(loginsert -> {
            try {
                houseQmCheckTaskIssueLogService.add(loginsert);
            } catch (Exception e) {
                log.info("insert new log failed, data=" + JSON.toJSONString(logInsertList) + "");
            }
        });
        //     # 处理退单情况融合推送
        for (Map.Entry<HouseQmCheckTaskIssue, ApiRefundInfo> entry : refundMap.entrySet()) {
            List<Integer> desUserIds = getIssueCheckerList(checkerMap, entry.getKey(), false);
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
        try {
            //  # 处理代办事项消息推送
            if (CollectionUtils.isNotEmpty(pushList)) {
                String title = "新的待处理问题";
                String msg = "您在［工程检查］有新的待处理问题，请进入App同步更新。";
                List<Integer> ids = new ArrayList<>();
                for (Object push : pushList) {
                    ids.add(Integer.parseInt(String.valueOf(push)));
                }
                scanMsgPushService.sendUPush(title,msg,0,ids,1);

            }
            //   # 处理退单消息推送
            List<Integer> ids = new ArrayList<>();
            List<Integer> sendIds = new ArrayList<>();

            if (refundMap.size() > 0) {
                for (Map.Entry<HouseQmCheckTaskIssue, ApiRefundInfo> entry : refundMap.entrySet()) {
                    ids.add(refundMap.get(entry.getKey()).getRepairer());

                }
                String title = "新的待处理问题";
                Map<Integer,User>userMap=scanMsgPushService.createUserMap(ids);

                for (Map.Entry<HouseQmCheckTaskIssue, ApiRefundInfo> entry : refundMap.entrySet()) {
                    sendIds.add(refundMap.get(entry.getKey()).getChecker());
                    String msg = " 退回了一条问题，请进入[工程检查]App跟进处理";
                    ApiRefundInfo info = refundMap.get(entry);
                    if (info != null) msg=userMap.get(info.getRepairer()).getRealName()+msg;
                    log.debug("处理退单消息推送: ====> {}",msg);
                    scanMsgPushService.sendUPush(title,msg,0,sendIds,1);
                }
            }
        } catch (NumberFormatException e) {
            log.error("消息推送异常"+e.getMessage());
            throw new LjBaseRuntimeException(500,"消息推送异常");
        }
        try {
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
            for (int i = 0; i < issueLogs.size(); i++) {
                ApiHouseQmCheckTaskIssueLogInfo log = issueLogs.get(i);
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

            log.info("kafka推送  data---------------:{}",JSON.toJSONString(report));

            //kafka消息推送
            kafkaProducer.produce(EventQueueEnum.PKG_HOUSEQM_ISSUE_REPORTED.getValue(), report);
        } catch (Exception e) {
            log.error("kafka 推送异常："+e.getMessage());
            throw new LjBaseRuntimeException(500,"kafka 推送异常");
        }

        ReportIssueVo vo = new ReportIssueVo();
        vo.setDropped(dropped);
        return vo;
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
            if (!squadIds.contains(entry.getKey())) {
                squadIds.add(entry.getKey());
            }
        }
        if (CollectionUtils.isEmpty(squadIds)) {//如果创建者属于检查人组
            return desUserIds;
        }
        Map<Integer, Map<Integer, Integer>> roleUsers = checkerMap.get(issue.getTaskId());
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : roleUsers.entrySet()) {
            Integer user = entry.getKey();
            for (Map.Entry<Integer, Integer> sentry : roleUsers.get(user).entrySet()) {
                Integer squadId = sentry.getKey();
                if (squadIds.contains(squadId)) {
                    if (b &&!CheckTaskRoleCanApproveType.Yes.getValue().equals(roleUsers.get(user).get(squadId))) {
                        continue;
                    }
                    if (user > 0 && !desUserIds.contains(user)) {
                        desUserIds.add(entry.getKey());
                    }
                }
            }

        }
        return desUserIds;
    }

    private Map<String, Object> modifyIssue(Map<HouseQmCheckTaskIssue, ApiRefundInfo> refundMap, HashMap<String, ApiUserRoleInIssue> issueRoleMap, HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item, Boolean b) {
        Boolean flag=!b && convertLogStatus(item.getStatus()).equals(CheckTaskIssueStatus.NoteNoAssign.getValue()) && issue.getStatus().equals(CheckTaskIssueStatus.AssignNoReform.getValue());
        //  # 判断是否修改检查项
        if (isCheckItemChange(issue, item)) {
            log.info("check_item info need to update");
            return reassignIssue(flag,issueRoleMap, issue, item);
        }
        //  # 如果是退单情况
        if (!b  && flag) {
            log.info("refund issue");
            if (issue.getRepairerId() > 0 && issue.getSenderId() > 0) {
                ApiRefundInfo info = new ApiRefundInfo();
                info.setRepairer(issue.getRepairerId());
                info.setChecker(issue.getSenderId());
                refundMap.put(issue, info);
            }
            return refundIssue(refundMap,issueRoleMap, issue, item);
        }
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail = item.getDetail();
        detail.forEach(detailInfo -> {
            if (detailInfo.getTyp() != -1) {
                issue.setTyp(detailInfo.getTyp());
            }
            Integer oldStatus = issue.getStatus();
            if (item.getStatus() != -1) {
                //  # 操作状态是否为需要修改issue状态的操作
                if (!item.getStatus().equals(CheckTaskIssueLogStatus.Repairing.getValue()) && !item.getStatus().equals(CheckTaskIssueLogStatus.EditBaseInfo.getValue())) {
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
                } else if (item.getStatus().equals(CheckTaskIssueLogStatus.UpdateIssueInfo.getValue())&&StringUtils.isNotEmpty(item.getDesc())) {
                    issue.setContent(String.format("%s; %s",issue.getContent() , item.getDesc()));
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
            //    结束时间
            if (detailInfo.getEnd_on() > 0) {
                issue.setEndOn(DateUtil.transForDate(detailInfo.getEnd_on()));
            }

            // # 严重程度
            if (detailInfo.getCondition() != -1) {
                issue.setCondition(detailInfo.getCondition());
            }
            // # 问题类型
            CheckTaskIssueType[] values = CheckTaskIssueType.values();
            List<Integer> checkTaskIssueTypes = Lists.newArrayList();
            for (CheckTaskIssueType value : values) {
                checkTaskIssueTypes.add(value.getValue());
            }
            if (detailInfo.getTyp() != 1 && checkTaskIssueTypes.contains(detailInfo.getTyp())) {
                issue.setTyp(detailInfo.getTyp());
            }
            ApiUserRoleInIssue roleItem;
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
                map.put(roleUser(detailInfo.getRepairer_id(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(map);
                // # 整改参与人
                List<Integer> followerIds = StringUtil.strToInts(detailInfo.getRepairer_follower_ids(), ",");
                for (Integer followerId : followerIds) {
                    map.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(map);
                }
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detailInfo.getRepairer_id());
                if (StringUtils.isNotEmpty(detailInfo.getRepairer_follower_ids())) {
                    issue.setRepairerFollowerIds(detailInfo.getRepairer_follower_ids());
                } else {
                    issue.setRepairerFollowerIds("");
                }
            }
            Map<String, Object> map = JSON.parseObject(issue.getDetail(), Map.class);
            // # 编辑问题的detail字段
            if (detailInfo.getIssue_reason() != -1 || detailInfo.getIssue_reason() != 0) {
                map.put(ISSUEREASON, detailInfo.getIssue_reason());
            }
            if (!detailInfo.getIssue_reason_detail().equals("") || !detailInfo.getIssue_reason_detail().equals("-1")) {
                map.put(ISSUE_REASOND_ETAIL, detailInfo.getIssue_reason_detail());
            }
            if (!detailInfo.getIssue_suggest().equals("") || !detailInfo.getIssue_suggest().equals("-1")) {
                map.put(ISSUE_SUGGEST, detailInfo.getIssue_suggest());
            }
            if (!detailInfo.getPotential_risk().equals("") || !detailInfo.getPotential_risk().equals("-1")) {
                map.put(POTENTIAL_RISK, detailInfo.getPotential_risk());
            }
            if (!detailInfo.getPreventive_action_detail().equals("") || !detailInfo.getPreventive_action_detail().equals("-1")) {
                map.put(PREVENTIVE_ACTION_DETAIL, detailInfo.getPreventive_action_detail());
            }
            issue.setDetail(JSON.toJSONString(map));
        });
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put(ISSUE, issue);
        resmap.put(REFUNDMAP, refundMap);
        return resmap;
    }

    private Map<String, Object> refundIssue(Map<HouseQmCheckTaskIssue, ApiRefundInfo> refundMap,HashMap<String, ApiUserRoleInIssue> issueRoleMap, HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item) {
        issue.setRepairerId(0);
        issue.setRepairerFollowerIds("");
        issue.setLastRepairer(0);
        issue.setLastRepairerAt(DateUtil.strToDate(START_VALUE, YMDHMS));
        issue.setPlanEndOn(DateUtil.strToDate("1970-01-01 08:00:00 ",YMDHMS));//1970-01-01 08:00:00
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
            List<Integer> checkTaskIssueTypes = Lists.newArrayList();
            for (CheckTaskIssueType value : values) {
                checkTaskIssueTypes.add(value.getValue());
            }
            if (detailInfo.getTyp() != 1 && checkTaskIssueTypes.contains(detailInfo.getTyp())) {
                issue.setTyp(detailInfo.getTyp());
            }
            ApiUserRoleInIssue roleItem ;
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
                map.put(roleUser(detailInfo.getRepairer_id(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(map);
                // # 整改参与人
                List<Integer> followerIds = StringUtil.strToInts(detailInfo.getRepairer_follower_ids(), ",");
                for (Integer followerId : followerIds) {
                    map.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(map);
                }
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detailInfo.getRepairer_id());
                if (StringUtils.isNotEmpty(detailInfo.getRepairer_follower_ids())) {
                    issue.setRepairerFollowerIds(detailInfo.getRepairer_follower_ids());
                } else {
                    issue.setRepairerFollowerIds("");
                }
            }
            Map<String,Object> map = JSON.parseObject(issue.getDetail(), Map.class);
            if (detailInfo.getIssue_reason() != -1 || detailInfo.getIssue_reason() != 0) {
                map.put(ISSUEREASON, detailInfo.getIssue_reason());
            }
            if (!detailInfo.getIssue_reason_detail().equals("") || !detailInfo.getIssue_reason_detail().equals("-1")) {
                map.put(ISSUE_REASOND_ETAIL, detailInfo.getIssue_reason_detail());
            }
            if (!detailInfo.getIssue_suggest().equals("") || !detailInfo.getIssue_suggest().equals("-1")) {
                map.put(ISSUE_SUGGEST, detailInfo.getIssue_suggest());
            }
            if (!detailInfo.getPotential_risk().equals("") || !detailInfo.getPotential_risk().equals("-1")) {
                map.put(POTENTIAL_RISK, detailInfo.getPotential_risk());
            }
            if (!detailInfo.getPreventive_action_detail().equals("") || !detailInfo.getPreventive_action_detail().equals("-1")) {
                map.put(PREVENTIVE_ACTION_DETAIL, detailInfo.getPreventive_action_detail());
            }
            issue.setDetail(JSON.toJSONString(map));
        });
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put(ISSUE, issue);
        resmap.put(REFUNDMAP, refundMap);
        return resmap;
    }
    // 检查项修改
    private Map<String, Object> reassignIssue(Boolean flag,HashMap<String, ApiUserRoleInIssue> issueRoleMap, HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item) {
        if (flag){
            issue.setRepairerId(0);
            issue.setRepairerFollowerIds("");
            issue.setLastRepairer(0);
            issue.setLastRepairerAt(DateUtil.strToDate(START_VALUE, YMDHMS));
            issue.setPlanEndOn(DateUtil.strToDate("1970-01-01 08:00:00 ",YMDHMS));
        }
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
            List<Integer> checkTaskIssueTypes = Lists.newArrayList();
            for (CheckTaskIssueType value : values) {
                checkTaskIssueTypes.add(value.getValue());
            }
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
                map.put(roleUser(detailInfo.getRepairer_id(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(map);
                // # 整改参与人
                List<Integer> followerIds = StringUtil.strToInts(detailInfo.getRepairer_follower_ids(), ",");
                for (Integer followerId : followerIds) {
                    map.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(map);
                }
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detailInfo.getRepairer_id());
                if (StringUtils.isNotEmpty(detailInfo.getRepairer_follower_ids())) {
                    issue.setRepairerFollowerIds(detailInfo.getRepairer_follower_ids());
                } else {
                    issue.setRepairerFollowerIds("");
                }
            }
            Map map = JSON.parseObject(issue.getDetail(), Map.class);
            // # 编辑问题的detail字段
            if (!detailInfo.getCheck_item_md5().equals("") || !detailInfo.getCheck_item_md5().equals("-1")) {
                map.put(CHECK_ITEM_MD5, detailInfo.getCheck_item_md5());
            }
            if (detailInfo.getIssue_reason() != -1 || detailInfo.getIssue_reason() != 0) {
                map.put(ISSUEREASON, detailInfo.getIssue_reason());
            }
            if (!detailInfo.getIssue_reason_detail().equals("") || !detailInfo.getIssue_reason_detail().equals("-1")) {
                map.put(ISSUE_REASOND_ETAIL, detailInfo.getIssue_reason_detail());
            }
            if (!detailInfo.getIssue_suggest().equals("") || !detailInfo.getIssue_suggest().equals("-1")) {
                map.put(ISSUE_SUGGEST, detailInfo.getIssue_suggest());
            }
            if (!detailInfo.getPotential_risk().equals("") || !detailInfo.getPotential_risk().equals("-1")) {
                map.put(POTENTIAL_RISK, detailInfo.getPotential_risk());
            }
            if (!detailInfo.getPreventive_action_detail().equals("") || !detailInfo.getPreventive_action_detail().equals("-1")) {
                map.put(PREVENTIVE_ACTION_DETAIL, detailInfo.getPreventive_action_detail());
            }
            issue.setDetail(JSON.toJSONString(map));
        });
        Map<String, Object> resmap = Maps.newHashMap();
        resmap.put(ISSUE, issue);
        resmap.put(REFUNDMAP, Maps.newHashMap());
        return resmap;
    }

    private boolean isCheckItemChange(HouseQmCheckTaskIssue issue, ApiHouseQmCheckTaskIssueLogInfo item) {
        ArrayList<Integer> objects = Lists.newArrayList();
        objects.add(CheckTaskIssueStatus.NoteNoAssign.getValue());
        objects.add(CheckTaskIssueStatus.AssignNoReform.getValue());
        List<ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo> detail = item.getDetail();
        if (objects.contains(issue.getStatus())) {
            for (int i = 0; i < detail.size(); i++) {
                if (!detail.get(i).getCategory_key().equals("") || !detail.get(i).getCategory_key().equals("-1") ||! detail.get(i).getCheck_item_key().equals("") || !detail.get(i).getCheck_item_key().equals("-1")) {
                    if (!issue.getCategoryKey().equals(detail.get(i).getCategory_key()) || !issue.getCheckItemKey().equals(detail.get(i).getCheck_item_key())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private Map<Integer, Map<Integer, Map<Integer, Integer>>> createCheckerMap(List<Integer> taskIds) {
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
            if (detail.getTitle()!=null && !detail.getTitle().equals("-1")) {
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
            if (!detail.getRepairer_id().equals(-1)) {
                issue.setRepairerId(detail.getRepairer_id());
                issue.setLastRepairer(detail.getRepairer_id());
                issue.setLastRepairerAt(DateUtil.transForDate(log.getClient_create_at()));
            }
            if (!detail.getRepairer_follower_ids().equals("-1")) {
                issue.setRepairerFollowerIds(detail.getRepairer_follower_ids());
            }else issue.setRepairerFollowerIds("");

            issue.setDestroyUser(0);
            issue.setDeleteUser(0);
            HashMap<String, Object> details = Maps.newHashMap();
            details.put(CHECK_ITEM_MD5, "");
            if (detail.getIssue_reason() != null && detail.getIssue_reason() != -1) {
                details.put(ISSUEREASON, detail.getIssue_reason());
            } else {
                details.put(ISSUEREASON, 0);
            }
            if (detail.getIssue_reason_detail() != null && !detail.getIssue_reason_detail().equals("-1")) {
                details.put(ISSUE_REASOND_ETAIL, detail.getIssue_reason_detail());
            } else {
                details.put(ISSUE_REASOND_ETAIL, "");
            }
            if (detail.getIssue_suggest() != null && !detail.getIssue_suggest().equals("-1")) {
                details.put(ISSUE_SUGGEST, detail.getIssue_suggest());
            } else {
                details.put(ISSUE_SUGGEST, "");
            }
            if (StringUtils.isNotEmpty(detail.getPotential_risk()) && !detail.getPotential_risk().equals("-1")) {
                details.put(POTENTIAL_RISK, detail.getPotential_risk());
            } else {
                details.put(POTENTIAL_RISK, "");
            }
            if (StringUtils.isNotEmpty(detail.getPreventive_action_detail()) && !detail.getPreventive_action_detail().equals("-1")) {
                details.put(PREVENTIVE_ACTION_DETAIL, detail.getPreventive_action_detail());
            } else {
                details.put(PREVENTIVE_ACTION_DETAIL, "");
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
                map.put(roleUser(issue.getRepairerId(), UserInIssueRoleType.Repairer.getValue()), true);
                roleItem.setUser_role(map);
                // # 整改参与人
                List<Integer> followerIds = StringUtil.strToInts(detail.getRepairer_follower_ids(), ",");
                followerIds.forEach(followerId -> {
                    map.put(roleUser(followerId, UserInIssueRoleType.RepairerFollower.getValue()), true);
                    roleItem.setUser_role(map);
                });
                issueRoleMap.put(issue.getUuid(), roleItem);
                //   # 写入issue中的冗余字段
                issue.setRepairerId(detail.getRepairer_id());
                if (StringUtils.isNotEmpty(detail.getRepairer_follower_ids())) {
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
            areaPathAndId =String.format("%s%d%s",area.getPath(),areaId,"/");
        }
        return areaPathAndId;
    }

    private String getCheckItemPathAndKey(String checkItemKey) {
        String checkItemPathAndKey = "";
        CheckItemV3 checkItem = checkItemV3Service.selectByKeyNotDel(checkItemKey);
        if (checkItem != null) {
            checkItemPathAndKey =String.format("%s%s%s",checkItem.getPath(),checkItemKey,"/");
        }
        return checkItemPathAndKey;

    }

    private String getCategoryPathAndKey(String categoryKey) {
        String categoryPathAndKey = "";
        CategoryV3 category = categoryV3Service.selectByKeyNotDel(categoryKey);
        if (category != null) {
            categoryPathAndKey=  String.format("%s%s%s",category.getPath(), categoryKey,"/");
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
            detailMap.put(PLAN_END_ON, item.getPlan_end_on());
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
            detailMap.put(CHECK_ITEM_MD5, item.getCheck_item_md5());
            detailMap.put(ISSUEREASON, item.getIssue_reason());
            detailMap.put(ISSUE_REASOND_ETAIL, item.getIssue_reason_detail());
            detailMap.put(ISSUE_SUGGEST, item.getIssue_suggest());
            detailMap.put(POTENTIAL_RISK, item.getPotential_risk());
            detailMap.put(PREVENTIVE_ACTION_DETAIL, item.getPreventive_action_detail());
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
                notifyStatDict.put(item.getUuid(), apiNotifyStat(item.getStatus(), item.getRepairerId(), StringUtil.strToInts(item.getRepairerFollowerIds(), ",")));
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


    private Map<String, Object> apiNotifyStat(Integer status, Integer repairerId, List<Integer> repairerFollowerId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(STATUS, status);
        map.put("repairerId", repairerId);
        map.put("repairerFollowerIds", repairerFollowerId);
        return map;
    }

    private boolean datetimeZero(Date deleteAt) {
        return deleteAt == null || new SimpleDateFormat(YMDHMS).format(deleteAt).equals(START_VALUE) || new SimpleDateFormat(YMDHMS).format(deleteAt).equals("") || DateUtil.datetimeToTimeStamp(deleteAt) <= DateUtil.datetimeToTimeStamp(new Date(0));
    }

    private List checkLogUuid(List<String> logUuids) {
        if (CollectionUtils.isEmpty(logUuids)) {
            return Lists.newArrayList();
        }
        List<HouseQmCheckTaskIssueLog> lst = houseQmCheckTaskIssueLogService.selectByUuidsAndNotDelete(logUuids);
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
            if (StringUtils.isEmpty(items.getUuid())) {
                log.info("uuid not exist, data=" + data + "");
                throw new LjBaseRuntimeException(646, "uuid not exist");
            }
            items.setTask_id((Integer) item.get("task_id"));
            if (items.getTask_id() == null) {
                log.info("task_id not exist, data=" + data + "");
                throw new LjBaseRuntimeException(651, "task_id not exist");
            }
            items.setIssue_uuid((String) item.get("issue_uuid"));
            if (StringUtils.isEmpty(items.getIssue_uuid())) {
                log.info("issue_uuid not exist, data=" + data + "");
                throw new LjBaseRuntimeException(656, "issue_uuid not exist");
            }
            if ((Integer) item.get("sender_id") != null) {
                items.setSender_id((Integer) item.get("sender_id"));
            } else {
                items.setSender_id(-1);
            }

            items.setDesc(item.get("desc")!=null?(String) item.get("desc"):"");

            if ((Integer) item.get(STATUS) != null) {
                items.setStatus((Integer) item.get(STATUS));
            } else {
                items.setStatus(-1);
            }

            items.setAttachment_md5_list(item.get("attachment_md5_list")!=null?(String) item.get("attachment_md5_list"):"");

            items.setAudio_md5_list(item.get("audio_md5_list")!=null?(String) item.get("audio_md5_list"):"");

            items.setMemo_audio_md5_list(item.get("memo_audio_md5_list")!=null?(String) item.get("memo_audio_md5_list"):"");

            if ((Integer) item.get("client_create_at") != null) {
                items.setClient_create_at((Integer) item.get("client_create_at"));
            } else {
                items.setClient_create_at(-1);
            }
            ApiHouseQmCheckTaskIssueLogInfo.ApiHouseQmCheckTaskIssueLogDetailInfo info = new ApiHouseQmCheckTaskIssueLogInfo().new ApiHouseQmCheckTaskIssueLogDetailInfo();
            Map detail = (Map) item.get("detail");
            if (detail != null) {
                info.setArea_id(detail.get("area_id") != null ? ((Integer) detail.get("area_id")) : (-1));
                info.setPos_x(detail.get("pos_x") != null ? ((Integer) detail.get("pos_x")) : (-1));
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

                info.setRepairer_follower_ids(detail.get("repairer_follower_ids")!=null?(String) detail.get("repairer_follower_ids"):"-1");

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

                info.setCategory_key( detail.get("category_key")!=null?(String) detail.get("category_key"):"-1");

                info.setDrawing_md5(detail.get("drawing_md5")!=null?(String) detail.get("drawing_md5"):"-1");

                info.setCheck_item_key(detail.get("check_item_key") != null ? (String) detail.get("check_item_key") : "-1");


                info.setRemove_memo_audio_md5_list(detail.get("remove_memo_audio_md5_list") != null ? (String) detail.get("remove_memo_audio_md5_list") : "-1");


                info.setTitle(detail.get("title") != null ? (String) detail.get("title") : "");


                info.setCheck_item_md5(detail.get("check_item_md5") != null ? (String) detail.get("check_item_md5") : "");

                if (detail.get("issue_reason") != null) {
                    info.setIssue_reason((Integer) detail.get("issue_reason"));
                } else {
                    info.setIssue_reason(0);
                }

                info.setIssue_reason_detail(detail.get("issue_reason_detail")!=null?(String) detail.get("issue_reason_detail"):"");

                info.setIssue_suggest(detail.get("issue_suggest")!=null?(String) detail.get("issue_suggest"):"");

                info.setPotential_risk(detail.get("potential_risk")!=null?(String) detail.get("potential_risk"):"");

                info.setPreventive_action_detail(detail.get("preventive_action_detail")!=null?(String) detail.get("preventive_action_detail"):"");

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


}
