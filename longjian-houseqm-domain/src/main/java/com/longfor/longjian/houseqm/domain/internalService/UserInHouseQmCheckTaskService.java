package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.UserInHouseQmCheckTask;

import java.util.List;
import java.util.Set;

public interface UserInHouseQmCheckTaskService {
    List<UserInHouseQmCheckTask> searchByUserId(Integer userId);

    List<UserInHouseQmCheckTask> selectByTaskIds(Set<Integer> taskIdList);

    List<UserInHouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList);

}
