package com.longfor.longjian.houseqm.app.controller.oapiv3houseqm;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.req.StatAreaSituationReq;
import com.longfor.longjian.houseqm.app.req.StatTaskSituationMembersCheckerReq;
import com.longfor.longjian.houseqm.app.req.StatTaskSituationMembersRepairerReq;
import com.longfor.longjian.houseqm.app.req.houseqmstat.*;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.*;
import com.longfor.longjian.houseqm.consts.RepossessionStatusEnum;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.MathUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    private static final String SEE = "项目.移动验房.统计.查看";
    private static final String YMDHMS = "yyyy-MM-dd hh:mm:ss";
    private static final String YMD = "yyyy-MM-dd";

    @Resource
    private IHouseqmStatService houseqmStatService;
    @Resource
    private IHouseqmStatisticService houseqmStatisticService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;


    @RequestMapping(value = "stat/category_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatCategoryStatRspVo> categoryStat(HttpServletRequest request, @Validated StatCategoryStatReq req) {
        LjBaseResponse<StatCategoryStatRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        if (req.getArea_id() == null) req.setArea_id(0);
        Date beginOn = DateUtil.timeStampToDate(0, YMD);
        Date endOn = DateUtil.timeStampToDate(0, YMD);
        if (req.getBegin_on() != null && !"".equals(req.getBegin_on())) {
            beginOn = DateUtil.strToDate(req.getBegin_on(), YMD);
        }
        if (req.getEnd_on() != null && !"".equals(req.getEnd_on())) {
            Date t = DateUtil.strToDate(req.getEnd_on(), YMD);
            endOn = DateUtil.dateAddDay(t, 1);
        }
        StatCategoryStatRspVo result = houseqmStatService.searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(req.getProject_id(), req.getTask_id(), req.getArea_id(), beginOn, endOn);
        response.setData(result);
        return response;
    }

    @RequestMapping(value = "stat/inspection_situation_search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatInspectionSituationSearchRspVo> inspectionSituationSearch(HttpServletRequest request, @Validated StatInspectionSituationSearchReq req) {
        LjBaseResponse<StatInspectionSituationSearchRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        if (req.getArea_id() == null) req.setArea_id(0);
        if (req.getIssue_status() == null) req.setIssue_status(0);
        if (req.getStatus() == null) req.setStatus(0);

        if (!req.getStatus().equals(RepossessionStatusEnum.Accept.getId())) {
            req.setStart_time("");
            req.setEnd_time("");
        }
        // 时间设置问题 可能造成数据结果不一致，修改
        Date startTime = DateUtil.timeStampToDate(0, YMD);
        Date endTime = DateUtil.timeStampToDate(0, YMD);
        if (req.getStart_time().length() > 0) {
            startTime = DateUtil.strToDate(req.getStart_time() + " 00:00:00", YMDHMS);
            endTime = DateUtil.strToDate(req.getEnd_time() + " 23:59:59", YMDHMS);
        }
        List<Integer> areaIds = houseqmStatService.searchRepossessInspectionAreaIdsByConditions(req.getProject_id(), req.getTask_id(), req.getArea_id(), req.getStatus(), req.getIssue_status(), startTime, endTime);
        StatInspectionSituationSearchRspVo data = new StatInspectionSituationSearchRspVo();
        data.setTotal(areaIds.size());
        List<Integer> ids = splitSliceByPaged(areaIds, req.getPage(), req.getPage_size());
        List<InspectionHouseStatusInfoVo> details = houseqmStatService.formatFenhuHouseInspectionStatusInfoByAreaIds(req.getTask_id(), ids);
        List<HouseQmStatInspectionSituationRspVo> items = new ArrayList<>();
        for (InspectionHouseStatusInfoVo detail : details) {
            HouseQmStatInspectionSituationRspVo item = new HouseQmStatInspectionSituationRspVo();
            item.setArea_id(detail.getAreaId());
            item.setArea_name(detail.getAreaName());
            item.setArea_path(detail.getAreaPathName());
            item.setIssue_approveded_count(detail.getIssueApprovededCount());
            item.setIssue_count(detail.getIssueCount());
            item.setIssue_repaired_count(detail.getIssueRepairedCount());
            item.setStatus(detail.getStatus());
            item.setStatus_name(detail.getStatusName());
            item.setTask_id(detail.getTaskId());
            items.add(item);
        }
        if (!items.isEmpty()) data.setItems(items);// go 源码 返回为null 所以需要进行判断
        response.setData(data);
        return response;
    }

    private List<Integer> splitSliceByPaged(List<Integer> areaIds, int page, int pageSize) {
        int start;
        if (page <= 0) {
            page = 1;
        }
        start = (page - 1) * pageSize;
        if (start >= areaIds.size()) return areaIds;
        int end = start + pageSize;
        if (end > areaIds.size()) {
            end = areaIds.size();
        }
        List<Integer> ids = Lists.newArrayList();
        for (int i = start; i < end; i++) {
            ids.add(areaIds.get(i));
        }
        return ids;
    }


    @RequestMapping(value = "stat_houseqm/checker_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<CheckerStatListVo> checkerStat(HttpServletRequest request, @Valid StatHouseqmCheckerStatReq req) {
        LjBaseResponse<CheckerStatListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
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
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    @RequestMapping(value = "stat/task_situation_daily", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskSituationDaily(HttpServletRequest request, @Valid StatTaskSituationDailyReq req) {
        LjBaseResponse<ProjectDailyListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
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
            log.warn(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }

    @RequestMapping(value = "stat/task_situation_overall", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectOveralListVo> taskSituationOverall(HttpServletRequest request, @Valid StatTaskSituationOverallReq req) {
        LjBaseResponse<ProjectOveralListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
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
                if (item == null) {
                    continue;
                }

                totalStat.setChecked_count(totalStat.getChecked_count() + item.getChecked_count());
                totalStat.setIssue_count(totalStat.getIssue_count() + item.getIssue_count());
                totalStat.setRecords_count(totalStat.getRecords_count() + item.getRecords_count());
                items.add(item);
            } catch (Exception e) {
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

    @RequestMapping(value = "stat/task_area_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskAreaListVo> taskAreaList(HttpServletRequest request, @Valid StatTaskAreaListReq req) {
        LjBaseResponse<TaskAreaListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        try {
            if (req.getTyp() == null) req.setTyp(0);
            TaskAreaListVo talv = houseqmStatService.searchAreasByProjTaskIdTyp(req.getProject_id(), req.getTask_id(), req.getTyp());
            response.setData(talv);
        } catch (Exception e) {
            log.warn(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    @RequestMapping(value = "stat/area_situation_task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<AreaTaskListVo> areaSituationTaskList(HttpServletRequest request, @Valid StatAreaSituationTaskListReq req) {
        LjBaseResponse<AreaTaskListVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        try {
            AreaTaskListVo areaTaskListVo = houseqmStatService.searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(req.getProject_id(), req.getArea_id(), Arrays.asList(req.getCategory_cls()));
            response.setData(areaTaskListVo);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    @RequestMapping(value = "stat/task_detail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskStatVo> taskDetail(HttpServletRequest request, @Valid StatTaskDetailReq req) {
        LjBaseResponse<TaskStatVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
            return response;
        }
        try {
            if (req.getArea_id() == null) req.setArea_id(0);
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
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    @RequestMapping(value = "stat/task_situation_repair_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatTaskSituationRepairStatRsp> taskSituationRepairStat(HttpServletRequest request, @Valid StatTaskSituationRepairStatReq req) {
        LjBaseResponse<StatTaskSituationRepairStatRsp> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        Date t = null;
        try {
            t = new SimpleDateFormat(YMDHMS).parse("0001-01-01 00:00:00");
        } catch (ParseException e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    @RequestMapping(value = "stat/task_situation_members_checker", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatTaskSituationMembersCheckerRspVo> taskSituationMembersChecker(HttpServletRequest request, @Valid StatTaskSituationMembersCheckerReq req) {
        LjBaseResponse<StatTaskSituationMembersCheckerRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
            if ("".equals(req.getBegin_on())) {
                req.setBegin_on("1970-01-01");
            }
            Date start = DateUtil.strToDate(req.getBegin_on(), YMD);
            Date end = null;
            if ("".equals(req.getEnd_on())) {
                end = new Date();
            } else {
                end = DateUtil.strToDate(req.getEnd_on(), YMD);
            }
            List<HouseQmStatTaskDetailMemberCheckerRspVo> result = houseqmStatService.searchCheckerIssueStatusStatByProjTaskIdBetweenTime(req.getProject_id(), req.getTask_id(), start, DateUtil.dateAddDay(end, 1));
            StatTaskSituationMembersCheckerRspVo data = new StatTaskSituationMembersCheckerRspVo();
            data.setItems(result);
            response.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        return response;
    }


    @RequestMapping(value = "stat/task_situation_members_repairer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatTaskSituationMembersRepairerRspVo> taskSituationMembersRepairer(HttpServletRequest request, @Valid StatTaskSituationMembersRepairerReq req) {
        LjBaseResponse<StatTaskSituationMembersRepairerRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
            if ("".equals(req.getBegin_on())) {
                req.setBegin_on("1970-01-01");
            }
            Date start = DateUtil.strToDate(req.getBegin_on(), YMD);
            Date end = null;
            if ("".equals(req.getEnd_on())) {
                end = new Date();
            } else {
                end = DateUtil.strToDate(req.getEnd_on(), YMD);
            }
            List<HouseQmStatTaskDetailMemberRepairerRspVo> res = houseqmStatService.searchRepaireIssueStatusStatByProjTaskIdBetweenTime(req.getProject_id(), req.getTask_id(), start, DateUtil.dateAddDay(end, 1));
            StatTaskSituationMembersRepairerRspVo data = new StatTaskSituationMembersRepairerRspVo();
            data.setItems(res);
            response.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        return response;
    }

    @RequestMapping(value = "stat/area_situation", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<StatAreaSituationRspVo> areaSituation(HttpServletRequest request, @Valid StatAreaSituationReq req) {
        LjBaseResponse<StatAreaSituationRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{SEE, SEE});
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        try {
            HouseQmStatAreaSituationIssueRspVo result = houseqmStatService.getAreaIssueTypeStatByProjectIdAreaIdCategoryCls(req.getProject_id(), req.getArea_id(), req.getCategory_cls());
            StatAreaSituationRspVo data = new StatAreaSituationRspVo();
            data.setIssue(result);
            response.setData(data);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
            return response;
        }
        return response;
    }

}
