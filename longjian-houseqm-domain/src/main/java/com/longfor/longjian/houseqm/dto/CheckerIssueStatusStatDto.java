package com.longfor.longjian.houseqm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.dto
 * @ClassName: CheckerIssueStatusStatDto
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/9 15:25
 */
@Data
@NoArgsConstructor
public class CheckerIssueStatusStatDto implements Serializable {

    //  IssuesCount     int
    //	RecordsCount    int
    //	ApprovededCount int
    //	UserId          int
    //	RealName        string

    private Integer issues_count;
    private Integer records_count;
    private Integer approveded_count;
    private Integer user_id;
    private String real_name;

}
