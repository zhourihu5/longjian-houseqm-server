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
        private Integer id;
        private Integer project_id;
        private Integer task_id;
        private String uuid;
        private String issue_uuid;
        private Integer sender_id;
        private String desc;
        private Integer status;
        private String attachment_md5_list;
        private String audio_md5_list;
        private String memo_audio_md5_list;
        private Integer client_create_at;
        private LogDetailVo detail;
        private Integer update_at;
        private Integer delete_at;
    }

    @Data
    @NoArgsConstructor
    public class AttachmentVo implements Serializable{
        private Integer id;
        private Integer project_id;
        private Integer task_id;
        private String issue_uuid;
        private Integer user_id;
        private Integer public_type;
        private Integer attachment_type;
        private String md5;
        private Integer status;
        private Integer update_at;
        private Integer delete_at;
    }

    @Data
    @NoArgsConstructor
    public class LogDetailVo implements Serializable{
        private String title;
        private Integer area_id;
        private Integer pos_x;
        private Integer pos_y;
        private Integer typ;
        private Integer plan_end_on;
        private Integer end_on;
        private Integer repairer_id;
        private String repairer_follower_ids;
        private Integer condition;
        private Integer category_cls;
        private String category_key;
        private String check_item_key;
        private Integer issue_reason;
        private String issue_reason_detail;
        private String issue_suggest;
        private String potential_risk;
        private String preventive_action_detail;
    }
}
