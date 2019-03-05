package com.longfor.longjian.houseqm.utils;

import tk.mybatis.mapper.entity.Example;

public class ExampleUtil {
    private ExampleUtil(){

    }

    /**
     * 添加非空判断条件deleteAt != null and deleteAt != '0001-01-01 00:00:00'
     *
     * @param example
     * @return
     */
    public static Example addDeleteAtJudge(Example example) {
        Example.Criteria unscoped = example.createCriteria().andIsNull("deleteAt").orEqualTo("deleteAt", "0001-01-01 00:00:00");
        example.and(unscoped);
        return example;
    }
}
