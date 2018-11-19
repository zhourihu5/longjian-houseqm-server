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
public class TaskAreaListVo implements Serializable {

  private List<TaskAreaVo> areas;

  @Data
  @NoArgsConstructor
  public class TaskAreaVo implements Serializable {

    private Integer  id;
    private Integer  father_id;
    private String name;
    private Integer typ;
    private String path;
  }
}
