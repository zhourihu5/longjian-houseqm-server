package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lipeishuai
 * @date 2018/11/19 18:29
 */
@Data
@NoArgsConstructor
public class TaskVo implements Serializable {

    private String area_ids;
    private String  area_types;
    private int category_cls;
    private int checker_approve_permission;
    private int  create_at;
    private int  delete_at;
    private String issue_default_desc;
    private int  issue_desc_status;
    private String name;
    private int  plan_begin_on;
    private int  plan_end_on;
    private int  project_id;
    private int repaired_picture_status;
    private int repairer_follower_permission;
    private int repairer_refund_permission   = 0;
    private String root_category_key;
    private int status;
    private int task_id;
    private int update_at;
    private String extra_ops;
}