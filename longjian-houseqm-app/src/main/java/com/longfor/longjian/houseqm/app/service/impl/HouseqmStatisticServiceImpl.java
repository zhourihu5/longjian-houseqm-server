package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.IssueMinStatusVo;
import com.longfor.longjian.houseqm.app.vo.TaskRepairStatVo;
import com.longfor.longjian.houseqm.app.vo.TaskStatVo;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.AreaService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.util.MathUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;

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
        List<IssueRepairCount> issueCounts=null;
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        HashMap<String, Object> condiMap = Maps.newHashMap();
        // 以下条件成立时调用对应成立时的方法。
        if (areaId>0){
            condiMap.put("areaId","%%/areaId/%%");//模糊查询 AreaPathAndId like '%%/ /areaId%%'
        }
        if (beginOn.getTime()/1000>0){
            condiMap.put("beginOn", beginOn);
        }
        if (endOn.getTime()/1000>0){
            condiMap.put("endOn", endOn);
        }
        Date now = new Date();
        condiMap.put("now", now);
        condiMap.put("projectId", projectId);
        condiMap.put("taskId", taskId);
        condiMap.put("types", types);
        condiMap.put("deleted", "false");
        issueCounts=houseQmCheckTaskIssueService.selectByProjectIdAndTaskIdAndTyeInAndDongTai(condiMap);
        IssueRepairCount ic=issueCounts.get(0);
        TaskRepairStatVo taskRepairStatVo = new TaskRepairStatVo();
        TaskRepairStatVo.TaskRepairVo item = taskRepairStatVo.new TaskRepairVo();
        if (ic.getTotal()==0){
            ic.setTotal(1);
        }
        DecimalFormat f = new DecimalFormat("0.00");
        String iniTimeFinish=MathUtil.getPercentage(ic.getInitimeFinish(),ic.getTotal());
        String iniTimeUnFinish = MathUtil.getPercentage(ic.getInitimeUnfinish() ,ic.getTotal());
        String overTimeFinish = MathUtil.getPercentage(ic.getOvertimeFinish(),ic.getTotal());
        String overTimeUnFinish = MathUtil.getPercentage(ic.getOvertimeUnfinish() ,ic.getTotal());
        String noPlanEndOn = MathUtil.getPercentage(ic.getNoPlanEndOn() ,ic.getTotal());

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
     *
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
