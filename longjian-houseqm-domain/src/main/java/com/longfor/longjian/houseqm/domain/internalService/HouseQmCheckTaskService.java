package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTask;

import java.util.Date;
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

    /**
     *
     * @author hy
     * @date 2018/12/25 0025
     * @param taskId
     * @return java.util.Date
     */
    HouseQmCheckTask selectUpdateAtByTaskIdAndNoDeleted(Integer taskId);

    int add(HouseQmCheckTask houseQmCheckTask);

    HouseQmCheckTask selectById(int one);

    int removeHouseQmCheckTaskByProjectIdAndTaskId(Integer project_id, Integer task_id);

    HouseQmCheckTask selectByTaskId(Integer integer);
}
