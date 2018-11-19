package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class TaskRepairStatVo implements Serializable {

  private TaskRepairVo item;

  @Data
  @NoArgsConstructor
  public class TaskRepairVo implements Serializable {
    private String initime_finish;
    private Integer  initime_finish_count;
    private String initime_unfinish;
    private Integer  initime_unfinish_count;
    private String no_plan_end_on;
    private Integer no_plan_end_on_count;
    private String overtime_finish;
    private Integer  overtime_finish_count;
    private String   overtime_unfinish;
    private Integer overtime_unfinish_count;
  }


}
