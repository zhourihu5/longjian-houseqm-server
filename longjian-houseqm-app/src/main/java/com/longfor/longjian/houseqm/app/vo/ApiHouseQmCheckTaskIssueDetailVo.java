package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

/**
 * Jiazm
 * 2018/12/20 10:56
 */
@Data
public class ApiHouseQmCheckTaskIssueDetailVo {
    private Integer IssueReason; // 产生问题的原因，需要检索project_setting表此project的PROJ_ISSUE_REASON_LIST记录
    private String  IssueReasonDetail; // 产生问题原因的文字描述
    private  String IssueSuggest;     // 措施建议
    private String  PotentialRisk; // 潜在风险
    private String PreventiveActionDetail; //预防措施
}
