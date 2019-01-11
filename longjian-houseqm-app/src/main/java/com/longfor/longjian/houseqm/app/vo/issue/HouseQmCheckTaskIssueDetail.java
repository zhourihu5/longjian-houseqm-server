package com.longfor.longjian.houseqm.app.vo.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issue
 * @ClassName: HouseQmCheckTaskIssueDetail
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 18:12
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueDetail implements Serializable {

    private Integer IssueReason; // 产生问题的原因，需要检索project_setting表此project的PROJ_ISSUE_REASON_LIST记录
    private String  IssueReasonDetail; // 产生问题原因的文字描述
    private  String IssueSuggest;     // 措施建议
    private String  PotentialRisk; // 潜在风险
    private String PreventiveActionDetail; //预防措施
}
