package com.longfor.longjian.houseqm.app.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.longfor.longjian.common.consts.HouseQmCheckTaskIssueStatusEnum;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.service.HouseqmStaticService;
import com.longfor.longjian.houseqm.app.service.IHouseqmService;
import com.longfor.longjian.houseqm.app.service.IHouseqmStatisticService;
import com.longfor.longjian.houseqm.app.vo.*;
import com.longfor.longjian.houseqm.app.vo.houseqmstatistic.TaskIssueRepairListVo;
import com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp.*;
import com.longfor.longjian.houseqm.consts.*;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueDto;
import com.longfor.longjian.houseqm.dto.HouseQmCheckTaskIssueListDto;
import com.longfor.longjian.houseqm.dto.RepossessionStatusCompleteDailyCountDto;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAreaGroupModel;
import com.longfor.longjian.houseqm.po.IssueRepairCount;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.User;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.CollectionUtil;
import com.longfor.longjian.houseqm.util.DateUtil;
import com.longfor.longjian.houseqm.util.MathUtil;
import com.longfor.longjian.houseqm.util.StringSplitToListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

    private static final String TASK_ID = "taskId";
    private static final String PROJECT_ID = "projectId";
    private static final String AREA_ID = "areaId";
    private static final String STATUS = "status";
    private static final String ERROR = "error:";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    @Resource
    private HouseQmCheckTaskIssueService houseQmCheckTaskIssueService;
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private IHouseqmService iHouseqmService;
    @Resource
    private HouseqmStaticService houseqmStaticService;
    @Resource
    private AreaService areaService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private CheckItemService checkItemService;
    @Resource
    private FileResourceService fileResourceService;
    @Resource
    private CategoryV3Service categoryV3Service;
    @Resource
    private CheckItemV3Service checkItemV3Service;
    @Resource
    private RepossessionStatusService repossessionStatusService;
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

    /**
     * ????????????????????????????????????
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

    //??????
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public ProjectRepairerStatRspVo projectRepairerStat(int uid, Integer projectId, Integer taskId, String source, Integer statBegin, Integer statEnd, Integer timestamp) {
        String statBeginInt = "2000-01-01 00:00:00";
        String statEndStr = "2100-01-01 23:59:59";
        if (!statBegin.equals(0)) {
            statBeginInt = com.longfor.longjian.common.util.DateUtil.timestampToString(statBegin);
        }
        if (!statEnd.equals(0)) {
            statEndStr = com.longfor.longjian.common.util.DateUtil.timestampToString(statEnd);
        }
        List<Integer> categoryClsList = getCategoryClsListByProjectList(source);
        List<Integer> myTaskIds = Lists.newArrayList();
        List<UserInHouseQmCheckTask> taskList = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        for (UserInHouseQmCheckTask task : taskList) {
            if (!myTaskIds.contains(task.getTaskId())) {
                myTaskIds.add(task.getTaskId());
            }
        }
        List<Integer> taskIds = buildTaskIds(taskId, projectId, categoryClsList, myTaskIds);
        List<UserInHouseQmCheckTask> userList = userInHouseQmCheckTaskService.searchByTaskIdInAndRoleType(taskIds, HouseQmCheckTaskRoleTypeEnum.Repairer.getId());
        List<Integer> allUserIds = Lists.newArrayList();
        for (UserInHouseQmCheckTask user : userList) {
            if (!allUserIds.contains(user.getUserId())) {
                allUserIds.add(user.getUserId());
            }
        }
        Map<Integer, String> userMap = createUsersMap(allUserIds);
        Map<Integer, ApiHouseQmRepairerStatVo> statMap = Maps.newHashMap();
        for (Integer repairerId : allUserIds) {
            ApiHouseQmRepairerStatVo stat = new ApiHouseQmRepairerStatVo();
            stat.setUser_id(repairerId);
            stat.setReal_name(userMap.getOrDefault(repairerId, ""));
            statMap.put(repairerId, stat);
        }
        List<Integer> typs = Lists.newArrayList(HouseQmCheckTaskIssueTypeEnum.FindProblem.getId(), HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        List<Integer> status = Lists.newArrayList(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId(), HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId(), HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());

        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put(PROJECT_ID,projectId);
        paramMap.put("categoryClsList",categoryClsList);
        paramMap.put("statBeginInt",statBeginInt);
        paramMap.put("statEndStr",statEndStr);
        paramMap.put("typs",typs);
        paramMap.put(STATUS,status);
        paramMap.put(TASK_ID,taskId);

        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.searchByProjIdAndCategoryClsInAndRepairerIdAndClientCreateAtAndTypInAndStatusInAndTaskIdOrderByClientCreateAt(paramMap, myTaskIds);

        addCount(statMap, issueList);
        List<ApiHouseQmRepairerStatVo> items = Lists.newArrayList();
        for (Map.Entry<Integer, ApiHouseQmRepairerStatVo> stat : statMap.entrySet()) {
            items.add(stat.getValue());
        }
        ProjectRepairerStatRspVo result = new ProjectRepairerStatRspVo();
        result.setItems(items);
        return result;
    }

    private void addCount(Map<Integer, ApiHouseQmRepairerStatVo> statMap, List<HouseQmCheckTaskIssue> issueList) {
        for (HouseQmCheckTaskIssue item : issueList) {
            if (statMap.containsKey(item.getRepairerId())) {
                if (HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId().equals(item.getStatus())) {
                    statMap.get(item.getRepairerId()).setIssue_assigned_count(statMap.get(item.getRepairerId()).getIssue_assigned_count() + 1);
                } else if (HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId().equals(item.getStatus())) {
                    statMap.get(item.getRepairerId()).setIssue_repaired_count(statMap.get(item.getRepairerId()).getIssue_repaired_count() + 1);
                } else if (HouseQmCheckTaskIssueStatusEnum.CheckYes.getId().equals(item.getStatus())) {
                    statMap.get(item.getRepairerId()).setIssue_approveded_count(statMap.get(item.getRepairerId()).getIssue_approveded_count() + 1);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public ProjectCheckerStatRspVo projectCheckerStat(int uid, Integer projectId, Integer taskId, String source, Integer statBeginInt, Integer statEndInt, Integer timestamp) {
        String statBegin = "2000-01-01 00:00:00";
        String statEnd = "2100-01-01 23:59:59";
        if (!statBeginInt.equals(0)) {
            statBegin = com.longfor.longjian.common.util.DateUtil.timestampToString(statBeginInt);
        }
        if (!statEndInt.equals(0)) {
            statEnd = com.longfor.longjian.common.util.DateUtil.timestampToString(statEndInt);
        }
        List<Integer> myTaskIds = Lists.newArrayList();
        List<UserInHouseQmCheckTask> taskList = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        for (UserInHouseQmCheckTask task : taskList) {
            if (!myTaskIds.contains(task.getTaskId())) {
                myTaskIds.add(task.getTaskId());
            }
        }
        List<Integer> categoryClsList = getCategoryClsListByProjectList(source);
        List<Integer> taskIds = buildTaskIds(taskId, projectId, categoryClsList, myTaskIds);
        List<UserInHouseQmCheckTask> userList = userInHouseQmCheckTaskService.searchByTaskIdInAndRoleType(taskIds, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        List<Integer> userIds = Lists.newArrayList();
        for (UserInHouseQmCheckTask user : userList) {
            if (!userIds.contains(user.getUserId())) {
                userIds.add(user.getUserId());
            }
        }
        Map<Integer, String> userMap = createUsersMap(userIds);
        Map<Integer, ApiHouseQmCheckerStatVo> statMap = Maps.newHashMap();
        for (Integer sender_id : userIds) {
            ApiHouseQmCheckerStatVo stat = new ApiHouseQmCheckerStatVo();
            stat.setUser_id(sender_id);
            stat.setReal_name(userMap.getOrDefault(sender_id, ""));
            statMap.put(sender_id, stat);
        }
        List<Integer> typs = Lists.newArrayList(HouseQmCheckTaskIssueTypeEnum.Record.getId(), HouseQmCheckTaskIssueTypeEnum.FindProblem.getId(), HouseQmCheckTaskIssueTypeEnum.Difficult.getId());
        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.searchByProjIdAndCategoryClsInAndSenderIdAndClientCreateAtAndTypAndTaskIdOrderByClientCreateAt(projectId, categoryClsList, statBegin, statEnd, typs, taskId, myTaskIds);
        for (HouseQmCheckTaskIssue item : issueList) {
            if (statMap.containsKey(item.getSenderId())) {
                List<Integer> status = Lists.newArrayList(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId(), HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId(), HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId(), HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
                if (item.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.NoProblem.getId())) {
                    statMap.get(item.getSenderId()).setRecords_count(statMap.get(item.getSenderId()).getRecords_count() + 1);
                } else if (status.contains(item.getStatus())) {
                    statMap.get(item.getSenderId()).setIssue_count(statMap.get(item.getSenderId()).getIssue_count() + 1);
                    if (item.getStatus().equals(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId())) {
                        statMap.get(item.getSenderId()).setIssue_approveded_count(statMap.get(item.getSenderId()).getIssue_approveded_count() + 1);
                    }
                }
            }
        }
        ProjectCheckerStatRspVo result = new ProjectCheckerStatRspVo();
        List<ApiHouseQmCheckerStatVo> items = Lists.newArrayList();
        for (Map.Entry<Integer, ApiHouseQmCheckerStatVo> stat : statMap.entrySet()) {
            items.add(stat.getValue());
        }
        result.setItems(items);
        return result;
    }

    private List<Integer> buildTaskIds(Integer taskId, Integer projectId, List<Integer> categoryClsList, List<Integer> myTaskIds) {
        List<Integer> taskIds = Lists.newArrayList();
        if (taskId > 0) {
            taskIds.add(taskId);
        } else {
            List<Integer> tempTaskIds = Lists.newArrayList();
            List<HouseQmCheckTask> newTaskList = houseQmCheckTaskService.searchByProjectIdAndCategoryClsIn(projectId, categoryClsList);
            for (HouseQmCheckTask task : newTaskList) {
                if (!tempTaskIds.contains(task.getTaskId())) {
                    tempTaskIds.add(task.getTaskId());
                }
            }
            for (Integer id : tempTaskIds) {
                if (myTaskIds.contains(id)) {
                    taskIds.add(id);
                }
            }
        }
        return taskIds;
    }

    private Map<Integer, String> createUsersMap(List<Integer> userIds) {
        if (userIds.isEmpty()) return Maps.newHashMap();
        List<User> resUser = userService.searchByUserIdInAndNoDeleted(userIds);
        return resUser.stream().collect(Collectors.toMap(User::getUserId, User::getRealName));
    }

    @Override
    @SuppressWarnings("squid:S3776")
    public ProjectIssueStatRspVo projectIssueStat(int uid, Integer projectId, String source, Integer areaId, Integer timestamp) {
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
        List<UserInHouseQmCheckTask> userTaskList = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        List<Integer> taskIds = Lists.newArrayList();
        userTaskList.forEach(task -> {
            if (!taskIds.contains(task.getTaskId())) {
                taskIds.add(task.getTaskId());
            }
        });
        if (taskIds.isEmpty()) return result;
        List<Integer> categoryClsList = getCategoryClsListByProjectList(source);
        List<HouseQmCheckTask> taskList = null;
        if (areaId.equals(0)) {
            taskList = houseQmCheckTaskService.searchByProjectIdAndCategoryClsInAndTaskIdIn(projectId, categoryClsList, taskIds);
            item.setTask_count(taskList.size());
        } else {
            taskList = houseQmCheckTaskService.searchByProjectIdAndCategoryClsInAndTaskIdIn(projectId, categoryClsList, taskIds);
            List<Integer> areaIds = Lists.newArrayList();
            for (HouseQmCheckTask task : taskList) {
                areaIds.addAll(StringUtil.strToInts(task.getAreaIds(), ","));
            }
            List<Integer> newAreaIds = new HashSet<>(areaIds).stream().collect(Collectors.toList());
            List<Area> areaList = areaService.selectByAreaIds(newAreaIds);
            Map<Integer, String> areaMap = Maps.newHashMap();
            for (Area area : areaList) {
                areaMap.put(area.getId(), area.getPath() + area.getId() + "/");
            }
            for (HouseQmCheckTask task : taskList) {
                List<Integer> ids = StringUtil.strToInts(task.getAreaIds(), ",");
                if (checkRootAreaIntersectAreas(areaMap, areaId, ids)) {
                    item.setTask_count(item.getIssue_count() + 1);
                }
            }
        }
        String areaPath = "";
        if (areaId > 0) {
            Area area = areaService.selectById(areaId);
            areaPath =String.format("%s%d%s", area.getPath(),area.getId(),"/");
        }
        List<HouseQmCheckTaskIssue> issueList = houseQmCheckTaskIssueService.searchByProjectIdAndCategoryClsInAndTaskIdInAndAreaPathAndIdLike(projectId, categoryClsList, taskIds, areaPath);
        for (HouseQmCheckTaskIssue issue : issueList) {
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
            if (areaMap.containsKey(comma.get(i))&&areaMap.get(comma.get(i)).contains("/" + areaId + "/")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ProjectListRspVo projectList(int uid, String source, Integer timestamp) {
        ProjectListRspVo result = new ProjectListRspVo();
        List<UserInProject> userProjectList = iUserInProjectService.searchByUserId(uid);
        List<Integer> projectIds = Lists.newArrayList();
        for (UserInProject project : userProjectList) {
            if (!projectIds.contains(project.getProjectId())) {
                projectIds.add(project.getProjectId());
            }
        }
        if (projectIds.isEmpty()) return result;
        List<UserInHouseQmCheckTask> userTaskList = userInHouseQmCheckTaskService.searchByUserIdAndRoleType(uid, HouseQmCheckTaskRoleTypeEnum.Checker.getId());
        List<Integer> checkers = Lists.newArrayList();
        userTaskList.forEach(task -> {
            if (!checkers.contains(task.getTaskId())) {
                checkers.add(task.getTaskId());
            }
        });
        List<HouseQmCheckTask> taskList = houseQmCheckTaskService.searchByProjectIdInAndCategoryClsIn(projectIds, getCategoryClsListByProjectList(source));
        List<Integer> newProjectIds = Lists.newArrayList();
        for (HouseQmCheckTask task : taskList) {
            if (checkers.contains(task.getTaskId())) {
                newProjectIds.add(task.getProjectId());
            }
        }
        if (projectIds.isEmpty()) return result;
        List<Project> projectList = projectService.searchByProjectIdIn(newProjectIds);
        List<Integer> teamIds = Lists.newArrayList();
        for (Project project : projectList) {
            if (!teamIds.contains(project.getTeamId())) {
                teamIds.add(project.getTeamId());
            }
        }
        Map<Integer, String> teamMap = createTeamsMap(teamIds);
        List<ApiHouseqmStatisticProjectList> items = Lists.newArrayList();
        for (Project project : projectList) {
            ApiHouseqmStatisticProjectList item = new ApiHouseqmStatisticProjectList();
            item.setId(project.getId());
            item.setName(project.getName());
            item.setTeam_id(project.getTeamId());
            item.setTeam_name(teamMap.getOrDefault(project.getTeamId(), ""));
            items.add(item);
        }
        result.setItems(items);
        return result;
    }

    private Map<Integer, String> createTeamsMap(List<Integer> teamIds) {
        List<Team> resTeam = teamService.searchByTeamIdIn(teamIds);
        return resTeam.stream().collect(Collectors.toMap(Team::getTeamId, Team::getTeamName));
    }

    //get_categoryClsList(source)
    private List<Integer> getCategoryClsListByProjectList(String source) {
        List<Integer> categoryClsList = Lists.newArrayList();
        if (source.equals("gcgl")) {
            categoryClsList.add(CategoryClsTypeEnum.RCJC.getId());
            categoryClsList.add(CategoryClsTypeEnum.YDJC.getId());
            categoryClsList.add(CategoryClsTypeEnum.JDJC.getId());
            categoryClsList.add(CategoryClsTypeEnum.YB.getId());
            categoryClsList.add(CategoryClsTypeEnum.FBFX.getId());
        } else {
            categoryClsList.add(CategoryClsTypeEnum.FHYS.getId());
            categoryClsList.add(CategoryClsTypeEnum.RHYF.getId());
        }
        return categoryClsList;
    }


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
            //?????????????????????
            if (e != null) {
                switch (e) {
                    case NoteNoAssign://??????????????????
                        result.setIssue_recorded_count(result.getIssue_recorded_count() + issue.getPosX());
                        break;
                    case AssignNoReform://??????????????????
                        result.setIssue_assigned_count(result.getIssue_assigned_count() + issue.getPosX());
                        break;
                    case ReformNoCheck://??????????????????
                        result.setIssue_repaired_count(result.getIssue_repaired_count() + issue.getPosX());
                        break;
                    case CheckYes://?????????
                        result.setIssue_approveded_count(result.getIssue_approveded_count() + issue.getPosX());
                        break;
                    default:
                        break;
                }
                //?????????????????????
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
                e = null;
            }
        }
        return result;
    }

    public TaskStatVo.HouseStatVo getHouseQmCheckTaskHouseStatByTaskId(Integer projectId, Integer taskId, Integer areaId) {
        try {
            TaskStatVo.HouseStatVo houseStatVo = new TaskStatVo().new HouseStatVo();
            houseStatVo.setHas_issue_count(0);
            houseStatVo.setRepaired_count(0);
            houseStatVo.setApproved_count(0);
            houseStatVo.setChecked_count(0);
            houseStatVo.setHouse_count(0);
            //????????????
            HouseQmCheckTask task = houseQmCheckTaskService.selectByProjectIdAndTaskId(projectId, taskId);
            if (task == null) throw new LjBaseRuntimeException(500, "???????????????");
            // ???????????????????????????????????????????????????
            List<Integer> areaIds = StringUtil.strToInts(task.getAreaIds(), ",");
            List<Integer> areaTypes = StringUtil.strToInts(task.getAreaTypes(), ",");
            if (areaIds.isEmpty() || areaTypes.isEmpty()) {
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

            //???????????????
            // ?????????????????????????????????????????????????????????????????????????????????
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
            log.error(ERROR, e.getMessage());
            return null;
        }
    }

    public TaskRepairStatVo searchIssueRepairStatisticByProjTaskIdAreaIdBeginOnEndOn(Integer projectId, Integer taskId, Integer areaId, Date beginOn, Date endOn) {
        List<IssueRepairCount> issueCounts = null;
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        HashMap<String, Object> condiMap = Maps.newHashMap();
        condiMap.put(PROJECT_ID, projectId);
        condiMap.put(TASK_ID, taskId);
        if (areaId > 0) {
            condiMap.put(AREA_ID, "%/" + areaId + "/%");
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
        condiMap.put(STATUS, HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());

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

    @Override
    public HouseqmStatisticCategoryIssueListRspMsgVo taskIssueRepairList(TaskIssueRepairListVo taskIssueRepairListVo) {
        Integer projectId=taskIssueRepairListVo.getProjectId();
        Integer taskId=taskIssueRepairListVo.getTaskId();
        Integer areaId=taskIssueRepairListVo.getAreaId();
        Integer beginOn=taskIssueRepairListVo.getBeginOn();
        Integer endOn=taskIssueRepairListVo.getEndOn();
        Integer planStatus=taskIssueRepairListVo.getPlanStatus();
        String source=taskIssueRepairListVo.getSource();
        Integer page=taskIssueRepairListVo.getPage();
        Integer pageSize=taskIssueRepairListVo.getPageSize();
        Date beginOn1 = DateUtil.timeStampToDate(0, YYYY_MM_DD);
        Date endOn1 = DateUtil.timeStampToDate(0, YYYY_MM_DD);
        if (beginOn != null && beginOn > 0) {
            beginOn1 = DateUtil.timeStampToDate(beginOn, YYYY_MM_DD);
        }
        if (endOn != null && endOn > 0) {
            Date date = DateUtil.timeStampToDate(endOn, YYYY_MM_DD);
            endOn1 = DateUtil.dateAddDay(date, 1);
        }
        if (planStatus <= 0 || planStatus > 5) throw new LjBaseRuntimeException(500, "invalid plan_status.");
        List<Integer> categoryClsList = getCategoryClsList(source);
        // ??????deleted_at is null
        Map<String, Object> paramMap = Maps.newHashMap();
        //beginOn1, endOn1, page, pageSize
        paramMap.put("beginOn1",beginOn1);
        paramMap.put("endOn1",endOn1);
        paramMap.put("page",page);
        paramMap.put("pageSize",pageSize);

        HouseQmCheckTaskIssueListDto issueListVo = houseQmCheckTaskIssueService.selectCountByProjectIdAndCategoryClsAndTypeAndStatusInAndDongTai2(projectId, taskId, categoryClsList, areaId, planStatus, paramMap);
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
            attachmentMd5List.addAll(StringUtil.strToStrs(item.getAttachmentMd5List(), ","));
        });
        //??????????????????
        List<Integer> areaIdList = CollectionUtil.removeDuplicate(areaIds);
        List<String> categoryKeyList = CollectionUtil.removeDuplicate(categoryKeys);
        List<String> checkItemKeyList = CollectionUtil.removeDuplicate(checkItemKeys);

        //??????file_resource service ?????? files
        List<FileResource> files = fileResourceService.searchFileResourceByFileMd5InAndNoDeleted(attachmentMd5List);
        HashMap<String, String> fileMap = Maps.newHashMap();
        files.forEach(item ->
            fileMap.put(item.getFileMd5(), item.getStoreKey())
        );

        AreaMapVo areaMap = createAreasMapByLeaveIds(areaIdList);
        List<CategoryV3> categorys = categoryV3Service.searchCategoryV3ByKeyInAndNoDeleted(categoryKeyList);

        categorys.forEach(item -> {
            List<String> list = StringSplitToListUtil.removeStartAndEndStrAndSplit(item.getPath(), "/", "/");
            list.add(item.getKey());
            categoryKeyList.addAll(list);
        });
        //?????? categoryKeyList
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

            List<String> list = StringUtil.strToStrs(item.getAttachmentMd5List(), ",");
            list.forEach(fm -> {
                if (fileMap.containsKey(fm)) {
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

    @Override
    public IssueRepairStatisticVo projectIssueRepair(Integer projectId, String source, Integer areaId, Integer beginOn, Integer endOn, Integer timestamp) {
        Date beginOn1 = DateUtil.timeStampToDate(beginOn, YYYY_MM_DD);
        Date endOn1 = DateUtil.timeStampToDate(endOn, YYYY_MM_DD);
        if (beginOn > 0) {
            beginOn1 = DateUtil.timeStampToDate(beginOn, YYYY_MM_DD);
        }
        if (endOn > 0) {
            Calendar c = Calendar.getInstance();
            Date date = DateUtil.timeStampToDate(endOn, YYYY_MM_DD);
            c.setTime(date);
            c.add(Calendar.DAY_OF_MONTH, 1);
            endOn1 = DateUtil.timeStampToDate((int) (c.getTime().getTime() / 1000), YYYY_MM_DD);
        }
        List<Integer> categoryClsList = getCategoryClsList(source);
        return searchIssueRepairStatisticByProjCategoryClsInAreaIdBeginOnEndOn(projectId, categoryClsList, areaId, beginOn1, endOn1);
    }

    @Override
    public HouseQmCheckTaskHouseStatInfoVo getHouseQmHouseQmCheckTaskHouseStatByTaskId(Integer prodectId, Integer taskId, Integer areaId) {
        ArrayList<Integer> taskIds = Lists.newArrayList();
        taskIds.add(taskId);
        // ????????????????????????????????????
        CheckTaskHouseStatInfoVo normalStat = houseqmStaticService.getHouseQmCheckTaskHouseStatByTaskId(prodectId, taskId, areaId);
        // ????????????????????????????????????
        RepossessionTasksStatusInfoVo repossessionInfo = houseqmStaticService.getRepossessionTasksStatusInfo(prodectId, taskIds, areaId);
        HouseQmCheckTaskHouseStatInfoVo result = new HouseQmCheckTaskHouseStatInfoVo();
        result.setCheckedCount(repossessionInfo.getCheckedCount());
        result.setRepairConfirmCount(repossessionInfo.getRepairConfirmCount());
        result.setAcceptHasIssueCount(repossessionInfo.getAcceptHasIssueCount());
        result.setAcceptNoIssueCount(repossessionInfo.getAcceptNoIssueCount());
        result.setOnlyWatchCount(repossessionInfo.getOnlyWatch());
        result.setRejectCount(repossessionInfo.getRejectCount());
        result.setAcceptApprovedCount(repossessionInfo.getAcceptApprovedCount());
        result.setHouseCount(normalStat==null?0:normalStat.getHouseCount());
        result.setHasIssueCount(normalStat==null?0:normalStat.getHasIssueCount());
        result.setRepairedCount(normalStat==null?0:normalStat.getRepairedCount());
        result.setApprovedCount(normalStat==null?0:normalStat.getApprovedCount());

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
    @SuppressWarnings("squid:S3776")
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
                List<Integer> ids = StringUtil.strToInts(path, "/");
                if (CollectionUtils.isNotEmpty(ids)) {
                    hasIssueAreaId.put(ids.get(ids.size() - 1), true);
                }
            }

            List<Integer> statuses = Lists.newArrayList();
            statuses.add(HouseQmCheckTaskIssueStatusEnum.NoteNoAssign.getId());
            statuses.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
            statuses.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());

            // ?????????????????????????????????
            houseqmStaticService.getHasIssueTaskCheckedAreaPathListByTaskId(taskId, true, statuses, areaId);
            HashMap<Integer, Boolean> hasIssueNoApprovedAreaId = Maps.newHashMap();
            for (String path : hasIssuePaths) {
                List<Integer> ids = StringUtil.strToInts(path, "/");
                if (CollectionUtils.isNotEmpty(ids)) {
                    hasIssueNoApprovedAreaId.put(ids.get(ids.size() - 1), true);
                }
            }
            int checkedCount = 0;
            HashMap<Integer, Boolean> rAreaId = Maps.newHashMap();
            for (RepossessionStatus item : items) {
                // ??????
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
                    case Accept:
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


                        break;
                    case RejectAccept:
                        checkedCount += 1;
                        info.setRejectCount(info.getRejectCount() + 1);
                        info.setUnacceptCount(info.getUnacceptCount() + 1);

                        break;
                    case OnlyCheck:
                        checkedCount += 1;
                        info.setOnlyWatch(info.getOnlyWatch() + 1);
                        info.setUnacceptCount(info.getUnacceptCount() + 1);

                        break;
                    case None:
                        break;
                    default:
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
     * ??????-????????????-??????????????????
     * @Author hy
     * @Description
     * @Date 20:47 2019/1/8
     * @Param [projectId, taskIds, beginOn, endOn, page, pageSizeInt]
     * @return com.longfor.longjian.houseqm.app.vo.StatHouseqmCompleteDailyRspVo
     **/
    @Override
    public StatHouseqmCompleteDailyRspVo searchRepossessionStatusCompleteDaily(Integer projectId, List<Integer> taskIds, int beginOn, int endOn, Integer page, Integer pageSizeInt) {
        // ???????????????
        List<RepossessionStatusCompleteDailyCountDto> list = null;
        Map<String, Object> condi = Maps.newHashMap();
        condi.put(TASK_ID, taskIds);
        condi.put(STATUS, RepossessionStatusEnum.Accept.getId());
        Date beginOns=null;
        Date endOns = null;
        if (beginOn > 0) {
            beginOns = DateUtil.timeStampToDate(beginOn, "yyyy-MM-dd hh:mm:ss");
            condi.put("status_client_update_atgte", beginOns);
        }
        if (endOn > 0) {
            endOns = DateUtil.timeStampToDate(endOn, "yyyy-MM-dd hh:mm:ss");
            condi.put("status_client_update_atlte", endOns);
        }
        RepossessionStatusCompleteDailyCountDto bean = repossessionStatusService.searchByTaskIdInAndStatusAndNoDeletedOrStatusClientUpdateAt(condi);
        int total = bean.getCount();
        int offset = (page - 1) * pageSizeInt;
        condi.put("page_size", pageSizeInt);
        condi.put("offset", offset);
        list = repossessionStatusService.searchByTaskIdInAndStatusAndNoDeletedGroupByDateOrderByDateByPage(condi);

        StatHouseqmCompleteDailyRspVo result = new StatHouseqmCompleteDailyRspVo();
        List<HouseQmHouseQmStatCompleteDailyRspVo> items = list.stream().map(repossessionStatusCompleteDailyCountDto -> {
            HouseQmHouseQmStatCompleteDailyRspVo hqs = new HouseQmHouseQmStatCompleteDailyRspVo();
            hqs.setDate(repossessionStatusCompleteDailyCountDto.getDate());
            hqs.setCount(repossessionStatusCompleteDailyCountDto.getCount());
            return hqs;
        }).collect(Collectors.toList());
        result.setItems(items);
        result.setTotal(total);
        return result;
    }

    private IssueRepairStatisticVo searchIssueRepairStatisticByProjCategoryClsInAreaIdBeginOnEndOn(Integer projectId, List<Integer> categoryClsList, Integer areaId, Date beginOn1, Date endOn1) {
        String nowStr = DateUtil.getNowTimeStr("yyyy-MM-dd HH:mm:ss");
        HashMap<String, Object> condiMap = new HashMap<>();
        condiMap.put(PROJECT_ID, projectId);
        if (CollectionUtils.isNotEmpty(categoryClsList))condiMap.put("categoryClsList", categoryClsList);

        if (areaId > 0) condiMap.put("areaPathAndId", "%/" + areaId + "/%");
        if (beginOn1.getTime() / 1000 > 0) condiMap.put("clientCreateAtGte", beginOn1);
        if (endOn1.getTime() / 1000 > 0) condiMap.put("clientCreateAtLte", endOn1);
        ArrayList<Integer> typs = Lists.newArrayList();
        typs.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        typs.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        condiMap.put("typ", typs);
        condiMap.put("now", nowStr);
        condiMap.put(STATUS, HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
        ArrayList<Integer> statusIn = Lists.newArrayList();
        statusIn.add(HouseQmCheckTaskIssueStatusEnum.AssignNoReform.getId());
        statusIn.add(HouseQmCheckTaskIssueStatusEnum.ReformNoCheck.getId());
        statusIn.add(HouseQmCheckTaskIssueStatusEnum.CheckYes.getId());
        condiMap.put("statusIn", statusIn);
        List<IssueRepairCount> issueCounts = houseQmCheckTaskIssueService.selectIssueRepairCountByProjectIdAndCategoryClsAndTypInAndStatusInAndNoDeletedAndDongTai(condiMap);
        IssueRepairCount ic = issueCounts.get(0);
        IssueRepairStatisticVo item = new IssueRepairStatisticVo();
        if (ic.getTotal() == 0) {
            // ???????????????0????????????total???0???????????????????????????0%
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
     * @param module
     * @return java.util.List<java.lang.Integer>
     * @author hy
     * @date 2018/12/22 0022
     */
    private List<Integer> getCategoryClsList(String module) {
        String source = null;
        if ("gcgl".equals(module)) { //??????app ???
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
        return calculateIssueCount(issueStatVoList);

    }

    @Override
    public HouseQmCheckTaskIssueVoRsp searchHouseQmCheckTaskIssueOnlineInfoByProjCategoryKeyAreaIdPaged(Integer projectId, String categoryKey, Integer areaId, Integer page, Integer pageSize) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        HashMap<String, Object> condiMap = Maps.newHashMap();
        condiMap.put(PROJECT_ID, projectId);
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
            condiMap.put(AREA_ID, areaId);
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
            log.error(ERROR, e.getMessage());
        }
        return houseQmCheckTaskIssueVo;
    }

    private List<HouseQmCheckTaskIssueOnlineInfoVo> formatHouseQmCheckTaskIssueOnlineInfo(List<HouseQmCheckTaskIssue> issueList) {
        List<HouseQmCheckTaskIssueOnlineInfoVo> infos = convertHouseQmCheckTaskIssueToOnlineInfo(issueList);
        try {
            infos = fillHouseQmCheckTaskIssueOnlineInfoAreaInfo(infos);
            infos = fillHouseQmCheckTaskIssueOnlineInfoCategoryInfo(infos);
            infos = fillHouseQmCheckTaskIssueOnlineInfoFileInfo(infos);
        } catch (Exception e) {
            throw new LjBaseRuntimeException(-1,ERROR + e);
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
        List<FileResource> fileList = fileResourceService.searchByMd5In(attachmentMd5List);
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

            String[] categoryPathAndKeys = getPathSlice(infos.get(i).getCategoryPathAndKey());
            for (int j = 0; j < categoryPathAndKeys.length; j++) {
                categoryKeys.add(categoryPathAndKeys[j]);
            }
            String[] checkItemPathAndKey = getPathSlice(infos.get(i).getCheckItemPathAndKey());
            for (int j = 0; j < checkItemPathAndKey.length; j++) {
                checkItemKeys.add(checkItemPathAndKey[j]);
            }
        }
        removeDuplicate(categoryKeys);
        removeDuplicate(checkItemKeys);
        List<CategoryV3> categoryList = categoryService.searchCategoryByKeyIn(categoryKeys);
        CategoryMapVo categoryMap = new CategoryMapVo().NewCategoryMap(categoryList);

        List<CheckItem> checkItemList = checkItemService.searchCheckItemByKeyIn(checkItemKeys);
        CheckItemMapVo checkItemMap = new CheckItemMapVo().NewCategoryMap(checkItemList);
        for (int i = 0; i < infos.size(); i++) {

            infos.get(i).setCategoryPathName(categoryMap.getFullNamesByKey(infos.get(i).getCategoryKey()));
            infos.get(i).setCategoryName(categoryMap.getNameByKey(infos.get(i).getCategoryKey()));
            infos.get(i).setCheckItemPathName(checkItemMap.getFullNamesByKey(infos.get(i).getCheckItemKey()));
            infos.get(i).setCheckItemName(checkItemMap.getNameByKey(infos.get(i).getCheckItemKey()));
        }

        return infos;
    }

    private String[] getPathSlice(String path) {
        String newStr = path.substring(1, path.length());
        return newStr.split("/");
    }

    private List<HouseQmCheckTaskIssueOnlineInfoVo> fillHouseQmCheckTaskIssueOnlineInfoAreaInfo(List<HouseQmCheckTaskIssueOnlineInfoVo> infos) {
        Set<Integer> areaIds = Sets.newHashSet();
        for (int i = 0; i < infos.size(); i++) {
            areaIds.add(infos.get(i).getAreaId());
        }
        AreaMapVo map =createAreasMapByLeaveIds(areaIds);
        for (int i = 0; i < infos.size(); i++) {
            infos.get(i).setAreaPathName(map.getPathNames(infos.get(i).getAreaId()));
            infos.get(i).setAreaName(map.getName(infos.get(i).getAreaId()));
        }

        return infos;
    }

    private AreaMapVo createAreasMapByLeaveIds(Set<Integer> areaIds) {
        List<Area> areaList = selectAllByLeaveIds(areaIds);
        return createAreasMapByAreaList(areaList);

    }

    public AreaMapVo createAreasMapByAreaList(List<Area> areaList) {
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

    private List<Area> selectAllByLeaveIds(Set<Integer> areaIds) {
        List<Integer> list = areaIds.stream().collect(Collectors.toList());
        List<Area> areaList = areaService.selectByAreaIds(list);
        ArrayList<Integer> totalIds = Lists.newArrayList();
        for (int i = 0; i < areaList.size(); i++) {
            totalIds.add(areaList.get(i).getId());

            List<Integer> sids = splitToIds(areaList.get(i).getPath(), "/");
            totalIds.addAll(sids);

        }
        List<Integer> lists = removeDuplicate(totalIds);

        return areaService.selectByAreaIds(lists);
    }

    /**
     * ??????????????????????????????Integer??????
     *
     * @param idstr
     * @param sep
     * @return
     */
    private List<Integer> splitToIds(String idstr, String sep) {
        List<Integer> result = Lists.newArrayList();
        String[] ids = idstr.split(sep);
        for (String id : ids) {
            id = id.trim();
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
    @SuppressWarnings("squid:S3776")
    public List<HouseQmIssueCategoryStatVo> calculateIssueCount
            (ArrayList<SimpleHouseQmCheckTaskIssueStatVo> issueStatVoList) {
        List<HouseQmIssueCategoryStatVo> r = Lists.newArrayList();

        Map<String, Object> map = groupIssueStatByCategoryAndCheckItem(issueStatVoList);

        // ??????????????????
        Map<String, CategoryV3> categoryMap = getCategoryMapByCategoryKeys((List<String>) map.get("categoryKeys"));
        Map<String, CheckItem> checkItemMap = getCheckItemMapByCheckItemKeys((List<String>) map.get("checkItemKeys"));

        //???????????? ??????????????????????????????????????????????????????
        // ????????????????????????????????????????????????????????????
        // ????????????????????????????????????????????????????????????????????????????????????
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
        for (Iterator<Map.Entry<String, HouseQmIssueCategoryStatVo>> iterator = categoryStatMap.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, HouseQmIssueCategoryStatVo> categoryStat = iterator.next();
            // ???????????????????????????????????????????????????????????????
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????V???????????????????????????????????????????????????????????????

            CategoryV3 cate = categoryMap.get(categoryStat.getValue().getKey());
            if (cate != null) {
                boolean isRoot = false;
                String str = cate.getPath();
                int level = StringSplitToListUtil.count(str, "/");
                //??????????????????
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
                // ????????????????????????????????????FatherKey?????????????????????????????????
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
        return r;
    }

    private Map<String, CheckItem> getCheckItemMapByCheckItemKeys(List<String> keys) {
        Map<String, CheckItem> map = new HashMap<>();
        if (keys.isEmpty()) return map;
        List<CheckItem> resCheckItems = checkItemService.searchCheckItemByKeyIn(keys);
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
                //??????key????????????
                if (!categoryStatMap.containsKey(key)) {
                    HouseQmIssueCategoryStatVo houseQmIssueCategoryStatVo = new HouseQmIssueCategoryStatVo();

                    houseQmIssueCategoryStatVo.setKey(key);
                    houseQmIssueCategoryStatVo.setIssueCount(0);
                    //??????key value???????????????key??????
                    categoryStatMap.put(key, houseQmIssueCategoryStatVo);
                }
                //categoryStatMap[key].IssueCount += item.Count
                int count = categoryStatMap.get(key).getIssueCount() + item.getCount();
                categoryStatMap.get(key).setIssueCount(count);
            }
            categoryKeys.addAll(categoryPathKeys);
            //???CheckItemKey???????????????0
            if (item.getCheckItemKey().length() > 0) {
                //??????key???????????????map
                if (!checkItemStatMap.containsKey(item.getCheckItemKey())) {
                    HouseQmIssueCategoryStatVo houseQmIssueCategoryStatVo = new HouseQmIssueCategoryStatVo();
                    houseQmIssueCategoryStatVo.setKey(item.getCheckItemKey());
                    houseQmIssueCategoryStatVo.setIssueCount(0);
                    //??????key value???????????????key??????
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

    // ?????????????????????????????????????????????????????????
    public boolean isCategoryStatLevelThree(String categoryRootKey) {
        List<CategoryV3> categoryList = categoryService.searchCategoryByFatherKey(categoryRootKey);
        try {
            if (categoryList.size() > 2) {
                return false;
            }
        } catch (Exception e) {
            log.error(ERROR + e);
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
            List<Integer> list = StringUtil.strToInts(area.getPath(), "/");
            totalIds.addAll(list);
        }
        List<Integer> areaIds = CollectionUtil.removeDuplicate(totalIds);
        List<Area> areas1 = areaService.selectAreasByIdInAreaIds(areaIds);

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
    // ????????????id?????????RootId?????????????????????????????????map
    private Map<Integer, IssueMinStatusVo> getIssueMinStatusMapByTaskIdAndAreaId(Integer taskId, Integer
            areaId, Boolean onlyIssue) {
        List<Integer> types = Lists.newArrayList();
        types.add(HouseQmCheckTaskIssueEnum.FindProblem.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        types.add(HouseQmCheckTaskIssueEnum.Difficult.getId());
        List<HouseQmCheckTaskIssueAreaGroupModel> result =null;


        result = houseQmCheckTaskIssueService.selectByTaskIdAndTyeInAndAreaPathAndIdLike(onlyIssue, taskId, types, areaId);


        Map<Integer, IssueMinStatusVo> maps = Maps.newHashMap();
        for (HouseQmCheckTaskIssueAreaGroupModel area : result) {
            List<Integer> aIds = StringUtil.strToInts(area.getAreaPath(), "/");
            if (CollectionUtils.isNotEmpty(aIds)) {
                IssueMinStatusVo minStatus = new IssueMinStatusVo();
                minStatus.setCount(area.getExtendCol());
                minStatus.setMinStatus(area.getStatus());
                maps.put(aIds.get(aIds.size() - 1), minStatus);
            }
        }
        return maps;
    }

}