package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2018/12/29.
 */

public enum RepossessionStatusEnum {
    None(0,"未检查"),
    OnlyCheck (1,"业主只看房／已查验"),
    Accept(2,"业主收楼"),
    RejectAccept(3,"业主拒绝收楼");

    private Integer id;
    private String value;

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    RepossessionStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
