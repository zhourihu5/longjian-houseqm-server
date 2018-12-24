package com.longfor.longjian.houseqm.app.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.lang.String;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.consts.*;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.MathUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Houyan
 * @date 2018/12/18 0018 11:21
 */
@Repository
@Service
@Slf4j
public class HouseqmStatisticServiceImpl implements IHouseqmStatisticService {

    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    AreaService areaService;

    @Resource
    FileResourceService fileResourceService;

    @Resource
    CategoryV3Service categoryV3Service;

    @Resource
    CheckItemV3Service checkItemV3Service;

    /**
     * @param taskId
     * @param areaId
     * @return
     */
    public TaskStatVo.IssueStatVo getCheckTaskIssueTypeStatByTaskIdAreaId(Integer taskId, Integer areaId) {
        String areaPath = "";
        if (areaId > 0) {
            Area areaInfo = areaService.selectById(areaId);
            areaPath = areaInfo.getPath() + areaInfo.getId() + "/%";
        }
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByTaskIdAndAreaPathAndIdLike(taskId, areaPath);
        TaskStatVo.IssueStatVo result = new TaskStatVo().new IssueStatVo();
        result.setIssue_approveded_count(0);
        result.setRecord_count(0);
        result.setIssue_count(0);
        result.setIssue_repaired_count(0);
        result.setIssue_assigned_count(0);
        result.setIssue_recorded_count(0);
        for (HouseQmCheckTaskIssue issue : issues) {
            Integer status = issue.getStatus();
            //处理详细统计数
            if (status == HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId()) {
                result.setIssue_recorded_count(issue.getPosX());
            } else if (status == HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId()) {
                result.setIssue_assigned_count(issue.getPosX());
            } else if (status == HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId()) {
                result.setIssue_repaired_count(issue.getPosX());
            } else if (status == HouseQmCheckTaskIssueStatusEnum.CheckYes.getId()) {
                result.setIssue_approveded_count(issue.getPosX());
            }
            //处理状态统计数
            if (status == HouseQmCheckTaskIssueStatusEnum.NoProblem.getId()) {
                result.setRecord_count(result.getRecord_count() + issue.getPosX());
            } else if (status == HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId() || status == HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId() || status == HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId() || status == HouseQmCheckTaskIssueStatusEnum.CheckYes.getId()) {
                result.setIssue_count(result.getIssue_count() + issue.getPosX());
            }
        }
        return result;
    }

    /**
     * @param projectId
     * @param taskId
     * @param areaId
     * @return
     */
    public TaskStatVo.HouseStatVo getHouseQmCheckTaskHouseStatByTaskId(Integer projectId, Integer taskId, Integer areaId) {
        try {
            TaskStatVo.HouseStatVo houseStatVo = new TaskStatVo().new HouseStatVo();
            houseStatVo.setHas_issue_count(0);
            houseStatVo.setRepaired_count(0);
            houseStatVo.setApproved_count(0);
            houseStatVo.setChecked_count(0);
            houseStatVo.setHouse_count(0);
            //读取任务
            HouseQmCheckTask task = houseQmCheckTaskService.selectByProjectIdAndTaskId(projectId, taskId);
            // 获取出任务下的区域与检验类型的交集
            List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(task.getAreaIds(), ",");
            List<Integer> areaTypes = StringSplitToListUtil.splitToIdsComma(task.getAreaTypes(), ",");
            if (areaIds.size() == 0 || areaTypes.size() == 0) {
                return houseStatVo;
            }
            List<Integer> areaIdsList = Lists.newArrayList();
            List<Integer> areaIdList = Lists.newArrayList();
            areaIdList.add(areaId);
            if (areaId > 0) {
                areaIdsList = areaService.getIntersectAreas(areaIds, areaIdList);
            }
            List<Area> areas = null;
            if (areaIdsList.isEmpty()) {
                areas = areaService.searchAreaListByRootIdAndTypes(projectId, areaIds, areaTypes);
            } else {
                areas = areaService.searchAreaListByRootIdAndTypes(projectId, areaIdsList, areaTypes);
            }
            houseStatVo.setHouse_count(areas.size());

            //计算总户数
            // 找出拥有问题的最小状态，用来区分是否所有问题都处理完了
            Map<Integer, IssueMinStatusVo> areaIssueMap = getIssueMinStatusMapByTaskIdAndAreaId(taskId, areaId, true);
            Map<Integer, IssueMinStatusVo> checkedAreaIssueMap = getIssueMinStatusMapByTaskIdAndAreaId(taskId, areaId, false);
            houseStatVo.setChecked_count(checkedAreaIssueMap.size());
            for (Map.Entry<Integer, IssueMinStatusVo> status : areaIssueMap.entrySet()) {
                houseStatVo.setHas_issue_count(houseStatVo.getHas_issue_count() + 1);
                if (status.getValue().getMinStatus() == HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId()) {
                    houseStatVo.setRepaired_count(houseStatVo.getRepaired_count() + 1);
                }
                if (status.getValue().getMinStatus() == HouseQmCheckTaskIssueStatusEnum.CheckYes.getId()) {
                    houseStatVo.setRepaired_count(houseStatVo.getRepaired_count() + 1);
                    houseStatVo.setApproved_count(houseStatVo.getApproved_count() + 1);
                }
            }
            return houseStatVo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param projectId
     * @param taskId
     * @param areaId
     * @param beginOn
     * @param endOn
     * @return
     */
    public TaskRepairStatVo searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn) {
        List<IssueRepairCount> issueCounts = null;
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        HashMap<String, Object> condiMap = Maps.newHashMap();
        // 以下条件成立时调用对应成立时的方法。
        if (areaId > 0) {
            condiMap.put("areaId", "%/"+areaId+"/%");//模糊查询 AreaPathAndId like '%%/ /areaId%%'
        }
        if (beginOn.getTime() / 1000 > 0) {
            condiMap.put("beginOn", beginOn);
        }
        if (endOn.getTime() / 1000 > 0) {
            condiMap.put("endOn", endOn);
        }
        Date now = new Date();
        condiMap.put("now", now);
        condiMap.put("projectId", projectId);
        condiMap.put("taskId", taskId);
        condiMap.put("types", types);
        condiMap.put("deleted", "false");
        issueCounts = houseQmCheckTaskIssueService.selectByProjectIdAndTaskIdAndTyeInAndDongTai(condiMap);
        IssueRepairCount ic = issueCounts.get(0);
        TaskRepairStatVo taskRepairStatVo = new TaskRepairStatVo();
        TaskRepairStatVo.TaskRepairVo item = taskRepairStatVo.new TaskRepairVo();
        if (ic.getTotal() == 0) {
            ic.setTotal(1);
        }
        DecimalFormat f = new DecimalFormat("0.00");
        String iniTimeFinish = MathUtil.getPercentage(ic.getInitimeFinish(), ic.getTotal());
        String iniTimeUnFinish = MathUtil.getPercentage(ic.getInitimeUnfinish(), ic.getTotal());
        String overTimeFinish = MathUtil.getPercentage(ic.getOvertimeFinish(), ic.getTotal());
        String overTimeUnFinish = MathUtil.getPercentage(ic.getOvertimeUnfinish(), ic.getTotal());
        String noPlanEndOn = MathUtil.getPercentage(ic.getNoPlanEndOn(), ic.getTotal());

        item.setInitime_finish(iniTimeFinish);
        item.setInitime_unfinish(iniTimeUnFinish);
        item.setOvertime_finish(overTimeFinish);
        item.setOvertime_unfinish(overTimeUnFinish);
        item.setNo_plan_end_on(noPlanEndOn);

        item.setInitime_finish_count(ic.getInitimeFinish());
        item.setInitime_unfinish_count(ic.getInitimeUnfinish());
        item.setOvertime_finish_count(ic.getOvertimeFinish());
        item.setOvertime_unfinish_count(ic.getOvertimeUnfinish());
        item.setNo_plan_end_on_count(ic.getNoPlanEndOn());
        item.setTotal_count(ic.getTotal());

        taskRepairStatVo.setItem(item);
        return taskRepairStatVo;
    }

    /**
     * @param
     * @return com.longfor.longjian.houseqm.app.vo.ProjectDailyListVo
     * @author hy
     * @date 2018/12/22 0022
     */
    @Override
    public HouseqmStatisticCategoryIssueListRspMsgVo taskIssueRepairList(Integer projectId,Integer taskId,Integer areaId,Integer beginOn,Integer endOn,Integer timestamp,Integer planStatus,String source,Integer page,Integer pageSize) {
        Date beginOn1 = DateUtil.timeStampToDate(beginOn, "yyyy-MM-dd");
        Date endOn1 = DateUtil.timeStampToDate(endOn, "yyyy-MM-dd");
        if (beginOn > 0) {
            beginOn1 = DateUtil.timeStampToDate(beginOn, "yyyy-MM-dd");
        }
        if (endOn > 0) {
            Calendar c = Calendar.getInstance();
            Date date = DateUtil.timeStampToDate(endOn, "yyyy-MM-dd");
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endOn1 = DateUtil.timeStampToDate((int) (c.getTime().getTime() / 1000), "yyyy-MM-dd");
        }

        List<Integer> categoryClsList = getCategoryClsList(source);
        HouseQmCheckTaskIssueListVo issueListVo = searchHouseQmCheckTaskIssueByProjTaskIdClsInAreaIdPlanStatusBeginOnEndOnPage(projectId, taskId, categoryClsList, areaId, planStatus, beginOn1, endOn1, page, pageSize);

        List<Integer> areaIds = Lists.newArrayList();
        List<String> categoryKeys = Lists.newArrayList();
        List<String> checkItemKeys = Lists.newArrayList();
        List<String> attachmentMd5List = Lists.newArrayList();

        List<HouseQmCheckTaskIssue> resIssues = issueListVo.getHouseQmCheckTaskIssues();
        resIssues.forEach(item -> {
            areaIds.add(item.getAreaId());
            categoryKeys.addAll(StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getCategoryKey(), "/", "/"));
            checkItemKeys.addAll(StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getCheckItemPathAndKey(), "/", "/"));
            attachmentMd5List.addAll(StringSplitToListUtil.splitToStringComma(item.getAttachmentMd5List(), ","));
        });
        //列表元素去重
        List areaIdList = CollectionUtil.removeDuplicate(areaIds);
        List categoryKeyList = CollectionUtil.removeDuplicate(categoryKeys);
        List checkItemKeyList = CollectionUtil.removeDuplicate(checkItemKeys);

        //调用file_resource service 方法 files
        List<FileResource> files = fileResourceService.searchFileResourceByFileMd5InAndNoDeleted(attachmentMd5List);
        HashMap<String, String> fileMap = Maps.newHashMap();
        files.forEach(item -> {
            fileMap.put(item.getFileMd5(), item.getStoreKey());
        });

        AreaMapVo areaMap = createAreasMapByLeaveIds(areaIdList);
        List<CategoryV3> categorys = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeyList);

        categorys.forEach(item -> {
            List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getPath(), "/", "/");
            list.add(item.getKey());
            categoryKeyList.addAll(list);
        });
        //去重 categoryKeyList
        CollectionUtil.removeDuplicate(categoryKeyList);

        List<CategoryV3> categoryV3s = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeyList);

        CategoryV3MapVo categoryMap = newCategoryMap(categorys);

        List<CheckItemV3> checkItems = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkItemKeyList);

        CheckItemV3MapVo checkItemV3Map = newCheckItemMap(checkItems);
        //
        HouseqmStatisticCategoryIssueListRspMsgVo issueListRspMsgVo = new HouseqmStatisticCategoryIssueListRspMsgVo();
        List<HouseqmStatisticCategoryIssueListRspMsgVo.ApiTaskIssueRepairListRsp> issueList = Lists.newArrayList();
        resIssues.forEach(item -> {
            HouseqmStatisticCategoryIssueListRspMsgVo.ApiTaskIssueRepairListRsp apiTaskIssueRepairListRsp = issueListRspMsgVo.new ApiTaskIssueRepairListRsp();
            apiTaskIssueRepairListRsp.setId(item.getId());
            apiTaskIssueRepairListRsp.setProjectId(item.getProjectId());
            apiTaskIssueRepairListRsp.setTaskId(item.getTaskId());
            apiTaskIssueRepairListRsp.setUuid(item.getUuid());
            apiTaskIssueRepairListRsp.setTitle(item.getTitle());
            apiTaskIssueRepairListRsp.setTye(item.getTyp());
            apiTaskIssueRepairListRsp.setContent(item.getContent());
            apiTaskIssueRepairListRsp.setCondition(item.getCondition());
            apiTaskIssueRepairListRsp.setStatus(item.getStatus());

            apiTaskIssueRepairListRsp.setPlanEndOn(DateUtil.datetimeToTimeStamp(item.getPlanEndOn()));
            apiTaskIssueRepairListRsp.setAttachmentMd5List(item.getAttachmentMd5List());
            apiTaskIssueRepairListRsp.setClientCreateAt(DateUtil.datetimeToTimeStamp(item.getClientCreateAt()));
            apiTaskIssueRepairListRsp.setUpdateAt(DateUtil.datetimeToTimeStamp(item.getUpdateAt()));

            List<String> list = StringSplitToListUtil.splitToStringComma(item.getAttachmentMd5List(), ",");
            apiTaskIssueRepairListRsp.setAttachmentUrlList(new ArrayList<String>());
            list.forEach(fm ->{
                if (fileMap.containsKey(fm)){
                    List<String> attachmentUrlList = apiTaskIssueRepairListRsp.getAttachmentUrlList();
                    attachmentUrlList.add(fileMap.get(fm));
                    apiTaskIssueRepairListRsp.setAttachmentUrlList(attachmentUrlList);
                }
            });
            apiTaskIssueRepairListRsp.setAreaPathName(areaMap.getPathNames(item.getAreaId()));
            apiTaskIssueRepairListRsp.setCategoryPathName(categoryMap.getFullNamesByKey(item.getCategoryKey()));
            apiTaskIssueRepairListRsp.setCheckItemPathName(checkItemV3Map.getFullNamesByKey(item.getCheckItemKey()));
            issueList.add(apiTaskIssueRepairListRsp);
        });
        issueListRspMsgVo.setIssueList(issueList);
        issueListRspMsgVo.setTotal(issueListVo.getTotal());
        return issueListRspMsgVo;
    }


    /**
     * @param projectId
     * @param taskId
     * @param categoryClsList
     * @param areaId
     * @param planStatus
     * @param beginOn
     * @param endOn
     * @param page
     * @param pageSize
     * @return com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueListVo
     * @author hy
     * @date 2018/12/22 0022
     */
    private HouseQmCheckTaskIssueListVo searchHouseQmCheckTaskIssueByProjTaskIdClsInAreaIdPlanStatusBeginOnEndOnPage(Integer projectId, Integer taskId, List<Integer> categoryClsList, Integer areaId, Integer planStatus, Date beginOn, Date endOn, Integer page, Integer pageSize) {
        HashMap<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId", projectId);
        condiMap.put("categoryCls", categoryClsList);
        if (taskId > 0) condiMap.put("taskId", taskId);
        if (areaId > 0) condiMap.put("areaId", "/"+areaId+"/");
        if (beginOn.getTime() / 1000 > 0) condiMap.put("clientCreateAtGte", beginOn);
        if (endOn.getTime() / 1000 > 0) condiMap.put("clientCreateAtLte", endOn);
        ArrayList<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        condiMap.put("typs", typs);
        Date now = new Date();
        String nowStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
        condiMap.put("now", nowStr);
        if (planStatus==HouseQmIssuePlanStatusEnum.OnTimeFinish.getId()){
            condiMap.put("status1", true);
        }else if (planStatus==HouseQmIssuePlanStatusEnum.UnOnTimeNotFinish.getId()){
            condiMap.put("status2", true);
        }else if (planStatus==HouseQmIssuePlanStatusEnum.NoSettingTime.getId()){
            condiMap.put("status3", true);
        }else if (planStatus==HouseQmIssuePlanStatusEnum.OverTimeFinish.getId()){
            condiMap.put("status4", true);
        }else if (planStatus==HouseQmIssuePlanStatusEnum.OverTimeNotFinish.getId()){
            condiMap.put("status5", true);
        }
        int start=0;
        if (page>0){
            start=(page-1)*pageSize;
        }
        int total=houseQmCheckTaskIssueService.selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai(condiMap);
        condiMap.put("start", start);
        condiMap.put("pageSize", pageSize);
        List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues=houseQmCheckTaskIssueService.selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(condiMap);

        HouseQmCheckTaskIssueListVo houseQmCheckTaskIssueListVo = new HouseQmCheckTaskIssueListVo();
        houseQmCheckTaskIssueListVo.setTotal(total);
        houseQmCheckTaskIssueListVo.setHouseQmCheckTaskIssues(houseQmCheckTaskIssues);
        return houseQmCheckTaskIssueListVo;
    }

    /**
     *
     * @author hy
     * @date 2018/12/22 0022
     * @param module
     * @return java.util.List<java.lang.Integer>
     */
    private List<Integer> getCategoryClsList(String module) {
        String source=null;
        if ("gcgl".equals(module)) { //兼容app 端
            source = CategoryClsConst.SOURCE_NAME_GCJC;
        }
        HashMap<String, List<Integer>> categoryClsMap = Maps.newHashMap();
        ArrayList<Integer> ydyf = Lists.newArrayList();
        ydyf.add(CategoryClsTypeEnum.FHYS.getId());
        ydyf.add(CategoryClsTypeEnum.RHYF.getId());

        ArrayList<Integer> gcjc = Lists.newArrayList();
        gcjc.add(CategoryClsTypeEnum.RCJC.getId());
        gcjc.add(CategoryClsTypeEnum.YDJC.getId());
        gcjc.add(CategoryClsTypeEnum.JDJC.getId());
        gcjc.add(CategoryClsTypeEnum.YB.getId());
        gcjc.add(CategoryClsTypeEnum.FBFX.getId());
        categoryClsMap.put(CategoryClsConst.SOURCE_NAME_YDYF, ydyf);
        categoryClsMap.put(CategoryClsConst.SOURCE_NAME_GCJC, gcjc);

        return categoryClsMap.get(source);
    }

    /**
     *  
     * @author hy
     * @date 2018/12/23 0023
     * @param areaIdList 
     * @return com.longfor.longjian.houseqm.app.vo.AreaMapVo
     */
    private AreaMapVo createAreasMapByLeaveIds(List<Integer> areaIdList) {
        List<Area> areas = areaService.selectAreasByIdInAreaIds(areaIdList);
        Map<Integer, Area> areaMap = areas.stream().collect(Collectors.toMap(Area::getId, a -> a, (k1, k2) -> k1));
        AreaMapVo areaMapVo = new AreaMapVo();
        areaMapVo.setAreas(areaMap);
        areaMapVo.setList(areas);
        return areaMapVo;
    }

    /**
     *
     * @author hy
     * @date 2018/12/23 0023
     * @param categorys
     * @return com.longfor.longjian.houseqm.app.vo.CategoryV3MapVo
     */
    private CategoryV3MapVo newCategoryMap(List<CategoryV3> categorys){
        CategoryV3MapVo categoryV3MapVo = new CategoryV3MapVo();
        Map<String, CategoryV3> categoryV3Map = categorys.stream().collect(Collectors.toMap(CategoryV3::getKey, a -> a, (k1, k2) -> k1));
        categoryV3MapVo.setCategoryV3Map(categoryV3Map);
        return categoryV3MapVo;
    }

    /**
     *  
     * @author hy
     * @date 2018/12/23 0023
     * @param checkItemV3s 
     * @return com.longfor.longjian.houseqm.app.vo.CheckItemV3MapVo
     */
    private CheckItemV3MapVo newCheckItemMap(List<CheckItemV3> checkItemV3s){
        CheckItemV3MapVo checkItemV3MapVo = new CheckItemV3MapVo();
        Map<String, CheckItemV3> checkItemV3Map = checkItemV3s.stream().collect(Collectors.toMap(CheckItemV3::getKey, a -> a, (k1, k2) -> k1));
        checkItemV3MapVo.setCheckItemV3Map(checkItemV3Map);
        return checkItemV3MapVo;
    }
    
    /**
     * @param taskId
     * @param areaId
     * @param onlyIssue
     * @return
     */
    private Map<Integer, IssueMinStatusVo> getIssueMinStatusMapByTaskIdAndAreaId(Integer taskId, Integer areaId, Boolean onlyIssue) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        List<HouseQmCheckTaskIssueAreaGroupModel> result = Lists.newArrayList();

        if (onlyIssue && areaId > 0) {
            result = houseQmCheckTaskIssueService.selectByTaskIdAndTyeInAndAreaPathAndIdLike(taskId, types, "%%/" + areaId + "/%%");
        } else if (onlyIssue && areaId <= 0) {
            result = houseQmCheckTaskIssueService.selectByTaskIdAndTyeIn(taskId, types);
        } else if (!onlyIssue && areaId > 0) {
            result = houseQmCheckTaskIssueService.selectHouseQmCheckTaskIssueAreaGroupModelByTaskIdAndAreaPathAndIdLike(taskId, "%%/" + areaId + "/%%");
        } else {
            result = houseQmCheckTaskIssueService.selectByTaskId(taskId);
        }

        HashMap<Integer, IssueMinStatusVo> maps = Maps.newHashMap();
        for (HouseQmCheckTaskIssueAreaGroupModel area : result) {
            List<Integer> aIds = StringSplitToListUtil.splitToIdsComma(area.getAreaPath(), "/");
            if (aIds.size() > 0) {
                IssueMinStatusVo minStatus = new IssueMinStatusVo();
                minStatus.setCount(area.getExtendCol());
                minStatus.setMinStatus(area.getStatus());
                maps.put(aIds.get(aIds.size() - 1), minStatus);
            }
        }
        return maps;
    }

}
