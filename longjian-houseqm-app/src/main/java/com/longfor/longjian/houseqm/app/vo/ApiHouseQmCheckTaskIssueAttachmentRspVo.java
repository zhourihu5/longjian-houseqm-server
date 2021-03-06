package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

/**
 * Jiazm
 * 2018/12/20 17:50
 */
@Data
public class ApiHouseQmCheckTaskIssueAttachmentRspVo {
    private Integer id;// ID
    private Integer project_id;// 项目ID
    private Integer task_id;// 任务ID
    private String issue_uuid; // 问题Uuid
    private Integer user_id;// 用户id
    private Integer public_type;// 公开类型 10=公有 20=私有
    private Integer attachment_type;// 附件类型 10=音频
    private String md5;// 附件md5
    private Integer status;// 状态 1=可用 2=删除
    private Integer update_at;// 更新时间
    private Integer delete_at;// 删除时间(0表示未删除)
}
