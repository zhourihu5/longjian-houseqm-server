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
public class AreaTaskListVo implements Serializable {

  private List<AreaTaskVo> tasks;

  @Data
  @NoArgsConstructor
  public class AreaTaskVo implements Serializable {

    private Integer  id;
    private String  name;
    private String  category_cls;
  }
}
