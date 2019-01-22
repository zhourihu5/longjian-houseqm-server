package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

/**
 * Jiazm
 * 2018/12/20 15:38
 */
@Data
public class ApiHouseQmCheckTaskIssueMemberRspVo {
    private Integer id;  // ID
    private Integer user_id;// 用户id
    private Integer role_type;// 角色类型  10=检查人(保留) 20=整改负责人 30=整改跟进人
    private Integer task_id;// 任务ID
    private String issue_uuid;// 问题Uuid
    private Integer update_at;// 更新时间
    private Integer delete_at;// 删除时间(0表示未删除)
}
