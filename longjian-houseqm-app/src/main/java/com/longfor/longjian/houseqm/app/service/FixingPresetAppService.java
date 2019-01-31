package com.longfor.longjian.houseqm.app.service;

import com.longfor.longjian.houseqm.po.zj2db.FixingPreset;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/25.
 */

public interface FixingPresetAppService {
    List<FixingPreset> appGetFixingPreset(Integer projectId, Integer lastId, Integer timestamp, Integer limit);
}
