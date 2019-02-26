package com.longfor.longjian.houseqm.app.service.impl;

import java.util.Date;

import java.lang.String;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.service.HouseqmStaticService;
import com.longfor.longjian.houseqm.app.service.IHouseqmService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.*;
import com.longfor.longjian.houseqm.consts.HouseQmCheckTaskIssueEnum;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.houseqm.domain.internalService.*;
import com.longfor.longjian.houseqm.consts.*;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueListDto;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueDto;
import com.longfor.longjian.houseqm.dto.RepossessionStatusCompleteDailyCountDto;
import com.longfor.longjian.houseqm.po.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.MathUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    IHouseqmService iHouseqmService;
    @Resource
    HouseqmStaticService houseqmStaticService;
    @Resource
    AreaService areaService;
    @Resource
    CategoryService categoryService;
    @Resource
    CheckItemService checkItemService;
    @Resource
    FileResourceService fileService;
    @Resource
    FileResourceService fileResourceService;

    @Resource
    CategoryV3Service categoryV3Service;

    @Resource
    CheckItemV3Service checkItemV3Service;
    @Resource
    RepossessionStatusService repossessionStatusService;
    @Resource
    private UserInProjectService iUserInProjectService;
    @Resource
    private UserInHouseQmCheckTaskService userInHouseQmCheckTaskService;
    @Resource
    private ProjectService projectService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserService userService;

    @Override
    public ProjectRepairerStatRspVo projectRepairerStat(int uid, Integer project_id, Integer task_id, String source, Integer stat_begin, Integer stat_end, Integer timestamp) {
        String statBegin = "2000-01-01 00:00:00";
        String statEnd = "2100-01-01 23:59:59";
        if (!stat_begin.equals(0)) {
            statBegin = com.longfor.longjian.common.util.DateUtil.timestampToString(stat_begin);
        }
        if (!stat_end.equals(0)) {
            statEnd = com.longfor.longjian.common.util.DateUtil.timestampToString(stat_end);
        }
        List<Integer> category_cls_list = getCategoryClsListByProjectList(source);
        List<Integer> my_task_ids = Lists.newArrayList();
        List<UserInHouseQmCheckTask> task_list = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        for (UserInHouseQmCheckTask task : task_list) {
            if (!my_task_ids.contains(task.getTaskId())) {
                my_task_ids.add(task.getTaskId());
            }
        }
        List<Integer> task_ids = Lists.newArrayList();
        if (task_id > 0) {
            task_ids.add(task_id);
        } else {
            List<Integer> temp_task_ids = Lists.newArrayList();
            List<HouseQmCheckTask> taskList = houseQmCheckTaskService.searchByProjectIdAndCategoryClsIn(project_id, category_cls_list);
            for (HouseQmCheckTask task : taskList) {
                if (!temp_task_ids.contains(task.getTaskId())) {
                    temp_task_ids.add(task.getTaskId());
                }
            }
            for (Integer id : temp_task_ids) {
                if (my_task_ids.contains(id)) {
                    task_ids.add(id);
                }
            }
        }
        List<UserInHouseQmCheckTask> user_list = userInHouseQmCheckTaskService.searchByTaskIdInAndRoleType(task_ids, HouseQmCheckTaskRoleTypeEnum.Repairer.getId());
        List<Integer> all_user_ids = Lists.newArrayList();
        for (UserInHouseQmCheckTask user : user_list) {
            if (!all_user_ids.contains(user.getUserId())) {
                all_user_ids.add(user.getUserId());
            }
        }
        Map<Integer, String> user_map = createUsersMap(all_user_ids);
        Map<Integer, ApiHouseQmRepairerStatVo> stat_map = Maps.newHashMap();
        for (Integer repairer_id : all_user_ids) {
            ApiHouseQmRepairerStatVo stat = new ApiHouseQmRepairerStatVo();
            stat.setUser_id(repairer_id);
            stat.setReal_name(user_map.getOrDefault(repairer_id, ""));
            stat_map.put(repairer_id, stat);
        }
        List<Integer> typs = Lists.newArrayList(HouseQmCheckTaskIssueTypeEnum.FindProblem.getId(), HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        List<Integer> status = Lists.newArrayList(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId(), HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId(), HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());

        List<HouseQmCheckTaskIssue> issue_list = houseQmCheckTaskIssueService.searchByProjIdAndCategoryClsInAndRepairerIdAndClientCreateAtAndTypInAndStatusInAndTaskIdOrderByClientCreateAt(project_id, category_cls_list, statBegin, statEnd, typs, status, task_id, my_task_ids);

        for (HouseQmCheckTaskIssue item : issue_list) {
            if (stat_map.containsKey(item.getRepairerId())) {
                if (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(item.getStatus())) {
                    stat_map.get(item.getRepairerId()).setIssue_assigned_count(stat_map.get(item.getRepairerId()).getIssue_assigned_count() + 1);
                } else if (HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId().equals(item.getStatus())) {
                    stat_map.get(item.getRepairerId()).setIssue_repaired_count(stat_map.get(item.getRepairerId()).getIssue_repaired_count() + 1);
                } else if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(item.getStatus())) {
                    stat_map.get(item.getRepairerId()).setIssue_approveded_count(stat_map.get(item.getRepairerId()).getIssue_approveded_count() + 1);
                }
            }
        }
        List<ApiHouseQmRepairerStatVo> items = Lists.newArrayList();
        for (Map.Entry<Integer, ApiHouseQmRepairerStatVo> stat : stat_map.entrySet()) {
            items.add(stat.getValue());
        }
        ProjectRepairerStatRspVo result = new ProjectRepairerStatRspVo();
        result.setItems(items);
        return result;
    }

    @Override
    public ProjectCheckerStatRspVo projectCheckerStat(int uid, Integer project_id, Integer task_id, String source, Integer stat_begin, Integer stat_end, Integer timestamp) {
        String statBegin = "2000-01-01 00:00:00";
        String statEnd = "2100-01-01 23:59:59";
        if (!stat_begin.equals(0)) {
            statBegin = com.longfor.longjian.common.util.DateUtil.timestampToString(stat_begin);
        }
        if (!stat_end.equals(0)) {
            statEnd = com.longfor.longjian.common.util.DateUtil.timestampToString(stat_end);
        }
        List<Integer> my_task_ids = Lists.newArrayList();
        List<UserInHouseQmCheckTask> task_list = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        for (UserInHouseQmCheckTask task : task_list) {
            if (!my_task_ids.contains(task.getTaskId())) {
                my_task_ids.add(task.getTaskId());
            }
        }
        List<Integer> category_cls_list = getCategoryClsListByProjectList(source);
        List<Integer> task_ids = Lists.newArrayList();
        if (task_id > 0) {
            task_ids.add(task_id);
        } else {
            List<Integer> temp_task_ids = Lists.newArrayList();
            List<HouseQmCheckTask> taskList = houseQmCheckTaskService.searchByProjectIdAndCategoryClsIn(project_id, category_cls_list);
            for (HouseQmCheckTask task : taskList) {
                if (!temp_task_ids.contains(task.getTaskId())) {
                    temp_task_ids.add(task.getTaskId());
                }
            }
            for (Integer id : temp_task_ids) {
                if (my_task_ids.contains(id)) {
                    task_ids.add(id);
                }
            }
        }
        List<UserInHouseQmCheckTask> user_list = userInHouseQmCheckTaskService.searchByTaskIdInAndRoleType(task_ids, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        List<Integer> user_ids = Lists.newArrayList();
        for (UserInHouseQmCheckTask user : user_list) {
            if (!user_ids.contains(user.getUserId())) {
                user_ids.add(user.getUserId());
            }
        }
        Map<Integer, String> user_map = createUsersMap(user_ids);
        Map<Integer, ApiHouseQmCheckerStatVo> stat_map = Maps.newHashMap();
        for (Integer sender_id : user_ids) {
            ApiHouseQmCheckerStatVo stat = new ApiHouseQmCheckerStatVo();
            stat.setUser_id(sender_id);
            stat.setReal_name(user_map.getOrDefault(sender_id, ""));
            stat_map.put(sender_id, stat);
        }
        List<Integer> typs = Lists.newArrayList(HouseQmCheckTaskIssueTypeEnum.Record.getId(), HouseQmCheckTaskIssueTypeEnum.FindProblem.getId(), HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        List<HouseQmCheckTaskIssue> issue_list = houseQmCheckTaskIssueService.searchByProjIdAndCategoryClsInAndSenderIdAndClientCreateAtAndTypAndTaskIdOrderByClientCreateAt(project_id, category_cls_list, statBegin, statEnd, typs, task_id, my_task_ids);
        for (HouseQmCheckTaskIssue item : issue_list) {
            if (stat_map.containsKey(item.getSenderId())) {
                List<Integer> status = Lists.newArrayList(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId(), HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId(), HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId(), HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
                if (item.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.NoProblem.getId())) {
                    stat_map.get(item.getSenderId()).setRecords_count(stat_map.get(item.getSenderId()).getRecords_count() + 1);
                } else if (status.contains(item.getStatus())) {
                    stat_map.get(item.getSenderId()).setIssue_count(stat_map.get(item.getSenderId()).getIssue_count() + 1);
                    if (item.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId())) {
                        stat_map.get(item.getSenderId()).setIssue_approveded_count(stat_map.get(item.getSenderId()).getIssue_approveded_count() + 1);
                    }
                }
            }
        }
        ProjectCheckerStatRspVo result = new ProjectCheckerStatRspVo();
        List<ApiHouseQmCheckerStatVo> items = Lists.newArrayList();
        for (Map.Entry<Integer, ApiHouseQmCheckerStatVo> stat : stat_map.entrySet()) {
            items.add(stat.getValue());
        }
        result.setItems(items);
        return result;
    }

    private Map<Integer, String> createUsersMap(List<Integer> user_ids) {
        if (user_ids.isEmpty()) return Maps.newHashMap();
        List<User> resUser = userService.searchByUserIdInAndNoDeleted(user_ids);
        return resUser.stream().collect(Collectors.toMap(User::getUserId, User::getRealName));
    }


    @Override
    public ProjectIssueStatRspVo projectIssueStat(int uid, Integer project_id, String source, Integer area_id, Integer timestamp) {
        ProjectIssueStatRspVo result = new ProjectIssueStatRspVo();
        ApiHouseQmProjectStatVo item = new ApiHouseQmProjectStatVo();
        item.setTask_count(0);
        item.setIssue_approveded_count(0);
        item.setIssue_assigned_count(0);
        item.setIssue_count(0);
        item.setIssue_recorded_count(0);
        item.setIssue_repaired_count(0);
        item.setRecord_count(0);
        result.setItem(item);
        List<UserInHouseQmCheckTask> user_task_list = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        List<Integer> task_ids = Lists.newArrayList();
        user_task_list.forEach(task -> {
            if (!task_ids.contains(task.getTaskId())) {
                task_ids.add(task.getTaskId());
            }
        });
        if (task_ids.size() == 0) return result;
        List<Integer> category_cls_list = getCategoryClsListByProjectList(source);
        List<HouseQmCheckTask> task_list = Lists.newArrayList();
        if (area_id.equals(0)) {
            task_list = houseQmCheckTaskService.searchByProjectIdAndCategoryClsInAndTaskIdIn(project_id, category_cls_list, task_ids);
            item.setTask_count(task_list.size());
        } else {
            task_list = houseQmCheckTaskService.searchByProjectIdAndCategoryClsInAndTaskIdIn(project_id, category_cls_list, task_ids);
            List<Integer> area_ids = Lists.newArrayList();
            for (HouseQmCheckTask task : task_list) {
                area_ids.addAll(StringUtil.strToInts(task.getAreaIds(), ","));
            }
            List<Integer> areaIds = new HashSet<>(area_ids).stream().collect(Collectors.toList());
            List<Area> area_list = areaService.selectByAreaIds(areaIds);
            Map<Integer, String> area_map = Maps.newHashMap();
            for (Area area : area_list) {
                area_map.put(area.getId(), area.getPath() + area.getId() + "/");
            }
            for (HouseQmCheckTask task : task_list) {
                List<Integer> ids = StringUtil.strToInts(task.getAreaIds(), ",");
                if (checkRootAreaIntersectAreas(area_map, area_id, ids)) {
                    item.setTask_count(item.getIssue_count() + 1);
                }
            }
        }
        String area_path = "";
        if (area_id > 0) {
            Area area = areaService.selectById(area_id);
            area_path = area.getPath() + area.getId() + "/";
        }
        List<HouseQmCheckTaskIssue> issue_list = houseQmCheckTaskIssueService.searchByProjectIdAndCategoryClsInAndTaskIdInAndAreaPathAndIdLike(project_id, category_cls_list, task_ids, area_path);
        for (HouseQmCheckTaskIssue issue : issue_list) {
            if (HouseQmCheckTaskIssueStatusEnum.NoProblem.getId().equals(issue.getStatus())) {
                item.setRecord_count(item.getRecord_count() + 1);
            } else if (HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId().equals(issue.getStatus())) {
                item.setIssue_count(item.getIssue_count() + 1);
                item.setIssue_recorded_count(item.getIssue_recorded_count() + 1);
            } else if (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(issue.getStatus())) {
                item.setIssue_count(item.getIssue_count() + 1);
                item.setIssue_assigned_count(item.getIssue_assigned_count() + 1);
            } else if (HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId().equals(issue.getStatus())) {
                item.setIssue_count(item.getIssue_count() + 1);
                item.setIssue_repaired_count(item.getIssue_repaired_count() + 1);
            } else if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(issue.getStatus())) {
                item.setIssue_count(item.getIssue_count() + 1);
                item.setIssue_approveded_count(item.getIssue_approveded_count() + 1);
            }
        }
        result.setItem(item);
        return result;
    }

    private Boolean checkRootAreaIntersectAreas(Map<Integer, String> areaMap, Integer areaId, List<Integer> comma) {
        for (int i = 0; i < comma.size(); i++) {
            if (comma.get(i).equals(areaId)) {
                return true;
            }
            if (areaMap.containsKey(comma.get(i))) {
                if (areaMap.get(comma.get(i)).contains("/" + areaId + "/")) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ProjectListRspVo projectList(int uid, String source, Integer timestamp) {
        ProjectListRspVo result = new ProjectListRspVo();
        List<UserInProject> user_project_list = iUserInProjectService.searchByUserId(uid);
        List<Integer> project_ids = Lists.newArrayList();
        for (UserInProject project : user_project_list) {
            if (!project_ids.contains(project.getProjectId())) {
                project_ids.add(project.getProjectId());
            }
        }
        if (project_ids.size() == 0) return result;
        List<UserInHouseQmCheckTask> user_task_list = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        List<Integer> checkers = Lists.newArrayList();
        user_task_list.forEach(task -> {
            if (!checkers.contains(task.getTaskId())) {
                checkers.add(task.getTaskId());
            }
        });
        List<HouseQmCheckTask> task_list = houseQmCheckTaskService.searchByProjectIdInAndCategoryClsIn(project_ids, getCategoryClsListByProjectList(source));
        List<Integer> projectIds = Lists.newArrayList();
        for (HouseQmCheckTask task : task_list) {
            if (checkers.contains(task.getTaskId())) {
                projectIds.add(task.getProjectId());
            }
        }
        if (projectIds.size() == 0) return result;
        List<Project> project_list = projectService.searchByProjectIdIn(projectIds);
        List<Integer> team_ids = Lists.newArrayList();
        for (Project project : project_list) {
            if (!team_ids.contains(project.getTeamId())) {
                team_ids.add(project.getTeamId());
            }
        }
        Map<Integer, String> team_map = createTeamsMap(team_ids);
        List<ApiHouseqmStatisticProjectList> items = Lists.newArrayList();
        for (Project project : project_list) {
            ApiHouseqmStatisticProjectList item = new ApiHouseqmStatisticProjectList();
            item.setId(project.getId());
            item.setName(project.getName());
            item.setTeam_id(project.getTeamId());
            item.setTeam_name(team_map.getOrDefault(project.getTeamId(), ""));
            items.add(item);
        }
        result.setItems(items);
        return result;
    }

    private Map<Integer, String> createTeamsMap(List<Integer> team_ids) {
        List<Team> resTeam = teamService.searchByTeamIdIn(team_ids);
        return resTeam.stream().collect(Collectors.toMap(Team::getTeamId, Team::getTeamName));
    }

    //get_category_cls_list(source)
    private List<Integer> getCategoryClsListByProjectList(String source) {
        List<Integer> category_cls_list = Lists.newArrayList();
        if (source.equals("gcgl")) {
            category_cls_list.add(CategoryClsTypeEnum.RCJC.getId());
            category_cls_list.add(CategoryClsTypeEnum.YDJC.getId());
            category_cls_list.add(CategoryClsTypeEnum.JDJC.getId());
            category_cls_list.add(CategoryClsTypeEnum.YB.getId());
            category_cls_list.add(CategoryClsTypeEnum.FBFX.getId());
        } else {
            category_cls_list.add(CategoryClsTypeEnum.FHYS.getId());
            category_cls_list.add(CategoryClsTypeEnum.RHYF.getId());
        }
        return category_cls_list;
    }

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
        HouseQmCheckTaskIssueStatusEnum e = null;
        for (HouseQmCheckTaskIssue issue : issues) {
            for (HouseQmCheckTaskIssueStatusEnum value : HouseQmCheckTaskIssueStatusEnum.values()) {
                if (value.getId().equals(issue.getStatus())) {
                    e = value;
                    break;
                }
            }
            //处理详细统计数
            if (e != null) {
                switch (e) {
                    case NoteNoAssign://已记录未分配
                        result.setIssue_recorded_count(result.getIssue_recorded_count()+issue.getPosX());
                        break;
                    case AssignNoReform://已分配未整改
                        result.setIssue_assigned_count(result.getIssue_assigned_count()+issue.getPosX());
                        break;
                    case ReformNoCheck://已整改未验收
                        result.setIssue_repaired_count(result.getIssue_repaired_count()+issue.getPosX());
                        break;
                    case CheckYes://已验收
                        result.setIssue_approveded_count(result.getIssue_approveded_count()+issue.getPosX());
                        break;
                    default:
                        break;
                }
                //处理状态统计数
                switch (e) {
                    case NoProblem:
                        result.setRecord_count(result.getRecord_count() + issue.getPosX());
                        break;
                    case NoteNoAssign:
                    case AssignNoReform:
                    case ReformNoCheck:
                    case CheckYes:
                        result.setIssue_count(result.getIssue_count() + issue.getPosX());
                        break;
                    default:
                        break;
                }
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
            if (task == null) throw new LjBaseRuntimeException(500, "任务不存在");
            // 获取出任务下的区域与检验类型的交集
            List<Integer> areaIds = StringSplitToListUtil.splitToIdsComma(task.getAreaIds(), ",");
            List<Integer> areaTypes = StringSplitToListUtil.splitToIdsComma(task.getAreaTypes(), ",");
            if (areaIds.size() == 0 || areaTypes.size() == 0) {
                return houseStatVo;
            }
            List<Integer> areaIdList = Lists.newArrayList();
            areaIdList.add(areaId);
            List<Integer> list = Lists.newArrayList();
            list.addAll(areaIds);
            if (areaId > 0) {
                list = areaService.getIntersectAreas(areaIds, areaIdList);
            }
            List<Area> areas = areaService.searchAreaListByRootIdAndTypes(projectId, list, areaTypes);
            houseStatVo.setHouse_count(areas.size());

            //计算总户数
            // 找出拥有问题的最小状态，用来区分是否所有问题都处理完了
            Map<Integer, IssueMinStatusVo> areaIssueMap = getIssueMinStatusMapByTaskIdAndAreaId(taskId, areaId, true);
            Map<Integer, IssueMinStatusVo> checkedAreaIssueMap = getIssueMinStatusMapByTaskIdAndAreaId(taskId, areaId, false);
            houseStatVo.setChecked_count(checkedAreaIssueMap.size());
            for (Map.Entry<Integer, IssueMinStatusVo> status : areaIssueMap.entrySet()) {
                houseStatVo.setHas_issue_count(houseStatVo.getHas_issue_count() + 1);
                if (status.getValue().getMinStatus().equals(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId())) {
                    houseStatVo.setRepaired_count(houseStatVo.getRepaired_count() + 1);
                }
                if (status.getValue().getMinStatus().equals(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId())) {
                    houseStatVo.setRepaired_count(houseStatVo.getRepaired_count() + 1);
                    houseStatVo.setApproved_count(houseStatVo.getApproved_count() + 1);
                }
            }
            return houseStatVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        HashMap<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId", projectId);
        condiMap.put("taskId", taskId);
        if (areaId > 0) {
            condiMap.put("areaId", "%/" + areaId + "/%");
        }
        if (beginOn != null && beginOn.getTime() / 1000 > 0) {
            condiMap.put("beginOn", beginOn);
        }
        if (endOn != null && endOn.getTime() / 1000 > 0) {
            condiMap.put("endOn", endOn);
        }
        Date now = new Date();
        String nowStr = com.longfor.longjian.common.util.DateUtil.dateToString(now);
        condiMap.put("now", nowStr);
        condiMap.put("types", types);
        condiMap.put("deleted", "false");
        condiMap.put("status", HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());

        condiMap.put("statusIn", Arrays.asList(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId(), HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId(), HouseQmCheckTaskIssueStatusEnum.CheckYes.getId()));
        issueCounts = houseQmCheckTaskIssueService.selectByProjectIdAndTaskIdAndTyeInAndDongTai(condiMap);
        IssueRepairCount ic = issueCounts.get(0);
        TaskRepairStatVo taskRepairStatVo = new TaskRepairStatVo();
        TaskRepairStatVo.TaskRepairVo item = taskRepairStatVo.new TaskRepairVo();
        if (ic.getTotal() == 0) {
            ic.setTotal(1);
        }
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

    /**
     * @param
     * @return com.longfor.longjian.houseqm.app.vo.ProjectDailyListVo
     * @author hy
     * @date 2018/12/22 0022
     */
    @Override
    public HouseqmStatisticCategoryIssueListRspMsgVo taskIssueRepairList(Integer projectId, Integer taskId, Integer areaId, Integer beginOn, Integer endOn, Integer timestamp, Integer planStatus, String source, Integer page, Integer pageSize) {
        Date beginOn1 = DateUtil.timeStampToDate(0, "yyyy-MM-dd");
        Date endOn1 = DateUtil.timeStampToDate(0, "yyyy-MM-dd");
        if (beginOn != null && beginOn > 0) {
            beginOn1 = DateUtil.timeStampToDate(beginOn, "yyyy-MM-dd");
        }
        if (endOn != null && endOn > 0) {
            Date date = DateUtil.timeStampToDate(endOn, "yyyy-MM-dd");
            endOn1 = DateUtil.dateAddDay(date, 1);
        }
        if (planStatus <= 0 || planStatus > 5) throw new LjBaseRuntimeException(500, "invalid plan_status.");
        List<Integer> categoryClsList = getCategoryClsList(source);
        // 没有deleted_at is null
        // HouseQmCheckTaskIssueListVo issueListVo = searchHouseQmCheckTaskIssueByProjTaskIdClsInAreaIdPlanStatusBeginOnEndOnPage(projectId, taskId, categoryClsList, areaId, planStatus, beginOn1, endOn1, page, pageSize);
        HouseQmCheckTaskIssueListDto issueListVo = houseQmCheckTaskIssueService.selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai2(projectId, taskId, categoryClsList, areaId, planStatus, beginOn1, endOn1, page, pageSize);
        HouseQmCheckTaskIssueListVo houseQmCheckTaskIssueListVo = new HouseQmCheckTaskIssueListVo();
        houseQmCheckTaskIssueListVo.setHouseQmCheckTaskIssues(issueListVo.getHouseQmCheckTaskIssues());
        houseQmCheckTaskIssueListVo.setTotal(issueListVo.getTotal());

        List<Integer> areaIds = Lists.newArrayList();
        List<String> categoryKeys = Lists.newArrayList();
        List<String> checkItemKeys = Lists.newArrayList();
        List<String> attachmentMd5List = Lists.newArrayList();

        List<HouseQmCheckTaskIssue> resIssues = issueListVo.getHouseQmCheckTaskIssues();
        resIssues.forEach(item -> {
            areaIds.add(item.getAreaId());
            categoryKeys.addAll(StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getCategoryKey(), "/", "/"));
            checkItemKeys.addAll(StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getCheckItemPathAndKey(), "/", "/"));
            attachmentMd5List.addAll(StringSplitToListUtil.splitToStringComma(item.getAttachmentMd5List(), ","));
        });
        //列表元素去重
        List<Integer> areaIdList = CollectionUtil.removeDuplicate(areaIds);
        List<String> categoryKeyList = CollectionUtil.removeDuplicate(categoryKeys);
        List<String> checkItemKeyList = CollectionUtil.removeDuplicate(checkItemKeys);

        //调用file_resource service 方法 files
        List<FileResource> files = fileResourceService.searchFileResourceByFileMd5InAndNoDeleted(attachmentMd5List);
        HashMap<String, String> fileMap = Maps.newHashMap();
        files.forEach(item -> {
            fileMap.put(item.getFileMd5(), item.getStoreKey());
        });

        AreaMapVo areaMap = createAreasMapByLeaveIds(areaIdList);
        List<CategoryV3> categorys = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeyList);

        categorys.forEach(item -> {
            List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getPath(), "/", "/");
            list.add(item.getKey());
            categoryKeyList.addAll(list);
        });
        //去重 categoryKeyList
        CollectionUtil.removeDuplicate(categoryKeyList);

        List<CategoryV3> categoryV3s = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeyList);

        CategoryV3MapVo categoryMap = newCategoryMap(categoryV3s);

        List<CheckItemV3> checkItems = checkItemV3Service.searchCheckItemyV3ByKeyInAndNoDeleted(checkItemKeyList);

        CheckItemV3MapVo checkItemV3Map = newCheckItemMap(checkItems);
        //
        HouseqmStatisticCategoryIssueListRspMsgVo issueListRspMsgVo = new HouseqmStatisticCategoryIssueListRspMsgVo();
        List<HouseqmStatisticCategoryIssueListRspMsgVo.ApiTaskIssueRepairListRsp> issueList = Lists.newArrayList();
        resIssues.forEach(item -> {
            HouseqmStatisticCategoryIssueListRspMsgVo.ApiTaskIssueRepairListRsp apiTaskIssueRepairListRsp = issueListRspMsgVo.new ApiTaskIssueRepairListRsp();
            apiTaskIssueRepairListRsp.setId(item.getId());
            apiTaskIssueRepairListRsp.setProject_id(item.getProjectId());
            apiTaskIssueRepairListRsp.setTask_id(item.getTaskId());
            apiTaskIssueRepairListRsp.setUuid(item.getUuid());
            apiTaskIssueRepairListRsp.setTitle(item.getTitle());
            apiTaskIssueRepairListRsp.setTyp(item.getTyp());
            apiTaskIssueRepairListRsp.setContent(item.getContent());
            apiTaskIssueRepairListRsp.setCondition(item.getCondition());
            apiTaskIssueRepairListRsp.setStatus(item.getStatus());

            apiTaskIssueRepairListRsp.setPlan_end_on(DateUtil.datetimeToTimeStamp(item.getPlanEndOn()));
            apiTaskIssueRepairListRsp.setAttachment_md5_list(item.getAttachmentMd5List());
            apiTaskIssueRepairListRsp.setClient_create_at(DateUtil.datetimeToTimeStamp(item.getClientCreateAt()));
            apiTaskIssueRepairListRsp.setUpdate_at(DateUtil.datetimeToTimeStamp(item.getUpdateAt()));

            List<String> list = StringSplitToListUtil.splitToStringComma(item.getAttachmentMd5List(), ",");
            //apiTaskIssueRepairListRsp.setAttachment_url_list(new ArrayList<String>());
            list.forEach(fm -> {
                if (fileMap.containsKey(fm)) {
                    //List<String> attachmentUrlList = apiTaskIssueRepairListRsp.getAttachment_url_list();
                    List<String> attachmentUrlList = Lists.newArrayList();
                    attachmentUrlList.add(fileMap.get(fm));
                    apiTaskIssueRepairListRsp.setAttachment_url_list(attachmentUrlList);
                }
            });
            apiTaskIssueRepairListRsp.setArea_path_name(areaMap.getPathNames(item.getAreaId()));
            apiTaskIssueRepairListRsp.setCategory_path_name(categoryMap.getFullNamesByKey(item.getCategoryKey()));
            apiTaskIssueRepairListRsp.setCheck_item_path_name(checkItemV3Map.getFullNamesByKey(item.getCheckItemKey()));
            issueList.add(apiTaskIssueRepairListRsp);
        });

        issueListRspMsgVo.setIssue_list(issueList);
        issueListRspMsgVo.setTotal(issueListVo.getTotal());
        return issueListRspMsgVo;
    }

    /**
     * @param projectId
     * @param source
     * @param areaId
     * @param beginOn
     * @param endOn
     * @param timestamp
     * @return com.longfor.longjian.houseqm.app.vo.IssueRepairStatisticVo
     * @author hy
     * @date 2018/12/24 0024
     */
    @Override
    public IssueRepairStatisticVo projectIssueRepair(Integer projectId, String source, Integer areaId, Integer beginOn, Integer endOn, Integer timestamp) {
        Date beginOn1 = DateUtil.timeStampToDate(beginOn, "yyyy-MM-dd");
        Date endOn1 = DateUtil.timeStampToDate(endOn, "yyyy-MM-dd");
        if (beginOn > 0) {
            beginOn1 = DateUtil.timeStampToDate(beginOn, "yyyy-MM-dd");
        }
        if (endOn > 0) {
            Calendar c = Calendar.getInstance();
            Date date = DateUtil.timeStampToDate(endOn, "yyyy-MM-dd");
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endOn1 = DateUtil.timeStampToDate((int) (c.getTime().getTime() / 1000), "yyyy-MM-dd");
        }
        List<Integer> categoryClsList = getCategoryClsList(source);
        IssueRepairStatisticVo issueRepairStatisticVo = searchIssueRepairStatisticByProjCategoryClsInAreaIdBeginOnEndOn(projectId, categoryClsList, areaId, beginOn1, endOn1);
        return issueRepairStatisticVo;
    }

    @Override
    public HouseQmCheckTaskHouseStatInfoVo getHouseQmHouseQmCheckTaskHouseStatByTaskId(Integer prodectId, Integer taskId, Integer areaId) {
        ArrayList<Integer> taskIds = Lists.newArrayList();
        taskIds.add(taskId);
        // 读取出正常检查任务的统计
        CheckTaskHouseStatInfoVo normalStat = houseqmStaticService.GetHouseQmCheckTaskHouseStatByTaskId(prodectId, taskId, areaId);
        // 读取出移动验房部分的统计
        RepossessionTasksStatusInfoVo repossessionInfo = houseqmStaticService.getRepossessionTasksStatusInfo(prodectId, taskIds, areaId);
        HouseQmCheckTaskHouseStatInfoVo result = new HouseQmCheckTaskHouseStatInfoVo();
        result.setCheckedCount(repossessionInfo.getCheckedCount());
        result.setRepairConfirmCount(repossessionInfo.getRepairConfirmCount());
        result.setAcceptHasIssueCount(repossessionInfo.getAcceptHasIssueCount());
        result.setAcceptNoIssueCount(repossessionInfo.getAcceptNoIssueCount());
        result.setOnlyWatchCount(repossessionInfo.getOnlyWatch());
        result.setRejectCount(repossessionInfo.getRejectCount());
        result.setAcceptApprovedCount(repossessionInfo.getAcceptApprovedCount());
        result.setHouseCount(normalStat.getHouseCount());
        result.setHasIssueCount(normalStat.getHasIssueCount());
        result.setRepairedCount(normalStat.getRepairedCount());
        result.setApprovedCount(normalStat.getApprovedCount());

        return result;
    }

    /*
     * @Author hy
     * @Description
     * @Date 17:09 2019/1/8
     * @Param [projectId, taskIds, areaId]
     * @return com.longfor.longjian.houseqm.app.vo.RepossessionTasksStatusInfoVo
     **/
    @Override
    public RepossessionTasksStatusInfoVo getRepossessionTasksStatusInfo(Integer projectId, List<Integer> taskIds, Integer areaId) {
        RepossessionTasksStatusInfoVo info = new RepossessionTasksStatusInfoVo();
        info.setTaskName("");
        info.setTotal(0);
        info.setCheckedCount(0);
        info.setUncheckedCount(0);
        info.setCheckedRate("");
        info.setAcceptCount(0);
        info.setUnacceptCount(0);
        info.setHasIssueCount(0);
        info.setNoIssueCount(0);
        info.setAcceptHasIssueCount(0);
        info.setAcceptNoIssueCount(0);
        info.setAcceptHasIssueSignCount(0);
        info.setAcceptNoIssueSignCount(0);
        info.setRejectCount(0);
        info.setOnlyWatch(0);
        info.setRepairConfirmCount(0);
        info.setAcceptApprovedCount(0);

        taskIds.forEach(taskId -> {
            List<Area> areas = iHouseqmService.searchTargetAreaByTaskId(projectId, taskId);
            int total = areas.size();
            List<RepossessionStatus> items = repossessionStatusService.searchRepossessionStatusByTaskIdAreaIdLike(taskId, areaId);
            List<String> hasIssuePaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, null, areaId);
            HashMap<Integer, Boolean> hasIssueAreaId = Maps.newHashMap();
            for (String path : hasIssuePaths) {
                List<Integer> ids = StringSplitToListUtil.strToInts(path, "/");
                if (ids.size() > 0) {
                    hasIssueAreaId.put(ids.get(ids.size() - 1), true);
                }
            }

            List<Integer> statuses = Lists.newArrayList();
            statuses.add(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId());
            statuses.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
            statuses.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());

            // 有问题但是未销项完成的
            List<String> hasIssueNoApprovedPaths = houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, statuses, areaId);
            HashMap<Integer, Boolean> hasIssueNoApprovedAreaId = Maps.newHashMap();
            for (String path : hasIssuePaths) {
                List<Integer> ids = StringSplitToListUtil.strToInts(path, "/");
                if (ids.size() > 0) {
                    hasIssueNoApprovedAreaId.put(ids.get(ids.size() - 1), true);
                }
            }
            int checkedCount = 0;
            HashMap<Integer, Boolean> rAreaId = Maps.newHashMap();
            for (RepossessionStatus item : items) {
                // 去重
                if (rAreaId.containsKey(item.getAreaId())) continue;
                else rAreaId.put(item.getAreaId(), true);

                RepossessionStatusEnum senum = RepossessionStatusEnum.valueOf("");
                for (RepossessionStatusEnum e : RepossessionStatusEnum.values()) {
                    if (item.getStatus().equals(e.getId())) {
                        senum = e;
                        break;
                    }
                }
                switch (senum) {
                    case Accept: {
                        checkedCount += 1;
                        info.setAcceptCount(info.getAcceptCount() + 1);
                        if (hasIssueAreaId.containsKey(item.getAreaId())) {
                            info.setAcceptHasIssueCount(info.getAcceptHasIssueCount() + 1);
                            if (item.getSignStatus() == 1) {
                                info.setAcceptHasIssueSignCount(info.getAcceptHasIssueSignCount() + 1);
                            }

                            if (!hasIssueNoApprovedAreaId.containsKey(item.getAreaId())) {
                                info.setAcceptApprovedCount(info.getAcceptApprovedCount() + 1);
                            }

                            if (item.getRepairStatus().equals(RepossessionRepairStatusEnum.Confirmed.getId())) {
                                info.setRepairConfirmCount(info.getRepairConfirmCount() + 1);
                            }

                        } else {
                            info.setAcceptNoIssueCount(info.getAcceptNoIssueCount() + 1);
                            if (item.getSignStatus() == 1) {
                                info.setAcceptNoIssueSignCount(info.getAcceptNoIssueSignCount() + 1);
                            }
                        }

                    }
                    break;
                    case RejectAccept: {
                        checkedCount += 1;
                        info.setRejectCount(info.getRejectCount() + 1);
                        info.setUnacceptCount(info.getUnacceptCount() + 1);
                    }
                    break;
                    case OnlyCheck: {
                        checkedCount += 1;
                        info.setOnlyWatch(info.getOnlyWatch() + 1);
                        info.setUnacceptCount(info.getUnacceptCount() + 1);
                    }
                    break;
                    case None:
                        break;
                }

            }
            info.setTotal(info.getTotal() + total);
            info.setCheckedCount(info.getCheckedCount() + checkedCount);
            info.setUncheckedCount(info.getUncheckedCount() + (total - checkedCount));
        });
        info.setCheckedRate(MathUtil.getPercentageByPattern(info.getCheckedCount(), info.getTotal(), "0.0"));
        return info;
    }

    /*
     * 统计-验房统计-每天的交付数
     * @Author hy
     * @Description
     * @Date 20:47 2019/1/8
     * @Param [project_id, taskIds, beginOn, endOn, page, page_size]
     * @return com.longfor.longjian.houseqm.app.vo.StatHouseqmCompleteDailyRspVo
     **/
    @Override
    public StatHouseqmCompleteDailyRspVo searchRepossessionStatusCompleteDaily(Integer project_id, List<Integer> taskIds, int beginOn, int endOn, Integer page, Integer page_size) {
        // 计算总数量
        List<RepossessionStatusCompleteDailyCountDto> list = Lists.newArrayList();
        Map<String, Object> condi = Maps.newHashMap();
        condi.put("task_id", taskIds);
        condi.put("status", RepossessionStatusEnum.Accept.getId());
        Date begin_on, end_on = null;
        if (beginOn > 0) {
            begin_on = DateUtil.timeStampToDate(beginOn, "yyyy-MM-dd hh:mm:ss");
            condi.put("status_client_update_atgte", begin_on);
        }
        if (endOn > 0) {
            end_on = DateUtil.timeStampToDate(endOn, "yyyy-MM-dd hh:mm:ss");
            condi.put("status_client_update_atlte", end_on);
        }
        RepossessionStatusCompleteDailyCountDto bean = repossessionStatusService.searchByTaskIdInAndStatusAndNoDeletedOrStatusClientUpdateAt(condi);
        int total = bean.getCount();
        int offset = (page - 1) * page_size;
        condi.put("page_size", page_size);
        condi.put("offset", offset);
        list = repossessionStatusService.searchByTaskIdInAndStatusAndNoDeletedGroupByDateOrderByDateByPage(condi);

        StatHouseqmCompleteDailyRspVo result = new StatHouseqmCompleteDailyRspVo();
        List<HouseQmHouseQmStatCompleteDailyRspVo> items = list.stream().map(RepossessionStatusCompleteDailyCountDto -> {
            HouseQmHouseQmStatCompleteDailyRspVo hqs = new HouseQmHouseQmStatCompleteDailyRspVo();
            hqs.setDate(RepossessionStatusCompleteDailyCountDto.getDate());
            hqs.setCount(RepossessionStatusCompleteDailyCountDto.getCount());
            return hqs;
        }).collect(Collectors.toList());
        result.setItems(items);
        result.setTotal(total);
        return result;
    }


    /**
     * @param projectId
     * @param categoryClsList
     * @param areaId
     * @param beginOn1
     * @param endOn1
     * @return com.longfor.longjian.houseqm.app.vo.IssueRepairStatisticVo
     * @author hy
     * @date 2018/12/24 0024
     */
    private IssueRepairStatisticVo searchIssueRepairStatisticByProjCategoryClsInAreaIdBeginOnEndOn(Integer projectId, List<Integer> categoryClsList, Integer areaId, Date beginOn1, Date endOn1) {
        String nowStr = DateUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss");
        HashMap<String, Object> condiMap = new HashMap<>();
        condiMap.put("projectId", projectId);
        condiMap.put("categoryClsList", categoryClsList);

        if (areaId > 0) condiMap.put("areaPathAndId", "%/" + areaId + "/%");
        if (beginOn1.getTime() / 1000 > 0) condiMap.put("clientCreateAtGte", beginOn1);
        if (endOn1.getTime() / 1000 > 0) condiMap.put("clientCreateAtLte", endOn1);
        ArrayList<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        condiMap.put("typ", typs);
        condiMap.put("now", nowStr);
        condiMap.put("status", HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
        ArrayList<Integer> statusIn = Lists.newArrayList();
        statusIn.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
        statusIn.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
        statusIn.add(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
        condiMap.put("statusIn", statusIn);
        List<IssueRepairCount> issueCounts = houseQmCheckTaskIssueService.selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(condiMap);
        IssueRepairCount ic = issueCounts.get(0);
        IssueRepairStatisticVo item = new IssueRepairStatisticVo();
        if (ic.getTotal() == 0) {
            // 防止除数为0。总之，total为0的时候，全部结果是0%
            ic.setTotal(1);
        }

        item.setNo_plan_end_on(MathUtil.getPercentage(ic.getNoPlanEndOn(), ic.getTotal()));
        item.setOvertime_unfinish(MathUtil.getPercentage(ic.getOvertimeUnfinish(), ic.getTotal()));
        item.setInitime_unfinish(MathUtil.getPercentage(ic.getInitimeUnfinish(), ic.getTotal()));
        item.setOvertime_finish(MathUtil.getPercentage(ic.getOvertimeFinish(), ic.getTotal()));
        item.setInitime_finish(MathUtil.getPercentage(ic.getInitimeFinish(), ic.getTotal()));

        item.setTotal_count(ic.getTotal());
        item.setNo_plan_end_on_count(ic.getNoPlanEndOn());
        item.setOvertime_unfinish_count(ic.getOvertimeUnfinish());
        item.setInitime_unfinish_count(ic.getInitimeUnfinish());
        item.setOvertime_finish_count(ic.getOvertimeFinish());
        item.setInitime_finish_count(ic.getInitimeFinish());
        return item;
    }

    /**
     * @param projectId
     * @param taskId
     * @param categoryClsList
     * @param areaId
     * @param planStatus
     * @param beginOn
     * @param endOn
     * @param page
     * @param pageSize
     * @return com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskIssueListVo
     * @author hy
     * @date 2018/12/22 0022
     */
    private HouseQmCheckTaskIssueListVo searchHouseQmCheckTaskIssueByProjTaskIdClsInAreaIdPlanStatusBeginOnEndOnPage(Integer projectId, Integer taskId, List<Integer> categoryClsList, Integer areaId, Integer planStatus, Date beginOn, Date endOn, Integer page, Integer pageSize) {
        HashMap<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId", projectId);
        condiMap.put("categoryCls", categoryClsList);
        if (taskId > 0) condiMap.put("taskId", taskId);
        if (areaId > 0) condiMap.put("areaId", "/" + areaId + "/");
        if (beginOn.getTime() / 1000 > 0) condiMap.put("clientCreateAtGte", beginOn);
        if (endOn.getTime() / 1000 > 0) condiMap.put("clientCreateAtLte", endOn);
        ArrayList<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        condiMap.put("typs", typs);
        String nowStr = DateUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss");
        condiMap.put("now", nowStr);
        HouseQmIssuePlanStatusEnum e = null;
        for (HouseQmIssuePlanStatusEnum value : HouseQmIssuePlanStatusEnum.values()) {
            if (planStatus.equals(value.getId())) e = value;
        }
        if (e != null) {
            switch (e) {
                case OnTimeFinish:
                    condiMap.put("status1", true);
                    break;
                case UnOnTimeNotFinish:
                    condiMap.put("status2", true);
                    break;
                case NoSettingTime:
                    condiMap.put("status3", true);
                    break;
                case OverTimeFinish:
                    condiMap.put("status4", true);
                    break;
                case OverTimeNotFinish:
                    condiMap.put("status5", true);
                    break;
                default:
                    break;
            }
        }
        int start = 0;
        if (page > 0) {
            start = (page - 1) * pageSize;
        }
        int total = houseQmCheckTaskIssueService.selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai(condiMap);
        condiMap.put("start", start);
        condiMap.put("pageSize", pageSize);
        List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues = houseQmCheckTaskIssueService.selectHouseQmCheckTaskIssueByProjectIdAndCategoryClsAndTypeAndStatusInAndOrderByDescAndPageDongTai(condiMap);

        HouseQmCheckTaskIssueListVo houseQmCheckTaskIssueListVo = new HouseQmCheckTaskIssueListVo();
        houseQmCheckTaskIssueListVo.setTotal(total);
        houseQmCheckTaskIssueListVo.setHouseQmCheckTaskIssues(houseQmCheckTaskIssues);
        return houseQmCheckTaskIssueListVo;
    }

    /**
     * @param module
     * @return java.util.List<java.lang.Integer>
     * @author hy
     * @date 2018/12/22 0022
     */
    private List<Integer> getCategoryClsList(String module) {
        String source = null;
        if ("gcgl".equals(module)) { //兼容app 端
            source = CategoryClsConst.SOURCE_NAME_GCJC;
        }
        Map<String, List<Integer>> categoryClsMap = Maps.newHashMap();
        List<Integer> ydyf = Lists.newArrayList();
        ydyf.add(CategoryClsTypeEnum.FHYS.getId());
        ydyf.add(CategoryClsTypeEnum.RHYF.getId());

        List<Integer> gcjc = Lists.newArrayList();
        gcjc.add(CategoryClsTypeEnum.RCJC.getId());
        gcjc.add(CategoryClsTypeEnum.YDJC.getId());
        gcjc.add(CategoryClsTypeEnum.JDJC.getId());
        gcjc.add(CategoryClsTypeEnum.YB.getId());
        gcjc.add(CategoryClsTypeEnum.FBFX.getId());
        categoryClsMap.put(CategoryClsConst.SOURCE_NAME_YDYF, ydyf);
        categoryClsMap.put(CategoryClsConst.SOURCE_NAME_GCJC, gcjc);

        return categoryClsMap.get(source);
    }

    @Override
    public List<HouseQmIssueCategoryStatVo> searchHouseQmIssueCategoryStatByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date begin, Date endOns) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        ArrayList<SimpleHouseQmCheckTaskIssueStatVo> issueStatVoList = Lists.newArrayList();
        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.houseQmCheckTaskIssueByProTaskIdAreaidBegin(projectId, taskId, areaId, begin, endOns, types);
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
    public HouseQmCheckTaskIssueVoRsp searchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged(Integer projectId, String categoryKey, Integer areaId, Integer page, Integer pageSize) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        HashMap<String, Object> condiMap = Maps.newHashMap();
        condiMap.put("projectId", projectId);
        condiMap.put("categoryKey", categoryKey);
        condiMap.put("types", types);
        if (pageSize < 1) {
            pageSize = 10;
        }
        condiMap.put("pageSize", pageSize);
        if (page < 1) {
            page = 1;
        }
        condiMap.put("page", page);
        if (areaId > 0) {
            condiMap.put("areaId", areaId);
        }

        HouseQmCheckTaskIssueDto dto = houseQmCheckTaskIssueService.searchHouseQmCheckTaskIssueByProjCategoryKeyAreaId(condiMap);
        List<HouseQmCheckTaskIssue> issueList = dto.getItems();
        List<HouseQmCheckTaskIssueOnlineInfoVo> vos = null;
        HouseQmCheckTaskIssueVoRsp houseQmCheckTaskIssueVo = null;
        try {
            vos = formatHouseQmCheckTaskIssueOnlineInfo(issueList);
            houseQmCheckTaskIssueVo = new HouseQmCheckTaskIssueVoRsp();
            houseQmCheckTaskIssueVo.setTotal(dto.getTotal());
            houseQmCheckTaskIssueVo.setItems(vos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return houseQmCheckTaskIssueVo;
    }

    private List<HouseQmCheckTaskIssueOnlineInfoVo> formatHouseQmCheckTaskIssueOnlineInfo(List<HouseQmCheckTaskIssue> issueList) throws Exception {
        List<HouseQmCheckTaskIssueOnlineInfoVo> infos = convertHouseQmCheckTaskIssueToOnlineInfo(issueList);
        try {
            infos = fillHouseQmCheckTaskIssueOnlineInfoAreaInfo(infos);
            infos = fillHouseQmCheckTaskIssueOnlineInfoCategoryInfo(infos);
            infos = fillHouseQmCheckTaskIssueOnlineInfoFileInfo(infos);
        } catch (Exception e) {
            throw new Exception("error:" + e);
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
                    List<String> list = Lists.newArrayList();
                    list.add(map.get(split[j]));
                    infos.get(i).setAttachmentUrlList(list);
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
        List<CategoryV3> categoryList = categoryService.searchCategoryByKeyIn(categoryKeys);
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

    public AreaMapVo CreateAreasMapByAreaList(List<Area> areaList) {
        AreaMapVo vo = new AreaMapVo();
        Map<Integer, Area> map = Maps.newHashMap();
        for (int i = 0; i < areaList.size(); i++) {
            Area area = areaList.get(i);
            map.put(area.getId(), area);
        }
        vo.setList(areaList);
        vo.setAreas(map);
        return vo;
    }

    private List<Area> SelectAllByLeaveIds(Set<Integer> areaIds) {
        List<Integer> list = areaIds.stream().collect(Collectors.toList());
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
        for (int i = 0; i < issueList.size(); i++) {
            HouseQmCheckTaskIssueOnlineInfoVo vo = new HouseQmCheckTaskIssueOnlineInfoVo();
            vo.setId(issueList.get(i).getId());
            vo.setProjectId(issueList.get(i).getProjectId());
            vo.setTaskId(issueList.get(i).getTaskId());
            vo.setUuid(issueList.get(i).getUuid());
            vo.setTitle(issueList.get(i).getTitle());
            vo.setTyp(issueList.get(i).getTyp());
            vo.setContent(issueList.get(i).getContent());
            vo.setCondition(issueList.get(i).getCondition());
            vo.setStatus(issueList.get(i).getStatus());
            vo.setPlanEndOn(issueList.get(i).getPlanEndOn());
            vo.setAttachmentMd5List(issueList.get(i).getAttachmentMd5List());
            vo.setClientCreateAt(issueList.get(i).getClientCreateAt());
            vo.setUpdateAt(issueList.get(i).getUpdateAt());
            vo.setAreaId(issueList.get(i).getAreaId());
            vo.setCategoryKey(issueList.get(i).getCategoryKey());
            vo.setCategoryPathAndKey(issueList.get(i).getCategoryPathAndKey());
            vo.setCheckItemKey(issueList.get(i).getCheckItemKey());
            vo.setCheckItemPathAndKey(issueList.get(i).getCheckItemPathAndKey());
            infos.add(vo);
        }
        return infos;

    }

    public List<HouseQmIssueCategoryStatVo> calculateIssueCount
            (ArrayList<SimpleHouseQmCheckTaskIssueStatVo> issueStatVoList) {
        List<HouseQmIssueCategoryStatVo> r = Lists.newArrayList();

        Map<String, Object> map = groupIssueStatByCategoryAndCheckItem(issueStatVoList);

        // 补充必要信息
        Map<String, CategoryV3> categoryMap = getCategoryMapByCategoryKeys((List<String>) map.get("categoryKeys"));
        Map<String, CheckItem> checkItemMap = getCheckItemMapByCheckItemKeys((List<String>) map.get("checkItemKeys"));

        //补充名称 并计算父级节点数量（其下子节点相＋）
        // 判断统计时是从第二级开始还是从第三级开始
        // 部分检查项树第二级还只是有一个节点，所以需要从第三级开始
        boolean isStatLevel3 = false;
        if (categoryMap.size() > 0) {
            String rootKey = "";
            for (Map.Entry<String, CategoryV3> cate : categoryMap.entrySet()) {
                List<String> strings = StringSplitToListUtil.removeStartAndEndStrAndSplit(cate.getValue().getPath(), "/", "/");
                strings.add(cate.getValue().getKey());
                rootKey = strings.get(0);
            }
            isStatLevel3 = isCategoryStatLevelThree(rootKey);
        }
        Map<String, HouseQmIssueCategoryStatVo> categoryStatMap = (Map<String, HouseQmIssueCategoryStatVo>) map.get("categoryStatMap");
        for (Map.Entry<String, HouseQmIssueCategoryStatVo> categoryStat : categoryStatMap.entrySet()) {
            // 过滤掉不包括的大类，因为前两级可能是根节点
            // 为了兼容旧数据转换的问题。旧检查项转过来会多一级的根节点。所以过滤出V开头的，跳过前两级；其他的默认只跳过第一级

            //if cate, ok := categoryMap[categoryStat.Key]; ok {
            CategoryV3 cate = categoryMap.get(categoryStat.getValue().getKey());
            if (cate != null) {
                boolean isRoot = false;
                String str = cate.getPath();
                int level = StringSplitToListUtil.count(str, "/");
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
                categoryStat.getValue().setName(cate.getName());
                // 如果节点是第二级的，则将FatherKey设置为空，否则补充上去
                if (!isRoot) {
                    categoryStat.getValue().setParentKey(cate.getFatherKey());
                }
                r.add(categoryStat.getValue());
            }

        }
        Map<String, HouseQmIssueCategoryStatVo> checkItemStatMap = (Map<String, HouseQmIssueCategoryStatVo>) map.get("checkItemStatMap");
        for (Map.Entry<String, HouseQmIssueCategoryStatVo> checkItemStat : checkItemStatMap.entrySet()) {
            CheckItem citem = checkItemMap.get(checkItemStat.getValue().getKey());
            if (citem != null) {
                checkItemStat.getValue().setName(citem.getName());
                checkItemStat.getValue().setParentKey(citem.getCategoryKey());
                checkItemStat.getValue().setKey("C_" + checkItemStat.getValue().getKey());
                r.add(checkItemStat.getValue());
            }

        }



/*        // boolean isStatLevel3 = false;
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
                for (Map.Entry<String, CategoryV3> entry : categoryMap.entrySet()) {
                    String newStr = entry.getValue().getPath().substring(1, entry.getValue().getPath().length());
                    String[] split = newStr.split("/");
*//*
                    split[split.length]= entry.getValue().getKey();
*//*
                    rootKey = split[0];
                }
                isStatLevel3 = isCategoryStatLevelThree(rootKey);
            }
            if (entrys.getKey().equals("categoryStatMap")) {
                HashMap<String, HouseQmIssueCategoryStatVo> categoryStatMap = (HashMap<String, HouseQmIssueCategoryStatVo>) entrys.getValue();

                for (Map.Entry<String, HouseQmIssueCategoryStatVo> entry : categoryStatMap.entrySet()) {
                    for (Map.Entry<String, CategoryV3> Entry : categoryMap.entrySet()) {

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
                            r.add(entry.getValue());
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
                            r.add(entryS.getValue());
                        }


                    }
                }

            }

        }*/

        return r;
    }


    /**
     * 判断某个字符串出现的次数
     *
     * @param srcText
     * @param findText
     * @return
     */
    public static int appearNumber(String srcText, String findText) {
        int count = 0;
        Pattern p = Pattern.compile(findText);
        Matcher m = p.matcher(srcText);
        while (m.find()) {
            count++;
        }
        return count;
    }


    private Map<String, CheckItem> getCheckItemMapByCheckItemKeys(List<String> keys) {
        Map<String, CheckItem> map = new HashMap<>();
        if (keys.isEmpty()) return map;
        List<CheckItem> resCheckItems = checkItemService.SearchCheckItemByKeyIn(keys);
        for (CheckItem citem : resCheckItems) {
            map.put(citem.getKey(), citem);
        }
        return map;
    }

    private Map<String, CategoryV3> getCategoryMapByCategoryKeys(List<String> keys) {
        Map<String, CategoryV3> map = new HashMap<>();
        if (keys.isEmpty()) return map;
        List<CategoryV3> resCategoryItems = categoryService.searchCategoryByKeyIn(keys);
        for (CategoryV3 cate : resCategoryItems) {
            map.put(cate.getKey(), cate);
        }
        return map;
    }

    private Map<String, Object> groupIssueStatByCategoryAndCheckItem
            (ArrayList<SimpleHouseQmCheckTaskIssueStatVo> issueStatVoList) {

        Map<String, HouseQmIssueCategoryStatVo> categoryStatMap = new HashMap<>();
        Map<String, HouseQmIssueCategoryStatVo> checkItemStatMap = new HashMap<>();
        ArrayList<String> categoryKeys = Lists.newArrayList();
        ArrayList<String> checkItemKeys = Lists.newArrayList();

        for (SimpleHouseQmCheckTaskIssueStatVo item : issueStatVoList) {
            List<String> categoryPathKeys = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getCategoryPathAndKey(), "/,", "/");
            for (String key : categoryPathKeys) {
                //判断key是否存在
                if (!categoryStatMap.containsKey(key)) {
                    HouseQmIssueCategoryStatVo houseQmIssueCategoryStatVo = new HouseQmIssueCategoryStatVo();

                    houseQmIssueCategoryStatVo.setKey(key);
                    houseQmIssueCategoryStatVo.setIssueCount(0);
                    //存放key value（对象中的key值）
                    categoryStatMap.put(key, houseQmIssueCategoryStatVo);
                }
                //categoryStatMap[key].IssueCount += item.Count
                int count = categoryStatMap.get(key).getIssueCount() + item.getCount();
                categoryStatMap.get(key).setIssueCount(count);
            }
            categoryKeys.addAll(categoryPathKeys);
            //当CheckItemKey的长度大于0
            if (item.getCheckItemKey().length() > 0) {
                //判断key是否存在于map
                if (!checkItemStatMap.containsKey(item.getCheckItemKey())) {
                    HouseQmIssueCategoryStatVo houseQmIssueCategoryStatVo = new HouseQmIssueCategoryStatVo();
                    houseQmIssueCategoryStatVo.setKey(item.getCheckItemKey());
                    houseQmIssueCategoryStatVo.setIssueCount(0);
                    //存放key value（对象中的key值）
                    checkItemStatMap.put(item.getCheckItemKey(), houseQmIssueCategoryStatVo);
                }
                int count = checkItemStatMap.get(item.getCheckItemKey()).getIssueCount() + item.getCount();
                checkItemStatMap.get(item.getCheckItemKey()).setIssueCount(count);
                checkItemKeys.add(item.getCheckItemKey());
            }
        }
        removeDuplicate(categoryKeys);
        removeDuplicate(checkItemKeys);
        Map<String, Object> map = Maps.newHashMap();
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
        List<CategoryV3> categoryList = categoryService.searchCategoryByFatherKey(categoryRootKey);
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
     * @param areaIdList
     * @return com.longfor.longjian.houseqm.app.vo.AreaMapVo
     * @author hy
     * @date 2018/12/23 0023
     */
    public AreaMapVo createAreasMapByLeaveIds(List<Integer> areaIdList) {
        List<Area> areas = areaService.selectAreasByIdInAreaIds(areaIdList);
        List<Integer> totalIds = Lists.newArrayList();
        for (Area area : areas) {
            totalIds.add(area.getId());
            List<Integer> list = StringSplitToListUtil.strToInts(area.getPath(), "/");
            totalIds.addAll(list);
        }
        List<Integer> areaIds = CollectionUtil.removeDuplicate(totalIds);
        List<Area> areas1 = areaService.selectAreasByIdInAreaIds(areaIds);
        //Map<Integer, Area> areaMap = areas1.stream().collect(Collectors.toMap(Area::getId, a -> a, (k1, k2) -> k1));
        AreaMapVo areaMapVo = new AreaMapVo();
        Map<Integer, Area> map = areaMapVo.GetAreas();
        for (Area area : areas1) {
            map.put(area.getId(), area);
        }
        areaMapVo.setAreas(map);
        areaMapVo.setList(areas1);
        return areaMapVo;
    }

    /**
     * @param categorys
     * @return com.longfor.longjian.houseqm.app.vo.CategoryV3MapVo
     * @author hy
     * @date 2018/12/23 0023
     */
    private CategoryV3MapVo newCategoryMap(List<CategoryV3> categorys) {
        CategoryV3MapVo categoryV3MapVo = new CategoryV3MapVo();
        Map<String, CategoryV3> categoryV3Map = categorys.stream().collect(Collectors.toMap(CategoryV3::getKey, a -> a, (k1, k2) -> k1));
        categoryV3MapVo.setCategoryV3Map(categoryV3Map);
        return categoryV3MapVo;
    }

    /**
     * @param checkItemV3s
     * @return com.longfor.longjian.houseqm.app.vo.CheckItemV3MapVo
     * @author hy
     * @date 2018/12/23 0023
     */
    private CheckItemV3MapVo newCheckItemMap(List<CheckItemV3> checkItemV3s) {
        CheckItemV3MapVo checkItemV3MapVo = new CheckItemV3MapVo();
        Map<String, CheckItemV3> checkItemV3Map = checkItemV3s.stream().collect(Collectors.toMap(CheckItemV3::getKey, a -> a, (k1, k2) -> k1));
        checkItemV3MapVo.setCheckItemV3Map(checkItemV3Map);
        return checkItemV3MapVo;
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