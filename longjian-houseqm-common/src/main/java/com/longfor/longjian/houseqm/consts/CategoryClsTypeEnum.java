package com.longfor.longjian.houseqm.consts;

/**
 *  // 任务类型的主类型
 * @author hy
 * @date 2018/12/22 0022
 */
public enum CategoryClsTypeEnum {

    Default(0,"V2默认检查项"),KeyProcedure(1,"关键工序"),Measure(102,"实测实量"),HouseQm(201,"移动验房"),BuildingQm(301,"工程管理"),
    //V2: 20 + v2
    FBFX(21,"分部分项"),YB(22,"样板点评"),RCJC(23,"日常检查"),YDJC(24,"月度检查"),JDJC(25,"季度检查"),RHYF(26,"入伙验房"),ZXJC(27,"专项检查"),FHYS(28,"分户验收");
    private Integer id;
    private String value;

    CategoryClsTypeEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
