package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.TaskDeleteReq;
import com.longfor.longjian.houseqm.app.req.TaskTaskRoleReq;
import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRoleListRspVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.app.vo.TaskTaskRoleRspVo;
import com.longfor.longjian.houseqm.po.User;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * http://192.168.37.159:3000/project/8/interface/api/266  任务编辑获取任务信息
 *
 * @author lipeishuai
 * @date 2018/11/19 18:25
 */

@RestController
@RequestMapping("oapi/v3/houseqm/task/")
@Slf4j
public class TaskController {
    @Resource
    ITaskService taskService;

    @GetMapping(value = "view/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseQmCheckTaskRspVo> view(@RequestParam(value = "project_id") Integer projectId,
                                                      @RequestParam(value = "task_id") Integer taskId) {

        HouseQmCheckTaskRspVo houseQmCheckTaskRspVo = taskService.getHouseQmCheckTaskByProjTaskId(projectId, taskId);
        LjBaseResponse<HouseQmCheckTaskRspVo> response = new LjBaseResponse<HouseQmCheckTaskRspVo>();
        response.setData(houseQmCheckTaskRspVo);

        return response;
    }

    /*
     * @Author hy
     * @Description 删除任务
     * http://192.168.37.159:3000/project/8/interface/api/3204
     *  SELECT * FROM user_in_house_qm_check_task WHERE project_id=930 AND task_id=86430885
        INSERT INTO user_in_house_qm_check_task (task_id,project_id,squad_id,user_id,role_type) VALUES(86430885,930,6554,20225,10)
     * SELECT * FROM house_qm_check_task_issue WHERE project_id=930 AND task_id=86430885
        insert into house_qm_check_task_issue (project_id,task_id,title)VALUES(930,86430885,'')
        SELECT * FROM house_qm_check_task WHERE project_id=930 AND task_id=86430885
        insert into house_qm_check_task (project_id,task_id)VALUES(930,86430885)
     * @Date 11:33 2019/1/9
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse
     **/
    @PostMapping(value = "delete/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse delete(@Valid TaskDeleteReq req) {
        //todo 鉴权 _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.任务管理.删除", "项目.工程检查.任务管理.删除"})
        LjBaseResponse response = new LjBaseResponse();
        try {
            taskService.deleteHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    /*
     * @Author hy
     * @Description 根据任务ID获取角色列表
     * @Date 13:45 2019/1/9
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.TaskTaskRoleRspVo>
     **/
    @GetMapping(value = "task_role/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskTaskRoleRspVo> taskRole(@Valid TaskTaskRoleReq req) {
        //todo 鉴权 _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.任务管理.查看", "项目.工程检查.任务管理.查看"})

        List<UserInHouseQmCheckTask> res = taskService.searchUserInKeyHouseQmCheckTaskByTaskId(req.getTask_id());
        log.debug("task_role --->" + res);
        List<Integer> uids = res.stream().map(UserInHouseQmCheckTask::getUserId).collect(Collectors.toList());
        Map<Integer, User> userMap = taskService.getUsersByIds(uids);
        LjBaseResponse<TaskTaskRoleRspVo> response = new LjBaseResponse<>();
        List<HouseQmCheckTaskRoleListRspVo> role_list = Lists.newArrayList();
        for (UserInHouseQmCheckTask item : res) {
            HouseQmCheckTaskRoleListRspVo role = new HouseQmCheckTaskRoleListRspVo();
            role.setId(item.getId());
            role.setUser_id(item.getUserId());
            role.setSquad_id(item.getSquadId());
            role.setRole_type(item.getRoleType());
            role.setCan_approve(item.getCanApprove());
            role.setTask_id(item.getTaskId());
            role.setReal_name("");
            if (userMap.containsKey(role.getUser_id())) {
                role.setReal_name(userMap.get(role.getUser_id()).getRealName());
            }
            role_list.add(role);
        }
        TaskTaskRoleRspVo data = new TaskTaskRoleRspVo();
        data.setRole_list(role_list);
        response.setData(data);
        response.setResult(0);
        return response;
    }
}
