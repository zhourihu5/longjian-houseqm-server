package com.longfor.longjian.houseqm.app.vo;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Houyan
 * @date 2018/12/22 0022 14:39
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueListVo implements Serializable {

    private List<HouseQmCheckTaskIssue> houseQmCheckTaskIssues;
    private Integer total;

}
