package com.longfor.longjian.houseqm.app.vo;

import com.sun.javafx.collections.MappingChange;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public class TaskList {
        List<HouseQmCheckTaskSimpleRspVo> task_list;
    }
}
