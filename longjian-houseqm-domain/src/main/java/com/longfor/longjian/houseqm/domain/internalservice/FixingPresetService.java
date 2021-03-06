package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.FixingPreset;

import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/25.
 */

public interface FixingPresetService {
    List<FixingPreset> selectByProAndIdAndUIdsAndminutes(Integer projectId, Integer lastId, Integer limit);

    List<FixingPreset> selectByProAndIdAndUpdate(Integer projectId, Integer lastId, Integer limit, Date time);
}
