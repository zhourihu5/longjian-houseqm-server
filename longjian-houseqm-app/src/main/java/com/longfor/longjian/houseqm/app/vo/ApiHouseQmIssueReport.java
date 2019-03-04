package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2019/3/2.
 */
@Data
@NoArgsConstructor
public class ApiHouseQmIssueReport implements Serializable {
    private List<ApiHouseQmIssue> created_issues;
    private List<ApiHouseQmIssue> assigned_issues;
    private List<ApiHouseQmIssue> reformed_issues;
    private List<ApiHouseQmIssue> checked_issues;
}