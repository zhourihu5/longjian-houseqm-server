package com.longfor.longjian.houseqm.app.vo.houseqmstatistic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: HouseqmStatisticProjectIssueRepairRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 16:31
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticProjectIssueRepairRsp implements Serializable {

    private ApiHouseQmIssueRepairStat item;

    @Data
    @NoArgsConstructor
    public class ApiHouseQmIssueRepairStat {
        private String initime_finish;
        private Integer initime_finish_count;
        private String initime_unfinish;
        private Integer initime_unfinish_count;
        private String no_plan_end_on;
        private Integer no_plan_end_on_count;
        private String overtime_finish;
        private Integer overtime_finish_count;
        private String overtime_unfinish;
        private Integer overtime_unfinish_count;
    }

}
