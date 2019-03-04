package com.longfor.longjian.houseqm.app.controller.v3api;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.ProjectReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatistic.HouseqmStatisticProjectIssueRepairReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatistic.HouseqmStatisticRhyfTaskStatReq;
import com.longfor.longjian.houseqm.app.req.houseqmstatistic.HouseqmStatisticTaskIssueRepairListReq;
import com.longfor.longjian.houseqm.app.service.HouseqmStaticService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.HouseQmStatCategorySituationRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.StatCategoryStatRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatistic.*;
import com.longfor.longjian.houseqm.app.vo.houseqmstatistic.HouseqmStatisticProjectIssueRepairRsp.ApiHouseQmIssueRepairStat;
import com.longfor.longjian.houseqm.consts.CategoryClsTypeEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskRoleTypeEnum;
import com.longfor.longjian.houseqm.consts.TimeStauEnum;
import com.longfor.longjian.houseqm.domain.internalservice.AreaService;
import com.longfor.longjian.houseqm.domain.internalservice.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.DecimalFormat;
import java.util.*;

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
    private static final String SOURCE_NAME_GCJC = "gcjc";
    @Resource
    private HouseqmStaticService houseqmStaticService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskRspService;
    @Resource
    private IHouseqmStatisticService iHouseqmStatisticService;
    @Resource
    private IHouseqmStatService iHouseqmStatService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;
    @Resource
    private AreaService areaService;
    private static final String YMD="yyyy-MM-dd";

    @RequestMapping(value = "rhyf_task_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticRhyfTaskStatRspVo> rhyfTaskStat(@Valid HouseqmStatisticRhyfTaskStatReq req) {
        LjBaseResponse<HouseqmStatisticRhyfTaskStatRspVo> response = new LjBaseResponse<>();
        HouseQmCheckTaskHouseStatInfoVo house = null;
        try {
            if (req.getArea_id() == null) req.setArea_id(0);
            house = iHouseqmStatisticService.getHouseQmHouseQmCheckTaskHouseStatByTaskId(req.getProject_id(), req.getTask_id(), req.getArea_id());
            HouseqmStatisticRhyfTaskStatRspVo item = new HouseqmStatisticRhyfTaskStatRspVo();
            ApiHouseQmRhyfTaskHouseStatVo result = new ApiHouseQmRhyfTaskHouseStatVo();
            result.setHouse_count(house.getHouseCount());
            result.setChecked_count(house.getCheckedCount());
            result.setHas_issue_count(house.getHasIssueCount());
            result.setRepaired_count(house.getRepairedCount());
            result.setApproved_count(house.getApprovedCount());
            result.setRepair_confirm_count(house.getRepairConfirmCount());
            result.setAccept_has_issue_count(house.getAcceptHasIssueCount());
            result.setAccept_no_issue_count(house.getAcceptNoIssueCount());
            result.setAccept_approved_count(house.getAcceptApprovedCount());
            result.setOnly_watch_count(house.getOnlyWatchCount());
            result.setReject_count(house.getRejectCount());

            result.setHouse_checked_percent(MathUtil.getPercentage2(house.getCheckedCount(), house.getHouseCount()));
            result.setHouse_repaired_percent(MathUtil.getPercentage2(house.getRepairedCount(), house.getHasIssueCount()));
            result.setHouse_approveded_percent(MathUtil.getPercentage2(house.getApprovedCount(), house.getRepairedCount()));
            result.setHouse_repair_confirm_percent(MathUtil.getPercentage2(house.getRepairConfirmCount(), house.getApprovedCount()));
            //缺少repair_confirm_count
            item.setItem(result);
            response.setData(item);
            response.setMessage("success");
            response.setResult(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage(e.getMessage());
            response.setResult(1);
        }
        return response;
    }


    @RequestMapping(value = "task_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskStatRspMsgVo> taskStat(@RequestParam(value = "project_id") Integer prodectId,
                                                                     @RequestParam(value = "task_id") Integer taskId,
                                                                     @RequestParam(value = "area_id") Integer areaId,
                                                                     @RequestParam(value = "timestamp") Integer timestamp) {
        CheckTaskHouseStatInfoVo checkTaskHouseStatInfo = houseqmStaticService.getHouseQmCheckTaskHouseStatByTaskId(prodectId, taskId, areaId);
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

    @RequestMapping(value = "get_daterange_options", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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


    @RequestMapping(value = "task_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskListRspMsgVo> taskList(@RequestParam(value = "project_id") Integer projectId,
                                                                     @RequestParam(value = "source") String source,
                                                                     @RequestParam(value = "timestamp") Integer timestamp,
                                                                     @RequestParam(value = "area_id", required = false) Integer areaId) {
        Integer userId = (Integer) sessionInfo.getBaseInfo("userId");
        List<UserInHouseQmCheckTask> checkers = houseqmStaticService.searchUserInHouseQmCheckTaskByUserIdRoleType(userId, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        if (CollectionUtils.isEmpty(checkers)) {
            return null;
        }
        HashMap<Integer, Boolean> checkMap = Maps.newHashMap();
        for (int i = 0; i < checkers.size(); i++) {
            checkMap.put(checkers.get(i).getTaskId(), true);
        }
        List<HouseQmCheckTask> resTask = Lists.newArrayList();


        if (areaId != null) {
            List<HouseQmCheckTask> res = houseqmStaticService.searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(projectId, areaId, getCategoryClsList(source));
            resTask.addAll(res);
        } else {
            List<HouseQmCheckTask> res = houseqmStaticService.searchHouseQmCheckTaskByProjCategoryClsIn(projectId, getCategoryClsList(source));
            resTask.addAll(res);
        }
        List<ApiTaskInfo> items = Lists.newArrayList();
        for (int i = 0; i < resTask.size(); i++) {
            if (checkMap.containsKey(resTask.get(i).getTaskId())) {
                ApiTaskInfo info = new ApiTaskInfo();
                info.setId(resTask.get(i).getTaskId());
                info.setName(resTask.get(i).getName());
                info.setCategory_cls(resTask.get(i).getCategoryCls());
                items.add(info);
            }
        }
        HouseqmStatisticTaskListRspMsgVo vo1 = new HouseqmStatisticTaskListRspMsgVo();
        vo1.setItems(items);
        LjBaseResponse<HouseqmStatisticTaskListRspMsgVo> response = new LjBaseResponse<>();
        response.setData(vo1);
        return response;

    }

    private List<Integer> getCategoryClsList(String source) {
        if (source.equals("gcgl")) {
            source = SOURCE_NAME_GCJC;
        }

        if (source.equals("ydyf")) {
            ArrayList<Integer> objects = Lists.newArrayList();
            objects.add(CategoryClsTypeEnum.FHYS.getId());
            objects.add(CategoryClsTypeEnum.RHYF.getId());
            return objects;
        }
        if (source.equals("gcjc")) {
            ArrayList<Integer> objects = Lists.newArrayList();
            objects.add(CategoryClsTypeEnum.RCJC.getId());
            objects.add(CategoryClsTypeEnum.YDJC.getId());
            objects.add(CategoryClsTypeEnum.JDJC.getId());
            objects.add(CategoryClsTypeEnum.YB.getId());
            objects.add(CategoryClsTypeEnum.FBFX.getId());
            return objects;
        }
        return new ArrayList<>();
    }


    @RequestMapping(value = "project_issue_repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticProjectIssueRepairRsp> projectIssueRepair(@Valid HouseqmStatisticProjectIssueRepairReq req) {
        if (req.getArea_id() == null) req.setArea_id(0);
        if (req.getBegin_on() == null) req.setBegin_on(0);
        if (req.getEnd_on() == null) req.setEnd_on(0);

        IssueRepairStatisticVo result = iHouseqmStatisticService.projectIssueRepair(req.getProject_id(), req.getSource(), req.getArea_id(), req.getBegin_on(), req.getEnd_on(), req.getTimestamp());
        LjBaseResponse<HouseqmStatisticProjectIssueRepairRsp> response = new LjBaseResponse<>();
        HouseqmStatisticProjectIssueRepairRsp data = new HouseqmStatisticProjectIssueRepairRsp();
        ApiHouseQmIssueRepairStat item = data.new ApiHouseQmIssueRepairStat();
        item.setInitime_finish(result.getInitime_finish());
        item.setInitime_finish_count(result.getInitime_finish_count());
        item.setInitime_unfinish(result.getInitime_unfinish());
        item.setInitime_unfinish_count(result.getInitime_unfinish_count());
        item.setNo_plan_end_on(result.getNo_plan_end_on());
        item.setNo_plan_end_on_count(result.getNo_plan_end_on_count());
        item.setOvertime_finish(result.getOvertime_finish());
        item.setOvertime_finish_count(result.getOvertime_finish_count());
        item.setOvertime_unfinish(result.getOvertime_unfinish());
        item.setOvertime_unfinish_count(result.getOvertime_unfinish_count());

        data.setItem(item);
        response.setData(data);
        return response;
    }

    @RequestMapping(value = "task_checkitem_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskCheckitemStatRspMsgVo> taskCheckitemStat(@RequestParam(value = "project_id") Integer projectId,
                                                                                       @RequestParam(value = "task_id") Integer taskId,
                                                                                       @RequestParam(value = "area_id") Integer areaId,
                                                                                       @RequestParam(value = "begin_on") Integer beginOn,
                                                                                       @RequestParam(value = "end_on") Integer endOn,
                                                                                       @RequestParam(value = "timestamp") Integer timestamp) {

        Date begin = null;
        if (beginOn > 0) {
            begin = DateUtil.timeStampToDate(beginOn, YMD);
        }
        Date endOns = null;
        if (endOn > 0) {
            endOns = DateUtil.timeStampToDate(endOn, YMD);
            //endon加一天
            Calendar c = Calendar.getInstance();
            c.setTime(endOns);
            c.add(Calendar.DAY_OF_MONTH, 1);// +1天
            endOns = c.getTime();
        }
        StatCategoryStatRspVo result = iHouseqmStatService.searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(projectId, taskId, areaId, begin, endOns);
        List<HouseQmStatCategorySituationRspVo> items = result.getItems();
        List<HouseQmIssueCategoryStatVo> categoryStatlist = Lists.newArrayList();
        items.forEach(e -> {
            HouseQmIssueCategoryStatVo item = new HouseQmIssueCategoryStatVo();
            item.setKey(e.getKey());
            item.setParentKey(e.getParent_key());
            item.setIssueCount(e.getIssue_count());
            item.setName(e.getName());
            categoryStatlist.add(item);
        });
        HouseqmStatisticTaskCheckitemStatRspMsgVo vo = new HouseqmStatisticTaskCheckitemStatRspMsgVo();
        List<HouseqmStatisticTaskCheckitemStatRspMsgVo.ApiHouseQmCheckItemIssueStat> issueStatList = Lists.newArrayList();
        for (HouseQmIssueCategoryStatVo item : categoryStatlist) {
            if (!"".equals(item.getParentKey())) {
                continue;
            }
            HouseqmStatisticTaskCheckitemStatRspMsgVo.ApiHouseQmCheckItemIssueStat tempItem = new HouseqmStatisticTaskCheckitemStatRspMsgVo().new ApiHouseQmCheckItemIssueStat();
            tempItem.setName(item.getName());
            tempItem.setKey(item.getKey());
            tempItem.setFather_key(item.getParentKey());
            tempItem.setIssue_count(item.getIssueCount());
            issueStatList.add(tempItem);
        }
        vo.setItems(issueStatList);

        //total
        LjBaseResponse<HouseqmStatisticTaskCheckitemStatRspMsgVo> ljBaseResponse = new LjBaseResponse();
        ljBaseResponse.setData(vo);

        return ljBaseResponse;
    }

    /**
     *
     * @return http://192.168.37.159:3000/project/8/interface/api/388
     */
    @RequestMapping(value = "task_issue_repair", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskIssueRepairRsp> taskIssueRepair(@RequestParam(value = "project_id") Integer projectId,
                                                                              @RequestParam(value = "task_id") Integer taskId,
                                                                              @RequestParam(value = "area_id") Integer areaId,
                                                                              @RequestParam(value = "begin_on") Integer beginOn,
                                                                              @RequestParam(value = "end_on") Integer endOn,
                                                                              @RequestParam(value = "timestamp") Integer timestamp) {
        Date begin = null;
        if (beginOn > 0) {
            begin = DateUtil.timeStampToDate(beginOn, YMD);
        }
        Date endOns = null;
        if (endOn > 0) {
            endOns = DateUtil.timeStampToDate(endOn, YMD);
            //endon加一天
            Calendar c = Calendar.getInstance();
            c.setTime(endOns);
            c.add(Calendar.DAY_OF_MONTH, 1);// +1天
            endOns = c.getTime();
        }
        TaskRepairStatVo res = iHouseqmStatisticService.searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(projectId, taskId, areaId, begin, endOns);
        LjBaseResponse<HouseqmStatisticTaskIssueRepairRsp> ljbr = new LjBaseResponse<>();
        HouseqmStatisticTaskIssueRepairRsp result = new HouseqmStatisticTaskIssueRepairRsp();
        HouseqmStatisticProjectIssueRepairRsp data = new HouseqmStatisticProjectIssueRepairRsp();
        ApiHouseQmIssueRepairStat item = data.new ApiHouseQmIssueRepairStat();
        item.setInitime_finish(res.getItem().getInitime_finish());
        item.setInitime_finish_count(res.getItem().getInitime_finish_count());
        item.setInitime_unfinish(res.getItem().getInitime_unfinish());
        item.setInitime_unfinish_count(res.getItem().getInitime_unfinish_count());
        item.setNo_plan_end_on(res.getItem().getNo_plan_end_on());
        item.setNo_plan_end_on_count(res.getItem().getNo_plan_end_on_count());
        item.setOvertime_finish(res.getItem().getOvertime_finish());
        item.setOvertime_finish_count(res.getItem().getOvertime_finish_count());
        item.setOvertime_unfinish(res.getItem().getOvertime_unfinish());
        item.setOvertime_unfinish_count(res.getItem().getOvertime_unfinish_count());
        result.setItem(item);
        ljbr.setData(result);
        return ljbr;
    }


    /**
     * http://192.168.37.159:3000/project/8/interface/api/392
     *
     *
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.HouseqmStatisticCategoryIssueListRspMsgVo>
     * @author hy
     * @date 2018/12/22 0022
     */
    @RequestMapping(value = "task_issue_repair_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskIssueRepairListRsp> taskIssueRepairList(@Validated HouseqmStatisticTaskIssueRepairListReq req) {
        if (req.getArea_id() == null) req.setArea_id(0);
        if (req.getBegin_on() == null) req.setBegin_on(0);
        if (req.getEnd_on() == null) req.setEnd_on(0);
        TaskIssueRepairListVo tvo=new TaskIssueRepairListVo();
        tvo.setProjectId(req.getProject_id());
        tvo.setAreaId(req.getArea_id());
        tvo.setBeginOn(req.getBegin_on());
        tvo.setEndOn(req.getEnd_on());
        tvo.setPage(req.getPage());
        tvo.setPageSize(req.getPage_size());
        tvo.setPlanStatus(req.getPlan_status());
        tvo.setSource(req.getSource());
        tvo.setTimestamp(req.getTimestamp());
        tvo.setTaskId(req.getTask_id());
        HouseqmStatisticCategoryIssueListRspMsgVo result = iHouseqmStatisticService.taskIssueRepairList(tvo);
        LjBaseResponse<HouseqmStatisticTaskIssueRepairListRsp> response = new LjBaseResponse<>();
        HouseqmStatisticTaskIssueRepairListRsp data = new HouseqmStatisticTaskIssueRepairListRsp();
        data.setIssue_list(result.getIssue_list());
        data.setTotal(result.getTotal());
        response.setData(data);
        return response;
    }


    @RequestMapping(value = "category_issue_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticCategoryIssueListRspMsgVo> categoryIssueList(@RequestParam(value = "project_id") Integer projectId,
                                                                                       @RequestParam(value = "category_key") String categoryKey,
                                                                                       @RequestParam(value = "area_id") Integer areaId,
                                                                                       @RequestParam(value = "page") Integer page,
                                                                                       @RequestParam(value = "page_size") Integer pageSize
    ) {


        HouseQmCheckTaskIssueVoRsp issueVo = iHouseqmStatisticService.searchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged(projectId, categoryKey, areaId, page, pageSize);
        List<HouseQmCheckTaskIssueOnlineInfoVo> infoList = issueVo.getItems();
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
        vo.setTotal(issueVo.getTotal());
        ljr.setData(vo);
        return ljr;
    }


    @RequestMapping(value = "project_building_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticProjectBuildingListRspMsgVo> projectBuildingList(@RequestParam(value = "project_id") Integer prodectId,
                                                                                           @RequestParam(value = "timestamp") Integer timestamp) {
        List<ApiBuildingInfo> buildingInfoList = houseqmStaticService.pSelectByFatherId(prodectId);
        HouseqmStatisticProjectBuildingListRspMsgVo msgVo = new HouseqmStatisticProjectBuildingListRspMsgVo();
        msgVo.setItems(buildingInfoList);
        LjBaseResponse<HouseqmStatisticProjectBuildingListRspMsgVo> response = new LjBaseResponse<>();
        response.setData(msgVo);

        return response;
    }

    @RequestMapping(value = "task_building_list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskBuildingListRspMsgVo> taskBuildingList(@RequestParam(value = "project_id") Integer prodectId,
                                                                                     @RequestParam(value = "task_id") Integer taskId,
                                                                                     @RequestParam(value = "timestamp") Integer timestamp) {
        HouseqmStatisticTaskBuildingListRspMsgVo msgVo = houseqmStaticService.taskBuildingList(prodectId, taskId);
        LjBaseResponse<HouseqmStatisticTaskBuildingListRspMsgVo> response = new LjBaseResponse<>();
        response.setData(msgVo);
        return response;
    }

    @RequestMapping(value = "task_issue_stat", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseqmStatisticTaskIssueStatRspMsgVo> taskIssueStat(@RequestParam(value = "project_id") Integer projectId,
                                                                               @RequestParam(value = "task_id") Integer taskId,
                                                                               @RequestParam(value = "area_id") Integer areaId,
                                                                               @RequestParam(value = "timestamp") Integer timestamp) {
        TaskStatVo.IssueStatVo vo = iHouseqmStatisticService.getCheckTaskIssueTypeStatByTaskIdAreaId(taskId, areaId);
        HouseQmCheckTaskHouseStatInfoVo infoVo = iHouseqmStatisticService.getHouseQmHouseQmCheckTaskHouseStatByTaskId(projectId, taskId, areaId);
        ApiHouseQmTaskStatVo houseQmTaskStatVo = new ApiHouseQmTaskStatVo();
        houseQmTaskStatVo.setIssue_approveded_count(vo.getIssue_approveded_count());
        houseQmTaskStatVo.setIssue_assigned_count(vo.getIssue_assigned_count());
        houseQmTaskStatVo.setIssue_count(vo.getIssue_count());
        houseQmTaskStatVo.setIssue_recorded_count(vo.getIssue_recorded_count());
        houseQmTaskStatVo.setIssue_repaired_count(vo.getIssue_repaired_count());
        houseQmTaskStatVo.setRecord_count(vo.getRecord_count());
        houseQmTaskStatVo.setHouse_count(infoVo.getHouseCount());
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
        return df.format((float) a / (float) b * 100);
    }

}


