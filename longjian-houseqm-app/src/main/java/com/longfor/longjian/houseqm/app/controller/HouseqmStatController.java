package com.longfor.longjian.houseqm.app.controller;

import com.ctrip.framework.apollo.core.utils.StringUtils;
import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.req.*;
import com.longfor.longjian.houseqm.app.req.houseqmstat.*;
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
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private IHouseqmStatService houseqmStatService;
    @Resource
    private IHouseqmStatisticService houseqmStatisticService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;

    /**
     * 项目/任务检查人员统计
     *
     * @param req
     * @return
     */
    @GetMapping(value = "stat_houseqm/checker_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CheckerStatListVo> checkerStat(HttpServletRequest request, @Valid StatHouseqmCheckerStatReq req) {
        LjBaseResponse<CheckerStatListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
            return response;
        }
        try {
            List<Integer> taskIdList = StringSplitToListUtil.splitToIdsComma(req.getTask_ids(), ",");
            CheckerStatListVo checkerStatListVo = houseqmStatService.searchCheckerIssueStatisticByProjIdAndTaskId(req.getProject_id(), taskIdList);
            response.setData(checkerStatListVo);
        } catch (Exception e) {
            log.warn(e.getMessage());
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    /**
     * 项目任务进度统计信息
     *
     * @param req
     * @return
     */
    @GetMapping(value = "stat/task_situation_daily", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskSituationDaily(HttpServletRequest request, @Valid StatTaskSituationDailyReq req) {
        LjBaseResponse<ProjectDailyListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        try {
            List<Integer> taskIdList = StringUtil.strToInts(req.getTask_ids(), ",");
            ProjectDailyListVo pdv = houseqmStatService.searchTaskSituationDailyByProjTaskIdInOnPage(req.getProject_id(), taskIdList, req.getPage(), req.getPage_size());
            response.setData(pdv);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    /**
     * 项目任务信息汇总 统计-任务汇总-汇总
     *
     * @param req
     * @return
     */
    @GetMapping(value = "stat/task_situation_overall", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectOveralListVo> taskSituationOverall(HttpServletRequest request, @Valid StatTaskSituationOverallReq req) {
        LjBaseResponse<ProjectOveralListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        List<Integer> taskIdList = StringSplitToListUtil.splitToIdsComma(req.getTask_ids(), ",");
        ProjectOveralListVo projectOveralListVo = new ProjectOveralListVo();
        ProjectOveralListVo.ProjectOveralVo totalStat = projectOveralListVo.new ProjectOveralVo();
        ArrayList<ProjectOveralListVo.ProjectOveralVo> items = Lists.newArrayList();
        totalStat.setTask_name("合计");
        totalStat.setIssue_count(0);
        totalStat.setRecords_count(0);
        totalStat.setChecked_count(0);
        for (Integer taskId : taskIdList) {
            try {
                ProjectOveralListVo.ProjectOveralVo item = houseqmStatService.getInspectTaskStatByProjTaskId(req.getProject_id(), taskId);
                totalStat.setChecked_count(totalStat.getChecked_count() + item.getChecked_count());
                totalStat.setIssue_count(totalStat.getIssue_count() + item.getIssue_count());
                totalStat.setRecords_count(totalStat.getRecords_count() + item.getRecords_count());
                items.add(item);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn(e.getMessage());
                response.setMessage(e.getMessage());
                response.setResult(1);
            }
        }
        items.add(totalStat);
        projectOveralListVo.setItems(items);
        response.setData(projectOveralListVo);
        return response;
    }

    /**
     * 获取区域列表
     *
     * @param req
     * @return
     */
    @GetMapping(value = "stat/task_area_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskAreaListVo> taskAreaList(HttpServletRequest request, @Valid StatTaskAreaListReq req) {
        LjBaseResponse<TaskAreaListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        try {
            TaskAreaListVo talv = houseqmStatService.searchAreasByProjTaskIdTyp(req.getProject_id(), req.getTask_id(), req.getTyp());
            response.setData(talv);
        } catch (Exception e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    /**
     * 获取区域下任务信息
     *
     * @param req
     * @return
     */
    @GetMapping(value = "stat/area_situation_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<AreaTaskListVo> areaSituationTaskList(HttpServletRequest request, @Valid StatAreaSituationTaskListReq req) {
        LjBaseResponse<AreaTaskListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        try {
            AreaTaskListVo areaTaskListVo = houseqmStatService.searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(req.getProject_id(), req.getArea_id(), Arrays.asList(req.getCategory_cls()));
            response.setData(areaTaskListVo);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    /**
     * 获取任务详情-总体情况
     *
     * @param req
     * @return
     */
    @GetMapping(value = "stat/task_detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskStatVo> taskDetail(HttpServletRequest request, @Valid StatTaskDetailReq req) {
        LjBaseResponse<TaskStatVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            response.setResult(1);
            response.setMessage(e.getMessage());
            return response;
        }
        try {
            TaskStatVo.IssueStatVo issue = houseqmStatisticService.getCheckTaskIssueTypeStatByTaskIdAreaId(req.getTask_id(), req.getArea_id());
            TaskStatVo.HouseStatVo house = houseqmStatisticService.getHouseQmCheckTaskHouseStatByTaskId(req.getProject_id(), req.getTask_id(), req.getArea_id());
            house.setHouse_checked_percent(MathUtil.getPercentage(house.getChecked_count(), house.getHouse_count()));
            house.setHouse_repaired_percent(MathUtil.getPercentage(house.getRepaired_count(), house.getHas_issue_count()));
            house.setHouse_approveded_percent(MathUtil.getPercentage(house.getApproved_count(), house.getRepaired_count()));
            TaskStatVo taskStatVo = new TaskStatVo();
            taskStatVo.setIssue(issue);
            taskStatVo.setHouse(house);
            response.setData(taskStatVo);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    /**
     * 获取整改追踪信息
     *
     * @param req
     * @return
     */
    @GetMapping(value = "stat/task_situation_repair_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatTaskSituationRepairStatRsp> taskSituationRepairStat(HttpServletRequest request, @Valid StatTaskSituationRepairStatReq req) {
        LjBaseResponse<StatTaskSituationRepairStatRsp> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        Date t = null;
        try {
            t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("0001-01-01 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            TaskRepairStatVo taskRepairStatVo = houseqmStatisticService.searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(req.getProject_id(), req.getTask_id(), 0, t, t);
            TaskRepairStatVo.TaskRepairVo item = taskRepairStatVo.getItem();

            StatTaskSituationRepairStatRsp res = new StatTaskSituationRepairStatRsp();
            StatTaskSituationRepairStatRsp.HouseQmStatTaskDetailRepairerStatRsp result = res.new HouseQmStatTaskDetailRepairerStatRsp();
            result.setInitime_finish(item.getInitime_finish());
            result.setInitime_finish_count(item.getInitime_finish_count());
            result.setInitime_unfinish(item.getInitime_unfinish());
            result.setInitime_unfinish_count(item.getInitime_unfinish_count());
            result.setNo_plan_end_on(item.getNo_plan_end_on());
            result.setNo_plan_end_on_count(item.getNo_plan_end_on_count());
            result.setOvertime_finish(item.getOvertime_finish());
            result.setOvertime_finish_count(item.getOvertime_finish_count());
            result.setOvertime_unfinish(item.getOvertime_unfinish());
            result.setOvertime_unfinish_count(item.getOvertime_unfinish_count());
            res.setItem(result);
            response.setData(res);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.StatHouseqmTaskSituationOverallRspVo>
     * @Author hy
     * @Description 统计-验房统计-任务总进度及交付情况
     * @Date 20:12 2019/1/8
     * @Param [req]
     * ////该接口不用了，废弃
     **/
    @Deprecated
    @GetMapping(value = "stat_houseqm/task_situation_overall", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatHouseqmTaskSituationOverallRspVo> taskSituationOverall(HttpServletRequest request, @RequestBody @Valid StatHouseqmTaskSituationOverallReq req) {
        LjBaseResponse<StatHouseqmTaskSituationOverallRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
            List<Integer> taskIds = StringSplitToListUtil.strToInts(req.getTask_ids(), ",");
            RepossessionTasksStatusInfoVo info = houseqmStatisticService.getRepossessionTasksStatusInfo(req.getProject_id(), taskIds, 0);
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
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    /**
     * @return
     * @Author hy
     * @Description 统计-验房统计-每天的交付数
     * @Date 20:16 2019/1/8
     * @Param //// 该接口不用了，废弃
     **/
    @Deprecated
    @GetMapping(value = "stat_houseqm/complete_daily", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatHouseqmCompleteDailyRspVo> completeDaily(HttpServletRequest request, @Valid StatHouseqmCompleteDailyReq req) {
        LjBaseResponse<StatHouseqmCompleteDailyRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
            List<Integer> taskIds = StringSplitToListUtil.strToInts(req.getTask_ids(), ",");
            int beginOn = 0;
            int endOn = 0;
            if (req.getBegin_on().length() > 0) {
                beginOn = DateUtil.datetimeToTimeStamp(DateUtil.strToDate(req.getBegin_on(), "yyyy-MM-dd hh:mm:ss"));
            }
            if (req.getEnd_on().length() > 0) {
                endOn = DateUtil.datetimeToTimeStamp(DateUtil.strToDate(req.getEnd_on(), "yyyy-MM-dd hh:mm:ss"));
            }
            StatHouseqmCompleteDailyRspVo data = houseqmStatisticService.searchRepossessionStatusCompleteDaily(req.getProject_id(), taskIds, beginOn, endOn, req.getPage(), req.getPage_size());
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.StatTaskSituationMembersCheckerRspVo>
     * @Author hy
     * @Description 统计-任务详情-人员情况-检查人
     * @Date 14:50 2019/1/9
     * @Param [req]
     **/
    @GetMapping(value = "stat/task_situation_members_checker/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatTaskSituationMembersCheckerRspVo> taskSituationMembersChecker(HttpServletRequest request, @Valid StatTaskSituationMembersCheckerReq req) {
        LjBaseResponse<StatTaskSituationMembersCheckerRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
            if ("".equals(req.getBegin_on())) {
                req.setBegin_on("1970-01-02");
            }
            Date start = DateUtil.strToDate(req.getBegin_on(), "yyyy-MM-dd");
            Date end = null;
            if ("".equals(req.getEnd_on())) {
                end = new Date();
            } else {
                end = DateUtil.strToDate(req.getEnd_on(), "yyyy-MM-dd");
            }
            List<HouseQmStatTaskDetailMemberCheckerRspVo> result = houseqmStatService.searchCheckerIssueStatusStatByProjTaskIdBetweenTime(req.getProject_id(), req.getTask_id(), start, DateUtil.dateAddDay(end, 1));
            StatTaskSituationMembersCheckerRspVo data = new StatTaskSituationMembersCheckerRspVo();
            data.setItems(result);
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.StatTaskSituationMembersRepairerRspVo>
     * @Author hy
     * @Description 项目下任务整改人员情况查询
     * http://192.168.37.159:3000/project/8/interface/api/3416
     * @Date 14:59 2019/1/10
     * @Param [req]
     **/
    @GetMapping(value = "stat/task_situation_members_repairer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatTaskSituationMembersRepairerRspVo> taskSituationMembersRepairer(HttpServletRequest request, @Valid StatTaskSituationMembersRepairerReq req) {
        LjBaseResponse<StatTaskSituationMembersRepairerRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
            if ("".equals(req.getBegin_on())) {
                req.setBegin_on("1970-01-02");
            }
            Date start = DateUtil.strToDate(req.getBegin_on(), "yyyy-MM-dd");
            Date end = null;
            if ("".equals(req.getEnd_on())) {
                end = new Date();
            } else {
                end = DateUtil.strToDate(req.getEnd_on(), "yyyy-MM-dd");
            }
            List<HouseQmStatTaskDetailMemberRepairerRspVo> res = houseqmStatService.searchRepaireIssueStatusStatByProjTaskIdBetweenTime(req.getProject_id(), req.getTask_id(), start, DateUtil.dateAddDay(end, 1));
            StatTaskSituationMembersRepairerRspVo data = new StatTaskSituationMembersRepairerRspVo();
            data.setItems(res);
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.HouseQmStatAreaSituationIssueRspVo>
     * @Author hy
     * @Description 项目下日常检查区域概况
     * http://192.168.37.159:3000/project/8/interface/api/3424
     * @Date 16:55 2019/1/10
     * @Param [req]
     **/
    @GetMapping(value = "stat/area_situation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseQmStatAreaSituationIssueRspVo> areaSituation(HttpServletRequest request, @Valid StatAreaSituationReq req) {
        LjBaseResponse<HouseQmStatAreaSituationIssueRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.统计.查看", "项目.工程检查.统计.查看"});
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        try {
            HouseQmStatAreaSituationIssueRspVo data = houseqmStatService.getAreaIssueTypeStatByProjectIdAreaIdCategoryCls(req.getProject_id(), req.getArea_id(), req.getCategory_cls());
            response.setData(data);
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        return response;
    }

}
