package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Jiazm
 * 2018/12/19 17:57
 */
@Data
@NoArgsConstructor
public class ApiHouseQmCheckTaskIssueLogDetailRspVo implements Serializable {
    private String title;                  // 问题标题
    private Integer area_id;                // 区域ID
    private Integer pos_x;                  // 在图纸上的位置X
    private Integer pos_y;                  // 在图纸上的位置Y
    private Integer typ;                   // 0 普通记录；1 问题记录
    private Integer plan_end_on;             // 计划整改完成时间
    private Integer end_on;                 // 实际整改完成时间
    private Integer repairer_id;            // 整改人负责人user_id
    private String repairer_follower_ids;    // 整改人跟进人user_ids
    private Integer condition;             // 问题严重程度  1 严重 2 较差 3 轻微
    private Integer category_cls;           // 任务模块
    private String category_key;            // 任务类型key
    private String check_item_key;           // 检查项key
    private Integer issue_reason;           // 产生问题的原因，需要检索project_setting表此project的PROJ_ISSUE_REASON_LIST记录
    private String issue_reason_detail;      // 产生问题原因的文字描述
    private String issue_suggest;           // 措施建议
    private String potential_risk;          // 潜在风险
    private String preventive_action_detail; // 预防措施
}
