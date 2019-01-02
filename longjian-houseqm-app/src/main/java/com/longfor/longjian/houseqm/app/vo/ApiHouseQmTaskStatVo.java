package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/29.
 */
@Data
@NoArgsConstructor
public class ApiHouseQmTaskStatVo implements Serializable {
  private   Integer HouseCount;      // 户数
    private  Integer IssueApprovededCount;  // 已消项问题数
    private Integer IssueAssignedCount;   // 待整改问题数
    private Integer IssueCount;      // 问题数
    private Integer IssueRecordedCount;    // 待指派问题数
    private Integer IssueRepairedCount;    // 已整改问题数
    private Integer RecordCount ;      // 记录数
}
