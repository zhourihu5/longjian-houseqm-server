package com.longfor.longjian.houseqm.app.vo;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/10.
 */
@Data
@NoArgsConstructor
public class ExportReplyDetail implements Serializable {
    private String task_name = "";
    private String check_item_name = "";
    private List<ExportIssueDetail> issue_detail= Lists.newArrayList();

    @Data
    @NoArgsConstructor
    public class ExportIssueDetail implements Serializable {
        private Integer issue_id = 0;
        private String ques_content = "";
        private String answ_content = "";
        private List<String> answ_attachment_path;
    }
}
