package com.longfor.longjian.houseqm.app.req.houseqmstatistic;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstatistic
 * @ClassName: HouseqmStatisticRhyfTaskStatReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 14:34
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticRhyfTaskStatReq implements Serializable {

    //ProjectId int `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"` // 项目ID
    //	TaskId    int `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`       // 任务ID
    //	AreaId    int `json:"area_id" zpf_name:"area_id"`                       // 区域ID，留空或为0则表示所有
    //	Timestamp int `json:"timestamp" zpf_reqd:"true" zpf_name:"timestamp"`   // 上次更新时间
    @NotNull
    private Integer project_id;//项目ID
    @NotNull
    private Integer task_id;// 任务ID

    private Integer area_id=0;// 区域ID，留空或为0则表示所有
    @NotNull
    private Integer timestamp;// 上次更新时间


}
