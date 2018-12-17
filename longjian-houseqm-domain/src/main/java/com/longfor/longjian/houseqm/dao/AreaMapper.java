package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AreaMapper extends LFMySQLMapper<Area> {

    /**
     *
     * @param areaIds
     * @return
     */
    List<Area> selectByIdInAreaIds(@Param("areaIds") List<Integer> areaIds);

}