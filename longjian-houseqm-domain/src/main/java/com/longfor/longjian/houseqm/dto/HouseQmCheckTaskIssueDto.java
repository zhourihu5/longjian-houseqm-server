package com.longfor.longjian.houseqm.dto;

import com.longfor.longjian.houseqm.po.zj2db.HouseQmCheckTaskIssue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Dongshun on 2019/1/19.
 */
@Data
@NoArgsConstructor
public class HouseQmCheckTaskIssueDto {
    private Integer total;
    private List<HouseQmCheckTaskIssue>items;
}
