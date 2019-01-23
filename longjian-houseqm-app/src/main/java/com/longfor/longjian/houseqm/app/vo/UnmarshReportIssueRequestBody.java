package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/23.
 */
@Data
@NoArgsConstructor
public class UnmarshReportIssueRequestBody {
    private List<Object> log_uuids;
    private List<Object>issue_uuids;
    private List<Object>issue_logs;
}
