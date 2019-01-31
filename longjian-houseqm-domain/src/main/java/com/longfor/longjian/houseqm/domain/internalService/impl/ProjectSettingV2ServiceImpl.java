package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.ProjectSettingV2Mapper;
import com.longfor.longjian.houseqm.domain.internalService.ProjectSettingV2Service;
import com.longfor.longjian.houseqm.po.zj2db.ProjectSettingV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/11.
 */
@Service
@Slf4j
public class ProjectSettingV2ServiceImpl implements ProjectSettingV2Service {
    @Resource
    ProjectSettingV2Mapper projectSettingV2Mapper;
    @Override
    @LFAssignDataSource("zhijian2")
    public List<ProjectSettingV2> getProjectSettingId(Integer projectId) {
        Example example = new Example(ProjectSettingV2.class);
        example.createCriteria().andEqualTo("projectId",projectId);
        return   projectSettingV2Mapper.selectByExample(example);

    }
}
