package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;

/**
 * Jiazm
 * 2018/12/20 15:10
 */
@Data
public class HouseQmCheckTaskIssueLogRspVo {
    private Integer id;
    private Integer projectId;
    private Integer taskId;
    private String uuid;
    private String issueUuid;
    private Integer senderId;
    private Integer status;
    private Integer clientCreateAt;
    private Integer createAt;
    private Integer updateAt;
    private Integer deleteAt;
    private String desc;
    private String attachmentMd5List;
    private String audioMd5List;
    private String memoAudioMd5List;
    private String detail;
}
