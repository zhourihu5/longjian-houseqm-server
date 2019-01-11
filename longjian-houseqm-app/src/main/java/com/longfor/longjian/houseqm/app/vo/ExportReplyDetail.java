package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/10.
 */
@Data
@NoArgsConstructor
public class ExportReplyDetail {
   private String task_name="";
    private String check_item_name="";
    private List<ExportIssueDetail>issue_detail;
    @Data
    @NoArgsConstructor
    public  class  ExportIssueDetail{
        private Integer   issue_id =0;
        private String    ques_content="";
        private String   answ_content="";
        private String    answ_attachment_path ;
    }
}
