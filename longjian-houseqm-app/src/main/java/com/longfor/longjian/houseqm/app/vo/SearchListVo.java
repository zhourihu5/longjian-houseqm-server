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
public class SearchListVo implements Serializable {

  private List<SearchVo> items;

  @Data
  @NoArgsConstructor
  public class SearchVo implements Serializable {

    private int id;
    private String  name;
    private int  team_id;
    private int status;
    private int update_at;
  }
}
