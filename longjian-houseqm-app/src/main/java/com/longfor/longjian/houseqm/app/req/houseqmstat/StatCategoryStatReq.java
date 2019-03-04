package com.longfor.longjian.houseqm.app.req.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstat
 * @ClassName: StatCategoryStatReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 10:52
 */
@Data
@NoArgsConstructor
public class StatCategoryStatReq implements Serializable {

    //ProjectId int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"` // 项目ID
    //	TaskId    int    `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`       // 任务ID
    //	AreaId    int    `json:"area_id" zpf_name:"area_id"`                       // 区域ID
    //	BeginOn   string `json:"begin_on" zpf_name:"begin_on"`                     // 开始时间
    //	EndOn     string `json:"end_on" zpf_name:"end_on"`                         // 结束时间
    @NotNull
    private Integer project_id;
    @NotNull
    private Integer task_id;
    private Integer area_id = 0;
    private String begin_on = "";
    private String end_on = "";
}
