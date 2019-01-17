package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dongshun on 2019/1/17.
 */
@Data
@NoArgsConstructor
public class ApiTaskInfo {
   private Integer id;                  // 任务ID
   private String name;       // 任务名称
   private Integer category_cls; // 任务类型
}
