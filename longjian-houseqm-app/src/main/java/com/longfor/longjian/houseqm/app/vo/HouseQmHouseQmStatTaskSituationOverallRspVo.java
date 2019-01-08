package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmHouseQmStatTaskSituationOverallRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/8 16:48
 */
@Data
@NoArgsConstructor
public class HouseQmHouseQmStatTaskSituationOverallRspVo implements Serializable {

    private Integer accept_has_issue_count;// 已查验中有问题的户数
    private Integer accept_has_issue_sign_count;// 已查验中有问题并签字的户数
    private Integer accept_no_issue; // 已查验中无问题的户数
    private Integer accept_no_issue_sign_count; // 已查验中无问题并签字的户数
    private Integer checked_count; // 已查验户数
    private String checked_rate;  // 查验率
    private Integer only_watch; // 仅看房
    private Integer reject_count; // 拒绝收房
    private String task_name;// 任务名
    private Integer total; // 总户数
    private Integer unaccept_count;// 未查验户数
    private Integer unchecked_count;// 未查验户数

}
