package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.Project;

import java.util.List;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService
 * @ClassName: ProjectService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:29
 */
public interface ProjectService {

    List<Project> searchByProjectIdIn(List<Integer> projectIds);

    Project getOneByProjId(int projId);

    List<Project> selectByIdNotDel(ArrayList<Integer> projectIdsList);
}
