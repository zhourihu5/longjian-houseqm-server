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
    private Integer HouseCount; // 总户数
    private Integer CheckedCount; // 查验户数
    private Integer HasIssueCount;  // 查验有问题户数
    private Integer RepairedCount;// 已整改户数
    private Integer ApprovedCount;// 已销项户数
    private String HouseCheckedPercent; // 户查验进度
    private String HouseRepairedPercent;// 户整改进度
    private String HouseApprovededPercent;// 户销项进度
}
