package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/17.
 */
@Data
@NoArgsConstructor
public class HouseQmTaskHouseStatVo implements Serializable {
    private Integer house_count; // 总户数
    private Integer checked_count; // 查验户数
    private Integer has_issue_count;  // 查验有问题户数
    private Integer repaired_count;// 已整改户数
    private Integer approved_count;// 已销项户数
    private String house_checked_percent; // 户查验进度
    private String house_repaired_percent;// 户整改进度
    private String house_approveded_percent;// 户销项进度
}
