package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.ProjectSetting;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalservice
 * @ClassName: ProjectSettingService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/3/4 10:54
 */
public interface ProjectSettingService {
    ProjectSetting getSettingByProjectIdSKey(int projId, String sKey);
}
