package com.longfor.longjian.houseqm.app.controller.oapiv3houseqm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.base.LjBaseResponse;
import com.longfor.longjian.common.entity.ProjectBase;
import com.longfor.longjian.common.util.CtrlTool;
import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.common.util.SessionInfo;
import com.longfor.longjian.houseqm.app.req.TaskDeleteReq;
import com.longfor.longjian.houseqm.app.req.TaskTaskRoleReq;
import com.longfor.longjian.houseqm.app.req.task.TaskListInfoReq;
import com.longfor.longjian.houseqm.app.service.ITaskListService;
import com.longfor.longjian.houseqm.app.service.ITaskService;
import com.longfor.longjian.houseqm.app.utils.CtrlToolUtils;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRoleListRspVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskRspVo;
import com.longfor.longjian.houseqm.app.vo.TaskTaskRoleRspVo;
import com.longfor.longjian.houseqm.app.vo.task.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * http://192.168.37.159:3000/project/8/interface/api/266  任务编辑获取任务信息
 *
 * @author lipeishuai
 * @date 2018/11/19 18:25
 */

@RestController
@RequestMapping("oapi/v3/houseqm/task/")
@Slf4j
public class TaskController {
    @Resource
    private ITaskService taskService;
    @Resource
    private CtrlTool ctrlTool;
    @Resource
    private SessionInfo sessionInfo;
    @Resource
    private ITaskListService iTaskListService;
    @Resource
    private SpeConfigVo spec;

    @RequestMapping(value = "view", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<HouseQmCheckTaskRspVo> view(@RequestParam(value = "project_id") Integer projectId,
                                                      @RequestParam(value = "task_id") Integer taskId) {

        HouseQmCheckTaskRspVo houseQmCheckTaskRspVo = taskService.getHouseQmCheckTaskByProjTaskId(projectId, taskId);
        LjBaseResponse<HouseQmCheckTaskRspVo> response = new LjBaseResponse<>();
        response.setData(houseQmCheckTaskRspVo);

        return response;
    }

    /**
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.task.TaskListInfoRspVo>
     * @Author hy
     * @Description 项目下任务近况
     * http://192.168.37.159:3000/project/8/interface/api/3699
     * @Date 11:19 2019/1/24
     * @Param [request, req]
     **/
    @RequestMapping(value = "list_info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskListInfoRspVo> listInfo(HttpServletRequest request, @Valid TaskListInfoReq req) {
        LjBaseResponse<TaskListInfoRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.任务管理.查看", "项目.工程检查.任务管理.查看"});
        } catch (Exception e) {
            log.error(e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        // 如果没有页数，默认取1000个
        if (req.getPage().equals(0)) {
            req.setPage(1);
            req.setPage_size(1000);
        }
        ProjectBase proj = (ProjectBase) this.sessionInfo.getBaseInfo("cur_proj");
        HouseQmCheckTaskListAndTotalVo result = taskService.searchHouseQmCheckTaskByProjCategoryClsStatusPage(proj.getId(), req.getCategory_cls(), req.getStatus(), req.getPage(), req.getPage_size());
        List<HouseQmCheckTask> res = result.getList();
        Integer total = result.getTotal();
        Team teamGroup = null;
        try {
            teamGroup = iTaskListService.getTopTeam(proj.getTeamId());
        } catch (Exception e) {
            log.warn(e.getMessage());
            response.setResult(1);
            response.setMessage(e.getMessage());
            return response;
        }
        TaskListInfoRspVo data = new TaskListInfoRspVo();
        List<Integer> taskIds = Lists.newArrayList();
        List<HouseQmCheckTaskInfoRspVo> taskList = Lists.newArrayList();
        String teamGroupId = "team_group_" + teamGroup.getTeamId();
        Map<String, String> map = Maps.newHashMap();
        map.put("team_group_100044", spec.getTeam_group_100044().getExport_issue());
        map.put("team_group_100194", spec.getTeam_group_100194().getExport_issue());
        map.put("team_group_100137", spec.getTeam_group_100137().getExport_issue());
        for (HouseQmCheckTask item : res) {
            taskIds.add(item.getTaskId());
            HouseQmCheckTaskInfoRspVo task = new HouseQmCheckTaskInfoRspVo();
            task.setProject_id(item.getProjectId());
            task.setTask_id(item.getTaskId());
            task.setName(item.getName());
            task.setStatus(item.getStatus());
            task.setCategory_cls(item.getCategoryCls());
            task.setRoot_category_key(item.getRootCategoryKey());
            task.setArea_ids(item.getAreaIds());
            task.setArea_types(item.getAreaTypes());
            task.setPlan_begin_on(item.getPlanBeginOn()==null?0:DateUtil.dateToTimestamp(item.getPlanBeginOn()));
            task.setPlan_end_on(item.getPlanEndOn()==null?0:DateUtil.dateToTimestamp(item.getPlanEndOn()));
            task.setCreate_at(item.getCreateAt() == null ? 0 : DateUtil.dateToTimestamp(item.getCreateAt()));
            task.setUpdate_at(item.getUpdateAt() == null ? 0 : DateUtil.dateToTimestamp(item.getUpdateAt()));
            task.setDelete_at(item.getDeleteAt() == null ? 0 : DateUtil.dateToTimestamp(item.getDeleteAt()));
            // 通过读取配置文件 获取导出问题报告路径
            if (map.containsKey(teamGroupId)) {
                HouseQmCheckTaskExOps exOps = new HouseQmCheckTaskExOps();
                exOps.setExport_issue(map.get(teamGroupId) + "?project_id=" + proj.getId() + "&task_id=" + task.getTask_id());
                task.setExtra_ops(exOps);
            }
            task.setIssue_count(0);
            task.setRecord_count(0);
            task.setIssue_recorded_count(0);
            task.setIssue_assigned_count(0);
            task.setIssue_repaired_count(0);
            task.setIssue_approveded_count(0);
            taskList.add(task);
        }

        Map<Integer, CheckTaskIssueTypeStatInfo> taskStatMap = iTaskListService.searchTaskIssueStatMapByTaskIds(taskIds);
        for (HouseQmCheckTaskInfoRspVo item : taskList) {
            if (taskStatMap.containsKey(item.getTask_id())) {
                CheckTaskIssueTypeStatInfo stat = taskStatMap.get(item.getTask_id());
                item.setIssue_count(stat.getIssueCount());
                item.setRecord_count(stat.getRecordCount());
                item.setIssue_recorded_count(stat.getIssueRecordedCount());
                item.setIssue_assigned_count(stat.getIssueAssignedCount());
                item.setIssue_repaired_count(stat.getIssueRepairedCount());
                item.setIssue_approveded_count(stat.getIssueApprovededCount());
            }
        }

        data.setTask_list(taskList);
        data.setTotal(total);
        response.setData(data);
        return response;
    }

    @RequestMapping(value = "delete", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse delete(HttpServletRequest request, @Valid TaskDeleteReq req) {
        LjBaseResponse response = new LjBaseResponse();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.任务管理.删除", "项目.工程检查.任务管理.删除"});
            taskService.deleteHouseQmCheckTaskByProjTaskId(req.getProject_id(), req.getTask_id());
        } catch (Exception e) {
            log.error(e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        return response;
    }

    /*
     * @Author hy
     * @Description 根据任务ID获取角色列表
     * @Date 13:45 2019/1/9
     * @Param [req]
     * @return com.longfor.longjian.common.base.LjBaseResponse<com.longfor.longjian.houseqm.app.vo.TaskTaskRoleRspVo>
     **/
    @RequestMapping(value = "task_role", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LjBaseResponse<TaskTaskRoleRspVo> taskRole(HttpServletRequest request, @Valid TaskTaskRoleReq req) {
        LjBaseResponse<TaskTaskRoleRspVo> response = new LjBaseResponse<>();
        try {
            ctrlTool.projPermMulti(request, new String[]{"项目.移动验房.任务管理.查看", "项目.工程检查.任务管理.查看"});
            List<UserInHouseQmCheckTask> res = taskService.searchUserInKeyHouseQmCheckTaskByTaskId(req.getTask_id());
            log.debug("task_role --->" + res);
            List<Integer> uids = res.stream().map(UserInHouseQmCheckTask::getUserId).collect(Collectors.toList());
            Map<Integer, User> userMap = taskService.getUsersByIds(uids);
            List<HouseQmCheckTaskRoleListRspVo> roleList = Lists.newArrayList();
            for (UserInHouseQmCheckTask item : res) {
                HouseQmCheckTaskRoleListRspVo role = new HouseQmCheckTaskRoleListRspVo();
                role.setId(item.getId());
                role.setUser_id(item.getUserId());
                role.setSquad_id(item.getSquadId());
                role.setRole_type(item.getRoleType());
                role.setCan_approve(item.getCanApprove());
                role.setTask_id(item.getTaskId());
                role.setReal_name("");
                if (userMap.containsKey(role.getUser_id())) {
                    role.setReal_name(userMap.get(role.getUser_id()).getRealName());
                }
                roleList.add(role);
            }
            TaskTaskRoleRspVo data = new TaskTaskRoleRspVo();
            data.setRole_list(roleList);
            response.setData(data);
            response.setResult(0);
        } catch (Exception e) {
            log.error(e.getMessage());
            return CtrlToolUtils.errorReturn(response,e);
        }
        return response;
    }
}
