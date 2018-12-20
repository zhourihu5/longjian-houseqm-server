package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.HouseQmCheckTask;

public interface HouseQmCheckTaskRspService {
    HouseQmCheckTask getHouseQmCheckTaskByProjTaskId(Integer projectId, Integer taskId);
}
