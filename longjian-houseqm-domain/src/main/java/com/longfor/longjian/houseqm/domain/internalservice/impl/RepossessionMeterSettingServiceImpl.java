package com.longfor.longjian.houseqm.domain.internalservice.impl;

import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.zj2db.RepossessionMeterSettingMapper;
import com.longfor.longjian.houseqm.domain.internalservice.RepossessionMeterSettingService;
import com.longfor.longjian.houseqm.po.zj2db.RepossessionMeterSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/21.
 */
@Transactional
@Service
@Slf4j
public class RepossessionMeterSettingServiceImpl implements RepossessionMeterSettingService {
    @Resource
    private RepossessionMeterSettingMapper repossessionMeterSettingMapper;

    @Override
    @LFAssignDataSource("zhijian2")
    public List<RepossessionMeterSetting> selectByProjectId(Integer integer) {
        Example example = new Example(RepossessionMeterSetting.class);
        example.createCriteria().andEqualTo("projectId", integer);
        return repossessionMeterSettingMapper.selectByExample(example);
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public void add(RepossessionMeterSetting setting) {
        setting.setUpdateAt(new Date());
        setting.setCreateAt(new Date());
        repossessionMeterSettingMapper.insert(setting);
    }
}
