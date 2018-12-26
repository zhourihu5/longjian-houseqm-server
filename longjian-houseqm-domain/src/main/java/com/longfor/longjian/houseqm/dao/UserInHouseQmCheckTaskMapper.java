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
    List<UserInHouseQmCheckTask> selectByTaskIds(@Param("idList")List<Integer> taskIdList, @Param("deleted")String deleted);


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

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param
     * @param uid
     * @param task_id
     * @param aFalse
     * @param checker
     * @return java.util.List<com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask>
     */
    List<UserInHouseQmCheckTask> selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(@Param("uid") Integer uid, @Param("taskId") Integer task_id, @Param("deleted") String aFalse,@Param("checker") Integer checker);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     *  @param
     * @param squadIds
     * @param aFalse
     * @return java.util.List<com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask>
     */
    List<UserInHouseQmCheckTask> selectUserIdBySquadIdInAndNoDeleted(@Param("squadIds") List<Integer> squadIds,@Param("deleted") String aFalse);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @param aFalse
     * @return com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask
     */
    List<UserInHouseQmCheckTask> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(@Param("taskId") Integer task_id,@Param("deleted") String aFalse);

}