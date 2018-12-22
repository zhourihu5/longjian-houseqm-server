package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskSquadMapper extends LFMySQLMapper<HouseQmCheckTaskSquad> {


    /**
     *
     * @param userId
     * @return
     */
    List<HouseQmCheckTaskSquad> selectByUserId(@Param("userId")Integer userId);


    /**
     *
     * @param taskIdList
     * @param deleted
     * @return
     */
    List<HouseQmCheckTaskSquad> selectByTaskIds(@Param("idList")Set<Integer> taskIdList, @Param("deleted")String deleted);

    /**
     *
     * @param squadIds
     * @return
     */
    List<HouseQmCheckTaskSquad> searchByInId(@Param("squadIds") List<Integer> squadIds);
}