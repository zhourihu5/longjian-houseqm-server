package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/29.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskHouseStatInfoVo  implements Serializable{

   private int HouseCount;
    private int CheckedCount;
    private int HasIssueCount;
    private int RepairedCount;
    private int ApprovedCount ;
    private int RepairConfirmCount;
    private int AcceptHasIssueCount;
    private int AcceptNoIssueCount;
    private  int OnlyWatchCount ;
    private int RejectCount;
    private int AcceptApprovedCount;
}
