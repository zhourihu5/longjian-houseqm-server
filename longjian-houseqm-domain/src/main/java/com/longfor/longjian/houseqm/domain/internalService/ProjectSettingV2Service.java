package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.ProjectSettingV2;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/11.
 */
public interface ProjectSettingV2Service {
    List<ProjectSettingV2> getProjectSettingId(Integer projectId);
}
