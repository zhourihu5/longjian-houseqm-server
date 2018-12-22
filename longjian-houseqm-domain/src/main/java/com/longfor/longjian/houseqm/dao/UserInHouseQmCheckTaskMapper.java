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
     * @param userId
     * @param task_id
     * @param checker
     * @return
     */
    List<UserInHouseQmCheckTask> searchByTaskIdUserIdRoleType(@Param("userId")Integer userId, @Param("task_id")Integer task_id, @Param("checker") Integer checker);

    /**
     *
     * @param squadIds
     * @return
     */
    List<UserInHouseQmCheckTask> searchBySquadIdIn(@Param("squadIds") List<Integer> squadIds);
    /**
     *
     * @param taskId
     * @param deleted
     * @return
     */
    List<UserInHouseQmCheckTask> selectByTaskIdAndNoDeleted(@Param("taskId") Integer taskId,@Param("deleted") String deleted);

    /**
     *
     * @param task_id
     * @param userId
     * @return
     */
    List<UserInHouseQmCheckTask> searchByCondition(@Param("task_id") Integer task_id,@Param("userId") Integer userId);

    /**
     *
     * @param task_id
     * @param existsSquadIds
     * @return
     */
    List<UserInHouseQmCheckTask> searchByTaskIdSquadIdIn(@Param("task_id") Integer task_id, @Param("existsSquadIds") List<Integer> existsSquadIds);
}