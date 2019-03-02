package com.longfor.longjian.houseqm.app.service;

import com.google.common.collect.Lists;
import com.longfor.longjian.common.exception.LjBaseRuntimeException;
import com.longfor.longjian.common.time.TimeFrame;
import com.longfor.longjian.common.time.TimeFrameHelper;
import com.longfor.longjian.common.util.DateUtil;
import com.longfor.longjian.houseqm.app.vo.StatDataVo;
import com.longfor.longjian.houseqm.config.LjTimeUtil;
import com.longfor.longjian.houseqm.domain.internalservice.ProjectService;
import com.longfor.longjian.houseqm.domain.internalservice.TeamService;
import com.longfor.longjian.houseqm.domain.internalservice.stat.StatHouseQmProjectDailyStatService;
import com.longfor.longjian.houseqm.po.stat.StatHouseQmProjectDailyStat;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
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

    private static final String TIME_FORMAT = "yyyy-MM-dd";


    public List<StatDataVo> searchStat(Integer groupId, String categoryKey, String timeFrameType, List<Integer> teamIds, Date timeFrameBegin,
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

        List<StatDataVo>  statDataVos = Lists.newArrayList();

        for (TimeFrame timeFrame : frames) {
            StatHouseQmProjectDailyStat stat = statHouseQmProjectDailyStatService.query(timeFrame, categoryKey,
                    projectIdList);
            tranferDataVo(stat,timeFrame,statDataVos);
        }
        return  statDataVos;
    }


    private void tranferDataVo(StatHouseQmProjectDailyStat stat, TimeFrame timeFrame, List<StatDataVo>  statDataVos){

        if(stat == null){
            return;
        }
        StatDataVo vo = new StatDataVo();
        BeanUtils.copyProperties(stat, vo);

        vo.setTimeFrameType(timeFrame.getType());
        vo.setBeginOn(DateUtil.dateToString(timeFrame.getBeginOn(),TIME_FORMAT));
        vo.setEndOn(DateUtil.dateToString(timeFrame.getEndOn(),TIME_FORMAT));
        vo.setYear(timeFrame.getYear());
        vo.setTimeFrameIdx(timeFrame.getIdx());

        statDataVos.add(vo);
    }

}
