package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/16.
 */
public enum IssueFieldDefaultListEnum {
    JCX(1, "检查项"),
    BCMS(2, "补充描述"),
    MSYY(3, "描述语音"),
    JCBW(4, "检查部位"),
    TZWZ(5, "图纸位置"),
    BWL(6, "备忘录"),
    ZGFZR(7, "整改负责人"),
    ZGCYR(8, "整改参与人"),
    ZGQX(9, "整改期限"),
    YZCD(10, "严重程度");


    private Integer id;
    private String value;

    IssueFieldDefaultListEnum(Integer id, String value) {
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
