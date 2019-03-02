package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Data
@NoArgsConstructor
public class ConfigVo  implements Serializable {
    private ApiPushStrategyAssignTime config_assign_time;
    private ApiPushStrategyCategoryOverdue config_category_overdue;
    private  ApiPushStrategyCategoryThreshold config_category_threshold;

    @Data
    @NoArgsConstructor
    public class ApiPushStrategyAssignTime implements Serializable{
        private String push_time="";
        private  String user_ids="";
    }

    @Data
    @NoArgsConstructor
    public class ApiPushStrategyCategoryOverdue implements Serializable{
        private String category_keys="";
        private  String user_ids="";
    }

    @Data
    @NoArgsConstructor
    public class ApiPushStrategyCategoryThreshold implements Serializable{
        private String category_keys="";
        private  String user_ids="";
        private Integer threshold=0;
    }
}
