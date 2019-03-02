package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Dongshun on 2018/12/20.
 */
@Data
@NoArgsConstructor
public class SimpleHouseQmCheckTaskIssueStatVo implements Serializable {
    private String CategoryKey;
    private String CategoryPathAndKey;
    private String CheckItemKey;
    private String CheckItemPathAndKey;
    private Integer Count;
}
