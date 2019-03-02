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
public class ApiHouseQmCheckTaskIssueLogInfo implements Serializable {
    private String uuid = "";
    private Integer task_id = 0;
    private String issue_uuid = "";
    private Integer sender_id = 0;
    private String desc = "";
    private Integer status = 0;
    private String attachment_md5_list = "";
    private String audio_md5_list = "";
    private String memo_audio_md5_list = "";
    private Integer client_create_at = 0;
    private List<ApiHouseQmCheckTaskIssueLogDetailInfo> detail;

    @Data
    @NoArgsConstructor
    public class ApiHouseQmCheckTaskIssueLogDetailInfo implements Serializable {
        private Integer area_id = 0;
        private Integer pos_x = 0;
        private Integer pos_y = 0;
        private Integer typ = 0;
        private Integer plan_end_on = 0;
        private Integer end_on = 0;
        private Integer repairer_id = 0;
        private String repairer_follower_ids = "";
        private Integer condition = 0;
        private Integer category_cls = 0;
        private String category_key = "";
        private String drawing_md5 = "";
        private String check_item_key = "";
        private String remove_memo_audio_md5_list = "";
        private String title = "";
        private String check_item_md5 = "";
        private Integer issue_reason = 0;
        private String issue_reason_detail = "";
        private String issue_suggest = "";
        private String potential_risk = "";
        private String preventive_action_detail = "";
    }

}
