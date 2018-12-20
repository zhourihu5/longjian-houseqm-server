package com.longfor.longjian.houseqm.app.controller;

import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.longfor.gaia.gfs.web.mock.MockOperation;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.ProjectReq;
import com.longfor.longjian.houseqm.app.service.HouseqmStaticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.consts.TimeStauEnum;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskRspService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * http://192.168.37.159:3000/project/8/interface/api/368
 * http://192.168.37.159:3000/project/8/interface/api/374
 * http://192.168.37.159:3000/project/8/interface/api/380
 * http://192.168.37.159:3000/project/8/interface/api/384
 * http://192.168.37.159:3000/project/8/interface/api/388
 * http://192.168.37.159:3000/project/8/interface/api/392
 * http://192.168.37.159:3000/project/8/interface/api/394
 * http://192.168.37.159:3000/project/8/interface/api/396
 * http://192.168.37.159:3000/project/8/interface/api/400
 * http://192.168.37.159:3000/project/8/interface/api/402
 *
 * @author lipeishuai
 * @date 2018/11/20 18:15
 */

@RestController
@RequestMapping("v3/api/houseqm_statistic/")
@Slf4j
public class HouseqmStatisticController {
    @Resource
    HouseqmStaticService houseqmStaticService;
    @Resource
    HouseQmCheckTaskRspService houseQmCheckTaskRspService;
    @Resource
    AreaService areaService;
    @Resource
    IHouseqmStatisticService houseqmStatisticService;



    @GetMapping(value = "task_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskStatRspMsgVo> taskStat(@RequestParam(value = "project_id") Integer prodectId,
                                                                     @RequestParam(value = "task_id") Integer taskId,
                                                                     @RequestParam(value = "area_id") Integer areaId,
                                                                     @RequestParam(value = "timestamp") Integer timestamp) {
        CheckTaskHouseStatInfoVo checkTaskHouseStatInfo = houseqmStaticService.GetHouseQmCheckTaskHouseStatByTaskId(prodectId, taskId, areaId);
        HouseQmTaskHouseStatVo houseStatVo = new HouseQmTaskHouseStatVo();
        houseStatVo.setHouseCheckedPercent(getPercentage(checkTaskHouseStatInfo.getCheckedCount(), checkTaskHouseStatInfo.getHouseCount()));
        houseStatVo.setHouseRepairedPercent(getPercentage(checkTaskHouseStatInfo.getRepairedCount(), checkTaskHouseStatInfo.getHasIssueCount()));
        houseStatVo.setHouseApprovededPercent(getPercentage(checkTaskHouseStatInfo.getApprovedCount(), checkTaskHouseStatInfo.getRepairedCount()));
        HouseqmStatisticTaskStatRspMsgVo vo = new HouseqmStatisticTaskStatRspMsgVo();
        vo.setItem(houseStatVo);
        LjBaseResponse<HouseqmStatisticTaskStatRspMsgVo> response = new LjBaseResponse<>();
        response.setData(vo);
        return response;
    }


    /**
     * @param projectReq
     */
    @GetMapping(value = "get_daterange_options", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticGetDaterangeOptionsRspMsgVo> getDaterangeOptions(ProjectReq projectReq) {
        HouseqmStatisticGetDaterangeOptionsRspMsgVo vo = new HouseqmStatisticGetDaterangeOptionsRspMsgVo();
        ArrayList<ApiDateRangeOption> list = new ArrayList<>();
        ApiDateRangeOption apiDateRangeOption = new ApiDateRangeOption();
        apiDateRangeOption.setValue(TimeStauEnum.TIME_RANGE_LAST_WEEK.getId());
        apiDateRangeOption.setText(TimeStauEnum.TIME_RANGE_LAST_WEEK.getValue());
        list.add(apiDateRangeOption);
        ApiDateRangeOption apiDateRangeOption1 = new ApiDateRangeOption();
        apiDateRangeOption1.setValue(TimeStauEnum.TIME_RANGE_LAST_MONTH.getId());
        apiDateRangeOption1.setText(TimeStauEnum.TIME_RANGE_LAST_MONTH.getValue());
        list.add(apiDateRangeOption1);
        ApiDateRangeOption apiDateRangeOption2 = new ApiDateRangeOption();
        apiDateRangeOption2.setValue(TimeStauEnum.TIME_RANGE_THIS_WEEK.getId());
        apiDateRangeOption2.setText(TimeStauEnum.TIME_RANGE_THIS_WEEK.getValue());
        list.add(apiDateRangeOption2);
        ApiDateRangeOption apiDateRangeOption3 = new ApiDateRangeOption();
        apiDateRangeOption3.setValue(TimeStauEnum.TIME_RANGE_THIS_MONTH.getId());
        apiDateRangeOption3.setText(TimeStauEnum.TIME_RANGE_THIS_MONTH.getValue());
        list.add(apiDateRangeOption3);
        vo.setItems(list);
        LjBaseResponse<HouseqmStatisticGetDaterangeOptionsRspMsgVo> response = new LjBaseResponse<>();
        response.setData(vo);
        return response;
    }

    /**
     * @param projectId
     * @param categoryCl
     * @return
     */
    @GetMapping(value = "task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueTaskListRspMsgVo> taskList(@RequestParam(value = "project_id") Integer projectId,
                                                          @RequestParam(value = "category_cls") Integer categoryCl) {

        List<HouseQmCheckTaskSimpleRspVo> vos = houseqmStaticService.SearchHouseQmCheckTaskByProjCategoryCls(projectId, categoryCl);
        IssueTaskListRspMsgVo vo = new IssueTaskListRspMsgVo();
        vo.setTaskList(vos);
        LjBaseResponse<IssueTaskListRspMsgVo> response = new LjBaseResponse<>();
        response.setData(vo);
        return response;
    }

    /**
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "project_issue_repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> projectIssueRepair(ProjectReq projectReq) {

        return null;
    }

    /**
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_checkitem_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskCheckitemStat(ProjectReq projectReq) {
        return null;
    }

    /**
     * @param
     * @return
     */

    @GetMapping(value = "task_issue_repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskRepairStatVo> taskIssueRepair(@RequestParam(value = "project_id") Integer projectId,
                                                            @RequestParam(value = "task_id") Integer taskId,
                                                            @RequestParam(value = "area_id") Integer areaId,
                                                            @RequestParam(value = "begin_on") Integer beginOn,
                                                            @RequestParam(value = "end_on") Integer endOn,
                                                            @RequestParam(value = "timestamp") Integer timestamp) {
        Date begin = null;
        Date endOns = null;
        if (beginOn > 0) {
            begin = transForDate(beginOn);
        }
        if (endOn > 0) {
            Date end = transForDate(endOn);
            endOns = addDate(end, 0, 0, 1);
        }
        TaskRepairStatVo taskRepairStatVo = houseqmStatisticService.searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(projectId, taskId, areaId, begin, endOns);

        LjBaseResponse<TaskRepairStatVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(taskRepairStatVo);
        return ljbr;
    }

    public static Date addDate(Date enOndate, int year, int month, int day) {
        //获取默认选中的日期的年月日星期的值，并赋值
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(enOndate);//设置当前日期
        //处理后的日期
        calendar.add(calendar.get(Calendar.YEAR), year);
        calendar.add(calendar.get(Calendar.MONTH), month);
        calendar.add(calendar.get(Calendar.DATE), day);
        return calendar.getTime();
    }


    // 时间戳转日期
    public static Date transForDate(Integer ms) {
        if (ms == null) {
            ms = 0;
        }
        long msl = (long) ms * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = null;
        if (ms != null) {
            try {
                String str = sdf.format(msl);
                temp = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }


    /**
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "task_issue_repair_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> taskIssueRepairList(ProjectReq projectReq) {
        return null;
    }

    /**
     * @param projectReq
     * @return
     */
    @MockOperation
    @GetMapping(value = "category_issue_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<ProjectDailyListVo> categoryIssueList(ProjectReq projectReq) {
        return null;
    }


    /**
     * @param prodectId
     * @param timestamp
     * @return
     */
    @GetMapping(value = "project_building_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticProjectBuildingListRspMsgVo> projectBuildingList(@RequestParam(value = "project_id") Integer prodectId,
                                                                                           @RequestParam(value = "timestamp") Integer timestamp) {
        List<ApiBuildingInfo> buildingInfoList = houseqmStaticService.PSelectByFatherId(prodectId);
        HouseqmStatisticProjectBuildingListRspMsgVo msgVo = new HouseqmStatisticProjectBuildingListRspMsgVo();
        msgVo.setItems(buildingInfoList);
        LjBaseResponse<HouseqmStatisticProjectBuildingListRspMsgVo> response = new LjBaseResponse<>();
        response.setData(msgVo);

        return response;
    }

    /**
     * @param
     * @return
     */
    @GetMapping(value = "task_building_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskBuildingListRspMsgVo> taskBuildingList(@RequestParam(value = "project_id") Integer prodectId,
                                                                                     @RequestParam(value = "task_id") Integer taskId,
                                                                                     @RequestParam(value = "timestamp") Integer timestamp) {
        HouseqmStatisticTaskBuildingListRspMsgVo msgVo = houseqmStaticService.taskBuildingList(prodectId, taskId);
        LjBaseResponse<HouseqmStatisticTaskBuildingListRspMsgVo> response = new LjBaseResponse<>();
        response.setData(msgVo);
        return response;
    }

    //用于计算百分比
    private String getPercentage(int a, int b) {
        if (a == 0 || b == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("0.00");
        String result = df.format((float) a / (float) b * 100);
        return result;
    }

}


