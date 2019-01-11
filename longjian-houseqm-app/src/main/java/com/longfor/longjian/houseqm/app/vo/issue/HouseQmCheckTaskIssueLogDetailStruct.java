package com.longfor.longjian.houseqm.app.vo.issue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.issue
 * @ClassName: HouseQmCheckTaskIssueLogDetailStruct
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 16:03
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueLogDetailStruct implements Serializable {

    private int planEndOn;
    private int endOn;
    private int repairerId;
    private String repairerFollowerIds;
    private int condition;
    private int areaId;
    private int posX;
    private int posY;
    private int typ;
    private String title;
    private String checkItemKey;
    private int categoryCls;
    private String categoryKey;
    private String drawingMD5;

    private String removeMemoAudioMd5List;// 要移除的备忘录录音MD5列表，半角逗号（“,”）分隔
    private int issueReason; // 产生问题的原因，需要检索project_setting表此project的PROJ_ISSUE_REASON_LIST记录
    private String issueReasonDetail;// 产生问题原因的文字描述
    private String issueSuggest;// 措施建议
    private String potentialRisk;// 潜在风险
    private String preventiveActionDetail;// 预防措施


}
