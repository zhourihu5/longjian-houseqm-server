package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wang on 2019/3/1.
 */
@Data
public class NoticeStatValue {

    private List<Integer> reformNoCheckIssueIds=new ArrayList<>();

    private  List<Integer> assignNoReformIssueIds=new ArrayList<>();

    public NoticeStatValue(List<Integer> reformNoCheckIssueIds, List<Integer> assignNoReformIssueIds) {
        this.reformNoCheckIssueIds = reformNoCheckIssueIds;
        this.assignNoReformIssueIds = assignNoReformIssueIds;
    }
}
