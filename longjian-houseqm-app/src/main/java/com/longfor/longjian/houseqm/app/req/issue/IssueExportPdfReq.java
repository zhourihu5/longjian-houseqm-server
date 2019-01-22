package com.longfor.longjian.houseqm.app.req.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmissue
 * @ClassName: IssueExportPdfReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 17:07
 */
@Data
@NoArgsConstructor
public class IssueExportPdfReq implements Serializable {

    //CategoryCls   int    `json:"category_cls" zpf_reqd:"true" zpf_name:"category_cls"` // 模块类型
    //	ProjectId     int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"`     // 项目ID
    //	TaskId        int    `json:"task_id" zpf_name:"task_id"`                           // 任务ID
    //	CategoryKey   string `json:"category_key" zpf_name:"category_key"`                 // 任务类型
    //	CheckItemKey  string `json:"check_item_key" zpf_name:"check_item_key"`             // 检查项类型
    //	AreaIds       []int  `json:"area_ids" zpf_name:"area_ids"`                         // 区域ID
    //	StatusIn      []int  `json:"status_in" zpf_name:"status_in"`                       // 问题状态
    @NotNull
    private Integer category_cls;
    @NotNull
    private Integer project_id;
    private Integer task_id;
    private String category_key;
    private String  check_item_key;
    private List<Integer> area_ids;
    private List<Integer> status_in;

    //	CheckerId     int    `json:"checker_id" zpf_name:"checker_id"`                     // 检查人ID
    //	RepairerId    int    `json:"repairer_id" zpf_name:"repairer_id"`                   // 整改人ID
    //	Type          int    `json:"type" zpf_name:"type"`                                 // 问题类型
    //	Condition     int    `json:"condition" zpf_name:"condition"`                       // 严重程度
    //	CreateOnBegin string `json:"create_on_begin" zpf_name:"create_on_begin"`           // 开始时间范围
    //	CreateOnEnd   string `json:"create_on_end" zpf_name:"create_on_end"`               // 结束时间范围
    //	IsOverdue     bool   `json:"is_overdue" zpf_name:"is_overdue"`                     // 是否超期
    //	Uuids         string `json:"uuids" zpf_name:"uuids"`                               // 问题uuid记录列表
    private Integer checker_id;
    private Integer repairer_id;
    private Integer type;
    private Integer condition;
    private String create_on_begin;
    private String create_on_end;
    private Boolean is_overdue;
    private String uuids;


}
