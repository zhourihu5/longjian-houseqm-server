package com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ApiHouseQmRepairerStatVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 12:14
 */
@Data
@NoArgsConstructor
public class ApiHouseQmRepairerStatVo implements Serializable {

    //user_id                = optional.IntegerField(default=0, desc='用户id')
    //    real_name              = optional.StringField(default='', desc='真名')
    //    issue_assigned_count   = optional.IntegerField(default=0, desc='被分配问题数')
    //    issue_repaired_count   = optional.IntegerField(default=0, desc='整改问题数')
    //    issue_approveded_count = optional.IntegerField(default=0, desc='销项问题数')
    private Integer user_id=0;//用户id
    private String real_name="";//真名
    private Integer issue_assigned_count=0;//被分配问题数
    private Integer issue_repaired_count=0;//整改问题数
    private Integer issue_approveded_count=0;//销项问题数

}
