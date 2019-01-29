package com.longfor.longjian.houseqm.app.vo.houseqmstat;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo.houseqmstat
 * @ClassName: InspectionHouseStatusInfoVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 15:00
 */
@Data
@NoArgsConstructor
public class InspectionHouseStatusInfoVo implements Serializable {

    //TaskId             int
    //	AreaPathName       []string
    //	AreaId             int
    //	AreaName           string
    //	Status             int
    //	StatusName         string
    //	IssueCount         int
    //	IssueRepairedCount int
    //	IssueApprovedCount int
    private Integer taskId;
    private List<String> areaPathName;
    private Integer areaId;
    private String areaName;
    private Integer status;
    private String statusName;
    private Integer issueCount;
    private Integer issueRepairedCount;
    private Integer issueApprovededCount;


}
