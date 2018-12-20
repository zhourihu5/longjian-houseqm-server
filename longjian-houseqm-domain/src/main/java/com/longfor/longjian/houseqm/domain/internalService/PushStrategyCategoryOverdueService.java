package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.PushStrategyCategoryOverdue;

import java.util.List;
import java.util.Set;

public interface PushStrategyCategoryOverdueService {
    List<PushStrategyCategoryOverdue> searchByTaskIds(Set<Integer> taskIds);
}
