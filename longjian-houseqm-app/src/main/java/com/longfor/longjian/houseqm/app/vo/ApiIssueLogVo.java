package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/1/22.
 */
@Data
@NoArgsConstructor
public class ApiIssueLogVo implements Serializable {
    private  ApiIssueLogInfoIssueRsp issue;
    private List<ApiIssueLogListRsp>issue_log_list;
    @Data
    @NoArgsConstructor
    public class ApiIssueLogInfoIssueRsp implements Serializable{
      private Integer  id;
        private Integer project_id;
        private Integer task_id;
        private String uuid;
        private String sender_name;
        private Integer  plan_end_on;
        private Integer  end_on;
        private Integer  area_id;
        private String area_path_and_id;
        private List<Object> area_path_name;
        private Integer  category_cls;
        private String category_key;
        private String category_path_and_key;
        private List<String> category_path_name;
        private String  check_item_key;
        private String check_item_path_and_key;
        private List<String> check_item_path_name;
        private String drawing_url;
        private String drawing_md5;
        private Integer  pos_x;
        private Integer  pos_y;
        private String title;
        private Integer  typ;
        private String content;
        private Integer  condition;
        private Integer status;/*整改状态：当前issue状态0没有问题1已记录未分配2已分配未整改3已整改未审核4已审核（消项）5已取消6=整改中（介乎于2和3之间）')*/
        private List<String> attachment_url_list;
        private List<String> audio_url_list;
        private String attachment_md5_list;
        private String audio_md5_list;
        private String repairer_name;
        private List<String> repairer_follower_names;
        private Integer client_create_at;
        private String last_assigner_name;
        private Integer last_assigner_at;
        private String last_repairer_name;
        private Integer last_repairer_at;
        private String destroy_user_name;
        private Integer destroy_at;
        private String delete_user_name;
        private Integer delete_time;
        private ApiHouseQmCheckTaskIssueDetailVo detail;
        private Integer update_at;
        private Integer delete_at;
    }
    @Data
    @NoArgsConstructor
    public class ApiIssueLogListRsp implements Serializable{
    private Integer  id;
      private Integer   project_id;
      private Integer  task_id;
      private String   uuid;
      private String  issue_uuid;
      private String  sender_name;
      private String  desc;
      private Integer   status;
      private List<String>  attachment_url_list;
      private List<String>  audio_url_list;
      private List<String>  memo_audio_url_list;
      private String   attachment_md5_list;
      private String   audio_md5_list;
      private String  memo_audio_md5_list;
      private Integer   client_create_at;
      private Integer   update_at;
    }
}
