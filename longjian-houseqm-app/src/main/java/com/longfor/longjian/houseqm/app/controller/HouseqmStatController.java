package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.service.HouseqmStatService;
import com.longfor.longjian.houseqm.app.vo.*;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/298  项目/任务检查人员统计
 *
 * http://192.168.37.159:3000/project/8/interface/api/304 项目任务进度统计信息
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
@RequestMapping("oapi/v3/houseqm/")
@Slf4j
public class HouseqmStatController {

    @Resource
    HouseqmStatService houseqmStatService;

    /**
     * 项目/任务检查人员统计
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param taskIds
     * @return
     */
    @GetMapping(value = "stat_houseqm/checker_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CheckerStatListVo> checkerStat(@RequestParam(value="project_id" ) Integer projectId,
                                                         @RequestParam(value="category_cls") String categoryCls,
                                                         @RequestParam(value="page_level") String pageLevel,
                                                         @RequestParam(value="group_id") String groupId,
                                                         @RequestParam(value="team_id") String teamId,
                                                         @RequestParam(value="task_ids") String taskIds){
        String[] taskId = taskIds.split(",");
        List<Integer> taskIdList = Lists.newArrayList();
        for (String s : taskId) {
            taskIdList.add(Integer.parseInt(s));
        }
        CheckerStatListVo checkerStatListVo =houseqmStatService.searchCheckerIssueStatisticByProjIdAndTaskId(projectId,taskIdList);
        LjBaseResponse<CheckerStatListVo> lbrsp = new LjBaseResponse<>();
        lbrsp.setData(checkerStatListVo);
        return lbrsp;
    }

    /**
     *  项目任务进度统计信息
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
    @GetMapping(value = "stat/task_situation_daily", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        String[] taskId = taskIds.split(",");
        List<Integer> taskIdList = Lists.newArrayList();
        for (String s : taskId) {
            taskIdList.add(Integer.parseInt(s));
        }

        ProjectDailyListVo pdv=houseqmStatService.searchTaskSituationDailyByProjTaskIdInOnPage(projectId,taskIdList,pageNum,pageSize);
        LjBaseResponse<ProjectDailyListVo> lbrsp = new LjBaseResponse<>();
        lbrsp.setData(pdv);
        return lbrsp;
    }

    /**
     *  项目任务信息汇总
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskIds
     * @param groupId
     * @return
     */
    @GetMapping(value = "stat/task_situation_overall", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectOveralListVo> taskSituationOverall(@RequestParam(value="project_id" ) Integer projectId,
                                                                  @RequestParam(value="category_cls") String categoryCls,
                                                                  @RequestParam(value="page_level") String pageLevel,
                                                                  @RequestParam(value="team_id") String teamId,
                                                                  @RequestParam(value="task_ids") String taskIds,
                                                                  @RequestParam(value="group_id") String groupId){
        String[] strTaskId = taskIds.split(",");
        List<Integer> taskIdList = Lists.newArrayList();
        for (String s : strTaskId) {
            taskIdList.add(Integer.parseInt(s));
        }
        ProjectOveralListVo projectOveralListVo = new ProjectOveralListVo();
        ProjectOveralListVo.ProjectOveralVo totalStat =projectOveralListVo.new ProjectOveralVo();
        ArrayList<ProjectOveralListVo.ProjectOveralVo> items = Lists.newArrayList();
        totalStat.setTask_name("合计");
        totalStat.setIssue_count(0);
        totalStat.setRecords_count(0);
        totalStat.setChecked_count(0);
        for (Integer taskId : taskIdList) {
            ProjectOveralListVo.ProjectOveralVo item=houseqmStatService.getInspectTaskStatByProjTaskId(projectId,taskId);
            totalStat.setChecked_count(totalStat.getChecked_count()+item.getChecked_count());
            totalStat.setIssue_count(totalStat.getIssue_count()+item.getIssue_count());
            totalStat.setRecords_count(totalStat.getRecords_count()+item.getRecords_count());
            items.add(item);
        }
        items.add(totalStat);
        projectOveralListVo.setItems(items);
        LjBaseResponse<ProjectOveralListVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(projectOveralListVo);
        return ljbr;
    }

    /**
     *  获取区域列表
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskId
     * @param groupId
     * @return
     */
    @MockOperation
    @GetMapping(value = "stat/task_area_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskAreaListVo> taskAreaList(@RequestParam(value="project_id" ) Integer projectId,
                                                     @RequestParam(value="category_cls") String categoryCls,
                                                     @RequestParam(value="page_level") String pageLevel,
                                                     @RequestParam(value="team_id") String teamId,
                                                     @RequestParam(value="task_id") String taskId,
                                                     @RequestParam(value="group_id") String groupId){


        return null;
    }

    @MockOperation
    @GetMapping(value = "stat/area_situation_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @GetMapping(value = "stat/task_detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
    @GetMapping(value = "stat/task_situation_repair_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskRepairStatVo> taskSituationRepairStat(@RequestParam(value="project_id" ) Integer projectId,
                                                                    @RequestParam(value="category_cls") String categoryCls,
                                                                    @RequestParam(value="page_level") String pageLevel,
                                                                    @RequestParam(value="team_id") String teamId,
                                                                    @RequestParam(value="task_id") String taskId,
                                                                    @RequestParam(value="group_id") String groupId){


        return null;
    }
}
