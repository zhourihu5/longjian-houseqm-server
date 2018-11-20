package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.req.UpdateDeviceReq;
import com.longfor.longjian.houseqm.app.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     *
     * @param deviceReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "buildingqm/my_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<TaskListVo> myTaskList(DeviceReq deviceReq){
        return null;
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
     * @param deviceReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "buildingqm/task_squads_members", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskMemberListVo> taskSquadsMembers(DeviceReq deviceReq){
        return null;
    }

    /**
     *
     * @param deviceReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "buildingqm/my_issue_patch_list/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<MyIssuePatchListVo> myIssuePatchList(DeviceReq deviceReq){
        return null;
    }

}
