package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryOverdue;

import java.util.List;
import java.util.Set;

public interface PushStrategyCategoryOverdueService {
    List<PushStrategyCategoryOverdue> searchByTaskIds(Set<Integer> taskIds);

    int add(PushStrategyCategoryOverdue pushStrategyCategoryOverdue);

    PushStrategyCategoryOverdue selectByTaskIdAndNotDel(Integer task_id);

    int update(PushStrategyCategoryOverdue dbConfigCategoryOverdue);

    int delete(PushStrategyCategoryOverdue dbConfigCategoryOverdue);
}
