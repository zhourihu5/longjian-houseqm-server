package com.longfor.longjian.houseqm.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.dto
 * @ClassName: RepaireIssueStatusStatDto
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 15:11
 */
@Data
@NoArgsConstructor
public class RepaireIssueStatusStatDto {

    //AssignedCount   int
    //	RepairedCount   int
    //	ApprovededCount int
    //	UserId          int
    //	RealName        string

    private Integer assigned_count;
    private Integer repaired_count;
    private Integer approveded_count;
    private Integer user_id;
    private String real_name;
}
