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
  private   Integer house_count;      // 户数
    private  Integer issue_approveded_count;  // 已消项问题数
    private Integer issue_assigned_count;   // 待整改问题数
    private Integer issue_count;      // 问题数
    private Integer issue_recorded_count;    // 待指派问题数
    private Integer issue_repaired_count;    // 已整改问题数
    private Integer record_count ;      // 记录数
}
