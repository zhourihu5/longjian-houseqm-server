package com.longfor.longjian.houseqm.domain.internalService.impl;
import com.longfor.gaia.gfs.data.mybatis.datasource.LFAssignDataSource;
import com.longfor.longjian.houseqm.dao.FixingPresetMapper;
import com.longfor.longjian.houseqm.domain.internalService.FixingPresetService;
import com.longfor.longjian.houseqm.po.FixingPreset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/25.
 */
@Service
@Slf4j
public class FixingPresetServiceImpl implements FixingPresetService {
    @Resource
    FixingPresetMapper fixingPresetMapper;
    @Override
    @LFAssignDataSource("zhijian2")
    public List<FixingPreset> selectByProAndIdAndUIdsAndminutes(Integer projectId, Integer lastId, Integer limit) {
        return fixingPresetMapper.selectByProAndIdAndUIdsAndminutes(projectId,lastId,limit,"false");
    }

    @Override
    @LFAssignDataSource("zhijian2")
    public List<FixingPreset> selectByProAndIdAndUpdate(Integer projectId, Integer lastId, Integer limit, Date time) {
        return fixingPresetMapper.selectByProAndIdAndUpdate( projectId,lastId, limit,time,"false");
    }
}
