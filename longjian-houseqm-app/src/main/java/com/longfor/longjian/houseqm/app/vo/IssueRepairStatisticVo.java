package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/19.
 */
@Data
@NoArgsConstructor
public class IssueRepairStatisticVo implements Serializable {
    private String no_plan_end_on;
    private String overtime_unfinish;
    private String initime_unfinish;
    private String overtime_finish;
    private String initime_finish;

    private Integer total_count;
    private Integer no_plan_end_on_count;
    private Integer overtime_unfinish_count;
    private Integer initime_unfinish_count;
    private Integer overtime_finish_count;
    private Integer initime_finish_count;
}
