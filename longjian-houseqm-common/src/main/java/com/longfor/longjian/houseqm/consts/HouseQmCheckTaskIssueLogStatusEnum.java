package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: HouseQmCheckTaskIssueLogStatusEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/11 19:48
 */
public enum HouseQmCheckTaskIssueLogStatusEnum {

    //NoProblem       niuhe.IntConstItem `const:"10,无问题"`
    //	NoteNoAssign    niuhe.IntConstItem `const:"20,已记录未分配"`
    //	AssignNoReform  niuhe.IntConstItem `const:"30,已分配未整改"`
    //	Repairing       niuhe.IntConstItem `const:"40,整改中"`
    //	ReformNoCheck   niuhe.IntConstItem `const:"50,已整改未验收"`
    //	CheckYes        niuhe.IntConstItem `const:"60,已验收"`
    //	Cancel          niuhe.IntConstItem `const:"70,已取消"`
    //	EditBaseInfo    niuhe.IntConstItem `const:"990,修改基础信息"`
    //	UpdateIssueInfo niuhe.IntConstItem `const:"991,后台更新基础信息"`
    NoProblem(10, "无问题"),
    NoteNoAssign(20, "已记录未分配"),
    AssignNoReform(30, "已分配未整改"),
    Repairing(40, "整改中"),
    ReformNoCheck(50, "已整改未验收"),
    CheckYes(60, "已验收"),
    Cancel(70, "已取消"),
    EditBaseInfo(990, "修改基础信息"),
    UpdateIssueInfo(991, "后台更新基础信息"),
    ;
    private Integer id;
    private String value;

HouseQmCheckTaskIssueLogStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

        public String getValue() {
        return value;
    }}
