package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.req.UpdateDeviceReq;
import com.longfor.longjian.houseqm.app.service.IBuildingqmService;
import com.longfor.longjian.houseqm.app.service.ICheckUpdateService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/626  项目下获取我的任务列表
 * http://192.168.37.159:3000/project/8/interface/api/658  检查任务更新
 * http://192.168.37.159:3000/project/8/interface/api/670  获取任务角色列表
 * http://192.168.37.159:3000/project/8/interface/api/678  补全与我相关问题信息
 *
 * @author lipeishuai
 * @date 2018/11/20 18:56
 */
@RestController
@RequestMapping("buildingqm/v3/papi/")
@Slf4j
public class BuildingqmController {


    @Resource
    IBuildingqmService buildingqmService;

    @Resource
    ICheckUpdateService iCheckUpdateService;

    /**
     * 项目下获取我的任务列表
     *
     * @param deviceId
     * @param token
     * @return
     */
    @GetMapping(value = "buildingqm/my_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<TaskListVo> myTaskList(@RequestParam(value = "device_id") String deviceId,
                                               @RequestParam(value = "token") String token) {
        //// TODO: 2018/11/24
        Integer uid = null;

        TaskListVo vo = buildingqmService.myTaskList(uid);
        TaskResponse<TaskListVo> response = new TaskResponse();
        response.setData(vo);

        return response;
    }

    /**
     * 检查任务更新
     *
     * @param updateDeviceReq
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.TaskIssueListVo>
     * @author hy
     * @date 2018/12/25 0025
     */
    @GetMapping(value = "check_update/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskIssueListVo> check(UpdateDeviceReq updateDeviceReq) {
        Date taskUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getTask_update_time(), "yyyy-MM-dd");
        Date issueUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getIssue_update_time(), "yyyy-MM-dd");
        Date issueLogUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getIssue_log_update_time(), "yyyy-MM-dd");
        Date taskMembersUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getTask_members_update_time(), "yyyy-MM-dd");
        Date issueMembersUpdateTime = DateUtil.timeStampToDate(updateDeviceReq.getIssue_members_update_time(), "yyyy-MM-dd");

        TaskIssueListVo taskIssueListVo = new TaskIssueListVo();
        TaskIssueListVo.TaskIussueVo item = taskIssueListVo.new TaskIussueVo();
        //获取最后更新时间
        Date lastUpdate = iCheckUpdateService.getHouseqmCheckTaskLastUpdateAtByTaskId(updateDeviceReq.getTask_id());
        if (lastUpdate != null && lastUpdate.after(taskUpdateTime)) {
            item.setTask(1);
        } else {
            item.setTask(0);
        }
        ////TODO uid = int(session['uid'])
        Integer uid = 7566;
        //  根据userid，taskid，issue_log_update_time查找最后一个issue单的id
        Integer newLastIssueId = iCheckUpdateService.getHouseqmCheckTaskIssueLastId(uid, updateDeviceReq.getTask_id(), issueUpdateTime);
        if (newLastIssueId > 0) {
            item.setIssue(1);
        } else {
            item.setIssue(0);
        }

        Integer newLastLogId = iCheckUpdateService.getHouseQmCheckTaskIssueLogLastId(uid, updateDeviceReq.getTask_id(), issueLogUpdateTime);
        if (newLastLogId > 0) {
            item.setIssue_log(1);
        } else {
            item.setIssue_log(0);
        }

        if (item.getIssue_log() == 1) {
            Date issueLastUpdateTime = iCheckUpdateService.getHouseQmCheckTaskIssueUserLastUpdateTime(updateDeviceReq.getTask_id());
            if (issueLastUpdateTime.after(issueMembersUpdateTime)) {
                item.setIssue_members(1);
            } else {
                item.setIssue_members(0);
            }
        }

        Date taskLastUpdateTime = iCheckUpdateService.getHouseQmCheckTaskLastUpdateTime(updateDeviceReq.getTask_id());
        if (taskLastUpdateTime.after(taskMembersUpdateTime)) {
            item.setTask_members(1);
        } else {
            item.setTask_members(0);
        }

        LjBaseResponse<TaskIssueListVo> ljbr = new LjBaseResponse<>();
        List<TaskIssueListVo.TaskIussueVo> list = Lists.newArrayList();
        list.add(item);
        taskIssueListVo.setItems(list);
        ljbr.setData(taskIssueListVo);
        return ljbr;
    }

    /**
     * @param deviceId
     * @param taskIds
     * @param token
     * @return
     */
    @GetMapping(value = "buildingqm/task_squads_members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskMemberListVo> taskSquadsMembers(@RequestParam(value = "device_id") String deviceId,
                                                              @RequestParam(value = "task_ids") String taskIds,
                                                              @RequestParam(value = "token") String token) {
        LjBaseResponse<TaskMemberListVo> vos = new LjBaseResponse<>();
        TaskMemberListVo vo = buildingqmService.taskSquadsMembers(taskIds);
        vos.setData(vo);
        return vos;
    }

    /**
     * 补全与我相关问题信息
     *
     * @param deviceReq
     * @return
     */
    @GetMapping(value = "buildingqm/my_issue_patch_list/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<MyIssuePatchListVo> myIssuePatchList(DeviceReq deviceReq) {
        //// Todo: 获取uid 为了测试改为0，
        Integer uid = 7556;
        //调用业务方法
        MyIssuePatchListVo miplv = buildingqmService.myIssuePathList(uid, deviceReq.getTask_id(), deviceReq.getTimestamp());
        LjBaseResponse<MyIssuePatchListVo> ljBaseResponse = new LjBaseResponse<>();
        ljBaseResponse.setData(miplv);
        return ljBaseResponse;
    }

}
