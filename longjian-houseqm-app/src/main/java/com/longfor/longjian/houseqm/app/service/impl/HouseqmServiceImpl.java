package com.longfor.longjian.houseqm.app.service.impl;

import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.vo.ApiHouseQmCheckTaskIssueLogDetailRspVo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqm.ApiHouseQmCheckTaskIssueDetail;
import com.longfor.longjian.houseqm.app.vo.houseqm.ApiHouseQmCheckTaskIssueLogRsp;
import com.longfor.longjian.houseqm.app.vo.houseqm.ApiHouseQmCheckTaskIssueRsp;
import com.longfor.longjian.houseqm.app.vo.houseqm.HouseqmMyIssueLogListRspVo;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueAttachmentPublicTypeEnum;
import com.longfor.longjian.houseqm.consts.HouseQmUserInIssueRoleTypeEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.JsonUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HouseqmServiceImpl implements IHouseqmService {
    private static final Integer HOUSEQM_API_GET_PER_TIME = 5000;
    @Resource
    private AreaService areaService;
    @Autowired
    private HouseQmCheckTaskIssueLogService houseQmCheckTaskIssueLogService;
    @Autowired
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;
    @Resource
    private SessionInfo sessionInfo;

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
    public TaskResponse<HouseqmMyIssueLogListRspVo> myIssueLogList(DeviceReq deviceReq, HttpServletRequest request) {
        TaskResponse<HouseqmMyIssueLogListRspVo> taskResponse = new TaskResponse<>();
        HouseqmMyIssueLogListRspVo myIssueListVo = new HouseqmMyIssueLogListRspVo();

        List<ApiHouseQmCheckTaskIssueLogRsp> result = new ArrayList<>();

        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        Integer start = 1;//源码 start=0
        Integer lastId = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        try {
            List<HouseQmCheckTaskIssueLog> res = houseQmCheckTaskIssueLogService.searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), limit, start, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            //获取最后一次的id
            if (res.size() > 0) lastId = res.get(res.size() - 1).getId();

            myIssueListVo.setLast_id(lastId);

            List<String> uuids = new ArrayList<>();
            res.forEach(item -> uuids.add(item.getIssueUuid()));

            List<HouseQmCheckTaskIssue> resIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(deviceReq.getTask_id(), uuids);
            Map<String, HouseQmCheckTaskIssue> mIssue = new HashMap<>();
            resIssues.forEach(item -> mIssue.put(item.getUuid(), item));

            res.forEach(item -> {

                ApiHouseQmCheckTaskIssueLogDetailRspVo detail = JSON.parseObject(item.getDetail(), ApiHouseQmCheckTaskIssueLogDetailRspVo.class);
                detail.setCategory_cls(0);
                HouseQmCheckTaskIssue v = mIssue.get(item.getIssueUuid());
                if (v != null) {
                    detail.setTitle(v.getTitle());
                    detail.setPos_x(v.getPosX());
                    detail.setPos_y(v.getPosY());
                    detail.setTyp(v.getTyp());
                    detail.setArea_id(v.getAreaId());
                }
                ApiHouseQmCheckTaskIssueLogRsp houseQmCheckTaskIssueLogRspVo = new ApiHouseQmCheckTaskIssueLogRsp();
                houseQmCheckTaskIssueLogRspVo.setId(item.getId());
                houseQmCheckTaskIssueLogRspVo.setProject_id(item.getProjectId());
                houseQmCheckTaskIssueLogRspVo.setTask_id(item.getTaskId());
                houseQmCheckTaskIssueLogRspVo.setUuid(item.getUuid());
                houseQmCheckTaskIssueLogRspVo.setIssue_uuid(item.getIssueUuid());
                houseQmCheckTaskIssueLogRspVo.setSender_id(item.getSenderId());
                houseQmCheckTaskIssueLogRspVo.setDesc(item.getDesc());
                houseQmCheckTaskIssueLogRspVo.setStatus(item.getStatus());
                houseQmCheckTaskIssueLogRspVo.setAttachment_md5_list(item.getAttachmentMd5List());
                houseQmCheckTaskIssueLogRspVo.setAudio_md5_list(item.getAudioMd5List());
                houseQmCheckTaskIssueLogRspVo.setMemo_audio_md5_list(item.getMemoAudioMd5List());
                houseQmCheckTaskIssueLogRspVo.setClient_create_at(DateUtil.datetimeToTimeStamp(item.getClientCreateAt()));
                houseQmCheckTaskIssueLogRspVo.setUpdate_at(DateUtil.datetimeToTimeStamp(item.getUpdateAt()));
                houseQmCheckTaskIssueLogRspVo.setDelete_at(item.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(item.getDeleteAt()));

                houseQmCheckTaskIssueLogRspVo.setDetail(detail);
                if (v != null) {
                    houseQmCheckTaskIssueLogRspVo.setIssue_uuid(v.getUuid());
                }
                result.add(houseQmCheckTaskIssueLogRspVo);
            });
            myIssueListVo.setIssue_list(result);
            taskResponse.setData(myIssueListVo);
        } catch (Exception e) {
            log.error("error:" + e);
        }
        return taskResponse;
    }

    @Override
    public TaskResponse<MyIssueListVo> myIssueList(DeviceReq deviceReq, HttpServletRequest request) {
        TaskResponse<MyIssueListVo> taskResponse = new TaskResponse<>();
        MyIssueListVo myIssueListVo = new MyIssueListVo();
        List<ApiHouseQmCheckTaskIssueRsp> items = new ArrayList<>();
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        Integer lastId = 0;
        try {
            List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), start, limit, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            // 上次获取的最后ID，首次拉取传`0`
            if (houseQmCheckTaskIssues.size() > 0)
                lastId = houseQmCheckTaskIssues.get(houseQmCheckTaskIssues.size() - 1).getId();
            houseQmCheckTaskIssues.forEach(houseQmCheckTaskIssue -> {
                ApiHouseQmCheckTaskIssueRsp houseQmCheckTaskIssueVo = new ApiHouseQmCheckTaskIssueRsp();
                houseQmCheckTaskIssueVo.setId(houseQmCheckTaskIssue.getId());
                houseQmCheckTaskIssueVo.setProject_id(houseQmCheckTaskIssue.getProjectId());
                houseQmCheckTaskIssueVo.setTask_id(houseQmCheckTaskIssue.getTaskId());
                houseQmCheckTaskIssueVo.setUuid(houseQmCheckTaskIssue.getUuid());
                houseQmCheckTaskIssueVo.setSender_id(houseQmCheckTaskIssue.getSenderId());

                houseQmCheckTaskIssueVo.setPlan_end_on(houseQmCheckTaskIssue.getPlanEndOn() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getPlanEndOn()));
                houseQmCheckTaskIssueVo.setEnd_on(houseQmCheckTaskIssue.getEndOn() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getEndOn()));

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
                //houseQmCheckTaskIssueVo.setAudio_md5_list(houseQmCheckTaskIssue.getAudioMd5List());
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

                //{"IssueReason":0,"IssueReasonDetail":"","IssueSuggest":"","PotentialRisk":"","PreventiveActionDetail":""}
                ApiHouseQmCheckTaskIssueDetail issueDetail = new ApiHouseQmCheckTaskIssueDetail();
                issueDetail.setIssue_reason((Integer) map.get("IssueReason"));
                issueDetail.setIssue_reason_detail((String) map.get("IssueReasonDetail"));
                issueDetail.setIssue_suggest((String) map.get("IssueSuggest"));
                issueDetail.setPotential_risk((String) map.get("PotentialRisk"));
                issueDetail.setPreventive_action_detail((String) map.get("PreventiveActionDetail"));
                houseQmCheckTaskIssueVo.setDetail(issueDetail);
                houseQmCheckTaskIssueVo.setUpdate_at(houseQmCheckTaskIssue.getUpdateAt() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getUpdateAt()));
                houseQmCheckTaskIssueVo.setDelete_at(houseQmCheckTaskIssue.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssue.getDeleteAt()));
                //String IssueVoJson = JsonUtil.GsonString(houseQmCheckTaskIssueVo);
                items.add(houseQmCheckTaskIssueVo);
            });
            myIssueListVo.setIssue_list(items);
            myIssueListVo.setLast_id(lastId);
            taskResponse.setData(myIssueListVo);
        } catch (Exception e) {
            log.error("error" + e);
            e.printStackTrace();
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
                //String rspVoJson = JSON.toJSONString(apiHouseQmCheckTaskIssueMemberRspVo);
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
    public LjBaseResponse<MyIssueAttachListVo> myIssueAttachementList(DeviceReq deviceReq) {
        LjBaseResponse<MyIssueAttachListVo> ljBaseResponse = new LjBaseResponse<>();
        MyIssueAttachListVo myIssueAttachListVo = new MyIssueAttachListVo();
        List<ApiHouseQmCheckTaskIssueAttachmentRspVo> houseQmCheckTaskIssueJsons = new ArrayList<>();

        //Integer userId = (Integer)request.getSession().getAttribute("uid");
        //todo 暂时获取不到uid
        Integer userId = 7566;
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

            if (attachments.size() > 0) lastId = attachments.get(attachments.size() - 1).getId();
            attachments.forEach(houseQmCheckTaskIssueAttachment -> {
                ApiHouseQmCheckTaskIssueAttachmentRspVo apiHouseQmCheckTaskIssueAttachmentRspVo = new ApiHouseQmCheckTaskIssueAttachmentRspVo();
                apiHouseQmCheckTaskIssueAttachmentRspVo.setId(houseQmCheckTaskIssueAttachment.getId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setProjectId(houseQmCheckTaskIssueAttachment.getProjectId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setTaskId(houseQmCheckTaskIssueAttachment.getTaskId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setIssueUuid(houseQmCheckTaskIssueAttachment.getIssueUuid());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setUserId(houseQmCheckTaskIssueAttachment.getUserId());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setPublicType(houseQmCheckTaskIssueAttachment.getPublicType());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setAttachmentType(houseQmCheckTaskIssueAttachment.getAttachmentType());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setMd5(houseQmCheckTaskIssueAttachment.getMd5());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setStatus(houseQmCheckTaskIssueAttachment.getStatus());
                apiHouseQmCheckTaskIssueAttachmentRspVo.setUpdateAt(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueAttachment.getUpdateAt()));
                apiHouseQmCheckTaskIssueAttachmentRspVo.setDeleteAt(houseQmCheckTaskIssueAttachment.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueAttachment.getDeleteAt()));
                //JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(apiHouseQmCheckTaskIssueAttachmentRspVo));
                houseQmCheckTaskIssueJsons.add(apiHouseQmCheckTaskIssueAttachmentRspVo);
            });
            myIssueAttachListVo.setAttachment_list(houseQmCheckTaskIssueJsons);
            myIssueAttachListVo.setLast_id(lastId);
            ljBaseResponse.setData(myIssueAttachListVo);
        } catch (Exception e) {
            log.error("error:" + e);
            e.printStackTrace();
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
        if (areaIds.size() == 0 || areaTypes.size() == 0) return null;
        List<Area> areas = areaService.searchAreaListByRootIdAndTypes(projectId, areaIds, areaTypes);
        return areas;
    }

}
