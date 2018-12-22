package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dongshun on 2018/12/20.
 */
@Data
@NoArgsConstructor
public class HouseqmStatisticTaskCheckitemStatRspMsgVo implements Serializable {
    List<ApiHouseQmCheckItemIssueStat> items;
    private  Integer DateType;


    @Data
    @NoArgsConstructor
    public class ApiHouseQmCheckItemIssueStat implements  Serializable{
     private  String  Key;// 检查项key
        private  String  Name;   // 检查项名称
        private String FatherKey;// 检查项父级key
        private Integer IssueCount;// 问题数
    }
}
