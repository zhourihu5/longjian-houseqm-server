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
    private  int date_type;


    @Data
    @NoArgsConstructor
    public class ApiHouseQmCheckItemIssueStat implements  Serializable{
     private  String  key;// 检查项key
        private  String  name;   // 检查项名称
        private String father_key;// 检查项父级key
        private Integer issue_count;// 问题数
    }
}
