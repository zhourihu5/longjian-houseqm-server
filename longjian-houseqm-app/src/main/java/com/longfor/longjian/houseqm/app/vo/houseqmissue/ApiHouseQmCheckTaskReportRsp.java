package com.longfor.longjian.houseqm.app.vo.houseqmissue;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmissue
 * @ClassName: ApiHouseQmCheckTaskReportRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 15:41
 */
@Data
@NoArgsConstructor
public class ApiHouseQmCheckTaskReportRsp implements Serializable {
    //Uuid       string `json:"uuid" zpf_reqd:"true" zpf_name:"uuid"`               // 抛弃的uuid
    //	ReasonType int    `json:"reason_type" zpf_reqd:"true" zpf_name:"reason_type"` // 抛弃原因类型
    //	Reason     string `json:"reason" zpf_reqd:"true" zpf_name:"reason"`           // 抛弃原因说明
    private String uuid;
    private Integer reason_type;
    private String reason;
}
