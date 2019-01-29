package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: StatisticFormRepossessionStatusEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 15:12
 */
public enum StatisticFormRepossessionStatusEnum {
    //All          niuhe.IntConstItem `const:"-1,全部"`
    //	None         niuhe.IntConstItem `const:"0,未检查"`
    //	OnlyCheck    niuhe.IntConstItem `const:"1,业主只看房／已查验"`
    //	Accept       niuhe.IntConstItem `const:"2,业主收楼"`
    //	RejectAccept niuhe.IntConstItem `const:"3,业主拒绝收楼"`
    All(-1,"全部"),
    None(0,"未检查"),
    OnlyCheck(1,"业主只看房／已查验"),
    Accept(2,"业主收楼"),
    RejectAccept(3,"业主拒绝收楼"),
    ;

    private Integer id;
    private String value;

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

    StatisticFormRepossessionStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }}
