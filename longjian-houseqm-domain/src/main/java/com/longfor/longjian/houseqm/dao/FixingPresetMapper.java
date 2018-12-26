package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.FixingPreset;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/25.
 */

public interface FixingPresetMapper  extends LFMySQLMapper<FixingPreset> {

    List<FixingPreset> selectByProAndIdAndUIdsAndminutes(@Param("projectId")Integer projectId, @Param("lastId")Integer lastId, @Param("limit")Integer limit, @Param("deleted")String deleted);

    List<FixingPreset> selectByProAndIdAndUpdate(@Param("projectId")Integer projectId,  @Param("lastId")Integer lastId, @Param("limit")Integer limit,@Param("time") Date time,@Param("deleted")String deleted);
}
