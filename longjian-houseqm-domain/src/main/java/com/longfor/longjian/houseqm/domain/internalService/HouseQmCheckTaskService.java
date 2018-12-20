package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTask;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskService {
    List<HouseQmCheckTask> selectByTaskIds(Set<Integer> taskIds);

    List<HouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIds);

    List<HouseQmCheckTask> selectByProjectIdAndCategoryCls(Integer projectId, Integer categoryCls);

    List<HouseQmCheckTask> selectByProjectIdAndCategoryClsAndStatus(HouseQmCheckTask houseQmCheckTask);

    HouseQmCheckTask selectByProjectIdAndTaskId(Integer projectId, Integer taskId);

    List<HouseQmCheckTask> searchByProjectIdAndCategoryClsIn(Integer projectId, List<Integer> categoryCls);

    HouseQmCheckTask selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(Integer projectId, Integer taskId);

}
