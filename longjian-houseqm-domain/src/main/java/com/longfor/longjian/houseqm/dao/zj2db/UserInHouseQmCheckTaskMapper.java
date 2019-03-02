package com.longfor.longjian.houseqm.dao.zj2db;

import com.longfor.gaia.gfs.data.mybatis.LFMySQLMapper;
import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserInHouseQmCheckTaskMapper extends LFMySQLMapper<UserInHouseQmCheckTask> {
    List<UserInHouseQmCheckTask> selectByTaskIds(@Param("idList")List<Integer> taskIdList, @Param("deleted")String deleted);

    List<UserInHouseQmCheckTask> searchByTaskIdUserIdRoleType(@Param("userId")Integer userId, @Param("task_id")Integer taskId, @Param("checker") Integer checker);

    List<UserInHouseQmCheckTask> searchBySquadIdIn(@Param("squadIds") List<Integer> squadIds);

    List<UserInHouseQmCheckTask> searchByCondition(@Param("task_id") Integer taskId,@Param("userId") Integer userId);

    List<UserInHouseQmCheckTask> searchByTaskIdSquadIdIn(@Param("task_id") Integer taskId, @Param("existsSquadIds") List<Integer> existsSquadIds);

}