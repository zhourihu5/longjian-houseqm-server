package com.longfor.longjian.houseqm.domain.internalservice;

import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryOverdue;

import java.util.List;
import java.util.Set;

public interface PushStrategyCategoryOverdueService {
    List<PushStrategyCategoryOverdue> searchByTaskIds(Set<Integer> taskIds);

    int add(PushStrategyCategoryOverdue pushStrategyCategoryOverdue);

    PushStrategyCategoryOverdue selectByTaskIdAndNotDel(Integer taskId);

    int update(PushStrategyCategoryOverdue dbConfigCategoryOverdue);

    int delete(PushStrategyCategoryOverdue dbConfigCategoryOverdue);
}
