package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Dongshun on 2019/1/14.
 */
@Data
@NoArgsConstructor
public class ApiBuildingQmTaskMemberInsertVo {
   private Integer squad_id =0;
    private Integer group_role =0;
    private Integer user_id=0;
    private Integer  can_approve=0;
    private Integer  can_direct_approve=0;
    private Integer can_reassign=0 ;
}
