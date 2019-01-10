package com.longfor.longjian.houseqm.app.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: longjian-houseqm-server
 * @Package: com.longfor.longjian.houseqm.app.vo
 * @ClassName: HouseQmHouseQmStatCompleteDailyRsp
 * @Description: java类作用描述
 * @Author: hy
 * @CreateDate: 2019/1/8 20:25
 */
@Data
@NoArgsConstructor
public class HouseQmHouseQmStatCompleteDailyRspVo implements Serializable {
    private String date;// 日期
    private Integer count;// 户数
}
