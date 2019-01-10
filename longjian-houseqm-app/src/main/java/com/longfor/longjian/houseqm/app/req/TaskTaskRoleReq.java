package com.longfor.longjian.houseqm.app.req;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req
 * @ClassName: TaskTaskRoleReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/9 11:36
 */
@Data
@NoArgsConstructor
public class TaskTaskRoleReq implements Serializable {
//ProjectId int `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"` // 项目ID
//	TaskId    int `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`       // 任务ID
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;

}
