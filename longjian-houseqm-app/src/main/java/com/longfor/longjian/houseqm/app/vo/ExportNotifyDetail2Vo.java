package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2019/1/11.
 */
@Data
@NoArgsConstructor
public class ExportNotifyDetail2Vo implements Serializable {
   private Integer issue_id=0;
   private String content="";
    private String issue_suggest="";

}
