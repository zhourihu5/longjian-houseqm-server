package com.longfor.longjian.houseqm.app.vo.export;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.export
 * @ClassName: ProjectIssueInfo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/14 10:28
 */
@Data
@NoArgsConstructor
public class ProjectIssueInfo implements Serializable {
    //Name          string
    //	IssueDesc     string
    //	CheckItemName string
    //	Records       string
    //	Repaired      string
    //	Status        string

    private String name;
    private String issueDesc;
    private String checkItemName;
    private String records;
    private String repaired;
    private String status;
}
