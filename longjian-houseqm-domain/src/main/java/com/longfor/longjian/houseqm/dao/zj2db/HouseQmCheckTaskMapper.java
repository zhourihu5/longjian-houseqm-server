package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;
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
    List<HouseQmCheckTask> selectByTaskIds(@Param("taskIdList")Set<Integer> taskIdList, @Param("deleted")String deleted);

    /**
     *
     * @param houseQmCheckTask
     * @param deleted
     * @return
     */
    List<HouseQmCheckTask> selectByProjectIdAndCategoryClsAndStatus(@Param("houseQmCheckTask") HouseQmCheckTask houseQmCheckTask,@Param("deleted")String deleted);

    /**
     *
     * @param projectId
     * @param taskId
     * @return
     */
    HouseQmCheckTask selectByProjectIdAndTaskId(@Param("projectId") Integer projectId,@Param("taskId") Integer taskId);

    /**
     *
     * @param projectId
     * @param categoryCls
     * @return
     */
    List<HouseQmCheckTask>  selectByProjectIdAndCategoryCls(@Param("projectId") Integer projectId, @Param("categoryCls") Integer categoryCls,@Param("deleted")String deleted);



    HouseQmCheckTask selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(@Param("projectId") Integer projectId, @Param("taskId") Integer taskId,@Param("deleted") String deleted);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param taskId
     * @param deleted
     * @return java.util.Date
     */
    HouseQmCheckTask selectUpdateAtByTaskIdAndNoDeleted(@Param("taskId") Integer taskId, @Param("deleted") String deleted);
}