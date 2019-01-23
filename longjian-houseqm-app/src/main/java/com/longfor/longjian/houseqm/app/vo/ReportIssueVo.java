package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/23.
 */
@Data
@NoArgsConstructor
public class ReportIssueVo {
    List<ApiHouseQmCheckTaskReportMsg> dropped;
    @Data
    @NoArgsConstructor
    public class ApiHouseQmCheckTaskReportMsg {
        private String uuid = "";
        private Integer reason_type = 0;
        private String reason = "";
    }

}
