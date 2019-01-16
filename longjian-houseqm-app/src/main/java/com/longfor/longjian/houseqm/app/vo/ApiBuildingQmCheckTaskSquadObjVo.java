package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Data
@NoArgsConstructor
public class ApiBuildingQmCheckTaskSquadObjVo {
    private Integer id=0;
    private String name="";
    private String user_ids="" ;
    private String approve_ids="" ;
    private String direct_approve_ids="";
    private String reassign_ids="";
}
