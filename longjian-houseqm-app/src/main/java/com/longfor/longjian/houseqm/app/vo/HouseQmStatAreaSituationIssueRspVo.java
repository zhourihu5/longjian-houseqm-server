package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmStatAreaSituationIssueRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 15:37
 */
@Data
@NoArgsConstructor
public class HouseQmStatAreaSituationIssueRspVo implements Serializable {

    //IssueApprovededCount int `json:"issue_approveded_count" zpf_reqd:"true" zpf_name:"issue_approveded_count"` // 已消项问题数
    //	IssueAssignedCount   int `json:"issue_assigned_count" zpf_reqd:"true" zpf_name:"issue_assigned_count"`     // 待整改问题数
    //	IssueCount           int `json:"issue_count" zpf_reqd:"true" zpf_name:"issue_count"`                       // 问题数
    //	IssueRecordedCount   int `json:"issue_recorded_count" zpf_reqd:"true" zpf_name:"issue_recorded_count"`     // 待指派问题数
    //	IssueRepairedCount   int `json:"issue_repaired_count" zpf_reqd:"true" zpf_name:"issue_repaired_count"`     // 待消项问题数
    //	RecordCount          int `json:"record_count" zpf_reqd:"true" zpf_name:"record_count"`                     // 记录数
    private Integer issue_approveded_count;
    private Integer issue_assigned_count;
    private Integer issue_count;

    private Integer issue_recorded_count;
    private Integer issue_repaired_count;
    private Integer record_count;

}
