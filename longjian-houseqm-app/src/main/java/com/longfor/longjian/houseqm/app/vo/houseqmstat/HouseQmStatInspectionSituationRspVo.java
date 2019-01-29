package com.longfor.longjian.houseqm.app.vo.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstat
 * @ClassName: HouseQmStatInspectionSituationRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 10:47
 */
@Data
@NoArgsConstructor
public class HouseQmStatInspectionSituationRspVo implements Serializable {

    //AreaId               int      `json:"area_id" zpf_reqd:"true" zpf_name:"area_id"`                               // 区域ID
    //	AreaName             string   `json:"area_name" zpf_reqd:"true" zpf_name:"area_name"`                           // 区域名
    //	AreaPath             []string `json:"area_path" zpf_name:"area_path"`                                           // 区域路径名(包括自己)
    //	IssueApprovededCount int      `json:"issue_approveded_count" zpf_reqd:"true" zpf_name:"issue_approveded_count"` // 消项问题数
    //	IssueCount           int      `json:"issue_count" zpf_reqd:"true" zpf_name:"issue_count"`                       // 问题数
    //	IssueRepairedCount   int      `json:"issue_repaired_count" zpf_reqd:"true" zpf_name:"issue_repaired_count"`     // 整改问题数
    //	Status               int      `json:"status" zpf_reqd:"true" zpf_name:"status"`                                 // 户状态
    //	StatusName           string   `json:"status_name" zpf_reqd:"true" zpf_name:"status_name"`                       // 户状态-文字
    //	TaskId               int      `json:"task_id" zpf_reqd:"true" zpf_name:"task_id"`                               // 任务ID
    private Integer area_id;
    private String area_name;
    private List<String> area_path;
    private Integer issue_approveded_count;
    private Integer issue_count;
    private Integer issue_repaired_count;
    private Integer status;
    private String status_name;
    private Integer task_id;

}
