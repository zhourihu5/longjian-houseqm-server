package com.longfor.longjian.houseqm.app.req.v3houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.v3houseqmstat
 * @ClassName: InspectionSituationExportReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/2/19 16:49
 */
@Data
@NoArgsConstructor
public class InspectionSituationExportReq implements Serializable {

    //ProjectId   int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"`
    //		TaskId      int    `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`
    //		AreaId      int    `json:"area_id" zpf_name:"area_id"`
    //		IssueStatus int    `json:"issue_status" zpf_name:"issue_status"`
    //		Status      int    `json:"status" zpf_name:"status"`
    //		StartTime   string `json:"start_time" zpf_name:"start_time"`
    //		EndTime     string `json:"end_time" zpf_name:"end_time"`
    //		AreaIds     string `json:"area_ids" zpf_name:"area_ids"`
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;
    private Integer area_id = 0;
    private Integer issue_status = 0;
    private Integer status = 0;
    private String start_time = "";
    private String end_time = "";
    private String area_ids = "";

}
