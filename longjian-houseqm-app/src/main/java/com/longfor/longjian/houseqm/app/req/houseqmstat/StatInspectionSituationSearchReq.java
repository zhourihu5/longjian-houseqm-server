package com.longfor.longjian.houseqm.app.req.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstat
 * @ClassName: StatInspectionSituationSearchReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 10:42
 */
@Data
@NoArgsConstructor
public class StatInspectionSituationSearchReq implements Serializable {

    //ProjectId   int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"` // 项目ID
    //	TaskId      int    `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`       // 任务ID
    //	AreaId      int    `json:"area_id" zpf_name:"area_id"`                       // 区域ID
    //	IssueStatus int    `json:"issue_status" zpf_name:"issue_status"`             // 问题状态
    //	Status      int    `json:"status" zpf_name:"status"`                         // 户状态
    //	StartTime   string `json:"start_time" zpf_name:"start_time"`                 // 开始时间
    //	EndTime     string `json:"end_time" zpf_name:"end_time"`                     // 结束时间
    //	Page        int    `json:"page" zpf_reqd:"true" zpf_name:"page"`             // 页码
    //	PageSize    int    `json:"page_size" zpf_reqd:"true" zpf_name:"page_size"`   // 一页多少条
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;
    private Integer area_id = 0;
    private Integer issue_status = 0;
    private Integer status = 0;
    private String start_time = "";
    private String end_time = "";
    @NotNull
    private Integer page;
    @NotNull
    private Integer page_size;
}
