package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskSquad;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskSquadMapper extends LFMySQLMapper<HouseQmCheckTaskSquad> {
    List<HouseQmCheckTaskSquad> selectByTaskIds(@Param("idList") Set<Integer> taskIdList,@Param("deleted") String aFalse);
    List<HouseQmCheckTaskSquad> searchByInId(@Param("squadIds") List<Integer> squadIds);
}