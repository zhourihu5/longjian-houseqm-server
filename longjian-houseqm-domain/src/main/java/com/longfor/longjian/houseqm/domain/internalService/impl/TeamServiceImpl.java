package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.TeamMapper;
import com.longfor.longjian.houseqm.domain.internalService.TeamService;
import com.longfor.longjian.houseqm.po.Team;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    @LFAssignDataSource("zhijian2_apisvr")
    public List<Team> selectByTeamIdsNotDel(ArrayList<Integer> teamIds) {
        Example example = new Example(Team.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("teamId",teamIds).andIsNull("deleteAt");
        return   teamMapper.selectByExample(example);
    }
}
