package com.longfor.longjian.houseqm.app.service.impl;


import com.longfor.longjian.houseqm.app.service.FixingPresetAppService;
import com.longfor.longjian.houseqm.domain.internalservice.FixingPresetService;
import com.longfor.longjian.houseqm.po.zj2db.FixingPreset;
import com.longfor.longjian.houseqm.util.DateUtil;
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
public class FixingPresetAppServiceImpl implements FixingPresetAppService {
    @Resource
    FixingPresetService fixingPresetService;

    @Override
    public List<FixingPreset> appGetFixingPreset(Integer projectId, Integer lastId, Integer timestamp, Integer limit) {
        if (limit == null) {
            limit = 2000;
        }
        if (timestamp == null) {
            return fixingPresetService.selectByProAndIdAndUIdsAndminutes(projectId, lastId, limit);
        } else {
            //int 转成date
            Date time = DateUtil.transForDate(timestamp);
            return fixingPresetService.selectByProAndIdAndUpdate(projectId, lastId, limit, time);
        }
    }
}
