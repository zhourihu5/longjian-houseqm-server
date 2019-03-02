package com.longfor.longjian.houseqm.consts;

/**
 * @author Houyan
 * @date 2018/12/22 0022 16:58
 */
public enum HouseQmIssuePlanStatusEnum {
    OnTimeFinish(1, "按期完成"), UnOnTimeNotFinish(2, "未按期未完成"), NoSettingTime(3, "未设置期限"), OverTimeFinish(4, "超期完成"), OverTimeNotFinish(5, "超期未完成");
    private String value;
    private Integer id;

    HouseQmIssuePlanStatusEnum(Integer id, String value) {
        this.value = value;
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public Integer getId() {
        return id;
    }
}
