package com.longfor.longjian.houseqm.app.vo.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issue
 * @ClassName: HouseQmCheckTaskIssueLogVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 16:13
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueLogVo implements Serializable {
    private Integer id;
    private Integer projectId;
    private Integer taskId;
    private String uuid;
    private String issueUuid;
    private Integer senderId;
    private Integer status;
    private Date clientCreateAt;
    private Date createAt;
    private Date updateAt;
    private Date deleteAt;
    private String desc;
    private String attachmentMd5List;
    private String audioMd5List;
    private String memoAudioMd5List;
    private HouseQmCheckTaskIssueLogDetailStruct detail;
}
