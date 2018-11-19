package com.longfor.longjian.houseqm.app.controller;

import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.vo.*;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * http://192.168.37.159:3000/project/8/interface/api/298  项目/任务检查人员统计
 * http://192.168.37.159:3000/project/8/interface/api/306  项目任务信息汇总
 * http://192.168.37.159:3000/project/8/interface/api/310  获取区域列表
 * http://192.168.37.159:3000/project/8/interface/api/314  获取任务详情-总体情况
 * http://192.168.37.159:3000/project/8/interface/api/318  获取整改追踪信息
 * http://192.168.37.159:3000/project/8/interface/api/322  获取区域下任务信息
 *
 * @author lipeishuai
 * @date 2018/11/17 15:07
 */

@RestController
@RequestMapping("oapi/v3/houseqm/stat/")
@Slf4j
public class HouseqmStatController {


    /**
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskIds
     * @param groupId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_situation_daily", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskSituationDaily(@RequestParam(value="project_id" ) Integer projectId,
                                                                 @RequestParam(value="category_cls") String categoryCls,
                                                                 @RequestParam(value="page_level") String pageLevel,
                                                                 @RequestParam(value="team_id") String teamId,
                                                                 @RequestParam(value="task_ids") String taskIds,
                                                                 @RequestParam(value="group_id") String groupId,
                                                                 @ApiParam(value = "当前页码", required = false)
                                                        @Valid @Min(0)
                                                        @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNum,
                                                                 @ApiParam(value = "分页大小", required = false)
                                                        @Valid @Min(1)
                                                        @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize
    ){


        return null;
    }

    /**
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskIds
     * @param groupId
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_situation_overall", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectOveralListVo> taskSituationOverall(@RequestParam(value="project_id" ) Integer projectId,
                                                                  @RequestParam(value="category_cls") String categoryCls,
                                                                  @RequestParam(value="page_level") String pageLevel,
                                                                  @RequestParam(value="team_id") String teamId,
                                                                  @RequestParam(value="task_ids") String taskIds,
                                                                  @RequestParam(value="group_id") String groupId){


        return null;
    }

    /**
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskId
     * @param groupId
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_area_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskAreaListVo> taskAreaList(@RequestParam(value="project_id" ) Integer projectId,
                                                     @RequestParam(value="category_cls") String categoryCls,
                                                     @RequestParam(value="page_level") String pageLevel,
                                                     @RequestParam(value="team_id") String teamId,
                                                     @RequestParam(value="task_id") String taskId,
                                                     @RequestParam(value="group_id") String groupId){


        return null;
    }

    @MockOperation
    @GetMapping(value = "area_situation_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskAreaListVo> areaSituationTaskList(@RequestParam(value="project_id" ) Integer projectId,
                                                       @RequestParam(value="category_cls") String categoryCls,
                                                       @RequestParam(value="page_level") String pageLevel,
                                                       @RequestParam(value="team_id") String teamId,
                                                       @RequestParam(value="area_id") String areaId,
                                                       @RequestParam(value="group_id") String groupId){


        return null;
    }



    /**
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskId
     * @param groupId
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskStatVo> taskDetail(@RequestParam(value="project_id" ) Integer projectId,
                                                 @RequestParam(value="category_cls") String categoryCls,
                                                 @RequestParam(value="page_level") String pageLevel,
                                                 @RequestParam(value="team_id") String teamId,
                                                 @RequestParam(value="task_id") String taskId,
                                                 @RequestParam(value="group_id") String groupId){


        return null;
    }




    /**
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskId
     * @param groupId
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_situation_repair_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskRepairStatVo> taskSituationRepairStat(@RequestParam(value="project_id" ) Integer projectId,
                                                                    @RequestParam(value="category_cls") String categoryCls,
                                                                    @RequestParam(value="page_level") String pageLevel,
                                                                    @RequestParam(value="team_id") String teamId,
                                                                    @RequestParam(value="task_id") String taskId,
                                                                    @RequestParam(value="group_id") String groupId){


        return null;
    }
}
