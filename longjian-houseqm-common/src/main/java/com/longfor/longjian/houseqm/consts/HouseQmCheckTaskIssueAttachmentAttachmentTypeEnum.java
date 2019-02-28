package com.longfor.longjian.houseqm.consts;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.consts
 * @ClassName: HouseQmCheckTaskIssueAttachmentAttachmentTypeEnum
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/12 13:40
 */
public enum HouseQmCheckTaskIssueAttachmentAttachmentTypeEnum {

    //Audio niuhe.IntConstItem `const:"10,音频"`
    Audio(10,"音频"),
    ;
    private Integer id;
    private String value;

    public Integer getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    HouseQmCheckTaskIssueAttachmentAttachmentTypeEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }}
