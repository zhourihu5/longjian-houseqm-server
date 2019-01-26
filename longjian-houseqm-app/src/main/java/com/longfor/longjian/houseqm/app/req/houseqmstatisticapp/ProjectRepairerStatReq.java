package com.longfor.longjian.houseqm.app.req.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ProjectRepairerStatReq
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 17:56
 */
@Data
@NoArgsConstructor
public class ProjectRepairerStatReq implements Serializable {
    // project_id=required.IntegerField(desc='项目ID'),
    //            source=required.StringField(desc='app名称，如：ydyf、gcgl'),
    //            task_id=optional.IntegerField(default=0, desc='任务ID(如果只统计项目数据，则不传或者传0)'),
    //            stat_begin=optional.IntegerField(default=0, desc='统计起始时间戳(单位秒)'),
    //            stat_end=optional.IntegerField(default=0, desc='统计结束时间戳(单位秒)'),
    //            timestamp=optional.IntegerField(default=0, desc='上次更新时间'),
    @NotNull
    private Integer project_id;//项目ID
    @NotNull
    private String source;//app名称，如：ydyf、gcgl

    private Integer task_id=0;//任务ID(如果只统计项目数据，则不传或者传0)
    private Integer stat_begin=0;//统计起始时间戳(单位秒)
    private Integer stat_end=0;//统计结束时间戳(单位秒)
    private Integer timestamp=0;//上次更新时间

}
