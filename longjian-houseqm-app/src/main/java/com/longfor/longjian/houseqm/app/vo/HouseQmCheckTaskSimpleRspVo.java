package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/14.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskSimpleRspVo implements Serializable {
    private Integer  project_id;
    private Integer task_id;
    private String name;
    private List<Integer> area_types;
    private Integer plan_begin_on;
    private Integer plan_end_on;
    private Integer create_at;
    @Data
    @NoArgsConstructor
    public class TaskList implements Serializable{
        List<HouseQmCheckTaskSimpleRspVo> task_list;
    }
}
