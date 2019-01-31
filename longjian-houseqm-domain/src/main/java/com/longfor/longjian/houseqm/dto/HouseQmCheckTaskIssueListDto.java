package com.longfor.longjian.houseqm.dto;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.dto
 * @ClassName: HouseQmCheckTaskIssueListVo
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/18 18:14
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueListDto  {
    private List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues;
    private Integer total;
}
