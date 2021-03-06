package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zhijian2_apisvr.Team;

import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/19 0019 19:32
 */
public interface TeamService {

    List<Team> searchByTeamIdIn(List<Integer> teamIds);

    Team selectByTeamId(int teamId);

    List<Team> selectByTeamIdsNotDel(List<Integer> teamIds);

    List<Team> selectGroupIdNotDel(Integer groupId);
}
