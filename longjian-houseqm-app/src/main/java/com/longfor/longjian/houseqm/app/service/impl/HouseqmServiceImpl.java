package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqm.ApiHouseQmCheckTaskIssueDetail;
import com.longfor.longjian.houseqm.app.vo.houseqm.ApiHouseQmCheckTaskIssueLogRsp;
import com.longfor.longjian.houseqm.app.vo.houseqm.ApiHouseQmCheckTaskIssueRsp;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmMyIssueLogListRspVo;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueAttachmentPublicTypeEnum;
import com.longfor.longjian.houseqm.consts.HouseQmUserInIssueRoleTypeEnum;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HouseqmServiceImpl implements IHouseqmService {
    private static final Integer HOUSEQM_API_GET_PER_TIME = 5000;
    @Resource
    private com.longfor.longjian.houseqm.domain.internalservice.AreaService areaService;
    @Resource
    private com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;
    @Resource
    private com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private com.longfor.longjian.houseqm.domain.internalservice.UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;
    @Resource
    private SessionInfo sessionInfo;

    private static final String USER_ID="userId";
    @Override
    public List<Integer> searchHouseQmApproveUserIdInMyCheckSquad(int userId, int taskId) {
        List<UserInHouseQmCheckTask> rs = userInHouseQmCheckTaskService.searchByTaskIdUserIdRoleType(taskId, userId, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
        List<Integer> squadIds = rs.stream().map(UserInHouseQmCheckTask::getSquadId).collect(Collectors.toSet()).stream().collect(Collectors.toList());
        List<UserInHouseQmCheckTask> rrs = userInHouseQmCheckTaskService.searchBySquadIdIn(squadIds);
        List<Integer> userIds = Lists.newArrayList();
        for (UserInHouseQmCheckTask r : rrs) {
            if (r.getCanApprove() > 0) {
                userIds.add(r.getUserId());
            }
        }
        if (userIds.isEmpty()) {
            userIds.add(userId);
            return userIds;
        }
        return userIds;
    }

    @Override
    public LjBaseResponse<HouseqmMyIssueLogListRspVo> myIssueLogList(DeviceReq deviceReq, HttpServletRequest request) {
        LjBaseResponse<HouseqmMyIssueLogListRspVo> taskResponse = new LjBaseResponse<>();
        HouseqmMyIssueLogListRspVo myIssueListVo = new HouseqmMyIssueLogListRspVo();
        List<ApiHouseQmCheckTaskIssueLogRsp> result = new ArrayList<>();
        Integer userId = (Integer) sessionInfo.getBaseInfo(USER_ID);
        Integer start = 0;
        Integer lastId = 0;
        //可能会导致接口出现504 请求超时
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        try {
            List<HouseQmCheckTaskIssueLog> houseQmCheckTaskIssueLogs = houseQmCheckTaskIssueLogService.searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), limit, start, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            //获取最后一次的id
            if (CollectionUtils.isNotEmpty(houseQmCheckTaskIssueLogs))
                lastId = houseQmCheckTaskIssueLogs.get(houseQmCheckTaskIssueLogs.size() - 1).getId();
            myIssueListVo.setLast_id(lastId);
            List<String> uuids = new ArrayList<>();
            houseQmCheckTaskIssueLogs.forEach(item ->
                uuids.add(item.getIssueUuid())
            );
            List<HouseQmCheckTaskIssue> resIssues = Lists.newArrayList();
            if (!uuids.isEmpty())
                resIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(deviceReq.getTask_id(), uuids);
            Map<String, HouseQmCheckTaskIssue> mIssue = new HashMap<>();
            resIssues.forEach(houseQmCheckTaskIssue ->
                mIssue.put(houseQmCheckTaskIssue.getUuid(), houseQmCheckTaskIssue)
            );
            houseQmCheckTaskIssueLogs.forEach(item -> {
                ApiHouseQmCheckTaskIssueLogDetailRspVo rspVo = JSON.parseObject(item.getDetail(), new TypeReference<ApiHouseQmCheckTaskIssueLogDetailRspVo>() {
                });

                rspVo.setCategory_cls(0);

                HouseQmCheckTaskIssue houseQmCheckTaskIssue = mIssue.get(item.getIssueUuid());
                if (houseQmCheckTaskIssue != null) {
                    rspVo.setTitle(houseQmCheckTaskIssue.getTitle());
                    rspVo.setPos_x(houseQmCheckTaskIssue.getPosX());
                    rspVo.setPos_y(houseQmCheckTaskIssue.getPosY());
                    rspVo.setTyp(houseQmCheckTaskIssue.getTyp());
                    rspVo.setArea_id(houseQmCheckTaskIssue.getAreaId());
                }
                ApiHouseQmCheckTaskIssueLogRsp issueLog = new ApiHouseQmCheckTaskIssueLogRsp();
                issueLog.setId(item.getId());
                issueLog.setProject_id(item.getProjectId());
                issueLog.setTask_id(item.getTaskId());
                issueLog.setUuid(item.getUuid());
                issueLog.setIssue_uuid(item.getIssueUuid());
                issueLog.setSender_id(item.getSenderId());
                issueLog.setDesc(item.getDesc());
                issueLog.setStatus(item.getStatus());
                issueLog.setAttachment_md5_list(item.getAttachmentMd5List());
                issueLog.setAudio_md5_list(item.getAudioMd5List());
                issueLog.setMemo_audio_md5_list(item.getMemoAudioMd5List());
                issueLog.setClient_create_at(DateUtil.datetimeToTimeStamp(item.getClientCreateAt()));
                issueLog.setUpdate_at(DateUtil.datetimeToTimeStamp(item.getUpdateAt()));
                issueLog.setDelete_at(item.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(item.getDeleteAt()));
                issueLog.setDetail(rspVo);
                if (houseQmCheckTaskIssue != null) {
                    issueLog.setIssue_uuid(houseQmCheckTaskIssue.getUuid());
                }
                result.add(issueLog);
            });
            myIssueListVo.setIssue_list(result);
            taskResponse.setData(myIssueListVo);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return taskResponse;
    }

    @Override
    public LjBaseResponse<MyIssueListVo> myIssueList(DeviceReq deviceReq, HttpServletRequest request) {
        LjBaseResponse<MyIssueListVo> taskResponse = new LjBaseResponse<>();
        MyIssueListVo myIssueListVo = new MyIssueListVo();
        List<ApiHouseQmCheckTaskIssueRsp> items = new ArrayList<>();
        Integer userId = (Integer) sessionInfo.getBaseInfo(USER_ID);
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        Integer lastId = 0;
        try {
            List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), start, limit, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            // 上次获取的最后ID，首次拉取传`0`
            if (CollectionUtils.isNotEmpty(houseQmCheckTaskIssues))
                lastId = houseQmCheckTaskIssues.get(houseQmCheckTaskIssues.size() - 1).getId();
            houseQmCheckTaskIssues.forEach(houseQmCheckTaskIssue -> {
                ApiHouseQmCheckTaskIssueRsp houseQmCheckTaskIssueVo = new ApiHouseQmCheckTaskIssueRsp();
                houseQmCheckTaskIssueVo.setId(houseQmCheckTaskIssue.getId());
                houseQmCheckTaskIssueVo.setProject_id(houseQmCheckTaskIssue.getProjectId());
                houseQmCheckTaskIssueVo.setTask_id(houseQmCheckTaskIssue.getTaskId());
                houseQmCheckTaskIssueVo.setUuid(houseQmCheckTaskIssue.getUuid());
                houseQmCheckTaskIssueVo.setSender_id(houseQmCheckTaskIssue.getSenderId());
                houseQmCheckTaskIssueVo.setPlan_end_on(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getPlanEndOn()));
                houseQmCheckTaskIssueVo.setEnd_on(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getEndOn()));
                houseQmCheckTaskIssueVo.setArea_id(houseQmCheckTaskIssue.getAreaId());
                houseQmCheckTaskIssueVo.setArea_path_and_id(houseQmCheckTaskIssue.getAreaPathAndId());
                houseQmCheckTaskIssueVo.setCategory_cls(houseQmCheckTaskIssue.getCategoryCls());
                houseQmCheckTaskIssueVo.setCategory_key(houseQmCheckTaskIssue.getCategoryKey());
                houseQmCheckTaskIssueVo.setCategory_path_and_key(houseQmCheckTaskIssue.getCategoryPathAndKey());
                houseQmCheckTaskIssueVo.setCheck_item_key(houseQmCheckTaskIssue.getCheckItemKey());
                houseQmCheckTaskIssueVo.setCheck_item_path_and_key(houseQmCheckTaskIssue.getCheckItemPathAndKey());
                houseQmCheckTaskIssueVo.setDrawing_md5(houseQmCheckTaskIssue.getDrawingMD5());
                houseQmCheckTaskIssueVo.setPos_x(houseQmCheckTaskIssue.getPosX());
                houseQmCheckTaskIssueVo.setPos_y(houseQmCheckTaskIssue.getPosY());
                houseQmCheckTaskIssueVo.setTitle(houseQmCheckTaskIssue.getTitle());
                houseQmCheckTaskIssueVo.setTyp(houseQmCheckTaskIssue.getTyp());
                houseQmCheckTaskIssueVo.setContent(houseQmCheckTaskIssue.getContent());
                houseQmCheckTaskIssueVo.setCondition(houseQmCheckTaskIssue.getCondition());
                houseQmCheckTaskIssueVo.setStatus(houseQmCheckTaskIssue.getStatus());
                houseQmCheckTaskIssueVo.setRepairer_id(houseQmCheckTaskIssue.getRepairerId());
                houseQmCheckTaskIssueVo.setRepairer_follower_ids(houseQmCheckTaskIssue.getRepairerFollowerIds());
                houseQmCheckTaskIssueVo.setAttachment_md5_list(houseQmCheckTaskIssue.getAttachmentMd5List());
                houseQmCheckTaskIssueVo.setAudio_md5_list("");
                houseQmCheckTaskIssueVo.setClient_create_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getClientCreateAt()));
                houseQmCheckTaskIssueVo.setLast_assigner(houseQmCheckTaskIssue.getLastAssigner());
                houseQmCheckTaskIssueVo.setLast_assigner_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getLastAssignAt()));
                houseQmCheckTaskIssueVo.setLast_repairer(houseQmCheckTaskIssue.getLastRepairer());
                houseQmCheckTaskIssueVo.setLast_repairer_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getLastRepairerAt()));
                houseQmCheckTaskIssueVo.setDestroy_user(houseQmCheckTaskIssue.getDestroyUser());
                houseQmCheckTaskIssueVo.setDestroy_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getDestroyAt()));
                houseQmCheckTaskIssueVo.setDelete_user(houseQmCheckTaskIssue.getDeleteUser());
                houseQmCheckTaskIssueVo.setDelete_time(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getDeleteTime()));

                String detail = houseQmCheckTaskIssue.getDetail();
                Map map = JSON.parseObject(detail, Map.class);

                ApiHouseQmCheckTaskIssueDetail issueDetail = new ApiHouseQmCheckTaskIssueDetail();
                issueDetail.setIssue_reason((Integer) map.get("IssueReason"));
                issueDetail.setIssue_reason_detail((String) map.get("IssueReasonDetail"));
                issueDetail.setIssue_suggest((String) map.get("IssueSuggest"));
                issueDetail.setPotential_risk((String) map.get("PotentialRisk"));
                issueDetail.setPreventive_action_detail((String) map.get("PreventiveActionDetail"));
                houseQmCheckTaskIssueVo.setDetail(issueDetail);
                houseQmCheckTaskIssueVo.setUpdate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getUpdateAt()));
                houseQmCheckTaskIssueVo.setDelete_at(houseQmCheckTaskIssue.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getDeleteAt()));
                items.add(houseQmCheckTaskIssueVo);
            });
            myIssueListVo.setIssue_list(items);
            myIssueListVo.setLast_id(lastId);
            taskResponse.setData(myIssueListVo);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return taskResponse;
    }

    @Override
    public LjBaseResponse<MyIssueMemberListVo> issueMembers(DeviceReq deviceReq) {
        LjBaseResponse<MyIssueMemberListVo> ljBaseResponse = new LjBaseResponse<>();
        MyIssueMemberListVo myIssueMemberListVo = new MyIssueMemberListVo();
        List<ApiHouseQmCheckTaskIssueMemberRspVo> memberList = new ArrayList<>();
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        Integer lastId = 0;
        try {
            List<HouseQmCheckTaskIssueUser> houseQmCheckTaskIssueUsers = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueUserByTaskIdLastIdUpdateAtGt(deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), start, limit);
            if (CollectionUtils.isNotEmpty(houseQmCheckTaskIssueUsers))
                lastId = houseQmCheckTaskIssueUsers.get(houseQmCheckTaskIssueUsers.size() - 1).getId();
            houseQmCheckTaskIssueUsers.forEach(houseQmCheckTaskIssueUser -> {
                ApiHouseQmCheckTaskIssueMemberRspVo apiHouseQmCheckTaskIssueMemberRspVo = new ApiHouseQmCheckTaskIssueMemberRspVo();
                apiHouseQmCheckTaskIssueMemberRspVo.setId(houseQmCheckTaskIssueUser.getId());
                apiHouseQmCheckTaskIssueMemberRspVo.setUser_id(houseQmCheckTaskIssueUser.getUserId());
                apiHouseQmCheckTaskIssueMemberRspVo.setTask_id(houseQmCheckTaskIssueUser.getTaskId());
                apiHouseQmCheckTaskIssueMemberRspVo.setRole_type(houseQmCheckTaskIssueUser.getRoleType());
                apiHouseQmCheckTaskIssueMemberRspVo.setIssue_uuid(houseQmCheckTaskIssueUser.getIssueUuid());
                apiHouseQmCheckTaskIssueMemberRspVo.setUpdate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueUser.getUpdateAt()));
                apiHouseQmCheckTaskIssueMemberRspVo.setDelete_at(houseQmCheckTaskIssueUser.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueUser.getDeleteAt()));
                memberList.add(apiHouseQmCheckTaskIssueMemberRspVo);
            });
            myIssueMemberListVo.setMember_list(memberList);
            myIssueMemberListVo.setLast_id(lastId);
            ljBaseResponse.setData(myIssueMemberListVo);
        } catch (Exception e) {
            log.error("error:" + e);
        }
        return ljBaseResponse;
    }

    @Override
    public LjBaseResponse<MyIssueAttachListVo> myIssueAttachementList(DeviceReq deviceReq, HttpServletRequest request) {
        LjBaseResponse<MyIssueAttachListVo> ljBaseResponse = new LjBaseResponse<>();
        MyIssueAttachListVo myIssueAttachListVo = new MyIssueAttachListVo();
        List<ApiHouseQmCheckTaskIssueAttachmentRspVo> houseQmCheckTaskIssueJsons = new ArrayList<>();

        Integer userId = (Integer) sessionInfo.getBaseInfo(USER_ID);
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        Integer lastId = 0;
        //与我相关的附件
        //需求：
        //自己只能看到自己的私人附件
        //自己能看到自己以及与自己同组的人所产生问题中所产生的公开的附件（包括检查人与整改人）
        //
        //思路：
        //A、找出与自己同组的人（无论是检查人还是整改人）
        //B、找出与这批人员相关的issue，
        //C1、根据相关的issue，找到与issue相关的所有人员，再找到这批人员的相关的 公开 的附件
        //C2、根据相关的issue 或 根据task_id和user_id，检索自己私人的附件
        try {
            List<HouseQmCheckTaskIssueAttachment> attachments = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueAttachmentByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), start, limit, HouseQmCheckTaskIssueAttachmentPublicTypeEnum.Private.getId(), HouseQmCheckTaskIssueAttachmentPublicTypeEnum.Public.getId());
            // go源码中未对lastid进行处理

            attachments.forEach(houseQmCheckTaskIssueAttachment -> {
                ApiHouseQmCheckTaskIssueAttachmentRspVo apiHouseQmCheckTaskIssueAttachmentRspVo = new ApiHouseQmCheckTaskIssueAttachmentRspVo();
                apiHouseQmCheckTaskIssueAttachmentRspVo.setId(houseQmCheckTaskIssueAttachment.getId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setProject_id(houseQmCheckTaskIssueAttachment.getProjectId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setTask_id(houseQmCheckTaskIssueAttachment.getTaskId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setIssue_uuid(houseQmCheckTaskIssueAttachment.getIssueUuid());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setUser_id(houseQmCheckTaskIssueAttachment.getUserId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setPublic_type(houseQmCheckTaskIssueAttachment.getPublicType());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setAttachment_type(houseQmCheckTaskIssueAttachment.getAttachmentType());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setMd5(houseQmCheckTaskIssueAttachment.getMd5());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setStatus(houseQmCheckTaskIssueAttachment.getStatus());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setUpdate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueAttachment.getUpdateAt()));
                apiHouseQmCheckTaskIssueAttachmentRspVo.setDelete_at(houseQmCheckTaskIssueAttachment.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueAttachment.getDeleteAt()));

                houseQmCheckTaskIssueJsons.add(apiHouseQmCheckTaskIssueAttachmentRspVo);
            });
            myIssueAttachListVo.setAttachment_list(houseQmCheckTaskIssueJsons);
            myIssueAttachListVo.setLast_id(lastId);
            ljBaseResponse.setData(myIssueAttachListVo);
        } catch (Exception e) {
            log.error("error:" + e);
        }
        return ljBaseResponse;
    }

    @Override
    public List<Area> searchTargetAreaByTaskId(Integer projectId, Integer taskId) {

        //读取出任务
        HouseQmCheckTask task = houseQmCheckTaskService.selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(projectId, taskId);
        //获取出任务下的区域与检验类型的交集
        List<Integer> areaIds = StringSplitToListUtil.strToInts(task.getAreaIds(), ",");
        List<Integer> areaTypes = StringSplitToListUtil.strToInts(task.getAreaTypes(), ",");
        if (CollectionUtils.isEmpty(areaIds)|| CollectionUtils.isEmpty(areaTypes)) return null;
        return  areaService.searchAreaListByRootIdAndTypes(projectId, areaIds, areaTypes);

    }

}
