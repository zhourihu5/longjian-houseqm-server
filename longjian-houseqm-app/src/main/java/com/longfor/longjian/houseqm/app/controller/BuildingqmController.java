package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.req.UpdateDeviceReq;
import com.longfor.longjian.houseqm.app.service.BuildingqmService;
import com.longfor.longjian.houseqm.app.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
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
    BuildingqmService buildingqmService;

    /**
     *
     * @param deviceId
     * @param token
     * @return
     */
    @MockOperation
    @GetMapping(value = "buildingqm/my_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<TaskListVo> myTaskList(@RequestParam(value="device_id" ) Integer deviceId,
                                               @RequestParam(value="token") String token){
        //// TODO: 2018/11/24
        Integer uid = null;

        TaskListVo vo = buildingqmService.myTaskList(uid);
        TaskResponse<TaskListVo> response = new TaskResponse();
        response.setData(vo);

        return response;
    }

    /**
     *
     * @param updateDeviceReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "check_update/check", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskIssueListVo> check(UpdateDeviceReq updateDeviceReq){
        return null;
    }

    /**
     *
     * @param deviceId
     * @param taskIds
     * @param token
     * @return
     */
    @MockOperation
    @GetMapping(value = "buildingqm/task_squads_members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskMemberListVo> taskSquadsMembers(@RequestParam(value="device_id" ) Integer deviceId,
                                                              @RequestParam(value="task_ids") String taskIds,
                                                              @RequestParam(value="token") String token){
        LjBaseResponse<TaskMemberListVo> vos = new LjBaseResponse<>();
        TaskMemberListVo vo = buildingqmService.taskSquadsMembers(taskIds);
        vos.setData(vo);
        return vos;
    }

    /**
     * 补全与我相关问题信息
     * @param deviceReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "buildingqm/my_issue_patch_list/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<MyIssuePatchListVo> myIssuePatchList(DeviceReq deviceReq){

        //// Todo: 获取uid
        Integer uid = null;
        //调用业务方法
        MyIssuePatchListVo miplv= buildingqmService.myIssuePathList(uid,deviceReq.getTask_id(),deviceReq.getTimestamp());
        LjBaseResponse<MyIssuePatchListVo> ljBaseResponse = new LjBaseResponse<>();
        ljBaseResponse.setData(miplv);
        return ljBaseResponse;
    }

}