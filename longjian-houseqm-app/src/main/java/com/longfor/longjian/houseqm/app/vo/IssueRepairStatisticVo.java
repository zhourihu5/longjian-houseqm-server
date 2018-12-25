package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/19.
 */
@Data
@NoArgsConstructor
public class IssueRepairStatisticVo implements Serializable {
   private   String NoPlanEndOn;
    private  String OvertimeUnfinish;
    private  String InitimeUnfinish;
    private  String OvertimeFinish ;
    private  String InitimeFinish ;

    private  Integer TotalCount  ;
    private Integer NoPlanEndOnCount  ;
    private  Integer OvertimeUnfinishCount ;
    private  Integer InitimeUnfinishCount  ;
    private  Integer OvertimeFinishCount  ;
    private  Integer InitimeFinishCount  ;
}
