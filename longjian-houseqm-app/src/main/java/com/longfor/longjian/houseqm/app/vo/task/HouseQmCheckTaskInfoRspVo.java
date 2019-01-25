package com.longfor.longjian.houseqm.app.vo.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.jcip.annotations.NotThreadSafe;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.task
 * @ClassName: HouseQmCheckTaskInfoRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 11:18
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskInfoRspVo implements Serializable {

    //ProjectId            int                    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"`                         // 项目ID
    //	TaskId               int                    `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`                               // 任务ID
    //	Name                 string                 `json:"name" zpf_reqd:"true" zpf_name:"name"`                                     // 任务名
    //	Status               int                    `json:"status" zpf_reqd:"true" zpf_name:"status"`                                 // 任务状态：1=未完成 2=已完成 3=已取消
    //	CategoryCls          int                    `json:"category_cls" zpf_reqd:"true" zpf_name:"category_cls"`                     // 模块类型 参数常量表
    //	RootCategoryKey      string                 `json:"root_category_key" zpf_reqd:"true" zpf_name:"root_category_key"`           // 任务类型的根节点
    //	AreaIds              string                 `json:"area_ids" zpf_reqd:"true" zpf_name:"area_ids"`                             // 区域ID(只含顶层区域ID)(多个ID用半角逗号分隔)
    //	AreaTypes            string                 `json:"area_types" zpf_reqd:"true" zpf_name:"area_types"`                         // 区域类型ID(楼层,户等)(多个ID用半角逗号分隔)
    private Integer project_id;
    private Integer task_id;
    private String name;
    private Integer status;
    private Integer category_cls;
    private String root_category_key;
    private String area_ids;
    private String area_types;
    //	PlanBeginOn          int                    `json:"plan_begin_on" zpf_reqd:"true" zpf_name:"plan_begin_on"`                   // 计划开始时间
    //	PlanEndOn            int                    `json:"plan_end_on" zpf_reqd:"true" zpf_name:"plan_end_on"`                       // 计划完成时间
    //	CreateAt             int                    `json:"create_at" zpf_reqd:"true" zpf_name:"create_at"`                           // 创建时间
    //	UpdateAt             int                    `json:"update_at" zpf_reqd:"true" zpf_name:"update_at"`                           // 更新时间
    //	DeleteAt             int                    `json:"delete_at" zpf_reqd:"true" zpf_name:"delete_at"`                           // 删除时间(0表示未删除)
    //	ExtraOps             *HouseQmCheckTaskExOps `json:"extra_ops" zpf_reqd:"true" zpf_name:"extra_ops"`                           // 额外操作
    //	IssueCount           int                    `json:"issue_count" zpf_reqd:"true" zpf_name:"issue_count"`                       // 问题数
    //	RecordCount          int                    `json:"record_count" zpf_reqd:"true" zpf_name:"record_count"`                     // 记录数
    //	IssueRecordedCount   int                    `json:"issue_recorded_count" zpf_reqd:"true" zpf_name:"issue_recorded_count"`     // 待指派问题数
    //	IssueAssignedCount   int                    `json:"issue_assigned_count" zpf_reqd:"true" zpf_name:"issue_assigned_count"`     // 待整改问题数
    //	IssueRepairedCount   int                    `json:"issue_repaired_count" zpf_reqd:"true" zpf_name:"issue_repaired_count"`     // 已整改问题数
    //	IssueApprovededCount int                    `json:"issue_approveded_count" zpf_reqd:"true" zpf_name:"issue_approveded_count"` // 已消项问题数
    private Integer plan_begin_on;
    private Integer plan_end_on;
    private Integer create_at;
    private Integer update_at;
    private Integer delete_at;
    private HouseQmCheckTaskExOps extra_ops;
    private Integer issue_count;
    private Integer record_count;
    private Integer issue_recorded_count;
    private Integer issue_assigned_count;
    private Integer issue_repaired_count;
    private Integer issue_approveded_count;


}
