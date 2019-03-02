package com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatisticapp
 * @ClassName: ApiHouseQmCheckerStatVo
 * @Description: 统计信息
 * @Author: hy
 * @CreateDate: 2019/1/22 10:25
 */
@Data
@NoArgsConstructor
public class ApiHouseQmCheckerStatVo implements Serializable {

    private Integer issue_approveded_count = 0;//销项问题数
    private Integer issue_count = 0;//提交问题数
    private String real_name = "";//真名
    private Integer records_count = 0;//提交记录数
    private Integer user_id = 0;//用户id
}
