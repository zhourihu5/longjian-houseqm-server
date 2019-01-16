package com.longfor.longjian.houseqm.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueAttachmentPublicTypeEnum;
import com.longfor.longjian.houseqm.consts.HouseQmUserInIssueRoleTypeEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.*;
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
        List<UserInHouseQmCheckTask> rs=userInHouseQmCheckTaskService.searchByTaskIdUserIdRoleType(taskId,userId,HouseQmUserInIssueRoleTypeEnum.Checker.getId());
        List<Integer> squadIds = rs.stream().map(UserInHouseQmCheckTask::getSquadId).collect(Collectors.toSet()).stream().collect(Collectors.toList());
        List<UserInHouseQmCheckTask> rrs=userInHouseQmCheckTaskService.searchBySquadIdIn(squadIds);
        List<Integer> userIds= Lists.newArrayList();
        for (UserInHouseQmCheckTask r : rrs) {
            if (r.getCanApprove()>0){
                userIds.add(r.getUserId());
            }
        }
        if (userIds.isEmpty()){
            userIds.add(userId);
            return userIds;
        }
        return userIds;
    }

    @Override
    public TaskResponse<MyIssueListVo> myIssueLogList(DeviceReq deviceReq, HttpServletRequest request) {
        TaskResponse<MyIssueListVo> taskResponse = new TaskResponse<>();
        MyIssueListVo myIssueListVo = new MyIssueListVo();
        List<String> houseQmCheckTaskIssueLogJsons = new ArrayList<>();
        //Integer userId = (Integer)request.getSession().getAttribute("uid");
        //todo 暂时获取不到uid
        Integer userId = 6;
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
                    rspVo.setPosX(houseQmCheckTaskIssue.getPosX());
                    rspVo.setPosY(houseQmCheckTaskIssue.getPosY());
                    rspVo.setTyp(houseQmCheckTaskIssue.getTyp());
                    rspVo.setAreaId(houseQmCheckTaskIssue.getAreaId());
                }
                HouseQmCheckTaskIssueLogRspVo houseQmCheckTaskIssueLogRspVo = new HouseQmCheckTaskIssueLogRspVo();
                houseQmCheckTaskIssueLogRspVo.setId(houseQmCheckTaskIssueLog.getId());
                houseQmCheckTaskIssueLogRspVo.setProjectId(houseQmCheckTaskIssueLog.getProjectId());
                houseQmCheckTaskIssueLogRspVo.setTaskId(houseQmCheckTaskIssueLog.getTaskId());
                houseQmCheckTaskIssueLogRspVo.setUuid(houseQmCheckTaskIssueLog.getUuid());
                houseQmCheckTaskIssueLogRspVo.setIssueUuid(houseQmCheckTaskIssueLog.getIssueUuid());
                houseQmCheckTaskIssueLogRspVo.setSenderId(houseQmCheckTaskIssueLog.getSenderId());
                houseQmCheckTaskIssueLogRspVo.setDesc(houseQmCheckTaskIssueLog.getDesc());
                houseQmCheckTaskIssueLogRspVo.setStatus(houseQmCheckTaskIssueLog.getStatus());
                houseQmCheckTaskIssueLogRspVo.setAttachmentMd5List(houseQmCheckTaskIssueLog.getAttachmentMd5List());
                houseQmCheckTaskIssueLogRspVo.setAudioMd5List(houseQmCheckTaskIssueLog.getAudioMd5List());
                houseQmCheckTaskIssueLogRspVo.setMemoAudioMd5List(houseQmCheckTaskIssueLog.getMemoAudioMd5List());
                houseQmCheckTaskIssueLogRspVo.setClientCreateAt(DateToInt(houseQmCheckTaskIssueLog.getClientCreateAt()));
                houseQmCheckTaskIssueLogRspVo.setUpdateAt(DateToInt(houseQmCheckTaskIssueLog.getUpdateAt()));
                houseQmCheckTaskIssueLogRspVo.setDeleteAt(houseQmCheckTaskIssueLog.getDeleteAt() == null ? 0 : DateToInt(houseQmCheckTaskIssueLog.getDeleteAt()));
                String rspVoJson = JSON.toJSONString(rspVo);
                houseQmCheckTaskIssueLogRspVo.setDetail(rspVoJson);
                if (houseQmCheckTaskIssue != null) {
                    houseQmCheckTaskIssueLogRspVo.setIssueUuid(houseQmCheckTaskIssue.getUuid());
                }
                String IssueLogJson = JsonUtil.GsonString(houseQmCheckTaskIssueLogRspVo);
                houseQmCheckTaskIssueLogJsons.add(IssueLogJson);
            });
            myIssueListVo.setIssue_list(houseQmCheckTaskIssueLogJsons);
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
        List<String> houseQmCheckTaskIssueJsons = new ArrayList<>();
        //Integer userId = (Integer)request.getSession().getAttribute("uid");
        //todo 暂时获取不到uid
        Integer userId = 6;
        Integer start = 0;
        Integer limit = HOUSEQM_API_GET_PER_TIME;
        Integer lastId = 0;
        try {
            List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByMyIdTaskIdLastIdUpdateAtGt(userId, deviceReq.getTask_id(), deviceReq.getLast_id(), deviceReq.getTimestamp(), start, limit, HouseQmUserInIssueRoleTypeEnum.Checker.getId());
            // 上次获取的最后ID，首次拉取传`0`
            lastId = houseQmCheckTaskIssues.get(houseQmCheckTaskIssues.size() - 1).getId();
            houseQmCheckTaskIssues.forEach(houseQmCheckTaskIssue -> {
                ApiHouseQmCheckTaskIssueRspVo houseQmCheckTaskIssueVo = new ApiHouseQmCheckTaskIssueRspVo();
                houseQmCheckTaskIssueVo.setId(houseQmCheckTaskIssue.getId());
                houseQmCheckTaskIssueVo.setProjectId(houseQmCheckTaskIssue.getProjectId());
                houseQmCheckTaskIssueVo.setTaskId(houseQmCheckTaskIssue.getTaskId());
                houseQmCheckTaskIssueVo.setUuid(houseQmCheckTaskIssue.getUuid());
                houseQmCheckTaskIssueVo.setSenderId(houseQmCheckTaskIssue.getSenderId());
                houseQmCheckTaskIssueVo.setPlanEndOn(DateToInt(houseQmCheckTaskIssue.getPlanEndOn()));
                houseQmCheckTaskIssueVo.setEndOn(DateToInt(houseQmCheckTaskIssue.getEndOn()));
                houseQmCheckTaskIssueVo.setAreaId(houseQmCheckTaskIssue.getAreaId());
                houseQmCheckTaskIssueVo.setAreaPathAndId(houseQmCheckTaskIssue.getAreaPathAndId());
                houseQmCheckTaskIssueVo.setCategoryCls(houseQmCheckTaskIssue.getCategoryCls());
                houseQmCheckTaskIssueVo.setCategoryKey(houseQmCheckTaskIssue.getCategoryKey());
                houseQmCheckTaskIssueVo.setCategoryPathAndKey(houseQmCheckTaskIssue.getCategoryPathAndKey());
                houseQmCheckTaskIssueVo.setCheckItemKey(houseQmCheckTaskIssue.getCheckItemKey());
                houseQmCheckTaskIssueVo.setCheckItemPathAndKey(houseQmCheckTaskIssue.getCheckItemPathAndKey());
                houseQmCheckTaskIssueVo.setDrawingMd5(houseQmCheckTaskIssue.getDrawingMD5());
                houseQmCheckTaskIssueVo.setPosX(houseQmCheckTaskIssue.getPosX());
                houseQmCheckTaskIssueVo.setPosY(houseQmCheckTaskIssue.getPosY());
                houseQmCheckTaskIssueVo.setTitle(houseQmCheckTaskIssue.getTitle());
                houseQmCheckTaskIssueVo.setTyp(houseQmCheckTaskIssue.getTyp());
                houseQmCheckTaskIssueVo.setContent(houseQmCheckTaskIssue.getContent());
                houseQmCheckTaskIssueVo.setCondition(houseQmCheckTaskIssue.getCondition());
                houseQmCheckTaskIssueVo.setStatus(houseQmCheckTaskIssue.getStatus());
                houseQmCheckTaskIssueVo.setRepairerId(houseQmCheckTaskIssue.getRepairerId());
                houseQmCheckTaskIssueVo.setRepairerFollowerIds(houseQmCheckTaskIssue.getRepairerFollowerIds());
                houseQmCheckTaskIssueVo.setAttachmentMd5List(houseQmCheckTaskIssue.getAttachmentMd5List());
                houseQmCheckTaskIssueVo.setClientCreateAt(DateToInt(houseQmCheckTaskIssue.getClientCreateAt()));
                houseQmCheckTaskIssueVo.setLastAssigner(houseQmCheckTaskIssue.getLastAssigner());
                houseQmCheckTaskIssueVo.setLastAssignerAt(DateToInt(houseQmCheckTaskIssue.getLastAssignAt()));
                houseQmCheckTaskIssueVo.setLastRepairer(houseQmCheckTaskIssue.getLastRepairer());
                houseQmCheckTaskIssueVo.setLastRepairerAt(DateToInt(houseQmCheckTaskIssue.getLastRepairerAt()));
                houseQmCheckTaskIssueVo.setDestroyUser(houseQmCheckTaskIssue.getDestroyUser());
                houseQmCheckTaskIssueVo.setDestroyAt(DateToInt(houseQmCheckTaskIssue.getDestroyAt()));
                houseQmCheckTaskIssueVo.setDeleteUser(houseQmCheckTaskIssue.getDeleteUser());
                houseQmCheckTaskIssueVo.setDeleteTime(DateToInt(houseQmCheckTaskIssue.getDeleteTime()));
                houseQmCheckTaskIssueVo.setDetail(houseQmCheckTaskIssue.getDetail());
                houseQmCheckTaskIssueVo.setUpdateAt(DateToInt(houseQmCheckTaskIssue.getUpdateAt()));
                houseQmCheckTaskIssueVo.setDeleteAt(houseQmCheckTaskIssue.getDeleteAt() == null ? 0 : DateToInt(houseQmCheckTaskIssue.getDeleteAt()));
                String IssueVoJson = JsonUtil.GsonString(houseQmCheckTaskIssueVo);
                houseQmCheckTaskIssueJsons.add(IssueVoJson);
            });
            myIssueListVo.setIssue_list(houseQmCheckTaskIssueJsons);
            myIssueListVo.setLast_id(lastId);
            taskResponse.setData(myIssueListVo);
        } catch (Exception e) {
            log.error("error" + e);
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
                apiHouseQmCheckTaskIssueMemberRspVo.setUserId(houseQmCheckTaskIssueUser.getUserId());
                apiHouseQmCheckTaskIssueMemberRspVo.setTaskId(houseQmCheckTaskIssueUser.getTaskId());
                apiHouseQmCheckTaskIssueMemberRspVo.setRoleType(houseQmCheckTaskIssueUser.getRoleType());
                apiHouseQmCheckTaskIssueMemberRspVo.setIssueUuid(houseQmCheckTaskIssueUser.getIssueUuid());
                apiHouseQmCheckTaskIssueMemberRspVo.setUpdateAt(DateToInt(houseQmCheckTaskIssueUser.getUpdateAt()));
                apiHouseQmCheckTaskIssueMemberRspVo.setDeleteAt(houseQmCheckTaskIssueUser.getDeleteAt() == null ? 0 : DateToInt(houseQmCheckTaskIssueUser.getDeleteAt()));
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
        Integer userId = 6;
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
        if (areaIds.size()==0||areaTypes.size()==0)return null;
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
