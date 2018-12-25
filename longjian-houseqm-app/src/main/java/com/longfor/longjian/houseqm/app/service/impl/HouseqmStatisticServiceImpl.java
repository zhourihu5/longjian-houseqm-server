package com.longfor.longjian.houseqm.app.service.impl;


import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.MathUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.awt.SystemColor.info;

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
    CategoryService categoryService;
    @Resource
    CheckItemService checkItemService;
    @Resource
    FileResourceService fileService;


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

        // 以下条件成立时调用对应成立时的方法。go代码较复杂
        if (areaId > 0) {

            HashMap<String, Object> condiMap = Maps.newHashMap();
            // 以下条件成立时调用对应成立时的方法。
            if (areaId > 0) {
                condiMap.put("areaId", "%%/areaId/%%");//模糊查询 AreaPathAndId like '%%/ /areaId%%'
            }
            if (beginOn.getTime() / 1000 > 0) {

                if (beginOn.getTime() / 1000 > 0) {
                    condiMap.put("beginOn", beginOn);
                }
                if (endOn.getTime() / 1000 > 0) {

                    if (endOn.getTime() / 1000 > 0) {
                        condiMap.put("endOn", endOn);
                    }
                    //不成立时调用下面的业务方法
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
            }

        }
        return null;
    }

    @Override
    public List<HouseQmIssueCategoryStatVo> searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn
            (Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        ArrayList<SimpleHouseQmCheckTaskIssueStatVo> issueStatVoList = Lists.newArrayList();
        ArrayList<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.houseQmCheckTaskIssueByProTaskIdAreaidBegin(projectId, taskId, areaId, begin, endOns, types);
        for (int i = 0; i < issueList.size(); i++) {
            SimpleHouseQmCheckTaskIssueStatVo simpleHouseQmCheckTaskIssueStatVo = new SimpleHouseQmCheckTaskIssueStatVo();
            simpleHouseQmCheckTaskIssueStatVo.setCategoryKey(issueList.get(i).getCategoryKey());
            simpleHouseQmCheckTaskIssueStatVo.setCategoryPathAndKey(issueList.get(i).getCategoryPathAndKey());
            simpleHouseQmCheckTaskIssueStatVo.setCheckItemKey(issueList.get(i).getCheckItemKey());
            simpleHouseQmCheckTaskIssueStatVo.setCheckItemPathAndKey(issueList.get(i).getCheckItemPathAndKey());
            simpleHouseQmCheckTaskIssueStatVo.setCount(issueList.get(i).getCount());
            issueStatVoList.add(simpleHouseQmCheckTaskIssueStatVo);
        }
        List<HouseQmIssueCategoryStatVo> vos = calculateIssueCount(issueStatVoList);
        return vos;
    }

    @Override
    public List<HouseQmCheckTaskIssueOnlineInfoVo> SearchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged(Integer projectId, String categoryKey, Integer areaId, Integer page, Integer pageSize) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        HashMap<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId",projectId);
        condiMap.put("categoryKey",categoryKey);
        condiMap.put("types",types);
        if (pageSize < 1) {
            pageSize = 10;
        }
        if (page < 1) {
            page = 1;
        }
        condiMap.put("page",page);
        condiMap.put("pageSize",pageSize);
        if (areaId > 0) {
            condiMap.put("areaId", areaId);
        }


        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(condiMap);
        List<HouseQmCheckTaskIssueOnlineInfoVo> vos = null;
        try {
            vos = formatHouseQmCheckTaskIssueOnlineInfo(issueList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vos;
    }

    private List<HouseQmCheckTaskIssueOnlineInfoVo> formatHouseQmCheckTaskIssueOnlineInfo(List<HouseQmCheckTaskIssue> issueList) throws Exception {
        List<HouseQmCheckTaskIssueOnlineInfoVo> infos = convertHouseQmCheckTaskIssueToOnlineInfo(issueList);
        try {
            infos = fillHouseQmCheckTaskIssueOnlineInfoAreaInfo(infos);
            infos = fillHouseQmCheckTaskIssueOnlineInfoCategoryInfo(infos);
            infos = fillHouseQmCheckTaskIssueOnlineInfoFileInfo(infos);
        } catch (Exception e) {
            throw new Exception("error:"+e);
        }
        return infos;
    }

    private List<HouseQmCheckTaskIssueOnlineInfoVo> fillHouseQmCheckTaskIssueOnlineInfoFileInfo(List<HouseQmCheckTaskIssueOnlineInfoVo> infos) {
        ArrayList<String> attachmentMd5List = Lists.newArrayList();
        for (int i = 0; i < infos.size(); i++) {
            String[] split = infos.get(i).getAttachmentMd5List().split(",");
            for (int j = 0; j < split.length; j++) {
                attachmentMd5List.add(split[j]);
            }

        }
        List<FileResource> fileList = fileService.SearchByMd5In(attachmentMd5List);
        HashMap<String, String> map = Maps.newHashMap();
        for (int i = 0; i < fileList.size(); i++) {
            map.put(fileList.get(i).getFileMd5(), fileList.get(i).getStoreKey());
        }
        for (int i = 0; i < infos.size(); i++) {
            String[] split = infos.get(i).getAttachmentMd5List().split(",");
            for (int j = 0; j < split.length; j++) {
                if (map.containsKey(split[j])) {

                    infos.get(i).getAttachmentUrlList().add(map.get(split[j]));

                }
            }
        }
        return infos;

    }

    private List<HouseQmCheckTaskIssueOnlineInfoVo> fillHouseQmCheckTaskIssueOnlineInfoCategoryInfo(List<HouseQmCheckTaskIssueOnlineInfoVo> infos) {
        ArrayList<String> categoryKeys = Lists.newArrayList();
        ArrayList<String> checkItemKeys = Lists.newArrayList();
        for (int i = 0; i < infos.size(); i++) {

            String[] CategoryPathAndKeys = GetPathSlice(infos.get(i).getCategoryPathAndKey());
            for (int j = 0; j < CategoryPathAndKeys.length; j++) {
                categoryKeys.add(CategoryPathAndKeys[j]);
            }
            String[] CheckItemPathAndKey = GetPathSlice(infos.get(i).getCheckItemPathAndKey());
            for (int j = 0; j < CheckItemPathAndKey.length; j++) {
                checkItemKeys.add(CheckItemPathAndKey[j]);
            }
        }
        removeDuplicate(categoryKeys);
        removeDuplicate(checkItemKeys);
        List<Category> categoryList = categoryService.SearchCategoryByKeyIn(categoryKeys);
        CategoryMapVo categoryMap = new CategoryMapVo().NewCategoryMap(categoryList);

        List<CheckItem> checkItemList = checkItemService.SearchCheckItemByKeyIn(checkItemKeys);
        CheckItemMapVo checkItemMap = new CheckItemMapVo().NewCategoryMap(checkItemList);
        for (int i = 0; i < infos.size(); i++) {

            infos.get(i).setCategoryPathName(categoryMap.getFullNamesByKey(infos.get(i).getCategoryKey()));
            infos.get(i).setCategoryName(categoryMap.getNameByKey(infos.get(i).getCategoryKey()));
            infos.get(i).setCheckItemPathName(checkItemMap.getFullNamesByKey(infos.get(i).getCheckItemKey()));
            infos.get(i).setCheckItemName(checkItemMap.getNameByKey(infos.get(i).getCheckItemKey()));
        }

        return infos;
    }

    private String[] GetPathSlice(String path) {
        String newStr = path.substring(1, path.length());
        String[] split = newStr.split("/");
        return split;
    }

    private List<HouseQmCheckTaskIssueOnlineInfoVo> fillHouseQmCheckTaskIssueOnlineInfoAreaInfo(List<HouseQmCheckTaskIssueOnlineInfoVo> infos) {
        Set<Integer> areaIds = Sets.newHashSet();
        for (int i = 0; i < infos.size(); i++) {
            areaIds.add(infos.get(i).getAreaId());
        }
        AreaMapVo map = CreateAreasMapByLeaveIds(areaIds);
        for (int i = 0; i < infos.size(); i++) {
            infos.get(i).setAreaPathName(map.getPathNames(infos.get(i).getAreaId()));
            infos.get(i).setAreaName(map.getName(infos.get(i).getAreaId()));
        }

        return infos;
    }

    private AreaMapVo CreateAreasMapByLeaveIds(Set<Integer> areaIds) {
        List<Area> areaList = SelectAllByLeaveIds(areaIds);
        return CreateAreasMapByAreaList(areaList);

    }

    private AreaMapVo CreateAreasMapByAreaList(List<Area> areaList) {
        AreaMapVo vo = new AreaMapVo();
        Map<Integer, Area> map = vo.GetAreas();
        for (int i = 0; i < areaList.size(); i++) {
            map.put(areaList.get(i).getId(), areaList.get(i));
        }
        vo.setList(areaList);
        return vo;
    }

    private List<Area> SelectAllByLeaveIds(Set<Integer> areaIds) {
        List<Integer> list= areaIds.stream().collect(Collectors.toList());
        List<Area> areaList = areaService.selectByAreaIds(list);
        ArrayList<Integer> totalIds = Lists.newArrayList();
        for (int i = 0; i < areaList.size(); i++) {
            totalIds.add(areaList.get(i).getId());

            List<Integer> sids = splitToIds(areaList.get(i).getPath(), "/");
            totalIds.addAll(sids);

        }
        List<Integer> lists = removeDuplicate(totalIds);
       /* HashSet<Integer> set = Sets.newHashSet();
       for (int i = 0; i < list.size(); i++) {
            set.add(list.get(i));
        }*/
        return areaService.selectByAreaIds(lists);
    }

    /**
     * 字符串切割，并转换成Integer集合
     *
     * @param idstr
     * @param sep
     * @return
     */
    private List<Integer> splitToIds(String idstr, String sep) {
        List<Integer> result = Lists.newArrayList();
        String[] ids = idstr.split(sep);
        for (String id : ids) {
            id.trim();
            if (id.equals("")) continue;
            int i = Integer.parseInt(id);
            result.add(i);
        }
        return result;
    }


    private List<HouseQmCheckTaskIssueOnlineInfoVo> convertHouseQmCheckTaskIssueToOnlineInfo(List<HouseQmCheckTaskIssue> issueList) {
        ArrayList<HouseQmCheckTaskIssueOnlineInfoVo> infos = Lists.newArrayList();
        for (int i = 0; i < infos.size(); i++) {
            HouseQmCheckTaskIssueOnlineInfoVo vo = new HouseQmCheckTaskIssueOnlineInfoVo();
            vo.setId(infos.get(i).getId());
            vo.setProjectId(infos.get(i).getProjectId());
            vo.setTaskId(infos.get(i).getTaskId());
            vo.setUuid(infos.get(i).getUuid());
            vo.setTitle(infos.get(i).getTitle());
            vo.setTyp(infos.get(i).getTyp());
            vo.setContent(infos.get(i).getContent());
            vo.setCondition(infos.get(i).getCondition());
            vo.setStatus(infos.get(i).getStatus());
            vo.setPlanEndOn(infos.get(i).getPlanEndOn());
            vo.setAttachmentMd5List(infos.get(i).getAttachmentMd5List());
            vo.setClientCreateAt(infos.get(i).getClientCreateAt());
            vo.setUpdateAt(infos.get(i).getUpdateAt());
            vo.setAreaId(infos.get(i).getAreaId());
            vo.setCategoryKey(infos.get(i).getCategoryKey());
            vo.setCategoryPathAndKey(infos.get(i).getCategoryPathAndKey());
            vo.setCheckItemKey(infos.get(i).getCheckItemKey());
            vo.setCheckItemPathAndKey(infos.get(i).getCheckItemPathAndKey());
            infos.add(vo);
        }
        return infos;

    }

    private List<HouseQmIssueCategoryStatVo> calculateIssueCount
            (ArrayList<SimpleHouseQmCheckTaskIssueStatVo> issueStatVoList) {
        ArrayList<HouseQmIssueCategoryStatVo> objects = Lists.newArrayList();
        Map<String, Object> map = groupIssueStatByCategoryAndCheckItem(issueStatVoList);
        Map<String, Category> categoryMap = null;
        Map<String, CheckItem> checkItemMap = null;
        boolean isStatLevel3 = false;
        for (Map.Entry<String, Object> entrys : map.entrySet()) {
            if (entrys.getKey().equals("categoryKeys")) {
                List<String> keys = (List<String>) entrys.getValue();
                categoryMap = getCategoryMapByCategoryKeys(keys);
            }
            if (entrys.getKey().equals("checkItemKeys")) {
                List<String> keys = (List<String>) entrys.getValue();
                checkItemMap = getCheckItemMapByCheckItemKeys(keys);
            }
            if (categoryMap.size() > 0) {
                String rootKey = "";
                for (Map.Entry<String, Category> entry : categoryMap.entrySet()) {
                    String newStr = entry.getValue().getPath().substring(1, entry.getValue().getPath().length());
                    String[] split = newStr.split("/");
/*
                    split[split.length]= entry.getValue().getKey();
*/
                    rootKey = split[0];
                }
                isStatLevel3 = isCategoryStatLevelThree(rootKey);
            }
            if (entrys.getKey().equals("categoryStatMap")) {
                HashMap<String, HouseQmIssueCategoryStatVo> categoryStatMap = (HashMap<String, HouseQmIssueCategoryStatVo>) entrys.getValue();

                for (Map.Entry<String, HouseQmIssueCategoryStatVo> entry : categoryStatMap.entrySet()) {
                    for (Map.Entry<String, Category> Entry : categoryMap.entrySet()) {

                        if (entry.getValue().getKey().equals(Entry.getKey())) {
                            boolean isRoot = false;

                            //通过“/”进行匹配
                            int level = 0;

                            String str = Entry.getValue().getPath();

                            for (int i = 0; i < str.length(); i++) {
                                if (str.charAt(i) == '/') {
                                    level++;
                                }
                            }
                            //需要跳过三级
                            if (isStatLevel3) {
                                if (level < 3) {
                                    continue;
                                } else if (level == 3) {
                                    isRoot = true;
                                }
                            } else {
                                if (level < 2) {
                                    continue;
                                } else if (level == 2) {
                                    isRoot = true;
                                }

                            }
                            entry.getValue().setName(Entry.getValue().getName());

                            if (!isRoot) {
                                entry.getValue().setParentKey(Entry.getValue().getFatherKey());
                            }
                            objects.add(entry.getValue());
                        }
                    }
                }
            }
            if (entrys.getKey().equals("checkItemStatMap")) {
                HashMap<String, HouseQmIssueCategoryStatVo> checkItemStatMap = (HashMap<String, HouseQmIssueCategoryStatVo>) entrys.getValue();
                for (Map.Entry<String, HouseQmIssueCategoryStatVo> entryS : checkItemStatMap.entrySet()) {
                    for (Map.Entry<String, CheckItem> Entry : checkItemMap.entrySet()) {

                        if (entryS.getValue().getKey().equals(Entry.getKey())) {
                            entryS.getValue().setName(Entry.getValue().getName());
                            entryS.getValue().setParentKey(Entry.getValue().getCategoryKey());
                            entryS.getValue().setKey("C" + Entry.getValue().getKey());
                            objects.add(entryS.getValue());
                        }


                    }
                }

            }

        }

        return objects;
    }


    private Map<String, CheckItem> getCheckItemMapByCheckItemKeys(List<String> keys) {
        List<CheckItem> checkItemsList = checkItemService.SearchCheckItemByKeyIn(keys);
        HashMap<String, CheckItem> map = Maps.newHashMap();
        for (int i = 0; i < checkItemsList.size(); i++) {
            map.put(checkItemsList.get(i).getKey(), checkItemsList.get(i));

        }
        return map;
    }

    private Map<String, Category> getCategoryMapByCategoryKeys(List<String> keys) {
        List<Category> categoryList = categoryService.SearchCategoryByKeyIn(keys);
        HashMap<String, Category> map = Maps.newHashMap();
        for (int i = 0; i < categoryList.size(); i++) {
            map.put(categoryList.get(i).getKey(), categoryList.get(i));

        }

        return map;
    }

    private Map<String, Object> groupIssueStatByCategoryAndCheckItem
            (ArrayList<SimpleHouseQmCheckTaskIssueStatVo> issueStatVoList) {
        HashMap<String, HouseQmIssueCategoryStatVo> categoryStatMap = Maps.newHashMap();
        HashMap<String, HouseQmIssueCategoryStatVo> checkItemStatMap = Maps.newHashMap();
        ArrayList<String> categoryKeys = Lists.newArrayList();
        ArrayList<String> checkItemKeys = Lists.newArrayList();
        for (int i = 0; i < issueStatVoList.size(); i++) {
            //切空格and“/”
            //String[] categoryPathKeys = issueStatVoList.get(i).getCategoryPathAndKey().trim().split("/");

            String[] categoryPathKeys = issueStatVoList.get(i).getCategoryPathAndKey().split("/");
            for (int j = 0; j < categoryPathKeys.length; j++) {
                //判断key是否存在
                if (!categoryStatMap.containsKey(categoryPathKeys[j])) {
                    HouseQmIssueCategoryStatVo houseQmIssueCategoryStatVo = new HouseQmIssueCategoryStatVo();
                    houseQmIssueCategoryStatVo.setKey(categoryPathKeys[j]);
                    //存放key value（对象中的key值）
                    categoryStatMap.put(categoryPathKeys[j], houseQmIssueCategoryStatVo);
                }
                //遍历此map
                for (Map.Entry<String, HouseQmIssueCategoryStatVo> entrys : categoryStatMap.entrySet()) {
                    //当map中的key存在
                    if (entrys.getKey().equals(categoryPathKeys[j])) {
                        //在key所对应的对象中添加issuencount值

                        entrys.getValue().setIssueCount(issueStatVoList.get(i).getCount());

                    }
                }
               /* ArrayList<String> categoryPathKeysList = Lists.newArrayList();
                for (int k = 0; k < categoryPathKeys.length; k++) {
                    categoryPathKeysList.add(categoryPathKeys[k]);
                }
                categoryKeys.addAll(categoryPathKeysList);*/
                for (int k = 0; k < categoryPathKeys.length; k++) {
                    categoryKeys.add(categoryPathKeys[k]);
                }

            }
            //当CheckItemKey的长度大于0
            if (issueStatVoList.get(i).getCheckItemKey().length() > 0) {
                //判断key是否存在于map
                if (!checkItemStatMap.containsKey(issueStatVoList.get(i).getCheckItemKey())) {
                    HouseQmIssueCategoryStatVo houseQmIssueCategoryStatVo = new HouseQmIssueCategoryStatVo();
                    houseQmIssueCategoryStatVo.setKey(issueStatVoList.get(i).getCheckItemKey());
                    //存放key value（对象中的key值）
                    categoryStatMap.put(issueStatVoList.get(i).getCheckItemKey(), houseQmIssueCategoryStatVo);
                }


                //遍历此map
                for (Map.Entry<String, HouseQmIssueCategoryStatVo> entrys : checkItemStatMap.entrySet()) {
                    //当map中的key存在
                    if (entrys.getKey().equals(issueStatVoList.get(i).getCheckItemKey())) {
                        //在key所对应的对象中添加issuencount值
                        entrys.getValue().setIssueCount(issueStatVoList.get(i).getCount() + entrys.getValue().getIssueCount());

                    }
                }
                checkItemKeys.add(issueStatVoList.get(i).getCheckItemKey());
            }
        }
        removeDuplicate(categoryKeys);
        removeDuplicate(checkItemKeys);
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("categoryKeys", categoryKeys);
        map.put("checkItemKeys", checkItemKeys);
        map.put("categoryStatMap", categoryStatMap);
        map.put("checkItemStatMap", checkItemStatMap);
        return map;
    }

//去重

    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    // 判断检查项的统计级别。部分检查项的顶级
    public boolean isCategoryStatLevelThree(String categoryRootKey) {
        List<Category> categoryList = categoryService.SearchCategoryByFatherKey(categoryRootKey);

        try {
            if (categoryList.size() > 2) {
                return false;
            }
        } catch (Exception e) {
            log.error("error:" + e);
            return false;

        }
        return true;
    }

    /**
     * @param taskId
     * @param areaId
     * @param onlyIssue
     * @return
     */
    private Map<Integer, IssueMinStatusVo> getIssueMinStatusMapByTaskIdAndAreaId(Integer taskId, Integer
            areaId, Boolean onlyIssue) {
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