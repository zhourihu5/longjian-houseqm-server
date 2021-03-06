package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: HouseQmCheckTaskIssueAttachmentStatusEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 13:41
 */
public enum HouseQmCheckTaskIssueAttachmentStatusEnum {
    //Enable  niuhe.IntConstItem `const:"1,可用"`
    //	Deleted niuhe.IntConstItem `const:"2,已删除"`
    Enable(1, "可用"),
    Deleted(2, "已删除"),
    ;
    private Integer id;
    private String value;

HouseQmCheckTaskIssueAttachmentStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

        public String getValue() {
        return value;
    }}
