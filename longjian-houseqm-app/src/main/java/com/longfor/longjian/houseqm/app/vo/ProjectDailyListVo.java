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

  private List<ProjectDailyVo> items;
  private Integer total;

  @Data
  @NoArgsConstructor
  public class ProjectDailyVo implements Serializable {

    private Integer  checked_count;
    private Integer  issue_count;
    private String date;
    private Integer records_count;
    private Integer total_checked_count;
  }
}
