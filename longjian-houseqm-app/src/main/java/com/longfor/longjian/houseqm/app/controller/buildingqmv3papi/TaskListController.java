package com.longfor.longjian.houseqm.app.controller.buildingqmv3papi;

import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.houseqm.app.req.tasklist.TaskListListReq;
import com.longfor.longjian.houseqm.app.req.tasklist.TaskRoleReq;
import com.longfor.longjian.houseqm.app.service.ITaskListService;
import com.longfor.longjian.houseqm.app.utils.CtrlToolUtils;
import com.longfor.longjian.houseqm.app.vo.TaskList2Vo;
import com.longfor.longjian.houseqm.app.vo.TaskRoleListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * http://192.168.37.159:3000/project/8/interface/api/226  获取项目下任务列表任务信息
 * http://192.168.37.159:3000/project/8/interface/api/280  获取任务角色列表
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("buildingqm/v3/papi/task/")
@Slf4j
public class TaskListController {


    @Resource
    private ITaskListService taskListService;
    @Resource
    private CtrlTool ctrlTool;

    /**
     * 获取项目下任务列表任务信息
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskList2Vo> list(HttpServletRequest request, @Valid TaskListListReq req) {
        log.info("team_id=" + req.getTeam_id() + ", project_id=" + req.getProject_id() + ", category_cls=" + req.getCategory_cls() + ", status=" + req.getStatus());
        LjBaseResponse<TaskList2Vo> taskResponse = new LjBaseResponse<>();
        try {
            ctrlTool.projPerm(request, "项目.工程检查.任务管理.查看");
            TaskList2Vo taskListVo = taskListService.list(req.getTeam_id(), req.getProject_id(), req.getCategory_cls(), req.getStatus());
            taskResponse.setMessage("success");
            taskResponse.setData(taskListVo);
        } catch (Exception e) {
            log.error("获取项目下任务列表任务信息error:", e.getMessage());
            CtrlToolUtils.errorReturn(taskResponse,e);
        }
        return taskResponse;
    }

    /**
     * 获取任务角色列表
     *
     * @param req
     * @return
     */
    @RequestMapping(value = "task_role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskRoleListVo> taskRole(HttpServletRequest request, @Valid TaskRoleReq req) {
        LjBaseResponse<TaskRoleListVo> taskResponse = new LjBaseResponse<>();
        log.info("task_role, project_id=" + req.getProject_id() + ", task_id=" + req.getTask_id());
        try {
            ctrlTool.projPerm(request, "项目.工程检查.任务管理.查看");
            TaskRoleListVo roleListVos = taskListService.taskRole(req.getTask_id());
            taskResponse.setData(roleListVos);
        } catch (Exception e) {
            log.error("获取任务角色列表error:", e.getMessage());
            CtrlToolUtils.errorReturn(taskResponse,e);
        }
        return taskResponse;
    }
}
