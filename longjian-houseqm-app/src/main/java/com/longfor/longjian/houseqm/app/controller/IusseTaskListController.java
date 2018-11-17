package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * http://192.168.37.159:3000/project/8/interface/api/282  获取可用于检索的任务列表
 * http://192.168.37.159:3000/project/8/interface/api/1524 项目下季度检查获取任务列表
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("houseqm/v3/papi/issue/")
@Slf4j
public class IusseTaskListController {


    /**
     * 获取可用于检索的任务列表
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<TaskListVo> doAction(@RequestParam(value="project_id" ) Integer projectId,
                                             @RequestParam(value="category_cls") String categoryCls,
                                             @RequestParam(value="page_level") String pageLevel,
                                             @RequestParam(value="task_id") String taskId,
                                             @RequestParam(value="team_id") String teamId
                                             ){


        return null;
    }
}
