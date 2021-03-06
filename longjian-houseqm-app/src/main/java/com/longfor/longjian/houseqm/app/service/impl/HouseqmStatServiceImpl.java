package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.service.HouseqmStaticService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.HouseQmStatCategorySituationRspVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.InspectionHouseStatusInfoVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstat.StatCategoryStatRspVo;
import com.longfor.longjian.houseqm.consts.*;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.dto.CheckerIssueStatusStatDto;
import com.longfor.longjian.houseqm.dto.RepaireIssueStatusStatDto;
import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.Area;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.zj2db.RepossessionStatus;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Houyan
 * @date 2018/12/14 0014 19:45
 */
@Repository
@Service
@Slf4j
public class HouseqmStatServiceImpl implements IHouseqmStatService {


    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private UserService userService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private AreaService areaService;
    @Resource
    private HouseqmStatisticServiceImpl houseqmStatisticService;
    @Resource
    private RepossessionStatusService repossessionStatusService;
    @Resource
    private HouseqmStaticService houseqmStaticService;

    private static final String PATH_AND_ID_REPEX="%s%d/";
    private static final String PROJECT_ID="projectId";
    private static final String TASK_ID="taskId";
    private static final String YYYY_MM_DD="yyyy-MM-dd";
    @Override
    public List<Integer> searchInspectionAreaIdsByConditions(Integer projectId, Integer taskId, Integer areaId, Integer status, Integer issueStatus) {
        //取出该任务下的所有户path
        List<String> areaPaths = Lists.newArrayList();
        HouseQmCheckTask task = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(projectId, taskId);
        if (task == null) return Lists.newArrayList();
        List<Integer> aids = StringUtil.strToInts(task.getAreaIds(), ",");
        List<Integer> types = StringUtil.strToInts(task.getAreaTypes(), ",");
        List<Area> areas = areaService.searchAreaListByRootIdAndTypes(projectId, aids, types);
        for (Area area : areas) {
            areaPaths.add(String.format(PATH_AND_ID_REPEX, area.getPath(), area.getId()));
        }
        //取出对应状态条件path
        if (status.equals(StatisticFormInspectionStatusEnum.UnChecked.getId())) {
            // 找出未查验，就是	所有－已查验的
            List<String> checkedAreaPaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, false, null, areaId);
            areaPaths.removeAll(checkedAreaPaths);//差集
        } else if (status.equals(StatisticFormInspectionStatusEnum.Checked.getId())) {
            // 找出已查验，就是	已查验的 交集 所有
            List<String> checkedAreaPaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, false, null, areaId);
            areaPaths.retainAll(checkedAreaPaths);
        }
        // 区分是否存在问题
        if (issueStatus.equals(StatisticFormInspectionIssueStatusEnum.HasIssue.getId())) {
            List<String> hasIssueAreaPaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, null, areaId);
            areaPaths.retainAll(hasIssueAreaPaths);//交集
        } else if (issueStatus.equals(StatisticFormInspectionIssueStatusEnum.NoProblem.getId())) {
            // 不存在问题的包括了那些未检查，就是 所有-已查验存在问题的
            List<String> noIssueAreaPaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, null, areaId);
            areaPaths.removeAll(noIssueAreaPaths);
        }
        //用areaId来过滤掉那些不属范围内的path
        return returnAreaIdsResult(areaId,areaPaths);
    }

    private List<Integer> returnAreaIdsResult(Integer areaId,List<String> areaPaths ){
        if (areaId > 0) {
            areaPaths = filterAreaPathListByRootAreaId(areaId, areaPaths);
        }
        //排序后返回areaId
        Collections.sort(areaPaths);
        ArrayList<Integer> result = Lists.newArrayList();
        for (String p : areaPaths) {
            List<Integer> ids = StringUtil.strToInts(p, "/");
            if (ids.isEmpty()) continue;
            result.add(ids.get(ids.size() - 1));
        }
        return result;
    }
    @Override
    public List<Integer> searchRepossessInspectionAreaIdsByConditions(Integer projectId, Integer taskId, Integer areaId, Integer status, Integer issueStatus, Date startTime, Date endTime) {
        //取出该任务下的所有户path
        List<String> areaPaths ;
        HouseQmCheckTask task = houseQmCheckTaskService.getHouseQmCheckTaskByProjTaskId(projectId, taskId);
        List<Integer> aids = StringUtil.strToInts(task.getAreaIds(), ",");
        List<Integer> types = StringUtil.strToInts(task.getAreaTypes(), ",");
        List<Area> areas = areaService.searchAreaListByRootIdAndTypes(projectId, aids, types);// 获取对应项目 区域下的楼栋 层 户
        List<String> taskAreaPaths = Lists.newArrayList();
        for (Area area : areas) {
            taskAreaPaths.add(String.format(PATH_AND_ID_REPEX, area.getPath(), area.getId()));
        }

        //筛选 户状态 取出对应状态条件path 库表 repossession_status 库中无数据 导致筛选条件无意义
        if (!status.equals(StatisticFormRepossessionStatusEnum.All.getId())) {
            if (status.equals(StatisticFormRepossessionStatusEnum.None.getId())) {//户状态 未检查
                List<String> checkedAreaPaths = getRepossessAreaPathListByTaskIdAndStatusesAndClientUpdateAt(taskId, Collections.singletonList(StatisticFormRepossessionStatusEnum.None.getId()), startTime, endTime);
                //求差集
                taskAreaPaths.removeAll(checkedAreaPaths);
                areaPaths = taskAreaPaths;
            } else {//业主只看房／已查验  业主收楼 业主拒绝收楼
                areaPaths = getRepossessAreaPathListByTaskIdAndStatusesAndClientUpdateAt(taskId, Collections.singletonList(status), startTime, endTime);
            }
        } else {//全部
            areaPaths = taskAreaPaths;
        }
        // 筛选问题状态
        // 区分是否存在问题
        if (issueStatus.equals(StatisticFormInspectionIssueStatusEnum.HasIssue.getId())) {// 有问题
            List<String> checkedAreaPaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, null, areaId);
            //求交集 源码求交集
            areaPaths.retainAll(checkedAreaPaths);
        } else if (issueStatus.equals(StatisticFormInspectionIssueStatusEnum.NoProblem.getId())) {//无问题
            // 不存在问题的包括了那些未检查，就是 所有-已查验存在问题的
            List<String> checkedAreaPaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, null, areaId);
            // 取差集
            areaPaths.removeAll(checkedAreaPaths);

        }
        //过滤掉不在任务中的path
        //出现此种情况的原因：在已上传验房报告的情况下，将已有数据的楼栋从任务中移除掉了

        //用areaId来过滤掉那些不属范围内的path
        return returnAreaIdsResult(areaId,areaPaths);
    }

    //依据根节点来过滤掉非子项的areapath
    public List<String> filterAreaPathListByRootAreaId(Integer rootAreaId, List<String> areaPaths) {
        Area area = areaService.selectById(rootAreaId);
        if (area == null) return Lists.newArrayList();
        String apath = String.format(PATH_AND_ID_REPEX, area.getPath(), area.getId());
        ArrayList<String> result = Lists.newArrayList();
        for (String path : areaPaths) {
            if (path.startsWith(apath)) {
                result.add(path);
            }
        }
        return result;
    }

    //通过任务id和需要的验房状态（已经收楼）以及收楼时间获取对应的area_path_and_id
    //通过任务id和需要的验房状态获取对应的area_path_and_id // RepossessionStatus 库中无数据 导致筛选条件无意义
    public List<String> getRepossessAreaPathListByTaskIdAndStatusesAndClientUpdateAt(int taskId, List<Integer> repossStatuses, Date startTime, Date endTime) {

        List<RepossessionStatus> reposs = repossessionStatusService.searchByTaskIdAndStatusInAndStatusClientUpdateAt(taskId, repossStatuses, startTime, endTime);
        List<String> result = Lists.newArrayList();
        for (RepossessionStatus r : reposs) {
            //补全兼容area_path_出错的记录
            result.add("/" + r.getAreaPathAndId().replaceFirst("/", ""));
        }
        return result;
    }

    // 格式化验房信息
    @Override
    @SuppressWarnings("squid:S3776")
    public List<InspectionHouseStatusInfoVo> formatFenhuHouseInspectionStatusInfoByAreaIds(Integer issueStatus,Integer taskId, List<Integer> ids) {
        List<InspectionHouseStatusInfoVo> result = Lists.newArrayList();
        if (ids.isEmpty()) return result;

        //获取区域信息
        AreaMapVo areaMap = houseqmStatisticService.createAreasMapByLeaveIds(ids);
        //获取问题map
        Map<Integer, List<HouseQmCheckTaskIssue>> issuesMap = searchHouseQmCheckTaskIssueMapByTaskIdAreaIds(issueStatus,taskId, ids);

        for (Integer aid : ids) {
            InspectionHouseStatusInfoVo item = new InspectionHouseStatusInfoVo();
            // 补全区域信息
            item.setAreaId(aid);
            item.setTaskId(taskId);
            item.setAreaName(areaMap.getName(aid));
            item.setAreaPathName(areaMap.getPathNames(aid));

            item.setIssueRepairedCount(0);
            item.setIssueApprovededCount(0);
            item.setIssueCount(0);
            //补全问题信息
            List<HouseQmCheckTaskIssue> issues = issuesMap.get(aid);
            if (issuesMap.containsKey(aid)) {
                item.setStatus(StatisticFormInspectionStatusEnum.Checked.getId());
                for (HouseQmCheckTaskIssue issue : issues) {
                    HouseQmCheckTaskIssueStatusEnum e = null;
                    for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                        if (value.getId().equals(issue.getStatus())) e = value;
                    }
                    if (e != null) {
                        switch (e) {
                            case ReformNoCheck:
                                item.setIssueRepairedCount(item.getIssueRepairedCount() + 1);
                                break;
                            case CheckYes:
                                item.setIssueRepairedCount(item.getIssueRepairedCount() + 1);
                                item.setIssueApprovededCount(item.getIssueApprovededCount() + 1);
                                break;
                            default:
                                break;
                        }
                    }
                    HouseQmCheckTaskIssueTypeEnum e1 = null;
                    for (HouseQmCheckTaskIssueTypeEnum value : HouseQmCheckTaskIssueTypeEnum.values()) {
                        if (value.getId().equals(issue.getTyp())) e1 = value;
                    }
                    if (e1 != null) {
                        switch (e1) {
                            case FindProblem:
                            case Difficult:
                                item.setIssueCount(item.getIssueCount() + 1);
                                break;
                            default:
                                break;
                        }
                    }
                }
            } else {
                item.setStatus(StatisticFormInspectionStatusEnum.UnChecked.getId());
            }

            item.setStatusName(StatisticFormInspectionStatusEnum.getName(item.getStatus()));
            result.add(item);
        }
        return result;
    }

    // 通过taskId和areaId获取按area_id分组的问题map 添加issueStatus 作区分
    @SuppressWarnings("squid:S3776")
    public Map<Integer, List<HouseQmCheckTaskIssue>> searchHouseQmCheckTaskIssueMapByTaskIdAreaIds(Integer issueStatus,int taskId, List<Integer> areaIds) {
        Map<Integer, List<HouseQmCheckTaskIssue>> result = Maps.newHashMap();

        String regexp = areaService.getRootRegexpConditionByAreaIds(areaIds);
        // 此处go源码 可能有点问题 ，taskId
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByTaskIdAndAreaPathAndIdRegexp(taskId, regexp);

        //排序后用area_path_and_id来聚合时间复杂度更低
        List<Area> areas = areaService.selectByAreaIds(areaIds);
        Map<String, Area> areaMap = Maps.newHashMap();
        List<String> areaPaths = Lists.newArrayList();
        for (Area a : areas) {
            String pAId = String.format(PATH_AND_ID_REPEX, a.getPath(), a.getId());
            areaMap.put(pAId, a);
            areaPaths.add(pAId);
        }
        Map<String, HouseQmCheckTaskIssue> issueMap = new IdentityHashMap<>();
        List<String> issuePaths = Lists.newArrayList();
        for (HouseQmCheckTaskIssue issue : issues) {
            issueMap.put(issue.getAreaPathAndId(), issue);
            issuePaths.add(issue.getAreaPathAndId());
        }
        Collections.sort(areaPaths);
        Collections.sort(issuePaths);

        int issuePos = 0;
        String lastPath = "Nothing";
        int lastCount = 0;
        for (String aPath : areaPaths) {
            if (!aPath.startsWith(lastPath)) {
                lastPath = aPath;
                issuePos += lastCount;
                lastCount = 0;
            }
            for (int i = issuePos; i < issuePaths.size(); i++) {
                if (issuePaths.get(i).startsWith(aPath)) {
                    Area area=null;
                    if (issueStatus.equals(1)||issueStatus.equals(0)) {
                        area = areaMap.get(issuePaths.get(i));// 源码是 aPath
                        List<HouseQmCheckTaskIssue> list = (area==null)? Lists.newArrayList():result.get(area.getId());
                        if (area!=null){
                            if (list!=null) {
                                String key = issuePaths.get(i);
                                HouseQmCheckTaskIssue e = issueMap.get(key);
                                list.add(e);
                                List<HouseQmCheckTaskIssue> ls = CollectionUtil.removeDuplicate(list);
                                result.put(area.getId(), ls);
                            } else {
                                List<HouseQmCheckTaskIssue> list1 = Lists.newArrayList();
                                list1.add(issueMap.get(issuePaths.get(i)));
                                List<HouseQmCheckTaskIssue> ls = CollectionUtil.removeDuplicate(list1);
                                result.put(area.getId(), ls);
                                result.put(area.getId(), list1);
                            }
                        }
                        lastCount++;
                    }
                    else if (issueStatus.equals(2)){
                        area = areaMap.get(aPath);// 源码是 aPath
                        List<HouseQmCheckTaskIssue> list = result.get(area.getId());
                        if (list!=null) {
                            String key = issuePaths.get(i);
                            HouseQmCheckTaskIssue e = issueMap.get(key);
                            list.add(e);
                            List<HouseQmCheckTaskIssue> ls = CollectionUtil.removeDuplicate(list);
                            result.put(area.getId(), ls);
                        } else {
                            List<HouseQmCheckTaskIssue> list1 = Lists.newArrayList();
                            list1.add(issueMap.get(issuePaths.get(i)));
                            List<HouseQmCheckTaskIssue> ls = CollectionUtil.removeDuplicate(list1);
                            result.put(area.getId(), ls);
                            result.put(area.getId(), list1);
                        }
                        lastCount++;
                    }

                }else {
                    break;
                }
            }
        }

        return result;
    }

    @Override
    public StatCategoryStatRspVo searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn) {
        Map<String, Object> condi = Maps.newHashMap();
        condi.put(PROJECT_ID, projectId);
        condi.put(TASK_ID, taskId);
        List<Integer> typs = Arrays.asList(HouseQmCheckTaskIssueTypeEnum.FindProblem.getId(), HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        condi.put("typ", typs);
        if (areaId > 0) {
            condi.put("AreaPathAndId", "%/" + areaId + "/%");
        }
        if (DateUtil.datetimeToTimeStamp(beginOn) > 0) {
            condi.put("ClientCreateAtGte", beginOn);
        }

        if (DateUtil.datetimeToTimeStamp(endOn) > 0) {
            condi.put("ClientCreateAtLte", endOn);
        }
        List<HouseQmCheckTaskIssue> issueStat = houseQmCheckTaskIssueService.searchByProjIdAndTaskIdAndTypInGroupByCategoryPathAndKeyAndCheckItemKey(condi);
        Integer total = houseQmCheckTaskIssueService.countByProjIdAndTaskIdAndTypInGroupByCategoryPathAndKeyAndCheckItemKey(projectId, taskId, typs, areaId, beginOn, endOn);

        StatCategoryStatRspVo result = new StatCategoryStatRspVo();
        result.setIssue_count(total);

        ArrayList<SimpleHouseQmCheckTaskIssueStatVo> newIssueStat = Lists.newArrayList();
        issueStat.forEach(i -> {
            SimpleHouseQmCheckTaskIssueStatVo s = new SimpleHouseQmCheckTaskIssueStatVo();
            s.setCategoryKey(i.getCategoryKey());
            s.setCategoryPathAndKey(i.getCategoryPathAndKey());
            s.setCheckItemKey(i.getCheckItemKey());
            s.setCheckItemPathAndKey(i.getCheckItemPathAndKey());
            s.setCount(i.getCount());
            newIssueStat.add(s);
        });
        // 针对结果的顺序问题 有要求时需要
        List<HouseQmIssueCategoryStatVo> list = houseqmStatisticService.calculateIssueCount(newIssueStat);
        ArrayList<HouseQmStatCategorySituationRspVo> items = new ArrayList<>();
        list.forEach(e -> {
            HouseQmStatCategorySituationRspVo item = new HouseQmStatCategorySituationRspVo();
            item.setKey(e.getKey());
            item.setParent_key(e.getParentKey() == null ? "" : e.getParentKey());
            item.setIssue_count(e.getIssueCount());
            item.setName(e.getName());
            items.add(item);
        });
        result.setItems(items);
        return result;
    }


    @Override
    @SuppressWarnings("squid:S3776")
    public HouseQmStatAreaSituationIssueRspVo getAreaIssueTypeStatByProjectIdAreaIdCategoryCls(Integer projectId, Integer areaId, Integer categoryCls) {
        String areaPath = "";
        if (areaId > 0) {
            Area areaInfo = areaService.selectById(areaId);
            if (areaInfo == null) throw new LjBaseRuntimeException(-1,ErrorEnum.DB_ITEM_UNFOUND.getMessage());
            areaPath = areaInfo.getPath() + areaInfo.getId() + "/%";
        } else return null;
        // 添加delete_at is null
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(projectId, categoryCls, areaPath);

        HouseQmStatAreaSituationIssueRspVo result = new HouseQmStatAreaSituationIssueRspVo();
        result.setIssue_approveded_count(0);
        result.setIssue_assigned_count(0);
        result.setIssue_count(0);
        result.setIssue_recorded_count(0);
        result.setIssue_repaired_count(0);
        result.setRecord_count(0);

        for (HouseQmCheckTaskIssue res : issues) {
            //处理详细统计数
            HouseQmCheckTaskIssueStatusEnum e = null;
            for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                if (res.getStatus().equals(value.getId())) {
                    e = value;
                    break;
                }
            }
            checkStatus(result, res, e);
        }

        return result;
    }

    private void checkStatus(HouseQmStatAreaSituationIssueRspVo result, HouseQmCheckTaskIssue res, HouseQmCheckTaskIssueStatusEnum e) {
        if (e != null) {
            switch (e) {
                case NoteNoAssign:  //已记录未分配
                    result.setIssue_recorded_count(result.getIssue_recorded_count() + res.getPosX());
                    break;
                case AssignNoReform://已分配未整改
                    result.setIssue_assigned_count(result.getIssue_assigned_count() + res.getPosX());
                    break;
                case ReformNoCheck://已整改未验收
                    result.setIssue_repaired_count(result.getIssue_repaired_count() + res.getPosX());
                    break;
                case CheckYes://已验收
                    result.setIssue_approveded_count(result.getIssue_approveded_count() + res.getPosX());
                    break;
                default:
                    break;
            }

            //处理状态统计
            switch (e) {
                case NoProblem:
                    result.setRecord_count(result.getRecord_count() + res.getPosX());
                    break;
                case ReformNoCheck:
                case AssignNoReform:
                case CheckYes:
                case NoteNoAssign:
                    result.setIssue_count(result.getIssue_count() + res.getPosX());
                    break;
                default:
                    break;
            }


        }
    }

    @Override
    public List<HouseQmStatTaskDetailMemberRepairerRspVo> searchRepaireIssueStatusStatByProjTaskIdBetweenTime(Integer projectId, Integer taskId, Date start, Date end) {
        Map<String, Object> condi = Maps.newHashMap();
        condi.put(PROJECT_ID, projectId);
        condi.put(TASK_ID, taskId);
        condi.put("end_onlte", new SimpleDateFormat(YYYY_MM_DD).format(end));
        condi.put("end_ongte", new SimpleDateFormat(YYYY_MM_DD).format(start));
        List<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueTypeEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        condi.put("typ", typs);
        condi.put("status_repaired_count", HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
        condi.put("status_approveded_count", HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
        List<RepaireIssueStatusStatDto> res = houseQmCheckTaskIssueService.searchRepaireIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(condi);
        if (res.isEmpty()) return Lists.newArrayList();
        List<Integer> userIds = res.stream().map(RepaireIssueStatusStatDto::getUser_id).collect(Collectors.toSet()).stream().collect(Collectors.toList());
        Map<Integer, User> userInfos = userService.selectByIds(userIds);
        for (RepaireIssueStatusStatDto item : res) {
            if (userInfos.containsKey(item.getUser_id())) {
                item.setReal_name(userInfos.get(item.getUser_id()).getRealName());
            }
        }

        List<HouseQmStatTaskDetailMemberRepairerRspVo> result = Lists.newArrayList();
        for (RepaireIssueStatusStatDto item : res) {
            HouseQmStatTaskDetailMemberRepairerRspVo v = new HouseQmStatTaskDetailMemberRepairerRspVo();
            v.setApproveded_count(item.getApproveded_count());
            v.setAssigned_count(item.getAssigned_count());
            v.setRepaired_count(item.getRepaired_count());
            v.setReal_name(item.getReal_name());
            v.setUser_id(item.getUser_id());
            result.add(v);
        }
        return result;
    }

    @Override
    public List<HouseQmStatTaskDetailMemberCheckerRspVo> searchCheckerIssueStatusStatByProjTaskIdBetweenTime(Integer projectId, Integer taskId, Date start, Date end) {
        Map<String, Object> condi = Maps.newHashMap();
        condi.put(PROJECT_ID, projectId);
        condi.put(TASK_ID, taskId);
        condi.put("client_create_atlte", new SimpleDateFormat(YYYY_MM_DD).format(end));
        condi.put("client_create_atgte", new SimpleDateFormat(YYYY_MM_DD).format(start));
        List<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueTypeEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        typs.add(HouseQmCheckTaskIssueTypeEnum.Record.getId());
        typs.add(HouseQmCheckTaskIssueTypeEnum.Good.getId());
        condi.put("typ", typs);
        condi.put("status_issues_count", HouseQmCheckTaskIssueStatusEnum.NoProblem.getId());
        condi.put("status_records_count", HouseQmCheckTaskIssueStatusEnum.NoProblem.getId());
        condi.put("status_approveded_count", HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());

        List<CheckerIssueStatusStatDto> r = houseQmCheckTaskIssueService.searchCheckerIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(condi);
        if (r.isEmpty()) return Lists.newArrayList();
        List<Integer> userIds = r.stream().map(CheckerIssueStatusStatDto::getUser_id).collect(Collectors.toSet()).stream().collect(Collectors.toList());
        Map<Integer, User> userInfos = userService.selectByIds(userIds);
        for (CheckerIssueStatusStatDto item : r) {
            if (userInfos.containsKey(item.getUser_id())) {
                item.setReal_name(userInfos.get(item.getUser_id()).getRealName());
            }
        }
        List<HouseQmStatTaskDetailMemberCheckerRspVo> result = Lists.newArrayList();
        for (CheckerIssueStatusStatDto item : r) {
            HouseQmStatTaskDetailMemberCheckerRspVo v = new HouseQmStatTaskDetailMemberCheckerRspVo();
            v.setApproveded_count(item.getApproveded_count());
            v.setIssue_count(item.getIssues_count());
            v.setRecords_count(item.getRecords_count());
            v.setReal_name(item.getReal_name());
            v.setUser_id(item.getUser_id());
            result.add(v);
        }

        return result;
    }

    public CheckerStatListVo searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds) {
        CheckerStatListVo statListVo = new CheckerStatListVo();
        List<CheckerStatListVo.CheckerStatVo> checkerStatList = Lists.newArrayList();
        List<CheckerIssueStat> checkerIssueStats = houseQmCheckTaskIssueService.searchCheckerIssueStatisticByProjIdAndTaskId(projectId, taskIds);
        if (CollectionUtils.isEmpty(checkerIssueStats))return statListVo;
        List<Integer> list = Lists.newArrayList();
        for (CheckerIssueStat stat : checkerIssueStats) {
            list.add(stat.getUserId());
        }
        Map<Integer, User> userMap = userService.selectByIds(list);
        Map<Integer, CheckerStatListVo.CheckerStatVo> checkerMap = Maps.newHashMap();
        Map<Integer, Map<String, Boolean>> areaMap = Maps.newHashMap();
        Map<String, Boolean> fatherPathMap = Maps.newHashMap();
        for (CheckerIssueStat l : checkerIssueStats) {
            if (!checkerMap.containsKey(l.getUserId())) {
                CheckerStatListVo checkerStatListVo = new CheckerStatListVo();
                CheckerStatListVo.CheckerStatVo checkerStatVo = checkerStatListVo.new CheckerStatVo();
                checkerStatVo.setRecords_count(0);
                checkerStatVo.setIssue_count(0);
                checkerStatVo.setChecked_count(0);
                checkerStatVo.setUser_id(l.getUserId());

                if (userMap.containsKey(l.getUserId())) {
                    checkerStatVo.setReal_name(userMap.get(l.getUserId()).getRealName());
                }
                checkerMap.put(l.getUserId(), checkerStatVo);
            }
            CheckerStatListVo.CheckerStatVo stat = checkerMap.get(l.getUserId());
            // 以下应使用枚举类，由于未改动包结构 先写死
            if (l.getTyp().equals(HouseQmCheckTaskIssueEnum.Record.getId())) {
                stat.setRecords_count(l.getCount() + stat.getRecords_count());
            } else if (l.getTyp().equals(HouseQmCheckTaskIssueEnum.FindProblem.getId()) || l.getTyp().equals(HouseQmCheckTaskIssueEnum.Difficult.getId())) {
                stat.setIssue_count(l.getCount() + stat.getIssue_count());
            }

            if (!areaMap.containsKey(l.getUserId())) {
                HashMap<String, Boolean> map = Maps.newHashMap();
                areaMap.put(l.getUserId(), map);
            }
            String areapath=   String.format("%d%s", l.getAreaId(), "/");
            String fatherPath =StringUtils.removeEnd(l.getAreaPathAndId(),areapath);
            fatherPathMap.put(fatherPath, true);
            areaMap.put(l.getUserId(), fatherPathMap);
        }

        //计算检查数
        for (Map.Entry<Integer, CheckerStatListVo.CheckerStatVo> entry : checkerMap.entrySet()) {
            entry.getValue().setChecked_count(areaMap.get(entry.getValue().getUser_id()).size());
            checkerStatList.add(entry.getValue());
        }
        statListVo.setItems(checkerStatList);
        return statListVo;
    }
    @SuppressWarnings("squid:S3776")
    public ProjectDailyListVo searchTaskSituationDailyByProjTaskIdInOnPage(Integer projectId, List<Integer> taskIdList, Integer pageNum, Integer pageSize) {
        ProjectDailyListVo projectDailyListVo = new ProjectDailyListVo();
        //读取出所有日期
        List<CheckerIssueStat> taskIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueActiveDateByProjTaskIdIn(projectId, taskIdList);
        List<String> totalDates = Lists.newArrayList();
        for (CheckerIssueStat issue : taskIssues) {
            String strCreateAt = new SimpleDateFormat(YYYY_MM_DD).format(issue.getDate());
            totalDates.add(strCreateAt);
        }
        if (pageNum <= 0) {
            pageNum = 1;
        }
        int start = (pageNum - 1) * pageSize;
        //时间进行降序排列
        String tmp = "";
        for (int i = 1; i < totalDates.size(); i++) {
            tmp = totalDates.get(i);
            int j = i - 1;
            for (; j >= 0 && (DateUtil.dateCompare(tmp, totalDates.get(j)) > 0); j--) {
                totalDates.set(j + 1, totalDates.get(j));
            }
            totalDates.set(j + 1, tmp);
        }
        List<String> dates = Lists.newArrayList();
        if (totalDates.size() > start) {
            if (totalDates.size() > (start + pageSize)) {
                for (int i = start; i < (start + pageSize); i++) {
                    dates.add(totalDates.get(i));
                }
            } else {
                for (int i = start; i < totalDates.size(); i++) {
                    dates.add(totalDates.get(i));
                }
            }
        }
        List<ProjectDailyListVo.ProjectDailyVo> list = Lists.newArrayList();

        for (String date : dates) {
            String beginOn = date + " 00:00:00";
            List<CheckerIssueStat> checkerIssueStat = houseQmCheckTaskIssueService.getIssueSituationDailyByProjTaskIdInDate(projectId, taskIdList, beginOn);
            // 赋值 计算
            ProjectDailyListVo projectDailyListVo1 = new ProjectDailyListVo();
            ProjectDailyListVo.ProjectDailyVo stat = projectDailyListVo1.new ProjectDailyVo();
            stat.setDate(date);
            stat.setTotal_checked_count(0);
            stat.setChecked_count(0);
            stat.setRecords_count(0);
            stat.setIssue_count(0);
            //计算检查户数据
            Map<String, Boolean> areaMap = Maps.newHashMap();
            for (CheckerIssueStat l : checkerIssueStat) {
                String areapath=   String.format("%d%s", l.getAreaId(), "/");
                String fatherPath = StringUtils.removeEnd(l.getAreaPathAndId(),areapath);
                if (l.getTyp().equals(HouseQmCheckTaskIssueEnum.Record.getId())) {
                    stat.setRecords_count(l.getCount() + stat.getRecords_count());
                } else if (l.getTyp().equals(HouseQmCheckTaskIssueEnum.FindProblem.getId()) || l.getTyp().equals(HouseQmCheckTaskIssueEnum.Difficult.getId())) {
                    stat.setIssue_count(l.getCount() + stat.getIssue_count());
                }
                areaMap.put(fatherPath, true);
            }
            stat.setChecked_count(areaMap.size());
            //计算累计数量
            Map<String, Integer> areaMap2 = Maps.newHashMap();

            List<CheckerIssueStat> totals = houseQmCheckTaskIssueService.searchByProjectIdAndTaskIdsAndClientCreateAt(projectId, taskIdList, beginOn);

            for (CheckerIssueStat l : totals) {
                String areapath=   String.format("%d%s", l.getAreaId(), "/");
                String fatherPath = StringUtils.removeEnd(l.getAreaPathAndId(),areapath);
                areaMap2.put(fatherPath, l.getCount());
            }
            stat.setTotal_checked_count(areaMap2.size());
            list.add(stat);
        }
        projectDailyListVo.setItems(list);
        projectDailyListVo.setTotal(totalDates.size());

        return projectDailyListVo;
    }


    public ProjectOveralListVo.ProjectOveralVo getInspectTaskStatByProjTaskId(Integer projectId, Integer taskId) {
        ProjectOveralListVo.ProjectOveralVo item = new ProjectOveralListVo().new ProjectOveralVo();
        List<CheckerIssueStat> list = houseQmCheckTaskIssueService.searchByProjectIdAndTaskId(projectId, taskId);
        if (CollectionUtils.isEmpty(list))return item;
        //计算下检查户数据
        HouseQmCheckTask task = houseQmCheckTaskService.selectByProjectIdAndTaskId(projectId, taskId);
        item.setChecked_count(0);
        item.setRecords_count(0);
        item.setIssue_count(0);
        if (task != null) item.setTask_name(task.getName());
        else item.setTask_name("");
        Map<String, Boolean> areaMap = Maps.newHashMap();
        for (CheckerIssueStat l : list) {

            String areapath =String.format("%d%s", l.getAreaId() , "/");
            String fatherPath=StringUtils.removeEnd(l.getAreaPathAndId(),areapath);

            // 以下应使用枚举类，由于未改动包结构 先写死
            if (l.getTyp().equals(HouseQmCheckTaskIssueEnum.Record.getId())) {
                item.setRecords_count(l.getCount() + item.getRecords_count());
            } else if (l.getTyp().equals(HouseQmCheckTaskIssueEnum.FindProblem.getId()) || l.getTyp().equals(HouseQmCheckTaskIssueEnum.Difficult.getId())) {
                item.setIssue_count(l.getCount() + item.getIssue_count());
            }
            areaMap.put(fatherPath, true);
        }
        item.setChecked_count(areaMap.size());
        return item;
    }

    public TaskAreaListVo searchAreasByProjTaskIdTyp(Integer projectId, Integer taskId, int typ) {
        TaskAreaListVo taskAreaListVo = new TaskAreaListVo();
        try {
            HouseQmCheckTask task = houseQmCheckTaskService.selectByProjectIdAndTaskId(projectId, taskId);
            if (task == null) return null;
            String strAreaIds = task.getAreaIds();
            String[] strAreaIdss = strAreaIds.split(",");
            List<Integer> areaIds = Lists.newArrayList();
            for (String item : strAreaIdss) {
                areaIds.add(Integer.parseInt(item));
            }
            List<Area> res = areaService.selectAreasByIdInAreaIds(areaIds);
            List<String> areaPathAndIds = Lists.newArrayList();
            for (Area area : res) {
                areaPathAndIds.add(area.getPath() + area.getId() + "/");
            }
            //getRootAreaIds()
            Map<String, Boolean> mPath = Maps.newHashMap();
            for (String v : areaPathAndIds) {
                String[] names = v.split("/");
                mPath.put(names[1], true);
            }
            areaIds.clear();
            for (Map.Entry<String, Boolean> k : mPath.entrySet()) {
                areaIds.add(Integer.parseInt(k.getKey()));
            }
            List<Area> areas = areaService.selectAreasByIdInAreaIds(areaIds);

            List<TaskAreaListVo.TaskAreaVo> list = Lists.newArrayList();
            for (Area item : areas) {
                TaskAreaListVo.TaskAreaVo taskAreaVo = taskAreaListVo.new TaskAreaVo();
                taskAreaVo.setId(item.getId());
                taskAreaVo.setName(item.getName());
                taskAreaVo.setFather_id(item.getFatherId());
                taskAreaVo.setPath(item.getPath());
                taskAreaVo.setTyp(item.getType());
                list.add(taskAreaVo);
            }
            taskAreaListVo.setAreas(list);
        } catch (Exception e) {
            taskAreaListVo.setAreas(new ArrayList<TaskAreaListVo.TaskAreaVo>());
            log.error(e.getMessage());
        }
        return taskAreaListVo;
    }

    public AreaTaskListVo searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(Integer projectId, Integer areaId, List<Integer> categoryCls) {
        List<HouseQmCheckTask> tasks = houseQmCheckTaskService.searchByProjectIdAndCategoryClsIn(projectId, categoryCls);
        List<Integer> areaIds = Lists.newArrayList();
        for (HouseQmCheckTask item : tasks) {
            List<Integer> areaList = StringUtil.strToInts(item.getAreaIds(), ",");
            areaIds.addAll(areaList);
        }
        //去重
        HashSet<Integer> set = Sets.newHashSet(areaIds);
        areaIds.clear();
        areaIds.addAll(set);

        List<Area> areas = areaService.selectAreasByIdInAreaIds(areaIds);
        HashMap<Integer, String> areaMap = Maps.newHashMap();
        for (Area area : areas) {
            areaMap.put(area.getId(), area.getPath() + area.getId() + "/");
        }
        AreaTaskListVo areaTaskListVo = new AreaTaskListVo();
        List<AreaTaskListVo.AreaTaskVo> list = Lists.newArrayList();
        for (HouseQmCheckTask item : tasks) {
            AreaTaskListVo.AreaTaskVo areaTaskVo = areaTaskListVo.new AreaTaskVo();
            List<Integer> areaList = StringUtil.strToInts(item.getAreaIds(), ",");
            if (checkRootAreaIntersectAreas(areaMap, areaId, areaList)) {
                areaTaskVo.setId(item.getTaskId());
                areaTaskVo.setName(item.getName());
                areaTaskVo.setCategory_cls("" + item.getCategoryCls());
                list.add(areaTaskVo);
            }
        }
        areaTaskListVo.setTasks(list);
        return areaTaskListVo;
    }

    private Boolean checkRootAreaIntersectAreas(Map<Integer, String> areaMap, Integer id, List<Integer> ids) {
        for (Integer i : ids) {
            if (i.equals(id)) {
                return true;
            }
            if (areaMap.containsKey(i)&&areaMap.get(i).contains("/" + id + "/")) {
                return true;
            }
        }
        return false;
    }


}
