package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.IssueFieldSettingMapper;
import com.longfor.longjian.houseqm.domain.internalService.IssueFieldSettingService;
import com.longfor.longjian.houseqm.po.IssueFieldSetting;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/16.
 */
@Transactional
@Slf4j
@Service
public class IssueFieldSettingServiceImpl  implements IssueFieldSettingService {
    @Resource
    IssueFieldSettingMapper issueFieldSettingMapper;
    @Override
    @LFAssignDataSource("zhijian2_setting")
    public int add(IssueFieldSetting settings6) {
        return issueFieldSettingMapper.insert(settings6);
    }

    @Override
    @LFAssignDataSource("zhijian2_setting")
    public List<IssueFieldSetting> findProjectIdsAndModuleId(List<Integer> projectIdList,Integer moduleId) {
        Example example = new Example(IssueFieldSetting.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("moduleId",moduleId).andIn("projectId",projectIdList);

        return issueFieldSettingMapper.selectByExample(example);
    }
}