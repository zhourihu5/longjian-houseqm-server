package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskMapper extends LFMySQLMapper<HouseQmCheckTask> {

    List<HouseQmCheckTask> selectByTaskIds(@Param("taskIdList")Set<Integer> taskIdList, @Param("deleted")String deleted);


    List<HouseQmCheckTask> selectByProjectIdAndCategoryClsAndStatus(@Param("houseQmCheckTask") HouseQmCheckTask houseQmCheckTask,@Param("deleted")String deleted);

    List<HouseQmCheckTask>  selectByProjectIdAndCategoryCls(@Param("projectId") Integer projectId, @Param("categoryCls") Integer categoryCls,@Param("deleted")String deleted);

    HouseQmCheckTask selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId,@Param("deleted") String deleted);


    HouseQmCheckTask selectUpdateAtByTaskIdAndNoDeleted(@Param("taskId") Integer taskId, @Param("deleted") String deleted);
}