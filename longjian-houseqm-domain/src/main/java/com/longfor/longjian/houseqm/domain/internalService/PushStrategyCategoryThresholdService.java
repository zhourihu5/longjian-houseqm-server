package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.zhijian2_notify.PushStrategyCategoryThreshold;

import java.util.List;
import java.util.Set;

public interface PushStrategyCategoryThresholdService {
    List<PushStrategyCategoryThreshold> searchByTaskIds(Set<Integer> taskIds);

    int add(PushStrategyCategoryThreshold pushStrategyCategoryThreshold);

    PushStrategyCategoryThreshold selectTaskIdAndNotDel(Integer taskId);

    int update(PushStrategyCategoryThreshold dbConfigCategoryThreshold);

    int delete(PushStrategyCategoryThreshold dbConfigCategoryThreshold);
}
