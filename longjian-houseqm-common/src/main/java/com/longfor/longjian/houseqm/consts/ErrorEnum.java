package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: ErrorEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/9 19:25
 */
public enum ErrorEnum {


    DB_ITEM_UNFOUND(100000,"数据不存在"),
    DB_ITEM_NOT_UPDATE(100001,"未更新任何数据"),
    ;
    private Integer code;
    private String message;

    private ErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
