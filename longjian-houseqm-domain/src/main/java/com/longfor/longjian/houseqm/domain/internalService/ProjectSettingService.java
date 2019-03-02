package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.ProjectSetting;


public interface ProjectSettingService {
    ProjectSetting getSettingByProjectIdSKey(int projId,String sKey);
}
