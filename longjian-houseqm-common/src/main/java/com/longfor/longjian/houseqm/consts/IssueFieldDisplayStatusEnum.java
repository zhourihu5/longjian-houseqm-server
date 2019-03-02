package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/16.
 */
public enum IssueFieldDisplayStatusEnum {
    Yes(1, "显示"),
    No(0, "不显示");
    private Integer id;
    private String value;

    IssueFieldDisplayStatusEnum(Integer id, String value) {
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
