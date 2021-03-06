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
public class ProjectDailyListVo implements Serializable {

    private Integer total;
    private List<ProjectDailyVo> items;

    @Data
    @NoArgsConstructor
    public class ProjectDailyVo implements Serializable {

        private Integer checked_count;
        private String date;
        private Integer issue_count;
        private Integer records_count;
        private Integer total_checked_count;
    }
}
