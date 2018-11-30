package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class StatDataVo implements Serializable {

    private int totalAcreage;
    private int issueCount;
    private int issueNewCount;
    private int issueNoteNoAssignCount;
    private int issueOverdueToAssignCount;
    private int issueOverdueToReformCount;
    private int issueOverdueToCheckCount;
    private int issueOverdueCheckedCount;
    private int issueIntimeToAssignCount;
    private int issueIntimeToReformCount;
    private int issueIntimeToCheckCount;
    private int issueIntimeCheckedCount;
    private int issueNotsetToAssignCount;
    private int issueNotsetToReformCount;
    private int issueNotsetToCheckCount;
    private int issueNotsetCheckedCount;
    private int year;
    private int timeFrameIdx;
    private String timeFrameType;
    private String beginOn;
    private String endOn;
}
