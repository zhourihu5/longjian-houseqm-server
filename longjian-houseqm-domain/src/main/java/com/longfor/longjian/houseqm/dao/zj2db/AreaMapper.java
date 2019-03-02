package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.Area;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/18.
 */
public interface AreaMapper extends LFMySQLMapper<Area> {


    List<Area> selectAreaByIdInAndNoDeleted(@Param("areaPaths") List<Integer> areaPaths,@Param("deleted") String deleted);

}
