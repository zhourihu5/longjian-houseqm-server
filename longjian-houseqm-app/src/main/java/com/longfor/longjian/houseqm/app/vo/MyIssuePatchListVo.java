package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * @author lipeishuai
 * @date 2018/11/17 17:23
 */
@Data
@NoArgsConstructor
public class MyIssuePatchListVo implements Serializable{

    private List<LogVo> log_list;
    private List<AttachmentVo> attachment_list;

    @Data
    @NoArgsConstructor
    public class LogVo implements Serializable{
        private int id;
        private int project_id;
        private int task_id;
        private String uuid;
        private String issue_uuid;
        private int sender_id;
        private String desc;
        private int status;
        private String attachment_md5_list;
        private String audio_md5_list;
        private String memo_audio_md5_list;
        private int client_create_at;
        private LogDetailVo detail;
        private int update_at;
        private int delete_at;
    }

    @Data
    @NoArgsConstructor
    public class AttachmentVo implements Serializable{
        private int id;
        private int project_id;
        private int task_id;
        private String issue_uuid;
        private int user_id;
        private int public_type;
        private int attachment_type;
        private String md5;
        private int status;
        private int update_at;
        private int delete_at;
    }

    @Data
    @NoArgsConstructor
    public class LogDetailVo implements Serializable{
        private String title;
        private int area_id;
        private int pos_x;
        private int pos_y;
        private int typ;
        private int plan_end_on;
        private int end_on;
        private int repairer_id;
        private String repairer_follower_ids;
        private int condition;
        private int category_cls;
        private String category_key;
        private String check_item_key;
        private int issue_reason;
        private String issue_reason_detail;
        private String issue_suggest;
        private String potential_risk;
        private String preventive_action_detail;
    }
}
