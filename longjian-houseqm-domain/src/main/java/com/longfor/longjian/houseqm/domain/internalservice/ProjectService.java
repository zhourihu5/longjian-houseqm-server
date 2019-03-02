package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.Project;

import java.util.ArrayList;
import java.util.List;


public interface ProjectService {

    List<Project> searchByProjectIdIn(List<Integer> projectIds);

    Project getOneByProjId(int projId);

    List<Project> selectByIdNotDel(ArrayList<Integer> projectIdsList);

    List<Project> selectByTeamIdsNotDel(List<Integer> teamIdsList);

}
