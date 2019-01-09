package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.User;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Dongshun on 2018/12/15.
 */
@Repository
@Service
@Slf4j
public class TaskServiceImpl implements ITaskService {
    @Resource
    private HouseQmCheckTaskRspService houseQmCheckTaskRspService;

    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;
    @Resource
    private UserService userService;

    /**
     * @param projectId
     * @param taskId
     * @return
     */
    public HouseQmCheckTaskRspVo getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId) {
        HouseQmCheckTask houseQmCheckTask = houseQmCheckTaskRspService.getHouseQmCheckTaskByProjTaskId(projectId, taskId);
        HouseQmCheckTaskRspVo houseQmCheckTaskRspVo = new HouseQmCheckTaskRspVo();
        houseQmCheckTaskRspVo.setProjectId(houseQmCheckTask.getProjectId());
        houseQmCheckTaskRspVo.setTaskId(houseQmCheckTask.getTaskId());
        houseQmCheckTaskRspVo.setName(houseQmCheckTask.getName());
        houseQmCheckTaskRspVo.setStatus(houseQmCheckTask.getStatus());
        houseQmCheckTaskRspVo.setCategoryCls(houseQmCheckTask.getCategoryCls());
        houseQmCheckTaskRspVo.setRootCategoryKey(houseQmCheckTask.getRootCategoryKey());
        houseQmCheckTaskRspVo.setAreaIds(houseQmCheckTask.getAreaIds());
        houseQmCheckTaskRspVo.setAreaTypes(houseQmCheckTask.getAreaTypes());
        houseQmCheckTaskRspVo.setAreaIds(houseQmCheckTask.getAreaIds());
        houseQmCheckTaskRspVo.setPlanBeginOn(dateToInt(houseQmCheckTask.getPlanBeginOn()));
        houseQmCheckTaskRspVo.setPlanEndOn(dateToInt(houseQmCheckTask.getPlanEndOn()));
        houseQmCheckTaskRspVo.setCreateAt(dateToInt(houseQmCheckTask.getCreateAt()));
        houseQmCheckTaskRspVo.setUpdateAt(dateToInt(houseQmCheckTask.getUpdateAt()));
        houseQmCheckTaskRspVo.setDeleteAt(dateToInt(houseQmCheckTask.getDeleteAt()));
        return houseQmCheckTaskRspVo;
    }


    /**
     * @param projectId
     * @param taskId
     * @return
     */
    public List<Integer> getHouseqmCheckTaskCheckedAreas(Integer projectId, Integer taskId) {
        List<Integer> result = Lists.newArrayList();
        HouseQmCheckTask areaIdsInfo = houseQmCheckTaskService.selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(projectId, taskId);

        if (areaIdsInfo == null) {
            return result;
        }
        List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(areaIdsInfo.getAreaIds(), ",");
        List<HouseQmCheckTaskIssue> issue_area_ids_info = houseQmCheckTaskIssueService.selectAreaIdByProjectIdAndTaskIdAndAreaIdInAndNoDeleted(projectId, taskId, areaIds);
        for (HouseQmCheckTaskIssue i : issue_area_ids_info) {
            result.add(i.getAreaId());
        }
        return result;
    }

    /*
     * @Author hy
     * @Description  删除移动验房单个任务 1.删除这个任务想关的整改人 2.删除这个任务
     * @Date 11:20 2019/1/9
     * @Param [project_id, task_id]
     * @return void
     **/
    @Override
    public void deleteHouseQmCheckTaskByProjTaskId(Integer project_id, Integer task_id) {
        // 删除人员
        userInHouseQmCheckTaskService.removeByTaskId(task_id);
        // 删除问题
        houseQmCheckTaskIssueService.removeHouseQmCheckTaskIssueByProjectIdAndTaskId(project_id, task_id);
        // 删除任务
        houseQmCheckTaskService.removeHouseQmCheckTaskByProjectIdAndTaskId(project_id, task_id);

    }

    @Override
    public List<UserInHouseQmCheckTask> searchUserInKeyHouseQmCheckTaskByTaskId(Integer task_id) {
        return userInHouseQmCheckTaskService.searchByTaskIdAndNoDeleted(task_id);
    }

    @Override
    public Map<Integer, User> getUsersByIds(List<Integer> uids) {
        return userService.selectByIds(uids);

    }

    //date 转int
    private Integer dateToInt(Date date) {
        if (date == null) {
            return null;
        }
        return (int) date.getTime() / 1000;
    }

}
