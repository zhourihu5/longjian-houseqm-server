package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.po.RepossessionStatus;

import java.util.List;

/**
 * Created by Dongshun on 2018/12/29.
 */

public interface RepossessionStatusService {
    List<RepossessionStatus> SearchByTaskIdAreaIdLike(Integer taskId, Integer areaId);
}
