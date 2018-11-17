package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.houseqm.app.vo.TaskResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * http://192.168.37.159:3000/project/8/interface/api/272 获取任务区域信息
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("buildingqm/v3/papi/task/checked_areas/")
@Slf4j
public class TaskCheckedAreasController {



    /**
     *
     * 获取任务区域信息
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param taskId
     * @return
     */
    @MockOperation
    @GetMapping(value = "checked_areas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public TaskResponse<String> doAction(@RequestParam(value="project_id" ) Integer projectId,
                                                 @RequestParam(value="category_cls") String categoryCls,
                                                 @RequestParam(value="page_level") String pageLevel,
                                                 @RequestParam(value="group_id") String groupId,
                                                 @RequestParam(value="team_id") String teamId,
                                                 @RequestParam(value="task_id") String taskId){


        return null;
    }
}
