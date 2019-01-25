package com.longfor.longjian.houseqm.app.service.impl;

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
        //Integer userId = (Integer)request.getSession().getAttribute("uid");
        //todo 暂时获取不到uid
        Integer userId = 7566;
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        try {
            List<HouseQmCheckTaskIssueLog> houseQmCheckTaskIssueLogs = houseQmCheckTaskIssueLogService.searchHouseQmCheckTaskIssueLogByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), limit, start, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            //获取最后一次的id
            Integer lastId = houseQmCheckTaskIssueLogs.get(houseQmCheckTaskIssueLogs.size() - 1).getId();
            myIssueListVo.setLast_id(lastId);
            List<String> uuids = new ArrayList<>();
            houseQmCheckTaskIssueLogs.forEach(houseQmCheckTaskIssueLog -> {
                uuids.add(houseQmCheckTaskIssueLog.getUuid());
            });
            List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByTaskIdUuidIn(deviceReq.getTask_id(), uuids);
            Map<String, HouseQmCheckTaskIssue> map = new HashMap<>();
            houseQmCheckTaskIssues.forEach(houseQmCheckTaskIssue -> {
                map.put(houseQmCheckTaskIssue.getUuid(), houseQmCheckTaskIssue);
            });
            houseQmCheckTaskIssueLogs.forEach(houseQmCheckTaskIssueLog -> {
                ApiHouseQmCheckTaskIssueLogDetailRspVo rspVo = JSON.parseObject(houseQmCheckTaskIssueLog.getDetail(), new TypeReference<ApiHouseQmCheckTaskIssueLogDetailRspVo>() {
                });
                HouseQmCheckTaskIssue houseQmCheckTaskIssue = map.get(houseQmCheckTaskIssueLog.getIssueUuid());
                if (houseQmCheckTaskIssue != null) {
                    rspVo.setTitle(houseQmCheckTaskIssue.getTitle());
                    rspVo.setPos_x(houseQmCheckTaskIssue.getPosX());
                    rspVo.setPos_y(houseQmCheckTaskIssue.getPosY());
                    rspVo.setTyp(houseQmCheckTaskIssue.getTyp());
                    rspVo.setArea_id(houseQmCheckTaskIssue.getAreaId());
                }
                ApiHouseQmCheckTaskIssueLogRsp houseQmCheckTaskIssueLogRspVo = new ApiHouseQmCheckTaskIssueLogRsp();
                houseQmCheckTaskIssueLogRspVo.setId(houseQmCheckTaskIssueLog.getId());
                houseQmCheckTaskIssueLogRspVo.setProject_id(houseQmCheckTaskIssueLog.getProjectId());
                houseQmCheckTaskIssueLogRspVo.setTask_id(houseQmCheckTaskIssueLog.getTaskId());
                houseQmCheckTaskIssueLogRspVo.setUuid(houseQmCheckTaskIssueLog.getUuid());
                houseQmCheckTaskIssueLogRspVo.setIssue_uuid(houseQmCheckTaskIssueLog.getIssueUuid());
                houseQmCheckTaskIssueLogRspVo.setSender_id(houseQmCheckTaskIssueLog.getSenderId());
                houseQmCheckTaskIssueLogRspVo.setDesc(houseQmCheckTaskIssueLog.getDesc());
                houseQmCheckTaskIssueLogRspVo.setStatus(houseQmCheckTaskIssueLog.getStatus());
                houseQmCheckTaskIssueLogRspVo.setAttachment_md5_list(houseQmCheckTaskIssueLog.getAttachmentMd5List());
                houseQmCheckTaskIssueLogRspVo.setAudio_md5_list(houseQmCheckTaskIssueLog.getAudioMd5List());
                houseQmCheckTaskIssueLogRspVo.setMemo_audio_md5_list(houseQmCheckTaskIssueLog.getMemoAudioMd5List());
                houseQmCheckTaskIssueLogRspVo.setClient_create_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueLog.getClientCreateAt()));
                houseQmCheckTaskIssueLogRspVo.setUpdate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueLog.getUpdateAt()));
                houseQmCheckTaskIssueLogRspVo.setDelete_at(houseQmCheckTaskIssueLog.getDeleteAt() == null ? 0 : DateUtil.datetimeToTimeStamp(houseQmCheckTaskIssueLog.getDeleteAt()));
                houseQmCheckTaskIssueLogRspVo.setDetail(rspVo);
                if (houseQmCheckTaskIssue != null) {
                    houseQmCheckTaskIssueLogRspVo.setIssue_uuid(houseQmCheckTaskIssue.getUuid());
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
        //Integer userId = (Integer)request.getSession().getAttribute("uid");
        //todo 暂时获取不到uid
        Integer userId = 7566;
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        Integer lastId = 0;
        try {
            List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), start, limit, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            // 上次获取的最后ID，首次拉取传`0`
            lastId = houseQmCheckTaskIssues.get(houseQmCheckTaskIssues.size() - 1).getId();
            houseQmCheckTaskIssues.forEach(houseQmCheckTaskIssue -> {
                ApiHouseQmCheckTaskIssueRsp houseQmCheckTaskIssueVo = new ApiHouseQmCheckTaskIssueRsp();
                houseQmCheckTaskIssueVo.setId(houseQmCheckTaskIssue.getId());
                houseQmCheckTaskIssueVo.setProject_id(houseQmCheckTaskIssue.getProjectId());
                houseQmCheckTaskIssueVo.setTask_id(houseQmCheckTaskIssue.getTaskId());
                houseQmCheckTaskIssueVo.setUuid(houseQmCheckTaskIssue.getUuid());
                houseQmCheckTaskIssueVo.setSender_id(houseQmCheckTaskIssue.getSenderId());
                houseQmCheckTaskIssueVo.setPlan_end_on(DateToInt(houseQmCheckTaskIssue.getPlanEndOn()));
                houseQmCheckTaskIssueVo.setEnd_on(DateToInt(houseQmCheckTaskIssue.getEndOn()));
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
                houseQmCheckTaskIssueVo.setAudio_md5_list(houseQmCheckTaskIssue.getAudioMd5List());
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
                houseQmCheckTaskIssueVo.setUpdate_at(DateToInt(houseQmCheckTaskIssue.getUpdateAt()));
                houseQmCheckTaskIssueVo.setDelete_at(houseQmCheckTaskIssue.getDeleteAt() == null ? 0 : DateToInt(houseQmCheckTaskIssue.getDeleteAt()));
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
        Integer userId = 9;
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        Integer lastId = 0;
        try {
            List<HouseQmCheckTaskIssueAttachment> houseQmCheckTaskIssueAttachments = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueAttachmentByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), start, limit, HouseQmCheckTaskIssueAttachmentPublicTypeEnum.Private.getId(), HouseQmCheckTaskIssueAttachmentPublicTypeEnum.Public.getId());
            lastId = houseQmCheckTaskIssueAttachments.get(houseQmCheckTaskIssueAttachments.size() - 1).getId();
            houseQmCheckTaskIssueAttachments.forEach(houseQmCheckTaskIssueAttachment -> {
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
                apiHouseQmCheckTaskIssueAttachmentRspVo.setUpdateAt(DateToInt(houseQmCheckTaskIssueAttachment.getUpdateAt()));
                apiHouseQmCheckTaskIssueAttachmentRspVo.setDeleteAt(houseQmCheckTaskIssueAttachment.getDeleteAt() == null ? 0 : DateToInt(houseQmCheckTaskIssueAttachment.getDeleteAt()));
                //JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(apiHouseQmCheckTaskIssueAttachmentRspVo));
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
        if (areaIds.size() == 0 || areaTypes.size() == 0) return null;
        List<Area> areas = areaService.searchAreaListByRootIdAndTypes(projectId, areaIds, areaTypes);
        return areas;
    }


    /**
     * lang类型转换Int类型
     *
     * @param date
     * @return
     */
    private int DateToInt(Date date) {
        long time = date.getTime();
        Long aLong = new Long(time);
        int i = aLong.intValue();
        return i;
    }

}
