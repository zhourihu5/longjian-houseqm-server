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
    private String TaskName;
    private Integer Total;
    private Integer CheckedCount;
    private Integer UncheckedCount;
    private String CheckedRate;
    private Integer AcceptCount;
    private Integer UnacceptCount;
    private Integer HasIssueCount;
    private Integer NoIssueCount;
    private Integer AcceptHasIssueCount;
    private Integer AcceptNoIssueCount;
    private Integer AcceptHasIssueSignCount;
    private Integer AcceptNoIssueSignCount;
    private Integer RejectCount;
    private Integer OnlyWatch;
    private Integer RepairConfirmCount;
    private Integer AcceptApprovedCount;
}
