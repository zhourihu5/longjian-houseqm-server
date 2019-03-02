package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/11.
 */
@Data
@NoArgsConstructor
public class ProjectSettingConfigVo implements Serializable {
    private Boolean has_issue_reason;
    private Boolean has_issue_suggest;
    private Boolean has_issue_potential_rist;
    private Boolean has_issue_preventive_action;
    private List<HouseQmIssueReason> reason_list;

    @Data
    @NoArgsConstructor
    public class HouseQmIssueReason implements Serializable {
        private Integer id;
        private String value;
    }
}