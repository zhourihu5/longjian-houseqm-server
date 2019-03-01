package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.ProjectSettingMapper;
import com.longfor.longjian.houseqm.domain.internalService.ProjectSettingService;
import com.longfor.longjian.houseqm.po.zj2db.ProjectSetting;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;


@Service
@Slf4j
public class ProjectSettingServiceImpl implements ProjectSettingService {


    @Resource
    private ProjectSettingMapper projectSettingMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public ProjectSetting getSettingByProjectIdSKey(int projId, String sKey) {
        Example example = new Example(ProjectSetting.class);
        example.createCriteria().andEqualTo("projectId",projId).andEqualTo("sKey",sKey);
        ExampleUtil.addDeleteAtJudge(example);
        return projectSettingMapper.selectOneByExample(example);
    }
}
