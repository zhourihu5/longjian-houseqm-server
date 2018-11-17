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
public class AreaListVo implements Serializable {

  private List<AreaVo> result;

  @Data
  @NoArgsConstructor
  public class AreaVo implements Serializable {

    private int  id;
    private String  name;
    private int  fatherId;
  }
}
