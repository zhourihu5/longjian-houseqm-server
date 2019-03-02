package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zhijian2_setting.IssueFieldSetting;

import java.util.List;

public interface BuildingqmCheckQuestionService {

    List<IssueFieldSetting> selectByProjectId(Integer projectId);

    List<IssueFieldSetting> findProjectIdsAndModuleId(List<Integer> projectIdList);

}
