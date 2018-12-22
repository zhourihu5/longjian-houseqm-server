package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/20.
 */
@Data
@NoArgsConstructor
public class HouseQmIssueCategoryStatVo implements Serializable {
    private String Key;
    private String ParentKey;
    private Integer IssueCount;
    private String Name;

}
