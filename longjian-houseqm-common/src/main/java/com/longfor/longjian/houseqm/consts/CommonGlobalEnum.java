package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/10.
 */
public enum CommonGlobalEnum {
    RES_SUCCESS(0, "成功"),
    RES_FAILED(-1, "失败"),
    RES_ERROR(-99, "接口调用失败"),
    EXPORT_BASE_DIR("/data/zhijian/writable", "问题导出文件绝对路径"),
    EXPORT_BASE_URI("writable", "问题导出文件URI");
    private Object id;
    private String value;

    CommonGlobalEnum(Object id, String value) {
        this.id = id;
        this.value = value;
    }

    public Object getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
