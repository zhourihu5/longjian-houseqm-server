package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.IssueFieldSetting;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/16.
 */
public interface IssueFieldSettingService {
    int add(IssueFieldSetting settings6);

    List<IssueFieldSetting> findProjectIdsAndModuleId(List<Integer> projectIdList,Integer moduleId);
}
