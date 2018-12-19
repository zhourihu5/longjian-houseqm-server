package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.service.TaskService;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.app.vo.TaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
    TaskService taskService;

    @GetMapping(value = "view", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseQmCheckTaskRspVo> view(@RequestParam(value="project_id" ) Integer projectId,
                                                      @RequestParam(value="task_id") Integer taskId){

        HouseQmCheckTaskRspVo houseQmCheckTaskRspVo=  taskService.getHouseQmCheckTaskByProjTaskId(projectId, taskId);
        LjBaseResponse<HouseQmCheckTaskRspVo> response = new LjBaseResponse<HouseQmCheckTaskRspVo>();
        response.setData(houseQmCheckTaskRspVo);

        return response;
    }
}
