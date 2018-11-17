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
public class IssueActionVo implements Serializable {

  private GroupVo group;
  private List<ProjAreaVo> items;

  @Data
  @NoArgsConstructor
  public class GroupVo implements Serializable {

    private int projArea;
    private int issueCreatedCount;
    private int issueReformedCount;
    private int issueCheckedCount;
    private int actionCount;
    private int activeUserTotal;
    private int  userTotal;
    private int activeUserRate;
    private int actionCountPerUser;
    private int groupId;
    private String groupName;
  }

  @Data
  @NoArgsConstructor
  public class ProjAreaVo implements Serializable{

    private int projArea;
    private int issueCreatedCount;
    private int issueReformedCount;
    private int issueCheckedCount;
    private int actionCount;
    private int activeUserTotal;
    private int userTotal;
    private int activeUserRate;
    private int actionCountPerUser;
    private int groupId;
    private int groupName;
    private int teamId;
    private String teamName;
    private int projId;
    private String projName;
  }

}
