package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.PushStrategyCategoryThreshold;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface PushStrategyCategoryThresholdMapper extends LFMySQLMapper<PushStrategyCategoryThreshold> {
    public List<PushStrategyCategoryThreshold> selectByTaskIds(@Param("taskIds") Set<Integer> taskIds, @Param("deleted") String deleted);
}