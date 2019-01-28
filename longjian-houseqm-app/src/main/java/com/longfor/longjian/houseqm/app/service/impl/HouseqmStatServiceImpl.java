package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatService;
import com.longfor.longjian.houseqm.consts.ErrorEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueTypeEnum;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.domain.internalService.UserService;
import com.longfor.longjian.houseqm.dto.CheckerIssueStatusStatDto;
import com.longfor.longjian.houseqm.dto.RepaireIssueStatusStatDto;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public HouseQmStatAreaSituationIssueRspVo getAreaIssueTypeStatByProjectIdAreaIdCategoryCls(Integer project_id, Integer area_id, Integer category_cls) throws Exception {
        String areaPath = "";
        if (area_id > 0) {
            Area areaInfo = areaService.selectById(area_id);
            if (areaInfo == null) throw new Exception(ErrorEnum.DB_ITEM_UNFOUND.getMessage());
            areaPath = areaInfo.getPath() + areaInfo.getId() + "/%";
        } else return null;
        List<HouseQmCheckTaskIssue> issues = houseQmCheckTaskIssueService.searchByProjIdAndCategoryClsAndAreaPathAndIdLikeGroupByStatus(project_id, category_cls, areaPath);

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
                if (res.getStatus().equals(value.getId())) e = value;
            }
            switch (e) {
                case NoteNoAssign:  //已记录未分配
                    result.setIssue_recorded_count(res.getPosX());
                    break;
                case AssignNoReform://已分配未整改
                    result.setIssue_assigned_count(res.getPosX());
                    break;
                case ReformNoCheck://已整改未验收
                    result.setIssue_repaired_count(res.getPosX());
                    break;
                case CheckYes://已验收
                    result.setIssue_approveded_count(res.getPosX());
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

        return result;
    }

    @Override
    public List<HouseQmStatTaskDetailMemberRepairerRspVo> searchRepaireIssueStatusStatByProjTaskIdBetweenTime(Integer project_id, Integer task_id, Date start, Date end) {
        Map<String, Object> condi = Maps.newHashMap();
        condi.put("project_id", project_id);
        condi.put("task_id", task_id);
        condi.put("end_onlte", new SimpleDateFormat("yyyy-MM-dd").format(end));
        condi.put("end_ongte", new SimpleDateFormat("yyyy-MM-dd").format(start));
        List<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueTypeEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        condi.put("typ", typs);
        condi.put("status_repaired_count", HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
        condi.put("status_approveded_count", HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
        List<RepaireIssueStatusStatDto> res = houseQmCheckTaskIssueService.searchRepaireIssueStatusStatDtoByProjIdAndTaskIdAndClientCreateAtAndTypInGroupByUserId(condi);
        if (res.size() <= 0) return Lists.newArrayList();
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
    public List<HouseQmStatTaskDetailMemberCheckerRspVo> searchCheckerIssueStatusStatByProjTaskIdBetweenTime(Integer project_id, Integer task_id, Date start, Date end) {
        Map<String, Object> condi = Maps.newHashMap();
        condi.put("project_id", project_id);
        condi.put("task_id", task_id);
        condi.put("client_create_atlte", new SimpleDateFormat("yyyy-MM-dd").format(end));
        condi.put("client_create_atgte", new SimpleDateFormat("yyyy-MM-dd").format(start));
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
        if (r.size() <= 0) return Lists.newArrayList();
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


    /**
     * @param projectId
     * @param taskIds
     * @return
     */
    public CheckerStatListVo searchCheckerIssueStatisticByProjIdAndTaskId(Integer projectId, List<Integer> taskIds) {
        CheckerStatListVo statListVo = new CheckerStatListVo();
        List<CheckerStatListVo.CheckerStatVo> checkerStatList = Lists.newArrayList();
        List<CheckerIssueStat> checkerIssueStats = houseQmCheckTaskIssueService.searchCheckerIssueStatisticByProjIdAndTaskId(projectId, taskIds);

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
                checkerMap.put(l.getUserId(), checkerStatVo);

                if (userMap.containsKey(l.getUserId())) {
                    checkerMap.get(l.getUserId()).setReal_name(userMap.get(l.getUserId()).getRealName());
                }
            }
            CheckerStatListVo.CheckerStatVo stat = checkerMap.get(l.getUserId());
            // 以下应使用枚举类，由于未改动包结构 先写死
            if (l.getTyp() == HouseQmCheckTaskIssueEnum.Record.getId()) {
                stat.setRecords_count(l.getCount() + stat.getRecords_count());
            } else if (l.getTyp() == HouseQmCheckTaskIssueEnum.FindProblem.getId() || l.getTyp() == HouseQmCheckTaskIssueEnum.Difficult.getId()) {
                stat.setIssue_count(l.getCount() + stat.getIssue_count());
            }

            if (!areaMap.containsKey(l.getUserId())) {
                HashMap<String, Boolean> map = Maps.newHashMap();
                areaMap.put(l.getUserId(), map);
            }
            String areapath = l.getAreaId() + "/";
            String fatherPath = l.getAreaPathAndId().replace(areapath, "");

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

    /**
     * @param projectId
     * @param taskIdList
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ProjectDailyListVo searchTaskSituationDailyByProjTaskIdInOnPage(Integer projectId, List<Integer> taskIdList, Integer pageNum, Integer pageSize) {
        ProjectDailyListVo projectDailyListVo = new ProjectDailyListVo();
        //读取出所有日期
        List<CheckerIssueStat> taskIssues = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueActiveDateByProjTaskIdIn(projectId, taskIdList);
        List<String> totalDates = Lists.newArrayList();
        for (CheckerIssueStat issue : taskIssues) {
            String strCreateAt = new SimpleDateFormat("yyyy-MM-dd").format(issue.getDate());
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
            List<CheckerIssueStat> checkerIssueStat = houseQmCheckTaskIssueService.getIssueSituationDailyByProjTaskIdInDate(projectId, taskIdList, date);
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
                String areapath = l.getAreaId() + "/";
                String fatherPath = l.getAreaPathAndId().replace(areapath, "");
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

            List<CheckerIssueStat> totals = houseQmCheckTaskIssueService.searchByProjectIdAndTaskIdsAndClientCreateAt(projectId, taskIdList, date);

            for (CheckerIssueStat l : totals) {
                String areapath = l.getAreaId() + "/";
                String fatherPath = l.getAreaPathAndId().replace(areapath, "");
                areaMap2.put(fatherPath, l.getCount());
            }
            stat.setTotal_checked_count(areaMap2.size());
            list.add(stat);
        }
        projectDailyListVo.setItems(list);
        projectDailyListVo.setTotal(totalDates.size());

        return projectDailyListVo;
    }


    /**
     * @param projectId
     * @param taskId
     * @return
     */
    public ProjectOveralListVo.ProjectOveralVo getInspectTaskStatByProjTaskId(Integer projectId, Integer taskId) {
        List<CheckerIssueStat> list = houseQmCheckTaskIssueService.searchByProjectIdAndTaskId(projectId, taskId);
        //计算下检查户数据
        HouseQmCheckTask task = houseQmCheckTaskService.selectByProjectIdAndTaskId(projectId, taskId);
        ProjectOveralListVo.ProjectOveralVo item = new ProjectOveralListVo().new ProjectOveralVo();
        item.setChecked_count(0);
        item.setRecords_count(0);
        item.setIssue_count(0);
        if (task != null) item.setTask_name(task.getName());
        else item.setTask_name("");
        Map<String, Boolean> areaMap = Maps.newHashMap();
        for (CheckerIssueStat l : list) {
            String areapath = l.getAreaId() + "/";
            String fatherPath = l.getAreaPathAndId().replace(areapath, "");
            // 以下应使用枚举类，由于未改动包结构 先写死
            if (l.getTyp() == HouseQmCheckTaskIssueEnum.Record.getId()) {
                item.setRecords_count(l.getCount() + item.getRecords_count());
            } else if (l.getTyp() == HouseQmCheckTaskIssueEnum.FindProblem.getId() || l.getTyp() == HouseQmCheckTaskIssueEnum.Difficult.getId()) {
                item.setIssue_count(l.getCount() + item.getIssue_count());
            }
            areaMap.put(fatherPath, true);
        }
        item.setChecked_count(areaMap.size());
        return item;
    }

    /**
     * @param projectId
     * @param taskId
     * @return
     */
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
            e.printStackTrace();
            taskAreaListVo.setAreas(new ArrayList<TaskAreaListVo.TaskAreaVo>());
        }
        return taskAreaListVo;
    }

    /**
     * @param projectId
     * @param areaId
     * @param categoryCls
     * @return
     */
    public AreaTaskListVo searchHouseQmCheckTaskByProjIdAreaIdCategoryClsIn(Integer projectId, Integer areaId, List<Integer> categoryCls) {
        List<HouseQmCheckTask> tasks = houseQmCheckTaskService.searchByProjectIdAndCategoryClsIn(projectId, categoryCls);
        List<Integer> areaIds = Lists.newArrayList();
        for (HouseQmCheckTask item : tasks) {
            List<Integer> areaList = StringUtil.strToInts(item.getAreaIds(), ",");
            for (Integer i : areaList) {
                areaIds.add(i);
            }
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
            List<Integer> areaList = StringSplitToListUtil.splitToIdsComma(item.getAreaIds(), ",");
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

    /**
     * @param areaMap
     * @param id
     * @param ids
     * @return
     */
    private Boolean checkRootAreaIntersectAreas(Map<Integer, String> areaMap, Integer id, List<Integer> ids) {
        for (Integer i : ids) {
            if (i == id) {
                return true;
            }
            if (areaMap.containsKey(i)) {
                if (areaMap.get(i).indexOf("/" + id + "/") != -1) {
                    return true;
                }
            }
        }
        return false;
    }


}
