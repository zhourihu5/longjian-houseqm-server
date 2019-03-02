package com.longfor.longjian.houseqm.consts;

/**
 * Created by houyan on 2018/12/17.
 */
public enum HouseQmCheckTaskIssueEnum {
    Record(99, "普通记录"), FindProblem(1, "发现问题"), Good(2, "优秀做法"), Difficult(3, "困难问题");

    private Integer id;
    private String value;

    HouseQmCheckTaskIssueEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
