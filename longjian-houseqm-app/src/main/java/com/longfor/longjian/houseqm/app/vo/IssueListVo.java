package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lipeishuai
 * @date 2018/11/17 15:54
 */
@Data
@NoArgsConstructor
public class IssueListVo {

    private int id;
    private int project_id;
    private int task_id;
    private String uuid;
    private int sender_id;
    private int plan_end_on;
    private int end_on;
    private int area_id;
    private String area_path_and_id;
    private int category_cls;
    private String category_key;
    private String category_path_and_key;
    private String category_name;
    private String check_item_key;
    private String check_item_path_and_key;
    private String check_item_name;
    private String drawing_md5;
    private int pos_x;
    private int pos_y;
    private String title;
    private int typ;
    private String content;
    private int condition;
    private int status;
    private String attachment_md5_list;
    private int client_create_at;
    private int last_assigner;
    private int last_assigner_at;
    private int last_repairer;
    private int last_repairer_at;
    private int destroy_user;
    private int destroy_at;
    private int delete_user;
    private int delete_time;
    private DetailVo detail;
    private String [] pictures;
    private String item;
    private String area_path_name;
    private String check_item_name_path;
    private String last_repairer_name;

    @Data
    @NoArgsConstructor
    public class DetailVo{
       private int issue_reason;
       private String issue_reason_detail;
       private String  issue_suggest;
       private String potential_risk;
       private String preventive_action_detail;
    }

}
