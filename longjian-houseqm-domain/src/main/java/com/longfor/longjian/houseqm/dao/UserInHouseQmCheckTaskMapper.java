package com.longfor.longjian.houseqm.dao;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserInHouseQmCheckTaskMapper extends LFMySQLMapper<UserInHouseQmCheckTask> {
    /**
     *
     * @param taskIdList
     * @param deleted
     * @return
     */
    List<UserInHouseQmCheckTask> selectByTaskIds(@Param("idList")Set<Integer> taskIdList, @Param("deleted")String deleted);


    /**
     *
     * @param userId
     * @return
     */
    List<UserInHouseQmCheckTask> selectByUserId(@Param("userId")Integer userId);

    /**
     *
     * @param taskId
     * @param deleted
     * @return
     */
    List<UserInHouseQmCheckTask> selectByTaskIdAndNoDeleted(@Param("taskId") Integer taskId,@Param("deleted") String deleted);

}