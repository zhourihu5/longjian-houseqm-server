package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskMapper extends LFMySQLMapper<HouseQmCheckTask> {

    /**
     *
     * @param taskIdList
     * @param deleted
     * @return
     */
    List<HouseQmCheckTask> selectByTaskIds(@Param("idList")Set<Integer> taskIdList, @Param("deleted")String deleted);

    List<HouseQmCheckTask>  selectByProjectIdAndCategoryCls(@Param("projectId") Integer projectId, @Param("categoryCls") Integer categoryCls,@Param("deleted")String deleted);

    HouseQmCheckTask getHouseQmCheckTaskByProjTaskId(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId);
}