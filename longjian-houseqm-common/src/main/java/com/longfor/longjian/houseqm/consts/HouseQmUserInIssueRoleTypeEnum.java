package com.longfor.longjian.houseqm.consts;

/**
 * Jiazm
 * 2018/12/19 15:58
 */
public enum HouseQmUserInIssueRoleTypeEnum {
    Checker(10, "检查人"), Repairer(20, "整改负责人"), RepairerFollower(30, "整改跟进人");

    private Integer id;
    private String value;

    HouseQmUserInIssueRoleTypeEnum(Integer id, String value) {
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
