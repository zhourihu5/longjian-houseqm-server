package com.longfor.longjian.houseqm.consts;

/**
 * Created by houyan on 2018/12/17.
 */
public enum HouseQmCheckTaskIssueStatusEnum {

    NoProblem(10,"无问题"),NoteNoAssign(20,"待分配"),AssignNoReform(30,"待整改"),
    Repairing(40,"整改中"),ReformNoCheck(50,"待销项"),CheckYes(60,"已销项"),
    Cancel(70,"已取消");

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

    HouseQmCheckTaskIssueStatusEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
