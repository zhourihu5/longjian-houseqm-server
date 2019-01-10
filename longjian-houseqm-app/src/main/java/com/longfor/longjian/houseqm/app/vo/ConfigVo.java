package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Data
@NoArgsConstructor
public class ConfigVo {
    private ApiPushStrategyAssignTime config_assign_time;
    private ApiPushStrategyCategoryOverdue config_category_overdue;
    private  ApiPushStrategyCategoryThreshold config_category_threshold;

    @Data
    @NoArgsConstructor
    public class ApiPushStrategyAssignTime{
        private String push_time="";
        private  String user_ids="";
    }

    @Data
    @NoArgsConstructor
    public class ApiPushStrategyCategoryOverdue{
        private String category_keys="";
        private  String user_ids="";
    }

    @Data
    @NoArgsConstructor
    public class ApiPushStrategyCategoryThreshold{
        private String category_keys="";
        private  String user_ids="";
        private Integer threshold=0;
    }
}
