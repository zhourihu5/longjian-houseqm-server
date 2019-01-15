package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.ProjectSetting;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService
 * @ClassName: ProjectSettingService
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 16:38
 */
public interface ProjectSettingService {
    ProjectSetting getSettingByProjectIdSKey(int projId,String sKey);
}
