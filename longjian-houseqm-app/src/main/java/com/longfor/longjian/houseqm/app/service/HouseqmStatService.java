package com.longfor.longjian.houseqm.app.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.houseqm.app.vo.CheckerStatListVo;
import com.longfor.longjian.houseqm.app.vo.ProjectDailyListVo;
import com.longfor.longjian.houseqm.app.vo.ProjectOveralListVo;
import com.longfor.longjian.houseqm.app.vo.TaskAreaListVo;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.domain.internalService.UserService;
import com.longfor.longjian.houseqm.po.Area;
import com.longfor.longjian.houseqm.po.CheckerIssueStat;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Houyan
 * @date 2018/12/14 0014 19:45
 */
@Repository
@Service
@Slf4j
public class HouseqmStatService {

    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    @Resource
    UserService userService;

    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    AreaService areaService;

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
            if (l.getTyp() == HouseQmCheckTaskIssueEnum.Record.getId()){
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
     *
     * @param projectId
     * @param taskIdList
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ProjectDailyListVo searchTaskSituationDailyByProjTaskIdInOnPage(Integer projectId, List<Integer> taskIdList, Integer pageNum, Integer pageSize) {
        ProjectDailyListVo projectDailyListVo = new ProjectDailyListVo();

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
        String tmp="";
        for(int i=1; i<totalDates.size(); i++){
            tmp = totalDates.get(i);
            int j=i-1;
            for(; j>=0&&(DateCompare(tmp, totalDates.get(j))>0); j--){
                totalDates.set(j+1, totalDates.get(j));
            }
            totalDates.set(j+1, tmp);
        }
        List<String> dates = Lists.newArrayList();
        if (totalDates.size() > start ){
            if (totalDates.size()> (start+pageSize) ) {
                for (int i=start;i<(start+pageSize);i++){
                    dates.add(totalDates.get(i));
                }
            } else {
                for (int i=start;i<totalDates.size();i++){
                    dates.add(totalDates.get(i));
                }
            }
        }
        List<ProjectDailyListVo.ProjectDailyVo> list = Lists.newArrayList();

        for (String date : dates) {
            List<CheckerIssueStat> checkerIssueStat=houseQmCheckTaskIssueService.getIssueSituationDailyByProjTaskIdInDate(projectId,taskIdList,date);
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
                if (l.getTyp() == HouseQmCheckTaskIssueEnum.Record.getId()){
                    stat.setRecords_count(l.getCount() + stat.getRecords_count());
                } else if (l.getTyp() == HouseQmCheckTaskIssueEnum.FindProblem.getId() || l.getTyp() == HouseQmCheckTaskIssueEnum.Difficult.getId()) {
                    stat.setIssue_count(l.getCount() + stat.getIssue_count());
                }
                areaMap.put(fatherPath, true);
            }
            stat.setChecked_count(areaMap.size());
            //计算累计数量
            Map<String, Integer> areaMap2 = Maps.newHashMap();

            List<CheckerIssueStat> totals =houseQmCheckTaskIssueService.searchByProjectIdAndTaskIdsAndClientCreateAt(projectId,taskIdList,date);

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
     *
     * @param projectId
     * @param taskId
     * @return
     */
    public ProjectOveralListVo.ProjectOveralVo getInspectTaskStatByProjTaskId(Integer projectId,Integer taskId){
        List<CheckerIssueStat>  list=houseQmCheckTaskIssueService.searchByProjectIdAndTaskId(projectId,taskId);
        //计算下检查户数据
        HouseQmCheckTask task = houseQmCheckTaskService.selectByProjectIdAndTaskId(projectId, taskId);
        ProjectOveralListVo.ProjectOveralVo item = new ProjectOveralListVo().new ProjectOveralVo();
        item.setChecked_count(0);
        item.setRecords_count(0);
        item.setIssue_count(0);
        item.setTask_name(task.getName());
        Map<String, Boolean> areaMap = Maps.newHashMap();
        for (CheckerIssueStat l : list) {
            String areapath = l.getAreaId() + "/";
            String fatherPath = l.getAreaPathAndId().replace(areapath, "");
            // 以下应使用枚举类，由于未改动包结构 先写死
            if (l.getTyp() == HouseQmCheckTaskIssueEnum.Record.getId()){
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
     *
     * @param projectId
     * @param taskId
     * @return
     */
    public TaskAreaListVo searchAreasByProjTaskIdTyp(Integer projectId, Integer taskId){
        HouseQmCheckTask task = houseQmCheckTaskService.selectByProjectIdAndTaskId(projectId, taskId);
        String strAreaIds = task.getAreaIds();
        String[] strAreaIdss = strAreaIds.split(",");
        List<Integer> areaIds = Lists.newArrayList();
        for (String item : strAreaIdss) {
            areaIds.add(Integer.parseInt(item));
        }
        List<Area> res = areaService.selectAreasByIdInAreaIds(areaIds);
        List<String> areaPathAndIds = Lists.newArrayList();
        for (Area area : res) {
            areaPathAndIds.add(area.getPath()+area.getId());
        }
        //getRootAreaIds()
        Map<String, Boolean> mPath = Maps.newHashMap();
        for (String v : areaPathAndIds) {
            String[] names = v.split("/");
            mPath.put(names[1],true);
        }
        areaIds.clear();
        for (Map.Entry<String, Boolean> k : mPath.entrySet()) {
            areaIds.add(Integer.parseInt(k.getKey()));
        }
        List<Area> areas = areaService.selectAreasByIdInAreaIds(areaIds);
        TaskAreaListVo taskAreaListVo = new TaskAreaListVo();
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
        return taskAreaListVo;
    }

    /**
     * 时间比较排序
     * @param s1
     * @param s2
     * @return
     */
    private long DateCompare(String s1, String s2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d1 = sdf.parse(s1);
            Date d2 = sdf.parse(s2);
            return ((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
