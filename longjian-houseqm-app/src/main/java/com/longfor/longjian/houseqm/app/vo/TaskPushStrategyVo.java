package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.houseqm.po.PushStrategyAssignTime;
import com.longfor.longjian.houseqm.po.PushStrategyCategoryOverdue;
import com.longfor.longjian.houseqm.po.PushStrategyCategoryThreshold;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Houyan
 * @date 2018/12/13 0013 14:44
 */
@Data
@NoArgsConstructor
public class TaskPushStrategyVo implements Serializable {

    private Map<Integer,PushStrategyAssignTime> assignTimeMap;
    private Map<Integer,PushStrategyCategoryOverdue> categoryOverdueMap;
    private Map<Integer,PushStrategyCategoryThreshold> categoryThresholdMap;
}
