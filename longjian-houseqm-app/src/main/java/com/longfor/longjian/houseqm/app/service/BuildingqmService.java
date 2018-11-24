package com.longfor.longjian.houseqm.app.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.checktask.*;
import com.longfor.longjian.houseqm.app.vo.TaskListVo;
import com.longfor.longjian.houseqm.app.vo.TaskMemberListVo;
import com.longfor.longjian.houseqm.app.vo.TaskVo;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskService;
import com.longfor.longjian.houseqm.domain.internalService.HouseQmCheckTaskSquadService;
import com.longfor.longjian.houseqm.domain.internalService.UserInHouseQmCheckTaskService;
import com.longfor.longjian.houseqm.innervo.ApiBuildingQmCheckTaskConfig;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lipeishuai
 * @date 2018/11/23 11:24
 */

@Repository
@Service
@Slf4j
public class BuildingqmService {

    @Resource
    UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;

    @Resource
    HouseQmCheckTaskService houseQmCheckTaskService;

    @Resource
    HouseQmCheckTaskSquadService houseQmCheckTaskSquadService;


    /**
     *
     * @param userId
     * @return
     */
    public TaskListVo myTaskList(Integer userId) {

        List<UserInHouseQmCheckTask> userInHouseQmCheckTasks = userInHouseQmCheckTaskService.searchByUserId(userId);
        Set<Integer> taskIds = Sets.newHashSet();

        for (UserInHouseQmCheckTask task : userInHouseQmCheckTasks) {
            taskIds.add(task.getTaskId());
        }

        Map<Integer, ApiBuildingQmCheckTaskConfig> apiBuildingQmCheckTaskConfigMap = Maps.newHashMap();
        List<HouseQmCheckTask> houseQmCheckTasks = houseQmCheckTaskService.selectByTaskIds(taskIds);

        fullTaskConfigVO(apiBuildingQmCheckTaskConfigMap,houseQmCheckTasks);
        List<HouseQmCheckTask> allHouseQmCheckTasks =houseQmCheckTaskService.selectByTaskIdsEvenDeleted(taskIds);
        List<TaskVo> vos = Lists.newArrayList();
        fullAllTaskConfigVO(apiBuildingQmCheckTaskConfigMap, allHouseQmCheckTasks, vos);
        TaskListVo taskListVo = new TaskListVo();
        taskListVo.setTask_list(vos);

        return taskListVo;
    }


    /**
     *
     * @param taskIdsStr
     * @return
     */
     public TaskMemberListVo taskSquadsMembers(String taskIdsStr){

         TaskMemberListVo taskMemberListVo = new TaskMemberListVo();
         if(StringUtils.isEmpty(taskIdsStr)){
             return taskMemberListVo;
         }
         Set<Integer> taskIds = Sets.newHashSet();
         String[] ids = taskIdsStr.split(",");
         for(String id: ids){
             taskIds.add(Integer.parseInt(id));
         }
         if(CollectionUtils.isEmpty(taskIds)){
             return taskMemberListVo;
         }

         List<UserInHouseQmCheckTask> allUserTasks = userInHouseQmCheckTaskService.selectByTaskIdsEvenDeleted(taskIds);
         List<HouseQmCheckTaskSquad> allHouseQmCheckTasks = houseQmCheckTaskSquadService.selectByTaskIdsEvenDeleted(taskIds);

         List<TaskMemberListVo.MemberVo> memberListVo = Lists.newArrayList();
         List<TaskMemberListVo.SquadVo> squaListVo = Lists.newArrayList();

         for(HouseQmCheckTaskSquad task: allHouseQmCheckTasks){
             TaskMemberListVo.SquadVo vo = taskMemberListVo.new SquadVo();
             vo.setId(task.getId());
             vo.setProject_id(task.getProjectId());
             vo.setTask_id(task.getTaskId());
             vo.setSquad_type(task.getSquadType());
             vo.setName(task.getName());
             vo.setUpdate_at((int)(task.getCreateAt().getTime()/1000));
             vo.setDelete_at((int)(task.getCreateAt().getTime()/1000));
             squaListVo.add(vo);
         }

         for(UserInHouseQmCheckTask task: allUserTasks){
             TaskMemberListVo.MemberVo vo = taskMemberListVo.new MemberVo();
             vo.setId(task.getId());
             vo.setSquad_id(task.getSquadId());
             vo.setUser_id(task.getUserId());
             vo.setRole_type(task.getRoleType());
             vo.setCan_approve(task.getCanApprove());
             vo.setCan_direct_approve(task.getCanDirectApprove());
             vo.setCan_reassign(task.getCanReassign());
             vo.setTask_id(task.getTaskId());
             vo.setUpdate_at((int)(task.getCreateAt().getTime()/1000));
             vo.setDelete_at((int)(task.getCreateAt().getTime()/1000));
             memberListVo.add(vo);
         }

         taskMemberListVo.setMember_list(memberListVo);
         taskMemberListVo.setSquad_list(squaListVo);

         return taskMemberListVo;
     }


    /**
     *
     * @param maps
     * @param houseQmCheckTasks
     */
    private void fullAllTaskConfigVO(Map<Integer, ApiBuildingQmCheckTaskConfig> maps,
                                  List<HouseQmCheckTask> houseQmCheckTasks, List<TaskVo> vos) {

        if (CollectionUtils.isEmpty(houseQmCheckTasks)) {
            return;
        }

        for (HouseQmCheckTask item : houseQmCheckTasks) {

            TaskVo task = new TaskVo();
            task.setTask_id(item.getTaskId());
            task.setProject_id(item.getProjectId());
            task.setName(item.getName());
            task.setStatus(item.getStatus());
            task.setCategory_cls(item.getCategoryCls());
            task.setRoot_category_key(item.getRootCategoryKey());
            task.setArea_ids(item.getAreaIds());
            // 有可能是area_type?
            task.setArea_types(item.getAreaTypes());
            task.setPlan_begin_on((int) (item.getPlanBeginOn().getTime()/1000));
            task.setPlan_end_on((int)(item.getPlanEndOn().getTime()/1000));
            task.setCreate_at((int)(item.getCreateAt().getTime()/1000));
            task.setUpdate_at((int)(item.getUpdateAt().getTime()/1000));
            task.setDelete_at((int)(item.getDeleteAt().getTime()/1000));

            if(maps.containsKey(task.getTask_id())){

                ApiBuildingQmCheckTaskConfig cfg= maps.get(task.getTask_id());
                task.setRepairer_refund_permission(cfg.getRepairer_refund_permission());
                task.setRepairer_follower_permission(cfg.getRepairer_follower_permission());
                task.setChecker_approve_permission(cfg.getChecker_approve_permission());
                task.setRepaired_picture_status(cfg.getRepaired_picture_status());
                task.setIssue_desc_status(cfg.getIssue_desc_status());
                task.setIssue_default_desc(cfg.getIssue_default_desc());
            }else{

                task.setRepairer_refund_permission(CheckTaskRepairerRefundPermission.No.getValue());
                task.setRepairer_follower_permission(CheckTaskRepairerFollowerPermission.CompleteRepair.getValue());
                task.setChecker_approve_permission(CheckerApprovePermission.No.getValue());
                task.setRepaired_picture_status(CheckTaskRepairedPictureEnum.UnForcePicture.getValue());
                task.setIssue_desc_status(CheckTaskIssueDescEnum.Arbitrary.getValue());
                task.setIssue_default_desc("(该问题无文字描述)");
            }
            vos.add(task);
        }
    }


    /**
     *
     * @param maps
     * @param houseQmCheckTasks
     */
    private void fullTaskConfigVO(Map<Integer, ApiBuildingQmCheckTaskConfig> maps,
                                              List<HouseQmCheckTask> houseQmCheckTasks) {

        if (CollectionUtils.isEmpty(houseQmCheckTasks)) {
            return;
        }

        for (HouseQmCheckTask task : houseQmCheckTasks) {

            ApiBuildingQmCheckTaskConfig item = new ApiBuildingQmCheckTaskConfig();
            if (StringUtils.isEmpty(task.getConfigInfo())) {

                item.setRepairer_refund_permission(CheckTaskRepairerRefundPermission.No.getValue());
                item.setRepairer_follower_permission(CheckTaskRepairerFollowerPermission.CompleteRepair.getValue());
                item.setChecker_approve_permission(CheckerApprovePermission.No.getValue());
                item.setRepaired_picture_status(CheckTaskRepairedPictureEnum.UnForcePicture.getValue());
                item.setIssue_desc_status(CheckTaskIssueDescEnum.Arbitrary.getValue());
                item.setIssue_default_desc("(该问题无文字描述)");
            } else {

                JSONObject configData = JSON.parseObject(task.getConfigInfo());
                item.setRepairer_refund_permission(getValueOrDefault(configData, "repairer_refund_permission",
                        CheckTaskRepairerRefundPermission.No.getValue()));
                item.setRepairer_follower_permission(getValueOrDefault(configData, "repairer_follower_permission",
                        CheckTaskRepairerFollowerPermission.CompleteRepair.getValue()));
                item.setChecker_approve_permission(getValueOrDefault(configData, "checker_approve_permission",
                        CheckerApprovePermission.No.getValue()));
                item.setRepaired_picture_status(getValueOrDefault(configData, "repaired_picture_status",
                        CheckTaskRepairedPictureEnum.UnForcePicture.getValue()));
                item.setIssue_desc_status(getValueOrDefault(configData, "issue_desc_status",
                        CheckTaskIssueDescEnum.Arbitrary.getValue()));
                item.setIssue_default_desc(getStringValueOrDefault(configData, "issue_default_desc", "(该问题无文字描述)"));
            }

            maps.put(task.getTaskId(), item);
        }
    }

    /**
     *
     * @param configData
     * @param name
     * @param defautValue
     * @return
     */
    private Integer getValueOrDefault(JSONObject configData, String name, Integer defautValue) {
            Integer obj = configData.getInteger(name);
            if ( obj == null) {
                return defautValue;
            } else {
                return obj;
            }
    }

    /**
     *
     * @param configData
     * @param name
     * @param defautValue
     * @return
     */
    private String getStringValueOrDefault(JSONObject configData, String name, String defautValue) {

        String obj = configData.getString(name);

        if (configData.get(name) == null) {
            return defautValue;
        } else {
            return obj;
        }
    }
}
