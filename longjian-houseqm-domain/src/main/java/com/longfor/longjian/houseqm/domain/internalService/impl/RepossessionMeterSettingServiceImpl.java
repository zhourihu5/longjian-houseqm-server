package com.longfor.longjian.houseqm.domain.internalService.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.RepossessionMeterSettingMapper;
import com.longfor.longjian.houseqm.domain.internalService.RepossessionMeterSettingService;
import com.longfor.longjian.houseqm.po.RepossessionMeterSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/21.
 */
@Transactional
@Service
@Slf4j
public class RepossessionMeterSettingServiceImpl implements RepossessionMeterSettingService {
    @Resource
    RepossessionMeterSettingMapper repossessionMeterSettingMapper;
    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepossessionMeterSetting> selectByProjectId(Integer integer) {
        Example example = new Example(RepossessionMeterSetting.class);
        example.createCriteria().andEqualTo("projectId",integer);
        return repossessionMeterSettingMapper.selectByExample(example);
    }

    @Override
    public void add(RepossessionMeterSetting setting) {
        repossessionMeterSettingMapper.insert(setting);
    }
}