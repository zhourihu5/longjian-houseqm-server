package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/29.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskHouseStatInfoVo implements Serializable {

    private Integer HouseCount;
    private Integer CheckedCount;
    private Integer HasIssueCount;
    private Integer RepairedCount;
    private Integer ApprovedCount;
    private Integer RepairConfirmCount;
    private Integer AcceptHasIssueCount;
    private Integer AcceptNoIssueCount;
    private Integer OnlyWatchCount;
    private Integer RejectCount;
    private Integer AcceptApprovedCount;
}
