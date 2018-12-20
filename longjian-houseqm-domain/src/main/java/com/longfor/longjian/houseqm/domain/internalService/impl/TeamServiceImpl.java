package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.longjian.houseqm.dao.TeamMapper;
import com.longfor.longjian.houseqm.domain.internalService.TeamService;
import com.longfor.longjian.houseqm.po.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Houyan
 * @date 2018/12/13 0013 18:05
 */
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Resource
    TeamMapper teamMapper;

    /**
     *
     * @param teamId
     * @return
     */
    public Team selectByTeamId(int teamId){
        return teamMapper.selectByTeamId(teamId,"false");
    }
}
