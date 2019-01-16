package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTask;
import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface UserInHouseQmCheckTaskService {



    List<UserInHouseQmCheckTask> searchByTaskIdUserIdRoleType(int taskId, int userId, Integer roleType);

    List<UserInHouseQmCheckTask> searchByUserId(Integer userId);

    List<UserInHouseQmCheckTask> selectByTaskIds(Set<Integer> taskIdList);

    List<UserInHouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList);

    List<UserInHouseQmCheckTask> searchByTaskIdAndNoDeleted(Integer taskId);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param checker
     * @param uid
     * @param task_id
     * @return java.util.List<com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask>
     */
    List<UserInHouseQmCheckTask> selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(Integer checker, Integer uid, Integer task_id);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param squadIds
     * @return java.util.List<com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask>
     */
    List<UserInHouseQmCheckTask> selectUserIdBySquadIdInAndNoDeleted(List<Integer> squadIds);

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param task_id
     * @return com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask
     */
    List<UserInHouseQmCheckTask> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer task_id);



    int add(UserInHouseQmCheckTask qmCheckTask);

    UserInHouseQmCheckTask selectByTaskIdAndUserIdAndNotDel(Integer taskId, Integer uid);
    int removeByTaskId(Integer task_id);

    List<UserInHouseQmCheckTask> searchBySquadIdIn(List<Integer> squadIds);
    UserInHouseQmCheckTask selectBysquadIdAnduserIdAndtaskIdAndNotDel(Integer squadId, Integer userId, Integer taskId);

    int update(UserInHouseQmCheckTask dbItem);

    List<UserInHouseQmCheckTask> selectByIdAndTaskId(Object o, Integer task_id);

    int delete(UserInHouseQmCheckTask userInHouseQmCheckTask);

    List<UserInHouseQmCheckTask> selectByTaskIdAndRoleType(Integer task_id, Integer value);

    List<UserInHouseQmCheckTask> selectBysquadIdAndtaskId(Object o, Integer task_id);
}
