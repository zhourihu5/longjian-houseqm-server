package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/29.
 */
@Data
@NoArgsConstructor
public class RepossessionTasksStatusInfoVo implements Serializable {
 private   String TaskName;
    private  int Total;
    private int  CheckedCount ;
    private int UncheckedCount;
    private String CheckedRate;
    private int AcceptCount ;
    private  int UnacceptCount  ;
    private int HasIssueCount;
    private int NoIssueCount;
    private int  AcceptHasIssueCount;
    private int AcceptNoIssueCount ;
    private int AcceptHasIssueSignCount;
    private int AcceptNoIssueSignCount;
    private int RejectCount ;
    private int OnlyWatch ;
    private int RepairConfirmCount;
    private int AcceptApprovedCount;
}
