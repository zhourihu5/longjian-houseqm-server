package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import org.apache.ibatis.annotations.Param;

public interface HouseQmCheckTaskMapper extends LFMySQLMapper<HouseQmCheckTask> {


    HouseQmCheckTask selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId, @Param("deleted") String deleted);


    HouseQmCheckTask selectUpdateAtByTaskIdAndNoDeleted(@Param("taskId") Integer taskId, @Param("deleted") String deleted);
}