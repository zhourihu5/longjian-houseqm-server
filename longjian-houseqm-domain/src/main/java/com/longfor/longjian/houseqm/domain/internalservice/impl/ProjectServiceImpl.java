package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.ProjectMapper;
import com.longfor.longjian.houseqm.domain.internalservice.ProjectService;
import com.longfor.longjian.houseqm.po.zj2db.Project;
import com.longfor.longjian.houseqm.utils.ExampleUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<Project> searchByProjectIdIn(List<Integer> projectIds) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id",projectIds);
        ExampleUtil.addDeleteAtJudge(example);
        return projectMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public Project getOneByProjId(int projId) {
        Example example = new Example(Project.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",projId);
        ExampleUtil.addDeleteAtJudge(example);
        return projectMapper.selectOneByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<Project> selectByIdNotDel(ArrayList<Integer> projectIdsList) {
        Example example = new Example(Project.class);
        example.createCriteria().andIn("id",projectIdsList).andIsNull("deleteAt");
        return projectMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<Project> selectByTeamIdsNotDel(List<Integer> teamIdsList) {
        Example example = new Example(Project.class);
        example.createCriteria().andIn("teamId",teamIdsList).andIsNull("deleteAt");
        return projectMapper.selectByExample(example);
    }
}
