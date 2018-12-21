package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

/**
 * Jiazm
 * 2018/12/20 15:38
 */
@Data
public class ApiHouseQmCheckTaskIssueMemberRspVo {
    private Integer id;  // ID
    private Integer userId;// 用户id
    private Integer roleType;// 角色类型  10=检查人(保留) 20=整改负责人 30=整改跟进人
    private Integer taskId;// 任务ID
    private String issueUuid;// 问题Uuid
    private Integer updateAt;// 更新时间
    private Integer deleteAt;// 删除时间(0表示未删除)
}
