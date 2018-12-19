package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.TaskVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param taskId
     * @return
     */
    @MockOperation
    @GetMapping(value = "view", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskVo> view(@RequestParam(value = "project_id") Integer projectId,
                                       @RequestParam(value = "category_cls") String categoryCls,
                                       @RequestParam(value = "page_level") String pageLevel,
                                       @RequestParam(value = "group_id") String groupId,
                                       @RequestParam(value = "team_id") String teamId,
                                       @RequestParam(value = "task_id") String taskId) {


        return null;
    }
}
