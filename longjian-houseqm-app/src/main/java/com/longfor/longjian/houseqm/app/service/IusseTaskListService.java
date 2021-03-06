package com.longfor.longjian.houseqm.app.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.util.StringUtil;
import com.longfor.longjian.houseqm.app.vo.ApiMineMsg;
import com.longfor.longjian.houseqm.app.vo.ApiStatHouseqmMeterSettingMsgVo;
import com.longfor.longjian.houseqm.app.vo.HouseQmCheckTaskSimpleRspVo;
import com.longfor.longjian.houseqm.domain.internalservice.*;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.po.zj2db.*;
import com.longfor.longjian.houseqm.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Dongshun on 2018/12/13.
 */
@Repository
@Service
@Slf4j
public class IusseTaskListService {
    @Resource
    private HouseQmCheckTaskService houseQmCheckTaskService;
    @Resource
    private RepossessionMeterSettingService repossessionMeterSettingService;
    @Resource
    private UserInTeamRoleService userInTeamRoleService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserInProjectService userInProjectService;
    @Resource
    private ProjectService projectService;

    public List<HouseQmCheckTaskSimpleRspVo> selectByProjectIdAndCategoryCls(Integer projectId, Integer categoryCls) {
        List<HouseQmCheckTask> houseQmCheckTaskList = houseQmCheckTaskService.selectByProjectIdAndCategoryCls(projectId, categoryCls);
        ArrayList<HouseQmCheckTaskSimpleRspVo> hQmCheckTaskList = new ArrayList<>();
        for (HouseQmCheckTask houseQmCheckTask : houseQmCheckTaskList) {
            HouseQmCheckTaskSimpleRspVo rspVo = new HouseQmCheckTaskSimpleRspVo();
            rspVo.setProject_id(houseQmCheckTask.getProjectId());
            rspVo.setTask_id(houseQmCheckTask.getTaskId());
            List<Integer> split = StringUtil.strToInts(houseQmCheckTask.getAreaTypes(), ",");
            rspVo.setArea_types(split);
            rspVo.setName(houseQmCheckTask.getName());
            rspVo.setPlan_end_on(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getPlanEndOn()));
            rspVo.setCreate_at(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getCreateAt()));
            rspVo.setPlan_begin_on(DateUtil.datetimeToTimeStamp(houseQmCheckTask.getPlanBeginOn()));
            hQmCheckTaskList.add(rspVo);
        }
        return hQmCheckTaskList;
    }

    public List<ApiStatHouseqmMeterSettingMsgVo> getAcceptanceitemsSetting(String projectIds, Integer timestamp) {
        List<Integer> projectIdList = StringUtil.strToInts(projectIds, ",");
        List<ApiStatHouseqmMeterSettingMsgVo> acceptanceItems = Lists.newArrayList();
        for (int i = 0; i < projectIdList.size(); i++) {
            List<RepossessionMeterSetting> itemlist = repossessionMeterSettingService.selectByProjectId(projectIdList.get(i));
            if (CollectionUtils.isEmpty(itemlist)) {
                ApiStatHouseqmMeterSettingMsgVo vo = new ApiStatHouseqmMeterSettingMsgVo();
                vo.setProject_id(projectIdList.get(i));
                vo.setItem_id(1);
                vo.setItem_name("??????(??????)");
                vo.setInput_type(0);
                vo.setSub_setting("");
                acceptanceItems.add(vo);
                ApiStatHouseqmMeterSettingMsgVo vos = new ApiStatHouseqmMeterSettingMsgVo();
                vos.setProject_id(projectIdList.get(i));
                vos.setItem_id(2);
                vos.setItem_name("??????(???)");
                vos.setInput_type(0);
                vos.setSub_setting("");
                acceptanceItems.add(vos);
                RepossessionMeterSetting setting = new RepossessionMeterSetting();
                setting.setProjectId(projectIdList.get(i));
                setting.setItemId(1);
                setting.setItemName("??????(??????)");
                setting.setInputType(0);
                setting.setSubSetting("");
                setting.setLastUpdate("system");
                setting.setCreateAt(new Date());
                setting.setUpdateAt(new Date());
                repossessionMeterSettingService.add(setting);
                RepossessionMeterSetting settings = new RepossessionMeterSetting();
                settings.setProjectId(projectIdList.get(i));
                settings.setItemId(2);
                settings.setItemName("??????(???)");
                settings.setInputType(0);
                settings.setSubSetting("");
                settings.setLastUpdate("system");
                settings.setCreateAt(new Date());
                settings.setUpdateAt(new Date());
                repossessionMeterSettingService.add(settings);

            } else {
                for (int j = 0; j < itemlist.size(); j++) {
                    int stamp = DateUtil.datetimeToTimeStamp(itemlist.get(j).getCreateAt());
                    if (stamp >= timestamp) {
                        ApiStatHouseqmMeterSettingMsgVo vo = new ApiStatHouseqmMeterSettingMsgVo();
                        vo.setItem_id(itemlist.get(j).getItemId());
                        vo.setItem_name(itemlist.get(j).getItemName());
                        vo.setInput_type(itemlist.get(j).getInputType());
                        vo.setSub_setting(itemlist.get(j).getSubSetting());
                        acceptanceItems.add(vo);
                    }
                }

            }
        }


        return acceptanceItems;
    }
    @SuppressWarnings("squid:S3776")
    public ApiMineMsg teamsAndProjects(Integer uid, String categorys) {
        ArrayList<ApiMineMsg.ApiMineTeamsMsg> teams = Lists.newArrayList();
        ArrayList<ApiMineMsg.ApiMineProjectsMsg> projects = Lists.newArrayList();
        List<Integer> categorylist = StringUtil.strToInts(categorys, ",");
        if (CollectionUtils.isEmpty(categorylist)) {
            log.info("categorysis empty, categorys=%" + categorys + "");
            throw new LjBaseRuntimeException(117, "??????????????????");
        }
        List<UserInTeamRole> userlist = userInTeamRoleService.selectByUserIdNotDel(uid);
        ArrayList<Integer> teamIds = Lists.newArrayList();
        for (UserInTeamRole userInTeamRole : userlist) {
            if (!teamIds.contains(userInTeamRole.getTeamId())) {
                teamIds.add(userInTeamRole.getTeamId());
            }
        }
        List<Team> teamlist = teamService.selectByTeamIdsNotDel(teamIds);
        for (Team team : teamlist) {
            ApiMineMsg.ApiMineTeamsMsg apiMineTeamsMsg = new ApiMineMsg().new ApiMineTeamsMsg();
            apiMineTeamsMsg.setId(team.getTeamId());
            apiMineTeamsMsg.setTeam_name(team.getTeamName());
            apiMineTeamsMsg.setParent_team_id(team.getParentTeamId());
            apiMineTeamsMsg.setUpdate_at(DateUtil.datetimeToTimeStamp(team.getUpdateAt()));
            teams.add(apiMineTeamsMsg);
        }
        ArrayList<Integer> parentIds = Lists.newArrayList();
        for (Team team : teamlist) {
            if (team.getTeamId() > 0) {
                parentIds.add(team.getTeamId());
            }
        }
        Map<Integer, Team> parentMap = createTeamsMap(parentIds);
        for (Map.Entry<Integer, Team> entry : parentMap.entrySet()) {
            for (Team team : teamlist) {
                if (!entry.getKey().equals(team.getTeamId())) {
                    ApiMineMsg.ApiMineTeamsMsg apiMineTeamsMsg = new ApiMineMsg().new ApiMineTeamsMsg();
                    apiMineTeamsMsg.setId(entry.getValue().getTeamId());
                    apiMineTeamsMsg.setTeam_name(entry.getValue().getTeamName());
                    apiMineTeamsMsg.setParent_team_id(entry.getValue().getParentTeamId());
                    apiMineTeamsMsg.setUpdate_at(DateUtil.datetimeToTimeStamp(entry.getValue().getUpdateAt()));
                    if (datetimeZero(entry.getValue().getDeleteAt())) {
                        apiMineTeamsMsg.setDelete_at(DateUtil.datetimeToTimeStamp(entry.getValue().getDeleteAt()));
                    }
                    teams.add(apiMineTeamsMsg);

                }
            }
        }
        //# ?????????????????????????????????
        List<UserInProject> projectList = userInProjectService.selectByUserIdNotDel(uid);
        ArrayList<Integer> projectIds = Lists.newArrayList();
        projectList.forEach(item -> {
            if (!projectIds.contains(item.getProjectId())) {
                projectIds.add(item.getProjectId());
            }
        });
        //  # ??????????????????????????????????????????
        List<HouseQmCheckTask> tasklist = houseQmCheckTaskService.selectByProjectIdsAndCategoryClsNotDel(projectIds, categorylist);
        ArrayList<Integer> projectIdsList = Lists.newArrayList();
        tasklist.forEach(item ->
            projectIdsList.add(item.getProjectId())
        );
        ArrayList<Integer> expTeamIds = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(projectIdsList)) {
            List<Project> projectlist = projectService.selectByIdNotDel(projectIdsList);
            projectlist.forEach(item -> {
                ApiMineMsg.ApiMineProjectsMsg project = new ApiMineMsg().new ApiMineProjectsMsg();
                project.setId(item.getId());
                project.setName(item.getName());
                project.setTeam_id(item.getTeamId());
                project.setUpdate_at(DateUtil.datetimeToTimeStamp(item.getUpdateAt()));
                projects.add(project);
                if (!teamIds.contains(item.getTeamId())) {
                    expTeamIds.add(item.getTeamId());
                }
            });
        }
        if (CollectionUtils.isNotEmpty(expTeamIds)) {
            List<Team> teamlists = teamService.selectByTeamIdsNotDel(expTeamIds);
            teamlists.forEach(item -> {
                ApiMineMsg.ApiMineTeamsMsg msg = new ApiMineMsg().new ApiMineTeamsMsg();
                msg.setId(item.getTeamId());
                msg.setTeam_name(item.getTeamName());
                msg.setParent_team_id(item.getParentTeamId());
                msg.setUpdate_at(DateUtil.datetimeToTimeStamp(item.getUpdateAt()));
                teams.add(msg);
            });
        }
        ArrayList<Integer> teamId = Lists.newArrayList();
        teams.forEach(item ->
            teamId.add(item.getId())
        );
        List<Team> teamlists = teamService.selectByTeamIdsNotDel(teamId);
        ArrayList<Integer> objects = Lists.newArrayList();
        teamlists.forEach(item -> {
            if (!objects.contains(item.getTeamId())) {
                objects.add(item.getTeamId());
            }
            List<Integer> idsComma = StringUtil.strToInts(item.getPath(), "/");
            idsComma.forEach(items -> {
                if (!objects.contains(items)) {
                    objects.add(items);
                }
            });
        });
        List<Team> teamsLists = teamService.selectByTeamIdsNotDel(objects);
        teams.clear();
        teamsLists.forEach(item -> {
            ApiMineMsg.ApiMineTeamsMsg msg = new ApiMineMsg().new ApiMineTeamsMsg();
            msg.setId(item.getTeamId());
            msg.setTeam_name(item.getTeamName());
            msg.setParent_team_id(item.getParentTeamId());
            msg.setUpdate_at(DateUtil.datetimeToTimeStamp(item.getUpdateAt()));
            teams.add(msg);
        });
        ApiMineMsg apiMineMsg = new ApiMineMsg();
        apiMineMsg.setProjects(projects);
        apiMineMsg.setTeams(teams);
        return apiMineMsg;
    }

    public boolean datetimeZero(Date deleteAt) {
        if (deleteAt == null) {
            return false;
        }

        String dateStr = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(deleteAt);
        return dateStr.equals("0001-01-01 00:00:00") || dateStr.equals("");

    }

    public Map createTeamsMap(List<Integer> parentIds) {
        List<Team> teamlist = teamService.selectByTeamIdsNotDel(parentIds);
        HashMap<Integer, Team> parentDict = Maps.newHashMap();
        for (int i = 0; i < teamlist.size(); i++) {
            parentDict.put(teamlist.get(i).getTeamId(), teamlist.get(i));
        }
        return parentDict;
    }
}
