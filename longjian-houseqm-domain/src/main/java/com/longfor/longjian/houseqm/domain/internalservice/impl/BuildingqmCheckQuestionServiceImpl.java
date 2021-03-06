package com.longfor.longjian.houseqm.domain.internalservice.impl;


import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_setting.BuildingqmSettingMapper;
import com.longfor.longjian.houseqm.domain.internalservice.BuildingqmCheckQuestionService;
import com.longfor.longjian.houseqm.po.zhijian2_setting.IssueFieldSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/11.
 */
@Service
@Slf4j
public class BuildingqmCheckQuestionServiceImpl implements BuildingqmCheckQuestionService {
    @Resource
    private BuildingqmSettingMapper buildingqmSettingMapper;

    @LFAssignDataSource("zhijian2_setting")
    public List<IssueFieldSetting> selectByProjectId(Integer projectId) {

        return buildingqmSettingMapper.selectByIds(String.valueOf(projectId));
    }


    @LFAssignDataSource("zhijian2_setting")
    public List<IssueFieldSetting> findProjectIdsAndModuleId(List<Integer> projectIdList) {
        return buildingqmSettingMapper.findProjectIdsAndModuleId(projectIdList);
    }
}
