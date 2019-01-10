package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskIssueService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskRspService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/15.
 */
@Repository
@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    @Resource
    HouseQmCheckTaskRspService houseQmCheckTaskRspService;

    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;

    /**
     * @param projectId
     * @param taskId
     * @return
     */
    public HouseQmCheckTaskRspVo getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId) {
        HouseQmCheckTask houseQmCheckTask = houseQmCheckTaskRspService.getHouseQmCheckTaskByProjTaskId(projectId, taskId);
        HouseQmCheckTaskRspVo houseQmCheckTaskRspVo = new HouseQmCheckTaskRspVo();
        houseQmCheckTaskRspVo.setProject_id(houseQmCheckTask.getProjectId());
        houseQmCheckTaskRspVo.setTask_id(houseQmCheckTask.getTaskId());
        houseQmCheckTaskRspVo.setName(houseQmCheckTask.getName());
        houseQmCheckTaskRspVo.setStatus(houseQmCheckTask.getStatus());
        houseQmCheckTaskRspVo.setCategory_cls(houseQmCheckTask.getCategoryCls());
        houseQmCheckTaskRspVo.setRoot_category_key(houseQmCheckTask.getRootCategoryKey());
        houseQmCheckTaskRspVo.setArea_ids(houseQmCheckTask.getAreaIds());
        houseQmCheckTaskRspVo.setArea_types(houseQmCheckTask.getAreaTypes());
        houseQmCheckTaskRspVo.setArea_ids(houseQmCheckTask.getAreaIds());
        houseQmCheckTaskRspVo.setPlan_begin_on(dateToInt(houseQmCheckTask.getPlanBeginOn()));
        houseQmCheckTaskRspVo.setPlan_end_on(dateToInt(houseQmCheckTask.getPlanEndOn()));
        houseQmCheckTaskRspVo.setCreate_at(dateToInt(houseQmCheckTask.getCreateAt()));
        houseQmCheckTaskRspVo.setUpdate_at(dateToInt(houseQmCheckTask.getUpdateAt()));
        houseQmCheckTaskRspVo.setDelete_at(dateToInt(houseQmCheckTask.getDeleteAt()));
        return houseQmCheckTaskRspVo;
    }


    /**
     *
     * @param projectId
     * @param taskId
     * @return
     */
    public List<Integer> getHouseqmCheckTaskCheckedAreas(Integer projectId,Integer taskId){
        List<Integer> result = Lists.newArrayList();
        HouseQmCheckTask areaIdsInfo=houseQmCheckTaskService.selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(projectId,taskId);

        if (areaIdsInfo==null){
            return  result;
        }
        List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(areaIdsInfo.getAreaIds(), ",");
        List<HouseQmCheckTaskIssue> issue_area_ids_info= houseQmCheckTaskIssueService.selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(projectId,taskId,areaIds);
        for (HouseQmCheckTaskIssue i : issue_area_ids_info) {
            result.add(i.getAreaId());
        }
        return result;
    }

    //date 转int
    private Integer dateToInt(Date date) {
        if (date == null) {
            return null;
        }
        return (int) date.getTime() / 1000;
    }

}
