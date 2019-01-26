package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/23.
 */
@Data
@NoArgsConstructor
public class UnmarshReportIssueRequestBody implements Serializable {
    private List<String> log_uuids;
    private List<String>issue_uuids;
    private List<ApiHouseQmCheckTaskIssueLogInfo>issue_logs;
}
