package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2018/12/29.
 */

public enum RepossessionRepairStatusEnum {
    NoRepair(0, "返修状态－不需返修"),
    NeedRepair(1, "返修状态－需要返修未确认"),
    Confirmed(2, "返修状态－确认");
    private Integer id;
    private String value;

    RepossessionRepairStatusEnum(Integer id, String value) {
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
