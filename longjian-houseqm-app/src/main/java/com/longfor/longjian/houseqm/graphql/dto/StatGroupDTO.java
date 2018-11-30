package com.longfor.longjian.houseqm.graphql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author lipeishuai
 * @date 2018/11/29 15:07
 */

@Data
@AllArgsConstructor
public class StatGroupDTO {

    private int totalAcreage;
    private int issueCount;
    private int year;
    private String timeFrameType;
    private String beginOn;
}
