package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.common.consts.ErrorNumEnum;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.app.vo.task.HouseQmCheckTaskListAndTotalVo;
import com.longfor.longjian.houseqm.consts.ErrorEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssue;
import com.longfor.longjian.houseqm.po.User;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    private HouseQmCheckTaskService houseQmCheckTaskRspService;

    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;
    @Resource
    private UserService userService;

    @Override
    public HouseQmCheckTaskListAndTotalVo searchHouseQmCheckTaskByProjCategoryClsStatusPage(Integer projId, Integer category_cls, Integer status, Integer page, Integer page_size) {
        Integer total = houseQmCheckTaskService.searchTotalByProjIdAndCategoryClsAndStatus(projId, category_cls, status);
        int limit=page_size;
        if (page<=0){
            page=1;
        }
        int start=(page-1)*page_size;
        List<HouseQmCheckTask> list = houseQmCheckTaskService.searchByProjIdAndCategoryClsAndStatusByPage(projId, category_cls, status, limit, start);
        HouseQmCheckTaskListAndTotalVo result = new HouseQmCheckTaskListAndTotalVo();
        result.setList(list);
        result.setTotal(total);
        return result;
    }

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
        houseQmCheckTaskRspVo.setPlan_begin_on(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getPlanBeginOn()));
        houseQmCheckTaskRspVo.setPlan_end_on(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getPlanEndOn()));
        houseQmCheckTaskRspVo.setCreate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getCreateAt()));
        houseQmCheckTaskRspVo.setUpdate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getUpdateAt()));
        houseQmCheckTaskRspVo.setDelete_at(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getDeleteAt()));
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
    @Transactional(rollbackFor = Exception.class)
    @LFAssignDataSource("zhijian2")
    public void deleteHouseQmCheckTaskByProjTaskId(Integer project_id, Integer task_id) throws Exception {

            // 删除人员
            int affect = userInHouseQmCheckTaskService.removeByTaskId(task_id);

            // 删除问题
            int affect2 = houseQmCheckTaskIssueService.removeHouseQmCheckTaskIssueByProjectIdAndTaskId(project_id, task_id);
            // 删除任务
            int affect3 = houseQmCheckTaskService.removeHouseQmCheckTaskByProjectIdAndTaskId(project_id, task_id);
            if (affect < 1 || affect2 < 1 || affect3 < 1)
                throw new Exception(ErrorEnum.DB_ITEM_NOT_UPDATE.getMessage());
    }

    @Override
    public List<UserInHouseQmCheckTask> searchUserInKeyHouseQmCheckTaskByTaskId(Integer task_id) {
        return userInHouseQmCheckTaskService.searchByTaskIdAndNoDeleted(task_id);
    }

    @Override
    public Map<Integer, User> getUsersByIds(List<Integer> uids) {
        return userService.selectByIds(uids);

    }


}
