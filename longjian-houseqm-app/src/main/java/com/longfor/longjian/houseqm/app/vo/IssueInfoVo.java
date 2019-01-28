package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.houseqm.po.HouseQmCheckTaskIssueAttachment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Dongshun on 2019/1/13.
 */
@Data
@NoArgsConstructor
public class IssueInfoVo implements Serializable{
   private Integer task_id;
   private String task_name;
    private Integer  create_at;
    private List category_path_names;
    private List  check_item_path_names;
    private List area_path_names;
    private Integer  issue_type;
    private Integer  pos_x;
    private Integer  pos_y;
    private String  drawing_md5;
    private List  attachment_md5_list;
    private Integer  close_status;
    private List   repair_follower_ids;
    private Integer  repairer_id;
    private String sender_name;
    private Integer  plan_end_on;
    private String  content;
    private List<HouseQmCheckTaskIssueDetailEditLog>edit_logs;
    private Map<String,Object> detail;
    private List<HouseQmCheckTaskIssueAttachmentVo> audios;
    private List<HouseQmCheckTaskIssueAttachmentVo>my_audio;
    private Integer   status;

    @Data
    @NoArgsConstructor
    public class HouseQmCheckTaskIssueDetail implements Serializable{
        private Integer issue_reason; // 产生问题的原因，需要检索project_setting表此project的PROJ_ISSUE_REASON_LIST记录
        private String  issue_reason_detail; // 产生问题原因的文字描述
        private  String issue_suggest;     // 措施建议
        private String  potential_risk; // 潜在风险
        private String preventive_action_detail; //预防措施
    }
    @Data
    @NoArgsConstructor
    public class HouseQmCheckTaskIssueDetailEditLog implements Serializable{
       private Integer  user_id;
        private  String  user_name;
        private Integer  create_at;
        private  String  content;
        private  List  attachment_md5_list;
    }
    @Data
    @NoArgsConstructor
    public class HouseQmCheckTaskIssueAttachmentVo implements Serializable{
        private String md5;
        private  Integer create_at;
        private  Integer typ  ;
    }


}
