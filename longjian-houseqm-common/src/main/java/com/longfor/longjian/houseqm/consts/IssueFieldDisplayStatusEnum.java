package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/16.
 */
public enum IssueFieldDisplayStatusEnum {
    Yes(1, "显示"),
    No (0, "不显示");
    private Integer id;
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    IssueFieldDisplayStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

}
