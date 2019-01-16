package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: HouseQmCheckTaskIssueCheckStatusEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 19:08
 */
public enum HouseQmCheckTaskIssueCheckStatusEnum {

    //CheckYes niuhe.IntConstItem `const:"1,通过"`
    //	CheckNo  niuhe.IntConstItem `const:"2,不通过"`
    CheckYes(1,"通过"),
    CheckNo(2,"不通过"),
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

    HouseQmCheckTaskIssueCheckStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }}
