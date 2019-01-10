package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmStatTaskDetailMemberRepairerRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/10 14:57
 */
@Data
@NoArgsConstructor
public class HouseQmStatTaskDetailMemberRepairerRspVo implements Serializable {

    //ApprovededCount int    `json:"approveded_count" zpf_reqd:"true" zpf_name:"approveded_count"` // 成功整改问题数
    //	AssignedCount   int    `json:"assigned_count" zpf_reqd:"true" zpf_name:"assigned_count"`     // 被分配问题数
    //	RepairedCount   int    `json:"repaired_count" zpf_reqd:"true" zpf_name:"repaired_count"`     // 整改问题数
    //	RealName        string `json:"real_name" zpf_reqd:"true" zpf_name:"real_name"`               // 真名
    //	UserId          int    `json:"user_id" zpf_reqd:"true" zpf_name:"user_id"`                   // 用户id

    private Integer approveded_count;
    private Integer assigned_count;
    private Integer repaired_count;
    private String real_name;
    private Integer user_id;
}
