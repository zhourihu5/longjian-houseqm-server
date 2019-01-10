package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmStatTaskDetailMemberCheckerRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/9 14:47
 */
@Data
@NoArgsConstructor
public class HouseQmStatTaskDetailMemberCheckerRspVo implements Serializable {

    //ApprovededCount int    `json:"approveded_count" zpf_reqd:"true" zpf_name:"approveded_count"` // 已消项问题数
    //	IssueCount      int    `json:"issue_count" zpf_reqd:"true" zpf_name:"issue_count"`           // 新增问题数
    //	RecordsCount    int    `json:"records_count" zpf_reqd:"true" zpf_name:"records_count"`       // 新增记录数
    //	RealName        string `json:"real_name" zpf_reqd:"true" zpf_name:"real_name"`               // 真名
    //	UserId          int    `json:"user_id" zpf_reqd:"true" zpf_name:"user_id"`                   // 用户id
    private Integer approveded_count;
    private Integer issue_count;
    private Integer records_count;
    private String real_name;
    private Integer user_id;

}
