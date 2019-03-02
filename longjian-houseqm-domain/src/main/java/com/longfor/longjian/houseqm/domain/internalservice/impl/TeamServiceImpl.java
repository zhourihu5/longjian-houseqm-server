package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_apisvr.TeamMapper;
import com.longfor.longjian.houseqm.domain.internalservice.TeamService;
import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Houyan
 * @date 2018/12/13 0013 18:05
 */
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {

    @Resource
    TeamMapper teamMapper;


    @Override
    @LFAssignDataSource("zhijian2_apisvr")
    public List<Team> searchByTeamIdIn(List<Integer> team_ids) {
        Example example = new Example(Team.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("teamId",team_ids);
        ExampleUtil.addDeleteAtJudge(example);
        return teamMapper.selectByExample(example);
    }

    /**
     *
     * @param teamId
     * @return
     */
    @Override
    @LFAssignDataSource("zhijian2_apisvr")
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

    @Override
    @LFAssignDataSource("zhijian2_apisvr")
    public List<Team> selectGroupIdNotDel(Integer groupId) {
        Example example = new Example(Team.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("parentTeamId",groupId).andIsNull("deleteAt");
        return   teamMapper.selectByExample(example);
    }
}
