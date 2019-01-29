package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: StatisticFormInspectionIssueStatusEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/29 15:58
 */
public enum StatisticFormInspectionIssueStatusEnum {
    //All       niuhe.IntConstItem `const:"0,全部"`
    //	NoProblem niuhe.IntConstItem `const:"1,没有问题"`
    //	HasIssue  niuhe.IntConstItem `const:"2,存在问题"`
    All(0,"全部"),
    NoProblem(1,"没有问题"),
    HasIssue(2,"存在问题"),
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

    StatisticFormInspectionIssueStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }}