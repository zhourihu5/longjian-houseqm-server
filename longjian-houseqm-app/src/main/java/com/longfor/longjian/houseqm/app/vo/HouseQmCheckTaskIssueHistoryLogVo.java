package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/9.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueHistoryLogVo {
    private Integer user_id;
    private String user_name;
    private Integer create_at;
    private List<HouseQmCheckTaskIssueHistoryLogItem> items;
    @Data
    @NoArgsConstructor
    public  class HouseQmCheckTaskIssueHistoryLogItem {
        private Integer   target_user_id =0;
        private String target_user_name="";
        private Integer log_type;
        private String data="";
    }


}
