package com.longfor.longjian.houseqm.app.vo.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstat
 * @ClassName: HouseQmStatCategorySituationRspVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 11:19
 */
@Data
@NoArgsConstructor
public class HouseQmStatCategorySituationRspVo implements Serializable {
    //Key        string `json:"key" zpf_reqd:"true" zpf_name:"key"`                 // key
    //	ParentKey  string `json:"parent_key" zpf_reqd:"true" zpf_name:"parent_key"`   // 上层key
    //	IssueCount int    `json:"issue_count" zpf_reqd:"true" zpf_name:"issue_count"` // 问题数
    //	Name       string `json:"name" zpf_reqd:"true" zpf_name:"name"`               // 检查项名称
    private String key;
    private String parent_key;
    private Integer issue_count;
    private String name;

}
