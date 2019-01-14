package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.ProjectMapper;
import com.longfor.longjian.houseqm.domain.internalService.ProjectService;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.domain.internalService.impl
 * @ClassName: ProjectServiceImpl
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:30
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public Project getOneByProjId(int projId) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",projId);
        ExampleUtil.addDeleteAtJudge(example);
        return projectMapper.selectOneByExample(example);
    }
}
