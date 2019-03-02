package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/16.
 */
public enum IssueFieldRequiredStatusEnum {
    Yes(1, "必填"),
    No(0, "非必填");
    private Integer id;
    private String value;

    IssueFieldRequiredStatusEnum(Integer id, String value) {
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
