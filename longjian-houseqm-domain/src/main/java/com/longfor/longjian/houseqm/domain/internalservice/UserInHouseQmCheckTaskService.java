package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.UserInHouseQmCheckTask;

import java.util.List;
import java.util.Set;

public interface UserInHouseQmCheckTaskService {

    List<UserInHouseQmCheckTask> searchByTaskIdInAndRoleType(List<Integer> taskIds, Integer roleType);

    List<UserInHouseQmCheckTask> searchByUserIdAndRoleType(int uid, Integer roleType);

    List<UserInHouseQmCheckTask> searchByTaskIdUserIdRoleType(int taskId, int userId, Integer roleType);

    List<UserInHouseQmCheckTask> searchByUserId(Integer userId);

    List<UserInHouseQmCheckTask> selectByTaskIds(Set<Integer> taskIdList);

    List<UserInHouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList);

    List<UserInHouseQmCheckTask> searchByTaskIdAndNoDeleted(Integer taskId);

    List<UserInHouseQmCheckTask> selectSquadIdByTaskIdAndUserIdAndRoleTypeAndNoDeleted(Integer checker, Integer uid, Integer taskId);

    List<UserInHouseQmCheckTask> selectUserIdBySquadIdInAndNoDeleted(List<Integer> squadIds);

    List<UserInHouseQmCheckTask> selectUpdateAtByTaskIdAndNoDeletedOrderByUpdateAt(Integer taskId);

    int add(UserInHouseQmCheckTask qmCheckTask);

    UserInHouseQmCheckTask selectByTaskIdAndUserIdAndNotDel(Integer taskId, Integer uid);

    int removeByTaskId(Integer taskId);

    List<UserInHouseQmCheckTask> searchBySquadIdIn(List<Integer> squadIds);

    UserInHouseQmCheckTask selectBysquadIdAnduserIdAndtaskIdAndNotDel(Integer squadId, Integer userId, Integer taskId);

    int update(UserInHouseQmCheckTask dbItem);

    List<UserInHouseQmCheckTask> selectByIdAndTaskId(Object o, Integer taskId);

    int delete(UserInHouseQmCheckTask userInHouseQmCheckTask);

    List<UserInHouseQmCheckTask> selectByTaskIdAndRoleType(Integer taskId, Integer value);

    List<UserInHouseQmCheckTask> selectBysquadIdAndtaskId(Object o, Integer taskId);

    List<UserInHouseQmCheckTask> searchUserInHouseQmCheckTaskByUserIdRoleType(Integer uid, Integer id);

    List<UserInHouseQmCheckTask> selectByTaskIdInAndRoleTypeNotDel(List<Integer> taskIds, Integer value);
}
