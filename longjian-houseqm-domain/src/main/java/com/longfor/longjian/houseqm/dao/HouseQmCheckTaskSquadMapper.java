package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskSquadMapper extends LFMySQLMapper<HouseQmCheckTaskSquad> {
    List<HouseQmCheckTaskSquad> selectByTaskIds(Set<Integer> taskIdList, String aFalse);

    int add(HouseQmCheckTaskSquad squad);

    List<HouseQmCheckTaskSquad> searchByInId(List<Integer> squadIds);
}