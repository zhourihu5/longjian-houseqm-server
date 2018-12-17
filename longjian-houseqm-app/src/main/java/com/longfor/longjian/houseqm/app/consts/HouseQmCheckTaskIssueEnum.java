package com.longfor.longjian.houseqm.app.consts;

public enum HouseQmCheckTaskIssueEnum {
    Record(99,"普通记录"),FindProblem(1,"发现问题"),Good(2,"优秀做法"),Difficult(3,"困难问题");

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

    HouseQmCheckTaskIssueEnum(Integer id, String value) {
        this.id = id;
        this.value = value;
    }
}
