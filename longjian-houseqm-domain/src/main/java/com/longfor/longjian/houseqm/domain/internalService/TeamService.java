package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/19 0019 19:32
 */
public interface TeamService {
    Team selectByTeamId(int teamId);

    List<Team> selectByTeamIdsNotDel(ArrayList<Integer> teamIds);
}
