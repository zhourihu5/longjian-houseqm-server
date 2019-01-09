package com.longfor.longjian.houseqm.app.controller;

import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.github.pagehelper.util.StringUtil;
import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.StatHouseqmCompleteDailyReq;
import com.longfor.longjian.houseqm.app.req.StatHouseqmTaskSituationOverallReq;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.MathUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/298  项目/任务检查人员统计
 * <p>
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
    IHouseqmStatService houseqmStatService;

    @Resource
    IHouseqmStatisticService houseqmStatisticService;

    /**
     * 项目/任务检查人员统计
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param groupId
     * @param teamId
     * @param taskIds
     * @return
     */
    @GetMapping(value = "stat_houseqm/checker_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CheckerStatListVo> checkerStat(@RequestParam(value = "project_id") Integer projectId,
                                                         @RequestParam(value = "category_cls") String categoryCls,
                                                         @RequestParam(value = "page_level") String pageLevel,
                                                         @RequestParam(value = "group_id") String groupId,
                                                         @RequestParam(value = "team_id") String teamId,
                                                         @RequestParam(value = "task_ids") String taskIds) {
        ////TODO 鉴权  _, _, err := ctrl_tool.ProjPermMulti(c, []string{
        List<Integer> taskIdList = StringSplitToListUtil.splitToIdsComma(taskIds, ",");
        CheckerStatListVo checkerStatListVo = houseqmStatService.searchCheckerIssueStatisticByProjIdAndTaskId(projectId, taskIdList);
        LjBaseResponse<CheckerStatListVo> lbrsp = new LjBaseResponse<>();
        lbrsp.setData(checkerStatListVo);
        return lbrsp;
    }

    /**
     * 项目任务进度统计信息
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
    @GetMapping(value = "stat/task_situation_daily", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskSituationDaily(@RequestParam(value = "project_id") Integer projectId,
                                                                 @RequestParam(value = "category_cls") String categoryCls,
                                                                 @RequestParam(value = "page_level") String pageLevel,
                                                                 @RequestParam(value = "team_id") String teamId,
                                                                 @RequestParam(value = "task_ids") String taskIds,
                                                                 @RequestParam(value = "group_id") String groupId,
                                                                 @ApiParam(value = "当前页码", required = false)
                                                                 @Valid @Min(0)
                                                                 @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNum,
                                                                 @ApiParam(value = "分页大小", required = false)
                                                                 @Valid @Min(1)
                                                                 @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize
    ) {
        ////TODO 鉴权 _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})
        List<Integer> taskIdList = StringSplitToListUtil.splitToIdsComma(taskIds, ",");
        ProjectDailyListVo pdv = houseqmStatService.searchTaskSituationDailyByProjTaskIdInOnPage(projectId, taskIdList, pageNum, pageSize);
        LjBaseResponse<ProjectDailyListVo> lbrsp = new LjBaseResponse<>();
        lbrsp.setData(pdv);
        return lbrsp;
    }

    /**
     * 项目任务信息汇总 统计-任务汇总-汇总
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskIds
     * @param groupId
     * @return
     */
    @GetMapping(value = "stat/task_situation_overall", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectOveralListVo> taskSituationOverall(@RequestParam(value = "project_id") Integer projectId,
                                                                    @RequestParam(value = "category_cls") String categoryCls,
                                                                    @RequestParam(value = "page_level") String pageLevel,
                                                                    @RequestParam(value = "team_id") String teamId,
                                                                    @RequestParam(value = "task_ids") String taskIds,
                                                                    @RequestParam(value = "group_id") String groupId) {
        ////TODO _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})

        List<Integer> taskIdList = StringSplitToListUtil.splitToIdsComma(taskIds, ",");
        ProjectOveralListVo projectOveralListVo = new ProjectOveralListVo();
        ProjectOveralListVo.ProjectOveralVo totalStat = projectOveralListVo.new ProjectOveralVo();
        ArrayList<ProjectOveralListVo.ProjectOveralVo> items = Lists.newArrayList();
        totalStat.setTask_name("合计");
        totalStat.setIssue_count(0);
        totalStat.setRecords_count(0);
        totalStat.setChecked_count(0);
        for (Integer taskId : taskIdList) {
            ProjectOveralListVo.ProjectOveralVo item = houseqmStatService.getInspectTaskStatByProjTaskId(projectId, taskId);
            totalStat.setChecked_count(totalStat.getChecked_count() + item.getChecked_count());
            totalStat.setIssue_count(totalStat.getIssue_count() + item.getIssue_count());
            totalStat.setRecords_count(totalStat.getRecords_count() + item.getRecords_count());
            items.add(item);
        }
        items.add(totalStat);
        projectOveralListVo.setItems(items);
        LjBaseResponse<ProjectOveralListVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(projectOveralListVo);
        return ljbr;
    }

    /**
     * 获取区域列表
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskId
     * @param groupId
     * @return
     */
    @GetMapping(value = "stat/task_area_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskAreaListVo> taskAreaList(@RequestParam(value = "project_id") Integer projectId,
                                                       @RequestParam(value = "category_cls") String categoryCls,
                                                       @RequestParam(value = "page_level") String pageLevel,
                                                       @RequestParam(value = "team_id") String teamId,
                                                       @RequestParam(value = "task_id") Integer taskId,
                                                       @RequestParam(value = "group_id") String groupId) {
        ////TODO _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})
        TaskAreaListVo talv = houseqmStatService.searchAreasByProjTaskIdTyp(projectId, taskId);
        LjBaseResponse<TaskAreaListVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(talv);
        return ljbr;
    }

    /**
     * 获取区域下任务信息
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param areaId
     * @param groupId
     * @return
     */
    @GetMapping(value = "stat/area_situation_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<AreaTaskListVo> areaSituationTaskList(@RequestParam(value = "project_id") Integer projectId,
                                                                @RequestParam(value = "category_cls") Integer categoryCls,
                                                                @RequestParam(value = "page_level") String pageLevel,
                                                                @RequestParam(value = "team_id") String teamId,
                                                                @RequestParam(value = "area_id") Integer areaId,
                                                                @RequestParam(value = "group_id") String groupId) {
        ////TODO _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})
        List<Integer> list = Lists.newArrayList();
        list.add(categoryCls);
        AreaTaskListVo areaTaskListVo = houseqmStatService.searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(projectId, areaId, list);
        LjBaseResponse<AreaTaskListVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(areaTaskListVo);
        return ljbr;
    }


    /**
     * 获取任务详情-总体情况
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskId
     * @param groupId
     * @return
     */
    @GetMapping(value = "stat/task_detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskStatVo> taskDetail(@RequestParam(value = "project_id") Integer projectId,
                                                 @RequestParam(value = "category_cls") String categoryCls,
                                                 @RequestParam(value = "page_level") String pageLevel,
                                                 @RequestParam(value = "team_id") String teamId,
                                                 @RequestParam(value = "task_id") Integer taskId,
                                                 @RequestParam(value = "group_id") String groupId,
                                                 @RequestParam(value = "area_id") Integer areaId) {
        ////TODO _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})
        TaskStatVo.IssueStatVo issue = houseqmStatisticService.getCheckTaskIssueTypeStatByTaskIdAreaId(taskId, areaId);
        TaskStatVo.HouseStatVo house = houseqmStatisticService.getHouseQmCheckTaskHouseStatByTaskId(projectId, taskId, areaId);
        house.setHouse_checked_percent(MathUtil.getPercentage(house.getChecked_count(), house.getHouse_count()));
        house.setHouse_repaired_percent(MathUtil.getPercentage(house.getRepaired_count(), house.getHas_issue_count()));
        house.setHouse_approveded_percent(MathUtil.getPercentage(house.getApproved_count(), house.getRepaired_count()));
        LjBaseResponse<TaskStatVo> ljbr = new LjBaseResponse<>();
        TaskStatVo taskStatVo = new TaskStatVo();
        taskStatVo.setIssue(issue);
        taskStatVo.setHouse(house);
        ljbr.setData(taskStatVo);
        return ljbr;
    }


    /**
     * 获取整改追踪信息
     *
     * @param projectId
     * @param categoryCls
     * @param pageLevel
     * @param teamId
     * @param taskId
     * @param groupId
     * @return
     */
    @GetMapping(value = "stat/task_situation_repair_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskRepairStatVo> taskSituationRepairStat(@RequestParam(value = "project_id") Integer projectId,
                                                                    @RequestParam(value = "category_cls") String categoryCls,
                                                                    @RequestParam(value = "page_level") String pageLevel,
                                                                    @RequestParam(value = "team_id") String teamId,
                                                                    @RequestParam(value = "task_id") Integer taskId,
                                                                    @RequestParam(value = "group_id") String groupId) {
        ////TODO _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})
        Date t = null;
        try {
            t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("0001-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TaskRepairStatVo taskRepairStatVo = houseqmStatisticService.searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(projectId, taskId, 0, t, t);
        LjBaseResponse<TaskRepairStatVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(taskRepairStatVo);
        return ljbr;
    }

    // todo 待测试
    /*
     * 统计-验房统计-任务总进度及交付情况
     * @Author hy
     * @Description
     * @Date 20:12 2019/1/8
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.StatHouseqmTaskSituationOverallRspVo>
     **/
    @GetMapping(value = "stat_houseqm/task_situation_overall", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatHouseqmTaskSituationOverallRspVo> taskSituationOverall(@RequestBody @Valid StatHouseqmTaskSituationOverallReq req) {


        //todo  鉴权  _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})
        //	if err != nil {
        //		log.Error(err.Error())
        //		return err
        //	}
        List<Integer> taskIds = StringSplitToListUtil.strToInts(req.getTask_ids(), ",");
        RepossessionTasksStatusInfoVo info = houseqmStatisticService.getRepossessionTasksStatusInfo(req.getProject_id(), taskIds, 0);

        LjBaseResponse<StatHouseqmTaskSituationOverallRspVo> response = new LjBaseResponse<>();
        response.setResult(0);
        StatHouseqmTaskSituationOverallRspVo data = new StatHouseqmTaskSituationOverallRspVo();
        HouseQmHouseQmStatTaskSituationOverallRspVo status = new HouseQmHouseQmStatTaskSituationOverallRspVo();
        status.setAccept_has_issue_count(info.getAcceptHasIssueCount());
        status.setAccept_has_issue_sign_count(info.getAcceptHasIssueSignCount());
        status.setAccept_no_issue(info.getAcceptNoIssueCount());
        status.setAccept_no_issue_sign_count(info.getAcceptNoIssueSignCount());
        status.setChecked_count(info.getCheckedCount());
        status.setChecked_rate(info.getCheckedRate());
        status.setOnly_watch(info.getOnlyWatch());
        status.setReject_count(info.getRejectCount());
        status.setTask_name(info.getTaskName());
        status.setTotal(info.getTotal());
        status.setUnaccept_count(info.getUnacceptCount());
        status.setUnchecked_count(info.getUncheckedCount());
        data.setStatus(status);
        response.setData(data);
        return response;
    }

    // todo 待测试
    /*
     * 统计-验房统计-每天的交付数
     * @Author hy
     * @Description
     * @Date 20:16 2019/1/8
     * @Param
     * @return
     **/
    @GetMapping(value = "stat_houseqm/complete_daily", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatHouseqmCompleteDailyRspVo> completeDaily(@Valid StatHouseqmCompleteDailyReq req) {

        // todo  鉴权   _, _, err := ctrl_tool.ProjPermMulti(c, []string{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"})

        List<Integer> taskIds = StringSplitToListUtil.strToInts(req.getTask_ids(), ",");

        int beginOn = 0;
        int endOn = 0;
        if (req.getBegin_on().length() > 0) {
            beginOn = DateUtil.datetimeToTimeStamp(DateUtil.strToDate(req.getBegin_on(), "yyyy-MM-dd hh:mm:ss"));
        }
        if (req.getEnd_on().length() > 0) {
            endOn = DateUtil.datetimeToTimeStamp(DateUtil.strToDate(req.getEnd_on(), "yyyy-MM-dd hh:mm:ss"));
        }

        StatHouseqmCompleteDailyRspVo data=houseqmStatisticService.searchRepossessionStatusCompleteDaily(req.getProject_id(), taskIds, beginOn, endOn, req.getPage(), req.getPage_size());
        LjBaseResponse<StatHouseqmCompleteDailyRspVo> response = new LjBaseResponse<>();
        response.setData(data);

        return response;
    }

}
