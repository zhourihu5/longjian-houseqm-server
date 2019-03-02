package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskSquad;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskSquadService {
    List<HouseQmCheckTaskSquad> selectByTaskIds(Set<Integer> taskIdList);

    List<HouseQmCheckTaskSquad> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList);

    int add(HouseQmCheckTaskSquad squad);

    HouseQmCheckTaskSquad selectById(int squadInfo);

    List<HouseQmCheckTaskSquad> searchHouseqmCheckTaskSquad(String projectId, String taskId);

    int delete(HouseQmCheckTaskSquad dbItem);

    int update(HouseQmCheckTaskSquad dbItem);

    List<HouseQmCheckTaskSquad> selectByProjectIdAndTaskIdAndSquadType(Integer projectId, Integer taskId, Integer value);
}
