package com.longfor.longjian.houseqm.domain.internalService;

import com.longfor.longjian.houseqm.dto.RepossessionStatusCompleteDailyCountDto;
import com.longfor.longjian.houseqm.po.RepossessionStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Dongshun on 2018/12/29.
 */

public interface RepossessionStatusService {
    List<RepossessionStatus> searchByTaskIdAreaIdLike(Integer taskId, Integer areaId);
    List<RepossessionStatus> searchRepossessionStatusByTaskIdAreaIdLike(Integer taskId, Integer areaId);

    RepossessionStatusCompleteDailyCountDto searchByTaskIdInAndStatusAndNoDeletedOrStatusClientUpdateAt(Map<String, Object> condi);

    List<RepossessionStatusCompleteDailyCountDto> searchByTaskIdInAndStatusAndNoDeletedGroupByDateOrderByDateByPage(Map<String, Object> condi);

    List<RepossessionStatus> searchByTaskIdAndStatusInAndStatusClientUpdateAt(int taskId, List<Integer> repossStatuses, Date startTime, Date endTime);
}
