package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.entity.UserBase;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.DeviceReq;
import com.longfor.longjian.houseqm.app.req.TaskEditReq;
import com.longfor.longjian.houseqm.app.req.TaskReq;
import com.longfor.longjian.houseqm.app.req.UpdateDeviceReq;
import com.longfor.longjian.houseqm.app.req.buildingqm.MyIssuePatchListReq;
import com.longfor.longjian.houseqm.app.service.IBuildingqmService;
import com.longfor.longjian.houseqm.app.service.ICheckUpdateService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.rmi.server.UID;
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
    private IBuildingqmService buildingqmService;
    @Resource
    private ICheckUpdateService iCheckUpdateService;
    @Resource
    private SessionInfo sessionInfo;

    /**
     * 项目下获取我的任务列表
     * http://192.168.37.159:3000/project/8/interface/api/626
     *
     * @return
     */
    @RequestMapping(value = "buildingqm/my_task_list/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<TaskListVo> myTaskList() {
        log.info("my_task_list");
        //// TODO: 2018/11/24 uid = session['uid']
        int uid = 7566;
        TaskListVo vo = buildingqmService.myTaskList(uid);
        TaskResponse<TaskListVo> response = new TaskResponse();
        response.setResult(0);
        response.setMessage("success");
        response.setData(vo);
        return response;
    }

    /**
     * 检查任务更新
     * http://192.168.37.159:3000/project/8/interface/api/658
     *
     * @param updateDeviceReq
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.TaskIssueListVo>
     * @author hy
     * @date 2018/12/25 0025
     */
    @GetMapping(value = "check_update/check/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskIssueListVo> check(@Valid UpdateDeviceReq updateDeviceReq) {

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
            }
        } else {
            item.setIssue_members(0);
        }

        Date taskLastUpdateTime = iCheckUpdateService.getHouseQmCheckTaskLastUpdateTime(updateDeviceReq.getTask_id());
        if (taskLastUpdateTime.after(taskMembersUpdateTime)) {
            item.setTask_members(1);
        } else {
            item.setTask_members(0);
        }
        LjBaseResponse<TaskIssueListVo> respone = new LjBaseResponse<>();
        taskIssueListVo.setItem(item);
        respone.setData(taskIssueListVo);
        return respone;
    }

    /**
     * http://192.168.37.159:3000/project/8/interface/api/670  获取任务角色列表
     * //@RequestParam(value = "device_id") String deviceId,@RequestParam(value = "token") String token
     *
     * @param taskIds
     * @return
     */
    @GetMapping(value = "buildingqm/task_squads_members/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskMemberListVo> taskSquadsMembers(@RequestParam(value = "task_ids", required = true) String taskIds) {
        log.info("task_squads_members, task_id= " + taskIds);
        LjBaseResponse<TaskMemberListVo> vos = new LjBaseResponse<>();
        TaskMemberListVo vo = buildingqmService.taskSquadsMembers(taskIds);
        vos.setData(vo);
        return vos;
    }

    /**
     * 补全与我相关问题信息
     * http://192.168.37.159:3000/project/8/interface/api/678
     *
     * @param req
     * @return
     */
    @GetMapping(value = "buildingqm/my_issue_patch_list/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<MyIssuePatchListVo> myIssuePatchList(MyIssuePatchListReq req) {
        log.info("my_issue_patch_list, task_id= " + req.getTask_id() + ", timestamp= " + req.getTimestamp());
        //// Todo: uid = session['uid']
        Integer uid = 9;
        //int uid = (int) sessionInfo.getBaseInfo("uid");
        MyIssuePatchListVo miplv = buildingqmService.myIssuePathList(uid, req.getTask_id(), req.getTimestamp());
        LjBaseResponse<MyIssuePatchListVo> ljBaseResponse = new LjBaseResponse<>();
        ljBaseResponse.setData(miplv);
        return ljBaseResponse;
    }

    /**
     * 项目下创建任务
     *
     * @param taskReq
     * @return
     */
    @PostMapping(value = "task/create/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse create(TaskReq taskReq) {
        log.info("create, project_id=" + taskReq.getProject_id() + "" +
                " name=" + taskReq.getName() + ", " +
                "category_cls=" + taskReq.getCategory_cls() + "," +
                " root_category_key=" + taskReq.getRoot_category_key() + "," +
                " area_ids=" + taskReq.getArea_ids() + ", area_types=" + taskReq.getArea_types() + "," +
                " plan_begin_on=" + taskReq.getPlan_begin_on() + "," +
                " plan_end_on=" + taskReq.getPlan_end_on() + ", " +
                "repairer_ids=" + taskReq.getRepairer_ids() + ", " +
                "checker_groups=" + taskReq.getChecker_groups() + ", " +
                "repairer_refund_permission=" + taskReq.getRepairer_refund_permission() + "," +
                " repairer_follower_permission=" + taskReq.getRepairer_follower_permission() + ", " +
                "checker_approve_permission=" + taskReq.getChecker_approve_permission() + ", " +
                "repaired_picture_status=" + taskReq.getRepaired_picture_status() + ", " +
                "issue_desc_status=" + taskReq.getIssue_desc_status() + ", " +
                "issue_default_desc=" + taskReq.getIssue_default_desc() + "," +
                " push_strategy_config=" + taskReq.getPush_strategy_config() + "");
        ////todo c从session中获取uid
/*
        uid = session['uid']
*/

        Integer uid = 1;
        ////todo 鉴权
       /* has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.任务管理.新增')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        buildingqmService.create(uid, taskReq);
        LjBaseResponse<Object> response = new LjBaseResponse<>();
        return response;
    }

    /**
     * 项目下获取检查组信息
     *
     * @param projectId
     * @param taskId
     * @return
     */
    @GetMapping(value = "task/task_squad/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ArrayList<HouseQmCheckTaskSquadListRspVo>> taskSquad(@RequestParam(name = "project_id", required = true) String projectId,
                                                                               @RequestParam(name = "task_id", required = true) String taskId) {
        ////todo c从session中获取uid
/*
        uid = session['uid']
*/
        Integer uid = 1;
        ////todo 鉴权
       /* has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.任务管理.新增')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/
        List<HouseQmCheckTaskSquad> info = buildingqmService.searchHouseqmCheckTaskSquad(projectId, taskId);
        ArrayList<HouseQmCheckTaskSquadListRspVo> squad_list = Lists.newArrayList();
        for (int i = 0; i < info.size(); i++) {
            HouseQmCheckTaskSquadListRspVo rspVo = new HouseQmCheckTaskSquadListRspVo();
            rspVo.setId(info.get(i).getId());
            rspVo.setName(info.get(i).getName());
            rspVo.setSquad_type(info.get(i).getSquadType());
            squad_list.add(rspVo);
        }
        LjBaseResponse<ArrayList<HouseQmCheckTaskSquadListRspVo>> response = new LjBaseResponse<>();
        response.setData(squad_list);
        return response;
    }

    /**
     * 项目下任务内容修改
     *
     * @param taskEditReq
     * @return
     */
    @PostMapping(value = "task/edit/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse edit(TaskEditReq taskEditReq) {
        log.info("edit, project_id=" + taskEditReq.getProject_id() + "" +
                " name=" + taskEditReq.getName() + ", " +
                "task_id=" + taskEditReq.getTask_id() + "," +
                " area_ids=" + taskEditReq.getArea_ids() + ", area_types=" + taskEditReq.getArea_types() + "," +
                " plan_begin_on=" + taskEditReq.getPlan_begin_on() + "," +
                " plan_end_on=" + taskEditReq.getPlan_end_on() + ", " +
                "repairer_ids=" + taskEditReq.getRepairer_ids() + ", " +
                "checker_groups=" + taskEditReq.getChecker_groups() + ", " +
                "repairer_refund_permission=" + taskEditReq.getRepairer_refund_permission() + "," +
                " repairer_follower_permission=" + taskEditReq.getRepairer_follower_permission() + ", " +
                "checker_approve_permission=" + taskEditReq.getChecker_approve_permission() + ", " +
                "repaired_picture_status=" + taskEditReq.getRepaired_picture_status() + ", " +
                "issue_desc_status=" + taskEditReq.getIssue_desc_status() + ", " +
                "issue_default_desc=" + taskEditReq.getIssue_default_desc() + "," +
                " push_strategy_config=" + taskEditReq.getPush_strategy_config() + "");
        ////todo c从session中获取uid
/*
        uid = session['uid']
*/
        Integer uid = 1;
        ////todo 鉴权
       /* has_per = ucenter_api.check_project_permission(uid, req.project_id, '项目.工程检查.任务管理.新增')
        if not has_per:
        rsp = errors_utils.err(rsp, 'PermissionDenied')*/

        buildingqmService.edit(uid, taskEditReq);
        LjBaseResponse<Object> response = new LjBaseResponse<>();
        return response;
    }

    /**
     * http://192.168.37.159:3000/project/8/interface/api/3288  上报验房报告数据
     * @param taskId
     * @param timestamp
     * @param issueUuid
     * @return
     */
    @GetMapping(value = "issue/issue_log_info/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ApiIssueLogVo> issueLogInfo(@RequestParam(name = "task_id", required = true) Integer taskId,
                                                                               @RequestParam(name = "timestamp", required = true) Integer timestamp,
                                                                                  @RequestParam(name = "issue_uuid",required = true)String issueUuid) {

        ApiIssueLogVo apiIssueLogVo=  buildingqmService.getIssueListLogByLastIdAndUpdataAt(taskId,timestamp,issueUuid);

        LjBaseResponse<ApiIssueLogVo> response = new LjBaseResponse<>();
        response.setData(apiIssueLogVo);
        return  response;

    }

    /**
     * http://192.168.37.159:3000/project/8/interface/api/3260  提交问题日志
     * @param projectId
     * @param data
     * @return
     */
    @GetMapping(value = "buildingqm/report_issue/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ReportIssueVo> reportIssue(@RequestParam(name = "project_id", required = true) String projectId,

                                                                     @RequestParam(name = "data", required = true) String data) {
        log.info("report_issue, project_id="+projectId+", data="+data+"" );
        //// todo   session 获取uid
       /* uid = session['uid']*/
        Integer uid=null;
        ReportIssueVo reportIssueVo=  buildingqmService.reportIssue(uid,projectId,data);
        LjBaseResponse<ReportIssueVo> response = new LjBaseResponse<>();
        response.setData(reportIssueVo);
        return  response;
    }






}
