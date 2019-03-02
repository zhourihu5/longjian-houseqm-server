package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class ProjectOveralListVo implements Serializable {

    private List<ProjectOveralVo> items;

    @Data
    @NoArgsConstructor
    public class ProjectOveralVo implements Serializable {

        private Integer checked_count;
        private Integer issue_count;
        private String task_name;
        private Integer records_count;
    }
}
