package com.longfor.longjian.houseqm.app.req.task;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.task
 * @ClassName: TaskListInfoReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 11:15
 */
@Data
@NoArgsConstructor
public class TaskListInfoReq implements Serializable {

    //ProjectId   int `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"`     // 项目ID
    //	CategoryCls int `json:"category_cls" zpf_reqd:"true" zpf_name:"category_cls"` // 模块类型
    //	Status      int `json:"status" zpf_reqd:"true" zpf_name:"status"`             // 任务状态
    //	Page        int `json:"page" zpf_name:"page"`                                 // 页码
    //	PageSize    int `json:"page_size" zpf_name:"page_size"`                       // 一页多少条
    @NotNull
    private Integer project_id;// 项目ID
    @NotNull
    private Integer category_cls;// 模块类型
    @NotNull
    private Integer status;// 任务状态
    private Integer page = 0;// 页码
    private Integer page_size; // 一页多少条

}
