package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/16.
 */
public enum IssueFieldAliasStatusEnum {
    Yes(1, "能"),
    No(0, "不能");
    private Integer id;
    private String value;

    IssueFieldAliasStatusEnum(Integer id, String value) {
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
