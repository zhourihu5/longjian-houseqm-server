package com.longfor.longjian.houseqm.app.vo.houseqmstatistic;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstatistic
 * @ClassName: ApiHouseQmRhyfTaskHouseStatVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/22 14:37
 */
@Data
@NoArgsConstructor
public class ApiHouseQmRhyfTaskHouseStatVo implements Serializable {

    private Integer house_count; // 总户数
    private Integer checked_count; // 查验户数
    private Integer has_issue_count;// 有问题户数
    private Integer repaired_count;// 已整改户数
    private Integer approved_count;// 已销项户数
    private Integer repair_confirm_count;// 维修确认户数

    private Integer accept_has_issue_count;// 已收楼中有问题的户数
    private Integer accept_no_issue_count; // 已收楼中无问题的户数
    private Integer accept_approved_count;// 已收楼且所有问题已销项的户数（维修确认的分母
    private Integer only_watch_count;// 仅看房
    private Integer reject_count;// 拒绝收房数

    private String house_checked_percent;// 户查验进度
    private String house_repaired_percent; // 户整改进度
    private String house_approveded_percent;// 户销项进度
    private String house_repair_confirm_percent;// 户维修确认进度

}
