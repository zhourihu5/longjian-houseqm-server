package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

/**
 * Jiazm
 * 2018/12/20 11:16
 */
@Data
public class ApiHouseQmCheckTaskIssueRspVo {
    private Integer id;
    private Integer projectId;
    private Integer taskId;
    private String uuid;
    private Integer senderId;
    private Integer planEndOn;
    private Integer EndOn;
    private Integer areaId;
    private String areaPathAndId;
    private Integer categoryCls;
    private String categoryKey;
    private String categoryPathAndKey;
    private String checkItemKey;
    private String checkItemPathAndKey;
    private String drawingMd5;
    private Integer posX;
    private Integer posY;
    private String title;
    private Integer typ;
    private String content;
    private Integer condition;
    private Integer status;
    private String attachmentMd5List;
    private String audioMd5List;
    private Integer repairerId;
    private String repairerFollowerIds;
    private Integer clientCreateAt;
    private Integer lastAssigner;
    private Integer lastAssignerAt;
    private Integer lastRepairer;
    private Integer lastRepairerAt;
    private Integer destroyUser;
    private Integer destroyAt;
    private Integer deleteUser;
    private Integer deleteTime;
    private String detail;
    private Integer updateAt;
    private Integer deleteAt;
}
