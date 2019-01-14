package com.longfor.longjian.houseqm.app.controller;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.vo.HouseqmStatisticTaskCheckitemStatRspMsgVo;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.util.DateUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.houseqm.app.req.ProjectReq;
import com.longfor.longjian.houseqm.app.service.HouseqmStaticService;

import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.consts.TimeStauEnum;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import java.text.DecimalFormat;
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
    HouseQmCheckTaskService houseQmCheckTaskRspService;
    @Resource
    IHouseqmStatisticService iHouseqmStatisticService;
    @Resource
    AreaService areaService;


    @GetMapping(value = "task_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskStatRspMsgVo> taskStat(@RequestParam(value = "project_id") Integer prodectId,
                                                                     @RequestParam(value = "task_id") Integer taskId,
                                                                     @RequestParam(value = "area_id") Integer areaId,
                                                                     @RequestParam(value = "timestamp") Integer timestamp) {
        CheckTaskHouseStatInfoVo checkTaskHouseStatInfo = houseqmStaticService.GetHouseQmCheckTaskHouseStatByTaskId(prodectId, taskId, areaId);
        HouseQmTaskHouseStatVo houseStatVo = new HouseQmTaskHouseStatVo();
        houseStatVo.setHouse_checked_percent(getPercentage(checkTaskHouseStatInfo.getCheckedCount(), checkTaskHouseStatInfo.getHouseCount()));
        houseStatVo.setHouse_repaired_percent(getPercentage(checkTaskHouseStatInfo.getRepairedCount(), checkTaskHouseStatInfo.getHasIssueCount()));
        houseStatVo.setHouse_approveded_percent(getPercentage(checkTaskHouseStatInfo.getApprovedCount(), checkTaskHouseStatInfo.getRepairedCount()));
        houseStatVo.setApproved_count(checkTaskHouseStatInfo.getApprovedCount());
        houseStatVo.setHas_issue_count(checkTaskHouseStatInfo.getHasIssueCount());
        houseStatVo.setRepaired_count(checkTaskHouseStatInfo.getRepairedCount());
        houseStatVo.setChecked_count(checkTaskHouseStatInfo.getCheckedCount());
        houseStatVo.setHouse_count(checkTaskHouseStatInfo.getHouseCount());
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
     * @param
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.ProjectDailyListVo>
     * @author hy
     * @date 2018/12/24 0024
     */
    @GetMapping(value = "project_issue_repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<IssueRepairStatisticVo> projectIssueRepair(@RequestParam(value = "project_id") Integer projectId, @RequestParam(value = "source") String source,
                                                                     @RequestParam(value = "area_id") Integer areaId, @RequestParam(value = "begin_on", defaultValue = "0") Integer beginOn,
                                                                     @RequestParam(value = "end_on", defaultValue = "0") Integer endOn, @RequestParam(value = "timestamp", defaultValue = "0") Integer timestamp
    ) {
        IssueRepairStatisticVo issueRepairStatisticVo = iHouseqmStatisticService.projectIssueRepair(projectId, source, areaId, beginOn, endOn, timestamp);
        LjBaseResponse<IssueRepairStatisticVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(issueRepairStatisticVo);
        return ljbr;
    }

    /**
     * @param
     * @return
     */
    @GetMapping(value = "task_checkitem_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskCheckitemStatRspMsgVo> taskCheckitemStat(@RequestParam(value = "project_id") Integer projectId,
                                                                                       @RequestParam(value = "task_id") Integer taskId,
                                                                                       @RequestParam(value = "area_id") Integer areaId,
                                                                                       @RequestParam(value = "begin_on") Integer beginOn,
                                                                                       @RequestParam(value = "end_on") Integer endOn,
                                                                                       @RequestParam(value = "timestamp") Integer timestamp) {

        Date begin = DateUtil.transForDate(beginOn);
        Date endOns = DateUtil.transForDate(endOn);
        List<HouseQmIssueCategoryStatVo> categoryStatlist = iHouseqmStatisticService.searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(projectId, taskId, areaId, begin, endOns);
        HouseqmStatisticTaskCheckitemStatRspMsgVo vo = new HouseqmStatisticTaskCheckitemStatRspMsgVo();
        List<HouseqmStatisticTaskCheckitemStatRspMsgVo.ApiHouseQmCheckItemIssueStat> issueStatList = Lists.newArrayList();
        for (int i = 0; i < categoryStatlist.size(); i++) {
            HouseqmStatisticTaskCheckitemStatRspMsgVo.ApiHouseQmCheckItemIssueStat apiHouseQmCheckItemIssueStat = new HouseqmStatisticTaskCheckitemStatRspMsgVo().new ApiHouseQmCheckItemIssueStat();
            apiHouseQmCheckItemIssueStat.setName(categoryStatlist.get(i).getName());
            apiHouseQmCheckItemIssueStat.setKey(categoryStatlist.get(i).getKey());
            apiHouseQmCheckItemIssueStat.setFather_key(categoryStatlist.get(i).getParentKey());
            apiHouseQmCheckItemIssueStat.setIssue_count(categoryStatlist.get(i).getIssueCount());
            issueStatList.add(apiHouseQmCheckItemIssueStat);
        }
        vo.setItems(issueStatList);

        //total
        LjBaseResponse<HouseqmStatisticTaskCheckitemStatRspMsgVo> ljBaseResponse = new LjBaseResponse();
        ljBaseResponse.setData(vo);

        return ljBaseResponse;
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
        Date begin = DateUtil.transForDate(beginOn);
        Date endOns = DateUtil.transForDate(endOn);
        //endon加一天
        Calendar c = Calendar.getInstance();
        c.setTime(endOns);
        c.add(Calendar.DAY_OF_MONTH, 1);// +1天
        endOns = c.getTime();
        TaskRepairStatVo taskRepairStatVo = iHouseqmStatisticService.searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(projectId, taskId, areaId, begin, endOns);
        LjBaseResponse<TaskRepairStatVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(taskRepairStatVo);
        return ljbr;
    }


    /**
     * @param
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.HouseqmStatisticCategoryIssueListRspMsgVo>
     * @author hy
     * @date 2018/12/22 0022
     */
    @GetMapping(value = "task_issue_repair_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticCategoryIssueListRspMsgVo> taskIssueRepairList(@RequestParam(value = "project_id") Integer projectId, @RequestParam(value = "task_id") Integer taskId,
                                                                                         @RequestParam(value = "area_id") Integer areaId, @RequestParam(value = "begin_on", defaultValue = "0") Integer beginOn,
                                                                                         @RequestParam(value = "end_on", defaultValue = "0") Integer endOn, @RequestParam(value = "timestamp", defaultValue = "0") Integer timestamp,
                                                                                         @RequestParam(value = "plan_status", defaultValue = "0") Integer planStatus, @RequestParam(value = "source") String source,
                                                                                         @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "page_size", defaultValue = "20") Integer pageSize) {

        HouseqmStatisticCategoryIssueListRspMsgVo projectDailyListVo = iHouseqmStatisticService.taskIssueRepairList(projectId, taskId, areaId, beginOn, endOn, timestamp, planStatus, source, page, pageSize);
        LjBaseResponse<HouseqmStatisticCategoryIssueListRspMsgVo> ljbr = new LjBaseResponse<>();
        ljbr.setData(projectDailyListVo);
        return ljbr;
    }

    /**
     * @param
     * @return
     */

    @GetMapping(value = "category_issue_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticCategoryIssueListRspMsgVo> categoryIssueList(@RequestParam(value = "project_id") Integer projectId,
                                                                                       @RequestParam(value = "category_key") String categoryKey,
                                                                                       @RequestParam(value = "area_id") Integer areaId,
                                                                                       @RequestParam(value = "page") Integer page,
                                                                                       @RequestParam(value = "page_size") Integer pageSize
    ) {
        List<HouseQmCheckTaskIssueOnlineInfoVo> infoList = iHouseqmStatisticService.SearchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged(projectId, categoryKey, areaId, page, pageSize);
        ArrayList<HouseqmStatisticCategoryIssueListRspMsgVo.ApiTaskIssueRepairListRsp> objects = Lists.newArrayList();
        for (int i = 0; i < infoList.size(); i++) {
            HouseqmStatisticCategoryIssueListRspMsgVo.ApiTaskIssueRepairListRsp listRsp = new HouseqmStatisticCategoryIssueListRspMsgVo().new ApiTaskIssueRepairListRsp();
            listRsp.setId(infoList.get(i).getId());
            listRsp.setProject_id(infoList.get(i).getProjectId());
            listRsp.setTask_id(infoList.get(i).getTaskId());
            listRsp.setUuid(infoList.get(i).getUuid());
            listRsp.setTitle(infoList.get(i).getTitle());
            listRsp.setTyp(infoList.get(i).getTyp());
            listRsp.setContent(infoList.get(i).getContent());
            listRsp.setCondition(infoList.get(i).getCondition());
            listRsp.setStatus(infoList.get(i).getStatus());
            listRsp.setPlan_end_on(DateUtil.datetimeToTimeStamp(infoList.get(i).getPlanEndOn()));
            listRsp.setAttachment_md5_list(infoList.get(i).getAttachmentMd5List());
            listRsp.setClient_create_at(DateUtil.datetimeToTimeStamp(infoList.get(i).getClientCreateAt()));
            listRsp.setUpdate_at(DateUtil.datetimeToTimeStamp(infoList.get(i).getUpdateAt()));
            listRsp.setAttachment_url_list(infoList.get(i).getAttachmentUrlList());
            listRsp.setArea_path_name(infoList.get(i).getAreaPathName());
            listRsp.setCategory_path_name(infoList.get(i).getCategoryPathName());
            listRsp.setCheck_item_path_name(infoList.get(i).getCheckItemPathName());
            objects.add(listRsp);

        }
        LjBaseResponse<HouseqmStatisticCategoryIssueListRspMsgVo> ljr = new LjBaseResponse<>();
        HouseqmStatisticCategoryIssueListRspMsgVo vo = new HouseqmStatisticCategoryIssueListRspMsgVo();
        vo.setIssue_list(objects);
        vo.setTotal(infoList.size());
        ljr.setData(vo);
        return ljr;
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

    @GetMapping(value = "task_issue_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskIssueStatRspMsgVo> taskIssueStat(@RequestParam(value = "project_id") Integer projectId,
                                                                               @RequestParam(value = "task_id") Integer taskId,
                                                                               @RequestParam(value = "area_id") Integer areaId,
                                                                               @RequestParam(value = "timestamp") Integer timestamp) {
        TaskStatVo.IssueStatVo vo = iHouseqmStatisticService.getCheckTaskIssueTypeStatByTaskIdAreaId(taskId, areaId);
        HouseQmCheckTaskHouseStatInfoVo infoVo = iHouseqmStatisticService.getHouseQmHouseQmCheckTaskHouseStatByTaskId(projectId, taskId, areaId);
        ApiHouseQmTaskStatVo houseQmTaskStatVo = new ApiHouseQmTaskStatVo();
        houseQmTaskStatVo.setIssueApprovededCount(vo.getIssue_approveded_count());
        houseQmTaskStatVo.setIssueAssignedCount(vo.getIssue_assigned_count());
        houseQmTaskStatVo.setIssueCount(vo.getIssue_count());
        houseQmTaskStatVo.setIssueRecordedCount(vo.getIssue_recorded_count());
        houseQmTaskStatVo.setIssueRepairedCount(vo.getIssue_repaired_count());
        houseQmTaskStatVo.setRecordCount(vo.getRecord_count());
        houseQmTaskStatVo.setHouseCount(infoVo.getHouseCount());
        HouseqmStatisticTaskIssueStatRspMsgVo houseqmStatisticTaskIssueStatRspMsgVo = new HouseqmStatisticTaskIssueStatRspMsgVo();
        houseqmStatisticTaskIssueStatRspMsgVo.setItem(houseQmTaskStatVo);
        LjBaseResponse<HouseqmStatisticTaskIssueStatRspMsgVo> response = new LjBaseResponse<>();
        response.setData(houseqmStatisticTaskIssueStatRspMsgVo);
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


