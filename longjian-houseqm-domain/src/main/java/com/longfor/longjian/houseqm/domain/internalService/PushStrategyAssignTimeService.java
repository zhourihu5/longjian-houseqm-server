package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.PushStrategyAssignTime;

import java.util.List;
import java.util.Set;

public interface PushStrategyAssignTimeService {
    List<PushStrategyAssignTime> searchByTaskIds(Set<Integer> taskIds);

    int add(PushStrategyAssignTime pushStrategyAssignTime);

    PushStrategyAssignTime selectByIdAndNotDel(Integer task_id);

    int update(PushStrategyAssignTime dbConfigAssignTime);

    int delete(PushStrategyAssignTime dbConfigAssignTime);
}
