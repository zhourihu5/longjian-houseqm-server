package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zhijian2_setting.IssueFieldSettingMapper;
import com.longfor.longjian.houseqm.domain.internalservice.IssueFieldSettingService;
import com.longfor.longjian.houseqm.po.zhijian2_setting.IssueFieldSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/16.
 */
@Transactional
@Slf4j
@Service
public class IssueFieldSettingServiceImpl implements IssueFieldSettingService {
    @Resource
    IssueFieldSettingMapper issueFieldSettingMapper;

    @Override
    @LFAssignDataSource("zhijian2_setting")
    public int add(IssueFieldSetting settings6) {
        settings6.setUpdateAt(new Date());
        settings6.setCreateAt(new Date());
        return issueFieldSettingMapper.insert(settings6);
    }

    @Override
    @LFAssignDataSource("zhijian2_setting")
    public List<IssueFieldSetting> findProjectIdsAndModuleId(List<Integer> projectIdList, Integer moduleId) {
        Example example = new Example(IssueFieldSetting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("moduleId", moduleId).andIn("projectId", projectIdList);

        return issueFieldSettingMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2_setting")
    public IssueFieldSetting get(Integer projectId, Integer fieldId, Integer value) {
        Example example = new Example(IssueFieldSetting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("projectId",projectId).andEqualTo("fieldId",fieldId).andEqualTo("moduleId",value).andIsNull("deleteAt");
        return issueFieldSettingMapper.selectOneByExample(example);
    }

    @Override
    public int update(IssueFieldSetting issueField) {

        return issueFieldSettingMapper.updateByPrimaryKeySelective(issueField);
    }
}
