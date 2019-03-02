package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/24.
 */
public enum DropDataReasonEnum {
    NotFound(1, "未找到资源"),
    Exists(2, "资源已存在"),
    HouseQmIssueLogUuidExists(101, "日志信息已存在"),
    HouseQmTaskRemoved(102, "工程任务已删除"),
    Other(999, "未知错误");


    private Integer id;
    private String value;

    DropDataReasonEnum(Integer id, String value) {
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
