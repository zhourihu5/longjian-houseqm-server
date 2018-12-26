package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.FixingPreset;

import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/25.
 */

public interface FixingPresetService {
    List<FixingPreset> selectByProAndIdAndUIdsAndminutes(Integer projectId, Integer lastId, Integer limit);

    List<FixingPreset> selectByProAndIdAndUpdate(Integer projectId, Integer lastId, Integer limit, Date time);
}
