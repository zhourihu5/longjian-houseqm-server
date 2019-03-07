package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: StatisticFormInspectionStatusEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 16:37
 */
public enum StatisticFormInspectionStatusEnum {
    //All       niuhe.IntConstItem `const:"0,全部"`
    //	UnChecked niuhe.IntConstItem `const:"1,未检查"`
    //	Checked   niuhe.IntConstItem `const:"2,已检查"`
    All(0, "全部"),
    UnChecked(1, "未检查"),
    Checked(2, "已检查"),
    ;
    private Integer id;
    private String value;

    StatisticFormInspectionStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public static String getName(Integer id) {
        StatisticFormInspectionStatusEnum[] values = StatisticFormInspectionStatusEnum.values();
        for (StatisticFormInspectionStatusEnum value : values) {
            if (value.getId().equals(id)) return value.getValue();
        }
        return StatisticFormInspectionStatusEnum.getName(1);
    }

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
