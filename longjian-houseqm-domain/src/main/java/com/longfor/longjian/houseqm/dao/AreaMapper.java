package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/18.
 */

public interface AreaMapper extends LFMySQLMapper<Area> {
    List<Area> selectByAreaIds(@Param(value = "idList") List<Integer> integers);

    List<Area> selectByFatherId(@Param(value="projectId")Integer prodectId, @Param(value="fatherId")int i);
}
