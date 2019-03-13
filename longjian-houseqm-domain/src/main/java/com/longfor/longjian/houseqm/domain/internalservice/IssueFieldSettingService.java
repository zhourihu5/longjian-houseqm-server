package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zhijian2_setting.IssueFieldSetting;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/16.
 */
public interface IssueFieldSettingService {
    int add(IssueFieldSetting settings6);

    List<IssueFieldSetting> findProjectIdsAndModuleId(List<Integer> projectIdList, Integer moduleId);

    IssueFieldSetting get(Integer projectId, Integer fieldId, Integer value);

    int update(IssueFieldSetting issueField);
}
