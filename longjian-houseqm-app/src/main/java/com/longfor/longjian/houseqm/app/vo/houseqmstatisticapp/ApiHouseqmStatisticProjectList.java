package com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ApiHouseqmStatisticProjectList
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/21 18:02
 */
@Data
@NoArgsConstructor
public class ApiHouseqmStatisticProjectList implements Serializable {

    // id        = optional.IntegerField(default=0, desc='项目ID')
    //    name      = optional.StringField(default='', desc='项目名称')
    //    team_id   = optional.IntegerField(default=0, desc='公司ID')
    //    team_name = optional.StringField(default='', desc='公司名称')
    private Integer id = 0;//项目ID
    private String name = "";//项目名称
    private Integer team_id = 0;//公司ID
    private String team_name = "";//公司名称
}
