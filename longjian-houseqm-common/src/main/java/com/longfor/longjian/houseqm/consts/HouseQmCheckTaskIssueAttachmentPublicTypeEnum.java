package com.longfor.longjian.houseqm.consts;

/**
 * Jiazm
 * 2018/12/20 17:01
 */
public enum  HouseQmCheckTaskIssueAttachmentPublicTypeEnum {
    Public(10, "公有"),Private(20, "私有");

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

    HouseQmCheckTaskIssueAttachmentPublicTypeEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
