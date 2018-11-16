package com.longfor.longjian.houseqm.consts;

/**
 * Created by lipeishuai on 2018/11/11.
 */
public enum GroupPaymentEnum {

    TRIAL(10, "未付费"),PURCHASED(20, "已付费");

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

    GroupPaymentEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
