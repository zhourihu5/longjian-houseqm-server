package com.longfor.longjian.houseqm.consts;

import org.aspectj.weaver.Checker;

/**
 * Created by Dongshun on 2019/1/17.
 */
public enum HouseQmCheckTaskRoleTypeEnum {

    Checker(10,"检查人"),
    Repairer (20,"整改人"),
    MixChecker(40,"多职能人员");
    private Integer id;
    private String value;

    public Integer getId() {
        return id;
    }
    public String getValue() {
        return value;
    }

    HouseQmCheckTaskRoleTypeEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }


}
