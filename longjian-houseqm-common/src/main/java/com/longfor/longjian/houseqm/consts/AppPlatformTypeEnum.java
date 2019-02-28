package com.longfor.longjian.houseqm.consts;

/**
 * Created by Dongshun on 2019/1/12.
 */
public enum AppPlatformTypeEnum {
    PUSH_PLATFORM_UMENG_ANDROID (1, "友盟android端"),
    PUSH_PLATFORM_UMENG_IOS(2, "友盟ios端"),
    PUSH_PLATFORM_XIAOMI(3, "小米android端"),
    PUSH_PLATFORM_HUAWEI(4, "华为android端");
    private Integer id;
    private String value;

    public Integer getId() {
        return id;
    }
    public String getValue() {
        return value;
    }
    AppPlatformTypeEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

}
