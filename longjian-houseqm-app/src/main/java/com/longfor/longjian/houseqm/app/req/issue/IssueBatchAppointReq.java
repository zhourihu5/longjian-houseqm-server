package com.longfor.longjian.houseqm.app.req.issue;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.issue
 * @ClassName: IssueBatchAppointReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 13:57
 */
@Data
@NoArgsConstructor
public class IssueBatchAppointReq implements Serializable {

    //ProjectId         int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"`   // 项目ID
    //	TaskId            int    `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`         // 任务ID
    //	IssueUuids        string `json:"issue_uuids" zpf_reqd:"true" zpf_name:"issue_uuids"` // issue的UUID,多个用半角逗号“,”分隔
    //	PlanEndOn         int    `json:"plan_end_on" zpf_name:"plan_end_on"`                 // 计划整改完成时间
    //	RepairerId        int    `json:"repairer_id" zpf_name:"repairer_id"`                 // 整改负责人ID
    //	RepairFollowerIds string `json:"repair_follower_ids" zpf_name:"repair_follower_ids"` // 整改跟进人IDs
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;
    @NotNull
    private String issue_uuids;

    private Integer plan_end_on;
    private Integer repairer_id;
    private String repair_follower_ids;

}
