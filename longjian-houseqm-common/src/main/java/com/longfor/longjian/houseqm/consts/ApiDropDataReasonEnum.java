package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: ApiDropDataReasonEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 17:01
 */
public enum ApiDropDataReasonEnum {
    HouseQmTaskRemoved("工程任务已删除",102),
    Other("未知错误",999),

    ;
    private String name;
    private int value;

    ApiDropDataReasonEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }}
