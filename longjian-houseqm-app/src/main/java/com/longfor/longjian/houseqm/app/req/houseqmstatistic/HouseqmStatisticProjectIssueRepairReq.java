package com.longfor.longjian.houseqm.app.req.houseqmstatistic;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.omg.CORBA.INTERNAL;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.req.houseqmstatistic
 * @ClassName: HouseqmStatisticProjectIssueRepairReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 14:37
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticProjectIssueRepairReq implements Serializable {

    //ProjectId int    `json:"project_id" zpf_reqd:"true" zpf_name:"project_id"` // 项目ID
    //	Source    string `json:"source" zpf_reqd:"true" zpf_name:"source"`         // app名称，如：ydyf、gcjc
    //	AreaId    int    `json:"area_id" zpf_name:"area_id"`                       // 区域id
    //	BeginOn   int    `json:"begin_on" zpf_name:"begin_on"`                     // 开始时间戳
    //	EndOn     int    `json:"end_on" zpf_name:"end_on"`                         // 结束时间戳
    //	Timestamp int    `json:"timestamp" zpf_reqd:"true" zpf_name:"timestamp"`   // 上次更新时间
    @NotNull
    private Integer project_id; // 项目ID
    @NotNull
    private String source;// app名称，如：ydyf、gcjc

    private Integer area_id;// 区域id
    private Integer begin_on;// 开始时间戳
    private Integer end_on;// 结束时间戳
    @NotNull
    private Integer timestamp; // 上次更新时间

}
