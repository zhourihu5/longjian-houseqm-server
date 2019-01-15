package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: StatTaskSituationRepairStatRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/15 15:16
 */
@Data
@NoArgsConstructor
public class StatTaskSituationRepairStatRsp implements Serializable {

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
