package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2018/12/18.
 */

public enum TimeStauEnum {
    TIME_RANGE_THIS_WEEK(0, "本周"),
    TIME_RANGE_THIS_MONTH(1, "本月"),
    TIME_RANGE_LAST_WEEK(2, "上周"),
    TIME_RANGE_LAST_MONTH(3, "上月");


    private Integer id;
    private String value;

    TimeStauEnum(Integer id, String value) {
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
