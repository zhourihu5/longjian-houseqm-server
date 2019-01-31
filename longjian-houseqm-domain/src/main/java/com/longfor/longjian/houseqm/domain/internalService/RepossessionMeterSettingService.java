package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zj2db.RepossessionMeterSetting;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/21.
 */
public interface RepossessionMeterSettingService  {
    List<RepossessionMeterSetting> selectByProjectId(Integer integer);

    void add(RepossessionMeterSetting setting);
}
