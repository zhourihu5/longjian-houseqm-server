package com.longfor.longjian.houseqm.app.vo.task;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.task
 * @ClassName: CheckTaskIssueTypeStatInfo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/24 14:44
 */
@Data
@NoArgsConstructor
public class CheckTaskIssueTypeStatInfo implements Serializable {

    private Integer IssueCount;
    private Integer RecordCount;
    private Integer IssueRecordedCount;
    private Integer IssueAssignedCount;
    private Integer IssueRepairedCount;
    private Integer IssueApprovededCount;


}
