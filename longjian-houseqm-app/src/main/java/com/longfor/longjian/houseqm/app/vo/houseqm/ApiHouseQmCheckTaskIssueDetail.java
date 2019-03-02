package com.longfor.longjian.houseqm.app.vo.houseqm;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqm
 * @ClassName: ApiHouseQmCheckTaskIssueDetail
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 15:30
 */
@Data
@NoArgsConstructor
public class ApiHouseQmCheckTaskIssueDetail implements Serializable {

    private Integer issue_reason; // 产生问题的原因，需要检索project_setting表此project的PROJ_ISSUE_REASON_LIST记录
    private String issue_reason_detail; // 产生问题原因的文字描述
    private String issue_suggest;     // 措施建议
    private String potential_risk; // 潜在风险
    private String preventive_action_detail; //预防措施
}