package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskService {

    Integer searchTotalByProjIdAndCategoryClsAndStatus(Integer projId, Integer categoryCls, Integer status);

    List<HouseQmCheckTask> searchByProjIdAndCategoryClsAndStatusByPage(Integer projId, Integer categoryCls, Integer status, int limit, int start);

    List<HouseQmCheckTask> searchByProjectIdAndCategoryClsInAndTaskIdIn(Integer projectId, List<Integer> categoryClsList, List<Integer> taskIds);

    List<HouseQmCheckTask> searchByProjectIdInAndCategoryClsIn(List<Integer> projectIds, List<Integer> categoryClsList);

    List<HouseQmCheckTask> searchHouseQmCheckTaskByTaskIdIn(List<Integer> taskIds);

    HouseQmCheckTask getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId);

    HouseQmCheckTask getHouseQmCheckTaskByProjTaskIdUnscoped(Integer projectId, Integer taskId);


    List<HouseQmCheckTask> selectByTaskIds(Set<Integer> taskIds);

    List<HouseQmCheckTask> selectByTaskIdsEvenDeleted(Set<Integer> taskIds);

    List<HouseQmCheckTask> selectByProjectIdAndCategoryCls(Integer projectId, Integer categoryCls);



    HouseQmCheckTask selectByProjectIdAndTaskId(Integer projectId, Integer taskId);

    List<HouseQmCheckTask> searchByProjectIdAndCategoryClsIn(Integer projectId, List<Integer> categoryCls);

    HouseQmCheckTask selectAreaIdsByProjectIdAndTaskIdAndNoDeleted(Integer projectId, Integer taskId);

    /**
     * @param taskId
     * @return java.util.Date
     * @author hy
     * @date 2018/12/25 0025
     */
    HouseQmCheckTask selectUpdateAtByTaskIdAndNoDeleted(Integer taskId);

    int add(HouseQmCheckTask houseQmCheckTask);

    HouseQmCheckTask selectById(int one);

    int removeHouseQmCheckTaskByProjectIdAndTaskId(Integer projectId, Integer taskId);

    HouseQmCheckTask selectByTaskId(Integer integer);

    HouseQmCheckTask selectByProjectIdAndTaskIdAndDel(Integer projectId, Integer taskId);

    int update(HouseQmCheckTask taskInfo);

    int delete(HouseQmCheckTask houseQmCheckTask);

    List<HouseQmCheckTask> selectByProjectIdsAndCategoryClsNotDel(ArrayList<Integer> parentIds, List<Integer> categorylist);

    HouseQmCheckTask getByTaskId(Integer taskId);

    List<HouseQmCheckTask> selectByProjectIdAndCategoryClsAndStatus(int projectId, int categoryCls, int status);
}
