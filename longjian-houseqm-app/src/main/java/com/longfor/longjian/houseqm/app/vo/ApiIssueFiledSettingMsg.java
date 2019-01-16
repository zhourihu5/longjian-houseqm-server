package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dongshun on 2019/1/16.
 */
@Data
@NoArgsConstructor
public class ApiIssueFiledSettingMsg {
    private Integer  project_id=0;
    private Integer  field_id=0;
    private String name="";
    private String alias="";
    private Integer display_status=0;
    private Integer required_status=0;
    private Integer alias_status=0;
}
