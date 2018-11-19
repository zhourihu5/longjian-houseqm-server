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
public class TaskStatVo implements Serializable {

  private HouseStatVo house;
  private IssueStatVo issue;

  @Data
  @NoArgsConstructor
  public class HouseStatVo implements Serializable {

    private Integer  approved_count;
    private Integer  has_issue_count;
    private Integer checked_count;
    private Integer  house_count;
    private Integer  repaired_count;
    private String house_checked_percent;
    private String house_repaired_percent;
    private String  house_approveded_percent;
  }

  @Data
  @NoArgsConstructor
  public class IssueStatVo implements Serializable {


    private Integer  issue_approveded_count;
    private Integer issue_assigned_count;
    private Integer issue_count;
    private Integer  issue_recorded_count;
    private Integer  issue_repaired_count;
    private Integer record_count;
  }
}
