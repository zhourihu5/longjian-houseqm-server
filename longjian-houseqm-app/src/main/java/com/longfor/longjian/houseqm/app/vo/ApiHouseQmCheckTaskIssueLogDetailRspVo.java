package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

/**
 * Jiazm
 * 2018/12/19 17:57
 */
@Data
public class ApiHouseQmCheckTaskIssueLogDetailRspVo {
    private String Title;                  // 问题标题
    private Integer AreaId;                // 区域ID
    private Integer PosX;                  // 在图纸上的位置X
    private Integer PosY;                  // 在图纸上的位置Y
    private Integer Typ;                   // 0 普通记录；1 问题记录
    private Integer PlanEndOn;             // 计划整改完成时间
    private Integer EndOn;                 // 实际整改完成时间
    private Integer RepairerId;            // 整改人负责人user_id
    private String RepairerFollowerIds;    // 整改人跟进人user_ids
    private Integer Condition;             // 问题严重程度  1 严重 2 较差 3 轻微
    private Integer CategoryCls;           // 任务模块
    private String CategoryKey;            // 任务类型key
    private String CheckItemKey;           // 检查项key
    private Integer IssueReason;           // 产生问题的原因，需要检索project_setting表此project的PROJ_ISSUE_REASON_LIST记录
    private String IssueReasonDetail;      // 产生问题原因的文字描述
    private String IssueSuggest;           // 措施建议
    private String PotentialRisk;          // 潜在风险
    private String PreventiveActionDetail; // 预防措施
}
