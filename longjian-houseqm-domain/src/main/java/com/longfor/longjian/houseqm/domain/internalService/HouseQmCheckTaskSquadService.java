package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskSquad;

import java.util.List;
import java.util.Set;

public interface HouseQmCheckTaskSquadService {
    List<HouseQmCheckTaskSquad> selectByTaskIds(Set<Integer> taskIdList);

    List<HouseQmCheckTaskSquad> selectByTaskIdsEvenDeleted(Set<Integer> taskIdList);
}
