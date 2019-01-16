package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/8.
 */
@Data
@NoArgsConstructor
public class ApiBuildingQmTaskMemberGroupVo {
    private Integer group_id=0;
    private String group_name="";
    private Integer group_role=0 ;
    private List<Integer> user_ids ;
    private List<Integer> approve_ids;
    private List<Integer> direct_approve_ids;
    private List<Integer> reassign_ids;

}
