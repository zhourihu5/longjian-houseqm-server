package com.longfor.longjian.houseqm.app.service;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.time.TimeFrame;
import com.longfor.longjian.common.time.TimeFrameHelper;
import com.longfor.longjian.houseqm.config.LjTimeUtil;
import com.longfor.longjian.houseqm.domain.internalService.ProjectService;
import com.longfor.longjian.houseqm.domain.internalService.TeamService;
import com.longfor.longjian.houseqm.domain.internalService.stat.StatHouseQmProjectDailyStatService;
import com.longfor.longjian.houseqm.po.Team;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectDailyStat;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2019/1/17 17:15
 */
@Service
@Slf4j
public class StatHouseQmProjectStatService {

    @Resource
    StatHouseQmProjectDailyStatService statHouseQmProjectDailyStatService;

    @Resource
    TeamService teamService;

    @Resource
    ProjectService projectService;


    /**
     * @param categoryKey
     * @param timeFrameType
     * @param teamIds
     * @param timeFrameBegin
     * @param timeFrameEnd
     * @param timeFrameMax
     */
    public void searchStat(String categoryKey, String timeFrameType, List<Integer> teamIds, Date timeFrameBegin,
                           Date timeFrameEnd, Integer timeFrameMax) {


        if (timeFrameMax == null) {
            timeFrameMax = 1;
        }

        if (timeFrameEnd == null) {
            timeFrameEnd = LjTimeUtil.yesterdayZeroDate();
        }

        List<TimeFrame> frames = TimeFrameHelper.produceFrames(timeFrameType, timeFrameMax, timeFrameBegin, timeFrameEnd, true);

        if (CollectionUtils.isEmpty(frames)) {
            log.error("searchStat timeFrameBegin:{}, timeFrameEnd:{},timeFrameMax:{},timeFrameType:{} ",
                    timeFrameBegin, timeFrameEnd, timeFrameMax, timeFrameType);
            throw new LjBaseRuntimeException(430, " no frames");
        }

        if (CollectionUtils.isEmpty(teamIds)) {
            teamIds = Lists.newArrayList();
            Integer groupId = 4;

            List<Team> teamList = teamService.selectGroupIdNotDel(groupId);
            if (CollectionUtils.isEmpty(teamList)) {
                throw new LjBaseRuntimeException(430, " no teamIds of groupId:" + groupId);
            }
            for (Team team : teamList) {
                teamIds.add(team.getTeamId());
            }
        }

        List<Project> projects = projectService.selectByTeamIdsNotDel(teamIds);
        if (CollectionUtils.isEmpty(projects)) {
            throw new LjBaseRuntimeException(430, " no projects be got");
        }
        List<Integer> projectIdList = Lists.newArrayList();
        for (Project project : projects) {
            projectIdList.add(project.getId());
        }
        projectIdList.add(767);

        List<StatHouseQmProjectDailyStat> stats = Lists.newArrayList();
        for (TimeFrame timeFrame : frames) {
            statHouseQmProjectDailyStatService.query(timeFrame, categoryKey, projectIdList);
        }
    }

}
