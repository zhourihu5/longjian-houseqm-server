package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req
 * @ClassName: StatTaskSituationMembersCheckerReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/9 14:44
 */
@Data
@NoArgsConstructor
public class StatTaskSituationMembersCheckerReq implements Serializable {

    //ProjectId int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"` // 项目id
    //	TaskId    int    `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`       // 任务ID
    //	BeginOn   string `json:"begin_on" zpf_name:"begin_on"`                     // 起始时间
    //	EndOn     string `json:"end_on" zpf_name:"end_on"`                         // 结束时间

    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;
    private String begin_on = "";
    private String end_on = "";

}
