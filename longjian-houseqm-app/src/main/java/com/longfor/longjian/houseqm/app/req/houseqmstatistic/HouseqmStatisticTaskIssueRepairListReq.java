package com.longfor.longjian.houseqm.app.req.houseqmstatistic;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstatisticapp
 * @ClassName: HouseqmStatisticTaskIssueRepairListReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 14:10
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticTaskIssueRepairListReq implements Serializable {

    //ProjectId  int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"`
    //	TaskId     int    `json:"task_id" zpf_name:"task_id"`
    //	Source     string `json:"source" zpf_reqd:"true" zpf_name:"source"`
    //	AreaId     int    `json:"area_id" zpf_name:"area_id"`
    //	BeginOn    int    `json:"begin_on" zpf_name:"begin_on"`
    //	EndOn      int    `json:"end_on" zpf_name:"end_on"`
    //	PlanStatus int    `json:"plan_status" zpf_reqd:"true" zpf_name:"plan_status"`
    //	Timestamp  int    `json:"timestamp" zpf_reqd:"true" zpf_name:"timestamp"`
    //	Page       int    `json:"page" zpf_reqd:"true" zpf_name:"page"`
    //	PageSize   int    `json:"page_size" zpf_reqd:"true" zpf_name:"page_size"`
    @NotNull
    private Integer project_id;// 项目ID

    private Integer task_id; // 任务ID
    @NotNull
    private String source; // app名称，如：ydyf、gcjc
    private Integer area_id; // 区域id
    private Integer begin_on;// 开始时间戳
    private Integer end_on;// 结束时间戳

    @NotNull
    private Integer plan_status;// 计划状态，（1、按期完成 2、未超期未完成 3、未设置期限 4、超期完成 5、超期未完成 ）
    @NotNull
    private Integer timestamp;// 上次更新时间
    @NotNull
    private Integer page;// 第几页
    @NotNull
    private Integer page_size;// 每页多少条数据


}
