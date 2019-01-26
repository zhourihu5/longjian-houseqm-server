package com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ApiHouseQmProjectStatVo
 * @Description: 汇总信息
 * @Author: hy
 * @CreateDate: 2019/1/21 20:27
 */
@Data
@NoArgsConstructor
public class ApiHouseQmProjectStatVo implements Serializable {

    // task_count             = optional.IntegerField(default=0, desc='任务数')
    //    issue_approveded_count = optional.IntegerField(default=0, desc='已消项问题数')
    //    issue_assigned_count   = optional.IntegerField(default=0, desc='待整改问题数')
    //    issue_count            = optional.IntegerField(default=0, desc='问题数')
    //    issue_recorded_count   = optional.IntegerField(default=0, desc='待指派问题数')
    //    issue_repaired_count   = optional.IntegerField(default=0, desc='已整改问题数')
    //    record_count           = optional.IntegerField(default=0, desc='记录数')

    private Integer task_count;
    private Integer issue_approveded_count;
    private Integer issue_assigned_count;
    private Integer issue_count;
    private Integer issue_recorded_count;
    private Integer issue_repaired_count;
    private Integer record_count;

}
