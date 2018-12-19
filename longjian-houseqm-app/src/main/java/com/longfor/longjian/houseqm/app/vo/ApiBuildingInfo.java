package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/18.
 */
@Data
@NoArgsConstructor
public class ApiBuildingInfo implements Serializable{
  private Integer  Id; // 楼栋ID
   private String  Name ;// 楼栋名称
}
