package com.longfor.longjian.houseqm.consts;

/**
 * Created by lipeishuai on 2018/11/11.
 */
public enum CompanyLevelEnum {

    Group(1, "集团"),Team(2, "公司"),SubTeam(3, "子公司");

    private Integer id;
    private String value;

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    CompanyLevelEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }


}
