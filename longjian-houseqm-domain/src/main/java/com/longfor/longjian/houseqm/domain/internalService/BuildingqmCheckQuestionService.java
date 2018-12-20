package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.IssueFieldSetting;

import java.util.ArrayList;
import java.util.List;

public interface BuildingqmCheckQuestionService {

    List<IssueFieldSetting> selectByProjectId(Integer projectId);

    List<IssueFieldSetting> findProjectIdsAndModuleId(ArrayList<Integer> projectIdList);

}
