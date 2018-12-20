package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.service.ITaskListService;
import com.longfor.longjian.houseqm.app.vo.TaskList2Vo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import com.longfor.longjian.houseqm.app.vo.TaskRoleListVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    ITaskListService taskListService;

    /**
     * 获取项目下任务列表任务信息
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param status
     * @return
     */

    @GetMapping(value = "list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<TaskList2Vo> list(@RequestParam(value = "project_id") Integer projectId,
                                          @RequestParam(value = "category_cls") Integer categoryCls,
                                          @RequestParam(value = "page_level") String pageLevel,
                                          @RequestParam(value = "group_id") String groupId,
                                          @RequestParam(value = "team_id") Integer teamId,
                                          @RequestParam(value = "status") Integer status) {

        ////Todo uid
        Integer uid = null;
        ////Todo 根据uid、project_id判断权限

        TaskList2Vo taskListVo = taskListService.list(teamId, projectId, categoryCls, status);
        TaskResponse<TaskList2Vo> taskResponse = new TaskResponse<>();
        taskResponse.setMessage("success");
        taskResponse.setMsg("success");
        taskResponse.setData(taskListVo);
        return taskResponse;
    }

    /**
     * 获取任务角色列表
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param status
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<TaskRoleListVo> taskRole(@RequestParam(value = "project_id") Integer projectId,
                                                 @RequestParam(value = "category_cls") String categoryCls,
                                                 @RequestParam(value = "page_level") String pageLevel,
                                                 @RequestParam(value = "group_id") String groupId,
                                                 @RequestParam(value = "team_id") String teamId,
                                                 @RequestParam(value = "status") String status) {


        return null;
    }
}
